/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/** Multihoming Configuration Informaiton. */
public class MultiHomingValue {
  /** Setting AnycastIP Address. */
  private String anycastAddress = null;

  /** Clag Bridge IF IP Address Configuraiton. */
  private String ifAddress = null;

  /** Clag Bridge IF Prefix configuraiton. */
  private Integer ifPrefix = null;

  /** Backup IP Address. */
  private String backupAddress = null;

  /** Peer IP Address. */
  private String peerAddress = null;

  /**
   * Getting AnycastIP Address Configuraition.
   *
   * @return anycastAddress
   */
  public String getAnycastAddress() {
    return anycastAddress;
  }

  /**
   * Setting AnycastIP Address Configuraiton.
   *
   * @param anycastAddress
   *          Setting anycastAddress
   */
  public void setAnycastAddress(String anycastAddress) {
    this.anycastAddress = anycastAddress;
  }

  /**
   * Getting Clag Bridge IF IP Address configuraiton.
   *
   * @return ifAddress
   */
  public String getIfAddress() {
    return ifAddress;
  }

  /**
   * Setting Clag Bridge IF IP Address configuraiton.
   *
   * @param ifAddress
   *          Setting ifAddress
   */
  public void setIfAddress(String ifAddress) {
    this.ifAddress = ifAddress;
  }

  /**
   * Getting Clag Bridge IF Prefix configuration.
   *
   * @return ifPrefix
   */
  public Integer getIfPrefix() {
    return ifPrefix;
  }

  /**
   * Setting Clag Bridge IF Prefix configuration.
   *
   * @param ifPrefix
   *          Setting ifPrefix
   */
  public void setIfPrefix(Integer ifPrefix) {
    this.ifPrefix = ifPrefix;
  }

  /**
   * Getting Backup IP Address.
   *
   * @return backupAddress
   */
  public String getBackupAddress() {
    return backupAddress;
  }

  /**
   * Setting Backup IP Address.
   *
   * @param backupAddress
   *          Setting backupAddress
   */
  public void setBackupAddress(String backupAddress) {
    this.backupAddress = backupAddress;
  }

  /**
   * Getting Peer IP Address.
   *
   * @return peerAddress
   */
  public String getPeerAddress() {
    return peerAddress;
  }

  /**
   * Setting Peer IP Address.
   *
   * @param peerAddress
   *          Setting peerAddress
   */
  public void setPeerAddress(String peerAddress) {
    this.peerAddress = peerAddress;
  }

  /**
   * Stringizing instance.
   *
   */
  @Override
  public String toString() {
    return "MultiHomingValue [anycastAddress=" + anycastAddress + ", ifAddress=" + ifAddress + ", ifPrefix="
        + ifPrefix + ", backupAddress=" + backupAddress + ", peerAddress=" + peerAddress + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          Operation type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (anycastAddress == null) {
      throw new CheckDataException();
    }
    if (ifAddress == null) {
      throw new CheckDataException();
    }
    if (ifPrefix == null) {
      throw new CheckDataException();
    }
    if (backupAddress == null) {
      throw new CheckDataException();
    }
    if (peerAddress == null) {
      throw new CheckDataException();
    }
  }

}
