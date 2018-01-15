/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * AS Configuration Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "as")
public class L2L3VpnAs {

  /** AS Number */
  @XmlElement(name = "as-number")
  private Long asNumber = null;

  /**
   * Generating new instance.
   */
  public L2L3VpnAs() {
    super();
  }

  /**
   * Getting AS number.
   *
   * @return AS number
   */
  public Long getAsNumber() {
    return asNumber;
  }

  /**
   * Setting AS number
   *
   * @param asNumber
   *          AS number
   */
  public void setAsNumber(Long asNumber) {
    this.asNumber = asNumber;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L3VpnAs [asNumber=" + asNumber + "]";
  }
}
