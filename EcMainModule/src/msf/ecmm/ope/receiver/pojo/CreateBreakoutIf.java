/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.ope.execute.OperationType;

/**
 * breakoutIF Registration.
 */
public class CreateBreakoutIf extends AbstractRestMessage {

  /** Device ID. */
  private String nodeId = null;

  /** Physical IF ID. */
  private String basePhysicalIfId = null;

  /** IF Speed After Division. */
  private String speed = null;

  /** breakoutIF ID List. */
  private List<String> breakoutIfId = new ArrayList<String>();

  /**
   * Getting device ID.
   *
   * @return device ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting device ID.
   *
   * @param id
   *          device ID
   */
  public void setNodeId(String id) {
    this.nodeId = id;
  }

  /**
   * Getting physical IF ID.
   *
   * @return physical IF ID
   */
  public String getBasePhysicalIfId() {
    return basePhysicalIfId;
  }

  /**
   * Setting physical IF ID.
   *
   * @param ifId
   *          physical IF ID
   */
  public void setBasePhysicalIfId(String ifId) {
    this.basePhysicalIfId = ifId;
  }

  /**
   * Getting IF speed after division.
   *
   * @return speed
   */
  public String getSpeed() {
    return speed;
  }

  /**
   * Setting IF speed after division.
   *
   * @param speed
   *          set speed
   */
  public void setSpeed(String speed) {
    this.speed = speed;
  }

  /**
   * Getting breakoutIF ID list.
   *
   * @return breakoutIfId
   */
  public List<String> getBreakoutIfId() {
    return breakoutIfId;
  }

  /**
   * Setting breakoutIF ID list.
   *
   * @param list
   *          set list
   */
  public void setBreakoutIfId(List<String> list) {
    this.breakoutIfId = list;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "CreateBreakoutIf [nodeId=" + nodeId + ", basePhysicalIfId=" + basePhysicalIfId + ", speed=" + speed
        + ", breakoutIfId=" + breakoutIfId + "]";
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
    if (nodeId == null) {
      throw new CheckDataException();
    }

    if (basePhysicalIfId == null) {
      throw new CheckDataException();
    }

    if (speed == null) {
      throw new CheckDataException();
    }

    if (breakoutIfId == null || breakoutIfId.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (String breakoutIfId : breakoutIfId) {
        if (breakoutIfId == null) {
          throw new CheckDataException();
        }
      }
    }
  }
}
