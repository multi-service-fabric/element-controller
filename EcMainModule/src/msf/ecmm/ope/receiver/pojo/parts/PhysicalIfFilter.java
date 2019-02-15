/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Physical IF Filter Information.
 */
public class PhysicalIfFilter {

  /** Device ID. */
  private String nodeId;

  /** Physical IF ID. */
  private String physicalIfId;

  /** Condition Informaiton. */
  private ArrayList<GetTerms> terms = new ArrayList<>();

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
   * Getting Physical IF ID.
   *
   * @return Physical IF ID
   */
  public String getPhysicalIfId() {
    return physicalIfId;
  }

  /**
   * Setting Physical IF ID.
   *
   * @param physicalIfId
   *          Physical IF ID
   */
  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
  }

  /**
   * Getting the condition.
   *
   * @return Condition
   */
  public ArrayList<GetTerms> getTerms() {
    return terms;
  }

  /**
   * Setting condition.
   *
   * @param terms
   *          Condition
   */
  public void setTerms(ArrayList<GetTerms> terms) {
    this.terms = terms;
  }

  @Override
  public String toString() {
    return "PhysicalIfFilter[nodeId" + nodeId + ", physicalIfId" + physicalIfId + ", terms" + terms + "]";
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
    if (physicalIfId == null) {
      throw new CheckDataException();
    }
    if (terms.size() == 0) {
      throw new CheckDataException();
    }
  }
}
