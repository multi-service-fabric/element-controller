/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.config;

import msf.ecmm.common.CommonDefinitions;


/**
 * Class for individual configuration with the extended function (to upgrade OS in the device)
 */
public class NodeOsUpgradeConfiguration extends ExpandConfigurationBase {

  /** The timer used when receiveing the notification that OS upgrade  has been completed. */
  public static final String NODE_OS_UPGRADE_TIMER = "node_os_upgrade_timer";

  /** My class instance. */
  private static NodeOsUpgradeConfiguration me = new NodeOsUpgradeConfiguration();

  /**
   * Constructor of class  for the extended individual configuration (to send ping between nodes and to monitor OSF neighbor).
   */	
  private NodeOsUpgradeConfiguration() {
    logger.trace(CommonDefinitions.START);

    configFileName = "../conf/NodeOsUpgrade.conf";

    ExpandConfigInfo config;

    config = new ExpandConfigInfo();
    config.setConfigName(NODE_OS_UPGRADE_TIMER);
    config.setType(Integer.class);
    configCondition.put(NODE_OS_UPGRADE_TIMER, config);

    logger.debug("configCondition=" + configCondition);
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Instace of Class for individual configuration with the extended function (to upgrade OS in the device) is returned.
   *
   * @return The nstace of Class with the configuration management
   */
  public static NodeOsUpgradeConfiguration getInstance() {
    return me;
  }

}
