/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.config;

/**
 * Extended operation details information class.
 */
public class ExpandOperationDetailInfo {

  /** Extension function operation type. */
  private String operationTypeName;
  /** Extension function operation ID. */
  private int operationTypeId;
  /** Extension function execution class. */
  private String operationExecuteClassName;
  /** Operation priority type. */
  private String operationPriority;
  /** Lock required/not required.*/
  private boolean isLock;

  /**
   * Getting Extension function operation type.
   *
   * @return Extension function operation type.
   */
  public String getOperationTypeName() {
    return operationTypeName;
  }

  /**
   * Setting Extension function operation type.
   *
   * @param operationTypeName
   *          Extension function operation type.
   */
  public void setOperationTypeName(String operationTypeName) {
    this.operationTypeName = operationTypeName;
  }

  /**
   * Getting Extension function operation ID.
   *
   * @return Extension function operation ID.
   */
  public int getOperationTypeId() {
    return operationTypeId;
  }

  /**
   * Setting Extension function operation ID.
   *
   * @param operationTypeId
   *          Extension function operation ID.
   */
  public void setOperationTypeId(int operationTypeId) {
    this.operationTypeId = operationTypeId;
  }

  /**
   * Getting Extension function execution class.
   *
   * @return Extension function exection class.
   */
  public String getOperationExecuteClassName() {
    return operationExecuteClassName;
  }

  /**
   * Setting Extension function execution class.
   *
   * @param operationExecuteClassName
   *          Execution function execution class.
   */
  public void setOperationExecuteClassName(String operationExecuteClassName) {
    this.operationExecuteClassName = operationExecuteClassName;
  }

  /**
   * Getting operation priority type.
   *
   * @return Operation priority type.
   */
  public String getOperationPriority() {
    return operationPriority;
  }

  /**
   * Setting operaiton priority type.
   *
   * @param operationPriority
   *          Operation priority type
   */
  public void setOperationPriority(String operationPriority) {
    this.operationPriority = operationPriority;
  }

  /**
   * Getting lock required/ not required.
   *
   * @return Lock required/ not required.
   */
  public boolean isLock() {
    return isLock;
  }

  /**
   * Setting lock required/ not required.
   *
   * @param isLock
   *          lock required/ not required.
   */
  public void setLock(boolean isLock) {
    this.isLock = isLock;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ExpandOperationDetailInfo [operationTypeName=" + operationTypeName + ", operationTypeId=" + operationTypeId
        + ", operationExecuteClassName=" + operationExecuteClassName + ", operationPriority=" + operationPriority
        + ", isLock=" + isLock + "]";
  }
}
