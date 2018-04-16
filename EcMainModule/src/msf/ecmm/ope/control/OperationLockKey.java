/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.control;

/**
 * Operation Lock Key Class Definition. Defining the key for locking operation.
 */
public class OperationLockKey extends AbstractQueueEntryKey {

  /** Operation Type. */
  private int operationType;

  /** Node ID. */
  private String nodeId;

  /**
   * Constructor.
   *
   * @param opeType
   *          operation type
   * @param nid
   *          node ID
   */
  public OperationLockKey(int opeType, String nid) {
    operationType = opeType;
    nodeId = nid;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
    result = prime * result + operationType;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    OperationLockKey other = (OperationLockKey) obj;
    if (nodeId == null) {
      if (other.nodeId != null) {
        return false;
      }
    } else if (!nodeId.equals(other.nodeId)) {
      return false;
    }
    if (operationType != other.operationType) {
      return false;
    }
    return true;
  }

  /**
   * Getting operation type.
   *
   * @return operation type
   */
  public int getOperationType() {
    return operationType;
  }

  /**
   * Getting node ID.
   *
   * @return node ID
   */
  public String getNode_id() {
    return nodeId;
  }

  @Override
  public String toString() {
    return "OperationLockKey [operationType=" + operationType + ", nodeId=" + nodeId + "]";
  }

}
