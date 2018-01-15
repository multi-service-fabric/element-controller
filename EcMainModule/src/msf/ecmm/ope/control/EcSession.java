/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.control;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import msf.ecmm.common.CommonDefinitions;

/**
 * Operation ID Retention Class Definition. Retaining operation ID.
 *
 */
public class EcSession implements AutoCloseable {

  /**
   * Logger
   */
  private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** Operation ID */
  private int operationId;

  /**
   * Constructor
   *
   * @param oid
   *          Operation ID
   */
  public EcSession(int oid) {
    operationId = oid;
  }

  /**
   * Termination of Operation<br>
   * Executing termination process of operation.
   */
  public void close() {

    logger.trace(CommonDefinitions.START);

    if (operationId > 0) {
      OperationControlManager.getInstance().terminateOperation(this);
    } else {
      throw new InternalError();
    }

    logger.trace(CommonDefinitions.END);

  }

  /**
   * Getting operation ID.
   *
   * @return operation ID
   */
  protected int getOperationId() {
    return operationId;
  }

  @Override
  public String toString() {
    return "EcSession [operationId=" + operationId + "]";
  }
}
