/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

/**
 * Server Machine Resource Information.
 */
public class Controller {

  /** Controller Type. */
  private String controller_type;

  /** Occurred Event. */
  private String event;

  /**
   * Getting controller type.
   *
   * @return controller type
   */
  public String getController_type() {
    return controller_type;
  }

  /**
   * Setting controller type.
   *
   * @param controller_type
   *          controller type
   */
  public void setController_type(String controller_type) {
    this.controller_type = controller_type;
  }

  /**
   * Getting occurred event.
   *
   * @return occurred event
   */
  public String getEvent() {
    return event;
  }

  /**
   * Setting occurred event.
   *
   * @param event
   *          occurred event
   */
  public void setEvent(String event) {
    this.event = event;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Controller [controller_type=" + controller_type + ", event=" + event + "]";
  }
}
