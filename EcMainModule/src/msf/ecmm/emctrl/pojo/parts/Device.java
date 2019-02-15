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
 * Device information class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "device")
public class Device {

  /** Attribute Data of device. */
  @XmlAttribute
  private String operation = null;

  /** VPN Type (only for Adding LAG for Internal Link). */
  @XmlElement(name = "vpn-type")
  private String vpnType = null;

  /** Name of Device To Be Extended. */
  private String name = null;

  /** Node Type Name(only for Service Update Node/Service reconfiguration). */
  @XmlElement(name = "node-type")
  private String nodeType = null;

  /** Information of Device To Be Extended (only for Spine/Leaf/B-Leaf Device Extention). */
  private Equipment equipment = null;

  /** breakoutIF Information List (only for Spine/Leaf Device Extention and breakoutIF Registration/Deletion). */
  @XmlElement(name = "breakout-interface")
  private List<BreakoutIf> breakoutIfList = null;

   /** Internal Link Configuration Information List (only for Spine/Leaf/B-Leaf Device Extention). */
  @XmlElement(name = "internal-interface")
  private List<InternalInterface> internalLagList = null;

  /** Management IF Configuration Information (only for Spine/Leaf/B-Leaf Device Extention). */
  @XmlElement(name = "management-interface")
  private ManagementInterface managementInterface = null;

 /** Loopback IF Configuration Information (only for Spine/Leaf/B-Leaf Device Extention). */
  @XmlElement(name = "loopback-interface")
  private LoopbackInterface loopbackInterface = null;

/** List of LAGIF Configuration Information for CE (only for LagIF Generation/Deletion). */
  @XmlElement(name = "ce-lag-interface")
  private List<CeLagInterface> ceLagInterfaceList = null;

 /** SNMP Server Information (only for Spine/Leaf/B-Leaf Device Extention). */
  private Snmp snmp = null;

 /** NTP Server Information (only for Spine/Leaf/B-Leaf Device Extention). */
  private Ntp ntp = null;

  /** L3VPN Configuration Information (only for Leaf/B-Leaf Device Extention). */
  @XmlElement(name = "l3-vpn")
  private L3Vpn l3Vpn = null;

  /** L2VPN Configuration Information (only for Leaf Device Extention). */
  @XmlElement(name = "l2-vpn")
  private L2Vpn l2Vpn = null;

  /** OSPF Configuration (only for Spine/Leaf/B-Leaf Device Extentioin). */
  @XmlElement(name = "ospf")
  private OspfAddNode ospfAddNode = null;

 /** physicalIF Information Conversion List (only for Service Update Node/Service reconfiguration). */
  @XmlElement(name = "physical-ifs")
  private List<InterfaceNames> physiIfNames = null;

  /** lagIF Information Conversion List (only for Service Update Node/Service reconfiguration). */
  @XmlElement(name = "lag-ifs")
  private List<InterfaceNames> lagIfNames = null;

  /** QoS Update information(only for Service reconfiguration). */
  private Qos qos = null;

  /**
   * Generating new instance.
   */
  public Device() {
    super();
  }

  /**
   * Getting attribute data of device.
   *
   * @return attribute data of device.
   */
  public String getOperation() {
    return operation;
  }

  /**
   * Setting attribute data of device.
   *
   * @param operation
   *          attribute data of device.
   */
  public void setOperation(String operation) {
    this.operation = operation;
  }

  /**
   * Getting VPN type (only for LAG Addition for Internal Link).
   *
   * @return VPN type (only for LAG Addition for Internal Link).
   */
  public String getVpnType() {
    return vpnType;
  }

  /**
   * Setting VPN type (only for LAG Addition for Internal Link).
   *
   * @param vpnType
   *          VPN type (only for LAG Addition for Internal Link).
   */
  public void setVpnType(String vpnType) {
    this.vpnType = vpnType;
  }

  /**
   * Getting name of device to be extended.
   *
   * @return name of device to be extended.
   */
  public String getName() {
    return name;
  }

  /**
   * Setting name of device to be extended.
   *
   * @return name of device to be extended.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting internal link IF configuration information list (only for Spine/Leaf/B-Leaf device extention).
   *
   * @return internal link IF configuration information list (only for Spine/Leaf/B-Leaf device extention).
   */
  public List<InternalInterface> getInternalLagList() {
    return internalLagList;
  }

  /**
   * Setting internal link IF configuration information list (only for Spine/Leaf/B-Leaf device extention).
   *
   * @param internalLagList
   *          internal link IF configuration information list (only for Spine/Leaf/B-Leaf device extention).
   */
  public void setInternalLagList(List<InternalInterface> internalLagList) {
    this.internalLagList = internalLagList;
  }

  /**
   * Getting management IF configuration information (only for Spine/Leaf/B-Leaf device extention).
   *
   * @return management IF configuration information (only for Spine/Leaf/B-Leaf device extention).
   */
  public ManagementInterface getManagementInterface() {
    return managementInterface;
  }

  /**
   * Setting management IF configuration information (only for Spine/Leaf/B-Leaf device extention).
   *
   * @param managementInterface
   *          management IF configuration information (only for Spine/Leaf/B-Leaf device extention).
   */
  public void setManagementInterface(ManagementInterface managementInterface) {
    this.managementInterface = managementInterface;
  }

  /**
   * Getting loopback IF configuration information (only for Spine/Leaf/B-Leaf device extention).
   *
   * @return loopback IF configuration information (only for Spine/Leaf/B-Leaf device extention).
   */
  public LoopbackInterface getLoopbackInterface() {
    return loopbackInterface;
  }

  /**
   * Setting loopback IF configuration information (only for Spine/Leaf/B-Leaf device extention).
   *
   * @param loopbackInterface
   *          loopback IF configuration information (only for Spine/Leaf/B-Leaf device extention).
   */
  public void setLoopbackInterface(LoopbackInterface loopbackInterface) {
    this.loopbackInterface = loopbackInterface;
  }

  /**
   * Getting LAGIF configuration information list for CE (only for LagIF generation/deletion).
   *
   * @return LAGIF configuration information list for CE (only for LagIF generation/deletion).
   */
  public List<CeLagInterface> getCeLagInterfaceList() {
    return ceLagInterfaceList;
  }

  /**
   * Setting LAGIF configuration information list for CE (only for LagIF generation/deletion).
   *
   * @param ceLagInterfaceList
   *          LAGIF configuration information list for CE (only for LagIF generation/deletion).
   */
  public void setCeLagInterfaceList(List<CeLagInterface> ceLagInterfaceList) {
    this.ceLagInterfaceList = ceLagInterfaceList;
  }

  /**
   * Getting SNMP server information (only for Spine/Leaf/B-Leaf device extention).
   *
   * @return SNMP server information (only for Spine/Leaf/B-Leaf device extention).
   */
  public Snmp getSnmp() {
    return snmp;
  }

  /**
   * Setting SNMP server information (only for Spine/Leaf/B-Leaf device extention).
   *
   * @param snmp
   *          SNMP server information (only for Spine/Leaf/B-Leaf device extention).
   */
  public void setSnmp(Snmp snmp) {
    this.snmp = snmp;
  }


  /**
   * Getting NTP server information (only for Spine/Leaf/B-Leaf device extention).
   *
   * @return NTP server information (only for Spine/Leaf/B-Leaf device extention).
   */
  public Ntp getNtp() {
    return ntp;
  }

  /**
   * Setting NTP server information (only for Spine/Leaf/B-Leaf device extention).
   *
   * @param ntp
   *          NTP server information (only for Spine/Leaf/B-Leaf device extention).
   */
  public void setNtp(Ntp ntp) {
    this.ntp = ntp;
  }

  /**
   * Getting MSDP configuration related information (only for Spine device extention).
   *
   * @return MSDP configuration related information (only for Spine device extention).
   */
  public L3Vpn getL3Vpn() {
    return l3Vpn;
  }

  /**
   * Setting MSDP configuration related information (only for Spine device extention).
   *
   * @param l3Vpn
   *          MSDP configuration related information (only for Spine device extention).
   */
  public void setL3Vpn(L3Vpn l3Vpn) {
    this.l3Vpn = l3Vpn;
  }

  /**
   * Getting information of device to be extended (only for Spine/Leaf/B-Leaf device extention).
   *
   * @return information of device to be extended (only for Spine/Leaf/B-Leaf device extention).
   */
  public Equipment getEquipment() {
    return equipment;
  }

  /**
   * Setting information of device to be extended (only for Spine/Leaf/B-Leaf device extention).
   *
   * @param equipment
   *          information of device to be extended (only for Spine/Leaf/B-Leaf device extention).
   */
  public void setEquipment(Equipment equipment) {
    this.equipment = equipment;
  }

  /**
   * Getting breakoutIF information list.
   *
   * @return breakoutIfList
   */
  public List<BreakoutIf> getBreakoutIfList() {
    return breakoutIfList;
  }

  /**
   * Setting breakoutIF information list.
   *
   * @param list
   *          set list
   */
  public void setBreakoutIfList(List<BreakoutIf> list) {
    this.breakoutIfList = list;
  }

  /**
   * Getting L2VPN configuration information (only for Leaf device extention).
   *
   * @return L2VPN configuration information (only for Leaf device extention).
   */
  public L2Vpn getL2Vpn() {
    return l2Vpn;
  }

  /**
   * Setting L2VPN configuration information (only for Leaf device extention).
   *
   * @param l2Vpn
   *          L2VPN configuration information (only for Leaf device extention).
   */
  public void setL2Vpn(L2Vpn l2Vpn) {
    this.l2Vpn = l2Vpn;
  }

  /**
   * Getting OSPF configuration (only for Spine/Leaf/B-Leaf device extention).
   *
   * @return OSPF configuration (only for Spine/Leaf/B-Leaf device extention).
   */
  public OspfAddNode getOspfAddNode() {
    return ospfAddNode;
  }

  /**
   * Setting OSPF configuration (only for Spine/Leaf/B-Leaf device extention).
   *
   * @param ospfAddNode
   *          OSPF configuration (only for Spine/Leaf/B-Leaf device extention).
   */
  public void setOspfAddNode(OspfAddNode ospfAddNode) {
    this.ospfAddNode = ospfAddNode;
  }

  /**
   * Getting Node type.
   *
   * @return nodeType
   */
  public String getNodeType() {
    return nodeType;
  }

  /**
   * Setting Node type.
   *
   * @param nodeType
   *          set nodeType
   */
  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  /**
   * Getting physicalIF Information Conversion List.
   *
   * @return physiIfNames
   */
  public List<InterfaceNames> getPhysiIfNames() {
    return physiIfNames;
  }

  /**
   * Setting physicalIF Information Conversion List.
   *
   * @param physiIfNames
   *          set physiIfNames
   */
  public void setPhysiIfNames(List<InterfaceNames> physiIfNames) {
    this.physiIfNames = physiIfNames;
  }

  /**
   * Getting lagIF Information Conversion List.
   *
   * @return lagIfNames
   */
  public List<InterfaceNames> getLagIfNames() {
    return lagIfNames;
  }

  /**
   * Setting lagIF Information Conversion List.
   *
   * @param lagIfNames
   *          lag lagIfNames
   */
  public void setLagIfNames(List<InterfaceNames> lagIfNames) {
    this.lagIfNames = lagIfNames;
  }

  /**
   * Getting QoS update information.
   *
   * @return qos
   */
  public Qos getQos() {
    return qos;
  }

  /**
   * Setting QoS update information.
   *
   * @param qos
   *          set qos
   */
  public void setQos(Qos qos) {
    this.qos = qos;
  }

  /*
   * Stringizing Instance.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Device [operation=" + operation + ", vpnType=" + vpnType + ", name=" + name + ", nodeType=" + nodeType
        + ", equipment=" + equipment + ", breakoutIfList=" + breakoutIfList + ", internalLagList=" + internalLagList
        + ", managementInterface=" + managementInterface + ", loopbackInterface=" + loopbackInterface
        + ", ceLagInterfaceList=" + ceLagInterfaceList + ", snmp=" + snmp + ", ntp=" + ntp + ", l3Vpn=" + l3Vpn
        + ", l2Vpn=" + l2Vpn + ", ospfAddNode=" + ospfAddNode + ", physiIfNames=" + physiIfNames + ", lagIfNames="
        + lagIfNames + ", qos=" + qos + "]";
  }

}
