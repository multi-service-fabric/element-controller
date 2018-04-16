/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Static Route Information List
 */
public class StaticRoute {

  /** IP Address Type */
  private String addressType;

  /** Destination Address */
  private String address;

  /** Destinatio Prefix */
  private Integer prefix;

  /** NEXT HOP */
  private String nextHop;

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
   * Getting destination address.
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
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "StaticRoute [addressType=" + addressType + ", address=" + address + ", prefix=" + prefix + ", nextHop="
        + nextHop + "]";
  }

  /**
   * Input Parameter Check
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (addressType == null) {
      throw new CheckDataException();
    }
    if (!addressType.equals("ipv4") && !addressType.equals("ipv6")) {
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
