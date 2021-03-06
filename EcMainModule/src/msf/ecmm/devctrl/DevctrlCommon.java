/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;

/**
 * EC Inter-Device IF Control Functional Part Class.
 */
public class DevctrlCommon {

  /**
   * Logger.
   */
  private static final MsfLogger logger = new MsfLogger();

  /**
   * Clean up (DHCP stop/Syslog monitoring stop).
   */
  public static void cleanUp() {
    logger.trace(CommonDefinitions.START);

    boolean dhcpOkFlag = false;
    DhcpController dhcpController = DhcpController.getInstance();
    SyslogController syslogController = SyslogController.getInstance();
    try {
      dhcpController.stop(false); 
      dhcpOkFlag = true;
      syslogController.monitorStop(true);
    } catch (DevctrlException e) {
      if (dhcpOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "DHCP stop was failed."), e);
      } else {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Watch syslog stop was failed."), e);
      }
    }
    logger.trace(CommonDefinitions.END);
  }
}
