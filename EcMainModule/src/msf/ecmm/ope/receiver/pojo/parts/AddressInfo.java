/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Address Information Class.
 */
public class AddressInfo {

  /** Address. */
  private String address;

  /** Address Prefix. */
  private Integer prefix;

  /**
   * Getting address.
   *
   * @return address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting address.
   *
   * @param address
   *          address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Getting address prefix.
   *
   * @return address prefix
   */
  public Integer getPrefix() {
    return prefix;
  }

  /**
   * Setting address prefix.
   *
   * @param prefix
   *          address prefix
   */
  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  @Override
  public String toString() {
    return "AddressInfo [address=" + address + ", prefix=" + prefix + "]";
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
    if (address == null) {
      throw new CheckDataException();
    }
    if (prefix == null) {
      throw new CheckDataException();
    }
  }
}
