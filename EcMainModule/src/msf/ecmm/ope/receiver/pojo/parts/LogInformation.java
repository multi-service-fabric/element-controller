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
   * Whether the number of acquired logs exceed the upper limit or not
   */
  private boolean overLimitNumber;

  /**
   * Host name of the server where the log is submitted
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
   * @param dataNumber the number of logs
   */
  public void setDataNumber(Integer dataNumber) {
    this.dataNumber = dataNumber;
  }

  /**
   * Getting whether the number of acquired logs exceed the upper limit or not.
   * @return exceeded/not exceeded
   */
  public boolean isOverLimitNumber() {
    return overLimitNumber;
  }

  /**
   * Setting whether the number of acquired logs exceed the upper limit or not.
   * @param overLimitNumber exceeded/not exceeded
   */
  public void setOverLimitNumber(boolean overLimitNumber) {
    this.overLimitNumber = overLimitNumber;
  }

  /**
   * Getting host name of the server where the log is submitted.
   * @return server host name
   */
  public String getServerName() {
    return serverName;
  }

  /**
   * Setting host name of the server where the log is submitted.
   * @param serverName server host name
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
   * @param logData log information list
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
