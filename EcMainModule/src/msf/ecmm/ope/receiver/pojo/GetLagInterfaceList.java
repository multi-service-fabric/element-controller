/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.LagIf;

/**
 * LagIF Information List Acquisition.
 */
public class GetLagInterfaceList extends AbstractResponseMessage {

  /** LagIF Information List. */
  private ArrayList<LagIf> lagIfs = new ArrayList<LagIf>();

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
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetLagInterfaceList [lagIfs=" + lagIfs + "]";
  }

}
