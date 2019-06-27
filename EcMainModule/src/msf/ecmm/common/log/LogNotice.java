/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.common.log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.LogNotificationToFc;
import msf.ecmm.fcctrl.pojo.parts.ControllerLogToFc;

/**
 * Notifying the logs.
 */
public class LogNotice extends Thread {

  private static final String EC = "ec_act";

  private String logLv = null;
  private String logMessage = null;

  @Override
  public void run() {
    ControllerLogToFc controllerLogToFc = new ControllerLogToFc();
    controllerLogToFc.setControllerType(EC);
    controllerLogToFc.setLogLevel(logLv);
    List<String> logsList = new ArrayList<String>();
    logsList.add(logMessage);
    controllerLogToFc.setLog(logsList);

    LogNotificationToFc outputData = new LogNotificationToFc();
    outputData.setController(controllerLogToFc);

    RestClient rc = new RestClient(CommonDefinitions.NOT_OUTPUT_LOG);
    HashMap<String, String> keyMap = new HashMap<String, String>();
    try {
      rc.request(RestClient.CONTROLLER_STATE_NOTIFICATION_LOG, keyMap, outputData, CommonResponseFromFc.class);

    } catch (RestClientException rce) {
    }
  }

  /**
   * Getting log level.
   *
   * @return log level
   */
  public String getLogLv() {
    return logLv;
  }

  /**
   * Setting log level.
   *
   * @param logLv
   *          log level
   */
  public void setLogLv(String logLv) {
    this.logLv = logLv;
  }

  /**
   * Getting log contents.
   *
   * @return log contents
   */
  public String getLogMessage() {
    return logMessage;
  }

  /**
   * Setting log contents.
   *
   * @param logMessage
   *          log contents
   */
  public void setLogMessage(String logMessage) {
    this.logMessage = logMessage;
  }

}
