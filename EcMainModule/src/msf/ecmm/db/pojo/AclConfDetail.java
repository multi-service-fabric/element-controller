/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * ACL configuration details information POJO class.
 */
public class AclConfDetail implements Serializable {

  /** Device ID. */
  private String node_id = null;
  /** ACL configuraiton ID. */
  private String acl_id = null;
  /** term name. */
  private String term_name = null;
  /** action. */
  private String action = null;
  /** direction. */
  private String direction = null;
  /** Transmission source MAC address. */
  private String source_mac_address = null;
  /** Transmission destination  address. */
  private String destination_mac_address = null;
  /** Transmission source IP address. */
  private String source_ip_address = null;
  /** Transmission destination IP address. */
  private String destination_ip_address = null;
  /** Transmission source Port. */
  private int source_port = -1;
  /** Transmission destination Port. */
  private int destination_port = -1;
  /** protocol. */
  private String protocol = null;
  /** ACLpriority value. */
  private int acl_priority = -1;

  /**
   * Generating new instance.
   *
   */
  public AclConfDetail() {
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
   * Setting ACL configuration ID.
   *
   * @param acl_id
   *          ACL configuration ID
   */
  public void setAcl_id(String acl_id) {
    this.acl_id = acl_id;
  }

  /**
   * Getting term name.
   *
   * @return term name
   */
  public String getTerm_name() {
    return term_name;
  }

  /**
   * Setting term name.
   *
   * @param term_name
   *          number
   */
  public void setTerm_name(String term_name) {
    this.term_name = term_name;
  }

  /**
   * Getting action.
   *
   * @return action
   */
  public String getAction() {
    return action;
  }

  /**
   * Setting action.
   *
   * @param action
   *          action
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * Getting direction.
   *
   * @return direction
   */
  public String getDirection() {
    return direction;
  }

  /**
   * Setting direction.
   *
   * @param direction
   *          direction
   */
  public void setDirection(String direction) {
    this.direction = direction;
  }

  /**
   * Getting transmission source MAC address.
   *
   * @return Transmission source MAC address
   */
  public String getSource_mac_address() {
    return source_mac_address;
  }

  /**
   * Setting transmission source MAC address.
   *
   * @param source_mac_address
   *          Transmission source MAC address
   */
  public void setSource_mac_address(String source_mac_address) {
    this.source_mac_address = source_mac_address;
  }

  /**
   * Getting transmission destination address.
   *
   * @return Transmission destination address
   */
  public String getDestination_mac_address() {
    return destination_mac_address;
  }

  /**
   * Setting transmission destination address.
   *
   * @param destination_mac_address
   *          Transmission destination  address
   */
  public void setDestination_mac_address(String destination_mac_address) {
    this.destination_mac_address = destination_mac_address;
  }

  /**
   * Getting transmission source IP address.
   *
   * @return Transmission source IP address
   */
  public String getSource_ip_address() {
    return source_ip_address;
  }

  /**
   * Setting transmission source IP address.
   *
   * @param source_ip_address
   *          Transmission source IP address
   */
  public void setSource_ip_address(String source_ip_address) {
    this.source_ip_address = source_ip_address;
  }

  /**
   * Getting transmission destination IP address.
   *
   * @return Transmission destination IP address
   */
  public String getDestination_ip_address() {
    return destination_ip_address;
  }

  /**
   * Setting transmission destination IP address.
   *
   * @param destination_ip_address
   *          Transmission destination IP address
   */
  public void setDestination_ip_address(String destination_ip_address) {
    this.destination_ip_address = destination_ip_address;
  }

  /**
   * Getting tansmission source port.
   *
   * @return Transmission source port
   */
  public int getSource_port() {
    return source_port;
  }

  /**
   * Setting transmission source port.
   *
   * @param source_port
   *          Transmission source port
   */
  public void setSource_port(int source_port) {
    this.source_port = source_port;
  }

  /**
   * Getting transmission destination port.
   *
   * @return Transmission destination port
   */
  public int getDestination_port() {
    return destination_port;
  }

  /**
   * Setting transmission destination port.
   *
   * @param destination_port
   *          Transmission destination port
   */
  public void setDestination_port(int destination_port) {
    this.destination_port = destination_port;
  }

  /**
   * Getting protocol.
   *
   * @return Protocol
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * Setting protocol.
   *
   * @param protocol
   *          Protocol
   */
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  /**
   * Getting ACL priority value.
   *
   * @return ACLpriority value
   */
  public int getAcl_priority() {
    return acl_priority;
  }

  /**
   * Set ACL priority value.
   *
   * @param acl_priority
   *          ACL priority value
   */
  public void setAcl_priority(int acl_priority) {
    this.acl_priority = acl_priority;
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
    if (term_name != null) {
      hashCode ^= term_name.hashCode();
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

    AclConfDetail target = (AclConfDetail) obj;
    if ((this.node_id != null) && this.node_id.equals(target.getNode_id()) && (this.acl_id != null)
        && this.acl_id.equals(target.getAcl_id()) && (this.term_name != null)
        && this.term_name.equals(target.getTerm_name())) {
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
    return "AclConfDetail [node_id=" + node_id + ", acl_id=" + acl_id + ", term_name=" + term_name + ", action="
        + action + ", direction=" + direction + ", source_mac_address=" + source_mac_address
        + ", destination_mac_address=" + destination_mac_address + ", source_ip_address=" + source_ip_address
        + ", destination_ip_address=" + destination_ip_address + ", source_port=" + source_port + ", protocol="
        + protocol + ", acl_priority=" + acl_priority + ", direction=" + direction + "]";
  }

}
