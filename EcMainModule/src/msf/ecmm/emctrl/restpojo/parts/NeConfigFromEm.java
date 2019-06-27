/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.restpojo.parts;

/**
 * Current Config information received from EM (EM received).
 */
public class NeConfigFromEm {

  /** Date and time when Config is got from host. */
  private String date;

  /** Config received from EM */
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
    return "NeConfigFromEm [date=" + date + ", config=" + config + "]";
  }

}
