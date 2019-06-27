/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Configurable Device Type.
 */
public class Capabilities {

  /** L2VPN Configurability. */
  private Boolean l2vpn = null;

  /** L3VPN Configurability. */
  private Boolean l3vpn = null;

  /** EVPN Configurability. */
  private Boolean evpn = null;

  /** IRB Configurability. */
  private IrbCapabilities irb = null;

  /** Q-in-Q Configurability.. */
  private QInQCapabilities qInQ = null;

  /**
   * Getting L2VPN configurability.
   *
   * @return L2VPN configurability
   */
  public Boolean isL2vpn() {
    return l2vpn;
  }

  /**
   * Setting L2VPN configurability.
   *
   * @param l2vpn
   *          L2VPN configurability
   */
  public void setL2vpn(Boolean l2vpn) {
    this.l2vpn = l2vpn;
  }

  /**
   * Getting L3VPN configurability.
   *
   * @return L3VPN configurability
   */
  public Boolean isL3vpn() {
    return l3vpn;
  }

  /**
   * Setting L3VPN configurability.
   *
   * @param l3vpn
   *          L3VPN configurability
   */
  public void setL3vpn(Boolean l3vpn) {
    this.l3vpn = l3vpn;
  }

  /**
   * Getting EVPN configurability.
   *
   * @return L3VPN configurability
   */
  public Boolean isEvpn() {
    return evpn;
  }

  /**
   * Setting L3VPN configurability.
   *
   * @param evpn
   *          L3VPN configurability
   */
  public void setEvpn(Boolean evpn) {
    this.evpn = evpn;
  }

  /**
   * Getting IRB configurability.
   *
   * @return configurability
   */
  public IrbCapabilities getIrb() {
    return irb;
  }

  /**
   * Setting IRB configurability.
   *
   * @param irb
   *          IRB configurability
   */
  public void setIrb(IrbCapabilities irb) {
    this.irb = irb;
  }

  /**
   * Getting Q-in-Q configurability.
   *
   * @return Q-in-Q configurability
   */
  public QInQCapabilities getqInQ() {
    return qInQ;
  }

  /**
   * Setting Q-in-Q configurability.
   *
   * @param qInQ
   *          Q-in-Q configurability
   */
  public void setqInQ(QInQCapabilities qInQ) {
    this.qInQ = qInQ;
  }

  /**
   * Stringizing instance.
   *
   * @return Stringizing instance
   */
  @Override
  public String toString() {
    return "Capabilities [l2vpn=" + l2vpn + ", l3vpn=" + l3vpn + ", evpn=" + evpn + ", irb=" + irb + ", qInQ=" + qInQ
        + "]";
  }
}
