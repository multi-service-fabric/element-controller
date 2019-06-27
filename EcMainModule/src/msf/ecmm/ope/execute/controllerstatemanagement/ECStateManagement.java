/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.controllerstatemanagement;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommandExecutor;
import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
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
import msf.ecmm.ope.receiver.pojo.parts.Informations;

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
  /** Failed to get SBY status. */
  private static final String ERROR_CODE_310305 = "310305";
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

  /** Controller status acquisition shell script name(SBY). */
  private static final String CONTROLLER_STATUS_SBY = "controller_status_sby.sh";

  /** Controller type. */
  /** EC(ACT). */
  private static final String EC_ACT = "ec";
  /** EM(ACT). */
  private static final String EM_ACT = "em";
  /** EC(SBY). */
  private static final String EC_SBY = "ec_sby";
  /** EM(SBY). */
  private static final String EM_SBY = "em_sby";

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
    int receiveCountSby = 0;
    int sendCount = 0;
    int sendCountSby = 0;
    List<String> stdList = new ArrayList<String>();
    List<String> errList = new ArrayList<String>();
    List<String> stdListSby = new ArrayList<String>();
    List<String> errListSby = new ArrayList<String>();
    ControllerStatusFromEm emControllerInformations = new ControllerStatusFromEm();

    boolean cmdRet = true;
    int ret = 0;

    boolean getEcFlg = false;
    boolean getEmFlg = false;
    boolean getEcSbyFlg = false;
    boolean getEmSbyFlg = false;

    String getUriConStr = getUriKeyMap().get(KEY_CONTROLLER);

    String getEmOnlyStr = "";

    if (getUriKeyMap().get(KEY_CONTROLLER) == null || getUriKeyMap().get(KEY_CONTROLLER).isEmpty()) {
      getEcFlg = true;
      getEmFlg = true;
      getEcSbyFlg = true;
      getEmSbyFlg = true;

      getEmOnlyStr = EM_ACT + "+" + EM_SBY;
    } else {
      String[] getConTarget = getUriConStr.split(" ");
      for (String target : getConTarget) {
        if (target.equals(EC_ACT)) {
          getEcFlg = true;
        }
        if (target.equals(EM_ACT)) {
          getEmFlg = true;
          if (getEmOnlyStr.length() != 0) {
            getEmOnlyStr += "+";
          }
          getEmOnlyStr += EM_ACT;
        }
        if (target.equals(EC_SBY)) {
          getEcSbyFlg = true;
        }
        if (target.equals(EM_SBY)) {
          getEmSbyFlg = true;
          if (getEmOnlyStr.length() != 0) {
            getEmOnlyStr += "+";
          }
          getEmOnlyStr += EM_SBY;
        }
      }
    }

    String top = "0";
    String nproc = "0";
    String df = "0";
    String sar = "0";

    AbstractResponseMessage response = null;

    try {
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

      if (getUriKeyMap().get(KEY_CONTROLLER) == null || getEcFlg || getUriKeyMap().get(KEY_CONTROLLER).isEmpty()) {

        ecMainState = ECMainState.ecMainStateToLabel(OperationControlManager.getInstance().getEcMainState(false));
        ecMainObstraction = OperationControlManager.getInstance().getEcMainObstraction(false);

        serverAddress = EcConfiguration.getInstance().get(String.class, EcConfiguration.SERVER_PHYSICAL_ADDRESS);
        String requestAverage = EcConfiguration.getInstance().get(String.class, EcConfiguration.REST_REQUEST_AVERAGE);

        String hostname = LINUX_COMMAND_HOSTNAME;

        String scriptPath = EcConfiguration.getInstance().get(String.class, EcConfiguration.SCRIPT_PATH);
        String[] acquisitionconditions = { scriptPath + "/" + CONTROLLER_STATUS, top, nproc, df, sar, hostname,
            CONTROLLER_PID };

        if (!CommonUtil.isFile(acquisitionconditions[0])) {
          logger.warn(
              LogFormatter.out.format(LogFormatter.MSG_403123, "StandbyControllerStatusScriptFile was not found."));
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_310305);
          return response;
        }

        ret = CommandExecutor.exec(acquisitionconditions, stdList, errList);

        if (ret != 0) {
          cmdRet = false;
          throw new Exception();
        }

        receiveCount = RestRequestCount.getRestRequestCount(REST_RECEPTION, Integer.parseInt(requestAverage));
        sendCount = RestRequestCount.getRestRequestCount(REST_TRANSMISSION, Integer.parseInt(requestAverage));
      }

      if (getUriKeyMap().get(KEY_CONTROLLER) == null || getEcSbyFlg || getUriKeyMap().get(KEY_CONTROLLER).isEmpty()) {
        serverAddress = EcConfiguration.getInstance().get(String.class, EcConfiguration.SBY_IP_ADDRESS);
        String requestAverage = EcConfiguration.getInstance().get(String.class, EcConfiguration.REST_REQUEST_AVERAGE);

        String sbyIpAddress = EcConfiguration.getInstance().get(String.class, EcConfiguration.SBY_IP_ADDRESS);
        String sbyUsrName = EcConfiguration.getInstance().get(String.class, EcConfiguration.SBY_USRNAME);
        String sbyPassword = EcConfiguration.getInstance().get(String.class, EcConfiguration.SBY_PASSWORD);
        String sbyStatusGetShellFile = EcConfiguration.getInstance().get(String.class,
            EcConfiguration.SBY_STATUS_GET_SHELL_FILE);

        String hostname = LINUX_COMMAND_HOSTNAME;

        String scriptPath = EcConfiguration.getInstance().get(String.class, EcConfiguration.SCRIPT_PATH);
        String[] acquisitionconditionsSby = { scriptPath + "/" + CONTROLLER_STATUS_SBY, sbyIpAddress, sbyUsrName,
            sbyPassword, sbyStatusGetShellFile, top, nproc, df, sar, hostname, };

        if (!CommonUtil.isFile(acquisitionconditionsSby[0])) {
          logger.warn(
              LogFormatter.out.format(LogFormatter.MSG_403123, "StandbyControllerStatusScriptFile was not found."));
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_310305);
          return response;
        }

        ret = CommandExecutor.exec(acquisitionconditionsSby, stdListSby, errListSby);

        if (ret != 0) {
          cmdRet = false;
          throw new Exception();
        }

        receiveCountSby = RestRequestCount.getRestRequestCount(REST_RECEPTION, Integer.parseInt(requestAverage));
        sendCountSby = RestRequestCount.getRestRequestCount(REST_TRANSMISSION, Integer.parseInt(requestAverage));
      }

      if (getUriKeyMap().get(KEY_CONTROLLER) == null || getEmFlg || getEmSbyFlg
          || getUriKeyMap().get(KEY_CONTROLLER).isEmpty()) {
        RestClientToEm rc = new RestClientToEm();
        HashMap<String, String> keyMap = new HashMap<String, String>();
        AbstractRequest abstractRequest = new AbstractRequest();
        keyMap.put(KEY_CONTROLLER, getEmOnlyStr);
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

      ArrayList<Informations> informationList = new ArrayList<Informations>();
      if (getEcFlg) {
        informationList
            .add(RestMapper.createInformations(EC_ACT, getinfo, serverAddress, receiveCount, sendCount, stdList));
      }
      if (getEcSbyFlg) {
        informationList.add(
            RestMapper.createInformations(EC_SBY, getinfo, serverAddress, receiveCountSby, sendCountSby, stdListSby));
      }
      informationList.addAll(emControllerInformations.getInformations());

      CheckEcMainModuleStatus outputData = RestMapper.toControllerStatus(ecMainObstraction, ecMainState,
          emControllerInformations.getStatus(), informationList);

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
