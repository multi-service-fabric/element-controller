/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
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
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.AddNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Device Extention Thread Class.
 */
public class NodeAdditionThread {

  protected static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** Device Star-up Succeeded. */
  private static final String BOOT_RET_SUCCESS = RECV_OK_NOTIFICATION_STRING;
  /** Device Start-up Failed. */
  private static final String BOOT_RET_FAILED = RECV_NG_NOTIFICATION_STRING;
  /** Device Start-up Cancelled. */
  private static final String BOOT_RET_CANCEL = WAIT_NOTIFICATION_STRING;

  /** Device Extention Information Received from FC. */
  private AddNode addNodeInfo;

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
   * Device Start-up Completion Notification.
   *
   * @param bootStatus
   *          device start-up result
   */
  public void notifyNodeBoot(boolean bootStatus) {
    logger.trace(CommonDefinitions.START);

    String msgToFc = "";

    try {
      boolean operationResult = false;

      updateNodeStatus(bootStatus, NodeAdditionState.InfraSettingComplete, NodeAdditionState.FailedInfraSetting);

      if (bootStatus == true) {

        operationResult = executeOperation();

        updateNodeStatus(operationResult, NodeAdditionState.Complete, NodeAdditionState.Failed);
      }

      msgToFc = notifyAddNodeResult(bootStatus, operationResult);

      if (addNodeInfo.getCreateNode().getProvisioning() == false) {
        deleteNodesStartupNotification(addNodeInfo.getCreateNode().getNodeId());
      }

    } catch (AddNodeException ane) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "notifyNodeBoot was failed."), ane);
    }

    if (!msgToFc.isEmpty() && !msgToFc.equals(BOOT_RET_SUCCESS)) {
      OperationControlManager.getInstance().rollbackAddedNodeInfo(addNodeInfo.getCreateNode().getNodeId());
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device Status Update.
   *
   * @param condition
   *          condition
   * @param trueStatus
   *          in case condition is true
   * @param falseStatus
   *          in case condition is false
   * @throws AddNodeException
   *           status update failed
   */
  private void updateNodeStatus(boolean condition, NodeAdditionState trueStatus, NodeAdditionState falseStatus)
      throws AddNodeException {
    logger.trace(CommonDefinitions.START);
    logger.debug("condition=" + condition + "," + trueStatus + "," + falseStatus);

    NodeAdditionState nodeStatus = null;

    if (condition == true) {
      nodeStatus = trueStatus;
    } else {
      nodeStatus = falseStatus;
    }
    boolean ret = OperationControlManager.getInstance().updateNodeAdditionState(nodeStatus,
        addNodeInfo.getCreateNode().getNodeId());
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
  private boolean executeOperation() {
    logger.trace(CommonDefinitions.START);


    OperationType operationType = OperationType.None;
    HashMap<String, String> uriKeyMap = new HashMap<>(); 
    AbstractRestMessage requestPojo = addNodeInfo;

    if (addNodeInfo.getCreateNode().getNodeType().equals(CommonDefinitions.NODETYPE_SPINE)) {
      operationType = OperationType.SpineAddition;
    } else if (addNodeInfo.getCreateNode().getNodeType().equals(CommonDefinitions.NODETYPE_LEAF)) {
      operationType = OperationType.LeafAddition;
    } else { 
      operationType = OperationType.BLeafAddition;
    }
    Operation operation = OperationFactory.create(operationType, uriKeyMap, requestPojo);

    AbstractResponseMessage executeResponse = operation.execute();

    logger.trace(CommonDefinitions.END);
    return checkResponse(executeResponse);
  }

  /**
   * Operation Execution Result Determination.
   *
   * @param response
   *          response (operation execution result)
   * @return true: success, false: fail
   */
  private boolean checkResponse(AbstractResponseMessage response) {
    logger.trace(CommonDefinitions.START);

    boolean result = false;
    switch (response.getResponseCode()) {
      case RESP_OK_200:
      case RESP_CREATED_201:
      case RESP_ACCEPTED_202:
      case RESP_NOCONTENTS_204:
        result = true;
        break;
      default:
        result = false;
    }
    logger.trace(CommonDefinitions.END);
    return result;
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
   * @return FC notification
   * @throws AddNodeException
   *           REST request failure
   */
  private String notifyAddNodeResult(boolean bootStatus, boolean executeResult) throws AddNodeException {

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
    keyMap.put(KEY_NODE_ID, String.valueOf(addNodeInfo.getCreateNode().getNodeId()));

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
