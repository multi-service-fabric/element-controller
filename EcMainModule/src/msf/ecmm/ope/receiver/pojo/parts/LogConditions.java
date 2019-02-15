/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Log Acquisition Condition Information
 */
public class LogConditions {

  /**
   * Start date of log acquisition period
   */
  private String start_date;

  /**
   * End date of log acquisiotion period
   */
  private String end_date;

  /**
	* Upper limit of the number of acquiring logs (lines)
   */
  private Integer limit_number;

  /**
   * Getting start date of log acquisiotion period.
   * @return start date of log acquisiotion period
   */
  public String getStart_date() {
    return start_date;
  }

  /**
   * Setting start date of log acquisiotion period
   * @param start_date start date of log acquisiotion period
   */
  public void setStart_date(String start_date) {
    this.start_date = start_date;
  }

  /**
   * Getting end date of log acquisition period.
   * @return end date of log acquisition period
   */
  public String getEnd_date() {
    return end_date;
  }

  /**
   * Setting end date of log acquisition period
   * @param end_date end date of log acquisition period
   */
  public void setEnd_date(String end_date) {
    this.end_date = end_date;
  }

  /**
	* Getting upper limit of the number of logs (lines).
   * @return upper limit of the number of logs (lines)
   */
  public Integer getLimit_number() {
    return limit_number;
  }

  /**
   * Setting upper limit of the number of logs (lines).
   * @param limit_number upper limit of the number of logs (lines)
   */
  public void setLimit_number(Integer limit_number) {
    this.limit_number = limit_number;
  }

}
