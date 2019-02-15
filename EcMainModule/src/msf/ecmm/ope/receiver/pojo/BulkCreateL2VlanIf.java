/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.CreateVlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.L3VniValue;
import msf.ecmm.ope.receiver.pojo.parts.LoopBackInterface;
import msf.ecmm.ope.receiver.pojo.parts.UpdateVlanIfs;

/**
 * L2VLAN IF Batch Generate/Change.
 */
public class BulkCreateL2VlanIf extends AbstractRestMessage {

  /** List information of the VLAN IF of which batch generation is to be controlled. */
  private ArrayList<CreateVlanIfs> createVlanIfs;

  /** Unique parameter for each slice ID. */
  private Integer vrfId = null;

  /** VN value. */
  private Integer vni = null;

  /** Plane where the  slice belongs to. **/
  private Integer plane = null;

  /** Parameter required for L3VNI generation. */
  private L3VniValue l3Vni = null;

  /** Loopback in slice(VRF). */
  private LoopBackInterface loopbackInterface = null;

  /** List information of VLANIF of which ESI value is to be changed. */
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
   * Getting plane where the slice belongs to.
   *
   * @return plane
   */
  public Integer getPlane() {
    return plane;
  }

  /**
   * Setting plane where the slice belongs to.
   *
   * @param plane
   *          Setting plane
   */
  public void setPlane(Integer plane) {
    this.plane = plane;
  }

  /**
   * Getting parameter required for L3VNI generation.
   *
   * @return l3Vni
   */
  public L3VniValue getL3Vni() {
    return l3Vni;
  }

  /**
   * Setting parameter required for L3VNI generation.
   *
   * @param l3Vni
   *          Setting l3Vni
   */
  public void setL3Vni(L3VniValue l3Vni) {
    this.l3Vni = l3Vni;
  }

  /**
   * Getting parameter required for L3VNI generation.
   *
   * @return loopbackInterface
   */
  public LoopBackInterface getLoopBackInterface() {
    return loopbackInterface;
  }

  /**
   * Setting parameter required for L3VNI generation.
   *
   * @param loopbackInterface
   *          Setting loopbackInterface
   */
  public void setLoopBackInterface(LoopBackInterface loopbackInterface) {
    this.loopbackInterface = loopbackInterface;
  }

  /**
   * Getting list information of VLANIF of which ESI value is to be changed.
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
        + updateVlanIfs + ", vni=" + vni + ",plane=" + plane + ", l3Vni=" + l3Vni + ", loopbackInterface="
        + loopbackInterface + "]";
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
    if (l3Vni != null) {
      l3Vni.check(ope);
    }
    if (loopbackInterface != null) {
      loopbackInterface.check(ope);
    }

    if (updateVlanIfs != null) {
      for (UpdateVlanIfs l2VlanIfs : updateVlanIfs) {
        l2VlanIfs.check(ope);
      }
    }
  }

}
