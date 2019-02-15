/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.RemoveUpdateVlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsDeleteVlanIf;

/**
 * L2VLAN IF Batch Delete/Change.
 */
public class BulkDeleteL2VlanIf extends AbstractRestMessage {

  /** Unique parametr for each slice ID. */
  private String vrfId;

  /** VN value. */
  private Integer vni = null;

  /** List information of VLANIF of which batch deletion is to be controlled. */
  @SerializedName("delete_vlan_ifs")
  private ArrayList<VlanIfsDeleteVlanIf> vlanIfsDeleteVlanIf;

  /** List information of VLANIF of which batch change is to be done. */
  private ArrayList<RemoveUpdateVlanIfs> updateVlanIfs;

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
   * Getting VN value.
   *
   * @return vni
   */
  public Integer getVni() {
    return vni;
  }

  /**
   * Setting VN value.
   *
   * @param vni
   *          Setting vni
   */
  public void setVni(Integer vni) {
    this.vni = vni;
  }

  /**
   * Getting List information of CP to be controlled.
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
   * Getting list of VLANIF of which batch change is to be done.
   *
   * @return updateVlanIfs
   */
  public ArrayList<RemoveUpdateVlanIfs> getUpdateVlanIfs() {
    return updateVlanIfs;
  }

  /**
   * Setting VLANIF of which batch change is to be done.
   *
   * @param updateVlanIfs
   *          Setting updateVlanIfs
   */
  public void setUpdateVlanIfs(ArrayList<RemoveUpdateVlanIfs> updateVlanIfs) {
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
        + updateVlanIfs + ", vni=" + vni + "]";
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
    if (vlanIfsDeleteVlanIf == null && updateVlanIfs == null) {
      throw new CheckDataException();
    }

    if (vlanIfsDeleteVlanIf != null) {
      for (VlanIfsDeleteVlanIf l2VlanIfs : vlanIfsDeleteVlanIf) {
        l2VlanIfs.check(ope);
      }
    }

    if (updateVlanIfs != null) {
      for (RemoveUpdateVlanIfs l2VlanIfs : updateVlanIfs) {
        l2VlanIfs.check(ope);
      }
    }
  }

}
