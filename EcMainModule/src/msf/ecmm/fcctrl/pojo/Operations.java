/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo;

/**
 * Process Execution Request.
 */
public class Operations extends AbstractRequest {

  /** Process Request Type. */
  private String action;

  /** Optional Information for Logical IF Status Update Process Request. */
  private UpdateLogicalIfStatus updateLogicalIfStatusOption;

  /**
   * Getting process request type.
   *
   * @return process request type
   */
  public String getAction() {
    return action;
  }

  /**
   * Setting process request type.
   *
   * @param action
   *          process request type
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * Getting optional information for logical IF status update process request.
   *
   * @return optional information for logical IF status update process request
   */
  public UpdateLogicalIfStatus getUpdateLogicalIfStatusOption() {
    return updateLogicalIfStatusOption;
  }

  /**
   * Setting optional information for logical IF status update process request.
   *
   * @param updateLogicalIfStatusOption
   *          optional information for logical IF status update process request
   */
  public void setUpdateLogicalIfStatusOption(UpdateLogicalIfStatus updateLogicalIfStatusOption) {
    this.updateLogicalIfStatusOption = updateLogicalIfStatusOption;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "Operations [action=" + action + ", updateLogicalIfStatusOption=" + updateLogicalIfStatusOption + "]";
  }

}
