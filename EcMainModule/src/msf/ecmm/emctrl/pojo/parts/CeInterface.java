/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * CP Configuration Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ce-interface")
public class CeInterface {

  /** IPv4 Address for CE */
  private String address = null;

  /** IPv4 Net Mask for CE */
  private Integer prefix = null;

  /** IPv6 Address for CE */
  private String address6 = null;

  /** IPv6 Net Mask for CE */
  private Integer prefix6 = null;

  /** MTU Value */
  private Long mtu = null;

  /**
   * Generating new instance.
   */
  public CeInterface() {
    super();
  }

  /**
   * Getting IPv4 address for CE.
   *
   * @return IPv4 address for CE
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting IPv4 address for CE.
   *
   * @param address
   *          IPv4 address for CE
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting IPv4 net mask for CE.
   *
   * @return IPv4 net mask for CE
   */
  public Integer getPrefix() {
    return prefix;
  }

  /**
   * Setting IPv4 net mask for CE.
   *
   * @param prefix
   *          IPv4 net mask for CE
   */
  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  /**
   * Getting IPv6 address for CE.
   *
   * @return IPv6 address for CE
   */
  public String getAddress6() {
    return address6;
  }

  /**
   * Setting IPv6 address for CE.
   *
   * @param address6
   *          IPv6 address for CE
   */
  public void setAddress6(String address6) {
    this.address6 = address6;
  }

  /**
   * Getting IPv6 net mask for CE.
   *
   * @return IPv6 net mask for CE
   */
  public Integer getPrefix6() {
    return prefix6;
  }

  /**
   * Setting IPv6 net mask for CE.
   *
   * @param prefix6
   *          IPv6 net mask for CE
   */
  public void setPrefix6(Integer prefix6) {
    this.prefix6 = prefix6;
  }

  /**
   * Getting MTU value.
   *
   * @return MTU value
   */
  public Long getMtu() {
    return mtu;
  }

  /**
   * Setting MTU value.
   *
   * @param mtu
   *          MTU value
   */
  public void setMtu(Long mtu) {
    this.mtu = mtu;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L3SliceCeInterface [address=" + address + ", prefix=" + prefix + ", address6=" + address6 + ", prefix6="
        + prefix6 + ", mtu=" + mtu + "]";
  }
}
