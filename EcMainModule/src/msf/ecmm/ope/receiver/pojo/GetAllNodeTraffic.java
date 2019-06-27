/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.SwitchTraffic;

/**
 * Traffic Information List Acquisition
 */
public class GetAllNodeTraffic extends AbstractResponseMessage {

  /** Acquisition Success/Fail */
  private Boolean isSuccess;

  /** MIB Acquisition Data and Time */
  private String time;

  /** TM Presumption Execution Cycle (Traffic Information Collection Cycle) */
  private Integer interval;

  /** Traffic Information for each device */
  private ArrayList<SwitchTraffic> switchTraffics = new ArrayList<SwitchTraffic>();

  /**
   * Getting acquisition success/fail.
   * 
   * @return acquisition success/fail
   */
  public Boolean getIsSuccess() {
    return isSuccess;
  }

  /**
   * Setting acquisition success/fail.
   * 
   * @param isSuccess
   *          acquisition success/fail
   */
  public void setIsSuccess(Boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  /**
   * Getting MIB acquisition date and time.
   * 
   * @return MIB acquisition date and time
   */
  public String getTime() {
    return time;
  }

  /**
   * Setting MIB acquisition date and time.
   * 
   * @param time
   *          MIB acquisition date and time
   */
  public void setTime(String time) {
    this.time = time;
  }

  /**
   * Getting TM presumption execution cycle (traffic information collection cycle).
   * 
   * @return TM presumption execution cycle (traffic information collection cycle)
   */
  public Integer getInterval() {
    return interval;
  }

  /**
   * Setting TM presumption execution cycle (traffic information collection cycle).
   * 
   * @param interval
   *          TM presumption execution cycle (traffic information collection cycle)
   */
  public void setInterval(Integer interval) {
    this.interval = interval;
  }

  /**
   * Getting traffic information for each device.
   * 
   * @return traffic information for each device
   */
  public ArrayList<SwitchTraffic> getSwitchTraffics() {
    return switchTraffics;
  }

  /**
   * Setting traffic information for each device.
   * 
   * @param switchTraffic
   *          traffic information for each device
   */
  public void setSwitchTraffics(ArrayList<SwitchTraffic> switchTraffics) {
    this.switchTraffics = switchTraffics;
  }

  /**
   * Stringizing Instance
   * 
   * @return instance string
   */
  @Override
  public String toString() {
    return "GetNodeTraffic [clusterId=" + ", isSuccess=" + isSuccess + ", time=" + time + ", interval=" + interval
        + ", switchTraffic=" + switchTraffics + "]";
  }
}
