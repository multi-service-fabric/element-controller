/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.ecstate;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.ControllerStatusToFc;
import msf.ecmm.fcctrl.pojo.parts.Controller;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

import org.hibernate.HibernateException;

/**
 * EC Blockage Status Change Class Definition. Changing EC blockage status.
 */
public class ObstructionStateController extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_320101 = "320101";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_320301 = "320301";

  /** Other exceptions */
  private static final String ERROR_CODE_320399 = "320399";

  /** Blockage Start. */
  private static final String START_BLOCKAGE = "start blockade";

  /** Blockage End. */
  private static final String END_BLOCKAGE = "end blockade";

  /** Controller Type (EC). */
  private static final String CONTROLLER_TYPE = "ec";

  /**
   * Constructor
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public ObstructionStateController(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.ObstructionStateController);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));

      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_320101);
    } else {
    }

    boolean obst;
    if (this.getUriKeyMap().get(KEY_INSTRUCTION_TYPE).equals(CommonDefinitions.EC_BUSY_STRING)) {
      obst = true;
    } else {
      obst = false;
    }

    try {
      OperationControlManager.getInstance().updateobstructionState(true, obst);

      sendObstructionStateChange(obst);

    } catch (DBAccessException | HibernateException dae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dae);
      return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_320301);
    } catch (RestClientException rce) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_513031, "REST request"), rce);
      return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_320399);
    }

    logger.trace(CommonDefinitions.END);

    return makeSuccessResponse(RESP_OK_200, new CommonResponse());
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;


    if (checkResult) {
      if (getUriKeyMap() == null) {
        checkResult = false;
      } else if (getUriKeyMap().get(KEY_INSTRUCTION_TYPE) == null) {
        checkResult = false;
      } else if (!getUriKeyMap().get(KEY_INSTRUCTION_TYPE).equals(CommonDefinitions.EC_BUSY_STRING)
          && !getUriKeyMap().get(KEY_INSTRUCTION_TYPE).equals(CommonDefinitions.EC_IN_SERVICE_STRING)) {
        checkResult = false;
      }
    } else {
    }

    logger.trace(CommonDefinitions.END + ", checkResult=" + checkResult);

    return checkResult;
  }

  /**
   * Notifying blockage start/end to FC.
   *
   * @param obst true: blockage start, false: blockage end
   * @throws RestClientException FC notification failure
   */
  private void sendObstructionStateChange(boolean obst) throws RestClientException {

    String event = "";
    if (obst == true) {
      event = START_BLOCKAGE;
    } else {
      event = END_BLOCKAGE;
    }
    HashMap<String, String> keyMap = new HashMap<String, String>();
    ControllerStatusToFc outputData = new ControllerStatusToFc();
    Controller ecController = new Controller();
    ecController.setController_type(CONTROLLER_TYPE);
    ecController.setEvent(event);
    outputData.setController(ecController);
    RestClient rc = new RestClient();
    rc.request(RestClient.CONTROLLER_STATE_NOTIFICATION, keyMap, outputData, CommonResponseFromFc.class);
  }
}
