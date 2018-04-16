/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsDeleteVlanIf;

/**
 * L3VLAN IF Batch Deletion.
 */
public class BulkDeleteL3VlanIf extends AbstractRestMessage {

  /** Information of VLAN IF to be controlled. */
  private ArrayList<VlanIfsDeleteVlanIf> vlanIfs = new ArrayList<VlanIfsDeleteVlanIf>();

  /** Unique parameter for each slice ID. */
  private String vrfId;

  /**
   * Getting information of VLAN IF to be controlled.
   *
   * @return list information of the CP to be controlled
   */
  public ArrayList<VlanIfsDeleteVlanIf> getVlanIfs() {
    return vlanIfs;
  }

  /**
   * Setting information of VLAN IF to be controlled.
   *
   * @param vlanIfs
   *          information of VLAN IF to be controlled
   */
  public void setVlanIfs(ArrayList<VlanIfsDeleteVlanIf> vlanIfs) {
    this.vlanIfs = vlanIfs;
  }

  /**
   * Getting unique parameter for each slice ID.
   *
   * @return unique parameter for each slice ID
   */
  public String getVrfId() {
    return vrfId;
  }

  /**
   * Setting unique parameter for each slice ID.
   *
   * @param vrfId
   *          unique parameter for each slice ID
   */
  public void setVrfId(String vrfId) {
    this.vrfId = vrfId;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "BulkDeleteL3VlanIf [vlanIfs=" + vlanIfs + ", vrfId=" + vrfId + "]";
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
    if (vlanIfs.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (VlanIfsDeleteVlanIf vlanIfsDeleteVlanIf : vlanIfs) {
        vlanIfsDeleteVlanIf.check(ope);
      }
    }
    if (vrfId == null) {
      throw new CheckDataException();
    }
  }

}
