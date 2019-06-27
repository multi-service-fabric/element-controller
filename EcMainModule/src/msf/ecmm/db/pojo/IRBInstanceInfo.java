/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * IRB instance information POJO class.
 */
public class IRBInstanceInfo implements Serializable {

  /** DeviceID. */
  private String node_id = null;
  /** VLAN ID. */
  private String vlan_id = null;
  /** IRB instance ID. */
  private String irb_instance_id = null;
  /** VNI for IRB setting. */
  private String irb_vni = null;
  /** IPv4 address for IRB setting. */
  private String irb_ipv4_address = null;
  /** IPv4 address prefix for IRB setting. */
  private Integer irb_ipv4_prefix = null;
  /** Virtual gateway address. */
  private String virtual_gateway_address = null;

  /**
   * Generate new instance.
   */
  public IRBInstanceInfo() {
    super();
  }

  /**
   * Getting Device ID.
   *
   * @return Device ID
   */
  public String getNode_id() {
    return node_id;
  }

  /**
   * Set Device ID.
   *
   * @param node_id
   *          DeviceID
   */
  public void setNode_id(String node_id) {
    this.node_id = node_id;
  }

  /**
   * Getting VLAN ID.
   *
   * @return VLAN ID
   */
  public String getVlan_id() {
    return vlan_id;
  }

  /**
   * Set VLAN ID.
   *
   * @param vlan_id
   *          VLAN ID
   */
  public void setVlan_id(String vlan_id) {
    this.vlan_id = vlan_id;
  }

  /**
   * Getting IRB instance ID.
   *
   * @return IRBinstanceID
   */
  public String getIrb_instance_id() {
    return irb_instance_id;
  }

  /**
   * Set IRB instance ID.
   *
   * @param irb_instance_id
   *          IRBinstanceID
   */
  public void setIrb_instance_id(String irb_instance_id) {
    this.irb_instance_id = irb_instance_id;
  }

  /**
   * Getting VNI for IRB setting.
   *
   * @return VNI for IRB setting
   */
  public String getIrb_vni() {
    return irb_vni;
  }

  /**
   * Set VNI for IRB setting.
   *
   * @param irb_vni
   *          VNI for IRB setting
   */
  public void setIrb_vni(String irb_vni) {
    this.irb_vni = irb_vni;
  }

  /**
   * Getting IPv4 address for IRB setting.
   *
   * @return IPv4 address for IRB setting
   */
  public String getIrb_ipv4_address() {
    return irb_ipv4_address;
  }

  /**
   * Setting IPv4 address for IRB setting.
   *
   * @param irb_ipv4_address
   *          IPv4 address for IRB setting
   */
  public void setIrb_ipv4_address(String irb_ipv4_address) {
    this.irb_ipv4_address = irb_ipv4_address;
  }

  /**
   * Getting IPv4 address prefix for IRB setting.
   *
   * @return IPv4 address prefix for IRB setting
   */
  public Integer getIrb_ipv4_prefix() {
    return irb_ipv4_prefix;
  }

  /**
   * Setting IPv4 address prefix for IRB setting.
   *
   * @param irb_ipv4_prefix
   *          IPv4 address prefix for IRB setting
   */
  public void setIrb_ipv4_prefix(Integer irb_ipv4_prefix) {
    this.irb_ipv4_prefix = irb_ipv4_prefix;
  }

  /**
   * Getting virtual gateway address.
   *
   * @return Virtual gateway address
   */
  public String getVirtual_gateway_address() {
    return virtual_gateway_address;
  }

  /**
   * Setting virtual gateway address.
   *
   * @param virtual_gateway_address
   *          Virtual gateway address
   */
  public void setVirtual_gateway_address(String virtual_gateway_address) {
    this.virtual_gateway_address = virtual_gateway_address;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public synchronized int hashCode() {
    int hashCode = 0;

    if (node_id != null) {
      hashCode ^= node_id.hashCode();
    }
    if (vlan_id != null) {
      hashCode ^= vlan_id.hashCode();
    }
    if (irb_instance_id != null) {
      hashCode ^= irb_instance_id.hashCode();
    }

    return hashCode;
  }

  /*
   * (Non Javadoc)
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

    IRBInstanceInfo target = (IRBInstanceInfo) obj;
    if ((this.node_id != null) && this.node_id.equals(target.getNode_id()) && (this.vlan_id != null)
        && this.vlan_id.equals(target.getVlan_id()) && (this.irb_instance_id != null)
        && this.irb_instance_id.equals(target.getIrb_instance_id())) {
      return true;
    }
    return false;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "IRBInstanceInfo [node_id=" + node_id + ", vlan_id=" + vlan_id + ", irb_instance_id=" + irb_instance_id
        + ", irb_vni=" + irb_vni + ", irb_ipv4_address=" + irb_ipv4_address + ", irb_ipv4_prefix=" + irb_ipv4_prefix
        + ", virtual_gateway_address=" + virtual_gateway_address + "]";
  }
}
