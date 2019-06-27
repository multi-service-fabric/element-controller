/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.IfSearchIf;

/**
 * IF Information List Acquisition.
 */
public class GetInterfaceList extends AbstractResponseMessage {

  /** IF Information List. */
  private IfSearchIf ifs;

  /**
   * Getting IF information list.
   *
   * @return IF information list
   */
  public IfSearchIf getIfs() {
    return ifs;
  }

  /**
   * Setting IF information list.
   *
   * @param ifs
   *          IF information list
   */
  public void setIfs(IfSearchIf ifs) {
    this.ifs = ifs;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetInterfaceList [ifs=" + ifs + "]";
  }

}
