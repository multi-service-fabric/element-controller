/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * IRB virtual gateway information class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "virtual-gateway")
public class VirtualGateway {

  /** IP address configuration. */
  private String address = null;

  /** Prefix configuraiton. */
  private Long prefix = null;

  /**
   * Getting IP address.
   *
   * @return IP address 
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting IP address.
   *
   * @param address
   *          IP address 
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting prefix.
   *
   * @return prefix
   */
  public Long getPrefix() {
    return prefix;
  }

  /**
   * Setting prefix.
   *
   * @param prefix
   *          Prefix
   */
  public void setPrefix(Long prefix) {
    this.prefix = prefix;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "VirtualGateway [address=" + address + ", prefix=" + prefix + "]";
  }

}
