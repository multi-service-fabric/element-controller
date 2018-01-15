/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.NodesStartupNotification;
import msf.ecmm.devctrl.DevctrlCommon;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.DhcpController;
import msf.ecmm.devctrl.SyslogController;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.NotifyNodeStartUpToFc;
import msf.ecmm.ope.control.ECMainState;
import msf.ecmm.ope.control.NodeAdditionState;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.NotifyNodeStartUp;

/**
 * Device Extention Notification.
 */
public class NodeAddedNotification extends Operation {

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_290402 = "290402";
  /** DHCP Termination Failed. */
  private static final String ERROR_CODE_290403 = "290403";
  /** Syslog Monitoring Termination Failed. */
  private static final String ERROR_CODE_290404 = "290404";

  /** Device Start-up Succeeded. */
  private static final String BOOT_RET_SUCCESS = RECV_OK_NOTIFICATION_STRING;
  /** Device Start-up Failed. */
  private static final String BOOT_RET_FAILED = RECV_NG_NOTIFICATION_STRING;
  /** Device Start-up Cancelled. */
  private static final String BOOT_RET_CANCEL = WAIT_NOTIFICATION_STRING;

  /** Device ID. */
  private String bootNodeId = "";

  /** Device Start-up Result. */
  private boolean bootStatus = false; 

  /** Extended Device Information. */
  private NodeAdditionThread nodeAdditionThread;

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public NodeAddedNotification(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.NodeAddedNotification);
  }

  @Override
  public boolean prepare() {

    logger.trace(CommonDefinitions.START);

    boolean result = false;

    boolean judge = false;
    synchronized (OperationControlManager.getInstance()) {

      judge = OperationControlManager.getInstance().judgeExecution(getOperationType());
      if (judge) {
        operationId = OperationControlManager.getInstance().startOperation(this);
        if (operationId == null) {
          judge = false;
        }
      }

      boolean reqNotifyFlag = false; 
      boolean reqDeleteFlag = true; 
      boolean reqUpdateNodeStateFlag = false; 

      do {

        if (!checkInData()) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
          reqNotifyFlag = false;
          reqDeleteFlag = false;
          reqUpdateNodeStateFlag = false;
          break;
        }

        if (getUriKeyMap().get(KEY_STATUS).equals(BOOT_RET_SUCCESS)) {
          bootStatus = true;
        } else {
          bootStatus = false;
        }

        if (judge == false && getEcMainState() != ECMainState.InService) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "NodeAddedNotification operation rejected."));
          reqNotifyFlag = false;
          reqDeleteFlag = false;
          reqUpdateNodeStateFlag = false;
          break;
        }

        waitNodeStateNotificationSending();

        try (DBAccessManager session = new DBAccessManager()) {

          Nodes bootNode = session.searchNodes(null, ((NotifyNodeStartUp) getInData()).getManagementIfAddress());
          if (bootNode == null) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
            reqNotifyFlag = false; 
            reqDeleteFlag = false; 
            reqUpdateNodeStateFlag = false; 
            break;
          }

          bootNodeId = bootNode.getNode_id();
          logger.debug(" boot nodeId=" + bootNodeId);
          reqNotifyFlag = true; 

          boolean clearFcData = true;
          nodeAdditionThread = OperationControlManager.getInstance().getNodeAdditionInfo();
          if (nodeAdditionThread == null) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [FC received data]"));
            reqNotifyFlag = false; 
            reqDeleteFlag = true;
            reqUpdateNodeStateFlag = true;
            clearFcData = false;
            break;
          }

          String nodeNotifyAddr = ((NotifyNodeStartUp) getInData()).getManagementIfAddress();
          String fcNotifyAddr = nodeAdditionThread.getAddNodeInfo().getCreateNode().getManagementInterface()
              .getAddress();
          if (!nodeNotifyAddr.equals(fcNotifyAddr)) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Unmatch data. [FC received data]"));
            reqNotifyFlag = false; 
            reqDeleteFlag = true;
            reqUpdateNodeStateFlag = true;
            clearFcData = false;
            break;
          }
          if (clearFcData == true) {
            OperationControlManager.getInstance().clearNodeAdditionInfo();
          }

          session.startTransaction();

          NodesStartupNotification nodeStartupNoticeDb = createNodeStartupInfo(bootNode);
          session.updateNodesStartupNotification(nodeStartupNoticeDb);

          session.commit();

          if (judge == true) {
            result = true;
          } else {
            reqNotifyFlag = true;
            reqDeleteFlag = true;
            reqUpdateNodeStateFlag = true;
          }

        } catch (DBAccessException dbae) {
          if (dbae.getCode() == DBAccessException.NO_UPDATE_TARGET) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [NodesStartupNotification]"),
                dbae);
            reqNotifyFlag = false; 
            reqDeleteFlag = false; 
            reqUpdateNodeStateFlag = true;
          } else {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
            if (bootNodeId.isEmpty()) {
              reqNotifyFlag = false;
              reqDeleteFlag = false;
              reqUpdateNodeStateFlag = false;
            } else {
              reqNotifyFlag = true;
              reqDeleteFlag = true; 
              reqUpdateNodeStateFlag = true; 
            }
          }
        }
      }
      while (false);

      if (result == false) {
        cleanUp(true, reqNotifyFlag, reqDeleteFlag, reqUpdateNodeStateFlag);
      }
    } 

    logger.trace(CommonDefinitions.END);
    return result;
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    boolean dhcpOkFlag = false;
    boolean syslogOkFlag = false;
    boolean reqNotifyFlag = true;
    boolean reqDeleteFlag = true;
    boolean reqUpdateNodeStateFlag = false; 
    boolean needCleanUpFlag = true; 

    try {

      DhcpController dhcpController = DhcpController.getInstance();
      dhcpController.stop(true);
      dhcpOkFlag = true;

      SyslogController syslogController = SyslogController.getInstance();
      syslogController.monitorStop(true); 
      syslogOkFlag = true;

      deleteNodesStartupNotification();
      reqDeleteFlag = false;

      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

      needCleanUpFlag = false;

    } catch (DevctrlException de) {
      if (dhcpOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "DHCP stop was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_290403);
      } else {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Watch syslog stop was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_290404);
      }
      reqUpdateNodeStateFlag = true;
      reqDeleteFlag = true;
    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_290402);
      reqDeleteFlag = false;
      reqUpdateNodeStateFlag = true;
    } finally {
      if (needCleanUpFlag == true) {
        cleanUp(!syslogOkFlag, reqNotifyFlag, reqDeleteFlag, reqUpdateNodeStateFlag);
      }
    }

    nodeAdditionThread.notifyNodeBoot(bootStatus);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    NotifyNodeStartUp notifyNodeStartUp = (NotifyNodeStartUp) getInData();

    if (!getUriKeyMap().containsKey(KEY_STATUS)) {
      checkResult = false;
    } else {
      String status = getUriKeyMap().get(KEY_STATUS);
      if (!status.equals("success") && !status.equals("failed")) {
        checkResult = false;
      }
    }
    if (checkResult) {
      try {
        notifyNodeStartUp.check(getOperationType());
      } catch (CheckDataException cde) {
        logger.warn("check error :", cde);
        checkResult = false;
      }
    }

    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

  /**
   * Device Start-up Notification Information.
   *
   * @param node
   *          device information
   * @return device start-up notification information
   */
  private NodesStartupNotification createNodeStartupInfo(Nodes node) {
    logger.trace(CommonDefinitions.START);
    logger.debug(node);
    NodesStartupNotification nodeStartupNoticeDb = new NodesStartupNotification();
    nodeStartupNoticeDb.setNode_id(node.getNode_id());
    int notification = CommonDefinitions.RECV_NG_NOTIFICATION;
    if (bootStatus) {
      notification = CommonDefinitions.RECV_OK_NOTIFICATION;
    }
    nodeStartupNoticeDb.setNotification_reception_status(notification);
    logger.trace(CommonDefinitions.END);
    return nodeStartupNoticeDb;
  }

  /**
   * REST request to FC (Device Extention Completion Notification).
   *
   * @param executeResult
   *          process result FC notification status
   * @return  FC notification
   * @throws RestClientException
   *           REST request failure
   */
  private String requestToFc(boolean executeResult) throws RestClientException {
    logger.trace(CommonDefinitions.START);
    logger.debug(executeResult);

    NotifyNodeStartUpToFc sendMessage = new NotifyNodeStartUpToFc();
    String status = "";
    if (bootStatus == false) {
      status = BOOT_RET_FAILED;
    } else {
      if (executeResult == true) {
        status = BOOT_RET_SUCCESS; 
      } else {
        status = BOOT_RET_CANCEL;
      }
    }
    sendMessage.setStatus(status);
    if (nodeAdditionThread != null) {
      sendMessage.setNodeInfo(nodeAdditionThread.getAddNodeInfo());
    } else {
      logger.debug("Not found data. [FC received data]");
      throw new RestClientException(RestClientException.COMMON_NG);
    }

    HashMap<String, String> keyMap = new HashMap<String, String>();
    if (!bootNodeId.isEmpty()) {
      keyMap.put(KEY_NODE_ID, String.valueOf(bootNodeId));
    } else {
      logger.debug("Not found data. [NodeID]");
      throw new RestClientException(RestClientException.COMMON_NG);
    }

    new RestClient().request(RestClient.NOTIFY_NODE_ADDITION, keyMap, sendMessage, CommonResponseFromFc.class);

    logger.trace(CommonDefinitions.END);
    return status;
  }

  /**
   * Do the followings as cleanup. - Terminating DHCP and syslog monitoring, - Error notification to FC, - Device start-up notification information deletion, - Device status update
   *
   * @param reqDhcpSyslogFlag
   *          DHCP termination and Syslog monitoring are necessary
   * @param reqNotifyFlag
   *          FC notification is necessary
   * @param reqDeleteFlag
   *          DB deletion is necessary
   * @param reqUpdateStatusFlag
   *          device status update is necessary
   */
  private void cleanUp(boolean reqDhcpSyslogFlag, boolean reqNotifyFlag, boolean reqDeleteFlag,
      boolean reqUpdateStatusFlag) {
    logger.trace(CommonDefinitions.START);
    logger.debug("reqDhcpSyslogFlag:" + reqDhcpSyslogFlag + " reqNotifyFlag:" + reqNotifyFlag + " reqDeleteFlag:"
        + reqDeleteFlag + " reqUpdateStatusFlag:" + reqUpdateStatusFlag);

    String msgToFc = "";

    try {
      if (reqDhcpSyslogFlag) {
        DevctrlCommon.cleanUp();
      }

      if (reqNotifyFlag) {
        msgToFc = requestToFc(false);
      }

      if (reqDeleteFlag) {
        deleteNodesStartupNotification();
      }

      if (reqUpdateStatusFlag) {
        updateNodeStatus();
      }

      if (!msgToFc.isEmpty() && !msgToFc.equals(BOOT_RET_SUCCESS)) {
        OperationControlManager.getInstance().rollbackAddedNodeInfo(bootNodeId);
      }
    } catch (RestClientException | DBAccessException | AddNodeException exp) {
      logger.debug("cleanUp error : ", exp);
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device Status Update.
   * @throws AddNodeException device status update failure
   */
  private void updateNodeStatus() throws AddNodeException {
    logger.trace(CommonDefinitions.START);
    NodeAdditionState state = null;
    if (bootStatus == false) {
      state = NodeAdditionState.FailedInfraSetting;
    } else {
      state = NodeAdditionState.Failed;
    }
    boolean ret = OperationControlManager.getInstance().updateNodeAdditionState(state, bootNodeId);
    if (ret == false) {
      logger.debug("updateNodeAdditinState error.");
      throw new AddNodeException();
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device Start-up Notification Information Deletion.
   *
   * @throws DBAccessException
   *           DB exception occurrence
   */
  private void deleteNodesStartupNotification() throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    try (DBAccessManager session = new DBAccessManager()) {
      session.startTransaction();
      session.deleteNodesStartupNotification(bootNodeId);
      session.commit();
    } catch (DBAccessException dbae) {
      if (dbae.getCode() == DBAccessException.NO_DELETE_TARGET) {
        logger.debug("Not found record. node_id:" + bootNodeId);
      } else {
        throw dbae;
      }
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * EC Main Status Acquisition.
   *
   * @return EC main status
   */
  private ECMainState getEcMainState() {
    logger.trace(CommonDefinitions.START);
    ECMainState status = null;

    try {
      status = OperationControlManager.getInstance().getEcMainState(false);
    } catch (DBAccessException dbae) {
      logger.debug("Internal Error.", dbae);
      status = ECMainState.Stop;
    }
    logger.trace(CommonDefinitions.END);
    return status;
  }

  /**
   * Waiting for Unsent Device Start-up Notification Sending Status.
   */
  private void waitNodeStateNotificationSending() {
    logger.trace(CommonDefinitions.START);
    while (OperationControlManager.getInstance().isUnsentNodeStateNotificationSendingState()) {
      CommonUtil.sleep();
    }
    logger.trace(CommonDefinitions.END);
    return;
  }
}
