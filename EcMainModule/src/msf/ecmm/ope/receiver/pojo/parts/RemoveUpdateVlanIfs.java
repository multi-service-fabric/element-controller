/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * List Information of VLANIF of which ESI value is to be changed. For batch deletion/change
 */
public class RemoveUpdateVlanIfs {
  /** ID of VLAN IF to be controlled. */
  private String vlanIfId = null;

  /** Device ID. */
  private String nodeId = null;

  /** Flag to be changed for dummy. */
  private boolean dummyFlag = false;

  /** clag-id value to be set. */
  private Integer clagId = null;

  /** ESI value. */
  private String esi = null;

  /** LACP system-id. */
  private String lacpSystemId = null;

  /**
   * Getting ID of VLAN IF to be controlled.
   *
   * @return vlanIfId
   */
  public String getVlanIfId() {
    return vlanIfId;
  }

  /**
   * Set ID of VLAN IF to be controlled.
   *
   * @param vlanIfId
   *          Setting vlanIfId
   */
  public void setVlanIfId(String vlanIfId) {
    this.vlanIfId = vlanIfId;
  }

  /**
   * Getting device ID.
   *
   * @return nodeId
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting device ID.
   *
   * @param nodeId
   *          Setting nodeId
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting ESI value.
   *
   * @return esi
   */
  public String getEsi() {
    return esi;
  }

  /**
   * Setting ESI value.
   *
   * @param esi
   *          Setting esi
   */
  public void setEsi(String esi) {
    this.esi = esi;
  }

  /**
   * Getting LACP system-id.
   *
   * @return lacpSystemId
   */
  public String getLacpSystemId() {
    return lacpSystemId;
  }

  /**
   * Setting LACP system-id.
   *
   * @param lacpSystemId
   *          Setting lacpSystemId
   */
  public void setLacpSystemId(String lacpSystemId) {
    this.lacpSystemId = lacpSystemId;
  }

  /**
   * Getting clag-id value.
   *
   * @return clagId
   */
  public Integer getClagId() {
    return clagId;
  }

  /**
   * Setting clag-id value.
   *
   * @param clagId
   *          Setting clagId
   */
  public void setClagId(int clagId) {
    this.clagId = clagId;
  }

  /**
   * Getting flag to be changed for dummy IRB.
   *
   * @return dummyFlag
   */
  public boolean getDummyFlag() {
    return dummyFlag;
  }

  /**
   * Setting flag to be changed for dummy IRB.
   *
   * @param dummyFlag
   *          Setting dummyFlag
   */
  public void setDummyFlag(boolean dummyFlag) {
    this.dummyFlag = dummyFlag;
  }

  @Override
  public String toString() {
    return "RemoveUpdateVlanIfs [vlanIfId=" + vlanIfId + ", nodeId=" + nodeId + ", dummyFlag=" + dummyFlag + ", clagId="
        + clagId + ", esi=" + esi + ", lacpSystemId=" + lacpSystemId + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          Operation type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (vlanIfId == null) {
      throw new CheckDataException();
    }
    if (nodeId == null) {
      throw new CheckDataException();
    }
    if (!dummyFlag) {
      if (esi == null) {
        throw new CheckDataException();
      }
      if (lacpSystemId == null) {
        throw new CheckDataException();
      }
    }
  }

}
