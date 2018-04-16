/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * QoS Configuration Class.
 */
public class QosValues {

  /** ShapingRate (inflow). */
  private Float inflowShapingRate = null;

  /** ShapingRate (outflow). */
  private Float outflowShapingRate = null;

  /** Remark Menu. */
  private String remarkMenu = null;

  /** Egress Queue Ratio Menu. */
  private String egressQueue = null;

  /**
   * Getting ShapingRate (inflow).
   *
   * @return inflowShapingRate
   */
  public Float getInflowShapingRate() {
    return inflowShapingRate;
  }

  /**
   * Setting ShapingRate (inflow).
   *
   * @param inflowShapingRate
   *          setting inflowShapingRate
   */
  public void setInflowShapingRate(Float inflowShapingRate) {
    this.inflowShapingRate = inflowShapingRate;
  }

  /**
   * Getting ShapingRate (outflow).
   *
   * @return outflowShapingRate
   */
  public Float getOutflowShapingRate() {
    return outflowShapingRate;
  }

  /**
   * Setting ShapingRate (outflow).
   *
   * @param outflowShapingRate
   *          setting outflowShapingRate
   */
  public void setOutflowShapingRate(Float outflowShapingRate) {
    this.outflowShapingRate = outflowShapingRate;
  }

  /**
   * Getting Remark menu.
   *
   * @return remarkMenu
   */
  public String getRemarkMenu() {
    return remarkMenu;
  }

  /**
   * Setting Remark menu.
   *
   * @param remarkMenu
   *          setting remarkMenu
   */
  public void setRemarkMenu(String remarkMenu) {
    this.remarkMenu = remarkMenu;
  }

  /**
   * Getting Egress Queue Ratio Menu.
   *
   * @return egressQueue
   */
  public String getEgressQueue() {
    return egressQueue;
  }

  /**
   * Setting Egress Queue Ratio Menu.
   *
   * @param egressQueue
   *          setting egressQueue
   */
  public void setEgressQueue(String egressQueue) {
    this.egressQueue = egressQueue;
  }

  /*
   * Stringizing Instance
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "QosValues [inflowShapingRate=" + inflowShapingRate + ", outflowShapingRate=" + outflowShapingRate
        + ", remarkMenu=" + remarkMenu + ", egressQueue=" + egressQueue + "]";
  }

}
