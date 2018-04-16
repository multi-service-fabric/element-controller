/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Information for VRRP.
 */
public class Vrrp {

  /** VRRP Group ID. */
  private Integer groupId = null;

  /** Role of VRRP to be configured. */
  private String role = null;

  /** Virtual IF Address (IPv4). */
  private String virtualIpv4Address = null;

  /** Virtual IF Address (IPv6). */
  private String virtualIpv6Address = null;

  /**
   * Getting VRRP group ID.
   *
   * @return VRRP group ID
   */
  public Integer getGroupId() {
    return groupId;
  }

  /**
   * Setting VRRP group ID.
   *
   * @param groupId
   *          VRRP group ID
   */
  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  /**
   * Getting role of VRRP to be configured.
   *
   * @return role of VRRP to be configured
   */
  public String getRole() {
    return role;
  }

  /**
   * Setting role of VRRP to be configured.
   *
   * @param role
   *          role of VRRP to be configured
   */
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * Getting virtual IF address (IPv4).
   *
   * @return virtual IF address (IPv4)
   */
  public String getVirtualIpv4Address() {
    return virtualIpv4Address;
  }

  /**
   * Setting virtual IF address (IPv4).
   *
   * @param virtualIpv4Address
   *          virtual IF address (IPv4)
   */
  public void setVirtualIpv4Address(String virtualIpv4Address) {
    this.virtualIpv4Address = virtualIpv4Address;
  }

  /**
   * Getting virtual IF address (IPv6).
   *
   * @return virtual IF address (IPv6)
   */
  public String getVirtualIpv6Address() {
    return virtualIpv6Address;
  }

  /**
   * Setting virtual IF address (IPv6).
   *
   * @param virtualIpv6Address
   *          virtual IF address (IPv6)
   */
  public void setVirtualIpv6Address(String virtualIpv6Address) {
    this.virtualIpv6Address = virtualIpv6Address;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "Vrrp [groupId=" + groupId + ", role=" + role + ", virtualIpv4Address=" + virtualIpv4Address
        + ", virtualIpv6Address=" + virtualIpv6Address + "]";
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

    if (groupId == null) {
      throw new CheckDataException();
    }
    if (role == null) {
      throw new CheckDataException();
    }
    if (!role.equals("master") && !role.equals("slave")) {
      throw new CheckDataException();
    }
    if (virtualIpv4Address == null && virtualIpv6Address == null) {
      throw new CheckDataException();
    }
  }
}
