/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.ope.control.EcSession;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

/**
 * Operation Class Definition. Executing operation. (Abstract Class)
 *
 */
public abstract class Operation {

  /**
   * Logger
   */
  protected final static MsfLogger logger = new MsfLogger();

  /** Operation ID. */
  protected EcSession operationId;

  /** Input Message. */
  private AbstractRestMessage inData;

  /** Operation Type. */
  private int operationType = OperationType.None;

  /** URI Key Information. */
  private HashMap<String, String> uriKeyMap;

  /** node_id to be operated. */
  protected String nodeId = "";

  /**
   * Constructor.
   *
   * @param idt
   *          input message
   * @param ukm
   *          URI key information
   */
  public Operation(AbstractRestMessage idt, HashMap<String, String> ukm) {
    operationId = new EcSession(-1);
    inData = idt;
    uriKeyMap = ukm;
    if (uriKeyMap != null && uriKeyMap.containsKey(KEY_NODE_ID)) {
      nodeId = uriKeyMap.get(KEY_NODE_ID);
    }
  }

  /**
   * Preparation of Operation Execution<br>
   * Preparing for operation execution.
   *
   * @return execution preparation success/fail
   */
  public boolean prepare() {

    logger.trace(CommonDefinitions.START);

    boolean judge = false;

    synchronized (OperationControlManager.getInstance()) {
      judge = OperationControlManager.getInstance().judgeExecution(operationType);

      if (judge) {
        operationId = OperationControlManager.getInstance().startOperation(this);

        if (operationId == null) {
          judge = false;
        } else {
          // do nothing
        }
      } else {
        // do nothing
      }
    }

    logger.trace(CommonDefinitions.END + ", return=" + judge);

    return judge;
  }

  /**
   * Operation Execution<br>
   * Executing the operation.
   *
   * @return message for REST response
   */
  public abstract AbstractResponseMessage execute();

  /**
   * Operation Execution Termination<br>
   * Executing the termination process of operation.
   *
   * @return execution termination process success/fail
   */
  public boolean close() {

    logger.trace(CommonDefinitions.START);

    try {
      operationId.close();
    } catch (IllegalArgumentException | InternalError exp) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403042), exp);
      return false;
    }

    logger.trace(CommonDefinitions.END);

    return true;
  }

  /**
   * Input Data Check<br>
   * Determining whether the input value of REST message is right or not.
   *
   * @return check result
   */
  abstract protected boolean checkInData();

  /**
   * Generating Normal Response<br>
   * Generating the instance for creating REST response in successful completion
   *
   * @param rescode
   *          response code
   * @param res
   *          instance for responding
   * @return normal response instance
   */
  protected AbstractResponseMessage makeSuccessResponse(int rescode, AbstractResponseMessage res) {

    res.setResponseCode(rescode);

    return res;
  }

  /**
   * Generating Abnormal Response<br>
   * Generatin the instance for creating REST response in error.
   *
   * @param rescode
   *          response code
   * @param errcode
   *          error code
   * @return abnormal response instance
   */
  protected CommonResponse makeFailedResponse(int rescode, String errcode) {

    CommonResponse res = new CommonResponse();

    res.setErrorCode(errcode);
    res.setResponseCode(rescode);

    return res;
  }

  /**
   * Setting opeartion type.
   *
   * @param operationType
   *          operation type
   */
  protected void setOperationType(int operationType) {
    this.operationType = operationType;
  }

  /**
   * Getting operation ID.
   *
   * @return operation ID
   */
  protected EcSession getOperationId() {
    return operationId;
  }

  /**
   * Getting input message.
   *
   * @return input message
   */
  protected AbstractRestMessage getInData() {
    return inData;
  }

  /**
   * Getting operation type.
   *
   * @return operation type
   */
  public int getOperationType() {
    return operationType;
  }

  /**
   * Getting URI key information.
   *
   * @return URI key information
   */
  protected HashMap<String, String> getUriKeyMap() {
    return uriKeyMap;
  }

  /**
   * node ID to be operated
   *
   * @return blank if any node is not specified.
   */
  public String getNodeId() {
    return nodeId;
  }

  @Override
  public String toString() {
    return "Operation [operationId=" + operationId + ", inData=" + inData + ", operationType=" + operationType
        + ", uriKeyMap=" + uriKeyMap + ", nodeId=" + nodeId + "]";
  }

}
