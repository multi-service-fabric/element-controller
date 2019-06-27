/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.common.CommonDefinitionsNodeOsUpgrade.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.NotifyOsUpgrade;

/**
 * Class for notifying that OS has been upgraded in the node.
 */
public class NodeOsUpgradeNotification extends Operation {

  /** The name of the extened operation. */
  String operationName = "NodeOsUpgradeNotification";

  /** In case the check of the input paramter is NG. */
  private static final String ERROR_CODE_660101 = "660101";


  /** In case the status of upgrding OS is not correct. */
  private static final String ERROR_CODE_660202 = "660202";

  /**
   * Constructor.
   *
   * @param idt
   *          Input data
   * @param ukm
   *          URI key information
   */
  public NodeOsUpgradeNotification(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(operationName).getOperationTypeId());
  }

  @Override
  public boolean prepare() {
    logger.trace(CommonDefinitions.START);
    boolean ret = super.prepare();
    if (ret == false) {
      logger.debug("Operation.prepare error");
      if (!NodeOsUpgrade.control(NODE_OS_UPGRADE_FAILED)) {
        logger.debug("Operation is not executed");
      }
    }
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    String ipAddress = "";
    String notifyResult = "";

    if (!checkInData()) {
      logger.debug("Input dadta wrong.");
      notifyResult = NODE_OS_UPGRADE_FAILED;
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_660101);
    } else {
      NotifyOsUpgrade inputData = (NotifyOsUpgrade) getInData();
      ipAddress = inputData.getManagementIfAddress();

      if (getUriKeyMap().get(KEY_STATUS).equals(RECV_OK_NOTIFICATION_STRING)) {
        notifyResult = NODE_OS_UPGRADE_SUCCESS;
      } else {
        notifyResult = NODE_OS_UPGRADE_FAILED;
      }
    }

    OperationControlManager.getInstance().setOperationInfo(NODE_OS_UPGRADE_NOTIFIED_IP_KEY, ipAddress);

    if (!NodeOsUpgrade.control(notifyResult)) {
      if (response == null) {
        response = makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_660202);
      }
    }

    if (response == null) {
      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    if (!getUriKeyMap().containsKey(KEY_STATUS)) {
      checkResult = false;
    } else {
      String status = getUriKeyMap().get(KEY_STATUS);
      if (!status.equals(RECV_OK_NOTIFICATION_STRING) && !status.equals(RECV_NG_NOTIFICATION_STRING)) {
        checkResult = false;
      }
    }

    if (checkResult) {
      NotifyOsUpgrade inputData = (NotifyOsUpgrade) getInData();
      try {
        inputData.check(new OperationType(getOperationType()));
      } catch (CheckDataException cde) {
        logger.warn("check error :", cde);
        checkResult = false;
      }
    }
    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

}
