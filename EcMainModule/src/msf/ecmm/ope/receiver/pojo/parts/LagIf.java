/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * LagIF Information List.
 *
 */
public class LagIf {

  /** LagIF ID. */
  private String lagIfId;

  /** IF Name. */
  private String ifName;

  /** LAG Member Information. */
  private LagMember lagMember;

  /** IF Status. */
  private String ifState;

  /** LinkSpeed. */
  private String linkSpeed;

  /** IPv4 Address. */
  private String ipv4Address;

  /** IPv4 Address Prefix. */
  private String ipv4Prefix;

  /** QoS capability. */
  private QosCapabilities qos;

    /**
   * Getting LagIF ID.
   *
   * @return LagIF ID
   */
  public String getLagIfId() {
    return lagIfId;
  }

  /**
   * Setting LagIF ID.
   *
   * @param lagIfId
   *          LagIF ID
   */
  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
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
   * Getting LAG member information.
   *
   * @return lagMember
   */
  public LagMember getLagMember() {
    return lagMember;
  }

  /**
   * Setting LAG member information.
   *
   * @param lagMember
   *          set lagMember
   */
  public void setLagMember(LagMember lagMember) {
    this.lagMember = lagMember;
  }

  /**
   * Getting IF status.
   *
   * @return ifState
   */
  public String getIfState() {
    return ifState;
  }

  /**
   * Setting IF status.
   *
   * @param ifState
   *          set ifState
   */
  public void setIfState(String ifState) {
    this.ifState = ifState;
  }

  /**
   * Getting LinkSpeed.
   *
   * @return linkSpeed
   */
  public String getLinkSpeed() {
    return linkSpeed;
  }

  /**
   * Setting LinkSpeed.
   *
   * @param linkSpeed
   *          set linkSpeed
   */
  public void setLinkSpeed(String linkSpeed) {
    this.linkSpeed = linkSpeed;
  }

  /**
   * Getting IPv4 address.
   *
   * @return ipv4Address
   */
  public String getIpv4Address() {
    return ipv4Address;
  }

  /**
   * Setting IPv4 address.
   *
   * @param ipv4Address
   *          set ipv4Address
   */
  public void setIpv4Address(String ipv4Address) {
    this.ipv4Address = ipv4Address;
  }

  /**
   * Getting IPv4 address prefix.
   *
   * @return ipv4Prefix
   */
  public String getIpv4Prefix() {
    return ipv4Prefix;
  }

  /**
   * Setting IPv4 address prefix.
   *
   * @param ipv4Prefix
   *          set ipv4Prefix
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
   *  Stringizing Instance.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "LagIf [lagIfId=" + lagIfId + ", ifName=" + ifName + ", lagMember=" + lagMember + ", ifState=" + ifState
        + ", linkSpeed=" + linkSpeed + ", ipv4Address=" + ipv4Address + ", ipv4Prefix=" + ipv4Prefix + ", qos=" + qos
        + "]";
  }

}
