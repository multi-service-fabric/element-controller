/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.BreakoutIf;

/**
 * BreakoutIF List Information Acquisition.
 */
public class GetBreakoutInterfaceList extends AbstractResponseMessage {

  /** BreakoutIF Information. */
  private ArrayList<BreakoutIf> breakoutIfs = new ArrayList<BreakoutIf>();

  /**
   * Getting BreakoutIF information.
   *
   * @return BreakoutIF information
   */
  public ArrayList<BreakoutIf> getBreakoutIfs() {
    return breakoutIfs;
  }

  /**
   * Setting BreakoutIF information.
   *
   * @param breakoutIfs
   *          BreakoutIF information
   */
  public void setBreakoutIfs(ArrayList<BreakoutIf> breakoutIfs) {
    this.breakoutIfs = breakoutIfs;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetBreakoutInterfaceList [breakoutIfs=" + breakoutIfs + "]";
  }
}
