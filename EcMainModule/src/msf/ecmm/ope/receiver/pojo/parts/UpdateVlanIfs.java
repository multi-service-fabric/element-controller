/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * List Information of VLANIF of which ESI value is to be changed.
 */
public class UpdateVlanIfs {

  /** ID of VLAN IF to be controlled. */
  private String vlanIfId = null;

  /** Device ID. */
  private String nodeId = null;

  /** ESI Value. */
  private String esi = null;

  /** LACP system-id. */
  private String lacpSystemId = null;

  /**
   * Getting the ID of VLAN IF to be controlled.
   *
   * @return vlanIfId
   */
  public String getVlanIfId() {
    return vlanIfId;
  }

  /**
   * Setting the ID of VLAN IF to be controlled.
   *
   * @param vlanIfId
   *          set vlanIfId
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
   *          set nodeId
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
   *          set esi
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
   *          set lacpSystemId
   */
  public void setLacpSystemId(String lacpSystemId) {
    this.lacpSystemId = lacpSystemId;
  }

  /**
   * Stringizing Instance.
   *
   */
  @Override
  public String toString() {
    return "UpdateVlanIfs [vlanIfId=" + vlanIfId + ", nodeId=" + nodeId + ", esi=" + esi + ", lacpSystemId="
        + lacpSystemId + "]";
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
    if (vlanIfId == null) {
      throw new CheckDataException();
    }
    if (nodeId == null) {
      throw new CheckDataException();
    }
    if (esi == null) {
      throw new CheckDataException();
    }
    if (lacpSystemId == null) {
      throw new CheckDataException();
    }
  }

}
