/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Information of IF in which VLAN IF is created.
 */
public class BaseIfCreateVlanIf {

  /** ID of device in which VLAN IF is created. */
  private String nodeId;

  /** VLAN IF Creating IF Type. */
  private String ifType;

  /** ID of IF in which VLAN IF is created. */
  private String ifId;

  /**
   * Getting the ID of device in which CP is created.
   *
   * @return the ID of device in which CP is created
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting the ID of device in which CP is created.
   *
   * @param nodeId
   *          the ID of device in which CP is created
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting CP creating IF type.
   *
   * @return CP creating IF type
   */
  public String getIfType() {
    return ifType;
  }

  /**
   * Setting CP creating IF type.
   *
   * @param type
   *          CP creating IF type
   */
  public void setIfType(String type) {
    this.ifType = type;
  }

  /**
   * Getting ID of IF in which CP is created.
   *
   * @return ID of IF in which CP is created
   */
  public String getIfId() {
    return ifId;
  }

  /**
   * Setting ID of IF in which CP is created.
   *
   * @param ifId
   *          ID of IF in which CP is created
   */
  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "BaseIfCreateVlanIf [nodeId=" + nodeId + ", ifType=" + ifType + ", ifId=" + ifId + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   *
   * @throws CheckDataException
   *           Input Check Error
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
