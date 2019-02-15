/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/** Loopback IF with slice (VRF). */
public class LoopBackInterface {
  /** Loopback IF address. */
  private String address = null;

  /** Loopback IF address prefix. */
  private Integer prefix = null;


  /**
   *  Getting loopback IF address.
   *
   * @return address
   */
  public String getAddress() {
    return address;
  }

  /**
   *  Setting loopback IF address.
   *
   * @param address
   *          Setting address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   *  Getting loopback IF address prefix.
   *
   * @return prefix
   */
  public Integer getPrefix() {
    return prefix;
  }

  /**
   *  Setting loopback IF address prefix.
   *
   * @param prefix
   *          Setting prefix
   */
  public void setPrefix(Integer prefix) {
    this.prefix = prefix;
  }

  /**
   * Stringizing instance.
   *
   * @return Instance string
   */
  @Override
  public String toString() {
    return "LoopBackInterface [address=" + address + ", prefix=" + prefix + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           Input check error
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
