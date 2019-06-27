/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/** Setting IRB instance information at the time of Update. */
public class IrbUpdateValue {
  /** IP Address. */
  private String ipv4Address = null;

  /** IP Address Prefix. */
  private Integer ipv4Prefix = null;

  /** Virtual gateway address. */
  private String virtualGatewayAddress = null;

  /**
   * Getting IP Address.
   *
   * @return ipv4Address
   */
  public String getIpv4Address() {
    return ipv4Address;
  }

  /**
   * Setting IP Address.
   *
   * @param ipv4Address
   *          Setting ipv4Address
   */
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  /**
   * Getting IP Address Prefix.
   *
   * @return ipv4Prefix
   */
  public Integer getIpv4Prefix() {
    return ipv4Prefix;
  }

  /**
   * Setting IP Address Prefix.
   *
   * @param ipv4Prefix
   *          Setting ipv4Prefix
   */
  public void setIpv4Prefix(Integer ipv4Prefix) {
    this.ipv4Prefix = ipv4Prefix;
  }

  /**
   * Getting virtual gateway address.
   *
   * @return ipv4Prefix
   */
  public String getVirtualGatewayAddress() {
    return virtualGatewayAddress;
  }

  /**
   * Setting Virtual Gateway Address.
   *
   * @param virtualGatewayAddress
   *          Setting ipv4Prefix
   */
  public void setVirtualGatewayAddress(String virtualGatewayAddress) {
    this.virtualGatewayAddress = virtualGatewayAddress;
  }

  /**
   * Stringizing Instance.
   *
   * @return Instance string
   */
  @Override
  public String toString() {
    return "IrbValue [ipv4Address=" + ipv4Address + ", ipv4Prefix=" + ipv4Prefix + ", virtualGatewayAddress="
        + virtualGatewayAddress + "]";
  }

  /**
   * Input parameter check
   *
   * @param ope
   *          Operation type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (ipv4Address == null) {
      throw new CheckDataException();
    }
    if (ipv4Prefix == null) {
      throw new CheckDataException();
    }
    if (virtualGatewayAddress == null) {
      throw new CheckDataException();
    }
  }

}
