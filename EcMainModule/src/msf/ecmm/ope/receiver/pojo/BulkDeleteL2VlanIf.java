/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.UpdateVlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsDeleteVlanIf;

/**
 * L2VLAN IF Batch Delete/Change.
 */
public class BulkDeleteL2VlanIf extends AbstractRestMessage {

  /** Unique parametr for each slice ID. */
  private String vrfId;

  /** List information of the VLANIF of which batch deletion is to be controlled. */
  @SerializedName("delete_vlan_ifs")
  private ArrayList<VlanIfsDeleteVlanIf> vlanIfsDeleteVlanIf;

  /** List information of the VLANIF of which ESI value is to be changed. */
  private ArrayList<UpdateVlanIfs> updateVlanIfs;

  /**
   * Getting unique parametr for each slice ID.
   *
   * @return vrfId
   */
  public String getVrfId() {
    return vrfId;
  }

  /**
   * Setting unique parametr for each slice ID.
   *
   * @param vrfId
   *          set vrfId
   */
  public void setVrfId(String vrfId) {
    this.vrfId = vrfId;
  }

  /**
   * Getting list information of the CP to be controlled.
   *
   * @return list information of the CP to be controlled
   */
  public ArrayList<VlanIfsDeleteVlanIf> getDeleteVlanIfs() {
    return vlanIfsDeleteVlanIf;
  }

  /**
   * Setting list information of the CP to be controlled.
   *
   * @param vlanIfsDeleteVlanIf
   *          list information of the CP to be controlled
   */
  public void setDeleteVlanIfs(ArrayList<VlanIfsDeleteVlanIf> vlanIfsDeleteVlanIf) {
    this.vlanIfsDeleteVlanIf = vlanIfsDeleteVlanIf;
  }

  /**
   * Getting list information of the VLANIF of which ESI value is to be changed.
   *
   * @return updateVlanIfs
   */
  public ArrayList<UpdateVlanIfs> getUpdateVlanIfs() {
    return updateVlanIfs;
  }

  /**
   * Setting list information of the VLANIF of which ESI value is to be changed.
   *
   * @param updateVlanIfs
   *          set updateVlanIfs
   */
  public void setUpdateVlanIfs(ArrayList<UpdateVlanIfs> updateVlanIfs) {
    this.updateVlanIfs = updateVlanIfs;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "BulkDeleteL2VlanIf [vrfId=" + vrfId + ", vlanIfsDeleteVlanIf=" + vlanIfsDeleteVlanIf + ", updateVlanIfs="
        + updateVlanIfs + "]";
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

    if (vlanIfsDeleteVlanIf == null && updateVlanIfs == null) {
      throw new CheckDataException();
    }

    if (vlanIfsDeleteVlanIf != null) {
      for (VlanIfsDeleteVlanIf l2VlanIfs : vlanIfsDeleteVlanIf) {
        l2VlanIfs.check(ope);
      }
    }

    if (updateVlanIfs != null) {
      for (UpdateVlanIfs l2VlanIfs : updateVlanIfs) {
        l2VlanIfs.check(ope);
      }
    }
  }

}
