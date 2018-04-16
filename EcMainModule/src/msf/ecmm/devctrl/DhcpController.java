/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

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

/**
 * DHCP Related Operation.
 */
public class DhcpController {

  /**
   * logger.
   */
  private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** Self(singleton) */
  private static DhcpController me = new DhcpController();

  /** Template Replacement Keyword Host Name. */
  private static final String TEMPLATE_KEYWORD_HOSTNAME = "$$HOSTNAME$$";

  /** Template Replacement Keyword MAC Address. */
  private static final String TEMPLATE_KEYWORD_MACADDRESS = "$$MACADDRESS$$";

  /** Template Replacement Keyword TFTP Host Name. */
  private static final String TEMPLATE_KEYWORD_TFTPHOSTNAME = "$$TFTPHOSTNAME$$";

  /** Template Replacement Keyword NTP Server Name. */
  private static final String TEMPLATE_KEYWORD_NTPSERVER = "$$NTPSERVER$$";

  /** Template Replacement Keyword Initialization File for Zero Touch Configuration. */
  private static final String TEMPLATE_KEYWORD_INITIALCONFIG = "$$INITIALCONFIG$$";

  /** Template Replacement Keyword Device's Management IF. */
  private static final String TEMPLATE_KEYWORD_MANAGEMENTADDRESS = "$$MANAGEMENTADDRESS$$";

  /** Template Replacement Keyword EC's Management IF. */
  private static final String TEMPLATE_KEYWORD_LOGSERVERADDRESS = "$$LOGSERVERADDRESS$$";

  /** Template Replacement Keyword Subnet mask of the network where the device's management IF belongs to. */
  private static final String TEMPLATE_KEYWORD_MANAGEMENTSUBNETMASK = "$$MANAGEMENTSUBNETMASK$$";

  /** Template Replacement Keyword Address of the network where the device's management IF belongs to. */
  private static final String TEMPLATE_KEYWORD_MANAGEMENTNETWORKADDRESS = "$$MANAGEMENTNETWORKADDRESS$$";

  /** Template Replacement Keyword the eldest value of address range of the network where the device's management IF can belongs to. */
  private static final String TEMPLATE_KEYWORD_MANAGEMENTRANGESTART = "$$MANAGEMENTRANGESTART$$";

  /** Template Replacement Keyword the youngest value of address range of the network where the device's management IF can belongs to. */
  private static final String TEMPLATE_KEYWORD_MANAGEMENTRANGEEND = "$$MANAGEMENTRANGEEND$$";

  /** Initial config path prefix "TFTP". */
  private static final String INITIALCONFIG_PREFIX_TFTP = "/var/lib/tftpboot";

  /** Initial config path prefix "HTTP". */
  private static final String INITIALCONFIG_PREFIX_HTTP = "/var/www/html";

  /** DHCP Start-up. */
  private static final String[] DHCP_START = { "systemctl", "start", "dhcpd.service" };

  /** DHCP Stop. */
  private static final String[] DHCP_STOP = { "systemctl", "stop", "dhcpd.service" };

  /**
   * Constructor.
   */
  private DhcpController() {

  }

  /**
   * Getting instance.
   *
   * @return self
   */
  public static DhcpController getInstance() {
    return me;
  }

  /**
   * Initialization.
   *
   * @return success/fail of initialization
   */
  public boolean initialize() {
    logger.trace(CommonDefinitions.START);

    try {
      stop(false);
    } catch (DevctrlException de) {
    }

    logger.trace(CommonDefinitions.END);
    return true;
  }

  /**
   * DHCP Start-up.
   *
   * @param info
   *          DHCP start-up information
   * @throws DevctrlException
   *           error has occurred in DHCP operation
   */
  public void start(DhcpInfo info) throws DevctrlException {
    logger.debug("DHCP start info=" + info);

    int mngPrefix = info.getPrefix();
    byte[] bip = addrTobyte(info.getEqManagementAddress());
    byte[] bmask = getMask(mngPrefix);
    byte[] bnwaddr = byteAND(bip, bmask);
    byte[] bstart = byteAND(bip, bmask);
    bstart[3]++;
    byte[] bend = maxSegment(info.getEqManagementAddress(), mngPrefix);
    bend[3]--;

    String devInitConfPath = info.getInitialConfig();
    devInitConfPath = devInitConfPath.replace(INITIALCONFIG_PREFIX_TFTP, "");
    devInitConfPath = devInitConfPath.replace(INITIALCONFIG_PREFIX_HTTP, "");

    String ecMngAddr = EcConfiguration.getInstance().get(String.class, EcConfiguration.DEVICE_EC_MANAGEMENT_IF);
    HashMap<String, String> replaceKeys = new HashMap<>();
    replaceKeys.put(TEMPLATE_KEYWORD_HOSTNAME, info.getHostname());
    replaceKeys.put(TEMPLATE_KEYWORD_MACADDRESS, info.getMacAddress());
    replaceKeys.put(TEMPLATE_KEYWORD_TFTPHOSTNAME, ecMngAddr);
    replaceKeys.put(TEMPLATE_KEYWORD_NTPSERVER, info.getNtpServerAddress());
    replaceKeys.put(TEMPLATE_KEYWORD_INITIALCONFIG, devInitConfPath);
    replaceKeys.put(TEMPLATE_KEYWORD_MANAGEMENTADDRESS, info.getEqManagementAddress());
    replaceKeys.put(TEMPLATE_KEYWORD_LOGSERVERADDRESS, ecMngAddr);
    replaceKeys.put(TEMPLATE_KEYWORD_MANAGEMENTSUBNETMASK, new IpAddress(bmask).toString());
    replaceKeys.put(TEMPLATE_KEYWORD_MANAGEMENTNETWORKADDRESS, new IpAddress(bnwaddr).toString());
    replaceKeys.put(TEMPLATE_KEYWORD_MANAGEMENTRANGESTART, new IpAddress(bstart).toString());
    replaceKeys.put(TEMPLATE_KEYWORD_MANAGEMENTRANGEEND, new IpAddress(bend).toString());

    String dhcpConfig = EcConfiguration.getInstance().get(String.class, EcConfiguration.DEVICE_DHCP_CONFIG);
    try {
      List<String> temple = Files.readAllLines(new File(info.getConfigTemplete()).toPath());

      ArrayList<String> rep = replace(replaceKeys, temple);

      Files.deleteIfExists(new File(dhcpConfig).toPath());

      Files.write(new File(dhcpConfig).toPath(), rep);

    } catch (IOException ioe) {
      stop(false);
      logger.error(LogFormatter.out.format(LogFormatter.MSG_403041, ioe));
      throw new DevctrlException("dhcp start fail.");
    }

    List<String> stdList = new ArrayList<String>();
    List<String> errList = new ArrayList<String>();
    int ret = CommandExecutor.exec(DHCP_START, stdList, errList);

    logger.debug("DHCP START : " + stdList);

    if (ret != 0) {
      stop(false);
      logger.error(LogFormatter.out.format(LogFormatter.MSG_403041, stdList));
      throw new DevctrlException("dhcp start fail.");
    }

    logger.trace(CommonDefinitions.END);

  }

  /**
   * DHCP Stop.
   *
   * @param strict
   *          Whether the log should be strictly submitted or not? true:should be submitted
   * @throws DevctrlException
   *           error has occurred in DHCP operation
   */
  public void stop(boolean strict) throws DevctrlException {
    logger.debug("DHCP stop strict=" + strict);

    String dhcpConfig = EcConfiguration.getInstance().get(String.class, EcConfiguration.DEVICE_DHCP_CONFIG);

    List<String> stdList = new ArrayList<String>();
    List<String> errList = new ArrayList<String>();
    int ret = CommandExecutor.exec(DHCP_STOP, stdList, errList);

    logger.debug("DHCP STOP : " + stdList);

    if (ret != 0) {
      if (strict) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_403041, stdList));
      }
    }

    try {
      Files.deleteIfExists(new File(dhcpConfig).toPath());
    } catch (IOException ioe) {
      if (strict) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_403041, ioe));
      }
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Template Replacement.
   *
   * @param keys
   *          replacement character string
   * @param lines
   *          template
   * @return character string after the replacement
   */
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

  /**
   * Return the greatest address with being subnet masked.
   *
   * @param segaddr
   *          IP address with format "xxx.xxx.xxx.xxx"
   * @param len
   *          mask length, 1-32
   * @return max. address byte[]
   */
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

  /**
   * IP Address String "xxx.xxx.xxx.xxx" -> byte[].
   *
   * @param s
   *          IP address string
   * @return byte[]
   */
  private byte[] addrTobyte(String str) {
    String[] sp = str.split("\\.");
    byte[] b = new byte[4];
    for (int i = 0; i < 4; i++) {
      b[i] = (byte) (Integer.parseInt(sp[i]));
    }
    return b;
  }

  /**
   * mask length -> subnet maske byte[].
   *
   * @param n
   *          mask length
   * @return subnet mask byte[]
   */
  private byte[] getMask(int n) {
    byte[] b = new BigInteger(1, new byte[] { -1, -1, -1, -1 }).shiftRight(n)
        .xor(new BigInteger(1, new byte[] { -1, -1, -1, -1 })).toByteArray();
    byte[] br = new byte[4];
    for (int i = 0; i < br.length; i++) {
      br[i] = b[i + 1];
    }
    return br;
  }

  /**
   * AND Operation of Bytes.
   *
   * @param b1
   *          byte
   * @param b2
   *          byte
   * @return operation result
   */
  private byte[] byteAND(byte[] b1, byte[] b2) {
    byte[] r = new byte[b1.length];
    for (int i = 0; i < r.length; i++) {
      r[i] = (byte) (b1[i] & b2[i]);
    }
    return r;
  }

  /**
   * OR Operation of Bytes.
   *
   * @param b1
   *          byte
   * @param b2
   *          byte
   * @return operation result
   */
  private byte[] byteOR(byte[] b1, byte[] b2) {
    byte[] r = new byte[b1.length];
    for (int i = 0; i < r.length; i++) {
      r[i] = (byte) (b1[i] | b2[i]);
    }
    return r;
  }

  /**
   * XOR Operation of Bytes.
   *
   * @param b1
   *          byte
   * @param b2
   *          byte
   * @return operation result
   */
  private byte[] byteXOR(byte[] b1, byte[] b2) {
    byte[] r = new byte[b1.length];
    for (int i = 0; i < r.length; i++) {
      r[i] = (byte) (b1[i] ^ b2[i]);
    }
    return r;
  }

}
