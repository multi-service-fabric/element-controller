/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

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

  /** Flag showing node OS upgrade. */
  private Boolean nodeUpgrade;

  /** Internal linl information list. */
  private List<InternalLinkOppo> internalLinkIfs = new ArrayList<>();

  /**
   * Getting Node Type.
   *
   * @return nodeType
   */
  public String getNodeType() {
    return nodeType;
  }

  /**
   * Setting Node Type.
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
   * (non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "NodeRecoverNode [nodeType=" + nodeType + ", username=" + username + ", password=" + password + ", macAddr="
        + macAddr + ", nodeUpgrade=" + nodeUpgrade + ", internalLinkIfs=" + internalLinkIfs + "]";
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
      if (!nodeType.equals(CommonDefinitions.NODETYPE_LEAF) && !nodeType.equals(CommonDefinitions.NODETYPE_BLEAF)
          && !nodeType.equals(CommonDefinitions.NODETYPE_SPINE)) {
        throw new CheckDataException();
      }
    }
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
   * Getting flag showing node OS upgrade
   *
   * @return flag showing node OS upgrade
   */
  public Boolean getNodeUpgrade() {
    return nodeUpgrade;
  }

  /**
   * Setting flag showing node OS upgrade
   *
   * @param nodeUpgrade
   *          flag showing node OS upgrade
   */
  public void setNodeUpgrade(Boolean nodeUpgrade) {
    this.nodeUpgrade = nodeUpgrade;
  }

  /**
   * Getting internal linl information list
   *
   * @return internal linl information list
   */
  public List<InternalLinkOppo> getInternalLinkIfs() {
    return internalLinkIfs;
  }

  /**
   * Setting internal linl information list
   *
   * @param internalLinkIfs
   *          internal linl information list
   */
  public void setInternalLinkIfs(List<InternalLinkOppo> internalLinkIfs) {
    this.internalLinkIfs = internalLinkIfs;
  }

}
