/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * OSPF Connection Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ospf")
public class L3SliceOspf {

  /** Metric */
  private Long metric = null;

  /**
   * Generating new instance.
   */
  public L3SliceOspf() {
    super();
  }

  /**
   * Getting metric.
   *
   * @return metric
   */
  public Long getMetric() {
    return metric;
  }

  /**
   * Setting metric.
   *
   * @param metric
   *          metric
   */
  public void setMetric(Long metric) {
    this.metric = metric;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L3SliceOspf [metric=" + metric + "]";
  }
}
