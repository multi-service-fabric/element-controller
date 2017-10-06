package msf.ecmm.devctrl;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DevctrlCommon {

    private static  final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	public static  void cleanUp() {
		logger.trace(CommonDefinitions.START);

		boolean dhcpOkFlag = false;
		DhcpController dhcpController = DhcpController.getInstance();
		SyslogController syslogController = SyslogController.getInstance();
		try {
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
