/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.restpojo.parts;

import java.util.ArrayList;

/**
 * Log Information
 */
public class LogInformation {

  /**
   * Number of Logs
   */
  private Integer dataNumber;

  /**
   * Checking if the number of acquired logs exceed the upper limit or not?
   */
  private boolean overLimitNumber;

  /**
   * Host name of the server in which log was released
   */
  private String serverName;

  /**
   * Log Information List
   */
  private ArrayList<LogData> logData = new ArrayList<LogData>();

  /**
   * Getting the number of logs.
   * @return the number of logs
   */
  public Integer getData_number() {
    return dataNumber;
  }

  /**
   * Setting the number of logs.
   * @param data_number the number of logs
   */
  public void setData_number(Integer data_number) {
    this.dataNumber = data_number;
  }

  /**
   * Getting the result of checking if the number of acquired logs exceed the upper limit.
   * @return yes/no
   */
  public boolean isOver_limit_number() {
    return overLimitNumber;
  }

  /**
   * Setting the result of checking if the number of acquired logs exceed the upper limit.
   * @param over_limit_number yes/no
   */
  public void setOver_limit_number(boolean over_limit_number) {
    this.overLimitNumber = over_limit_number;
  }

  /**
   * Getting the host name in which log was released.
   * @return the host name in which log was released
   */
  public String getServer_name() {
    return serverName;
  }

  /**
   * Setting the host name in which log was released.
   * @param server_name the host name in which log was released
   */
  public void setServer_name(String server_name) {
    this.serverName = server_name;
  }

  /**
   * Getting log information list.
   * @return log information list
   */
  public ArrayList<LogData> getLog_data() {
    return logData;
  }

  /**
   * Setting log information list.
   * @param log_data log information list
   */
  public void setLog_data(ArrayList<LogData> log_data) {
    this.logData = log_data;
  }

  @Override
  public String toString() {
    return "LogInformation [data_number=" + dataNumber + ", over_limit_number="
      + overLimitNumber + ", server_name=" + serverName + ", log_data=" + logData + "]";
  }

}
