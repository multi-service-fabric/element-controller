/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * Dummy VLAN IF information POJO class.
 */
public class DummyVlanIfs implements Serializable {

  /** DeviceID. */
  private String node_id = null;
  /** VLAN IF ID. */
  private String vlan_if_id = null;
  /** VLAN ID. */
  private String vlan_id = null;
  /** IRB instance ID. */
  private String irb_instance_id = null;

  /**
   * Generate new instance.
   */
  public DummyVlanIfs() {
    super();
  }

  /**
   * Getting Device ID.
   *
   * @return DeviceID
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
   * Getting VLAN IF ID.
   *
   * @return VLAN IF ID
   */
  public String getVlan_if_id() {
    return vlan_if_id;
  }

  /**
   * Set VLAN IF ID.
   *
   * @param vlan_if_id
   *          VLAN IF ID
   */
  public void setVlan_if_id(String vlan_if_id) {
    this.vlan_if_id = vlan_if_id;
  }

  /**
   * Getting VLAN ID.
   *
   * @return /** VLAN ID
   */
  public String getVlan_id() {
    return vlan_id;
  }

  /**
   * Set VLAN ID.
   *
   * @param vlan_id
   *          /** VLAN ID
   */
  public void setVlan_id(String vlan_id) {
    this.vlan_id = vlan_id;
  }

  /**
   * Getting IRB instance ID.
   *
   * @return IRB instance ID
   */
  public String getIrb_instance_id() {
    return irb_instance_id;
  }

  /**
   * Set IRB instance ID.
   *
   * @param irb_instance_id
   *          IRB instance ID
   */
  public void setIrb_instance_id(String irb_instance_id) {
    this.irb_instance_id = irb_instance_id;
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
    if (vlan_if_id != null) {
      hashCode ^= vlan_if_id.hashCode();
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

    DummyVlanIfs target = (DummyVlanIfs) obj;
    if ((this.node_id != null) && this.node_id.equals(target.getNode_id()) && (this.vlan_if_id != null)
        && this.vlan_if_id.equals(target.getVlan_if_id())) {
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
    return "DummyVlanIfs [node_id=" + node_id + ", vlan_if_id=" + vlan_if_id + ", vlan_id=" + vlan_id
        + ", irb_instance_id=" + irb_instance_id + "]";
  }
}
