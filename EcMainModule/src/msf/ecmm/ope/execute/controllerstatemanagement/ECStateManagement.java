/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.controllerstatemanagement;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommandExecutor;
import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.emctrl.RestClientToEm;
import msf.ecmm.emctrl.restpojo.AbstractRequest;
import msf.ecmm.emctrl.restpojo.ControllerStatusFromEm;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.ope.control.ECMainState;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.control.RestRequestCount;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckEcMainModuleStatus;

/**
 * Getting controller status.
 */
public class ECStateManagement extends Operation {

  /** In case input data check is NG. */
  private static final String ERROR_CODE_310101 = "310101";
  /** Linux command execution failed. */
  private static final String ERROR_CODE_310304 = "310304";
  /** Error occurred from EM while requesting controller status acquisition to EM (Error response reception). */
  private static final String ERROR_CODE_310302 = "310302";
  /** Disconnection with EM or timeout has occurred while requesting controller status acquisition to EM. */
  private static final String ERROR_CODE_310303 = "310303";
  /** Other exception. */
  private static final String ERROR_CODE_310399 = "310399";

  /** Linux command execution variable（top）. */
  private static final String LINUX_COMMAND_TOP = "1";

  /** Linux command execution variable（nproc）. */
  private static final String LINUX_COMMAND_NPROC = "1";

  /** Linux command execution variable（df）. */
  private static final String LINUX_COMMAND_DF = "1";

  /** Linux command execution variable（sar）. */
  private static final String LINUX_COMMAND_SAR = "1";

  /** Linux command execution variable（hostname）. */
  private static final String LINUX_COMMAND_HOSTNAME = "1";

  /** Request counter sending/reception type: Send. */
  private static final int REST_TRANSMISSION = 0;

  /** Request counter sending/reception type: Receive. */
  private static final int REST_RECEPTION = 1;

  /** Controller status acquisition shell script name. */
  private static final String CONTROLLER_STATUS = "controller_status.sh";

  /**
   * process id (pid).
   */
  static final String CONTROLLER_PID =
    java.lang.management.ManagementFactory.getRuntimeMXBean().getName().split("@")[0];

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public ECStateManagement(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.ECMainStateConfirm);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_310101);
    }

    String getinfo = getUriKeyMap().get(KEY_GET_INFO);
    String ecMainState = "";
    boolean ecMainObstraction = false;
    String serverAddress = "";
    int receiveCount = 0;
    int sendCount = 0;
    List<String> stdList = new ArrayList<String>();
    List<String> errList = new ArrayList<String>();
    ControllerStatusFromEm emControllerInformations = new ControllerStatusFromEm();

    boolean cmdRet = true;

    AbstractResponseMessage response = null;

    try {
      if (getUriKeyMap().get(KEY_CONTROLLER) == null || getUriKeyMap().get(KEY_CONTROLLER).contains("ec")
          || getUriKeyMap().get(KEY_CONTROLLER).isEmpty()) {

        ecMainState = ECMainState.ecMainStateToLabel(OperationControlManager.getInstance().getEcMainState(false));
        ecMainObstraction = OperationControlManager.getInstance().getEcMainObstraction(false);

        serverAddress = EcConfiguration.getInstance().get(String.class, EcConfiguration.SERVER_PHYSICAL_ADDRESS);
        String requestAverage = EcConfiguration.getInstance().get(String.class, EcConfiguration.REST_REQUEST_AVERAGE);

        String top = "0";
        String nproc = "0";
        String df = "0";
        String sar = "0";
        if (getinfo == null || getinfo.isEmpty()) {
          top = LINUX_COMMAND_TOP;
          nproc = LINUX_COMMAND_NPROC;
          df = LINUX_COMMAND_DF;
          sar = LINUX_COMMAND_SAR;
        } else {
          if (getinfo.contains("os-cpu") || getinfo.contains("os-mem") || getinfo.contains("os-disk")
              || getinfo.contains("ctr-cpu") || getinfo.contains("ctr-mem")) {
            top = LINUX_COMMAND_TOP;
          }
          if (getinfo.contains("ctr-cpu")) {
            nproc = LINUX_COMMAND_NPROC;
          }
          if (getinfo.contains("os-disk")) {
            df = LINUX_COMMAND_DF;
          }
          if (getinfo.contains("os-traffic")) {
            sar = LINUX_COMMAND_SAR;
          }
        }
        String hostname = LINUX_COMMAND_HOSTNAME;

        String scriptPath = EcConfiguration.getInstance().get(String.class, EcConfiguration.SCRIPT_PATH);
        String[] acquisitionconditions = { scriptPath + "/" + CONTROLLER_STATUS, top, nproc, df, sar, hostname,
            CONTROLLER_PID };
        int ret = CommandExecutor.exec(acquisitionconditions, stdList, errList);
        if (ret != 0) {
          cmdRet = false;
          throw new Exception();
        }

        receiveCount = RestRequestCount.getRestRequestCount(REST_RECEPTION, Integer.parseInt(requestAverage));
        sendCount = RestRequestCount.getRestRequestCount(REST_TRANSMISSION, Integer.parseInt(requestAverage));
      }

      if (getUriKeyMap().get(KEY_CONTROLLER) == null || getUriKeyMap().get(KEY_CONTROLLER).contains("em")
          || getUriKeyMap().get(KEY_CONTROLLER).isEmpty()) {
        RestClientToEm rc = new RestClientToEm();
        HashMap<String, String> keyMap = new HashMap<String, String>();
        AbstractRequest abstractRequest = new AbstractRequest();
        keyMap.put(KEY_GET_INFO, getUriKeyMap().get(KEY_GET_INFO));
        emControllerInformations = (ControllerStatusFromEm) rc.request(RestClientToEm.CONTROLLER_STATE, keyMap,
            abstractRequest, ControllerStatusFromEm.class);
        logger.debug("EM info=" + emControllerInformations);

        if (emControllerInformations.getStatus() == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST response"));
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_310302);
          return response;
        }
      }

      CheckEcMainModuleStatus outputData = RestMapper.toControllerStatus(getinfo, ecMainState, serverAddress,
          ecMainObstraction, receiveCount, sendCount, stdList, emControllerInformations);

      response = makeSuccessResponse(RESP_OK_200, outputData);

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Never occur error"), dbae);
    } catch (RestClientException rce) {
      if (rce.getCode() == RestClientException.ERROR_RESPONSE) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST request"), rce);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_310302);
      } else if (rce.getCode() == RestClientException.JSON_FORMAT_NG) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST json format"), rce);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_310302);
      } else {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST timeout"), rce);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_310303);
      }
    } catch (Exception exp) {
      if (cmdRet == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_503060, "Linux command wrong."), exp);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_310304);
      } else {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Other error"), exp);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_310399);
      }
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean result = true;

    if (!getUriKeyMap().containsKey(KEY_CONTROLLER)) {
      result = false;
    } else if (!getUriKeyMap().containsKey(KEY_GET_INFO)) {
      result = false;
    }
    logger.trace(CommonDefinitions.END);
    return result;
  }

}
