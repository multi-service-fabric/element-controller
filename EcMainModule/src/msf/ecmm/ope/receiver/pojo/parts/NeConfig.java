/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Current Config information received from node.
 */
public class NeConfig {

  /** Date and time when config is got from node. */
  private String date;

  /** Config got by node  access. */
  private String config;


  /**
   * Getting date and time.
   *
   * @return date and time
   */
  public String getDate() {
    return date;
  }

  /**
   * Setting date and time.
   *
   * @param date
   *          date and time
   */
  public void setDate(String date) {
    this.date = date;
  }

  /**
   * Getting config.
   *
   * @return config
   */
  public String getConfig() {
    return config;
  }

  /**
   * Setting config.
   *
   * @param config
   *          Config
   */
  public void setConfig(String config) {
    this.config = config;
  }

  @Override
  public String toString() {
    return "NeConfig [date=" + date + ", config=" + config + "]";
  }

}
