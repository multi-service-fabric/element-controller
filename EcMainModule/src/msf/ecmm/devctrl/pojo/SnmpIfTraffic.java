/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl.pojo;

/**
 * SNMP Traffic Informtion.
 */
public class SnmpIfTraffic {

  /** IF Name. */
  private String ifName;

  /** Inbound Traffic. */
  private long inOctets;

  /** Outbound Traffic. */
  private long outOctets;

  /**
   * @param ifName
   *          IF name
   * @param inOctets
   *          inbound traffic
   * @param outOctets
   *          outbound traffic
   */
  public SnmpIfTraffic(String ifName, long inOctets, long outOctets) {
    super();
    this.ifName = ifName;
    this.inOctets = inOctets;
    this.outOctets = outOctets;
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
   * Getting inbound traffic.
   *
   * @return inbound traffic
   */
  public long getInOctets() {
    return inOctets;
  }

  /**
   * Setting inbound traffic.
   *
   * @param inOctets
   *          inbound traffic
   */
  public void setInOctets(long inOctets) {
    this.inOctets = inOctets;
  }

  /**
   * Getting outbound traffic.
   *
   * @return outbound traffic
   */
  public long getOutOctets() {
    return outOctets;
  }

  /**
   * Setting outbound traffic.
   *
   * @param outOctets
   *          outbound traffic
   */
  public void setOutOctets(long outOctets) {
    this.outOctets = outOctets;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "SnmpIfTraffic [ifName=" + ifName + ", inOctets=" + inOctets + ", outOctets=" + outOctets + "]";
  }

}
