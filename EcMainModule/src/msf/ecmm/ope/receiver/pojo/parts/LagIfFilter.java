/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * LAGIF Filter Information.
 */
public class LagIfFilter {
  /** Device ID. */
  private String nodeId;

  /** LAGIF ID. */
  private String lagIfId;

  /** LAG Member Information. */
  private LagMember lagMember;

  /** Condition. */
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
   * Getting LAGIF ID.
   *
   * @return LAGIF ID
   */
  public String getLagIfId() {
    return lagIfId;
  }

  /**
   * Setting LAGIF ID.
   *
   * @param lagIfId
   *          LAGIF ID
   */
  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  /**
   * Getting LAG Member Information.
   *
   * @return  LAG Member Information
   */
  public LagMember getLagMember() {
    return lagMember;
  }

  /**
   * Setting LAG Member Information.
   *
   * @param lagMember
   *          LAG Member Information
   */
  public void setLagmember(LagMember lagMember) {
    this.lagMember = lagMember;
  }

  /**
   * Getting the condition.
   *
   * @return condition
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
    return "LagIfFilter[nodeId" + nodeId + ", lagIfId" + lagIfId + ", lagMember" + lagMember + ", terms" + terms + "]";
  }

  /**
   * Checking input parameter.
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
    if (lagIfId == null) {
      throw new CheckDataException();
    }
    if (lagMember == null) {
      throw new CheckDataException();
    }
    if (terms.size() == 0) {
      throw new CheckDataException();
    }

  }

}
