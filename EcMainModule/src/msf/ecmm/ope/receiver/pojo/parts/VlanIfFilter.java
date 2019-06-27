/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * VLANIF Filter Information.
 */
public class VlanIfFilter {
  /** Equipment ID. */
  private String nodeId;

  /** VLANIF ID. */
  private String vlanIfId;

  /** Base IF Information. */
  private BaseIf baseIf;

  /** Condition. */
  private ArrayList<GetTerms> terms = new ArrayList<>();

  /**
   * Getting the Device ID.
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
   * Getting VLANIF ID.
   *
   * @return VLANIF ID
   */
  public String getVlanIfId() {
    return vlanIfId;
  }

  /**
   * Setting VLANIF ID.
   *
   * @param vlanIfId
   *          VLANIF ID
   */
  public void setVlanIfId(String vlanIfId) {
    this.vlanIfId = vlanIfId;
  }

  /**
   * Getting the Base IF Information.
   *
   * @return  Base IF Information
   */
  public BaseIf getBaseIf() {
    return baseIf;
  }

  /**
   * Setting the Base IF Information.
   *
   * @param baseIf
   *          Base IF Information
   */
  public void setBaseIf(BaseIf baseIf) {
    this.baseIf = baseIf;
  }

  /**
   * Getting conditon.
   *
   * @return  Condition
   */
  public ArrayList<GetTerms> getTerms() {
    return terms;
  }

  /**
   * Setting condition.
   *
   * @param terms
   *          condition
   */
  public void setTerms(ArrayList<GetTerms> terms) {
    this.terms = terms;
  }

  @Override
  public String toString() {
    return "VlanIfdFilter[nodeId=" + nodeId + ", vlanIfId" + vlanIfId + ", baseIf" + baseIf + ", terms" + terms + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          Operation Type
   * @throws CheckDataException
   *           Input Check Error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (nodeId == null) {
      throw new CheckDataException();
    }
    if (vlanIfId == null) {
      throw new CheckDataException();
    }
    if (baseIf == null) {
      throw new CheckDataException();
    }
    if (terms.size() == 0) {
      throw new CheckDataException();
    }
  }
}
