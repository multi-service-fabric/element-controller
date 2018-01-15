/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

/**
 * LagIF Information List.
 *
 */
public class LagMember {

  /** Physical IF Information List. */
  private List<LagMembersPhysicalIfs> physicalIfs = new ArrayList<LagMembersPhysicalIfs>();

  /** breakoutIF Information List. */
  private List<LagMembersBreakoutIfs> breakoutIfs = new ArrayList<LagMembersBreakoutIfs>();

  /**
   * Getting physical IF information list.
   *
   * @return physicalIfs
   */
  public List<LagMembersPhysicalIfs> getPhysicalIfs() {
    return physicalIfs;
  }

  /**
   * Setting physical IF information list.
   *
   * @param physicalIfs
   *          set physicalIfs
   */
  public void setPhysicalIfs(List<LagMembersPhysicalIfs> physicalIfs) {
    this.physicalIfs = physicalIfs;
  }

  /**
   * Getting breakoutIF information list.
   *
   * @return breakoutIfs
   */
  public List<LagMembersBreakoutIfs> getBreakoutIfs() {
    return breakoutIfs;
  }

  /**
   * Setting breakoutIF information list.
   *
   * @param breakoutIfs
   *          set breakoutIfs
   */
  public void setBreakoutIfs(List<LagMembersBreakoutIfs> breakoutIfs) {
    this.breakoutIfs = breakoutIfs;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "LagIf [physicalIfs=" + physicalIfs + ", breakoutIfs=" + breakoutIfs + "]";
  }

}
