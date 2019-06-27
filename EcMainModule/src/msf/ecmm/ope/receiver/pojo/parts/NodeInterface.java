/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * IF Information (Physical IF/LagIF/Internal Link).
 */
public class NodeInterface {

  /** breakoutIF information. */
  private List<BreakoutBaseIf> breakoutBaseIfs = new ArrayList<>();

  /** Internal Link Information List. */
  private List<InternalLinkInfo> internalLinkIfs = new ArrayList<>();

  /** Unused Physical IF List. */
  @SerializedName("unused_physical_ifs")
  private List<UnusePhysicalIf> unusePhysicalIfs = new ArrayList<>();

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
  public List<InternalLinkInfo> getInternalLinkIfs() {
    return internalLinkIfs;
  }

  /**
   * Setting internal link information list.
   *
   * @param internalLinkIfs
   *          internal link information list.
   */
  public void setInternalLinkIfs(List<InternalLinkInfo> internalLinkIfs) {
    this.internalLinkIfs = internalLinkIfs;
  }

  /**
   * Getting unused physical IF list.
   *
   * @return unused physical IF list.
   */
  public List<UnusePhysicalIf> getUnusePhysicalIfs() {
    return unusePhysicalIfs;
  }

  /**
   * Setting unused physical IF list.
   *
   * @param unusePhysicalIfs
   *          unused physical IF list.
   */
  public void setUnusePhysicalIfs(List<UnusePhysicalIf> unusePhysicalIfs) {
    this.unusePhysicalIfs = unusePhysicalIfs;
  }

  @Override
  public String toString() {
    return "NodeInterface [breakoutBaseIfs=" + breakoutBaseIfs + ", internalLinkIfs=" + internalLinkIfs
        + ", unusePhysicalIfs=" + unusePhysicalIfs + "]";
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
    if (!breakoutBaseIfs.isEmpty()) {
      for (BreakoutBaseIf baseIf : breakoutBaseIfs) {
        baseIf.check(ope);
      }
    }
    if (!internalLinkIfs.isEmpty()) {
      for (InternalLinkInfo linkInfo : internalLinkIfs) {
        linkInfo.check(ope);
      }
    }
    if (!unusePhysicalIfs.isEmpty()) {
      for (UnusePhysicalIf physicalIf : unusePhysicalIfs) {
        physicalIf.check(ope);
      }
    }
  }
}
