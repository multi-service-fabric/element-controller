/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo;

import msf.ecmm.fcctrl.pojo.parts.ControllerLogToFc;

/**
 * Notified controller status(log).
 */
public class LogNotificationToFc extends AbstractRequest {

  /** Server machine resource information */
  private ControllerLogToFc controller;

  /**
   * Getting Server machine resource information.
   *
   * @return Server machine resource information
   */
  public ControllerLogToFc getController() {
    return controller;
  }

  /**
   * Getting Server machine resource information.
   *
   * @param controller
   *          Server machine resource information
   */
  public void setController(ControllerLogToFc controller) {
    this.controller = controller;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance  string
   */
  @Override
  public String toString() {
    return "LogNotificationToFc [controllerLog=" + controller + "]";
  }
}
