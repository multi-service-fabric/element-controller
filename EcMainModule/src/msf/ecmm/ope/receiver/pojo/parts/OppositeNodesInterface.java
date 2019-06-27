/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Device Extention/Change Opposing Device.
 */
public class OppositeNodesInterface {

  /** Opposing Device ID. */
  private String nodeId;

  /** breakoutIF Information. */
  private List<BreakoutBaseIf> breakoutBaseIfs = new ArrayList<>();

  /** Internal Link Information List. */
  private InternalLinkIf internalLinkIf;

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
   * Getting breakoutIF information.
   *
   * @return breakoutIF information.
   */
  public List<BreakoutBaseIf> getBreakoutBaseIfs() {
    return breakoutBaseIfs;
  }

  /**
   * Setting breakoutIF information.
   *
   * @param breakoutBaseIfs
   *          breakoutIF information.
   */
  public void setBreakoutBaseIfs(List<BreakoutBaseIf> breakoutBaseIfs) {
    this.breakoutBaseIfs = breakoutBaseIfs;
  }

  /**
   * Getting internal link information list.
   *
   * @return internal link information list.
   */
  public InternalLinkIf getInternalLinkIf() {
    return internalLinkIf;
  }

  /**
   * Setting internal link information list.
   *
   * @param internalLinkIf
   *          internal link information.
   */
  public void setInternalLinkIf(InternalLinkIf internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  @Override
  public String toString() {
    return "OppositeNodesInterface [nodeId=" + nodeId + ", breakoutBaseIfs=" + breakoutBaseIfs + ", internalLinkIf="
        + internalLinkIf + "]";
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
    if (!breakoutBaseIfs.isEmpty()) {
      for (BreakoutBaseIf baseIf : breakoutBaseIfs) {
        baseIf.check(ope);
      }
    }
    if (internalLinkIf != null) {
      internalLinkIf.check(ope);
    }
  }
}
