/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.control;

import java.util.Date;

/**
 * Operation Queue Reception Definition. A class for holding the operation ID and stored time in the operation queue.
 *
 */
public class OperationQueueEntry {

  /** Operation ID */
  private EcSession operationId;

  /** Stored Time */
  private Date timestamp;

  /**
   * Constructor
   *
   * @param entry
   *          operation ID
   */
  public OperationQueueEntry(EcSession entry) {
    operationId = entry;
    timestamp = new Date();
  }

  /**
   * Getting operation ID.
   *
   * @return operation ID
   */
  protected EcSession getOperationId() {
    return operationId;
  }

  /**
   * Getting the time it is stored in the queue.
   *
   * @return stored time
   */
  protected Date getTimestamp() {
    return timestamp;
  }

  @Override
  public String toString() {
    return "OperationQueueEntry [operationId=" + operationId + ", timestamp=" + timestamp + "]";
  }

}
