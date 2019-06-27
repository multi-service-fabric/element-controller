/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.ope.receiver.pojo.parts;

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
  public Integer getDataNumber() {
    return dataNumber;
  }

  /**
   * Setting the number of logs.
   * @param data_number the number of logs
   */
  public void setDataNumber(Integer dataNumber) {
    this.dataNumber = dataNumber;
  }

  /**
   * Getting the result of checking if the number of acquired logs exceed the upper limit.
   * @return yes/no
   */
  public boolean isOverLimitNumber() {
    return overLimitNumber;
  }

  /**
   * Setting the result of checking if the number of acquired logs exceed the upper limit.
   * @param over_limit_number yes/no
   */
  public void setOverLimitNumber(boolean overLimitNumber) {
    this.overLimitNumber = overLimitNumber;
  }

  /**
   * Getting the host name in which log was released.
   * @return the host name in which log was released
   */
  public String getServerName() {
    return serverName;
  }

  /**
   * Setting the host name in which log was released.
   * @param server_name the host name in which log was released
   */
  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  /**
   * Getting log information list.
   * @return log information list
   */
  public ArrayList<LogData> getLogData() {
    return logData;
  }

  /**
   * Setting log information list.
   * @param log_data log information list
   */
  public void setLog_data(ArrayList<LogData> logData) {
    this.logData = logData;
  }

  @Override
  public String toString() {
    return "LogInformation [dataNumber=" + dataNumber + ", overLimitNumber=" + overLimitNumber + ", serverName="
        + serverName + ", logData=" + logData + "]";
  }
}
