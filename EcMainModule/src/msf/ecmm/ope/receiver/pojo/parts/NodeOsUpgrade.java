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
 * Class of node information when OS is upgraded)
 */
public class NodeOsUpgrade {

  /** The node type. */
  private String nodeType;
  /** The path of the script for executing OS upgrade in the node. */
  private String upgradeScriptPath;
  /** The flag which shows whether ZTP is required to execute or not. */
  private Boolean ztpFlag;
  /** The message for the decision on whether OS has been successfully upgraded or not. */
  private String upgradeCompleteMsg;
  /** The message list for the decision on whether OS has been not successfully upgraded or not. */
  private List<String> upgradeErrorMsgs = new ArrayList<>();

  /**
   * The node type is acquired.
   *
   * @return The node type
   */
  public String getNodeType() {
    return nodeType;
  }

  /**
   * The node type is set.
   *
   * @param nodeType
   *          The node type
   */
  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  /**
   * The path for the script to upgrade the node is acquired.
   *
   * @return The path for the script to upgrade the node is acquired
   */
  public String getUpgradeScriptPath() {
    return upgradeScriptPath;
  }

  /**
   * The path for the script to upgrade the node is set.
   *
   * @param upgradeScriptPath
   *          The path for the script to upgrade the node
   */
  public void setUpgradeScriptPath(String upgradeScriptPath) {
    this.upgradeScriptPath = upgradeScriptPath;
  }

  /**
   * The information on whether ZTP should be executed or not is acquired.
   *
   * @return The flag on  whether ZTP should be executed or not
   */
  public Boolean getZtpFlag() {
    return ztpFlag;
  }

  /**
   * The information on whether ZTP should be executed or not is set.
   *
   * @param ztpFlag
   *          The flag on  whether ZTP should be executed or not
   */
  public void setZtpFlag(Boolean ztpFlag) {
    this.ztpFlag = ztpFlag;
  }

  /**
   * The message for confirming the completion of OS upgrade process is acquired.
   *
   * @return The message for confirming the completion of OS upgrade process
   */
  public String getUpgradeCompleteMsg() {
    return upgradeCompleteMsg;
  }

  /**
   * The message for confirming the completion of OS upgrade process is set.
   *
   * @param upgradeCompleteMsg
   *          The message for confirming the completion of OS upgrade process
   */
  public void setUpgradeCompleteMsg(String upgradeCompleteMsg) {
    this.upgradeCompleteMsg = upgradeCompleteMsg;
  }

  /**
   * The message list for confirming the failure of OS upgrade process is acquired.
   *
   * @return The message list for confirming the failure of OS upgrade process.
   */
  public List<String> getUpgradeErrorMsgs() {
    return upgradeErrorMsgs;
  }

  /**
   * The message list for confirming the failure of OS upgrade process is set.
   *
   * @param upgradeErrorMsgs
   *          The message list for confirming the failure of OS upgrade process.
   */
  public void setUpgradeErrorMsgs(List<String> upgradeErrorMsgs) {
    this.upgradeErrorMsgs = upgradeErrorMsgs;
  }

  /* (non Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "NodeOsUpgrade [nodeType=" + nodeType + ", upgradeScriptPath=" + upgradeScriptPath + ", ztpFlag=" + ztpFlag
        + ", upgradeCompleteMsg=" + upgradeCompleteMsg + ", upgradeErrorMsgs=" + upgradeErrorMsgs + "]";
  }

  /**
   * The input parameter is checked.
   *
   * @param ope
   *          The Operation type
   * @throws CheckDataException
   *          The input paramter error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (nodeType == null) {
      throw new CheckDataException();
    }
    if (!nodeType.equals(CommonDefinitions.NODETYPE_SPINE) && !nodeType.equals(CommonDefinitions.NODETYPE_LEAF)
        && !nodeType.equals(CommonDefinitions.NODETYPE_BLEAF)) {
      throw new CheckDataException();
    }
    if (upgradeScriptPath == null) {
      throw new CheckDataException();
    }
    if (ztpFlag == null) {
      throw new CheckDataException();
    }
    if (upgradeCompleteMsg == null) {
      throw new CheckDataException();
    }
  }
}
