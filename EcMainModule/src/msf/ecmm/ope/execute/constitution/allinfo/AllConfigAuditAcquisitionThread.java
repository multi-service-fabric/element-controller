/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.allinfo;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.emctrl.RestClientToEm;
import msf.ecmm.emctrl.restpojo.AbstractRequest;
import msf.ecmm.emctrl.restpojo.ConfigAuditFromEm;
import msf.ecmm.fcctrl.RestClientException;

/**
 * Executing Thread  for Config-Audit list acquisition.
 */
public class AllConfigAuditAcquisitionThread extends Thread {

  /** Logger. */
  protected static final MsfLogger logger = new MsfLogger();

  /** Host name of server. */
  private String hostname;

  /** Parent thread instance. */
  private AllConfigAuditUtility instance;

  /**
   * Executing Thread.
   */
  public void run() {

    logger.trace(CommonDefinitions.START);
    logger.debug("thread start. hostname=" + hostname);

    AbstractRequest abstractRequest = new AbstractRequest();
    ConfigAuditFromEm result = new ConfigAuditFromEm();

    HashMap<String, String> keyMap = new HashMap<>();
    keyMap.put(CommonDefinitions.KEY_HOSTNAME, hostname);

    try {
      result = (ConfigAuditFromEm) new RestClientToEm().request(RestClientToEm.CONFIG_AUDIT, keyMap, abstractRequest,
          ConfigAuditFromEm.class);
      instance.setResult(result);

    } catch (RestClientException rce) {
      instance.setResult(rce.getCode());
    }

    logger.debug("ConfigAuditResult=" + result);
    logger.trace(CommonDefinitions.END);

  }

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
   * Getting parent thread instance.
   *
   * @return parent thread instance
   */
  public AllConfigAuditUtility getInstance() {
    return instance;
  }

  /**
   * Setting parent thread instance.
   *
   * @param instance
   *           parent thread instance
   */
  public void setInstance(AllConfigAuditUtility instance) {
    this.instance = instance;
  }

}
