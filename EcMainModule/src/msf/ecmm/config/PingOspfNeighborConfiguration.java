/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.config;

import msf.ecmm.common.CommonDefinitions;

/**
 * Class for the extended individual configuration(to send ping between nodes and to monitor OSF neighbor).
 */
public class PingOspfNeighborConfiguration extends ExpandConfigurationBase {

  /** The key of the directory path for the script executing ping. */
  public static final String PING_SCRIPT_DIR_PATH = "ping_script_dir_path";

  /** The key of the retrial number for the ping  between nodes. */	
  public static final String PING_RETRY_NUM = "ping_retry_num";

  /** The key of the timeout for the ping between nodes. */	
  public static final String ALL_PING_TIMEOUT = "all_ping_timeout";

  /** The key of the timeout for the OSPF neighbor. */
  public static final String ALL_OSPF_NEIGHBOR_TIMEOUT = "all_ospf_neighbor_timeout";


  /** The instance of my class. */
  private static PingOspfNeighborConfiguration me = new PingOspfNeighborConfiguration();

  /**
   * Constructor of Class for individual configuration in the  extended function.
   * (to send the ping between nodes and to monitor OSPF neighbor).
   */
  private PingOspfNeighborConfiguration() {
    logger.trace(CommonDefinitions.START);

    configFileName = "../conf/PingOspfNeighbor.conf";

    ExpandConfigInfo config;

    config = new ExpandConfigInfo();
    config.setConfigName(PING_SCRIPT_DIR_PATH);
    config.setType(String.class);
    configCondition.put(PING_SCRIPT_DIR_PATH, config);

    config = new ExpandConfigInfo();
    config.setConfigName(PING_RETRY_NUM);
    config.setType(Integer.class);
    configCondition.put(PING_RETRY_NUM, config);

    config = new ExpandConfigInfo();
    config.setConfigName(ALL_PING_TIMEOUT);
    config.setType(Integer.class);
    configCondition.put(ALL_PING_TIMEOUT, config);

    config = new ExpandConfigInfo();
    config.setConfigName(ALL_OSPF_NEIGHBOR_TIMEOUT);
    config.setType(Integer.class);
    configCondition.put(ALL_OSPF_NEIGHBOR_TIMEOUT, config);

    logger.debug("configCondition=" + configCondition);
    logger.trace(CommonDefinitions.END);
  }

  /**
   * The instance is returned of the class for the extended individual configuration.
   * (to send ping between nodes and to monitor OSPF neighbor).
   *
   * @return The instance of the class managing the configuration
   */
  public static PingOspfNeighborConfiguration getInstance() {
    return me;
  }

}
