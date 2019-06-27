/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.restpojo.parts;

/**
 * Config information for host in EM(EM receives).
 */
public class LatestEmConfigFromEm {

  /** Date and time when EM store Config. */
  private String date;

  /** Server host name  which stores Config. */
  private String serverName;

  /** Current host Config in EM. */
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
   * Getting server host name.
   *
   * @return server host name
   */
  public String getServerName() {
    return serverName;
  }

  /**
   * Setting server host name..
   *
   * @param serverName
   *          server host name
   */
  public void setServerName(String serverName) {
    this.serverName = serverName;
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
    return "LatestEmConfigFromEm [date=" + date + ", serverName=" + serverName + ", config=" + config + "]";
  }

}
