/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.LagIf;

/**
 * LagIF Information Acquisition.
 */
public class GetLagInterface extends AbstractResponseMessage {

  /** LagIF Information. */
  private LagIf lagIf = new LagIf();

  /**
   * Getting LagIF information.
   *
   * @return LagIF information
   */
  public LagIf getLagIfs() {
    return lagIf;
  }

  /**
   * Setting LagIF information.
   *
   * @param lagIf
   *          LagIF information
   */
  public void setLagIfs(LagIf lagIf) {
    this.lagIf = lagIf;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "GetLagInterface [lagIf=" + lagIf + "]";
  }
}
