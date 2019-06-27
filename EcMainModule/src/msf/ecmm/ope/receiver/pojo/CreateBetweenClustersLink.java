/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.ClusterLink;
import msf.ecmm.ope.receiver.pojo.parts.TargetIf;

/**
 * Inter-Cluster Link Generation.
 */
public class CreateBetweenClustersLink extends AbstractRestMessage {

  /** Information of IF to be generated. */
  private TargetIf targetIf = null;

  /** Inter-Cluster Link Information. */
  private ClusterLink clusterLink = null;

  /**
   * Getting information of IF to be generated.
   *
   * @return information of IF to be generated
   */
  public TargetIf getTargetIf() {
    return targetIf;
  }

  /**
   * Setting information of IF to be generated.
   *
   * @param targetIf
   *          information of IF to be generated
   */
  public void setTargetIf(TargetIf targetIf) {
    this.targetIf = targetIf;
  }

  /**
   * Getting inter-cluster link information.
   *
   * @return inter-cluster link information
   */
  public ClusterLink getClusterLink() {
    return clusterLink;
  }

  /**
   * Setting inter-cluster link information.
   *
   * @param clusterLink
   *          inter-cluster link information
   */
  public void setClusterLink(ClusterLink clusterLink) {
    this.clusterLink = clusterLink;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "CreateBetweenClustersLink [targetIf=" + targetIf + ", clusterLink=" + clusterLink + "]";
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
    if (clusterLink == null) {
      throw new CheckDataException();
    } else {
      clusterLink.check(ope);
    }
  }

}
