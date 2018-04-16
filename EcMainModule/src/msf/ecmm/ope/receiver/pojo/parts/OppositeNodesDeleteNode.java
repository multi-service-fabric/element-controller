/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * IF Information of Opposing Device for Device Removal.
 */
public class OppositeNodesDeleteNode {

  /** Opposing Device ID. */
  private String nodeId;

  /** Internal Link Information. */
  InternalLinkIfsDeleteNode internalLinkIfs;

  /**
   * Getting opposing device ID.
   *
   * @return opposing device ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting opposing device ID.
   *
   * @param nodeId
   *          opposing device ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting internal link information.
   *
   * @return internal link information
   */
  public InternalLinkIfsDeleteNode getInternalLinkIf() {
    return internalLinkIfs;
  }

  /**
   * Setting internal link information.
   *
   * @param internalLinkIf
   *          internal link information
   */
  public void setInternalLinkIf(InternalLinkIfsDeleteNode internalLinkIf) {
    this.internalLinkIfs = internalLinkIf;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "OppositeNodesDeleteNode [nodeId=" + nodeId + ", internalLinkIfs=" + internalLinkIfs + "]";
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
    if (nodeId == null) {
      throw new CheckDataException();
    }
    if (internalLinkIfs == null) {
      throw new CheckDataException();
    } else {
      internalLinkIfs.check(ope);
    }
  }
}
