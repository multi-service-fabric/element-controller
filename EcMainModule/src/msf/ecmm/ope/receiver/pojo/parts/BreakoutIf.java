/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * breakoutIF Information.
 */
public class BreakoutIf {

  /** breakoutIF ID. */
  private String breakoutIfId;

  /** Physical IF ID. */
  private String basePhysicalIfId;

  /** Speed After Division. */
  private String linkSpeed;

  /** IF Name. */
  private String ifName;

  /** IF Status. */
  private String ifState;

  /** IPv4 Address. */
  private String ipv4Address;

  /** IPv4 Prefix. */
  private String ipv4Prefix;

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
   *          physical IF ID
   */
  public void setPhysicalIfId(String basePhysicalIfId) {
    this.basePhysicalIfId = basePhysicalIfId;
  }

  /**
   * Getting speed after division.
   *
   * @return speed after division
   */
  public String getLinkSpeed() {
    return linkSpeed;
  }

  /**
   * Setting speed after division.
   *
   * @param linkSpeed
   *          speed after division
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

  /* (Non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "BreakoutIf [breakoutIfId=" + breakoutIfId + ", basePhysicalIfId=" + basePhysicalIfId + ", linkSpeed="
        + linkSpeed + ", ifName=" + ifName + ", ifState=" + ifState + ", ipv4Address=" + ipv4Address + ", ipv4Prefix="
        + ipv4Prefix + "]";
  }
}
