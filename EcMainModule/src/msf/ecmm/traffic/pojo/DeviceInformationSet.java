/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.traffic.pojo;

import java.util.HashMap;

import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.VlanIfs;

/**
 * Device Information Set Class Definition. Stores information set for a node.
 */
public class DeviceInformationSet {

  /** Model Information */
  private Equipments equipmentsType = null;

  /** Device Information */
  private Nodes equipmentsData = null;

  /** VLAN IF Information */
  private HashMap<Integer, VlanIfs> vlanIfData = null;

  /** LAG IF Information */
  private HashMap<Integer, LagIfs> lagIfData = null;

  /** Physical IF Information */
  private HashMap<Integer, PhysicalIfs> physicalIfData = null;

  /** breakout IF Information */
  private HashMap<String, BreakoutIfs> breakoutIfData = null;

  /**
   * Generating new instance.
   */
  public DeviceInformationSet() {
    super();
  }

  /**
   * Getting model information.
   *
   * @return model information
   */
  public Equipments getEquipmentsType() {
    return equipmentsType;
  }

  /**
   * Setting model information.
   *
   * @param equipmentsType
   *          model information
   */
  public void setEquipmentsType(Equipments equipmentsType) {
    this.equipmentsType = equipmentsType;
  }

  /**
   * Getting device information.
   *
   * @return device information
   */
  public Nodes getEquipmentsData() {
    return equipmentsData;
  }

  /**
   * Setting device information.
   *
   * @param equipmentsData
   *          device information
   */
  public void setEquipmentsData(Nodes equipmentsData) {
    this.equipmentsData = equipmentsData;
  }

  /**
   * Getting VLAN IF information.
   *
   * @return VLAN IF information
   */
  public HashMap<Integer, VlanIfs> getVlanIfData() {
    return vlanIfData;
  }

  /**
   * Setting VLAN IF information.
   *
   * @param vlanIfData
   *          VLAN IF information
   */
  public void setVlanIfData(HashMap<Integer, VlanIfs> vlanIfData) {
    this.vlanIfData = vlanIfData;
  }

  /**
   * Getting LAG IF information.
   *
   * @return LAG IF information
   */
  public HashMap<Integer, LagIfs> getLagIfData() {
    return lagIfData;
  }

  /**
   * Setting LAG IF information.
   *
   * @param lagIfData
   *          LAG IF information
   */
  public void setLagIfData(HashMap<Integer, LagIfs> lagIfData) {
    this.lagIfData = lagIfData;
  }

  /**
   * Getting physical IF information.
   *
   * @return physical IF information
   */
  public HashMap<Integer, PhysicalIfs> getPhysicalIfData() {
    return physicalIfData;
  }

  /**
   * Setting physical IF information.
   *
   * @param physicalIfData
   *          physical IF information
   */
  public void setPhysicalIfData(HashMap<Integer, PhysicalIfs> physicalIfData) {
    this.physicalIfData = physicalIfData;
  }

  /**
   * Getting breakout IF information.
   *
   * @return breakout IF information
   */
  public HashMap<String, BreakoutIfs> getBreakoutIfData() {
    return breakoutIfData;
  }

  /**
   * Setting breakout IF information.
   *
   * @param breakoutIfData
   *          breakout IF information
   */
  public void setBreakoutIfData(HashMap<String, BreakoutIfs> breakoutIfData) {
    this.breakoutIfData = breakoutIfData;
  }

  @Override
  public String toString() {
    return "DeviceInformationSet [equipmentsType=" + equipmentsType + ", equipmentsData=" + equipmentsData
        + ", vlanIfData=" + vlanIfData + ", lagIfData=" + lagIfData + ", physicalIfData=" + physicalIfData
        + ", breakoutIfData=" + breakoutIfData + "]";
  }

}
