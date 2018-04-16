/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Information for each Interface
 */
public class InterfaceInfoTraffic {

  /**
   * Interface Name
   */
  private String ifname;

  /**
   * Number of Receiving Packets Per One Second
   */
  private Float rxpck;

  /**
   * Number of Sending Packets Per One Second
   */
  private Float txpck;

  /**
   * Number of Receiving Bytes Per One Second
   */
  private Float rxkb;

  /**
   * Number of Sending Bytes Per One Second
   */
  private Float txkb;

  /**
   * Getting interface name.
   *
   * @return interface name
   */
  public String getIfname() {
    return ifname;
  }

  /**
   * Setting interface name.
   *
   * @param ifname
   *          interface name
   */
  public void setIfname(String ifname) {
    this.ifname = ifname;
  }

  /**
   * Getting the number of receiving packets per one second.
   *
   * @return the number of receiving packets per one second
   */
  public Float getRxpck() {
    return rxpck;
  }

  /**
   * Setting the number of receiving packets per one second.
   *
   * @param rxpck
   *          the number of receiving packets per one second
   */
  public void setRxpck(Float rxpck) {
    this.rxpck = rxpck;
  }

  /**
   * Getting the number of sending packets per one second.
   *
   * @return the number of sending packets per one second
   */
  public Float getTxpck() {
    return txpck;
  }

  /**
   * Setting the number of sending packets per one second.
   *
   * @param txpck
   *          the number of sending packets per one second
   */
  public void setTxpck(Float txpck) {
    this.txpck = txpck;
  }

  /**
   * Getting the number of receiving bytes per one second.
   *
   * @return the number of receiving bytes per one second
   */
  public Float getRxkb() {
    return rxkb;
  }

  /**
   * Setting the number of receiving bytes per one second.
   *
   * @param rxkb
   *          the number of receiving bytes per one second
   */
  public void setRxkb(Float rxkb) {
    this.rxkb = rxkb;
  }

  /**
   * Getting the number of sending bytes per one second.
   *
   * @return the number of sending bytes per one second
   */
  public Float getTxkb() {
    return txkb;
  }

  /**
   * Setting the number of sending bytes per one second.
   *
   * @param txkb
   *          the number of sending bytes per one second
   */
  public void setTxkb(Float txkb) {
    this.txkb = txkb;
  }

  @Override
  public String toString() {
    return "InterfaceInfoTraffic [ifname=" + ifname + ", rxpck=" + rxpck + ", txpck=" + txpck + ", rxkb=" + rxkb
        + ", txkb=" + txkb + "]";
  }

}
