/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * IRB instance configuraiton information class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Irb {
  /** IP address configuration. */
  @XmlElement(name = "physical-ip-address")
  private PhysicalIpAddress physicalIpAddress = null;

  /** virtual-mac-address configuration. */
  @XmlElement(name = "virtual-mac-address")
  private String virtualMacAddress = null;

  /** virtual-gateway information. */
  @XmlElement(name = "virtual-gateway")
  private VirtualGateway virtualGateway = null;

  /**
   * Acquiring IP address configuration.
   *
   * @return IP address configuration
   */
  public PhysicalIpAddress getPhysicalIpAddress() {
    return physicalIpAddress;
  }

  /**
   * Setting IP address.
   *
   * @param physicalIpAddress
   *          IP address configuraiton
   */
  public void setPhysicalIpAddress(PhysicalIpAddress physicalIpAddress) {
    this.physicalIpAddress = physicalIpAddress;
  }

  /**
   * Acquiring virtual MAC address configuraition.
   *
   * @return virtualMacAddress
   */
  public String getVirtualMacAddress() {
    return virtualMacAddress;
  }

  /**
   * Setting virtual MAC address.
   *
   * @param virtualMacAddress
   *          Virtual MAC address
   */
  public void setVirtualMacAddress(String virtualMacAddress) {
    this.virtualMacAddress = virtualMacAddress;
  }

  /**
   * Getting Virtual Gateway configuraiton.
   *
   * @return virtual gateway configuration
   */
  public VirtualGateway getVirtualGateway() {
    return virtualGateway;
  }

  /**
   * Setting virtual gateway.
   *
   * @param virtualGateway
   *          Virtual gateway configuraiton
   */
  public void setVirtualGateway(VirtualGateway virtualGateway) {
    this.virtualGateway = virtualGateway;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Irb [physicalIpAddress=" + physicalIpAddress + ", virtualMacAddress=" + virtualMacAddress
        + ", virtualGateway=" + virtualGateway + "]";
  }

}
