/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.config;

/**
 *  The individual configuration class for the extended function(to update controller file)
 */
public class ControllerFileUpgradeConfiguration extends ExpandConfigurationBase {
  /** The path key for the script executing the switch-over */
  public static final String SWITCH_SCRIPT_PATH = "switch_script_file_path";

  /** The instance of my class */
  private static ControllerFileUpgradeConfiguration me = new ControllerFileUpgradeConfiguration();

  /**
   * The Individual configuration class constructor for Extended function(to update controller file)
   */
  private ControllerFileUpgradeConfiguration() {

    configFileName = "../conf/ControllerFileUpdate.conf";
    ExpandConfigInfo config;
    config = new ExpandConfigInfo();
    config.setConfigName(SWITCH_SCRIPT_PATH);
    config.setType(String.class);
    configCondition.put(SWITCH_SCRIPT_PATH, config);
  }

  /**
   * The instance for Individual configuration class for Extended function(to update controller file) is returned.
   *
   * @return The instance for configuraton management function class.
   */
  public static ControllerFileUpgradeConfiguration getInstance() {
    return me;
  }
}
