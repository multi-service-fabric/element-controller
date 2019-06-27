/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Internal Link IF Change/IF Information to be generated.
 */
public class UpdateInternalLinkIfs {

  /** Device ID. */
  private String nodeId;

  /** Internal Link IF Information. */
  private InternalLinkIfInternalLinkChange internalLinkIf;

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
   * Getting Internal Link IF Information.
   *
   * @return Internal Link IF Information
   */
  public InternalLinkIfInternalLinkChange getInternalLinkIf() {
    return internalLinkIf;
  }

  /**
   * Setting Internal Link IF Information.
   *
   * @param internalLinkIf
   *          Internal Link IF Information
   */
  public void setInternalLinkIf(InternalLinkIfInternalLinkChange internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  /**
   * Stringizing instance.
   *
   * @return Instance string
   */
  @Override
  public String toString() {
    return "TargetIfInternalLinkChange [nodeId=" + nodeId + ", internalLinkIf=" + internalLinkIf + "]";
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
    if (nodeId == null) {
      throw new CheckDataException();
    }

    if (internalLinkIf == null) {
      throw new CheckDataException();
    } else {
      internalLinkIf.check(ope);
    }
  }
}
