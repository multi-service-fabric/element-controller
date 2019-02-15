/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BootErrorMessages;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.EquipmentIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.LagMembers;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.NodesStartupNotification;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.devctrl.CreateInitialDeviceConfig;
import msf.ecmm.devctrl.DevctrlCommon;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.DhcpController;
import msf.ecmm.devctrl.HttpdController;
import msf.ecmm.devctrl.SyslogController;
import msf.ecmm.devctrl.XinetdController;
import msf.ecmm.devctrl.pojo.DhcpInfo;
import msf.ecmm.devctrl.pojo.InitialDeviceConfig;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.RecoverNodeService;

/**
 * Additional service recovery（Request Acception）.
 */
public class AcceptNodeRecover extends Operation {

  /** In case of invalid data. */
  private static final String ERROR_CODE_460101 = "460101";
  /** In case Restoration impossibility becomes impossible due to recovery permission judgment. */
  private static final String ERROR_CODE_460102 = "460102";
  /** In case No recovery target device, model after recovery. */
  private static final String ERROR_CODE_460201 = "460201";
  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_460401 = "460401";
  /** dhcpd、rsyslog、xinetd、httpd failed. */
  private static final String ERROR_CODE_460402 = "460402";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public AcceptNodeRecover(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.AcceptNodeRecover);
  }

  /**
   * Executing recovery expansion request reception processing.
   *
   * @see msf.ecmm.ope.execute.Operation#execute()
   */
  @Override
  public AbstractResponseMessage execute() {

    AbstractResponseMessage response = null;

    boolean createInitialConfigOkFlag = false;
    boolean dhcpOkFlag = false;
    boolean syslogOkFlag = false;
    boolean xinetdOkFlag = false;
    boolean needCleanUpFlag = true; 

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_460101);
    }

    RecoverNodeService inputData = (RecoverNodeService) getInData();
    nodeId = getUriKeyMap().get(KEY_NODE_ID);

    try (DBAccessManager session = new DBAccessManager()) {
      Nodes inputNodesDb = session.searchNodes(nodeId, null);
      if (inputNodesDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_460201);
      }
      Equipments outEquipments = session.searchEquipments(inputData.getEquipment().getEquipmentTypeId());
      if (outEquipments == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Equipments]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_460201);
      }

      if (!judgeRecoverPermission(inputNodesDb, outEquipments)) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Restoration impossible."));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_460102);
      }

      if (outEquipments.getRouter_type() != CommonDefinitions.ROUTER_TYPE_COREROUTER) {

        InitialDeviceConfig initialDeviceConfg = createInitialDeviceConfig(inputNodesDb, outEquipments);
        CreateInitialDeviceConfig.getInstance().create(initialDeviceConfg);
        createInitialConfigOkFlag = true;

        DhcpInfo dhcpInfo = createDhcpInfo(inputNodesDb, outEquipments);
        DhcpController.getInstance().start(dhcpInfo);
        dhcpOkFlag = true;

        startSyslogWatch(inputNodesDb, outEquipments);
        syslogOkFlag = true;

        XinetdController.getInstance().check();
        xinetdOkFlag = true;

        HttpdController.getInstance().check();

        NodesStartupNotification nodeStartupNotify = new NodesStartupNotification();
        nodeStartupNotify.setNode_id(nodeId);
        nodeStartupNotify.setNotification_reception_status(WAIT_NOTIFICATION);

        session.startTransaction();
        session.addNodesStartupNotification(nodeStartupNotify);
        session.commit();
      }

      OperationControlManager.getInstance().setRecoverExecution(true);

      NodeAdditionThread nodeAdditionThread = new NodeAdditionThread();
      nodeAdditionThread.setRecoverNodeInfo((RecoverNodeService) getInData());
      nodeAdditionThread.setRecoverNodeUriKeyMap(getUriKeyMap());
      OperationControlManager.getInstance().registerNodeAdditionInfo(nodeAdditionThread);

      if (outEquipments.getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
        needCleanUpFlag = false;
        nodeAdditionThread = OperationControlManager.getInstance().getNodeAdditionInfo();
        nodeAdditionThread.notifyNodeBoot(true);
      }

      response = makeSuccessResponse(RESP_ACCEPTED_202, new CommonResponse());

      needCleanUpFlag = false;

    } catch (DevctrlException de) {
      if (createInitialConfigOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "InitialConfig create failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_460402);
      } else if (dhcpOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "DHCP startup was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_460402);
      } else if (syslogOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Watch syslog was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_460402);
      } else if (xinetdOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Xinetd startup was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_460402);
      } else {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Httpd startup was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_460402);
      }
      OperationControlManager.getInstance().setRecoverExecution(false);
    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_460401);
      OperationControlManager.getInstance().setRecoverExecution(false);
    } finally {
      if (needCleanUpFlag == true) {
        DevctrlCommon.cleanUp();
      }
    }

    return response;
  }

  /**
   * Input data check.
   *
   * @see msf.ecmm.ope.execute.Operation#checkInData()
   */
  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    if (getUriKeyMap() == null) {
      checkResult = false;
    } else {
      if (!(getUriKeyMap().containsKey(KEY_NODE_ID)) || getUriKeyMap().get(KEY_NODE_ID) == null) {
        checkResult = false;
      }
    }

    RecoverNodeService inputData = (RecoverNodeService) getInData();
    try {
      inputData.check(new OperationType(getOperationType()));
    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      checkResult = false;
    }

    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

  /**
   * judge recovery permission.
   *
   * @param inNodes
   *          device information befor recovery
   * @param outEquipments
   *          model information after recovery
   * @return possible：true／impossible：false
   */
  private boolean judgeRecoverPermission(Nodes inNodes, Equipments outEquipments) {

    List<Integer> inUsePhysiIfIdList = new ArrayList<Integer>();
    if (inNodes.getPhysicalIfsList() == null) {
      return false;
    } else {
      for (PhysicalIfs inPhysiIfs : inNodes.getPhysicalIfsList()) {
        if (inPhysiIfs.getIf_speed() != null) {
          if (!compareIfSpeed(inPhysiIfs.getIf_speed(), outEquipments)) {
            logger.debug("IF speed not registered. PhysicalIfSpeed=" + inPhysiIfs.getIf_speed());
            return false;
          }
          inUsePhysiIfIdList.add(new Integer(inPhysiIfs.getPhysical_if_id()));
        }
        if (inPhysiIfs.getBreakoutIfsList() != null && !inPhysiIfs.getBreakoutIfsList().isEmpty()) {
          if (outEquipments.getBreakout_if_name_syntax() == null) {
            logger.debug("breakout not allowed. equipmentTypeId=" + outEquipments.getEquipment_type_id());
            return false;
          }
          for (BreakoutIfs inBoIfs : inPhysiIfs.getBreakoutIfsList()) {
            if (inBoIfs.getSpeed() != null) {
              Integer boBaseIfSpeed = Integer.parseInt(inBoIfs.getSpeed().replace(SPEED_UNIT_GIGA, ""))
                  * inPhysiIfs.getBreakoutIfsList().size();
              if (!compareIfSpeed(boBaseIfSpeed.toString() + SPEED_UNIT_GIGA, outEquipments)) {
                logger.debug("IF speed not registered. breakoutedIfSpeed=" + inBoIfs.getSpeed());
                return false;
              }
              inUsePhysiIfIdList.add(new Integer(inBoIfs.getPhysical_if_id()));
              break;
            }
          }
        }
      }
    }
    if (inNodes.getLagIfsList() != null) {
      for (LagIfs inLagIfs : inNodes.getLagIfsList()) {
        if (!compareIfSpeed(inLagIfs.getIf_speed(), outEquipments)) {
          logger.debug("IF speed not registered. lagIfSpeed=" + inLagIfs.getIf_speed());
          return false;
        }
        if (inLagIfs.getLagMembersList() != null) {
          for (LagMembers inLagMem : inLagIfs.getLagMembersList()) {
            if (inLagMem.getPhysical_if_id() == null) {
              if (inLagMem.getBreakout_if_id() != null) {
                physiIfsLoop: for (PhysicalIfs inPhysiIfs : inNodes.getPhysicalIfsList()) {
                  for (BreakoutIfs inBoIfs : inPhysiIfs.getBreakoutIfsList()) {
                    if (inBoIfs.getBreakout_if_id().equals(inLagMem.getBreakout_if_id())) {
                      inUsePhysiIfIdList.add(new Integer(inBoIfs.getPhysical_if_id()));
                      break physiIfsLoop;
                    }
                  }
                }
              }
            } else {
              inUsePhysiIfIdList.add(new Integer(inLagMem.getPhysical_if_id()));
            }
          }
        }
      }
    }

    if (!inUsePhysiIfIdList.isEmpty()) {
      Collections.sort(inUsePhysiIfIdList);
      List<Integer> outEquipIfIdList = new ArrayList<Integer>();
      for (EquipmentIfs outEquipIfs : outEquipments.getEquipmentIfsList()) {
        if (!outEquipIfIdList.contains(new Integer(outEquipIfs.getPhysical_if_id()))) {
          outEquipIfIdList.add(new Integer(outEquipIfs.getPhysical_if_id()));
        }
      }
      if (outEquipIfIdList.isEmpty()) {
        logger.debug("EquipmentIfs is Empty.");
        return false;
      }
      Collections.sort(outEquipIfIdList);
      if (inUsePhysiIfIdList.get(inUsePhysiIfIdList.size() - 1) > outEquipIfIdList.get(outEquipIfIdList.size() - 1)) {
        logger.debug("In use IFID maximum value does not exist. inUsePhysiIfIdMax=%d, outEquipIfIdMax=%d",
            inUsePhysiIfIdList.get(inUsePhysiIfIdList.size() - 1), outEquipIfIdList.get(outEquipIfIdList.size() - 1));
        return false;
      }
    }

    if (inNodes.getVpn_type() == null) {
      logger.debug("No VPN type specification. vpnType=null");
      return false;
    }
    if (inNodes.getVpn_type().equals(VPNTYPE_L2)) {
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

    if (inNodes.getEquipments().getRouter_type() != outEquipments.getRouter_type()) {
      logger.debug("Router type mismatch.");
      return false;
    }

    return true;
  }

  /**
   * IF speed comparison.
   *
   * @param destSpeed
   *          Comparison target IF speed
   * @param srcEquip
   *          Model with comparison source IF
   * @return After recovering, you can select the speed of IF before recovery in model：true／Can not be selected：false
   */
  private boolean compareIfSpeed(String destSpeed, Equipments srcEquip) {
    boolean ret = false;
    for (EquipmentIfs outEqIfs : srcEquip.getEquipmentIfsList()) {
      if (destSpeed.equals(outEqIfs.getPort_speed())) {
        ret = true;
        break;
      }
    }
    return ret;
  }

  /**
   * DHCP Information Creation.
   *
   * @param equipments
   *          model information
   * @return DHCP information
   */
  private DhcpInfo createDhcpInfo(Nodes nodes, Equipments equipments) {
    logger.trace(CommonDefinitions.START);

    RecoverNodeService recoverNode = (RecoverNodeService) getInData();
    DhcpInfo dhcpInfo = new DhcpInfo(equipments.getDhcp_template(), equipments.getInitial_config(),
        nodes.getHost_name(), recoverNode.getNode().getMacAddr(), "", nodes.getNtp_server_address(),
        nodes.getManagement_if_address(), nodes.getMng_if_addr_prefix());

    logger.trace(CommonDefinitions.END);
    return dhcpInfo;
  }

  /**
   * Syslog Monitoring Start-up.
   *
   * @param equipments
   *          model information
   * @throws DevctrlException
   *           device related exception
   */
  private void startSyslogWatch(Nodes nodes, Equipments equipments) throws DevctrlException {
    logger.trace(CommonDefinitions.START);

    List<String> bootErrorMessages = new ArrayList<>();
    for (BootErrorMessages message : equipments.getBootErrorMessagesList()) {
      bootErrorMessages.add(message.getBoot_error_msgs());
    }
    SyslogController syslogController = SyslogController.getInstance();
    syslogController.monitorStart(nodes.getManagement_if_address(), equipments.getBoot_complete_msg(),
        bootErrorMessages);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Initial config setting creation.
   *
   * @param equipments
   *          model information
   * @return Initial config setting
   * @throws DevctrlException
   *           DevctrlException
   */
  private InitialDeviceConfig createInitialDeviceConfig(Nodes nodes, Equipments equipments) throws DevctrlException {
    logger.trace(CommonDefinitions.START);

    String bgpCommunity = null;
    String bgpCommunityWildcard = null;
    if (nodes.getAs_number() != null) {
      if (nodes.getPlane() != null) {
        bgpCommunity = new StringBuffer().append(nodes.getAs_number()).append(":").append(nodes.getPlane()).toString();
      }
      bgpCommunityWildcard = new StringBuffer().append(nodes.getAs_number()).append(":*").toString();
    }

    InitialDeviceConfig initialDeviceConfigInfo = new InitialDeviceConfig(equipments.getConfig_template(),
        equipments.getInitial_config(), nodes.getManagement_if_address(), nodes.getNtp_server_address(),
        bgpCommunityWildcard, bgpCommunity, nodes.getMng_if_addr_prefix());
    logger.trace(CommonDefinitions.END);
    return initialDeviceConfigInfo;

  }

}
