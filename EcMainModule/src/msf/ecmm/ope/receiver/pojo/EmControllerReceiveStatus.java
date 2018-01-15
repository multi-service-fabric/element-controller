/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.Controller;

/**
 * EM Status Notification
 */
public class EmControllerReceiveStatus extends AbstractRestMessage {

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

  /**
   * Input Parameter Check
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (controller.getController_type() == null || controller.getController_type().length() == 0
        || controller.getEvent() == null || controller.getEvent().length() == 0) {
      throw new CheckDataException();
    }
  }

  @Override
  public String toString() {
    return "EmControllerReceiveStatus [controller=" + controller + "]";
  }

}
