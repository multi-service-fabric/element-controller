/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.LagIfChangeLagIf;

/**
 * Updating LagIF.
 */
public class UpdateLagInterface extends AbstractRestMessage {
  /** Action type. */
  private String action;

  /** Information on node with LagIF to be updated. */
  private LagIfChangeLagIf lagIf;

  /**
   * Getting action type.
   *
   * @return action type
   */
  public String getAction() {
    return action;
  }

  /**
   * Setting action type.
   *
   * @param action
   *          action type
   */
  public void setAction(String action) {
    this.action = action;
  }

  /**
   * Getting information on node with LagIF to be updated.
   *
   * @return information on node with LagIF to be updated
   */
  public LagIfChangeLagIf getLagIfs() {
    return lagIf;
  }

  /**
   * Setting information on node with LagIF to be updated.
   *
   * @param lagIf
   *          information on node with LagIF to be updated
   */
  public void setLagIfs(LagIfChangeLagIf lagIf) {
    this.lagIf = lagIf;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "UpdateLagIf [action=" + action + ", lagIf=" + lagIf + "]";
  }

  /**
   * Input paramter check.
   *
   * @param ope
   *           operation type
   * @throws CheckDataException
   *           inpt paramter error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (action == null) {
      throw new CheckDataException();
    } else if (!(action.equals(CommonDefinitions.OPERATION_TYPE_ADD))
        && !(action.equals(CommonDefinitions.OPERATION_TYPE_DELETE))) {
      throw new CheckDataException();
    }

    if (lagIf == null) {
      throw new CheckDataException();
    } else {
      lagIf.check(ope);
    }
  }
}
