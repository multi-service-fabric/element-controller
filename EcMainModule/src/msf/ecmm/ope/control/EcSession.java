/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.control;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.log.MsfLogger;

/**
 * Operation ID Retention Class Definition. Retaining operation ID.
 *
 */
public class EcSession implements AutoCloseable {

  /**
   * Logger
   */
  private final MsfLogger logger = new MsfLogger();

  /** Operation ID  */
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
