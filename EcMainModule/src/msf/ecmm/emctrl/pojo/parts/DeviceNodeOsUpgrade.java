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
* Class for the device infomation (to upgrade OS in the node).
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "device")
public class DeviceNodeOsUpgrade {

  /** The name of the node to be added. */
  private String name = null;

  /** The name of the node type to be added. */
  @XmlElement(name = "node-type")
  private String nodeType = null;

  /** The equipment information to be added. */
  private Equipment equipment = null;

  /** The coversion tatble of the physical IF information. */
  @XmlElement(name = "physical-ifs")
  private List<InterfaceNames> physiIfNames = null;

  /** The coversion tatble of the LAG information IF. */
  @XmlElement(name = "lag-ifs")
  private List<InterfaceNames> lagIfNames = null;

  /** The list of the internal link IF information. */
  @XmlElement(name = "internal-interface")
  private List<InternalInterface> internalInterfaceList = null;

  /** The DB-updating flag */
  @XmlElement(name = "db_update")
  private String dbUpdate = null;

  /**
   * A new instance is generated.
   */
  public DeviceNodeOsUpgrade() {
    super();
  }

  /**
   * The name of the added node is acquired.
   *
   * @return The name of the added node
   */
  public String getName() {
    return name;
  }

  /**
   * The name of the added node is set.
   *
   * @param name
   *          The name of the added node
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * The type name of the node is acquired.
   *
   * @return The type name of the node
   */
  public String getNodeType() {
    return nodeType;
  }

  /**
   * The type name of the node is set.
   *
   * @param nodeType
   *          The type name of the node.
   */
  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  /**
   * The information on the added equipment is acquired. 
   *
   * @return The information on the added equipment.
   */
  public Equipment getEquipment() {
    return equipment;
  }

  /**
   * The information on the added device set.
   *
   * @param equipment
   *          The information on the added device
   */
  public void setEquipment(Equipment equipment) {
    this.equipment = equipment;
  }

  /**
   * The table for converting the physical IF to the information IF is acquired.
   *
   * @return The table for converting the physical IF to the information IF
   */
  public List<InterfaceNames> getPhysiIfNames() {
    return physiIfNames;
  }

  /**
   * The table for converting the physical IF to the information IF is set
   *
   * @param physiIfNames
   *          The table for converting the physical IF to the information IF
   */
  public void setPhysiIfNames(List<InterfaceNames> physiIfNames) {
    this.physiIfNames = physiIfNames;
  }

  /**
   * The table for converting the LAG information IF is acquired.
   *
   * @return The table for converting the LAG information IF
   */
  public List<InterfaceNames> getLagIfNames() {
    return lagIfNames;
  }

  /**
   * The table for converting the LAG information IF is acquired.
   *
   * @param lagIfNames
   *          The table for converting the LAG information IF
   */
  public void setLagIfNames(List<InterfaceNames> lagIfNames) {
    this.lagIfNames = lagIfNames;
  }

  /**
   * The informaton list for setting the internal link is acquired.
   *
   * @return The informaton list for setting the internal link
   */
  public List<InternalInterface> getInternalIntefaceList() {
    return internalInterfaceList;
  }

  /**
   * The informaton list for setting the internal link is set.
   *
   * @param internalLagList
   *          The informaton list for setting the internal link.
   */
  public void setInternalInterfaceList(List<InternalInterface> internalLagList) {
    this.internalInterfaceList = internalLagList;
  }

  /**
   * The DB-updating flag is added. 
   *
   * @return The DB-updating flag
   */
  public String addDbUpdate() {
    return dbUpdate = new String("");
  }

  /**
   * The DB-updating flag is deleted.
   *
   */
  public void delDbUpdate() {
    this.dbUpdate = null;
  }

  /* (non JavaDoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "DeviceNodeOsUpgrade [name=" + name + ", nodeType=" + nodeType + ", equipment=" + equipment
        + ", physiIfNames=" + physiIfNames + ", lagIfNames=" + lagIfNames + ", internalLagList=" + internalInterfaceList
        + ", dbUpdate=" + dbUpdate + "]";
  }


}
