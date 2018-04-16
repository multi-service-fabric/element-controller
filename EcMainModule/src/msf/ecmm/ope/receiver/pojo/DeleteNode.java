/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.NodeDeleteNode;
import msf.ecmm.ope.receiver.pojo.parts.UpdateDeleteNode;

/**
 * Device Removal Change Class.
 */
public class DeleteNode extends AbstractRestMessage {

  /** Device Removal Deleting Device Information. */
  private NodeDeleteNode deleteNodes;

  /** Device Removal Changing Device Information. */
  private UpdateDeleteNode updateNode;

  /**
   * Getting device removal deleting device information.
   *
   * @return device removal deleting device information
   */
  public NodeDeleteNode getDeleteNodes() {
    return deleteNodes;
  }

  /**
   * Setting device removal deleting device information.
   *
   * @param deleteNodes
   *          device removal deleting device information
   */
  public void setDeleteNodes(NodeDeleteNode deleteNodes) {
    this.deleteNodes = deleteNodes;
  }

  /**
   * Getting device removal changing device information.
   *
   * @return device removal changing device information
   */
  public UpdateDeleteNode getUpdateNode() {
    return updateNode;
  }

  /**
   * Setting device removal changing device information.
   *
   * @param updateNode
   *          device removal changing device information
   */
  public void setUpdateNode(UpdateDeleteNode updateNode) {
    this.updateNode = updateNode;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "DeleteNode [deleteNodes=" + deleteNodes + ", updateNode=" + updateNode + "]";
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
    if (deleteNodes == null) {
      throw new CheckDataException();
    } else {
      deleteNodes.check(ope);
    }
    if (updateNode != null) {
      updateNode.check(ope);
    }
  }
}
