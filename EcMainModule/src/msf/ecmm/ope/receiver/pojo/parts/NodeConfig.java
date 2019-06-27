/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Config information and difference information.
 */
public class NodeConfig {

  /** Node ID. */
  private String nodeId;

  /** Node Config inormation stored in EM. */
  private LatestEmConfig latestEmConfig;

  /** Current config inormation received from node. */
  private NeConfig neConfig;

  /** Difference information. */
  private Diff diff;

  /**
   * Getting node ID.
   *
   * @return node ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting node ID
   *
   * @param nodeId
   *          node ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting Config information.
   *
   * @return Config information
   */
  public LatestEmConfig getLatestEmConfig() {
    return latestEmConfig;
  }

  /**
   * Setting Config information.
   *
   * @param latestEmConfig
   *          Config information
   */
  public void setLatestEmConfig(LatestEmConfig latestEmConfig) {
    this.latestEmConfig = latestEmConfig;
  }

  /**
   * Getting current config information.
   *
   * @return current config information
   */
  public NeConfig getNeConfig() {
    return neConfig;
  }

  /**
   * Setting curent config information.
   *
   * @param neConfig
   *          current config information
   */
  public void setNeConfig(NeConfig neConfig) {
    this.neConfig = neConfig;
  }

  /**
   * Getting  difference information.
   *
   * @return difference information
   */
  public Diff getDiff() {
    return diff;
  }

  /**
   * Setting difference information.
   *
   * @param diff
   *          difference information.
   */
  public void setDiff(Diff diff) {
    this.diff = diff;
  }

  @Override
  public String toString() {
    return "NodeConfig [nodeId=" + nodeId + ", latestEmConfig=" + latestEmConfig + ", neConfig=" + neConfig + ", diff="
        + diff + "]";
  }

}
