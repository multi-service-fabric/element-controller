/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Neighbor's IP Address Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "neighbor")
public class L2L3VpnNeighbor {

  /** Neighbor's IPv4 Address */
  private String address;

  /**
   * Generating new instance.
   */
  public L2L3VpnNeighbor() {
    super();
  }

  /**
   * Getting neighbor's IPv4 address.
   *
   * @return neighbor's IPv4 address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting neighbor's IPv4 address
   *
   * @param address
   *          neighbor's IPv4 address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L3VpnNeighbor [address=" + address + "]";
  }
}
