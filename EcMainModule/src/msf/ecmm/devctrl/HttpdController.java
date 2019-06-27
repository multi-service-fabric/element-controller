/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.common.CommandExecutor;
import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;

/**
 * httpd start-up / confirmation.
 */
public class HttpdController {
  /**
   * logger.
   */
  private final MsfLogger logger = new MsfLogger();

  /** Self(singleton) */
  private static HttpdController me = new HttpdController();

  /** httpd start-up state confirmation. */
  private static final String[] HTTPD_STATUS_CHECK = { "systemctl", "status", "httpd.service" };

  /** httpd start-up. */
  private static final String[] HTTPD_START = { "systemctl", "start", "httpd.service" };

  /**
   * Constructor.
   */
  private HttpdController() {

  }

  /**
   * Getting instance.
   *
   * @return self
   */
  public static HttpdController getInstance() {
    return me;
  }

  /**
   * httpd confirmation..
   *
   * @throws DevctrlException
   *           Abnormality caused by httpd operation
   */
  public void check() throws DevctrlException {
    List<String> stdList = new ArrayList<String>();
    List<String> errList = new ArrayList<String>();
    int ret = CommandExecutor.exec(HTTPD_STATUS_CHECK, stdList, errList);

    logger.debug("HTTPD STATUS CHECK : " + stdList);

    if (ret != 0) {
      start();
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * httpd start-up.
   *
   * @throws DevctrlException
   *           Abnormality caused by httpd operation
   */
  public void start() throws DevctrlException {
    logger.debug("httpd start httpdStatus=False");
    List<String> stdList = new ArrayList<String>();
    List<String> errList = new ArrayList<String>();
    int ret = CommandExecutor.exec(HTTPD_START, stdList, errList);

    logger.debug("HTTPD START : " + stdList);

    if (ret != 0) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_403041, stdList));
      throw new DevctrlException("httpd start up error");
    }
    logger.trace(CommonDefinitions.END);
  }

}
