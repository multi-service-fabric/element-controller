/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import msf.ecmm.common.CommandExecutor;
import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.devctrl.pojo.SnmpIfTraffic;

/**
 * Operation related to VLAN unit trafic collection script.
 */
public class ScriptController {
  /**
   * logger.
   */
  private final MsfLogger logger = new MsfLogger();

  private static final String SHELL_ERROR_SSH = ".*SSH connection error.*";

  /**
   * Traffic information acquisition.
   *
   * @param eq
   *          Model information
   * @param node
   *          device information
   * @return Traffic information
   * @throws DevctrlException
   *           Error occurred at the time of script execution
   */
  public ArrayList<SnmpIfTraffic> getVlanTraffic(Equipments eq, Nodes node) throws DevctrlException {

    logger.trace(CommonDefinitions.START);
    List<String> stdList = new ArrayList<String>();
    List<String> errList = new ArrayList<String>();
    ArrayList<SnmpIfTraffic> list = null;
    String host = node.getUsername() + "@" + node.getManagement_if_address();
    String[] params = { "bash", eq.getCli_exec_path(), host, node.getPassword() };

    int ret = CommandExecutor.exec(params, stdList, errList);
    if (ret != 0) {
      if (stdList.toString().matches(SHELL_ERROR_SSH)) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_405097, host));
      }
      File file = new File(eq.getCli_exec_path());
      if (!file.exists()) {
        logger.debug("Script file not found.");
      }
      logger.error(LogFormatter.out.format(LogFormatter.MSG_505096, eq.getCli_exec_path()));
      throw new DevctrlException("Script execution failure.");
    } else {
      Gson gson = new Gson();

      Type listType = new TypeToken<List<SnmpIfTraffic>>() {
      }.getType();

      String json = "";
      if (0 != stdList.size()) {
        for (String param : stdList) {
          json = json + param;
        }
      }

      try {
        logger.trace("Traffic json from cumulus" + json);
        list = gson.fromJson(json, listType);
      } catch (JsonSyntaxException jse) {
        logger.debug("fromJson error.", jse);
        throw new DevctrlException("fromJson error.");
      }

      logger.trace("CLI Vlan Traffic  regular collection is complete.");
      logger.debug("traffic : " + list);
    }
    return list;
  }

}
