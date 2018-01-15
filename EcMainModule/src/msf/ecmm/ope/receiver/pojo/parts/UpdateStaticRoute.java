/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Information for Static Route Change.
 */
public class UpdateStaticRoute {

  /** Operation Type. */
  private String operationType;

  /** IP Address Type. */
  private String addressType;

  /** Destination Address. */
  private String address;

  /** Destination Prefix. */
  private Integer prefix;

  /** NEXT HOP. */
  private String nextHop;

  /**
   * Getting operation type.
   *
   * @return operationType
   */
  public String getOperationType() {
    return operationType;
  }

  /**
   * Setting operation type.
   *
   * @param operationType
   *          set operationType
   */
  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  /**
   * Getting IP address type.
   *
   * @return IP address type
   */
  public String getAddressType() {
    return addressType;
  }

  /**
   * Setting IP address type.
   *
   * @param addressType
   *          IP address type
   */
  public void setAddressType(String addressType) {
    this.addressType = addressType;
  }

  /**
   * Getting destination address..
   *
   * @return destination address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting destination address.
   *
   * @param address
   *          destination address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting destination prefix.
   *
   * @return destination prefix
   */
  public Integer getPrefix() {
    return prefix;
  }

  /**
   * Setting destination prefix.
   *
   * @param prefix
   *          destination prefix
   */
  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  /**
   * Getting NEXT HOP.
   *
   * @return NEXT HOP
   */
  public String getNextHop() {
    return nextHop;
  }

  /**
   * Setting NEXT HOP.
   *
   * @param nextHop
   *          NEXT HOP
   */
  public void setNextHop(String nextHop) {
    this.nextHop = nextHop;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "UpdateStaticRoute [operationType=" + operationType + ", addressType=" + addressType + ", address=" + address
        + ", prefix=" + prefix + ", nextHop=" + nextHop + "]";
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
    if (operationType == null) {
      throw new CheckDataException();
    }
    if (!operationType.equals("add") && !operationType.equals("delete")) {
      throw new CheckDataException();
    }
    if (addressType == null) {
      throw new CheckDataException();
    }
    if (!addressType.equals(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV4_STRING)
        && !addressType.equals(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV6_STRING)) {
      throw new CheckDataException();
    }
    if (address == null) {
      throw new CheckDataException();
    }
    if (prefix == null) {
      throw new CheckDataException();
    }
    if (nextHop == null) {
      throw new CheckDataException();
    }
  }

}
