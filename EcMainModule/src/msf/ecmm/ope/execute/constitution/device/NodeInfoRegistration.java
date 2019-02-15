/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BootErrorMessages;
import msf.ecmm.db.pojo.EquipmentIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
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
import msf.ecmm.ope.receiver.pojo.AddNode;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.parts.BreakoutBaseIf;
import msf.ecmm.ope.receiver.pojo.parts.CreateNode;
import msf.ecmm.ope.receiver.pojo.parts.InterfaceInfo;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkInfo;
import msf.ecmm.ope.receiver.pojo.parts.UnusePhysicalIf;

/**
 * Extention Information Registration.
 */
public class NodeInfoRegistration extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_080101 = "080101";
  /** In case the number of pieces of information of device extention/removal is zero (data acquisition from DBs is failed in the previous step of process). */
  private static final String ERROR_CODE_080102 = "080102";
  /** Detected unexpected unused physical IF ID or physical IF ID which is not registered in the model information. */
  private static final String ERROR_CODE_080104 = "080104";
  /** In case the device information to be registered already exists (incl. duplication of management IF address). */
  private static final String ERROR_CODE_080304 = "080304";
  /** DHCP start-up failed. */
  private static final String ERROR_CODE_080411 = "080411";
  /** Syslog monitoring start-up failed. */
  private static final String ERROR_CODE_080412 = "080412";
  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_080413 = "080413";
  /** Initial config file creation failed. */
  private static final String ERROR_CODE_080414 = "080414";
  /** Xinetd start-up failed. */
  private static final String ERROR_CODE_080415 = "080415";
  /** Httpd start-up failed. */
  private static final String ERROR_CODE_080416 = "080416";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public NodeInfoRegistration(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.NodeInfoRegistration);
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080101);
    }
    AddNode addNode = (AddNode) getInData();

    boolean dhcpOkFlag = false;
    boolean needCleanUpFlag = true; 
    boolean createInitialConfigOkFlag = false;
    boolean xinetdOkFlag = false;
    boolean startSyslogOkFlag = false;
    boolean httpdOkFlag = false;

    try (DBAccessManager session = new DBAccessManager()) {

      Equipments equipments = session.searchEquipments(addNode.getEquipment().getEquipmentTypeId());
      if (equipments == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Equipments]"));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080102);
      }

      if (equipments.getIrb_asymmetric_capability() && !equipments.getIrb_symmetric_capability()) {
        if (null != addNode.getCreateNode().getIrbType()
            && !addNode.getCreateNode().getIrbType().equals(CommonDefinitions.IRB_TYPE_ASYMMETRIC)) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "IRB Type is different."));
          return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080104);
        }
      } else if (!equipments.getIrb_asymmetric_capability() && equipments.getIrb_symmetric_capability()) {
        if (null != addNode.getCreateNode().getIrbType()
            && !addNode.getCreateNode().getIrbType().equals(CommonDefinitions.IRB_TYPE_SYMMETRIC)) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "IRB Type is different."));
          return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080104);
        }
      } else if (!equipments.getIrb_asymmetric_capability() && !equipments.getIrb_symmetric_capability()) {
        if (null != addNode.getCreateNode().getIrbType()) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "This Equipments does not support IRB."));
          return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080104);
        }
      }
      if (addNode.getCreateNode().getProvisioning() == true) {

        if (checkPhysicalIfIdUnuse(equipments) == false) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Found unused physical IF ID"));
          return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080104);
        }

        InitialDeviceConfig initialDeviceConfg = createInitialDeviceConfig(equipments);
        CreateInitialDeviceConfig.getInstance().create(initialDeviceConfg);
        createInitialConfigOkFlag = true;

        DhcpInfo dhcpInfo = createDhcpInfo(equipments);
        DhcpController.getInstance().start(dhcpInfo);
        dhcpOkFlag = true;

        startSyslogWatch(equipments);
        startSyslogOkFlag = true;

        XinetdController.getInstance().check();
        xinetdOkFlag = true;

        HttpdController.getInstance().check();
        httpdOkFlag = true;
      }

      if (!checkDoubleRegistration(equipments, addNode)) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Double registration."));
        return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_080304);
      }

      Nodes nodesDb = DbMapper.toAddNode(addNode, equipments);

      session.startTransaction();

      session.addNodes(nodesDb);

      session.commit();

      registerAddNodeInfo();

      if (addNode.getCreateNode().getProvisioning() == false) {
        needCleanUpFlag = false;
        NodeAdditionThread nodeAdditionThread = OperationControlManager.getInstance().getNodeAdditionInfo();
        nodeAdditionThread.notifyNodeBoot(true);
      }

      response = makeSuccessResponse(RESP_CREATED_201, new CommonResponse());

      needCleanUpFlag = false;

    } catch (DevctrlException de) {
      if (createInitialConfigOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "InitialConfig create failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080414);
      } else if (dhcpOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "DHCP startup was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080411);
      } else if (startSyslogOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Watch syslog was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080412);
      } else if (xinetdOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Xinetd startup was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080415);
      } else if (httpdOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Httpd startup was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080416);
      }

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      if (dbae.getCode() == DBAccessException.DOUBLE_REGISTRATION) {
        response = makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_080304);
      } else {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080413);
      }
    } finally {
      if (needCleanUpFlag == true) {
        DevctrlCommon.cleanUp();
      }
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    AddNode addNode = (AddNode) getInData();
    try {
      addNode.check(new OperationType(getOperationType()));
    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      checkResult = false;
    }
    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

  /**
   * Unused Physical IFID Check.
   *
   * @param equipments
   *          model information
   * @return true: there is no unexpected unused, false: there are ones
   */
  private boolean checkPhysicalIfIdUnuse(Equipments equipments) {
    logger.trace(CommonDefinitions.START);

    boolean ret = true;

    AddNode addNode = (AddNode) getInData();

    Set<String> eqIfIds = new HashSet<>();

    for (EquipmentIfs eqIfs : equipments.getEquipmentIfsList()) {
      eqIfIds.add(eqIfs.getPhysical_if_id()); 
    }

    for (BreakoutBaseIf boIfs : addNode.getCreateNode().getIfInfo().getBreakoutBaseIfs()) {
      if (eqIfIds.contains(boIfs.getBasePhysicalIfId()) == false) {
        logger.debug("Not found. " + boIfs.getBasePhysicalIfId());
        ret = false;
      } else {
        eqIfIds.remove(boIfs.getBasePhysicalIfId());
      }
    }

    for (InternalLinkInfo internalIf : addNode.getCreateNode().getIfInfo().getInternalLinkIfs()) {
      if (internalIf.getInternalLinkIf().getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
        if (eqIfIds.contains(internalIf.getInternalLinkIf().getIfId()) == false) {
          logger.debug("Not found. " + internalIf.getInternalLinkIf().getIfId());
          ret = false;
        } else {
          eqIfIds.remove(internalIf.getInternalLinkIf().getIfId());
        }
      } else if (internalIf.getInternalLinkIf().getIfType().equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
        for (InterfaceInfo lagMember : internalIf.getInternalLinkIf().getLagMember()) {
          if (lagMember.getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
            if (eqIfIds.contains(lagMember.getIfId()) == false) {
              logger.debug("Not found. " + lagMember.getIfId());
              ret = false;
            } else {
              eqIfIds.remove(lagMember.getIfId());
            }
          }
        }
      }
    }

    for (UnusePhysicalIf unusedIfId : addNode.getCreateNode().getIfInfo().getUnusePhysicalIfs()) {
      if (eqIfIds.contains(unusedIfId.getPhysicalIfId()) == false) {
        logger.debug("Not found. " + unusedIfId.getPhysicalIfId());
        ret = false;
      } else {
        eqIfIds.remove(unusedIfId.getPhysicalIfId());
      }
    }

    if (!eqIfIds.isEmpty()) {
      logger.debug("Found unused physicalIfId. " + eqIfIds);
      ret = false;
    }

    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DHCP Information Creation.
   *
   * @param equipments
   *          model information
   * @return DHCP information
   */
  private DhcpInfo createDhcpInfo(Equipments equipments) {
    logger.trace(CommonDefinitions.START);

    CreateNode nodeInfo = ((AddNode) getInData()).getCreateNode(); 
    DhcpInfo dhcpInfo = new DhcpInfo(equipments.getDhcp_template(), equipments.getInitial_config(),
        nodeInfo.getHostname(), nodeInfo.getMacAddress(), "", nodeInfo.getNtpServerAddress(), nodeInfo
            .getManagementInterface().getAddress(), nodeInfo.getManagementInterface().getPrefix());

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
  private void startSyslogWatch(Equipments equipments) throws DevctrlException {
    logger.trace(CommonDefinitions.START);
    CreateNode nodeInfo = ((AddNode) getInData()).getCreateNode(); 

    List<String> bootErrorMessages = new ArrayList<>();
    for (BootErrorMessages message : equipments.getBootErrorMessagesList()) {
      bootErrorMessages.add(message.getBoot_error_msgs());
    }
    SyslogController syslogController = SyslogController.getInstance();
    syslogController.monitorStart(nodeInfo.getManagementInterface().getAddress(), equipments.getBoot_complete_msg(),
        bootErrorMessages);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device Extention Information Retention.
   */
  private void registerAddNodeInfo() {
    logger.trace(CommonDefinitions.START);

    AddNode addNode = (AddNode) getInData();
    NodeAdditionThread nodeAdditionThread = new NodeAdditionThread();
    nodeAdditionThread.setAddNodeInfo(addNode);
    OperationControlManager.getInstance().registerNodeAdditionInfo(nodeAdditionThread);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Initial config setting creation.
   *
   * @param equipments
   *          model information
   * @return Initial config setting
   * @throws DevctrlException
   *           Initial config setting creation failed
   */
  private InitialDeviceConfig createInitialDeviceConfig(Equipments equipments) throws DevctrlException {
    logger.trace(CommonDefinitions.START);
    CreateNode nodeInfo = ((AddNode) getInData()).getCreateNode(); 

    if (nodeInfo.getVpn() == null) {
      InitialDeviceConfig initialDeviceConfigInfo = new InitialDeviceConfig(equipments.getConfig_template(),
          equipments.getInitial_config(), nodeInfo.getManagementInterface().getAddress(),
          nodeInfo.getNtpServerAddress(), null, null, nodeInfo.getManagementInterface().getPrefix());
      logger.trace(CommonDefinitions.END);
      return initialDeviceConfigInfo;
    }
    if (nodeInfo.getVpn().getVpnType().equals("l2")) {
      InitialDeviceConfig initialDeviceConfigInfo = new InitialDeviceConfig(equipments.getConfig_template(),
          equipments.getInitial_config(), nodeInfo.getManagementInterface().getAddress(),
          nodeInfo.getNtpServerAddress(), nodeInfo.getVpn().getL2vpn().getBgp().getCommunityWildcard(), nodeInfo
              .getVpn().getL2vpn().getBgp().getCommunity(), nodeInfo.getManagementInterface().getPrefix());
      logger.trace(CommonDefinitions.END);
      return initialDeviceConfigInfo;
    }
    if (nodeInfo.getVpn().getVpnType().equals("l3")) {
      InitialDeviceConfig initialDeviceConfigInfo = new InitialDeviceConfig(equipments.getConfig_template(),
          equipments.getInitial_config(), nodeInfo.getManagementInterface().getAddress(),
          nodeInfo.getNtpServerAddress(), nodeInfo.getVpn().getL3vpn().getBgp().getCommunityWildcard(), nodeInfo
              .getVpn().getL3vpn().getBgp().getCommunity(), nodeInfo.getManagementInterface().getPrefix());
      logger.trace(CommonDefinitions.END);
      return initialDeviceConfigInfo;
    } else {
      return null;
    }

  }

  /**
   * Duplication check for device registration.
   *   Switch fabric: Performing duplication check for device name, and management IF address.
   *   Core rooter: Conducting duplication check for device name as wel as for confirming that all devices (line card) have the same IP.
   *
   * @param equimpents
   *          Model information of extension target device (DB)
   * @param addNode
   *          Device information of the device to be extended (REST received from FC)
   * @return true: no duplication false: duplication
   * @throws DBAccessException
   *           DB exception
   */
  private boolean checkDoubleRegistration(Equipments equipments, AddNode addNode) throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    boolean ret = true;
    String nodeName = addNode.getCreateNode().getHostname();
    String ip = addNode.getCreateNode().getManagementInterface().getAddress();

    try (DBAccessManager session = new DBAccessManager()) {
      List<Nodes> dbNodesList = session.getNodesList();
      for (Nodes nodes : dbNodesList) {
        if (equipments.getRouter_type() == CommonDefinitions.ROUTER_TYPE_NORMAL) {
          if (nodes.getNode_name().equals(nodeName) || nodes.getHost_name().equals(nodeName)
              || nodes.getManagement_if_address().equals(ip)) {
            ret = false;
            break;
          }
        } else {
          if (nodes.getNode_name().equals(nodeName) || nodes.getHost_name().equals(nodeName)) {
            ret = false;
            break;
          }
          if (!nodes.getManagement_if_address().equals(ip)) {
            ret = false;
            break;
          }
        }
      }
    }
    logger.trace(CommonDefinitions.END);
    return ret;
  }
}
