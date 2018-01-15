/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Process Execution Request (Device Extention/Removal).
 */
public class AddDeleteNode extends AbstractRestMessage {

  /** Process Request Type. */
  private String action;

  /** Device Extention Optional information for change request. */
  private AddNode addNodeOption;

  /** Device Removal Optional information for change request. */
  @SerializedName("del_node_option")
  private DeleteNode deleteNodeOption;

  /** Optional information for device change request. */
  @SerializedName("update_node_option")
  private ChangeNode changeNodeOption;

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
   * Getting device extention optional information for change request.
   *
   * @return device extention optional information for change request
   */
  public AddNode getAddNodeOption() {
    return addNodeOption;
  }

  /**
   * Setting device extention optional information for change request.
   *
   * @param addNodeOption
   *          device extention optional information for change request
   */
  public void setAddNodeOption(AddNode addNodeOption) {
    this.addNodeOption = addNodeOption;
  }

  /**
   * Getting device removal optional information for change request.
   *
   * @return device removal optional information for change request
   */
  public DeleteNode getDeleteNodeOption() {
    return deleteNodeOption;
  }

  /**
   * Setting device removal optional information for change request.
   *
   * @param deleteNodeOption
   *          device removal optional information for change request
   */
  public void setDeleteNodeOption(DeleteNode deleteNodeOption) {
    this.deleteNodeOption = deleteNodeOption;
  }

  /**
   * Getting optional information for device change request.
   *
   * @return optional information for device change request
   */
  public ChangeNode getChangeNodeOption() {
    return changeNodeOption;
  }

  /**
   * Setting optional information for device change request.
   *
   * @param changeNodeOption
   *          optional information for device change request
   */
  public void setChangeNodeOption(ChangeNode changeNodeOption) {
    this.changeNodeOption = changeNodeOption;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "AddDeleteNode [action=" + action + ", addNodeOption=" + addNodeOption + ", deleteNodeOption="
        + deleteNodeOption + ", changeNodeOption=" + changeNodeOption + "]";
  }
}
