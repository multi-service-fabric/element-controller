/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * breakoutIF Information.
 */
public class BreakoutBaseIf {

  /** breakout Target Physical IFID. */
  private String basePhysicalIfId;

  /** IF Speed After Division. */
  private String speed;

  /** breakoutIFID List. */
  private List<String> breakoutIfIds = new ArrayList<String>();

  /**
   * Getting breakout target physical IFID.
   *
   * @return breakout target physical IFID.
   */
  public String getBasePhysicalIfId() {
    return basePhysicalIfId;
  }

  /**
   * Setting breakout target physical IFID.
   *
   * @param basePhysicalIfId
   *          breakout target physical IFID.
   */
  public void setBasePhysicalIfId(String basePhysicalIfId) {
    this.basePhysicalIfId = basePhysicalIfId;
  }

  /**
   * Getting IF speed after division.
   *
   * @return IF speed after division.
   */
  public String getSpeed() {
    return speed;
  }

  /**
   * Setting IF speed after division.
   *
   * @param speed
   *          IF speed after division.
   */
  public void setSpeed(String speed) {
    this.speed = speed;
  }

  /**
   * Getting breakoutIFID list.
   *
   * @return breakoutIFID list.
   */
  public List<String> getBreakoutIfIds() {
    return breakoutIfIds;
  }

  /**
   * Setting breakoutIFID list.
   *
   * @param breakoutIfIds
   *          breakoutIFID list.
   */
  public void setBreakoutIfIds(List<String> breakoutIfIds) {
    this.breakoutIfIds = breakoutIfIds;
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (basePhysicalIfId == null) {
      throw new CheckDataException();
    }
    if (speed == null) {
      throw new CheckDataException();
    }
    if (breakoutIfIds.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (String str : breakoutIfIds) {
        if (str == null) {
          throw new CheckDataException();
        }
      }
    }
  }

  @Override
  public String toString() {
    return "BreakoutBaseIf [basePhysicalIfId=" + basePhysicalIfId + ", speed=" + speed + ", breakoutIfIds="
        + breakoutIfIds + "]";
  }
}
