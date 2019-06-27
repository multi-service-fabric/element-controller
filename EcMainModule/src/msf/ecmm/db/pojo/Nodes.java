/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
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
  /** Management IF Address prefix. */
  private int mng_if_addr_prefix = 0;
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
  /** AS number. */
  private String as_number = null;
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
  /** IRB configuration type. */
  private String irb_type = null;
  /** Q-in-Q type. */
  private String q_in_q_type = null;
  /** Model information table. */
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
   * Getting management IF address prefix.
   *
   * @return mng_if_addr_prefix
   */
  public int getMng_if_addr_prefix() {
    return mng_if_addr_prefix;
  }

  /**
   * Setting management IF address prefix.
   *
   * @param mng_if_addr_prefix
   *          mng_if_addr_prefix to be set
   */
  public void setMng_if_addr_prefix(int mng_if_addr_prefix) {
    this.mng_if_addr_prefix = mng_if_addr_prefix;
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
   * Getting AS number.
   *
   * @return as_number
   */
  public String getAs_number() {
    return as_number;
  }

  /**
   * Setting AS number.
   *
   * @param as_number
   *          Set as_number
   */
  public void setAs_number(String as_number) {
    this.as_number = as_number;
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
   * Getting IRB configuration type.
   *
   * @return IRB configuration type
   */
  public String getIrb_type() {
    return irb_type;
  }

  /**
   * Set IRB configuration type.
   *
   * @param irb_type
   *          IRB configuration type
   */
  public void setIrb_type(String irb_type) {
    this.irb_type = irb_type;
  }


  /**
   * Getting Q-in-Q type.
   *
   * @return Q-in-Q type
   */
  public String getQ_in_q_type() {
    return q_in_q_type;
  }

  /**
   * Setting Q-in-Q type.
   *
   *
   * @param q_in_q_type
   *          Q-in-Q type
   */
  public void setQ_in_q_type(String q_in_q_type) {
    this.q_in_q_type = q_in_q_type;
  }

  /**
   * Acquire model information.
   *
   * @return model information
   */
  public Equipments getEquipments() {
    return equipments;
  }

  /**
   * Set model information.
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
   * Stringize instance
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Nodes [node_id=" + node_id + ", node_name=" + node_name + ", equipment_type_id=" + equipment_type_id
        + ", management_if_address=" + management_if_address + ", mng_if_addr_prefix=" + mng_if_addr_prefix
        + ", snmp_community=" + snmp_community + ", node_state=" + node_state + ", provisioning=" + provisioning
        + ", plane=" + plane + ", vpn_type=" + vpn_type + ", as_number=" + as_number + ", loopback_if_address="
        + loopback_if_address + ", username=" + username + ", password=" + password + ", ntp_server_address="
        + ntp_server_address + ", host_name=" + host_name + ", mac_addr=" + mac_addr + ", irb_type=" + irb_type
        + ", q_in_q_type=" + q_in_q_type + ", equipments=" + equipments + ", nodesStartupNotificationList="
        + nodesStartupNotificationList + ", physicalIfsList=" + physicalIfsList + ", lagIfsList=" + lagIfsList + "]";
  }

}
