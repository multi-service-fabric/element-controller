/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;

/**
 * Physical IF Information Change.
 */
public class UpdatePhysicalInterface extends AbstractRestMessage {

  /** Control Type. */
  private String action;

  /** IF Speed. */
  private String speed;

  /**
   * Getting control type.
   *
   * @return control type
   */
  public String getAction() {
    return action;
  }

  /**
   * Setting control type.
   *
   * @param action
   *          control type
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * Getting IF speed.
   *
   * @return IF speed
   */
  public String getSpeed() {
    return speed;
  }

  /**
   * Setting IF speed.
   *
   * @param speed
   *          IF speed
   */
  public void setSpeed(String speed) {
    this.speed = speed;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "UpdatePhysicalInterface [action=" + action + ", speed=" + speed + "]";
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
    if (action == null) {
      throw new CheckDataException();
    }
    if ((!action.equals("speed_set")) && (!action.equals("speed_delete"))) {
      throw new CheckDataException();
    }

    if (action.equals("speed_set") && speed == null) {
      throw new CheckDataException();
    }
  }
}
