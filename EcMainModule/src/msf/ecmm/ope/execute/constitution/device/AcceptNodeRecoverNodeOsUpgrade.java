/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.RecoverNodeService;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkOppo;

/**
 * The extended class (for receiving the request) to recover the service(when OS is upgraded in the node).
 */
public class AcceptNodeRecoverNodeOsUpgrade extends AcceptNodeRecover {

  /**
   * Constructor.
   *
   * @param idt
   *          The input data
   * @param ukm
   *          URI key information
   */
  public AcceptNodeRecoverNodeOsUpgrade(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    RecoverNodeService inputData = (RecoverNodeService) getInData();

    checkResult = super.checkInData();
    if (checkResult) {
      try {
        Boolean nodeUpgrade = inputData.getNode().getNodeUpgrade();
        if (nodeUpgrade == null || nodeUpgrade == false) {
          if (inputData.getNode().getNodeType().equals(CommonDefinitions.NODETYPE_SPINE)) {
            throw new CheckDataException();
          }
          if (inputData.getNode().getUsername() == null) {
            throw new CheckDataException();
          }
          if (inputData.getNode().getPassword() == null) {
            throw new CheckDataException();
          }
          if (inputData.getNode().getMacAddr() == null) {
            throw new CheckDataException();
          }
        } else {
          if (inputData.getNode().getNodeType().equals(CommonDefinitions.NODETYPE_SPINE)) {
            for (InternalLinkOppo ilo : inputData.getNode().getInternalLinkIfs()) {
              ilo.check(new OperationType(getOperationType()));
            }
          }
        }
      } catch (CheckDataException cde) {
        logger.warn("check error :", cde);
        checkResult = false;
      }
    }
    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

  @Override
  protected void baseCheck() throws CheckDataException {
    logger.trace(CommonDefinitions.START);
    logger.trace(CommonDefinitions.END);
  }

  @Override
  protected void setUnspecifiedInfo(Nodes nodes) {
    logger.trace(CommonDefinitions.START);
    RecoverNodeService inputData = (RecoverNodeService) getInData();
    if (inputData.getNode().getUsername() == null) {
      inputData.getNode().setUsername(nodes.getUsername());
    }
    if (inputData.getNode().getPassword() == null) {
      inputData.getNode().setPassword(nodes.getPassword());
    }
    if (inputData.getNode().getMacAddr() == null) {
      inputData.getNode().setMacAddr(nodes.getMac_addr());
    }
    logger.trace(CommonDefinitions.END);
  }

  @Override
  protected boolean needZtp(Equipments eq) {
    logger.trace(CommonDefinitions.START);
    RecoverNodeService inputData = (RecoverNodeService) getInData();
    boolean ret;
    if (eq.getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
      ret = false;
    } else if (inputData.getNode().getNodeUpgrade() != null && inputData.getNode().getNodeUpgrade() == true) {
      ret = false;
    } else {
      ret = true;
    }

    logger.debug("ret=" + ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  @Override
  protected NodeAdditionThread createNodeAdditionThread() {
    RecoverNodeService inputData = (RecoverNodeService) getInData();
    Boolean nodeUpgrade = inputData.getNode().getNodeUpgrade();
    if (nodeUpgrade == null || nodeUpgrade == false) {
      return new NodeAdditionThread();
    } else {
      return new NodeAdditionThreadNodeOsUpgrade();
    }
  }

  @Override
  protected boolean vpnCheck(Nodes inNodes, Equipments outEquipments) {
    if (inNodes.getVpn_type() == null) {
      if (outEquipments.getEvpn_capability() || outEquipments.getL2vpn_capability()
          || outEquipments.getL3vpn_capability()) {
        logger.debug("Spine not allowed.");
        return false;
      }
    } else if (inNodes.getVpn_type().equals(VPNTYPE_L2)) {
      if (!outEquipments.getEvpn_capability() || !outEquipments.getL2vpn_capability()) {
        logger.debug("L2VPN not allowed.");
        return false;
      }
    } else if (inNodes.getVpn_type().equals(VPNTYPE_L3)) {
      if (!outEquipments.getL3vpn_capability()) {
        logger.debug("L3VPN not allowed.");
        return false;
      }
    } else {
      logger.debug("VPN type is invalid. vpnType=" + inNodes.getVpn_type());
      return false;
    }

    return true;
  }

}
