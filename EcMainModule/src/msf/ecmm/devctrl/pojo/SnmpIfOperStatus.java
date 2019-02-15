/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl.pojo;

/**
 * ifOperStatus Information of SNMP.
 */
public class SnmpIfOperStatus {

  /** ifOperStatus : 1 - Up */
  public static final int IF_OPER_STATUS_UP = 1;

  /** ifOperStatus : 2 - Down */
  public static final int IF_OPER_STATUS_DOWN = 2;

  /** ifOperStatus : 3 - Testing */
  public static final int IF_OPER_STATUS_TESTING = 3;

  /** ifOperStatus : 7 - LowerLayerDown */
  public static final int IF_OPER_STATUS_LOWER_LAYER_DOWN = 7;

  /** IF Name */
  private String ifName;

  /** ifOperStatus */
  private int ifOperStatus;

  /**
   * Constructor.
   *
   * @param ifName
   *          IF name
   * @param ifOperStatus
   *          ifOperStatus
   */
  public SnmpIfOperStatus(String ifName, int ifOperStatus) {
    super();
    this.ifName = ifName;
    this.ifOperStatus = ifOperStatus;
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
   * Getting ifOperStatus.
   *
   * @return ifOperStatus
   */
  public int getIfOperStatus() {
    return ifOperStatus;
  }

  /**
   * Setting ifOperStatus.
   *
   * @param ifOperStatus
   *          ifOperStatus
   */
  public void setIfOperStatus(int ifOperStatus) {
    this.ifOperStatus = ifOperStatus;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "SnmpIfOperStatus [ifName=" + ifName + ", ifOperStatus=" + ifOperStatus + "]";
  }

}
