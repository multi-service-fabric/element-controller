/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import msf.ecmm.emctrl.pojo.parts.LeafDevice;

/**
 * POJO Class for Inter-Cluster Link Generation/Deletion.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "cluster-link")
public class BetweenClustersLinkAddDelete extends AbstractMessage {

  /** Attribute Data of Service Basic Configuration. */
  @XmlAttribute
  private String xmlns = "http://www.ntt.co.jp/msf/service/cluster-link";

  /** Service Name. */
  private String name = null;

  /** Leaf Device Information. */
  @XmlElement(name = "leaf-device")
  private LeafDevice device = null;

  /**
   * Generating new instance.
   */
  public BetweenClustersLinkAddDelete() {
    super();
  }

  /**
   * Getting service name.
   *
   * @return service name
   */
  public String getName() {
    return name;
  }

  /**
   * Setting service name.
   *
   * @param name
   *          service name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting Leaf device information.
   *
   * @return Leaf device information
   */
  public LeafDevice getDevice() {
    return device;
  }

  /**
   * Setting Leaf device information.
   *
   * @param device
   *          Leaf device information
   */
  public void setDevice(LeafDevice device) {
    this.device = device;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "BetweenClustersLinkAddDelete [xmlns=" + xmlns + ", name=" + name + ", device=" + device + "]";
  }

}
