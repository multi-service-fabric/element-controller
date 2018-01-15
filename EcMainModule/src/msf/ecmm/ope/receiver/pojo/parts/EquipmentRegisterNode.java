/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Model Information for Device Registration
 */
public class EquipmentRegisterNode {

  /** Device ID */
  private String equipmentTypeId;

  /** dhcp.conf Template File Path */
  private String configTemplate;

  /** Device Initial Injection Configuration File Path */
  private String initialConfig;

  /** Start-up Completion Determination Message */
  private String bootCompleteMsg;

  /** Start-up Failure Determination Message List */
  private ArrayList<String> bootErrorMsgs = new ArrayList<String>();

  /**
   * Getting device ID.
   *
   * @return device ID
   */
  public String getEquipmentTypeId() {
    return equipmentTypeId;
  }

  /**
   * Setting device ID.
   *
   * @param equipmentTypeId
   *          device ID
   */
  public void setEquipmentTypeId(String equipmentTypeId) {
    this.equipmentTypeId = equipmentTypeId;
  }

  /**
   * Getting dhcp.conf template file path.
   *
   * @return dhcp.conf template file path
   */
  public String getConfigTemplate() {
    return configTemplate;
  }

  /**
   * Setting dhcp.conf template file path.
   *
   * @param configTemplate
   *          dhcp.conf template file path
   */
  public void setConfigTemplate(String configTemplate) {
    this.configTemplate = configTemplate;
  }

  /**
   * Getting device initial injection configuration file path.
   *
   * @return device initial injection configuration file path
   */
  public String getInitialConfig() {
    return initialConfig;
  }

  /**
   * Setting device initial injection configuration file path.
   *
   * @param initialConfig
   *          device initial injection configuration file path
   */
  public void setInitialConfig(String initialConfig) {
    this.initialConfig = initialConfig;
  }

  /**
   * Getting start-up completion determination message.
   *
   * @return start-up completion determination message
   */
  public String getBootCompleteMsg() {
    return bootCompleteMsg;
  }

  /**
   * Setting start-up completion determination message
   *
   * @param bootCompleteMsg
   *          start-up completion determination message
   */
  public void setBootCompleteMsg(String bootCompleteMsg) {
    this.bootCompleteMsg = bootCompleteMsg;
  }

  /**
   * Getting start-up failure determination message list.
   *
   * @return start-up failure determination message list
   */
  public ArrayList<String> getBootErrorMsgs() {
    return bootErrorMsgs;
  }

  /**
   * Setting start-up failure determination message list.
   *
   * @param bootErrorMsgs
   *          start-up failure determination message list
   */
  public void setBootErrorMsgs(ArrayList<String> bootErrorMsgs) {
    this.bootErrorMsgs = bootErrorMsgs;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "EquipmentRegisterNode [equipmentTypeId=" + equipmentTypeId + ", configTemplate=" + configTemplate
        + ", initialConfig=" + initialConfig + ", bootCompleteMsg=" + bootCompleteMsg + ", bootErrorMsgs="
        + bootErrorMsgs + "]";
  }

  /**
   * Input Parameter Check
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (equipmentTypeId == null) {
      throw new CheckDataException();
    }
    if (configTemplate == null) {
      throw new CheckDataException();
    }
    if (initialConfig == null) {
      throw new CheckDataException();
    }
    if (bootCompleteMsg == null) {
      throw new CheckDataException();
    }
  }

}
