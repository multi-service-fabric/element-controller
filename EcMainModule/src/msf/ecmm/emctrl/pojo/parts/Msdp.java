/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Spin Extention-MSDP Basic Configuration-MSDP Configuration Infomation List Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Msdp {

  /** MSDP Configuration Information */
  @XmlElement(name = "peer")
  private MsdpPeer msdpPeer = null;

  /**
   * Generating new instance.
   */
  public Msdp() {
    super();
  }

  /**
   * Getting MSDP configuration information.
   *
   * @return MSDP configuration information
   */
  public MsdpPeer getMsdpPeer() {
    return msdpPeer;
  }

  /**
   * Setting MSDP configuration information.
   *
   * @param msdpPeer
   *          MSDP configuration information
   */
  public void setMsdpPeer(MsdpPeer msdpPeer) {
    this.msdpPeer = msdpPeer;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Msdp [msdpPeer=" + msdpPeer + "]";
  }
}
