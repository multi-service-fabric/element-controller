/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.TargetIf;

/**
 * Inter-Cluster Link Deletion.
 */
public class DeleteBetweenClustersLink extends AbstractRestMessage {

  /** Information of IF to be deleted. */
  private TargetIf targetIf = null;

  /**
   * Getting information of IF to be deleted.
   *
   * @return information of IF to be deleted
   */
  public TargetIf getTargetIf() {
    return targetIf;
  }

  /**
   * Setting information of IF to be deleted.
   *
   * @param targetIf
   *          information of IF to be deleted
   */
  public void setTargetIf(TargetIf targetIf) {
    this.targetIf = targetIf;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "DeleteBetweenClustersLink [targetIf=" + targetIf + "]";
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
    if (targetIf == null) {
      throw new CheckDataException();
    } else {
      targetIf.check(ope);
    }
  }

}
