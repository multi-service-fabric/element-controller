/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Optional Information for Change
 */
public class UpdateOption {

  /** Accommodated Device IF Address (IPv4) */
  private String ipv4Address;

  /** Accommodated Device IF Address (IPv6) */
  private String ipv6Address;

  /** Accommodated Device IF Prefix (IPv4) */
  private Integer ipv4Prefix;

  /** Accommodated Device IF Prefix (IPv6) */
  private Integer ipv6Prefix;

  /** Unique Parameter for each Slice ID */
  private Integer vrfId;

  /** Operation Type */
  private String operationType;

  /** Accommodated Device Router ID */
  private String routerId;

  /** Static Route Information List */
  private ArrayList<StaticRoute> staticRoutes = new ArrayList<StaticRoute>();

  /**
   * Getting accommodated device IF adderss (IPv4).
   *
   * @return accommodated device IF address (IPv4)
   */
  public String getIpv4Address() {
    return ipv4Address;
  }

  /**
   * Setting accommodatd device IF address (IPv4).
   *
   * @param ipv4Address
   *          accommodated IF address (IPv4)
   */
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  /**
   * Getting accommodated device IF address (IPv6).
   *
   * @return accommodated device IF address (IPv6)
   */
  public String getIpv6Address() {
    return ipv6Address;
  }

  /**
   * Setting accommodated device IF address (IPv6).
   *
   * @param ipv6Address
   *          accommodated device IF address (IPv6)
   */
  public void setIpv6Address(String ipv6Address) {
    this.ipv6Address = ipv6Address;
  }

  /**
   * Getting accommodated device IF prefix (IPv4).
   *
   * @return accommodated device IF prefix (IPv4)
   */
  public Integer getIpv4Prefix() {
    return ipv4Prefix;
  }

  /**
   * Setting accommodated device IF prefix (IPv4).
   *
   * @param ipv4Prefix
   *          accommodated device IF prefix (IPv4)
   */
  public void setIpv4Prefix(Integer ipv4Prefix) {
    this.ipv4Prefix = ipv4Prefix;
  }

  /**
   * Getting accommodated device IF prefix (IPv6).
   *
   * @return accommodated device IF prefix (IPv6)
   */
  public Integer getIpv6Prefix() {
    return ipv6Prefix;
  }

  /**
   * Setting accommodated device IF prefix (IPv6).
   *
   * @param ipv6Prefix
   *          accommodated device IF prefix (IPv6)
   */
  public void setIpv6Prefix(Integer ipv6Prefix) {
    this.ipv6Prefix = ipv6Prefix;
  }

  /**
   * Getting unique parameter for each slice ID.
   *
   * @return unique parameter for each slice ID
   */
  public Integer getVrfId() {
    return vrfId;
  }

  /**
   * Setting unique parameter for each slice ID.
   *
   * @param vrfId
   *          unique parameter for each slice ID
   */
  public void setVrfId(Integer vrfId) {
    this.vrfId = vrfId;
  }

  /**
   * Getting operation type.
   *
   * @return operation type
   */
  public String getOperationType() {
    return operationType;
  }

  /**
   * Setting operation type.
   *
   * @param operationType
   *          operation type
   */
  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  /**
   * Getting accommodated device router ID.
   *
   * @return accommodated device router ID
   */
  public String getRouterId() {
    return routerId;
  }

  /**
   * Setting accommodated device router ID.
   *
   * @param routerId
   *          accommodated device router ID
   */
  public void setRouterId(String routerId) {
    this.routerId = routerId;
  }

  /**
   * Getting static route information list.
   *
   * @return static route information list
   */
  public ArrayList<StaticRoute> getStaticRoutes() {
    return staticRoutes;
  }

  /**
   * Setting static route information list.
   *
   * @param staticRoutes
   *          static route information list
   */
  public void setStaticRoutes(ArrayList<StaticRoute> staticRoutes) {
    this.staticRoutes = staticRoutes;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "UpdateOption [ipv4Address=" + ipv4Address + ", ipv6Address=" + ipv6Address + ", ipv4Prefix=" + ipv4Prefix
        + ", ipv6Prefix=" + ipv6Prefix + ", vrfId=" + vrfId + ", operationType=" + operationType + ", routerId="
        + routerId + ", staticRoutes=" + staticRoutes + "]";
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
    if (ipv4Address == null && ipv6Address == null) {
      throw new CheckDataException();
    }
    if (vrfId == null) {
      throw new CheckDataException();
    }
    if (operationType == null) {
      throw new CheckDataException();
    }
    if (!operationType.equals("add") && !operationType.equals("delete")) {
      throw new CheckDataException();
    }
    if (routerId == null) {
      throw new CheckDataException();
    }
    if (staticRoutes.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (StaticRoute sr : staticRoutes) {
        sr.check(ope);
      }
    }
  }

}
