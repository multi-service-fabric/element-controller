/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Inter-Cluster Link Configuration.
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "cluster-link")
public class ClusterLink {

  /** Attribute Data of cluster-link. */
  @XmlAttribute
  private String operation = null;

  /** Inter-Cluster Link Using IF Name. */
  private String name = null;

  /** Inter-Cluster Link Using IF Type. */
  @XmlElement(name = "if_type")
  private String ifType = null;

  /** Address art (xx.xx.xx.xx) of the IPv4 Address with Net Mask (xx.xx.xx.xx/yy) to be configured to inter-cluster link using IF. */
  private String address = null;

  /** Net Mask part (yy) of the IPv4 Address with Net Mask (xx.xx.xx.xx/yy) to be configured to inter-cluster link using IF. */
  private Integer prefix = null;

  /** List of LAG Member's Physical IF Name or breakoutIF Name. */
  @XmlElement(name = "cluster-lagmem-interface")
  private List<LagMemberIf> lagMemberList = null;

  /** OSPF Connection Related Information. */
  private Ospf ospf = null;

  /**
   * Getting attribute data of cluster-link.
   *
   * @return attribute data of cluster-link.
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting attribute data of cluster-link.
   *
   * @param operation
   *          attribute data of cluster-link.
   */
  public void setOperation(String operation) {
    this.operation = operation;
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

  /**
   * Getting IF type.
   *
   * @return IF type
   */
  public String getIfType() {
    return ifType;
  }

  /**
   * Setting IF type.
   *
   * @param ifType
   *          IF type
   */
  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  /**
   * Getting IPv4 address.
   *
   * @return IPv4 address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting IPv4 address.
   *
   * @param address
   *          IPv4 address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting IPv4 address prefix.
   *
   * @return IPv4 address prefix
   */
  public int getPrefix() {
    return prefix;
  }

  /**
   * Setting IPv4 address prefix.
   *
   * @param prefix
   *          IPv4 address prefix
   */
  public void setPrefix(int prefix) {
    this.prefix = prefix;
  }

  /**
   * Getting LAG member information list.
   *
   * @return CP configuration information list
   */
  public List<LagMemberIf> getLagMemberList() {
    return lagMemberList;
  }

  /**
   * Setting IPv4 address prefix.
   *
   * @param lagMemberList
   *          CP configurtion information list
   */
  public void setLagMemberList(List<LagMemberIf> lagMemberList) {
    this.lagMemberList = lagMemberList;
  }

  /**
   * Getting OSPF connection related information.
   *
   * @return OSPF connection related information
   */
  public Ospf getOspf() {
    return ospf;
  }

  /**
   * Setting OSPF connection related information.
   *
   * @param metric
   *          OSPF connection related information
   */
  public void setOspf(Ospf metric) {
    this.ospf = metric;
  }

  /**
   * Stringizing Instance.
   *
   */
  @Override
  public String toString() {
    return "DeviceLeaf [name=" + name + ", ifType" + ifType + ", address" + address + ", prefix" + prefix
        + ", lagMemberList" + lagMemberList + ", ospf=" + ospf + "]";
  }
}
