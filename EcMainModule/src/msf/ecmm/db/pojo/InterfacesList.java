/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Physical/LAG/breakoutIF List Information POJO Class.
 */
public class InterfacesList implements Serializable {

  /** Physical IF Information List. */
  private List<PhysicalIfs> physicalIfsList;
  /** LAGIF Information List. */
  private List<LagIfs> lagIfsList;
  /** breakoutIF Information List. */
  private List<BreakoutIfs> breakoutIfsList;

  /**
   * Generating new instance.
   */
  public InterfacesList() {
    super();
  }

  /**
   * Getting physical IF information list.
   *
   * @return physical IF information list
   */
  public List<PhysicalIfs> getPhysicalIfsList() {
    return physicalIfsList;
  }

  /**
   * Setting physical IF information list.
   *
   * @param physicalIfsList
   *          physical IF information list
   */
  public void setPhysicalIfsList(List<PhysicalIfs> physicalIfsList) {
    this.physicalIfsList = physicalIfsList;
  }

  /**
   * Getting LAGIF information list.
   *
   * @return LAGIF information list
   */
  public List<LagIfs> getLagIfsList() {
    return lagIfsList;
  }

  /**
   * Setting LAGIF information list.
   *
   * @param lagIfsList
   *          LAGIF information list
   */
  public void setLagIfsList(List<LagIfs> lagIfsList) {
    this.lagIfsList = lagIfsList;
  }

  /**
   * Getting breakoutIF information list.
   *
   * @return breakoutIfsList
   */
  public List<BreakoutIfs> getBreakoutIfsList() {
    return breakoutIfsList;
  }

  /**
   * Setting breakoutIF information list.
   *
   * @param breakoutIfsList
   *          breakoutIF information list
   */
  public void setBreakoutIfsList(List<BreakoutIfs> breakoutIfsList) {
    this.breakoutIfsList = breakoutIfsList;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "InterfacesList [physicalIfsList=" + physicalIfsList + ", lagIfsList=" + lagIfsList + ", breakoutIfsList="
        + breakoutIfsList + "]";
  }
}
