/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Spin Extention-MSDP Basic Configuration-MSDP Configuration Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "l2-vpn")
public class MsdpPeer {

  /** MSDP Peer Address Designation */
  private String address = null;

  /** MSDP Local Address Designation */
  @XmlElement(name = "local-address")
  private String localAddress = null;

  /**
   * Generating new instance.
   */
  public MsdpPeer() {
    super();
  }

  /**
   * Getting MSDP peer address designation.
   *
   * @return MSDP peer address designation
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting MSDP peer address designation.
   *
   * @param address
   *          MSDP peer address designation
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting MSDP local address designation.
   *
   * @return MSDP local address designation
   */
  public String getLocalAddress() {
    return localAddress;
  }

  /**
   * Setting MSDP local address designation.
   *
   * @param localAddress
   *          MSDP local address designation
   */
  public void setLocalAddress(String localAddress) {
    this.localAddress = localAddress;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "MsdpPeer [address=" + address + ", localAddress=" + localAddress + "]";
  }
}
