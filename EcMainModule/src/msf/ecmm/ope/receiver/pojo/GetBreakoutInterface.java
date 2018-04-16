/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.BreakoutIf;

/**
 * BreakoutIF Information Acquisition.
 */
public class GetBreakoutInterface extends AbstractResponseMessage {

  /** BreakoutIF Information. */
  private BreakoutIf breakoutIf;

  /**
   * Getting BreakoutIF information.
   *
   * @return BreakoutIF information
   */
  public BreakoutIf getBreakoutIf() {
    return breakoutIf;
  }

  /**
   * Setting BreakoutIF information.
   *
   * @param breakoutIf
   *          BreakoutIF information
   */
  public void setBreakoutIf(BreakoutIf breakoutIf) {
    this.breakoutIf = breakoutIf;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetBreakoutInterface [breakoutIf=" + breakoutIf + "]";
  }
}
