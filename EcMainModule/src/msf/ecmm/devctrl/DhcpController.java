package msf.ecmm.devctrl;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import msf.ecmm.common.CommandExecutor;
import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.devctrl.pojo.DhcpInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.snmp4j.smi.IpAddress;

public class DhcpController {
	private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	private static final String TEMPLATE_KEYWORD_HOSTNAME = "$$HOSTNAME$$";

	private static final String TEMPLATE_KEYWORD_TFTPHOSTNAME = "$$TFTPHOSTNAME$$";

	private static final String TEMPLATE_KEYWORD_INITIALCONFIG = "$$INITIALCONFIG$$";

	private static final String TEMPLATE_KEYWORD_LOGSERVERADDRESS = "$$LOGSERVERADDRESS$$";

	private static final String TEMPLATE_KEYWORD_MANAGEMENTNETWORKADDRESS = "$$MANAGEMENTNETWORKADDRESS$$";

	private static final String TEMPLATE_KEYWORD_MANAGEMENTRANGEEND = "$$MANAGEMENTRANGEEND$$";

	private static final String[] DHCP_STOP = { "systemctl", "stop", "dhcpd.service" };

	private DhcpController() {

	}

	public static DhcpController getInstance() {
		return me;
	}

	public boolean initialize() {
		logger.trace(CommonDefinitions.START);

		try {
			stop(false);
		} catch (DevctrlException e) {
		}

		logger.trace(CommonDefinitions.END);
		return true;
	}

	public void start(DhcpInfo info) throws DevctrlException {
		logger.debug("DHCP start info=" + info);

		String dhcpConfig = EcConfiguration.getInstance().get(String.class, EcConfiguration.DEVICE_DHCP_CONFIG);
		String ecMngAddr = EcConfiguration.getInstance().get(String.class, EcConfiguration.DEVICE_EC_MANAGEMENT_IF);

		int mngPrefix = info.getPrefix();
		byte[] bip = addrTobyte(info.getEqManagementAddress());
		byte[] bmask = getMask(mngPrefix);
		byte[] bnwaddr = byteAND(bip, bmask);
		byte[] bstart = byteAND(bip, bmask);
		bstart[3]++;
		byte[] bend = maxSegment(info.getEqManagementAddress(), mngPrefix);
		bend[3]--;

		HashMap<String, String> replaceKeys = new HashMap<>();
		replaceKeys.put(TEMPLATE_KEYWORD_HOSTNAME, info.getHostname());
		replaceKeys.put(TEMPLATE_KEYWORD_MACADDRESS, info.getMacAddress());
		replaceKeys.put(TEMPLATE_KEYWORD_TFTPHOSTNAME, ecMngAddr);
		replaceKeys.put(TEMPLATE_KEYWORD_NTPSERVER, info.getNtpServerAddress());
		replaceKeys.put(TEMPLATE_KEYWORD_INITIALCONFIG, info.getInitialConfig());
		replaceKeys.put(TEMPLATE_KEYWORD_MANAGEMENTADDRESS, info.getEqManagementAddress());
		replaceKeys.put(TEMPLATE_KEYWORD_LOGSERVERADDRESS, ecMngAddr);
		replaceKeys.put(TEMPLATE_KEYWORD_MANAGEMENTSUBNETMASK, new IpAddress(bmask).toString());
		replaceKeys.put(TEMPLATE_KEYWORD_MANAGEMENTNETWORKADDRESS, new IpAddress(bnwaddr).toString());
		replaceKeys.put(TEMPLATE_KEYWORD_MANAGEMENTRANGESTART, new IpAddress(bstart).toString());
		replaceKeys.put(TEMPLATE_KEYWORD_MANAGEMENTRANGEEND, new IpAddress(bend).toString());

		try {
			List<String> temple = Files.readAllLines(new File(info.getConfigTemplete()).toPath());

			ArrayList<String> rep = replace(replaceKeys, temple);

			Files.deleteIfExists(new File(dhcpConfig).toPath());

			Files.write(new File(dhcpConfig).toPath(), rep);

		} catch (IOException e) {
			stop(false);
			logger.error(LogFormatter.out.format(LogFormatter.MSG_505028, e));
			throw new DevctrlException("dhcp start fail.");
		}

		List<String> stdList = new ArrayList<String>();
		List<String> errList = new ArrayList<String>();
		int ret = CommandExecutor.exec(DHCP_START, stdList, errList);

		logger.debug("DHCP START : " + stdList);

		if (ret != 0) {
			stop(false);
			logger.error(LogFormatter.out.format(LogFormatter.MSG_505028, stdList));
			throw new DevctrlException("dhcp start fail.");
		}

		logger.trace(CommonDefinitions.END);

	}

	public void stop(boolean strict) throws DevctrlException {
		logger.debug("DHCP stop strict=" + strict);

		String dhcpConfig = EcConfiguration.getInstance().get(String.class, EcConfiguration.DEVICE_DHCP_CONFIG);

		List<String> stdList = new ArrayList<String>();
		List<String> errList = new ArrayList<String>();
		int ret = CommandExecutor.exec(DHCP_STOP, stdList, errList);

		logger.debug("DHCP STOP : " + stdList);

		if (ret != 0) {
			if (strict) {
				logger.error(LogFormatter.out.format(LogFormatter.MSG_505029, stdList));
			}
		}

		try {
			Files.deleteIfExists(new File(dhcpConfig).toPath());
		} catch (IOException e) {
			if (strict) {
				logger.error(LogFormatter.out.format(LogFormatter.MSG_505029, e));
			}
		}

		logger.trace(CommonDefinitions.END);
	}

	private ArrayList<String> replace(HashMap<String, String> keys, List<String> lines) {
		ArrayList<String> ret = new ArrayList<>();
		for (String line : lines) {
			for (Entry<String, String> entry : keys.entrySet()) {
				line = line.replace(entry.getKey(), entry.getValue());
			}
			ret.add(line);
		}
		return ret;
	}

	private byte[] maxSegment(String segaddr, int len) {
		byte[] bmask = getMask(len);
		byte[] bseg = addrTobyte(segaddr);
		byte[] bmax = byteOR(bseg, byteXOR(bmask, addrTobyte("255.255.255.255")));
		byte[] b = new BigInteger(1, bmax).toByteArray();
		byte[] br = new byte[4];
		int p = b.length > 4 ? 1 : 0;
		for (int i = 0; i < br.length; i++, p++) {
			br[i] = b[p];
		}
		return br;
	}

	private byte[] addrTobyte(String s) {
		String[] sp = s.split("\\.");
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (Integer.parseInt(sp[i]));
		}
		return b;
	}

	private byte[] getMask(int n) {
		byte[] b = new BigInteger(1, new byte[] { -1, -1, -1, -1 })
				.shiftRight(n)
				.xor(new BigInteger(1, new byte[] { -1, -1, -1, -1 }))
				.toByteArray();
		byte[] br = new byte[4];
		for (int i = 0; i < br.length; i++) {
			br[i] = b[i + 1];
		}
		return br;
	}

	private byte[] byteAND(byte[] b1, byte[] b2) {
		byte[] r = new byte[b1.length];
		for (int i = 0; i < r.length; i++) {
			r[i] = (byte) (b1[i] & b2[i]);
		}
		return r;
	}

	private byte[] byteOR(byte[] b1, byte[] b2) {
		byte[] r = new byte[b1.length];
		for (int i = 0; i < r.length; i++) {
			r[i] = (byte) (b1[i] | b2[i]);
		}
		return r;
	}

	private byte[] byteXOR(byte[] b1, byte[] b2) {
		byte[] r = new byte[b1.length];
		for (int i = 0; i < r.length; i++) {
			r[i] = (byte) (b1[i] ^ b2[i]);
		}
		return r;
	}

}
