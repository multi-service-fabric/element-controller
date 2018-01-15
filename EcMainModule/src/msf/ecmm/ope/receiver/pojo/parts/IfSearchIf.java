/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

/**
 * IF Information List.
 */
public class IfSearchIf {

  /** Physical IF Information List. */
  private ArrayList<PhysicalIf> physicalIfs = new ArrayList<PhysicalIf>();

  /** LagIF Information List. */
  private ArrayList<LagIf> lagIfs = new ArrayList<LagIf>();

  /** BreakoutIF Information List. */
  private ArrayList<BreakoutIf> breakoutIfs = new ArrayList<BreakoutIf>();

  /**
   * Getting physical IF information list.
   *
   * @return physical IF information list
   */
  public ArrayList<PhysicalIf> getPhysicalIfs() {
    return physicalIfs;
  }

  /**
   * Setting physical IF information list.
   *
   * @param physicalIfs
   *          physical IF information list
   */
  public void setPhysicalIfs(ArrayList<PhysicalIf> physicalIfs) {
    this.physicalIfs = physicalIfs;
  }

  /**
   * Getting LagIF information list.
   *
   * @return LagIF information list
   */
  public ArrayList<LagIf> getLagIfs() {
    return lagIfs;
  }

  /**
   * Setting LagIF information list.
   *
   * @param lagIfs
   *          LagIF information list
   */
  public void setLagIfs(ArrayList<LagIf> lagIfs) {
    this.lagIfs = lagIfs;
  }

  /**
   * Getting BreakoutIF information list.
   *
   * @return breakoutIf BreakoutIF information list
   */
  public ArrayList<BreakoutIf> getBreakoutIf() {
    return breakoutIfs;
  }

  /**
   * Setting BreakoutIF information list.
   *
   * @param breakoutIf
   *          BreakoutIF information list
   */
  public void setBreakoutIf(ArrayList<BreakoutIf> breakoutIf) {
    this.breakoutIfs = breakoutIf;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "IfSearchIf [physicalIfs=" + physicalIfs + ", lagIfs=" + lagIfs + ", breakoutIf=" + breakoutIfs + "]";
  }

}
