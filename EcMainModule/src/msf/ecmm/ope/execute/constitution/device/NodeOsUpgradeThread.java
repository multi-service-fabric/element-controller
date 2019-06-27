/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.common.CommonDefinitionsNodeOsUpgrade.*;

import java.util.Calendar;
import java.util.Date;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.config.NodeOsUpgradeConfiguration;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.DhcpController;
import msf.ecmm.devctrl.NodeOsUpgradeScriptExecutor;
import msf.ecmm.devctrl.SyslogController;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.UpdateNodeInfo;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.UpgradeOperationToFc;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.receiver.pojo.NodeOsUpgradeRequest;

/**
 * The thread class for upgrading OS in the node.
 */
public class NodeOsUpgradeThread extends Thread {

  /**
   * Logger.
   */
  private static final MsfLogger logger = new MsfLogger();

  /** FC request. */
  private NodeOsUpgradeRequest fcdata;

  /** The node inforation. */
  private Nodes nodes;

  /** The equiment infomation after OS is upgraded. */
  private Equipments newEq;

  /** The polling interval(msec). */
  private static final int POLLING_INTERVAL = 1000;

  @Override
  public void run() {
    logger.trace(CommonDefinitions.START);
    logger.info(LogFormatter.out.format(LogFormatter.MSG_303111));

    boolean result = false;

    try {
      try (DBAccessManager session = new DBAccessManager()) {

        if (!NodeOsUpgrade.control(NODE_OS_UPGRADE_WAIT)) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Operation is not started."));
          return;
        }

        new NodeOsUpgradeScriptExecutor().executeNodeOsUpgradeScript(fcdata.getNode().getUpgradeScriptPath(),
            nodes.getManagement_if_address(), nodes.getUsername(), nodes.getPassword());

        waitEndOfProcess();

        checkNotifyedIpAddr();

        session.startTransaction();

        session.updateNodeEquipment(nodes.getNode_id(), newEq.getEquipment_type_id());

        UpdateNodeInfo updateNodeInfo = EmMapper.toNodeInfoUpdate(nodes, newEq);

        AbstractMessage emResult = EmController.getInstance().request(updateNodeInfo);
        if (!emResult.isResult()) {
          throw new EmctrlException("EM result is failed.");
        }

        session.commit();

        result = true;

      } catch (DevctrlException de) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503112), de);
      } catch (DBAccessException dbae) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      } catch (EmctrlException ee) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      } catch (IllegalArgumentException iae) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."), iae);
      } catch (Exception exp) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "failed."), exp);
      }

      try {
        notifyNodeOsUpgradeResultToFc(result);
      } catch (RestClientException rce) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "failed."), rce);
      }

      try {
        DhcpController.getInstance().stop(true);
      } catch (DevctrlException de) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "DHCP stop was failed."), de);
      }

      try {
        SyslogController.getInstance().monitorStop(true);
      } catch (DevctrlException de) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Watch syslog stop was failed."), de);
      }

    } finally {
      OperationControlManager.getInstance().clearOperationInfo(NODE_OS_UPGRADE_NOTIFIED_IP_KEY);
      NodeOsUpgrade.control(null);

      logger.debug("end NodeOsUpgradeThread.");
      logger.trace(CommonDefinitions.END);
    }
  }

  /**
   * The completion notifition of the OS upgrade process is awaited  
   *
   * @throws Exception
   *           The OS upgrade process in the node has not been completed  normally.
   */
  private void waitEndOfProcess() throws Exception {

    logger.trace(CommonDefinitions.START);

    int timeout = NodeOsUpgradeConfiguration.getInstance().get(Integer.class,
        NodeOsUpgradeConfiguration.NODE_OS_UPGRADE_TIMER);
    Date startTime = new Date();
    Calendar timeoutTime = Calendar.getInstance();
    timeoutTime.setTime(startTime);
    timeoutTime.add(Calendar.SECOND, timeout);

    while (true) {

      String status = (String) OperationControlManager.getInstance().getOperationInfo(NODE_OS_UPGRADE_STATUS_KEY);
      if (status == null) {
        throw new Exception("Not Found Node Os Upgrade Status.");
      }
      if (status.equals(NODE_OS_UPGRADE_SUCCESS)) {
        logger.debug("Node os upgrade receive success notice.");
        break;
      }
      if (status.equals(NODE_OS_UPGRADE_FAILED)) {
        throw new Exception("Node os upgrade receive failure notice.");
      }

      CommonUtil.sleep(POLLING_INTERVAL);
      Date now = new Date();
      Calendar nowTime = Calendar.getInstance();
      nowTime.setTime(now);
      if (nowTime.compareTo(timeoutTime) > 0) {
        throw new Exception("Node Os Upgrade Process Timeout.");
      }
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * The notified IP address is checked.
   *
   * @throws IllegalArgumentException
   *           The Check Result is NG.
   */
  private void checkNotifyedIpAddr() throws IllegalArgumentException {
    logger.trace(CommonDefinitions.START);
    String notifyIpaddr = (String) OperationControlManager.getInstance()
        .getOperationInfo(NODE_OS_UPGRADE_NOTIFIED_IP_KEY);
    if (!nodes.getManagement_if_address().equals(notifyIpaddr)) {
      logger
          .debug("Unexpected ipaddr. expected:" + nodes.getManagement_if_address() + " notifyedIpaddr:" + notifyIpaddr);
      throw new IllegalArgumentException("Unexpected ipaddr.");
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * The completion of the OS upgrade in the node is notified
   *
   * @param result
   *          Result of the executedprocess
   * @throws RestClientException
   *          FC notification has a error.
   */
  private void notifyNodeOsUpgradeResultToFc(boolean result) throws RestClientException {
    logger.trace(CommonDefinitions.START);
    logger.debug("result=" + result);

    UpgradeOperationToFc fcNotificationMessage = new UpgradeOperationToFc();
    fcNotificationMessage.setFabricType(fcdata.getNode().getNodeType());
    fcNotificationMessage.setNodeId(nodes.getNode_id());
    fcNotificationMessage.setOperationType(OPETYPE_OS_UPGRADE);
    String notifyResult = "";
    if (result) {
      notifyResult = RECV_OK_NOTIFICATION_STRING_SUCCEEDED;
    } else {
      notifyResult = RECV_NG_NOTIFICATION_STRING;
    }
    fcNotificationMessage.setOsUpgradeResult(notifyResult);

    EcConfiguration config = EcConfiguration.getInstance();
    String fcIpaddr = config.get(String.class, EcConfiguration.FC_ADDRESS);
    String fcPort = config.get(String.class, EcConfiguration.FC_PORT);

    new RestClient().request(fcIpaddr, fcPort, RestClient.PUT, NODE_OS_UPGRADE_NOTIFICATION_URI, fcNotificationMessage,
        CommonResponseFromFc.class);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * FCrequest is acquired
   *
   * @return FCrequest.
   */
  public NodeOsUpgradeRequest getFcdata() {
    return fcdata;
  }

  /**
   * FCrequest is set.
   *
   * @param fcdata
   *          FCrequest.
   */
  public void setFcdata(NodeOsUpgradeRequest fcdata) {
    this.fcdata = fcdata;
  }

  /**
   * The node infomation is acquired.
   *
   * @return The node infomation
   */
  public Nodes getNodes() {
    return nodes;
  }

  /**
   * The node infomation is set.
   *
   * @param nodes
   *          The node infomation
   */
  public void setNodes(Nodes nodes) {
    this.nodes = nodes;
  }

  /**
   * The equipment infomation after OS is upgraded is acquired.
   *
   * @return The equipment infomation after OS is upgraded
   */
  public Equipments getNewEq() {
    return newEq;
  }

  /**
   * The equipment infomation after OS is upgraded is set.
   *
   * @param newEq
   *          The equipment infomation after OS is upgraded
   */
  public void setNewEq(Equipments newEq) {
    this.newEq = newEq;
  }
}
