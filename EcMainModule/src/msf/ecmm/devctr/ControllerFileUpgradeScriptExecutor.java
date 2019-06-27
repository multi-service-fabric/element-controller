/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctr;

import java.io.IOException;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.log.MsfLogger;

/**
 * Class for executing the script to update the controller file.
 *
 */
public class ControllerFileUpgradeScriptExecutor {

  /** Logger. */
  private static final MsfLogger logger = new MsfLogger();

  /**
   * The method for executing the asynchronous command.
   *
   * @param params
   *          Character string including arguments in the executed command
   * @param errList
   *          The output error list
   * @return  Result 0: normal others: error
   */
  public static int exec(String[] params, List<String> errList) {

    logger.trace(CommonDefinitions.START);

    final int error = -1;

    if ((params == null) || (params.length == 0)) {
      return error;
    }
    for (int i = 0; i < params.length; i++) {
      logger.debug("args[" + params[i] + "]");
    }

    ProcessBuilder pb = new ProcessBuilder(params);

    int retVal = 0;

    try {
      pb.start();
    } catch (IOException ioex) {
      logger.error("IOException", ioex);
      errList.add(ioex.getLocalizedMessage());
      retVal = error;
    }
    return retVal;
  }


}
