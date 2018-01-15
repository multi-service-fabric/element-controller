/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Physical IF Information.
 */
public class LagMembersPhysicalIfs {

  /** Physical IF ID */
  private String physicalIfId;

  /**
   * Getting physical IF ID.
   *
   * @return physical IF ID
   */
  public String getPhysicalIfId() {
    return physicalIfId;
  }

  /**
   * Setting physical IF ID.
   *
   * @param physicalIfId
   *          physical IF ID
   */
  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "PhysicalIf [physicalIfId=" + physicalIfId + "]";
  }

}
