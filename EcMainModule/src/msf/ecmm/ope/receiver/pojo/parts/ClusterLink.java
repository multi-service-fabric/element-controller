/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Inter-Cluster Link Configuration.
 *
 */
public class ClusterLink {

  /** IGP cost. */
  private Integer cost = null;

  /** IF address. */
  private String address = null;

  /** IF address prefix. */
  private Integer prefix = null;

  /** Port state. */
  private String condition = null;

  /**
   * Getting IGP cost.
   *
   * @return IGP cost
   */
  public int getCost() {
    return cost;
  }

  /**
   * Setting IGP cost.
   *
   * @param cost
   *          IGP cost
   */
  public void setCost(Integer cost) {
    this.cost = cost;
  }

  /**
   * Getting IF address.
   *
   * @return IF address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting IF address.
   *
   * @param address
   *          IF address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting IF address prefix.
   *
   * @return LagIF ID
   */
  public int getPrefix() {
    return prefix;
  }

  /**
   * Setting IF address prefix.
   *
   * @param prefix
   *          IF address prefix
   */
  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  /**
   * Getting port state.
   *
   * @return port state
   */
  public String getCondition() {
    return condition;
  }

  /**
   * Setting port state.
   *
   * @param condition
   *          port state
   */
  public void setCondition(String condition) {
    this.condition = condition;
  }

  /**
   * Stringizing instance.
   *
   * @return Stringizing instance
   */
  @Override
  public String toString() {
    return "TargetIf [cost=" + cost + ", address=" + address + ", prefix=" + prefix + ", condition=" + condition + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          Operation type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (cost == null) {
      throw new CheckDataException();
    }

    if (address == null) {
      throw new CheckDataException();
    }

    if (prefix == null) {
      throw new CheckDataException();
    }

    if (condition == null) {
      throw new CheckDataException();
    } else if (!(condition.equals(CommonDefinitions.PORT_CONDITION_ENABLE))
        && !(condition.equals(CommonDefinitions.PORT_CONDITION_DISABLE))) {
      throw new CheckDataException();
    }

  }
}
