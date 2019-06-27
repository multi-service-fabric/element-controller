/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Information for Ztp.
 */
public class Ztp {

  /** Replaced device initial injection configuration file layout directory separator. */
  public static final String INITIAL_DIR_DELIMITER = ":";

  /** dhcp.conf template file layout directory list. */
  private String dhcpTemplate;

  /** Device Initail Injection Configuration Template File Layout Directory List. */
  private String configTemplate;

  /** Device Initial Injection Configuration File. */
  private String initialConfig;

  /** Start-up Completion Determination Message. */
  private String bootCompleteMsg;

  /** Start-up Failure Determination Message List. */
  private ArrayList<String> bootErrorMsgs;

  /**
   * Getting dhcp.conf template file path.
   *
   * @return dhcp.conf template file path
   */
  public String getDhcpTemplate() {
    return this.dhcpTemplate;
  }

  /**
   * Setting dhcp.conf template file path.
   *
   * @param dhcpTemplate
   *          dhcp.conf template file path
   */
  public void setDhcpTemplate(String dhcpTemplate) {
    this.dhcpTemplate = dhcpTemplate;
  }

  /**
   * Getting device initial injection configuration template file layout directory list.
   *
   * @return device initial injection configuration template file directory list
   */
  public String getConfigTemplate() {
    return configTemplate;
  }

  /**
   * Setting device initial injection configuration template file layout directry list.
   *
   * @param configTemplate
   *          Setting device initial injection configuration template file layout directory list
   */
  public void setConfigTemplate(String configTemplate) {
    this.configTemplate = configTemplate;
  }

  /**
   * Getting device initial injection configuration template file directory list.
   *
   * @return device initial injection configuration template file directory list
   */
  public String getInitialConfig() {
    return initialConfig;
  }

  /**
   * Setting device initial injection configuration template file directory list.
   *
   * @param initialConfig
   *          device initial injection configuration template file directory list
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
   * Setting start-up completion determination message.
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
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "Ztp [dhcpTemplate=" + dhcpTemplate + ", configTemplate=" + configTemplate + ", initialConfig="
        + initialConfig + ", bootCompleteMsg=" + bootCompleteMsg + ", bootErrorMsgs=" + bootErrorMsgs + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param operationType
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType operationType) throws CheckDataException {
    if (dhcpTemplate == null) {
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
    if (configTemplate.split(INITIAL_DIR_DELIMITER).length != initialConfig.split(INITIAL_DIR_DELIMITER).length) {
      throw new CheckDataException();
    }
  }
}
