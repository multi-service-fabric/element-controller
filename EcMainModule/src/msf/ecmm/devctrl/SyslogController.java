/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import msf.ecmm.common.CommandExecutor;
import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Rsyslog Related Operations.
 */
public class SyslogController {
  /**
   * logger.
   */
  private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** Self(singleton). */
  private static SyslogController me = new SyslogController();

  /** the starting line of appending rsyslog.conf */
  private static final String START_PARAGRAPH_KEY = "# $$EC_MONITOR_CONFIG$$";

  /** Filter which excludes the notifications received from the other devices */
  private static final String IP_FILTER = ":fromhost-ip, !isequal, \"%s\" ~";

  /** Filter which captures the messages at the time start-up succeeded */
  private static final String SUCCESS_FILTER = ":msg, contains, \"%s\" ^%s/boot_success.sh;hostip";

  /** Filter which captures the messages at the time of start-up failure */
  private static final String FAILURE_FILTER = ":msg, contains, \"%s\"  ^%s/boot_fail.sh;hostip";

  /** Rsyslog Start-up */
  private static final String[] RSYSLOG_RESTART = { "systemctl", "restart", "rsyslog.service" };
  private static final String[] RSYSLOG_RESTART_NON_BLOCK = { "systemctl", "--no-block", "restart", "rsyslog.service" };

  /**
   * Constructor.
   */
  private SyslogController() {

  }

  /**
   * Getting Instance.
   *
   * @return its own instance
   */
  public static SyslogController getInstance() {
    return me;
  }

  /**
   * Syslog Monitoring Start.
   *
   * @param mngAddr
   *          device's management IF address
   * @param bootCompleteMsg
   *          start-up completion message
   * @param bootErrorMsgs
   *          list of start-up failure messages
   * @throws DevctrlException
   *           error has occurred in rsyslog operation
   */
  public void monitorStart(String mngAddr, String bootCompleteMsg, List<String> bootErrorMsgs) throws DevctrlException {
    logger.debug("syslog monitor start bootCompleteMsg=" + bootCompleteMsg + " bootErrorMsgs=" + bootErrorMsgs);

    String syslogConfig = EcConfiguration.getInstance().get(String.class, EcConfiguration.DEVICE_RSYSLOG_CONFIG);

    try {
      List<String> config = Files.readAllLines(new File(syslogConfig).toPath());

      ArrayList<String> rep = replace(false, config, mngAddr, bootCompleteMsg, bootErrorMsgs);

      Files.deleteIfExists(new File(syslogConfig).toPath());

      Files.write(new File(syslogConfig).toPath(), rep);

    } catch (IOException e) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_403041, e), e);
      throw new DevctrlException("rsyslog start fail.");
    }

    rsyslogReboot(false);

    logger.trace(CommonDefinitions.END);

  }

  /**
   * Syslog Monitoring Stop.
   *
   * @param nonblockFlag
   *          systemctl command non block flag
   * @throws DevctrlException
   *           error has occurred in rsyslog operation
   */
  public void monitorStop(boolean nonblockFlag) throws DevctrlException {
    logger.debug("syslog monitor stop");

    String syslogConfig = EcConfiguration.getInstance().get(String.class, EcConfiguration.DEVICE_RSYSLOG_CONFIG);

    try {
      List<String> config = Files.readAllLines(new File(syslogConfig).toPath());

      ArrayList<String> rep = replace(true, config, null, null, null);

      Files.deleteIfExists(new File(syslogConfig).toPath());

      Files.write(new File(syslogConfig).toPath(), rep);

    } catch (IOException e) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_403041, e), e);
      throw new DevctrlException("rsyslog start fail.");
    }

    rsyslogReboot(nonblockFlag);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Rsyslog Restart.
   *
   * @param nonblockFlag
   *          systemctl command non block flag
   * @throws DevctrlException
   *           in case command execution failed
   */
  private void rsyslogReboot(boolean nonblockFlag) throws DevctrlException {

    List<String> stdList = new ArrayList<String>();
    List<String> errList = new ArrayList<String>();
    int ret = CommonDefinitions.NOT_SET;
    if (nonblockFlag == false) {
      ret = CommandExecutor.exec(RSYSLOG_RESTART, stdList, errList);
    } else {
      ret = CommandExecutor.exec(RSYSLOG_RESTART_NON_BLOCK, stdList, errList);
    }
    logger.debug("RSYSLOG RESTART : " + stdList);

    if (ret != 0) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_403041, stdList));
      throw new DevctrlException("rsyslog start fail. ret=" + ret);
    }
  }

  /**
   * Creating Monitoring Configuration.
   *
   * @param del
   *          deletion mode
   * @param lines
   *          contents of rsyslog.conf
   * @param mngAddr
   *          device management IF address
   * @param bootCompleteMsg
   *          start-up succeeded
   * @param bootErrorMsgs
   *          start-up failed
   * @return created configuration
   */
  private ArrayList<String> replace(boolean del, List<String> lines, String mngAddr, String bootCompleteMsg,
      List<String> bootErrorMsgs) {

    String scriptPath = EcConfiguration.getInstance().get(String.class, EcConfiguration.SCRIPT_PATH);

    ArrayList<String> ret = new ArrayList<>();
    for (String line : lines) {
      if (line.startsWith(START_PARAGRAPH_KEY) == true)
        break;
      ret.add(line);
    }

    if (del == true) {
      logger.debug("rsyslog.conf : " + ret);
      return ret;
    }

    ret.add(START_PARAGRAPH_KEY);
    ret.add(String.format(IP_FILTER, mngAddr));
    ret.add(String.format(SUCCESS_FILTER, bootCompleteMsg, scriptPath));
    for (String msg : bootErrorMsgs) {
      ret.add(String.format(FAILURE_FILTER, msg, scriptPath));
    }
    logger.debug("rsyslog.conf : " + ret);
    return ret;
  }

}
