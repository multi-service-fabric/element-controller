/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

/**
 * Current Config information received from server(for sending FC).
 */
public class NeConfigToFc {

  /** Date and time when Config information received from server. */
  private String date;

  /** Config received from server. */
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
   * Getting Config.
   *
   * @return Config
   */
  public String getConfig() {
    return config;
  }

  /**
   * Setting Config.
   *
   * @param config
   *          Config
   */
  public void setConfig(String config) {
    this.config = config;
  }

  @Override
  public String toString() {
    return "NeConfigToFc [date=" + date + ", config=" + config + "]";
  }

}
