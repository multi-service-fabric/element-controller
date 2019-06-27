/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;
import java.util.Set;

/**
 * ACL configuration information POJO class.
 */
public class AclConf implements Serializable {

  /** Device ID. */
  private String node_id = null;
  /** ACL configuration ID. */
  private String acl_id = null;
  /** Physical IF ID. */
  private String physical_if_id = null;
  /** LagIFID. */
  private String lag_if_id = null;
  /** breakoutIF ID. */
  private String breakout_if_id = null;
  /** VLAN IF ID. */
  private String vlan_if_id = null;
  /** ACL configuration details information list. */
  private Set<AclConfDetail> aclConfDetailList;

  /**
   * Generating new instance.
   */
  public AclConf() {
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
   * Setting Device ID.
   *
   * @param node_id
   *          Device ID
   */
  public void setNode_id(String node_id) {
    this.node_id = node_id;
  }

  /**
   * Getting ACL configuration ID.
   *
   * @return ACL configuration ID
   */
  public String getAcl_id() {
    return acl_id;
  }

  /**
   * Setting ACL configuraiton ID.
   *
   * @param acl_id
   *          ACL configuration ID
   */
  public void setAcl_id(String acl_id) {
    this.acl_id = acl_id;
  }

  /**
   * Getting physical IF ID.
   *
   * @return physical IF ID
   */
  public String getPhysical_if_id() {
    return physical_if_id;
  }

  /**
   * Setting Physical IF ID.
   *
   * @param physical_if_id
   *          Physical IF ID
   */
  public void setPhysical_if_id(String physical_if_id) {
    this.physical_if_id = physical_if_id;
  }

  /**
   * Getting LagIFID.
   *
   * @return LagIFID
   */
  public String getLag_if_id() {
    return lag_if_id;
  }

  /**
   * Setting LagIFID.
   *
   * @param lag_if_id
   *          LagIFID
   */
  public void setLag_if_id(String lag_if_id) {
    this.lag_if_id = lag_if_id;
  }

  /**
   * Getting breakoutIF ID.
   *
   * @return breakoutIF ID
   */
  public String getBreakout_if_id() {
    return breakout_if_id;
  }

  /**
   * Setting breakoutIF ID.
   *
   * @param breakout_if_id
   *          breakoutIF ID
   */
  public void setBreakout_if_id(String breakout_if_id) {
    this.breakout_if_id = breakout_if_id;
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
   * Setting VLAN IF ID.
   *
   * @param vlan_if_id
   *          VLAN IF ID
   */
  public void setVlan_if_id(String vlan_if_id) {
    this.vlan_if_id = vlan_if_id;
  }

  /**
   * Getting a list of ACL configuraiton details information.
   *
   * @return list of ACL configuration details information
   */
  public Set<AclConfDetail> getAclConfDetailList() {
    return aclConfDetailList;
  }

  /**
   * Setting list of ACL configuration details information.
   *
   * @param aclConfDetailList
   *          ACL configuration details information list
   */
  public void setAclConfDetailList(Set<AclConfDetail> aclConfDetailList) {
    this.aclConfDetailList = aclConfDetailList;
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
    if (acl_id != null) {
      hashCode ^= acl_id.hashCode();
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

    AclConf target = (AclConf) obj;
    if ((this.node_id != null) && this.node_id.equals(target.getNode_id()) && (this.acl_id != null)
        && this.acl_id.equals(target.getAcl_id())) {
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
    return "AclConf [node_id=" + node_id + ", acl_id=" + acl_id + ", physical_if_id=" + physical_if_id + ", lag_if_id="
        + lag_if_id + ", breakout_if_id=" + breakout_if_id + ", vlan_if_id=" + vlan_if_id + ", aclConfDetailList="
        + aclConfDetailList + "]";
  }

}
