/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * QoS Configuration Information Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Qos {

  /** Inflow Limit Value. */
  @XmlElement(name = "inflow-shaping-rate")
  private XmlFloatElement inflowShapingRate = null;

  /** Outflow Limit Value. */
  @XmlElement(name = "outflow-shaping-rate")
  private XmlFloatElement outflowShapingRate = null;

  /** Remark Menu. */
  @XmlElement(name = "remark-menu")
  private XmlStringElement remarkMenu = null;

  /** Egress Queue Menu. */
  @XmlElement(name = "egress-menu")
  private XmlStringElement egressMenu = null;

  /**
   * Getting attribute data of ospf.
   *
   * @return attribute data of device.
   */
  public Qos() {
    super();
  }

  /**
   * Getting Inflow Limit Value.
   *
   * @return inflow limit value
   */
  public XmlFloatElement getInflowShapingRate() {
    return inflowShapingRate;
  }

  /**
   * Setting Inflow Limit Value.
   *
   * @param inflowShapingRate
   *        inflow limit value.
   */
  public void setInflowShapingRate(XmlFloatElement inflowShapingRate) {
    this.inflowShapingRate = inflowShapingRate;
  }

  /**
   * Getting Outflow Limit Value.
   *
   * @return   outflow limit value.
   */
  public XmlFloatElement getOutflowShapingRate() {
    return outflowShapingRate;
  }

  /**
   * Setting Outflow Limit Value.
   *
   * @param outflowShapingRate
   *          outflow limit value.
   */
  public void setOutflowShapingRate(XmlFloatElement outflowShapingRate) {
    this.outflowShapingRate = outflowShapingRate;
  }

  /**
   * Getting Remark menu.
   *
   * @return remark menu
   */
  public XmlStringElement getRemarkMenu() {
    return remarkMenu;
  }

  /**
   * Setting Remark menu.
   *
   * @param remarkMenu
   *          remark menu
   */
  public void setRemarkMenu(XmlStringElement remarkMenu) {
    this.remarkMenu = remarkMenu;
  }

  /**
   * Getting Egress queue menu.
   *
   * @return egress queue menu
   */
  public XmlStringElement getEgressMenu() {
    return egressMenu;
  }

  /**
   * Getting Egress queue menu.
   *
   * @param egressMenu
   *          Egress queue menu.
   */
  public void setEgressMenu(XmlStringElement egressMenu) {
    this.egressMenu = egressMenu;
  }

  /*
   * Stingazing Instance.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Qos [inflowShapingRate=" + inflowShapingRate + ", outflowShapingRate=" + outflowShapingRate
        + ", remarkMenu=" + remarkMenu + ", egressMenu=" + egressMenu + "]";
  }

}
