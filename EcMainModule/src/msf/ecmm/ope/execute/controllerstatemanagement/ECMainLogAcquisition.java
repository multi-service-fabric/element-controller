/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.controllerstatemanagement;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.input.ReversedLinesFileReader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import msf.ecmm.common.CommandExecutor;
import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.emctrl.RestClientToEm;
import msf.ecmm.emctrl.restpojo.AbstractRequest;
import msf.ecmm.emctrl.restpojo.ControllerLog;
import msf.ecmm.emctrl.restpojo.parts.LogData;
import msf.ecmm.emctrl.restpojo.parts.LogInformation;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetControllerLog;

/**
 * Controller Log Acquisition.
 */
public class ECMainLogAcquisition extends Operation {

  /** Linux Command Execution Variable (hostname). */
  private static final String Linux_command_hostname = "1";

  private static final String BR = System.getProperty("line.separator");

  /** Controller Log Acquisition - Linux command execution failed. */
  private static final String ERROR_CODE_430303 = "430303";

  /** Input Data Check NG. */
  private static final String ERROR_CODE_430101 = "430101";

  /** Error has returned in controller log acquisition request to EM. */
  private static final String ERROR_CODE_430301 = "430301";

  /** Disconnection or connection timeout with EM has occurred while requesting controller log acquisition to EM. */
  private static final String ERROR_CODE_430302 = "430302";

  /** Other Exceptions. */
  private static final String ERROR_CODE_430299 = "430299";

  /** Controller Status Acquisition Shell Script Name. */
  private static final String CONTROLLER_STATUS = "controller_status.sh";

  /** Linux Command Execution Variable (Controller Type). */
  private static final String LINUX_COMMAND_CONTROLLER = "ec";

  /** log File Name. */
  private static final String LOG_FILE_NAME = "application.log";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public ECMainLogAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.ECMainLogAcquisition);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_430101);
    }

    String startDate = getUriKeyMap().get(KEY_START_DATE);
    int startdateInt = Integer.parseInt(startDate);
    String endDate = getUriKeyMap().get(KEY_END_DATE);
    int enddateInt = Integer.parseInt(endDate);
    String limitNumber = getUriKeyMap().get(KEY_LIMIT_NUMBER);
    int limitNumberInt = Integer.parseInt(limitNumber);

    ControllerLog eccontrollerlog = null;
    ControllerLog emcontrollerlog = null;

    boolean cmdRet = true;

    AbstractResponseMessage response = null;

    try {
      if (getUriKeyMap().get(KEY_CONTROLLER) == null || getUriKeyMap().get(KEY_CONTROLLER).contains("ec")
          || getUriKeyMap().get(KEY_CONTROLLER).isEmpty()) {

        eccontrollerlog = new ControllerLog();

        boolean overLimitNumber = false;

        Pattern ptn = Pattern.compile("\\d{4}/\\d{2}/\\d{2}");

        String logFilePath = EcConfiguration.getInstance().get(String.class, EcConfiguration.LOG_FILE_PATH);
        ArrayList<LogData> logList = new ArrayList<LogData>();
        File file = new File(logFilePath);
        if (file != null) {
          String[] fileNames = file.list();
          List<String> fileNameList = Arrays.asList(fileNames);
          Collections.reverse(fileNameList);
          for (String filename : fileNameList) {

            if (!filename.startsWith("application")) {
              continue;
            }

            if (overLimitNumber) {
              logger.debug("overLimit!");
              break;
            }

            int logdate = 0;
            if (filename.equals(LOG_FILE_NAME)) {
              Calendar cal = Calendar.getInstance();
              SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
              logdate = Integer.parseInt(sdf.format(cal.getTime()));

            } else {
              String[] logfilename = filename.split("-", 5);
              logdate = Integer.parseInt(logfilename[1] + logfilename[2] + logfilename[3]);
            }
            if (startdateInt <= logdate && enddateInt >= logdate) {
              try (ReversedLinesFileReader rr = new ReversedLinesFileReader(new File(logFilePath + "/" + filename),
                  StandardCharsets.UTF_8)) {
                String text = "";
                String logline = "";
                while ((text = rr.readLine()) != null) {
                  if (text != null) {
                    LogData logmessage = new LogData();
                    if (text != null) {
                      Matcher matcher = ptn.matcher(text);
                      if (matcher.find()) {
                        if (logline.length() == 0) {
                          logmessage.setMessage(text);
                        } else {
                          logline = text + BR + logline;
                          logmessage.setMessage(logline);
                        }
                        logList.add(logmessage);
                        logline = "";
                        if (logList.size() == limitNumberInt) {
                          overLimitNumber = true;
                          break;
                        }
                      } else {
                        logline = text + BR + logline;
                      }
                    }
                  }
                }
              } catch (IOException ioe) {
                throw ioe;
              }
            } else {
              continue;
            }
          }
        }
        String top = "0";
        String nproc = "0";
        String df = "0";
        String sar = "0";
        String hostname = Linux_command_hostname;
        String scriptPath = EcConfiguration.getInstance().get(String.class, EcConfiguration.SCRIPT_PATH);
        String[] acquisitionconditions = { scriptPath + "/" + CONTROLLER_STATUS, top, nproc, df, sar, hostname,
            LINUX_COMMAND_CONTROLLER };
        List<String> stdList = new ArrayList<String>();
        List<String> errList = new ArrayList<String>();
        int ret = CommandExecutor.exec(acquisitionconditions, stdList, errList);
        if (ret != 0) {
          cmdRet = false;
          throw new Exception();
        }

        LogInformation logInfo = new LogInformation();
        logInfo.setLog_data(logList);
        logInfo.setOver_limit_number(overLimitNumber);
        logInfo.setData_number(logList.size());
        Gson gson = new Gson();
        Map<String, Object> scriptResultList = gson.fromJson(stdList.get(0), new TypeToken<HashMap<String, Object>>() {
        }.getType());
        logInfo.setServer_name((String) scriptResultList.get("hostname"));
        eccontrollerlog.setEm_log(logInfo);
      }

      if (getUriKeyMap().get(KEY_CONTROLLER) == null || getUriKeyMap().get(KEY_CONTROLLER).contains("em")
          || getUriKeyMap().get(KEY_CONTROLLER).isEmpty()) {

        emcontrollerlog = new ControllerLog();

        HashMap<String, String> keyMap = new HashMap<String, String>();
        keyMap.put(KEY_END_DATE, endDate);
        keyMap.put(KEY_START_DATE, startDate);
        keyMap.put(KEY_LIMIT_NUMBER, limitNumber);
        AbstractRequest abstractRequest = new AbstractRequest();
        emcontrollerlog = (ControllerLog) new RestClientToEm().request(RestClientToEm.CONTROLLER_LOG, keyMap,
            abstractRequest, ControllerLog.class);
        if (emcontrollerlog.getEm_log().getServer_name() == null || emcontrollerlog.getEm_log().getLog_data().isEmpty()
            || emcontrollerlog.getEm_log().getData_number() == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST response"));
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_430301);
          return response;
        }
      }
      GetControllerLog outputData = RestMapper.toControllerLog(startDate, endDate, limitNumberInt, eccontrollerlog,
          emcontrollerlog);

      response = makeSuccessResponse(RESP_OK_200, outputData);
    } catch (IOException ioe) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Log file read error"), ioe);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_430299);
    } catch (RestClientException rce) {
      if (rce.getCode() == RestClientException.ERROR_RESPONSE) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST request"), rce);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_430301);
      } else if (rce.getCode() == RestClientException.JSON_FORMAT_NG) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST json format"), rce);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_430301);
      } else {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST timeout"), rce);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_430302);
      }
    } catch (Exception exp) {
      if (cmdRet == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_503060, "Linux command wrong."), exp);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_430303);
      } else {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Other error"), exp);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_430299);
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
    }
    if (result && !getUriKeyMap().containsKey(KEY_START_DATE)) { 
      result = false;
    } else {
      String keyStartDate = getUriKeyMap().get(KEY_START_DATE);
      try {
        Integer.parseInt(keyStartDate);
      } catch (NumberFormatException nfe) {
        result = false;
      }
    }
    if (result && !getUriKeyMap().containsKey(KEY_END_DATE)) { 
      result = false;
    } else {
      String keyEndDate = getUriKeyMap().get(KEY_END_DATE);
      try {
        Integer.parseInt(keyEndDate);
      } catch (NumberFormatException nfe) {
        result = false;
      }
    }
    if (result && !getUriKeyMap().containsKey(KEY_LIMIT_NUMBER)) { 
      result = false;
    } else {
      String limitNumber = getUriKeyMap().get(KEY_LIMIT_NUMBER);
      try {
        Integer.parseInt(limitNumber);
      } catch (NumberFormatException nfe) {
        result = false;
      }
    }

    logger.trace(CommonDefinitions.END);
    return result;
  }

}
