/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Leaf Device Information Class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "leaf-device")
public class LeafDevice {

  /** Leaf Device Name. */
  private String name = null;

  /** Container Storing Inter-Cluster Link Configuration To Be Added */
  @XmlElement(name = "cluster-link")
  private ClusterLink clusterLink = null;

  /**
   * Generating new instance.
   */
  public LeafDevice() {
    super();
  }

  /**
   * Getting Leaf device name.
   *
   * @return Leaf device name
   */
  public String getName() {
    return name;
  }

  /**
   * Setting Leaf device name.
   *
   * @param name
   *          Leaf device name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting inter-cluster link configuration container.
   *
   * @return inter-cluster link configuration container
   */
  public ClusterLink getClusterLink() {
    return clusterLink;
  }

  /**
   * Setting inter-cluster link configuration container.
   *
   * @param clusterLink
   *          inter-cluster link configuration container
   */
  public void setClusterLink(ClusterLink clusterLink) {
    this.clusterLink = clusterLink;
  }

  /**
   * Stringazing Instance.
   *
   */
  @Override
  public String toString() {
    return "DeviceLeaf [name=" + name + ", clusterLink=" + clusterLink + "]";
  }
}
