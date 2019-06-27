/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.ControllerServer;

/**
 * Notificating controller status (server).
 */
public class NotifyEmStatusServer extends AbstractRestMessage {

  /** Server machine resource information. */
  private ControllerServer controller;

  /**
   * Getting server machine resource information.
   *
   * @return server machine resource information.
   */
  public ControllerServer getControllerServer() {
    return controller;
  }

  /**
   * Setting server machine resource information.
   *
   * @param controller
   *          server machine resource information
   */
  public void setControllerServer(ControllerServer controller) {
    this.controller = controller;
  }

  /**
   * Input paramter check.
   *
   * @param ope
   *           operation type
   * @throws CheckDataException
   *           inpt paramter error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (controller == null) {
      throw new CheckDataException();
    } else {
      controller.check(ope);
    }
  }

  @Override
  public String toString() {
    return "NotifyEmStatusServer [controller=" + controller + "]";
  }

}
