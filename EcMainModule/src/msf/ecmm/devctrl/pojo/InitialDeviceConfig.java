/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl.pojo;

/**
 * Device initial config input information.
 */
public class InitialDeviceConfig {

  /** Device initial config file template path */
  private String configTemplete;

  /** Initial config file path for Zero touch config */
  private String initialConfig;

  /** device management IF address */
  private String deviceManagementAddress;

  /** NTP server address */
  private String ntpServerAddress;

  /** management IF prefix */
  private Integer devicemanagementCidraddress;

  /** BGP community value */
  private String commmunityMembers;

  /** BGP Master / Slave community value */
  private String belongingSideMembers;

  /**
   * Constructor.
   *
   * @param configTemplete
   *          Device initial config file template path
   * @param initialConfig
   *          Initial config file path for Zero touch config
   * @param deviceManagementAddress
   *          device management IF address
   * @param ntpServerAddress
   *          NTP server address
   * @param commmunityMembers
   *          BGP community value
   * @param belongingSideMembers
   *          BGP Master / Slave community value
   * @param devicemanagementCidraddress
   *          management IF prefix
   */
  public InitialDeviceConfig(String configTemplete, String initialConfig, String deviceManagementAddress,
      String ntpServerAddress, String commmunityMembers, String belongingSideMembers,
      Integer devicemanagementCidraddress) {
    super();
    this.configTemplete = configTemplete;
    this.initialConfig = initialConfig;
    this.deviceManagementAddress = deviceManagementAddress;
    this.devicemanagementCidraddress = devicemanagementCidraddress;
    this.ntpServerAddress = ntpServerAddress;
    this.commmunityMembers = commmunityMembers;
    this.belongingSideMembers = belongingSideMembers;
  }

  /**
   * Getting Device initial config file template path.
   *
   * @return Device initial config file template path
   */
  public String getConfigTemplete() {
    return configTemplete;
  }

  /**
   * Setting Device initial config file template path.
   *
   * @param configTemplete
   *                Device initial config file template path
   */
  public void setConfigTemplete(String configTemplete) {
    this.configTemplete = configTemplete;
  }

  /**
   * Getting Initial config file path for Zero touch config.
   *
   * @return Initial config file path for Zero touch config
   */
  public String getInitialConfig() {
    return initialConfig;
  }

  /**
   * Setting Initial config file path for Zero touch config.
   *
   * @param initialConfig
   *                Initial config file path for Zero touch config
   */
  public void setInitialConfig(String initialConfig) {
    this.initialConfig = initialConfig;
  }

  /**
   * Getting device management IF address.
   *
   * @return device management IF address
   */
  public String getDeviceManagementAddress() {
    return deviceManagementAddress;
  }

  /**
   * Setting device management IF address..
   *
   * @param deviceManagementAddress
   *          device management IF address
   */
  public void setDeviceManagementAddress(String deviceManagementAddress) {
    this.deviceManagementAddress = deviceManagementAddress;
  }

  /**
   * Getting management IF prefix.
   *
   * @return management IF prefix
   */
  public Integer getDevicemanagementCidraddress() {
    return devicemanagementCidraddress;
  }

  /**
   * Setting management IF prefix.
   *
   * @param devicemanagementCidraddress
   *          management IF prefix
   */
  public void setDevicemanagementCidraddress(Integer devicemanagementCidraddress) {
    this.devicemanagementCidraddress = devicemanagementCidraddress;
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
   * Getting BGP community value.
   *
   * @return BGP community value
   */
  public String getCommmunityMembers() {
    return commmunityMembers;
  }

  /**
   * Setting BGP community value.
   *
   * @param commmunityMembers
   *          BGP community value
   */
  public void setCommmunityMembers(String commmunityMembers) {
    this.commmunityMembers = commmunityMembers;
  }

  /**
   * Getting BGP Master / Slave community value.
   *
   * @return BGP Master / Slave community value
   */
  public String getBelongingSideMembers() {
    return belongingSideMembers;
  }

  /**
   * Setting BGP Master / Slave community value.
   *
   * @param belongingSideMembers
   *          BGP Master / Slave community value
   */
  public void setBelongingSideMembers(String belongingSideMembers) {
    this.belongingSideMembers = belongingSideMembers;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "InitialDeviceConfig [configTemplete=" + configTemplete + ", deviceManagementAddress="
        + deviceManagementAddress + ", devicemanagementCidraddress=" + devicemanagementCidraddress
        + ", ntpServerAddress=" + ntpServerAddress + ", commmunitymembers=" + commmunityMembers
        + ", belongingsidemembers=" + belongingSideMembers + "]";
  }

}
