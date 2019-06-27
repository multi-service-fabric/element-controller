/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.traffic.pojo;

import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;

/**
 * Node Key Set Class Definition. Stores information specifying a node.
 *
 */
public class NodeKeySet {

  /** Model Information */
  private Equipments nodeType = null;

  /** Device Information */
  private Nodes nodeId = null;

  /**
   * Generating new instance.
   */
  public NodeKeySet() {
    super();
  }

  /**
   * Getting model information.
   *
   * @return model information
   */
  public Equipments getEquipmentsType() {
    return nodeType;
  }

  /**
   * Setting model information.
   *
   * @param nodeType
   *          model information
   */
  public void setEquipmentsType(Equipments nodeType) {
    this.nodeType = nodeType;
  }

  /**
   * Getting device information.
   *
   * @return device information
   */
  public Nodes getEquipmentsData() {
    return nodeId;
  }

  /**
   * Setting device information
   *
   * @param nodeId
   *          device information
   */
  public void setEquipmentsData(Nodes nodeId) {
    this.nodeId = nodeId;
  }

  @Override
  public String toString() {
    return "NodeKeySet [nodeType=" + nodeType + ", nodeId=" + nodeId + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
    result = prime * result + ((nodeType == null) ? 0 : nodeType.hashCode());
    return result;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    NodeKeySet other = (NodeKeySet) obj;
    if (nodeId == null) {
      if (other.nodeId != null)
        return false;
    } else if (!nodeId.equals(other.nodeId))
      return false;
    if (nodeType == null) {
      if (other.nodeType != null)
        return false;
    } else if (!nodeType.equals(other.nodeType))
      return false;
    return true;
  }
}
