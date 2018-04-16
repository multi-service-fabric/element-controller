/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsCreateL3VlanIf;

/**
 * L3VLAN IF Batch Generation.
 */
public class BulkCreateL3VlanIf extends AbstractRestMessage {

  /** Information of VLAN IF to be controlled. */
  private ArrayList<VlanIfsCreateL3VlanIf> vlanIfs = new ArrayList<VlanIfsCreateL3VlanIf>();

  /** Unique parameter for each slice ID. */
  private Integer vrfId = null;

  /** Plane where the slice belongs to. */
  private Integer plane = null;

  /**
   * Constructor.
   */
  public BulkCreateL3VlanIf() {
    super();
  }

  /**
   * Getting information of VLAN IF to be controlled.
   *
   * @return list information of CP to be controlled
   */
  public ArrayList<VlanIfsCreateL3VlanIf> getVlanIfs() {
    return vlanIfs;
  }

  /**
   * Setting information of VLAN IF to be controlled.
   *
   * @param vlanIfsCreateL3VlanIf
   *          list information of CP to be controlled
   */
  public void setVlanIfs(ArrayList<VlanIfsCreateL3VlanIf> vlanIfsCreateL3VlanIf) {
    this.vlanIfs = vlanIfsCreateL3VlanIf;
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
   * Getting plane where the slice belongs to.
   *
   * @return plane where the slice belongs to
   */
  public Integer getPlane() {
    return plane;
  }

  /**
   * Setting plane where the slice belongs to.
   *
   * @param plane
   *          plane where the slice belongs to
   */
  public void setPlane(Integer plane) {
    this.plane = plane;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "BulkCreateL3VlanIf [vlanIfs=" + vlanIfs + ", vrfId=" + vrfId + ", plane=" + plane + "]";
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
      for (VlanIfsCreateL3VlanIf vlanIfsCreateL3VlanIf : vlanIfs) {
        vlanIfsCreateL3VlanIf.check(ope);
      }
    }
    if (vrfId == null) {
      throw new CheckDataException();
    }
    if (plane == null) {
      throw new CheckDataException();
    }

  }

}
