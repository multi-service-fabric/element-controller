/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Log Acquisition Condition Information
 */
public class LogConditions {

  /**
   * Start Date of Log Acquisition Period
   */
  private String start_date;

  /**
   * End Date of Log Acquisition Period
   */
  private String end_date;

  /**
   * The Upper Limit Number of Acquiring Logs (Lines)
   */
  private Integer limit_number;

  /**
   * Getting the start date of log acquisition period.
   * @return start date of log acquisition period
   */
  public String getStart_date() {
    return start_date;
  }

  /**
   * Setting the start date of log acquisition period.
   * @param start_date start date of log acquisition period
   */
  public void setStart_date(String start_date) {
    this.start_date = start_date;
  }

  /**
   * Getting the end date of log acquisition period.
   * @return end date of log acquisition period
   */
  public String getEnd_date() {
    return end_date;
  }

  /**
   * Setting the end date of log acquisition period.
   * @param end_date end date of log acquisition period
   */
  public void setEnd_date(String end_date) {
    this.end_date = end_date;
  }

  /**
   * Getting the upper limit number of acquiring logs (lines).
   * @return the upper limit number of acquiring logs (lines)
   */
  public Integer getLimit_number() {
    return limit_number;
  }

  /**
   * Setting the upper limit number of acquiring logs (lines).
   * @param limit_number the upper limit number of acquiring logs (lines)
   */
  public void setLimit_number(Integer limit_number) {
    this.limit_number = limit_number;
  }

}
