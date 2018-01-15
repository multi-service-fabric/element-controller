/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Device Information for Device Registration.
 */
public class NodeRegisterNode {

  /** Device ID. */
  private String nodeId;

  /** Host Name. */
  private String hostName;

  /** MAC Address. */
  private String macAddr;

  /** NTP Server Address. */
  private String ntpServerAddress;

  /** Management IF Information. */
  private AddressInfo managementInterface;

  /** SNMP Community Name. */
  private String snmpCommunity;

  /**
   * Getting device ID.
   *
   * @return device ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting device ID.
   *
   * @param nodeId
   *          device ID
   */
  public void setNodeId(String nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * Getting host name.
   *
   * @return host name
   */
  public String getHostName() {
    return hostName;
  }

  /**
   * Setting host name.
   *
   * @param hostName
   *          host name
   */
  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  /**
   * Getting MAC address.
   *
   * @return MAC address
   */
  public String getMacAddr() {
    return macAddr;
  }

  /**
   * Setting MAC address.
   *
   * @param macAddr
   *          MAC address
   */
  public void setMacAddr(String macAddr) {
    this.macAddr = macAddr;
  }

  /**
   * Getting NTP server address.
   *
   * @return NTP server address
   */
  public String getNtpServerAddress() {
    return ntpServerAddress;
  }

  /**
   * Setting NTP server address.
   *
   * @param ntpServerAddress
   *          NTP server address
   */
  public void setNtpServerAddress(String ntpServerAddress) {
    this.ntpServerAddress = ntpServerAddress;
  }

  /**
   * Getting management IF information.
   *
   * @return management IF information
   */
  public AddressInfo getManagementInterface() {
    return managementInterface;
  }

  /**
   * Setting management IF information.
   *
   * @param managementInterface
   *          management IF information
   */
  public void setManagementInterface(AddressInfo managementInterface) {
    this.managementInterface = managementInterface;
  }

  /**
   * Getting SNMP community name.
   *
   * @return SNMP community name
   */
  public String getSnmpCommunity() {
    return snmpCommunity;
  }

  /**
   * Setting SNMP community name.
   *
   * @param snmpCommunity
   *          SNMP community name
   */
  public void setSnmpCommunity(String snmpCommunity) {
    this.snmpCommunity = snmpCommunity;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "NodeRegisterNode [nodeId=" + nodeId + ", hostName=" + hostName + ", macAddr=" + macAddr
        + ", ntpServerAddress=" + ntpServerAddress + ", managementInterface=" + managementInterface + ", snmpCommunity="
        + snmpCommunity + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (nodeId == null) {
      throw new CheckDataException();
    }
    if (hostName == null) {
      throw new CheckDataException();
    }
    if (macAddr == null) {
      throw new CheckDataException();
    }
    if (ntpServerAddress == null) {
      throw new CheckDataException();
    }
    if (managementInterface == null) {
      throw new CheckDataException();
    } else {
      managementInterface.check(ope);
    }
    if (snmpCommunity == null) {
      throw new CheckDataException();
    }
  }

}
