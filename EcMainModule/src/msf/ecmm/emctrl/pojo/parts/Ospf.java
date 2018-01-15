/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * OSPF Connection Related Information Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ospf")
public class Ospf {

  /** IGP Cost Value. */
  private Integer metric = null;

  /**
   * Getting IGP cost value.
   *
   * @return IGP cost value
   */
  public Integer getMetric() {
    return metric;
  }

  /**
   * Setting IGP cost value.
   *
   * @param metric
   *          IGP cost value
   */
  public void setMetric(Integer metric) {
    this.metric = metric;
  }

  /**
   * Stingazing Instance.
   *
   */
  @Override
  public String toString() {
    return "Ospf [metric=" + metric + "]";
  }
}
