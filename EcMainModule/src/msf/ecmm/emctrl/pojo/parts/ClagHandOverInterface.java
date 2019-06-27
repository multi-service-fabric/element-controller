/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clag Bridge IF IP address setting class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "interface")
public class ClagHandOverInterface {

  /** Clag Bridge IF IP address setting. */
  private String address = null;

  /** Prefix setting. */
  private Long prefix = null;

  /**
   * Acquiring IP address.
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
   * Acquiring prefix.
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
   *          prefix
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
    return "ClagHandOverInterface [address=" + address + ", prefix=" + prefix + "]";
  }
}
