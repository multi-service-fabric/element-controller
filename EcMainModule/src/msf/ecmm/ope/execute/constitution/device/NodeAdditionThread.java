/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.NotifyNodeStartUpToFc;
import msf.ecmm.ope.control.NodeAdditionState;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationFactory;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AddNode;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.RecoverNodeService;

/**
 * Device Extention Thread Class.
 */
public class NodeAdditionThread {

  protected static final MsfLogger logger = new MsfLogger();

  /** EM request "recover node" disconnection or connection timeout has occurred. */
  private static final String ERROR_CODE_080403 = "080403";
  /** EM request "recover node" error response has been received. */
  private static final String ERROR_CODE_080404 = "080404";
  /** EM request "recover service" disconnection or connection timeout has occurred. */
  private static final String ERROR_CODE_080405 = "080405";
  /** EM request "recover service" error response has been received. */
  private static final String ERROR_CODE_080406 = "080406";

  /** Device Star-up Succeeded. */
  private static final String BOOT_RET_SUCCESS = RECV_OK_NOTIFICATION_STRING;
  /** Device Start-up Failed. */
  private static final String BOOT_RET_FAILED = RECV_NG_NOTIFICATION_STRING;
  /** Device Start-up Cancelled. */
  private static final String BOOT_RET_CANCEL = WAIT_NOTIFICATION_STRING;

  /** Device Extention Information Received from FC. */
  private AddNode addNodeInfo;

  /** Additional service recovery Information Received from FC. */
  private RecoverNodeService recoverNodeInfo;

  /** Additional service recovery URI Parameters Information Received from FC. */
  private HashMap<String, String> recoverNodeUriKeyMap;

  /**
   * Getting device extention information received from FC.
   *
   * @return device extention information received from FC
   */
  public AddNode getAddNodeInfo() {
    return addNodeInfo;
  }

  /**
   * Setting device extention information received from FC.
   *
   * @param addNodeInfo
   *          device extention information received from FC
   */
  public void setAddNodeInfo(AddNode addNodeInfo) {
    this.addNodeInfo = addNodeInfo;
  }

  /**
   * Getting Additional service recovery information received from FC.
   *
   * @return recoverNodeInfo
   */
  public RecoverNodeService getRecoverNodeInfo() {
    return recoverNodeInfo;
  }

  /**
   * Setting Additional service recovery information received from FC.
   *
   * @param recoverNodeInfo
   *          Setting recoverNodeInfo
   */
  public void setRecoverNodeInfo(RecoverNodeService recoverNodeInfo) {
    this.recoverNodeInfo = recoverNodeInfo;
  }

  /**
   * Getting Additional service recovery URI Parameters information received from FC.
   *
   * @return recoverNodeUriKeyMap
   */
  public HashMap<String, String> getRecoverNodeUriKeyMap() {
    return recoverNodeUriKeyMap;
  }

  /**
   * Setting Additional service recovery URI Parameters information received from FC.
   *
   * @param recoverNodeUriKeyMap
   *          Setting recoverNodeUriKeyMap
   */
  public void setRecoverNodeUriKeyMap(HashMap<String, String> recoverNodeUriKeyMap) {
    this.recoverNodeUriKeyMap = recoverNodeUriKeyMap;
  }

  /**
   * Device Start-up Completion Notification.
   *
   * @param bootStatus
   *          device start-up result
   */
  public void notifyNodeBoot(boolean bootStatus) {
    logger.trace(CommonDefinitions.START);

    String msgToFc = "";

    boolean recoverFlag = OperationControlManager.getInstance().getRecoverExecution();

    try {

      String nodeId = null;
      if (recoverFlag) {
        nodeId = recoverNodeUriKeyMap.get(KEY_NODE_ID);
      } else {
        nodeId = addNodeInfo.getCreateNode().getNodeId();
      }
      updateNodeStatus(nodeId, bootStatus, NodeAdditionState.InfraSettingComplete,
          NodeAdditionState.FailedInfraSetting);

      boolean operationResultFlag = false;

      if (bootStatus == true) {

        AbstractResponseMessage operationResult = executeOperation();

        NodeAdditionState failState = null;
        switch (operationResult.getResponseCode()) {
          case RESP_OK_200:
          case RESP_CREATED_201:
          case RESP_ACCEPTED_202:
          case RESP_NOCONTENTS_204:
            operationResultFlag = true;
            break;
          default:
            if (recoverFlag) {
              switch (((CommonResponse) operationResult).getErrorCode()) {
                case ERROR_CODE_080403:
                case ERROR_CODE_080404:
                  failState = NodeAdditionState.NodeRecoverFailed;
                  break;
                case ERROR_CODE_080405:
                case ERROR_CODE_080406:
                  failState = NodeAdditionState.ServiceRecoverFailed;
                  break;
                default:
                  failState = NodeAdditionState.FailedOther;
              }
            } else {
              failState = NodeAdditionState.Failed;
            }
        }
        updateNodeStatus(nodeId, operationResultFlag, NodeAdditionState.Complete, failState);

      }

      msgToFc = notifyAddNodeResult(bootStatus, operationResultFlag, nodeId);

      if (!recoverFlag && !addNodeInfo.getCreateNode().getProvisioning()) {
        deleteNodesStartupNotification(nodeId);
      }

    } catch (AddNodeException ane) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "notifyNodeBoot was failed."), ane);
    }

    if (!msgToFc.isEmpty() && !msgToFc.equals(BOOT_RET_SUCCESS) && !recoverFlag) {
      OperationControlManager.getInstance().rollbackAddedNodeInfo(addNodeInfo.getCreateNode().getNodeId());
    }

    if (recoverFlag) {
      OperationControlManager.getInstance().setRecoverExecution(false);
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device Status Update.
   *
   * @param nodeId
   *          Device ID
   * @param condition
   *          condition
   * @param trueStatus
   *          in case condition is true
   * @param falseStatus
   *          in case condition is false
   * @throws AddNodeException
   *           status update failed
   */
  private void updateNodeStatus(String nodeId, boolean condition, NodeAdditionState trueStatus,
      NodeAdditionState falseStatus) throws AddNodeException {
    logger.trace(CommonDefinitions.START);
    logger.debug("condition=" + condition + "," + trueStatus + "," + falseStatus);

    NodeAdditionState nodeStatus = null;

    if (condition == true) {
      nodeStatus = trueStatus;
    } else {
      nodeStatus = falseStatus;
    }
    boolean ret = OperationControlManager.getInstance().updateNodeAdditionState(nodeStatus, nodeId);
    if (ret == false) {
      logger.debug("updateNodeStatus error");
      throw new AddNodeException();
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Operation Execution.
   *
   * @return operation execution result
   */
  private AbstractResponseMessage executeOperation() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage executeResponse = null;
    Operation operation = null;
    if (!OperationControlManager.getInstance().getRecoverExecution()) {
      int operationType = OperationType.None;
      if (addNodeInfo.getCreateNode().getNodeType().equals(CommonDefinitions.NODETYPE_SPINE)) {
        operationType = OperationType.SpineAddition;
      } else if (addNodeInfo.getCreateNode().getNodeType().equals(CommonDefinitions.NODETYPE_LEAF)) {
        operationType = OperationType.LeafAddition;
      } else {
        operationType = OperationType.BLeafAddition;
      }

      operation = OperationFactory.create(operationType, new HashMap<>(), addNodeInfo);
      executeResponse = operation.execute();
    } else {
      operation = OperationFactory.create(OperationType.NodeRecover, recoverNodeUriKeyMap, recoverNodeInfo);
      executeResponse = operation.execute();
    }

    logger.trace(CommonDefinitions.END);
    return executeResponse;
  }

  /**
   * Device Start-up Notifiction Information Deletion.
   *
   * @throws AddNodeException
   *           DB exception occurrence
   */
  private void deleteNodesStartupNotification(String bootNodeId) throws AddNodeException {

    logger.trace(CommonDefinitions.START);
    logger.debug("nodeId=" + bootNodeId);

    try (DBAccessManager session = new DBAccessManager()) {
      session.startTransaction();
      session.deleteNodesStartupNotification(bootNodeId);
      session.commit();
    } catch (DBAccessException dbae) {
      logger.debug("deleteNodesStartupNotification error");
      throw new AddNodeException(dbae);
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * REST Request to FC (Device Extentin Completion Notification).
   *
   * @param bootStatus
   *          device start-up result - true: success, false: fail
   * @param executeResult
   *          process result FC notification status
   * @param nodeId
   *          Device ID
   * @return FC Notification
   * @throws AddNodeException
   *           REST request failure
   */
  protected String notifyAddNodeResult(boolean bootStatus, boolean executeResult, String nodeId)
      throws AddNodeException {

    logger.trace(CommonDefinitions.START);
    logger.debug("bootStatus = " + bootStatus);
    logger.debug("executeResult" + executeResult);

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
    sendMessage.setNodeInfo(addNodeInfo);

    HashMap<String, String> keyMap = new HashMap<String, String>();
    keyMap.put(KEY_NODE_ID, nodeId);

    try {
      new RestClient().request(RestClient.NOTIFY_NODE_ADDITION, keyMap, sendMessage, CommonResponseFromFc.class);
    } catch (RestClientException rce) {
      logger.debug("request error");
      throw new AddNodeException(rce);
    }
    logger.trace(CommonDefinitions.END);
    return status;
  }
}
