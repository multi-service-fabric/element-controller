/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Information for BGP
 */
public class BgpCreateL3cp {

  /** Role Information */
  private String role;

  /** Opposing AS Number */
  private Integer neighborAs;

  /** Opposing Device IPv4 Address */
  private String neighborIpv4Address;

  /** Opposing Device IPv6 Address */
  private String neighborIpv6Address;

  /**
   * Getting role information.
   *
   * @return role information
   */
  public String getRole() {
    return role;
  }

  /**
   * Setting role information.
   *
   * @param role
   *          role information
   */
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * Getting opposing AS number.
   *
   * @return opposing AS number
   */
  public Integer getNeighborAs() {
    return neighborAs;
  }

  /**
   * Setting opposing AS number.
   *
   * @param neighborAs
   *          opposing AS number
   */
  public void setNeighborAs(Integer neighborAs) {
    this.neighborAs = neighborAs;
  }

  /**
   * Getting opposing device IPv4 address.
   *
   * @return opposing device IPv4 address
   */
  public String getNeighborIpv4Address() {
    return neighborIpv4Address;
  }

  /**
   * Setting opposing device IPv4 address.
   *
   * @param neighborIpv4Address
   *          opposing device IPv4 address
   */
  public void setNeighborIpv4Address(String neighborIpv4Address) {
    this.neighborIpv4Address = neighborIpv4Address;
  }

  /**
   * Getting opposing device IPv6 address.
   *
   * @return opposing device IPv6 address
   */
  public String getNeighborIpv6Address() {
    return neighborIpv6Address;
  }

  /**
   * Setting opposing device IPv6 address.
   *
   * @param neighborIpv6Address
   *          opposing device IPv6 address
   */
  public void setNeighborIpv6Address(String neighborIpv6Address) {
    this.neighborIpv6Address = neighborIpv6Address;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "BgpCreateL3cp [role=" + role + ", neighborAs=" + neighborAs + ", neighborIpv4Address=" + neighborIpv4Address
        + ", neighborIpv6Address=" + neighborIpv6Address + "]";
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
    if (role == null) {
      throw new CheckDataException();
    }
    if (!role.equals("master") && !role.equals("slave")) {
      throw new CheckDataException();
    }
    if (neighborAs == null) {
      throw new CheckDataException();
    }
    if (neighborIpv4Address == null && neighborIpv6Address == null) {
      throw new CheckDataException();
    }
  }

}
