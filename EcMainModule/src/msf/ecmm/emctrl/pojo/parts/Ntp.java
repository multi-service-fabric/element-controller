/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Spin Extention-NTP Configuration Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Ntp {

  /** IPv4 Address of NTP Server */
  @XmlElement(name = "server-address")
  private String serverAdddress = null;

  /**
   * Generating new instance.
   */
  public Ntp() {
    super();
  }

  /**
   * Getting IPv4 address of NTP server.
   *
   * @return IPv4 address of NTP server
   */
  public String getServerAdddress() {
    return serverAdddress;
  }

  /**
   * Setting IPv4 address of NTP server.
   *
   * @param serverAdddress
   *          IPv4 address of NTP server
   */
  public void setServerAdddress(String serverAdddress) {
    this.serverAdddress = serverAdddress;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Ntp [serverAdddress=" + serverAdddress + "]";
  }

}
