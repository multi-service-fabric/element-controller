/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl.pojo.parts;

/**
 * Config information on server in EM(FC sends).
 */
public class LatestEmConfigToFc {

  /** Date and time when EM stores Config. */
  private String date;

  /** Server host name storing Config. */
  private String serverName;

  /** Current host config in EM. */
  private String config;

  /**
   * Getting Date and time.
   *
   * @return Date and time
   */
  public String getDate() {
    return date;
  }

  /**
   * Setting Date and time.
   *
   * @param date
   *           Date and time
   */
  public void setDate(String date) {
    this.date = date;
  }

  /**
   * Getting server host name.
   *
   * @return server host name
   */
  public String getServerName() {
    return serverName;
  }

  /**
   * Setting server host name.
   *
   * @param serverName
   *          server host name
   */
  public void setServerName(String serverName) {
    this.serverName = serverName;
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
   *          config
   */
  public void setConfig(String config) {
    this.config = config;
  }

  @Override
  public String toString() {
    return "LatestEmConfigToFc [date=" + date + ", serverName=" + serverName + ", config=" + config + "]";
  }

}
