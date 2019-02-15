/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.controllerstatemanagement;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.ControllerStatusToFc;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.EmControllerReceiveStatus;

/**
 * Controller Status Notification.
 */
public class ECMainStateSendNotification extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_450101 = "450101";
  /** Error has occurred from FC in requesting controller status notification to FC (error response received). */
  private static final String ERROR_CODE_450301 = "450301";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public ECMainStateSendNotification(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.ControllerStateSendNotification);
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_450101);
    }

    EmControllerReceiveStatus emcontrollerstatus = (EmControllerReceiveStatus) getInData();

    ControllerStatusToFc outputData = RestMapper.toControllerStatusSend(emcontrollerstatus);

    RestClient rc = new RestClient();
    HashMap<String, String> keyMap = new HashMap<String, String>();
    try {
      rc.request(RestClient.CONTROLLER_STATE_NOTIFICATION, keyMap, outputData, CommonResponseFromFc.class);

      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (RestClientException rce) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_513031, "REST request"), rce);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_450301);
    }

    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {
      EmControllerReceiveStatus inputData = (EmControllerReceiveStatus) getInData();
      inputData.check(new OperationType(OperationType.ControllerStateSendNotification));

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }
    logger.trace(CommonDefinitions.END);
    return result;
  }

}
