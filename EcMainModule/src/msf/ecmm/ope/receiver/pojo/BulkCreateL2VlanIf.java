/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.CreateVlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.UpdateVlanIfs;

/**
 * L2VLAN IF Batch Generate/Change.
 */
public class BulkCreateL2VlanIf extends AbstractRestMessage {

  /** List information of the VLAN IF of which batch generation is to be controlled. */
  private ArrayList<CreateVlanIfs> createVlanIfs;

  /** Unique parameter for each slice ID. */
  private Integer vrfId = null;

  /** List information of the VLANIF of which ESI value is to be changed. */
  private ArrayList<UpdateVlanIfs> updateVlanIfs;

  /**
   * Getting list information of the VLAN IF of which batch generation is to be controlled.
   *
   * @return list information of the VLAN IF of which batch generation is to be controlled
   */
  public ArrayList<CreateVlanIfs> getCreateVlanIfs() {
    return createVlanIfs;
  }

  /**
   * Setting list information of the VLAN IF of which batch generation is to be controlled.
   *
   * @param vlanIfs
   *          list information of the VLAN IF of which batch generation is to be controlled
   */
  public void setCreateVlanIfs(ArrayList<CreateVlanIfs> vlanIfs) {
    this.createVlanIfs = vlanIfs;
  }

  /**
   * Getting unique parameter for each slice ID.
   *
   * @return unique parameter for each slice ID
   */
  public Integer getVrfId() {
    return vrfId;
  }

  /**
   * Setting unique parameter for each slice ID.
   *
   * @param vrfId
   *          unique parameter for each slice ID
   */
  public void setVrfId(Integer vrfId) {
    this.vrfId = vrfId;
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
   *          Set updateVlanIfs
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
    return "BulkCreateL2VlanIf [createVlanIfs=" + createVlanIfs + ", vrfId=" + vrfId + ", updateVlanIfs="
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

    if (createVlanIfs == null && updateVlanIfs == null) {
      throw new CheckDataException();
    }

    if (createVlanIfs != null) {
      for (CreateVlanIfs l2VlanIfs : createVlanIfs) {
        l2VlanIfs.check(ope);
      }
    }

    if (vrfId == null) {
      throw new CheckDataException();
    }

    if (updateVlanIfs != null) {
      for (UpdateVlanIfs l2VlanIfs : updateVlanIfs) {
        l2VlanIfs.check(ope);
      }
    }
  }

}
