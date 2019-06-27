/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsBulkUpdate;

/**
 * L3VLAN IF Batch Change.
 */
public class BulkUpdateL3VlanIf extends AbstractRestMessage {

  /** Unique parameter for each slice ID. */
  private String vrfId = null;

  /** List information of the VLAN IF is to be controlled. */
  private ArrayList<VlanIfsBulkUpdate> vlanIfs;

  /** Remark Menu. */
  private String remarkMenu;

  /**
   * Getting Unique parameter for each slice ID.
   *
   * @return unique parameter for each slice ID
   */
  public String getVrfId() {
    return vrfId;
  }

  /**
   * Setting Unique parameter for each slice ID.
   *
   * @param vrfId
   *         unique parameter for each slice ID
   */
  public void setVrfId(String vrfId) {
    this.vrfId = vrfId;
  }

  /**
   * Getting List information of the VLAN IF is to be controlled.
   *
   * @return List information of the VLAN IF is to be controlled
   */
  public ArrayList<VlanIfsBulkUpdate> getVlanIfs() {
    return vlanIfs;
  }

  /**
   * Setting List information of the VLAN IF is to be controlled.
   *
   * @param vlanIfs
   *         List information of the VLAN IF is to be controlled
   */
  public void setVlanIfs(ArrayList<VlanIfsBulkUpdate> vlanIfs) {
    this.vlanIfs = vlanIfs;
  }

  /**
   * Getting Remark Menu.
   *
   * @return remarkMenu
   */
  public String getRemarkMenu() {
    return remarkMenu;
  }

  /**
   * Setting Remark Menu.
   *
   * @param remarkMenu
   *          setting remarkMenu
   */
  public void setRemarkMenu(String remarkMenu) {
    this.remarkMenu = remarkMenu;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "BulkCreateL3VlanIf [vlanIfs=" + vlanIfs + ", vrfId=" + vrfId + ", remarkMenu=" + remarkMenu + "]";
  }

 /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (vrfId == null) {
      throw new CheckDataException();
    }

    if (vlanIfs != null) {
      for (VlanIfsBulkUpdate l3VlanIfs : vlanIfs) {
        l3VlanIfs.check(ope);
      }
    }

  }

}
