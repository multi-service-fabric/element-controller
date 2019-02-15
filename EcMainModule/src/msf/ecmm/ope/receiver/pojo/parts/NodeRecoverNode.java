/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Service Recover Node Infomation.
 */
public class NodeRecoverNode {

  /** Node Type. */
  private String nodeType;

  /** Login user name. */
  private String username;

  /** Login password. */
  private String password;

  /** MAC Address. */
  private String macAddr;

  /**
   * Getting Node Type.
   *
   * @return nodeType
   */
  public String getNodeType() {
    return nodeType;
  }

  /**
   *  Setting Node Type.
   *
   * @param nodeType
   *          setting nodeType
   */
  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  /**
   * Getting User name.
   *
   * @return username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Setting Login User name.
   *
   * @param username
   *          setting username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Getting login password.
   *
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Setting login password.
   *
   * @param password
   *          Setting password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Getting Mac Address.
   *
   * @return macAddr
   */
  public String getMacAddr() {
    return macAddr;
  }

  /**
   * Setting Mac Address.
   *
   * @param macAddr
   *          setting macAddr
   */
  public void setMacAddr(String macAddr) {
    this.macAddr = macAddr;
  }

  /**
   *  Stringizing Instance
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "NodeRecoverNode [nodeType=" + nodeType + ", username=" + username + ", password=" + password + ", macAddr="
        + macAddr + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *           operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {

    if (nodeType == null) {
      throw new CheckDataException();
    } else {
      if (!nodeType.equals(CommonDefinitions.NODETYPE_LEAF) && !nodeType.equals(CommonDefinitions.NODETYPE_BLEAF)) {
        throw new CheckDataException();
      }
    }
    if (username == null) {
      throw new CheckDataException();
    }
    if (password == null) {
      throw new CheckDataException();
    }
    if (macAddr == null) {
      throw new CheckDataException();
    }

  }

}
