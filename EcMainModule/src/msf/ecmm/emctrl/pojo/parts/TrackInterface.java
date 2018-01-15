/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Tracking IF Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "interface")
public class TrackInterface {

  /** IF Name */
  private String name = null;

  /**
   * Generating new instance.
   */
  public TrackInterface() {
    super();
  }

  /**
   * Getting IF name.
   *
   * @return IF name
   */
  public String getName() {
    return name;
  }

  /**
   * Setting IF name.
   *
   * @param name
   *          IF name
   */
  public void setName(String name) {
    this.name = name;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "TrackInterface [name=" + name + "]";
  }

}
