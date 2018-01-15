/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Server Machine Resource Information
 */
public class Controller {

  /** Controller Type */
  private String controller_type;

  /** Occurring Event */
  private String event;

  /**
  * Getting controller type.
  * @return controller type
  */
  public String getController_type() {
    return controller_type;
  }

  /**
   * Setting controller type.
   * @param controller_type controller type
   */
  public void setController_type(String controller_type) {
    this.controller_type = controller_type;
  }

  /**
  * Getting occurring event.
  * @return occurring event
  */
  public String getEvent() {
    return event;
  }

  /**
   * Setting occurring event.
   * @param event occurring event
   */
  public void setEvent(String event) {
    this.event = event;
  }
}
