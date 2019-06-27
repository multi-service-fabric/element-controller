/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * IF information.
 */
public class BaseIfInfo {

  /** Node ID. */
  private String nodeId;

  /** IF type(physical IF/LagIF/breakout IF). */
  private String ifType;

  /** IF ID. */
  private String ifId;

  /**
   * Getting node ID.
   *
   * @return node ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting node ID.
   *
   * @param nodeId
   *          node ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting IF type.
   *
   * @return IF type
   */
  public String getIfType() {
    return ifType;
  }

  /**
   * Setting IF type.
   *
   * @param type
   *          IF type
   */
  public void setIfType(String type) {
    this.ifType = type;
  }

  /**
   * Getting IF ID.
   *
   * @return IF type
   */
  public String getIfId() {
    return ifId;
  }

  /**
   * Setting IF ID.
   *
   * @param type
   *          IF ID
   */
  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  /**
   * Stringizing Instance
   *
   * @return istance string
   */
  @Override
  public String toString() {
    return "BaseIfInfo [nodeId=" + nodeId + ", ifType=" + ifType + ", ifId=" + ifId + "]";
  }

  /**
   * Input paramter check.
   *
   * @param ope
   *           operation type
   * @throws CheckDataException
   *           inpt paramter error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (nodeId == null) {
      throw new CheckDataException();
    }
    if (ifType == null) {
      throw new CheckDataException();
    } else if (!ifType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF) && !ifType.equals(CommonDefinitions.IF_TYPE_LAG_IF)
        && !ifType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      throw new CheckDataException();
    }
    if (ifId == null) {
      throw new CheckDataException();
    }
  }

}
