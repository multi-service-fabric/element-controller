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
@XmlRootElement(name = "management-interface")
public class ManagementInterface {

  /** IPv4 Address of Management IF with Net Mask */
  private String address = null;

  /** IPv4 Address of Management IF with Net Mask */
  private Integer prefix = null;

  /**
   * Generating new instance.
   */
  public ManagementInterface() {
    super();
  }

  /**
   * Getting IPv4 address of management IF with net mask.
   *
   * @return IPv4 address of management IF with net mask
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting IPv4 address of management IF with net mask.
   *
   * @param address
   *          IPv4 address of management IF with net mask
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting IPv4 address of management IF with net mask.
   *
   * @return IPv4 address of management IF with net mask
   */
  public Integer getPrefix() {
    return prefix;
  }

  /**
   * Setting IPv4 address of management IF with net mask
   *
   * @param prefix
   *          IPv4 address of management IF with net mask
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
    return "ManagementInterface [address=" + address + ", prefix=" + prefix + "]";
  }

}
