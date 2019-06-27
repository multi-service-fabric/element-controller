/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Physical IF Information List.
 */
public class PhysicalIf {

  /** Physical IF ID. */
  private String physicalIfId;

  /** IF Name. */
  private String ifName;

  /** breakoutIF Information List. */
  private ArrayList<BreakoutIfId> breakoutIf = new ArrayList<BreakoutIfId>();

/** IF Status. */
  private String ifState;

  /** Line Speed. */
  private String linkSpeed;

  /** Accommodated Device IF Address. */
  private String ipv4Address;

  /** Accommodated Device IF Prefix. */
  private String ipv4Prefix;

  /** QoS capability. */
  private QosCapabilities qos;

 /**
   * Getting physical IF ID.
   *
   * @return physical IF ID
   */
  public String getPhysicalIfId() {
    return physicalIfId;
  }

  /**
   * Setting IF ID.
   *
   * @param physicalIfId
   *          physical IF ID
   */
  public void setPhysicalIfId(String physicalIfId) {
    this.physicalIfId = physicalIfId;
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
   * Getting breakoutIF information list.
   *
   * @return breakoutIF information list
   */
  public ArrayList<BreakoutIfId> getBreakoutIf() {
    return breakoutIf;
  }

  /**
   * Setting breakoutIF information list.
   *
   * @param breakoutIf
   *          breakoutIF information list
   */
  public void setBreakoutIf(ArrayList<BreakoutIfId> breakoutIf) {
    this.breakoutIf = breakoutIf;
  }

  /**
   * Getting IF status.
   *
   * @return IF status
   */
  public String getIfState() {
    return ifState;
  }

  /**
   * Setting IF status.
   *
   * @param ifState
   *          IF status
   */
  public void setIfState(String ifState) {
    this.ifState = ifState;
  }

  /**
   * Getting line speed.
   *
   * @return line speed
   */
  public String getLinkSpeed() {
    return linkSpeed;
  }

  /**
   * Setting line speed.
   *
   * @param linkSpeed
   *          line speed
   */
  public void setLinkSpeed(String linkSpeed) {
    this.linkSpeed = linkSpeed;
  }

  /**
   * Getting accommodated device IF address.
   *
   * @return accommodated device IF address
   */
  public String getIpv4Address() {
    return ipv4Address;
  }

  /**
   * Setting accommodated device IF address.
   *
   * @param ipv4Address
   *          accommodated device IF address
   */
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  /**
   * Getting accommodated device IF prefix.
   *
   * @return accommodated device IF prefix
   */
  public String getIpv4Prefix() {
    return ipv4Prefix;
  }

  /**
   * Setting accommodated device IF prefix.
   *
   * @param ipv4Prefix
   *          accommodated IF prefix
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
   *          setting qos
   */
  public void setQos(QosCapabilities qos) {
    this.qos = qos;
  }

  /*
   * Stringizing Instance.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "PhysicalIf [physicalIfId=" + physicalIfId + ", ifName=" + ifName + ", breakoutIf=" + breakoutIf
        + ", ifState=" + ifState + ", linkSpeed=" + linkSpeed + ", ipv4Address=" + ipv4Address + ", ipv4Prefix="
        + ipv4Prefix + ", qos=" + qos + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (physicalIfId == null) {
      throw new CheckDataException();
    }
  }
}
