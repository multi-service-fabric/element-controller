/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo;

import msf.ecmm.fcctrl.pojo.parts.Controller;

/**
 * Controler Status Notification.
 */
public class ControllerStatusToFc extends AbstractRequest {

  /** Server Machine Resource Information. */
  private Controller controller;

   /**
   * Getting server machine resource information.
   * @return server machine resource information
   */
  public Controller getController() {
    return controller;
  }

  /**
   * Setting server machine resource information.
   * @param controller server machine resource information
   */
  public void setController(Controller controller) {
    this.controller = controller;
  }

  @Override
  public String toString() {
    return "ControllerStatusToFc [controller=" + controller + "]";
  }

}
