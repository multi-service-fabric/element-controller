/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * breakoutIF Information.
 */
public class BreakoutIf {

  /** breakoutIF ID. */
  private String breakoutIfId;

  /** Physical IF ID. */
  private String basePhysicalIfId;

  /** Speed after partition. */
  private String linkSpeed;

  /** IF name. */
  private String ifName;

  /** IF status. */
  private String ifState;

  /** IPv4 address. */
  private String ipv4Address;

  /** IPv4 prefix. */
  private String ipv4Prefix;

  /** QoS capability. */
  private QosCapabilities qos;

  /**
   * Getting breakoutIF ID.
   *
   * @return breakoutIF ID
   */
  public String getBreakoutIfId() {
    return breakoutIfId;
  }

  /**
   * Setting breakoutIF ID.
   *
   * @param breakoutIfId
   *          breakoutIF ID
   */
  public void setBreakoutIfId(String breakoutIfId) {
    this.breakoutIfId = breakoutIfId;
  }

  /**
   * Getting physical IF ID.
   *
   * @return physical IF ID
   */
  public String getPhysicalIfId() {
    return basePhysicalIfId;
  }

  /**
   * Setting physical IF ID.
   *
   * @param basePhysicalIfId
   *          Physical IF ID
   */
  public void setPhysicalIfId(String basePhysicalIfId) {
    this.basePhysicalIfId = basePhysicalIfId;
  }

  /**
   * Getting speed after partition.
   *
   * @return speed after partition
   */
  public String getLinkSpeed() {
    return linkSpeed;
  }

  /**
   * Setting speed after partition.
   *
   * @param linkSpeed
   *          Speed after partition
   */
  public void setLinkSpeed(String linkSpeed) {
    this.linkSpeed = linkSpeed;
  }

  /**
   * Getting IF name.
   *
   * @return IF name
   */
  public String getIfName() {
    return ifName;
  }

  /**
   * Setting IF name.
   *
   * @param ifName
   *          IF name
   */
  public void setIfName(String ifName) {
    this.ifName = ifName;
  }

  /**
   * Getting IF state.
   *
   * @return IF state
   */
  public String getIfState() {
    return ifState;
  }

  /**
   * Setting IF state.
   *
   * @param ifState
   *          IF state
   */
  public void setIfState(String ifState) {
    this.ifState = ifState;
  }

  /**
   * Getting IP4 address.
   *
   * @return IPv4 address
   */
  public String getIpv4Address() {
    return ipv4Address;
  }

  /**
   * Setting IPv4 address.
   *
   * @param ipv4Address
   *          IPv4 address
   */
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  /**
   * Getting IPv4 prefix.
   *
   * @return IPv4 prefix
   */
  public String getIpv4Prefix() {
    return ipv4Prefix;
  }

  /**
   * Setting IPv4 prefix.
   *
   * @param ipv4Prefix
   *          IPv4 prefix
   */
  public void setIpv4Prefix(String ipv4Prefix) {
    this.ipv4Prefix = ipv4Prefix;
  }

  /**
   * Getting QoS capability.
   *
   * @return qos
   */
  public QosCapabilities getQos() {
    return qos;
  }

  /**
   * Setting QoS capability.
   *
   * @param qos
   *          Setting qos
   */
  public void setQos(QosCapabilities qos) {
    this.qos = qos;
  }

  /*
   * Stringizing instance.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "BreakoutIf [breakoutIfId=" + breakoutIfId + ", basePhysicalIfId=" + basePhysicalIfId + ", linkSpeed="
        + linkSpeed + ", ifName=" + ifName + ", ifState=" + ifState + ", ipv4Address=" + ipv4Address + ", ipv4Prefix="
        + ipv4Prefix + ", qos=" + qos + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          Oeration type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (basePhysicalIfId == null) {
      throw new CheckDataException();
    }
    if (breakoutIfId == null) {
      throw new CheckDataException();
    }
  }
}
