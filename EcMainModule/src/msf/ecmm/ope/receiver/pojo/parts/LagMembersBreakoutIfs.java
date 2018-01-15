/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Physical IF Information.
 */
public class LagMembersBreakoutIfs {

  /** breakout Source Physical IF ID. */
  private String basePhysicalIfId;

  /** breakoutIF ID. */
  private String breakoutIfId;

  /**
   * Getting breakout sourece physical IF ID.
   *
   * @return physical IF ID
   */
  public String getPhysicalIfId() {
    return basePhysicalIfId;
  }

  /**
   * Setting breakout sourece physical IF ID.
   *
   * @param physiId
   *          physical IF ID
   */
  public void setPhysicalIfId(String physiId) {
    this.basePhysicalIfId = physiId;
  }

  /**
   * Getting breakoutIF ID.
   *
   * @return breakoutIfId
   */
  public String getBreakoutIfId() {
    return breakoutIfId;
  }

  /**
   * Setting breakoutIF ID.
   *
   * @param breakoutIfId
   *          set breakoutIfId
   */
  public void setBreakoutIfId(String breakoutIfId) {
    this.breakoutIfId = breakoutIfId;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "PhysicalIf [physicalIfId=" + basePhysicalIfId + ", breakoutIfId" + breakoutIfId + "]";
  }

}
