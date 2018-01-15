/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * breakoutIFID for Physical IF Information Acquisition Class.
 */
public class BreakoutIfId {

  /** breakoutIFID. */
  private String breakoutIfId;

  /**
   * Getting breakoutIFID.
   *
   * @return breakoutIFID
   */
  public String getBreakoutIdId() {
    return breakoutIfId;
  }

  /**
   * Setting breakoutIFID.
   *
   * @param breakoutIdId
   *          breakoutIFID
   */
  public void setBreakoutIfId(String breakoutIdId) {
    this.breakoutIfId = breakoutIdId;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "BreakoutIfId [breakoutIdId=" + breakoutIfId + "]";
  }
}
