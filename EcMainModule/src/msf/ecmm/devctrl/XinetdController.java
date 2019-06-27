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
 * xinetd start-up / confirmation.
 */
public class XinetdController {

  /**
   * logger.
   */
  private final MsfLogger logger = new MsfLogger();

  /** Self(singleton) */
  private static XinetdController me = new XinetdController();

  /** xinetd confirmation. */
  private static final String[] XINETD_STATUS_CHECK = { "systemctl", "status", "xinetd.service" };

  /** xinetd start-up. */
  private static final String[] XINETD_START = { "systemctl", "start", "xinetd.service" };

  /**
   * Constructor.
   */
  private XinetdController() {

  }

  /**
   * Getting instance.
   *
   * @return self
   */
  public static XinetdController getInstance() {
    return me;
  }

  /**
   * xinetd confirmation..
   *
   * @throws DevctrlException
   *           Abnormality caused by xinetd operation
   */
  public void check() throws DevctrlException {
    List<String> stdList = new ArrayList<String>();
    List<String> errList = new ArrayList<String>();
    int ret = CommandExecutor.exec(XINETD_STATUS_CHECK, stdList, errList);

    logger.debug("XINETD STATUS CHECK : " + stdList);

    if (ret != 0) {
      start();
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * xinetd start-up.
   *
   * @throws DevctrlException
   *           Abnormality caused by xinetd operation
   */
  public void start() throws DevctrlException {
    logger.debug("xinetd start xinetdStatus=False");
    List<String> stdList = new ArrayList<String>();
    List<String> errList = new ArrayList<String>();
    int ret = CommandExecutor.exec(XINETD_START, stdList, errList);

    logger.debug("XINETD START : " + stdList);

    if (ret != 0) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_403041, stdList));
      throw new DevctrlException("xinetd start up error");
    }
    logger.trace(CommonDefinitions.END);
  }

}
