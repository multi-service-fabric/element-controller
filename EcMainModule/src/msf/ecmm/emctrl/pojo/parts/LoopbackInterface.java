/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Spin Extention-SW Common-Loopback IF Configuration Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "loopback-interface")
public class LoopbackInterface {

  /** IPv4 Address of Loopback IF with Net Mask */
  private String address = "";

  /** IPv4 Address of Loopback IF with Net Mask */
  private Integer prefix = null;

  /**
   * Generating new instance.
   */
  public LoopbackInterface() {
    super();
  }

  /**
   * Getting IPv4 address of loopback IF with net mask.
   *
   * @return IPv4 address of loopback IF with net mask
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting IPv4 address of loopback IF with net mask.
   *
   * @param address
   *          IPv4 address of loopback IF with net mask
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting IPv4 address of loopback IF with net mask.
   *
   * @return IPv4 address of loopback IF with net mask
   */
  public Integer getPrefix() {
    return prefix;
  }

  /**
   * Setting IPv4 address of loopback IF with net mask.
   *
   * @param prefix
   *          IPv4 address of loopback IF with net mask
   */
  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "SpineLoopbackInterface [address=" + address + ", prefix=" + prefix + "]";
  }
}
