/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.traffic.pojo;

/**
 * Traffic Information Storage Class Definition. Stores traffic information of an IF.
 *
 */
public class TrafficData {

  /** IF Name */
  private String ifName = "";

  /** Traffic Information (In) */
  private double ifHclnOctets = -1.0;

	/** Traffic Information (Out) */
  private double ifHcOutOctets = -1.0;

  /**
   * Generating new instance.
   */
  public TrafficData() {
    super();
  }

  /**
   * Getting ifName.
   *
   * @return ifName
   */
  public String getIfname() {
    return ifName;
  }

  /**
   * Setting ifName.
   *
   * @param ifName
   *          ifName
   */
  public void setIfname(String ifName) {
    this.ifName = ifName;
  }

  /**
   * Getting traffic information (in).
   *
   * @return traffic information (in)
   */
  public double getIfHclnOctets() {
    return ifHclnOctets;
  }

  /**
   * Setting traffic information (in).
   *
   * @param ifHclnOctets
   *          traffic information (in)
   */
  public void setIfHclnOctets(double ifHclnOctets) {
    this.ifHclnOctets = ifHclnOctets;
  }

  /**
   * Getting traffic information (out).
   *
   * @return traffic information (out)
   */
  public double getIfHcOutOctets() {
    return ifHcOutOctets;
  }

  /**
   * Setting traffic information (out).
   *
   * @param ifHcOutOctets
   *          traffic information (out)
   */
  public void setIfHcOutOctets(double ifHcOutOctets) {
    this.ifHcOutOctets = ifHcOutOctets;
  }

  @Override
  public String toString() {
    return "TrafficData [ifName=" + ifName + ", ifHclnOctets=" + ifHclnOctets + ", ifHcOutOctets=" + ifHcOutOctets
        + "]";
  }

}
