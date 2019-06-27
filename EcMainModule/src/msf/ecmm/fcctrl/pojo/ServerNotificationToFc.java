/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo;

import msf.ecmm.fcctrl.pojo.parts.ControllerServerToFc;

/**
 * Notified cotroller status (server).
 */
public class ServerNotificationToFc extends AbstractRequest {

  /** Server machine resource information. */
  private ControllerServerToFc controller;

  /**
   * Getting server machine resource information.
   *
   * @return server machine resource information
   */
  public ControllerServerToFc getController() {
    return controller;
  }

  /**
   * Setting server machine resource information..
   *
   * @param controller
   *          erver machine resource information
   */
  public void setController(ControllerServerToFc controller) {
    this.controller = controller;
  }

  /**
   *  Stringizing Instance.
   *
   * @return  instance string
   */
  @Override
  public String toString() {
    return "ServerNotificationToFc [controller=" + controller + "]";
  }
}
