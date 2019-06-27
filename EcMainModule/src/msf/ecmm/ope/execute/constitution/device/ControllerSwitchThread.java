/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.config.ControllerFileUpgradeConfiguration;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.devctr.ControllerFileUpgradeScriptExecutor;

/**
  * Thread class for  switching-over in the controller
  */
public class ControllerSwitchThread extends Thread {

  /** The eror code when the script is executed. */
  static final int SCRIPT_EXECUTE_ERROR = -1;

  /** Logger. */
  private static final MsfLogger logger = new MsfLogger();

  /**
   * The thread is executed.
   */
  public void run() {
    logger.trace(CommonDefinitions.START);
    String scriptPath = ControllerFileUpgradeConfiguration.getInstance()
        .get(String.class, ControllerFileUpgradeConfiguration.SWITCH_SCRIPT_PATH);
    String grpEc = EcConfiguration.getInstance().get(String.class, EcConfiguration.EC_RESOURCE_GROUP_NAME);
    String prmEc = EcConfiguration.getInstance().get(String.class, EcConfiguration.EC_RESOURCE_STATUS_TARGET_NAME);
    String[] switchover = { scriptPath,  grpEc, prmEc };
    List<String> errList = new ArrayList<String>();
    int ret = ControllerFileUpgradeScriptExecutor.exec(switchover, errList);
    if (SCRIPT_EXECUTE_ERROR == ret) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_403115, "Script execute error." + errList.toString()));
    }
    logger.trace(CommonDefinitions.END);
  }

}
