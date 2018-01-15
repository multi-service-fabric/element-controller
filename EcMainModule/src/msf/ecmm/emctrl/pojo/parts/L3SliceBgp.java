/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * BGP Connection Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "bgp")
public class L3SliceBgp {

  /** Corresponding Device Master Flag */
  private String master = null;

  /** AS Number of CE */
  @XmlElement(name = "remote-as-number")
  private Long remoteAsNumber = null;

  /** IPv4 Address for CE */
  @XmlElement(name = "local-address")
  private String localAddress = null;

  /** IPv4 Address for CE */
  @XmlElement(name = "remote-address")
  private String remoteAddress = null;

  /** IPv6 Address for CE */
  @XmlElement(name = "local-address6")
  private String localAddress6 = null;

  /** IPv6 Address for CE */
  @XmlElement(name = "remote-address6")
  private String remoteAddress6 = null;

  /**
   * Generating new instance.
   */
  public L3SliceBgp() {
    super();
  }

  /**
   * Adding the corresponding device master flag.
   */
  public void addMaster() {
    this.master = new String("");
  }

  /**
   * Deleting the corresponding device master flag.
   */
  public void delMaster() {
    this.master = null;
  }

  /**
   * Getting AS number of CE.
   *
   * @return AS number of CE
   */
  public Long getRemoteAsNumber() {
    return remoteAsNumber;
  }

  /**
   * Setting AS number of CE.
   *
   * @param remoteAsNumber
   *          AS number of CE
   */
  public void setRemoteAsNumber(Long remoteAsNumber) {
    this.remoteAsNumber = remoteAsNumber;
  }

  /**
   * Getting IPv4 address for CE.
   *
   * @return IPv4 address for CE
   */
  public String getLocalAddress() {
    return localAddress;
  }

  /**
   * Setting IPv4 address for CE.
   *
   * @param localAddress
   *          IPv4 address for CE
   */
  public void setLocalAddress(String localAddress) {
    this.localAddress = localAddress;
  }

  /**
   * Getting IPv4 address of CE.
   *
   * @return IPv4 address of CE
   */
  public String getRemoteAddress() {
    return remoteAddress;
  }

  /**
   * Setting IPv4 address of CE.
   *
   * @param remoteAddress
   *          IPv4 address of CE
   */
  public void setRemoteAddress(String remoteAddress) {
    this.remoteAddress = remoteAddress;
  }

  /**
   * Getting IPv6 address for CE.
   *
   * @return IPv6 address for CE
   */
  public String getLocalAddress6() {
    return localAddress6;
  }

  /**
   * Setting IPv6 address for CE.
   *
   * @param localAddress6
   *          IPv6 address for CE
   */
  public void setLocalAddress6(String localAddress6) {
    this.localAddress6 = localAddress6;
  }

  /**
   * Getting IPv6 address of CE.
   *
   * @return IPv6 address of CE
   */
  public String getRemoteAddress6() {
    return remoteAddress6;
  }

  /**
   * Setting IPv6 address of CE.
   *
   * @param remoteAddress6
   *          IPv6 address of CE
   */
  public void setRemoteAddress6(String remoteAddress6) {
    this.remoteAddress6 = remoteAddress6;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L3SliceBgp [master=" + master + ", remoteAsNumber=" + remoteAsNumber + ", localAddress=" + localAddress
        + ", remoteAddress=" + remoteAddress + ", localAddress6=" + localAddress6 + ", remoteAddress6=" + remoteAddress6
        + "]";
  }
}
