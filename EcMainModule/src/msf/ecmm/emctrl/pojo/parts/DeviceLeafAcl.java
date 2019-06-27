/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Leaf Device Information Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "device-leaf")
public class DeviceLeafAcl {

  /** Leaf Device Name. */
  private String name = null;

  /** Filter List. */
  @XmlElement(name = "filter")
  private List<AclFilter> filter = null;

  /**
   * Generating new instance.
   */
  public DeviceLeafAcl() {
    super();
  }

  /**
   * Getting name of Leaf device.
   *
   * @return name of Leaf device
   */
  public String getName() {
    return name;
  }

  /**
   * Setting name of Leaf device.
   *
   * @param name
   *          name of Leaf device
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting a list for configuring the ACL that you want to add.
   *
   * @return filter
   */
  public List<AclFilter> getFilter() {
    return filter;
  }

  /**
   * Getting a list for configuring the ACL that you want to add.
   *
   * @param filter
   *          set filter
   */
  public void setFilter(List<AclFilter> filter) {
    this.filter = filter;
  }

  /* (Non Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "DeviceLeafAcl [name=" + name + ", filter=" + filter + "]";
  }
}
