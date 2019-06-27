/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.controllerstatemanagement;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.RESP_BADREQUEST_400;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.RESP_INTERNALSERVERERROR_500;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.RESP_OK_200;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.LogNotificationToFc;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.NotifyEmStatusLog;

/**
 * Notication controller status(Log).
 */
public class LogNotification extends Operation {

  /** In case input paramter check is NG. */
  private static final String ERROR_CODE_640101 = "640101";
  /** In case error has been received from FC while requesting controller status notification. */
  private static final String ERROR_CODE_640301 = "640301";

  /**
   * process id (pid).
   */
  static final String CONTROLLER_PID = java.lang.management.ManagementFactory.getRuntimeMXBean().getName()
      .split("@")[0];

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public LogNotification(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.ControllerStateSendNotificationLog);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);
    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_640101);
    }

    NotifyEmStatusLog emConStatusLog = (NotifyEmStatusLog) getInData();

    LogNotificationToFc outputData = RestMapper.toLogNotificationEm(emConStatusLog);

    RestClient rc = new RestClient();
    HashMap<String, String> keyMap = new HashMap<String, String>();
    try {
      rc.request(RestClient.CONTROLLER_STATE_NOTIFICATION_LOG, keyMap, outputData, CommonResponseFromFc.class);

      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (RestClientException rce) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_513031, "REST request"), rce);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_640301);
    }
    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {
      NotifyEmStatusLog inputData = (NotifyEmStatusLog) getInData();
      inputData.check(new OperationType(OperationType.ControllerStateSendNotificationLog));

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }
    logger.trace(CommonDefinitions.END);
    return result;
  }

}