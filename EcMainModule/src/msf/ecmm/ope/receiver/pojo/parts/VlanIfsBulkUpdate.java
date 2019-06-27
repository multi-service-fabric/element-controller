/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * VLAN IF Infomation to be controlled.
 */
public class VlanIfsBulkUpdate {

  /** VLAN IF ID to be controlled. */
  private String vlanIfId = null;

  /** Device ID. */
  private String nodeId = null;

  /**
   * Getting VLAN IF ID to be controlled.
   *
   * @return VLAN IF ID to be controlled.
   */
  public String getVlanIfId() {
    return vlanIfId;
  }

  /**
   * Setting VLAN IF ID to be controlled.
   *
   * @param vlanIfId
   *          VLAN IF ID to be controlled.
   */
  public void setVlanIfId(String vlanIfId) {
    this.vlanIfId = vlanIfId;
  }

  /**
   * Getting Device ID.
   *
   * @return Device ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting Device ID.
   *
   * @param nodeId
   *          Device ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "VlanIfsDeleteVlanIf [vlanIfId=" + vlanIfId + ", nodeId=" + nodeId + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *         input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (vlanIfId == null) {
      throw new CheckDataException();
    }
    if (nodeId == null) {
      throw new CheckDataException();
    }
  }

}
