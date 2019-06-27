/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.restpojo;

import msf.ecmm.emctrl.restpojo.parts.DiffFromEm;
import msf.ecmm.emctrl.restpojo.parts.LatestEmConfigFromEm;
import msf.ecmm.emctrl.restpojo.parts.NeConfigFromEm;

/**
 * Getting Config-Audit.
 */
public class ConfigAuditFromEm extends AbstractResponse {

  /** Host name. */
  private String hostname;

  /** Config information on server in EM. */
  private LatestEmConfigFromEm latestEmConfig;

  /** Current config information received from server. */
  private NeConfigFromEm neConfig;

  /** Difference information. */
  private DiffFromEm diff;

  /**
   * Getting host name.
   *
   * @return  host name
   */
  public String getHostname() {
    return hostname;
  }

  /**
   * Setting host name.
   *
   * @param hostname
   *          host name
   */
  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  /**
   * Getting Config information.
   *
   * @return Config information
   */
  public LatestEmConfigFromEm getLatestEmConfig() {
    return latestEmConfig;
  }

  /**
   * Setting Config information..
   *
   * @param latestEmConfig
   *          Config information
   */
  public void setLatestEmConfig(LatestEmConfigFromEm latestEmConfig) {
    this.latestEmConfig = latestEmConfig;
  }

  /**
   * Getting current Config information.
   *
   * @return current Config information
   */
  public NeConfigFromEm getNeConfig() {
    return neConfig;
  }

  /**
   * GSetting current Config information.
   *
   * @param neConfig
   *          current Config information
   */
  public void setNeConfig(NeConfigFromEm neConfig) {
    this.neConfig = neConfig;
  }

  /**
   * Getting difference information.
   *
   * @return difference information
   */
  public DiffFromEm getDiff() {
    return diff;
  }

  /**
   * Setting difference information.
   *
   * @param diff
   *          difference information
   */
  public void setDiff(DiffFromEm diff) {
    this.diff = diff;
  }

  @Override
  public String toString() {
    return "ConfigAuditFromEm [hostname=" + hostname + ", latestEmConfig=" + latestEmConfig + ", neConfig=" + neConfig
        + ", diff=" + diff + "]";
  }

}
