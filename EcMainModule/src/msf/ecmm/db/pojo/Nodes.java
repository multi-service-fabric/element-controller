/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;
import java.util.Set;

/**
 * Device Information POJO Class.
 */
public class Nodes implements Serializable {

  /** Device ID. */
  private String node_id = null;
  /** Device Name. */
  private String node_name = null;
  /** Model ID. */
  private String equipment_type_id = null;
  /** Management IF Address. */
  private String management_if_address = null;
  /** SNMP COMMUNITY. */
  private String snmp_community = null;
  /** Device Status. */
  private int node_state = 0;
  /** Device Configuration Necessity Flag. */
  private Boolean provisioning;
  /** Belonging Surface. */
  private Integer plane = null;
  /** VPN Type. */
  private String vpn_type = null;
  /** Loopback IF Address. */
  private String loopback_if_address = null;
  /** User Name. */
  private String username = null;
  /** Password. */
  private String password = null;
  /** NTP Server Address. */
  private String ntp_server_address = null;
  /** Host Name. */
  private String host_name = null;
  /** MAC Address. */
  private String mac_addr = null;
  /** Model Information Table. */
  private Equipments equipments;
  /** Device Start-up Notification Information Table. */
  private Set<NodesStartupNotification> nodesStartupNotificationList;
  /** Physical IF Information Table. */
  private Set<PhysicalIfs> physicalIfsList;
  /** LAG Information Table. */
  private Set<LagIfs> lagIfsList;

  /**
   * Generating new instance.
   */
  public Nodes() {
    super();
  }

  /**
   * Getting device ID.
   *
   * @return devicee ID
   */
  public String getNode_id() {
    return node_id;
  }

  /**
   * Setting device ID.
   *
   * @param node_id
   *          device ID
   */
  public void setNode_id(String node_id) {
    this.node_id = node_id;
  }

  /**
   * Getting device name.
   *
   * @return device name
   */
  public String getNode_name() {
    return node_name;
  }

  /**
   * Setting device name.
   *
   * @param node_name
   *          device name
   */
  public void setNode_name(String node_name) {
    this.node_name = node_name;
  }

  /**
   * Getting model ID.
   *
   * @return model ID
   */
  public String getEquipment_type_id() {
    return equipment_type_id;
  }

  /**
   * Setting model ID.
   *
   * @param equipment_type_id
   *          model ID
   */
  public void setEquipment_type_id(String equipment_type_id) {
    this.equipment_type_id = equipment_type_id;
  }

  /**
   * Getting management IF address.
   *
   * @return management IF address
   */
  public String getManagement_if_address() {
    return management_if_address;
  }

  /**
   * Setting management IF address.
   *
   * @param management_if_address
   *          management IF address
   */
  public void setManagement_if_address(String management_if_address) {
    this.management_if_address = management_if_address;
  }

  /**
   * Getting SNMP COMMUNITY.
   *
   * @return SNMP COMMUNITY
   */
  public String getSnmp_community() {
    return snmp_community;
  }

  /**
   * Setting SNMP COMMUNITY.
   *
   * @param snmp_community
   *          SNMP COMMUNITY
   */
  public void setSnmp_community(String snmp_community) {
    this.snmp_community = snmp_community;
  }

  /**
   * Getting device status.
   *
   * @return device status
   */
  public int getNode_state() {
    return node_state;
  }

  /**
   * Setting device status.
   *
   * @param node_state
   *          device status
   */
  public void setNode_state(int node_state) {
    this.node_state = node_state;
  }

  /**
   * Getting device configuration necessity flag.
   *
   * @return device configuration necessity flag
   */
  public Boolean getProvisioning() {
    return provisioning;
  }

  /**
   * Setting device configuration necessity flag.
   *
   * @param provisioning
   *          device configuration necessity flag
   */
  public void setProvisioning(Boolean provisioning) {
    this.provisioning = provisioning;
  }

  /**
   * Getting belonging surface.
   *
   * @return belonging surface
   */
  public Integer getPlane() {
    return plane;
  }

  /**
   * Setting belonging surface.
   *
   * @param plane
   *          belonging surface
   */
  public void setPlane(Integer plane) {
    this.plane = plane;
  }

  /**
   * Getting VPN type.
   *
   * @return VPN type
   */
  public String getVpn_type() {
    return vpn_type;
  }

  /**
   * Setting VPN type.
   *
   * @param vpn_type
   *          VPN type
   */
  public void setVpn_type(String vpn_type) {
    this.vpn_type = vpn_type;
  }

  /**
   * Getting loopback IF address.
   *
   * @return loopback IF address
   */
  public String getLoopback_if_address() {
    return loopback_if_address;
  }

  /**
   * Setting loopback IF address.
   *
   * @param loopback_if_address
   *          loopback IF address
   */
  public void setLoopback_if_address(String loopback_if_address) {
    this.loopback_if_address = loopback_if_address;
  }

  /**
   * Getting user name.
   *
   * @return user name
   */
  public String getUsername() {
    return username;
  }

  /**
   * Setting user name.
   *
   * @param username
   *          user name
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Getting password.
   *
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Setting password.
   *
   * @param password
   *          password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Getting NTP server address.
   *
   * @return NTP server address
   */
  public String getNtp_server_address() {
    return ntp_server_address;
  }

  /**
   * Setting NTP server address.
   *
   * @param ntp_server_address
   *          NTP server address
   */
  public void setNtp_server_address(String ntp_server_address) {
    this.ntp_server_address = ntp_server_address;
  }

  /**
   * Getting host name.
   *
   * @return host name
   */
  public String getHost_name() {
    return host_name;
  }

  /**
   * Setting host name.
   *
   * @param host_name
   *          host name
   */
  public void setHost_name(String host_name) {
    this.host_name = host_name;
  }

  /**
   * Getting MAC address.
   *
   * @return MAC address
   */
  public String getMac_addr() {
    return mac_addr;
  }

  /**
   * Setting MAC address.
   *
   * @param mac_addr
   *          MAC address
   */
  public void setMac_addr(String mac_addr) {
    this.mac_addr = mac_addr;
  }

  /**
   * Getting model information.
   *
   * @return model information
   */
  public Equipments getEquipments() {
    return equipments;
  }

  /**
   * Setting model information.
   *
   * @param equipments
   *          model information
   */
  public void setEquipments(Equipments equipments) {
    this.equipments = equipments;
  }

  /**
   * Getting device start-up notification information.
   *
   * @return device start-up notification information
   */
  public Set<NodesStartupNotification> getNodesStartupNotificationList() {
    return nodesStartupNotificationList;
  }

  /**
   * Setting device start-up notification information.
   *
   * @param nodesStartupNotificationList
   *          device start-up notification information
   */
  public void setNodesStartupNotificationList(Set<NodesStartupNotification> nodesStartupNotificationList) {
    this.nodesStartupNotificationList = nodesStartupNotificationList;
  }

  /**
   * Getting physical IF information list.
   *
   * @return physical IF information list
   */
  public Set<PhysicalIfs> getPhysicalIfsList() {
    return physicalIfsList;
  }

  /**
   * Setting physical IF information list.
   *
   * @param physicalIfsList
   *          physical IF information list
   */
  public void setPhysicalIfsList(Set<PhysicalIfs> physicalIfsList) {
    this.physicalIfsList = physicalIfsList;
  }

  /**
   * Getting LAG information list.
   *
   * @return LAG information list
   */
  public Set<LagIfs> getLagIfsList() {
    return lagIfsList;
  }

  /**
   * Setting LAG information list.
   *
   * @param lagIfsList
   *          LAG information list
   */
  public void setLagIfsList(Set<LagIfs> lagIfsList) {
    this.lagIfsList = lagIfsList;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int hashCode = 0;
    if (node_id != null) {
      hashCode ^= node_id.hashCode();
    }
    return hashCode;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || this.hashCode() != obj.hashCode()) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    Nodes target = (Nodes) obj;
    if (this.node_id.equals(target.getNode_id())) {
      return true;
    }
    return false;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Nodes [node_id=" + node_id + ", node_name=" + node_name + ", equipment_type_id=" + equipment_type_id
        + ", management_if_address=" + management_if_address + ", snmp_community=" + snmp_community + ", node_state="
        + node_state + ", provisioning=" + provisioning + ", plane=" + plane + ", vpn_type=" + vpn_type
        + ", loopback_if_address=" + loopback_if_address + ", username=" + username + ", password=" + password
        + ", ntp_server_address=" + ntp_server_address + ", host_name=" + host_name + ", mac_addr=" + mac_addr
        + ", equipments=" + equipments + ", nodesStartupNotificationList=" + nodesStartupNotificationList
        + ", physicalIfsList=" + physicalIfsList + ", lagIfsList=" + lagIfsList + "]";
  }
}
