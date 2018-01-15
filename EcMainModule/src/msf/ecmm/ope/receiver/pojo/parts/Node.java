/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Device Information.
 */
public class Node {

  /** Device ID. */
  private String nodeId;

  /** Model ID. */
  private String equipmentTypeId;

  /** Device Status. */
  private String nodeState;

  /** Management IF Address. */
  private String managementIfAddress;

  /** Loopback IF Address. Call it address in accordance with the IF specification.*/
  private String loopbackIfAdress;

  /** SNMP Community Name. */
  private String snmpCommunity;

  /** Host Name. */
  private String hostName;

  /** MAC Address. */
  private String macAddr;

  /** NTP Server Address. */
  private String ntpServerAddress;

  /** L2/L3VPN Type. */
  private String vpnType;

  /** Login User Name. */
  private String username;

  /** Login Password. */
  private String password;

  /** Device Configuration Necessity Flag. */
  private Boolean provisioning;

  /** Belonging Plane. */
  private String plane;

  /**
   * Getting device ID.
   *
   * @return device ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting device ID.
   *
   * @param nodeId
   *          device ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting device ID.
   *
   * @return device ID
   */
  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  /**
   * Setting device ID.
   *
   * @param equipmentTypeId
   *          device ID
   */
  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  /**
   * Getting device status.
   *
   * @return device status
   */
  public String getNodeState() {
    return nodeState;
  }

  /**
   * Setting device status.
   *
   * @param nodeState
   *          device status
   */
  public void setNodeState(String nodeState) {
    this.nodeState = nodeState;
  }

  /**
   * Getting management IF address.
   *
   * @return management IF address
   */
  public String getManagementIfAddress() {
    return managementIfAddress;
  }

  /**
   * Setting management IF address.
   *
   * @param managementIfAddress
   *          management IF address
   */
  public void setManagementIfAddress(String managementIfAddress) {
    this.managementIfAddress = managementIfAddress;
  }

  /**
   * Getting loopback address.
   *
   * @return loopback address
   */
  public String getLoopbackIfAddress() {
    return loopbackIfAdress;
  }

  /**
   * Setting loopback address.
   *
   * @param loopbackIfAddress
   *          loopback address
   */
  public void setLoopbackIfAddress(String loopbackIfAddress) {
    this.loopbackIfAdress = loopbackIfAddress;
  }

  /**
   * Getting SNMP community name.
   *
   * @return SNMP community name
   */
  public String getSnmpCommunity() {
    return snmpCommunity;
  }

  /**
   * Setting SNMP community name.
   *
   * @param snmpCommunity
   *          SNMP community name
   */
  public void setSnmpCommunity(String snmpCommunity) {
    this.snmpCommunity = snmpCommunity;
  }

  /**
   * Getting host name.
   *
   * @return host name
   */
  public String getHostName() {
    return hostName;
  }

  /**
   * Setting host name.
   *
   * @param hostName
   *          host name
   */
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  /**
   * Getting MAC address.
   *
   * @return MAC address
   */
  public String getMacAddr() {
    return macAddr;
  }

  /**
   * Setting MAC address.
   *
   * @param macAddr
   *          MAC address
   */
  public void setMacAddr(String macAddr) {
    this.macAddr = macAddr;
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
   * Getting L2/L3VPN type.
   *
   * @return L2/L3VPN type
   */
  public String getVpnType() {
    return vpnType;
  }

  /**
   * Setting L2/L3VPN type.
   *
   * @param vpnType
   *          L2/L3VPN type
   */
  public void setVpnType(String vpnType) {
    this.vpnType = vpnType;
  }

  /**
   * Getting login user name.
   *
   * @return login user name
   */
  public String getUsername() {
    return username;
  }

  /**
   * Setting login user name.
   *
   * @param username
   *          login user name
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Getting login password.
   *
   * @return login password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Setting login password.
   *
   * @param password
   *          login password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Getting device configuration necessity flag.
   *
   * @return device configuration necessity flag
   */
  public boolean isProvisioning() {
    return provisioning;
  }

  /**
   * Setting device configuration necessity flag.
   *
   * @param provisioning
   *          device configuration necessity flag
   */
  public void setProvisioning(boolean provisioning) {
    this.provisioning = provisioning;
  }

  /**
   * Getting belonging plane.
   *
   * @return belonging plane
   */
  public String getPlane() {
    return plane;
  }

  /**
   * Setting belonging plane.
   *
   * @param plane
   *          belonging plane
   */
  public void setPlane(String plane) {
    this.plane = plane;
  }

  /* (Non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Node [nodeId=" + nodeId + ", equipmentTypeId=" + equipmentTypeId + ", nodeState=" + nodeState
        + ", managementIfAddress=" + managementIfAddress + ", loopbackIfAddress=" + loopbackIfAdress
        + ", snmpCommunity=" + snmpCommunity + ", hostName=" + hostName + ", macAddr=" + macAddr + ", ntpServerAddress="
        + ntpServerAddress + ", vpnType=" + vpnType + ", username=" + username + ", password=" + password
        + ", provisioning=" + provisioning + ", plane=" + plane + "]";
  }
}
