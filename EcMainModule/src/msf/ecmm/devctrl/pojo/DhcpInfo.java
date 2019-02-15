/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl.pojo;

/**
 * DHCP Start-up Information.
 */
public class DhcpInfo {
  /** DHCP Configuration Template Name. */
  private String configTemplete;

  /** Initialization File for Zero Touch Configuration. */
  private String initialConfig;

  /** Host Name. */
  private String hostname;

  /** MAC Address. */
  private String macAddress;

  /** TFTP Host Name. */
  private String tftpHostname;

  /** NTP Server Address. */
  private String ntpServerAddress;

  /** Device's Management IF Address. */
  private String eqManagementAddress;

  /** Device's Prefix. */
  private int prefix;

  /**
   * @param configTemplete.
   *          DHCP configuration template name
   * @param initialConfig
   *          initializaiton file for zero touch configuration
   * @param hostname
   *          host name
   * @param macAddress
   *          MAC address
   * @param tftpHostname
   *          TFTP host name
   * @param ntpServerAddress
   *          NTP server address
   * @param eqManagementAddress
   *          device's management IP address
   * @param prefix
   *          prefix
   */
  public DhcpInfo(String configTemplete, String initialConfig, String hostname, String macAddress, String tftpHostname,
      String ntpServerAddress, String eqManagementAddress, int prefix) {
    super();
    this.configTemplete = configTemplete;
    this.initialConfig = initialConfig;
    this.hostname = hostname;
    this.macAddress = macAddress;
    this.tftpHostname = tftpHostname;
    this.ntpServerAddress = ntpServerAddress;
    this.eqManagementAddress = eqManagementAddress;
    this.prefix = prefix;
  }

  /**
   * Getting DHCP configuration template name.
   *
   * @return DHCP configuration template name
   */
  public String getConfigTemplete() {
    return configTemplete;
  }

  /**
   * Setting DHCP configuration template name.
   *
   * @param configTemplete
   *          DHCP configuration template name
   */
  public void setConfigTemplete(String configTemplete) {
    this.configTemplete = configTemplete;
  }

  /**
   * Getting initialization file for zero touch configuration.
   *
   * @return initialization file for zero touch configuration
   */
  public String getInitialConfig() {
    return initialConfig;
  }

  /**
   * Setting initialization file for zero touch configuration.
   *
   * @param initialConfig
   *          initialization file for zero touch configuration
   */
  public void setInitialConfig(String initialConfig) {
    this.initialConfig = initialConfig;
  }

  /**
   * Getting host name.
   *
   * @return host name
   */
  public String getHostname() {
    return hostname;
  }

  /**
   * Setting host name.
   *
   * @param hostname
   *          host name
   */
  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  /**
   * Getting MAC address.
   *
   * @return MAC address
   */
  public String getMacAddress() {
    return macAddress;
  }

  /**
   * Setting MAC address.
   *
   * @param macAddress
   *          MAC address
   */
  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }

  /**
   * Getting TFTP host name.
   *
   * @return TFTP host name
   */
  public String getTftpHostname() {
    return tftpHostname;
  }

  /**
   * Setting TFTP host name.
   *
   * @param tftpHostname
   *          TFTP host name
   */
  public void setTftpHostname(String tftpHostname) {
    this.tftpHostname = tftpHostname;
  }

  /**
   * Getting NTP server address.
   *
   * @return NTP server address
   */
  public String getNtpServerAddress() {
    return ntpServerAddress;
  }

  /**
   * Setting NTP server address.
   *
   * @param ntpServerAddress
   *          NTP server address
   */
  public void setNtpServerAddress(String ntpServerAddress) {
    this.ntpServerAddress = ntpServerAddress;
  }

  /**
   * Getting device's management IF address.
   *
   * @return device's management IF address
   */
  public String getEqManagementAddress() {
    return eqManagementAddress;
  }

  /**
   * Setting device's management IF address.
   *
   * @param eqManagementAddress
   *          device's management IF address
   */
  public void setEqManagementAddress(String eqManagementAddress) {
    this.eqManagementAddress = eqManagementAddress;
  }

  /**
   * Getting device's Prefix.
   *
   * @return device's Prefix
   */
  public int getPrefix() {
    return prefix;
  }

  /**
   * Setting device's Prefix.
   *
   * @param prefix
   *          device's Prefix
   */
  public void setPrefix(int prefix) {
    this.prefix = prefix;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "DhcpInfo [configTemplete=" + configTemplete + ", initialConfig=" + initialConfig + ", hostname=" + hostname
        + ", macAddress=" + macAddress + ", tftpHostname=" + tftpHostname + ", ntpServerAddress=" + ntpServerAddress
        + ", eqManagementAddress=" + eqManagementAddress + ", prefix=" + prefix + "]";
  }

}
