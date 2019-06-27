/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Process Execution Request.
 */
public class Operations extends AbstractRestMessage {

  /** Process Request Type. */
  private String action;

  /** Optional information for L2VLAN IF batch generation process request. */
  @SerializedName("create_update_l2vlan_if_option")
  private BulkCreateL2VlanIf createUpdateL2VlanIfOption;

  /** Optional information for L2VLAN IF batch deletion process request. */
  @SerializedName("delete_update_l2vlan_if_option")
  private BulkDeleteL2VlanIf deleteUpdateL2VlanIfOption;

  /** Optional information for L3VLAN IF batch generation process request. */
  @SerializedName("create_l3vlan_if_option")
  private BulkCreateL3VlanIf createL3VlanIfOption;

  /** Optional information for L3VLAN IF batch deletion process request. */
  @SerializedName("delete_l3vlan_if_option")
  private BulkDeleteL3VlanIf deleteL3VlanIfOption;

  /** Optional information for L2VLAN IF batch change process request. */
  @SerializedName("update_l2vlan_if_option")
  private BulkUpdateL2VlanIf updateL2VlanIfOption;

  /** Optional information for L2VLAN IF batch change process request. */
  @SerializedName("update_l3vlan_if_option")
  private BulkUpdateL3VlanIf updateL3VlanIfOption;

  /** Optional information for breakoutIF registratioin process request. */
  private CreateBreakoutIf registerBreakoutIfOption;

  /** Optional information for breakoutIF deletion process request. */
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
   * Getting optional information for L2VLAN IF generation process request.
   *
   * @return  Optional information for L2VLAN IF generation process request
   */
  public BulkCreateL2VlanIf getCreateUpdateL2VlanIfOption() {
    return createUpdateL2VlanIfOption;
  }

  /**
   * Setting optional information for L2VLAN IF generation process request.
   *
   * @param option
   *          Optional information for L2VLAN IF generation process request
   */
  public void setCreateUpdateL2VlanIfOption(BulkCreateL2VlanIf option) {
    this.createUpdateL2VlanIfOption = option;
  }

  /**
   * Getting optional information for L2VLAN IF deletion process request.
   *
   * @return optional information for L2VLAN IF deletion process request
   */
  public BulkDeleteL2VlanIf getDeleteUpdateL2VlanIfOption() {
    return deleteUpdateL2VlanIfOption;
  }

  /**
   * Setting optional information for L2VLAN IF deletion process request.
   *
   * @param option
   *          optional information for L2VLAN IF deletion process request
   */
  public void setDeleteUpdateL2VlanIfOption(BulkDeleteL2VlanIf option) {
    this.deleteUpdateL2VlanIfOption = option;
  }

  /**
   * Getting optional information for L3VLAN IF generation process request.
   *
   * @return optional information for L3VLAN IF generation process request
   */
  public BulkCreateL3VlanIf getCreateL3VlanIfOption() {
    return createL3VlanIfOption;
  }

  /**
   * Setting optional information for L3VLAN IF generation process request.
   *
   * @param option
   *          optional information for L3VLAN IF generation process request
   */
  public void setCreateL3VlanIfOption(BulkCreateL3VlanIf option) {
    this.createL3VlanIfOption = option;
  }

  /**
   * Getting optional information for L3VLAN IF deletion process request.
   *
   * @return optional information for L3VLAN IF deletion process request
   */
  public BulkDeleteL3VlanIf getDeleteL3VlanIfOption() {
    return deleteL3VlanIfOption;
  }

  /**
   * Setting optional information for L3VLAN IF deletion process request.
   *
   * @param option
   *          optional information for L3VLAN IF deletion process request
   */
  public void setDeleteL3VlanIfOption(BulkDeleteL3VlanIf option) {
    this.deleteL3VlanIfOption = option;
  }

  /**
   * Getting optional information for L2VLAN IF batch change process request.
   *
   * @return updateL2VlanIfOption
   */
  public BulkUpdateL2VlanIf getUpdateL2VlanIfOption() {
    return updateL2VlanIfOption;
  }

  /**
   * Setting optional information for L2VLAN IF batch change process request.
   *
   * @param updateL2VlanIfOption
   *          Setting updateL2VlanIfOption
   */
  public void setUpdateL2VlanIfOption(BulkUpdateL2VlanIf updateL2VlanIfOption) {
    this.updateL2VlanIfOption = updateL2VlanIfOption;
  }

  /**
   * Getting optional information for L3VLAN IF batch change process request.
   *
   * @return updateL3VlanIfOption
   */
  public BulkUpdateL3VlanIf getUpdateL3VlanIfOption() {
    return updateL3VlanIfOption;
  }

  /**
   * Setting optional information for L3VLAN IF batch change process request.
   *
   * @param updateL3VlanIfOption
   *          Setting updateL3VlanIfOption
   */
  public void setUpdateL3VlanIfOption(BulkUpdateL3VlanIf updateL3VlanIfOption) {
    this.updateL3VlanIfOption = updateL3VlanIfOption;
  }

  /**
   * Getting optional information for breakoutIF addition processing request.
   *
   * @return registerBreakoutIfOption
   */
  public CreateBreakoutIf getRegisterBreakoutIfOption() {
    return registerBreakoutIfOption;
  }

  /**
   * Setting optional information for breakoutIF addition process request.
   *
   * @param option
   *          Setting option
   */
  public void setRegisterBreakoutIfOption(CreateBreakoutIf option) {
    this.registerBreakoutIfOption = option;
  }

  /**
   * Getting optional information for breakoutIF deletion process request.
   *
   * @return deleteBreakoutIfOption
   */
  public DeleteBreakoutIf getDeleteBreakoutIfOption() {
    return deleteBreakoutIfOption;
  }

  /**
   * Setting optional information for breakoutIF deletion process request.
   *
   * @param option
   *          Setting option
   */
  public void setDeleteBreakoutIfOption(DeleteBreakoutIf option) {
    this.deleteBreakoutIfOption = option;
  }

  /*
   * Stringizing instance
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Operations [action=" + action + ", createUpdateL2VlanIfOption=" + createUpdateL2VlanIfOption
        + ", deleteUpdateL2VlanIfOption=" + deleteUpdateL2VlanIfOption + ", createL3VlanIfOption="
        + createL3VlanIfOption + ", deleteL3VlanIfOption=" + deleteL3VlanIfOption + ", updateL2VlanIfOption="
        + updateL2VlanIfOption + ", updateL3VlanIfOption=" + updateL3VlanIfOption + ", registerBreakoutIfOption="
        + registerBreakoutIfOption + ", deleteBreakoutIfOption=" + deleteBreakoutIfOption + "]";
  }

}
