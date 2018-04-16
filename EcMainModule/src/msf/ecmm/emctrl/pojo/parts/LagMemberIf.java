/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * LAG Member Information Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "cluster-lagmem-interface")
public class LagMemberIf {

  /** Physical IF Name or breakoutIF Name. */
  private String name = null;

  /** Port Open Status. */
  private String condition = null;

  /**
   * Getting IF name.
   *
   * @return physical IF name or breakoutIF name
   */
  public String getName() {
    return name;
  }

  /**
   * Setting IF name.
   *
   * @param name
   *          physical IF name or breakoutIF name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting port open status.
   *
   * @return port open status
   */
  public String getCondition() {
    return condition;
  }

  /**
   * Setting port open status.
   *
   * @param condition
   *          port open status
   */
  public void setCondition(String condition) {
    this.condition = condition;
  }

  /**
   * Stringazing Instance.
   *
   */
  @Override
  public String toString() {
    return "LagMemberIf [name=" + name + ", condition=" + condition + "]";
  }
}
