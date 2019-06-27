/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

/**
 *  Difference information(FC sends).
 */
public class NodeConfigToFc {

  /** NodeID. */
  private String nodeId;

  /** Config information server in EM. */
  private LatestEmConfigToFc latestEmConfig;

  /** Current information received from server. */
  private NeConfigToFc neConfig;

  /** Difference information. */
  private DiffToFc diff;

  /**
   * Getting Node ID.
   *
   * @return Node ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting Node ID.
   *
   * @param nodeId
   *          Node ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting Config information.
   *
   * @return 
   */
  public LatestEmConfigToFc getLatestEmConfig() {
    return latestEmConfig;
  }

  /**
   * Setting Config information.
   *
   * @param latestEmConfig
   *          Config information
   */
  public void setLatestEmConfig(LatestEmConfigToFc latestEmConfig) {
    this.latestEmConfig = latestEmConfig;
  }

  /**
   * Getting current Config information.
   *
   * @return current Config information
   */
  public NeConfigToFc getNeConfig() {
    return neConfig;
  }

  /**
   * Setting current Config information.
   *
   * @param neConfig
   *          current Config information
   */
  public void setNeConfig(NeConfigToFc neConfig) {
    this.neConfig = neConfig;
  }

  /**
   * Getting  difference information.
   *
   * @return difference information
   */
  public DiffToFc getDiff() {
    return diff;
  }

  /**
   * Setting difference information.
   *
   * @param diff
   *          difference information.
   */
  public void setDiff(DiffToFc diff) {
    this.diff = diff;
  }

  @Override
  public String toString() {
    return "NodeConfigToFc [nodeId=" + nodeId + ", latestEmConfig=" + latestEmConfig + ", neConfig=" + neConfig
        + ", diff=" + diff + "]";
  }
}
