/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * AnyCast setting information class.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "anycast")
public class Anycast {

  /** anycast-IP address. */
  private String address = null;

  /**
   * Acquiring anycast-IP address.
   *
   * @return anycast-IP address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting anycast-IP address.
   *
   * @param address
   *          IP address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Anycast [address=" + address + "]";
  }

}
