/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitionsNodeOsUpgrade.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BootErrorMessages;
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
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.NodeOsUpgradeRequest;

/**
 * Class for upgrading  OS in the node 
 */
public class NodeOsUpgrade extends Operation {

  /** The name of the extened function operation. */
  String operationName = "NodeOsUpgrade";

  /** In case the check of the input paramter is NG. */
  private static final String ERROR_CODE_600101 = "600101";

  /** In case the OS upgrade process has been already executed. */
  private static final String ERROR_CODE_600302 = "600302";

  /** In case the infomation to be modified des not exist. */
  private static final String ERROR_CODE_600201 = "600201";

  /** In case the upgrade script does not exist. */
  private static final String ERROR_CODE_600202 = "600202";

  /** In case the error has occurred in the DB access. */	
  private static final String ERROR_CODE_600401 = "600401";

  /** 
   * The ztp preparation operation has failed as the following reasons.
   * (dhcpd, rsyslog, xinetd, httpd,
   *  the file where initial values are defined cannot be created in the node.
   */	
  private static final String ERROR_CODE_600402 = "600402";

  /** The name of the script executed when the OS has been successfully upgraded. */
  private static final String SUCCESS_SCRIPT = "os_update_success.sh";

  /** The name of the script executed when the OS has not been successfully upgraded. */
  private static final String FAILURE_SCRIPT = "os_update_fail.sh";

  /**
   * Constructor.
   *
   * @param idt
   *          Input data
   * @param ukm
   *          URI key information
   */
  public NodeOsUpgrade(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(operationName).getOperationTypeId());
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_600101);
    }
    NodeOsUpgradeRequest inputData = (NodeOsUpgradeRequest) getInData();

    if (!CommonUtil.isFile(inputData.getNode().getUpgradeScriptPath())) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found upgrade script file."));
      return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_600202);
    }

    if (!control(NODE_OS_UPGRADE_STARTED)) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Node os upgrade is already started."));
      return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_600302);
    }

    boolean dhcpOkFlag = false;
    boolean needCleanUpFlag = true; 
    boolean createInitialConfigOkFlag = false;
    boolean xinetdOkFlag = false;
    boolean startSyslogOkFlag = false;
    boolean httpdOkFlag = false;

    try (DBAccessManager session = new DBAccessManager()) {

      Nodes nodesDb = session.searchNodes(nodeId, null);
      if (nodesDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data from db. [nodes]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_600201);
      }

      Equipments newEqDb = session.searchEquipments(inputData.getEquipment().getEquipmentTypeId());
      if (newEqDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db. [equipments]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_600201);
      }

      fetch(nodesDb, newEqDb);


      InitialDeviceConfig initialDeviceConfg = createInitialDeviceConfig(nodesDb, newEqDb);
      CreateInitialDeviceConfig.getInstance().create(initialDeviceConfg);
      createInitialConfigOkFlag = true;

      DhcpInfo dhcpInfo = createDhcpInfo(nodesDb, newEqDb);
      DhcpController.getInstance().start(dhcpInfo);
      dhcpOkFlag = true;

      startSyslogWatch(nodesDb, newEqDb);
      startSyslogOkFlag = true;

      XinetdController.getInstance().check();
      xinetdOkFlag = true;

      HttpdController.getInstance().check();
      httpdOkFlag = true;

      startWorkerThread(nodesDb, newEqDb);

      response = makeSuccessResponse(RESP_ACCEPTED_202, new CommonResponse());

      needCleanUpFlag = false;

    } catch (DevctrlException de) {
      if (createInitialConfigOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "InitialConfig create failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_600402);
      } else if (dhcpOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "DHCP startup was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_600402);
      } else if (startSyslogOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Watch syslog was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_600402);
      } else if (xinetdOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Xinetd startup was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_600402);
      } else if (httpdOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Httpd startup was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_600402);
      }
    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_600401);
    } finally {
      if (needCleanUpFlag == true) {
        DevctrlCommon.cleanUp();
        control(null);
      }
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    NodeOsUpgradeRequest inputData = (NodeOsUpgradeRequest) getInData();
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
   * The DHCP infomation is generated.
   *
   * @param equipments
   *         The equipment information
   * @return DHCP information
   */
  private DhcpInfo createDhcpInfo(Nodes nodes, Equipments equipments) {
    logger.trace(CommonDefinitions.START);

    DhcpInfo dhcpInfo = new DhcpInfo(equipments.getDhcp_template(), equipments.getInitial_config(),
        nodes.getHost_name(), nodes.getMac_addr(), "", nodes.getNtp_server_address(), nodes.getManagement_if_address(),
        nodes.getMng_if_addr_prefix());

    logger.trace(CommonDefinitions.END);
    return dhcpInfo;
  }

  /**
   * The monitoring of system logs is initiated.
   *
   * @param equipments
   *          Information on the equipment type
   * @throws DevctrlException
   *          The exceptions related to the equiment
   */
  private void startSyslogWatch(Nodes nodes, Equipments equipments) throws DevctrlException {
    logger.trace(CommonDefinitions.START);

    List<String> bootErrorMessages = new ArrayList<>();
    for (BootErrorMessages message : equipments.getBootErrorMessagesList()) {
      bootErrorMessages.add(message.getBoot_error_msgs());
    }
    NodeOsUpgradeRequest inputData = (NodeOsUpgradeRequest) getInData();
    SyslogController syslogController = SyslogController.getInstance();
    syslogController.monitorStart(nodes.getManagement_if_address(), inputData.getNode().getUpgradeCompleteMsg(),
        bootErrorMessages, SUCCESS_SCRIPT, FAILURE_SCRIPT);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * The information required to add the new equipment in the system is generated. 
   *
   * @param equipments
   *          The equipment  information 
   * @return  The information required to add the new equipment in the system
   * @throws DevctrlException
   *          DevctrlException
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

  /**
   * The thread to execute the OS upgrade operation in the node is initiated. 
   *
   * @param nodes
   *          The equipement information
   * @param newEq
   *          The Type information after OS is upgraded
   */
  private void startWorkerThread(Nodes nodes, Equipments newEq) {
    logger.trace(CommonDefinitions.START);

    NodeOsUpgradeThread nodeOsUpgradeThread = new NodeOsUpgradeThread();
    nodeOsUpgradeThread.setFcdata((NodeOsUpgradeRequest) getInData());
    nodeOsUpgradeThread.setNodes(nodes);
    nodeOsUpgradeThread.setNewEq(newEq);
    nodeOsUpgradeThread.start();

    logger.trace(CommonDefinitions.END);
  }

  /**
   * The acquired DB information is fetched.   
   *
   * @param nodes
   *          The node information
   * @param equipments
   *          The equipment information
   */
  private void fetch(Nodes nodes, Equipments equipments) {

    nodes.getManagement_if_address().toString();
    nodes.getUsername().toString();
    nodes.getPassword().toString();
    nodes.getNode_id().toString();
    nodes.getNode_name().toString();

    equipments.getEquipment_type_id().toString();
    equipments.getPlatform_name().toString();
    equipments.getOs_name().toString();
    equipments.getFirmware_version().toString();
  }

  /**
   * The deleting and the setting operations are managed.
   *
   * @param value
   *          Value (Null is set when it is deleted.)
   * @return  Result( true:success, false:fail)
   */
  protected static synchronized boolean control(Object value) {

    boolean ret = false;
    String key = NODE_OS_UPGRADE_STATUS_KEY;

    if (value == null) {
      OperationControlManager.getInstance().clearOperationInfo(key);
      ret = true;
    } else {
      Object getValue = OperationControlManager.getInstance().getOperationInfo(key);

      if (getValue == null && value.equals(NODE_OS_UPGRADE_STARTED)) {
        OperationControlManager.getInstance().setOperationInfo(key, value);
        ret = true;
      } else if (getValue != null && getValue.equals(NODE_OS_UPGRADE_STARTED) && value.equals(NODE_OS_UPGRADE_WAIT)) {
        OperationControlManager.getInstance().setOperationInfo(key, value);
        ret = true;
      } else if ((getValue != null && getValue.equals(NODE_OS_UPGRADE_WAIT))
          && (value.equals(NODE_OS_UPGRADE_SUCCESS) || value.equals(NODE_OS_UPGRADE_FAILED))) {
        OperationControlManager.getInstance().setOperationInfo(key, value);
        ret = true;
      }
    }
    return ret;
  }
}
