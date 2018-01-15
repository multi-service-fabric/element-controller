/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Process Execution Request.
 */
public class Operations extends AbstractRestMessage {

  /** Process Request Type. */
  private String action;

  /** Optional Information for L2VLAN IF Generation Process Request. */
  @SerializedName("create_update_l2vlan_if_option")
  private BulkCreateL2VlanIf createUpdateL2VlanIfOption;

  /** Optional Information for L2VLAN IF Deletion Process Request. */
  @SerializedName("delete_update_l2vlan_if_option")
  private BulkDeleteL2VlanIf deleteUpdateL2VlanIfOption;

  /** Optional Information for L3VLAN IF Generation Process Request. */
  @SerializedName("create_l3vlan_if_option")
  private BulkCreateL3VlanIf createL3VlanIfOption;

  /** Optional Information for L3VLAN IF Deletion Process Request. */
  @SerializedName("delete_l3vlan_if_option")
  private BulkDeleteL3VlanIf deleteL3VlanIfOption;

  /** Optional Information for breakoutIF Registration Process Request. */
  private CreateBreakoutIf registerBreakoutIfOption;

  /** Optional Information for breakoutIF Deletion Process Request. */
  private DeleteBreakoutIf deleteBreakoutIfOption;

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
   * Getting Optional Information for L2VLAN IF Generation Process Request.
   *
   * @return Optional Information for L2VLAN IF Generation Process Request
   */
  public BulkCreateL2VlanIf getCreateUpdateL2VlanIfOption() {
    return createUpdateL2VlanIfOption;
  }

  /**
   * Setting Optional Information for L2VLAN IF Generation Process Request.
   *
   * @param option
   *          Optional Information for L2VLAN IF Generation Process Request
   */
  public void setCreateUpdateL2VlanIfOption(BulkCreateL2VlanIf option) {
    this.createUpdateL2VlanIfOption = option;
  }

  /**
   * Getting Optional Information for L2VLAN IF Deletion Process Request.
   *
   * @return Optional Information for L2VLAN IF Deletion Process Request
   */
  public BulkDeleteL2VlanIf getDeleteUpdateL2VlanIfOption() {
    return deleteUpdateL2VlanIfOption;
  }

  /**
   * Setting Optional Information for L2VLAN IF Deletion Process Request.
   *
   * @param option
   *          Optional Information for L2VLAN IF Deletion Process Request
   */
  public void setDeleteUpdateL2VlanIfOption(BulkDeleteL2VlanIf option) {
    this.deleteUpdateL2VlanIfOption = option;
  }

  /**
   * Getting Optional Information for L3VLAN IF Generation Process Request.
   *
   * @return Optional Information for L3VLAN IF Generation Process Request
   */
  public BulkCreateL3VlanIf getCreateL3VlanIfOption() {
    return createL3VlanIfOption;
  }

  /**
   * Setting Optional Information for L3VLAN IF Generation Process Request.
   *
   * @param option
   *          Optional Information for L3VLAN IF Generation Process Request
   */
  public void setCreateL3VlanIfOption(BulkCreateL3VlanIf option) {
    this.createL3VlanIfOption = option;
  }

  /**
   * Getting Optional Information for L3VLAN IF Deletion Process Request.
   *
   * @return Optional Information for L3VLAN IF Deletion Process Request
   */
  public BulkDeleteL3VlanIf getDeleteL3VlanIfOption() {
    return deleteL3VlanIfOption;
  }

  /**
   * Setting Optional Information for L3VLAN IF Deletion Process Request.
   *
   * @param option
   *          Optional Information for L3VLAN IF Deletion Process Request
   */
  public void setDeleteL3VlanIfOption(BulkDeleteL3VlanIf option) {
    this.deleteL3VlanIfOption = option;
  }

  /**
   * Getting Optional Information for breakoutIF Registration Process Request.
   *
   * @return registerBreakoutIfOption
   */
  public CreateBreakoutIf getRegisterBreakoutIfOption() {
    return registerBreakoutIfOption;
  }

  /**
   * Setting Optional Information for breakoutIF Registration Process Request.
   *
   * @param option
   *          set option
   */
  public void setRegisterBreakoutIfOption(CreateBreakoutIf option) {
    this.registerBreakoutIfOption = option;
  }

  /**
   * Getting Optional Information for breakoutIF Deletion Process Request.
   *
   * @return deleteBreakoutIfOption
   */
  public DeleteBreakoutIf getDeleteBreakoutIfOption() {
    return deleteBreakoutIfOption;
  }

  /**
   * Setting Optional Information for breakoutIF Deletion Process Request.
   *
   * @param option
   *          set option
   */
  public void setDeleteBreakoutIfOption(DeleteBreakoutIf option) {
    this.deleteBreakoutIfOption = option;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "Operations [action=" + action + ", createUpdateL2VlanIfOption=" + createUpdateL2VlanIfOption
        + ", deleteUpdateL2VlanIfOption=" + deleteUpdateL2VlanIfOption + ", createL3VlanIfOption="
        + createL3VlanIfOption + ", deleteL3VlanIfOption=" + deleteL3VlanIfOption + ", registerBreakoutIfOption="
        + registerBreakoutIfOption + ", deleteBreakoutIfOption=" + deleteBreakoutIfOption + "]";
  }

}
