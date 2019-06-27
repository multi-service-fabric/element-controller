/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
  * ACL configuration information details.
  */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "term")
public class Term {
  /** Attribute data for ACL configuraiton information details. */
  @XmlAttribute
  String operation = null;

  /** Identification name of ACL configuration information details. */
  @XmlElement(name = "term_name")
  private String termName = null;

  /** Name of IF for CE.  LAG-IF only for EVPN support.*/
  private String name = null;

  /** VLAN ID. To be omitted in case of setting to Base IF.*/
  @XmlElement(name = "vlan-id")
  private Long vlanId = null;

  /** Operation Type. Setting as discard this time. */
  private String action = null;

  /** Specifing the direction of filter. Setting as in this time. */
  private String direction = null;

  /** Transmissin source MAC address. */
  @XmlElement(name = "source-mac-address")
  private String sourceMacAddress = null;

  /** Transmission destination MAC address. */
  @XmlElement(name = "destination-mac-address")
  private String destinationMacAddress = null;

  /** Transmission source port. */
  @XmlElement(name = "source-port")
  private Long sourcePort = null;

  /** Transission destination port. */
  @XmlElement(name = "destination-port")
  private Long destinationPort = null;

  /** Transnission source IP address. */
  @XmlElement(name = "source-ip-address")
  private String sourceIpAddress = null;

  /** Transission destination IP address. */
  @XmlElement(name = "destination-ip-address")
  private String destinationIpAddress = null;

  /** Protocol type. */
  private String protocol = null;

  /** Priority. */
  private Long priority = null;


  /**
   * Getting the identification name of ACL configuraiton information details.
   *
   * @return termName
   */
  public String getTermName() {
    return termName;
  }

  /**
   * Setting the identification name of ACL configuration information details.
   *
   * @param termName
   *          setting termName
   */
  public void setTermName(String termName) {
    this.termName = termName;
  }

  /**
   * Getting name of IF for CE.
   *
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Setting name of IF for CE.
   *
   * @param name
   *          set name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting VLAN ID.
   *
   * @return vlanId
   */
  public Long getVlanId() {
    return vlanId;
  }

  /**
   * Setting VLAN ID.
   *
   * @param vlanId
   *          set Long vlanId
   */
  public void setVlanId(Long vlanId) {
    this.vlanId = vlanId;
  }

  /**
   * Getting operation type.
   *
   * @return action
   */
  public String getAction() {
    return action;
  }

  /**
   * Setting operation type.
   *
   * @param action
   *          setting action
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * Getting the direction of filter.
   *
   * @return direction
   */
  public String getDirection() {
    return direction;
  }

  /**
   * Setting the direction of filter.
   *
   * @param direction
   *          setting direction
   */
  public void setDirection(String direction) {
    this.direction = direction;
  }

  /**
   * Getting the transmission source.
   *
   * @return sourceMacAddress
   */
  public String getSourceMacAddress() {
    return sourceMacAddress;
  }

  /**
   * Setting the transmission source MAC.
   *
   * @param sourceMacAddress
   *          setting sourceMacAddress
   */
  public void setSourceMacAddress(String sourceMacAddress) {
    this.sourceMacAddress = sourceMacAddress;
  }

  /**
   * Getting the transmission source MAC address.
   *
   * @return destinationMacAddress
   */
  public String getDestinationMacAddress() {
    return destinationMacAddress;
  }

  /**
   * Setting the transmission destination MAC address.
   *
   * @param destinationMacAddress
   *          set destinationMacAddress
   */
  public void setDestinationMacAddress(String destinationMacAddress) {
    this.destinationMacAddress = destinationMacAddress;
  }

  /**
   * Getting the transmission source port.
   *
   * @return sourcePort
   */
  public Long getSourcePort() {
    return sourcePort;
  }

  /**
   * Setting transmission source port.
   *
   * @param sourcePort
   *          set sourcePort
   */
  public void setSourcePort(Long sourcePort) {
    this.sourcePort = sourcePort;
  }

  /**
   * Getting transimssion destination port.
   *
   * @return destinationPort
   */
  public Long getDestinationPort() {
    return destinationPort;
  }

  /**
   * Setting transmission destination port.
   *
   * @param destinationPort
   *          set destinationPort
   */
  public void setDestinationPort(Long destinationPort) {
    this.destinationPort = destinationPort;
  }

  /**
   * Getting transmission source IP address.
   *
   * @return sourceIpAddress
   */
  public String getSourceIpAddress() {
    return sourceIpAddress;
  }

  /**
   * Setting transmission source IP address.
   *
   * @param sourceIpAddress
   *          set sourceIpAddress
   */
  public void setSourceIpAddress(String sourceIpAddress) {
    this.sourceIpAddress = sourceIpAddress;
  }

  /**
   * Getting the transmission destination IP address.
   *
   * @return destinationIpAddress
   */
  public String getDestinationIpAddress() {
    return destinationIpAddress;
  }

  /**
   * Setting transmission destination IP address.
   *
   * @param destinationIpAddress
   *          set destinationIpAddress
   */
  public void setDestinationIpAddress(String destinationIpAddress) {
    this.destinationIpAddress = destinationIpAddress;
  }

  /**
   * Getting protocol type.
   *
   * @return protocol
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * Setting protocol type.
   *
   * @param protocol
   *          set protocol
   */
  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  /**
   * Getting priority.
   *
   * @return priority
   */
  public Long getPriority() {
    return priority;
  }

  /**
   * Setting priority.
   *
   * @param priority
   *          set priority
   */
  public void setPriority(Long priority) {
    this.priority = priority;
  }

  /**
   * Getting attribute data.
   *
   * @return operation
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting attribute data.
   *
   * @param operation
   *          set operation
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  @Override
  public String toString() {
    return "Term [operation=" + operation + ", termName=" + termName + ", name=" + name + ", vlanId=" + vlanId
        + ", action=" + action + ", direction=" + direction + ", sourceMacAddress=" + sourceMacAddress
        + ", destinationMacAddress=" + destinationMacAddress + ", sourcePort=" + sourcePort + ", destinationPort="
        + destinationPort + ", sourceIpAddress=" + sourceIpAddress + ", destinationIpAddress=" + destinationIpAddress
        + ", protocol=" + protocol + ", priority=" + priority + "]";
  }
}
