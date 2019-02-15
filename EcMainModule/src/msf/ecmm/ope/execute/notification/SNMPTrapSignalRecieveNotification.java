/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.notification;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.LogicalPhysicalConverter;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.SnmpController;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.Operations;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.NotifyReceiveSnmpTrap;

/**
 * SNMPTrap Reception Notification.
 */
public class SNMPTrapSignalRecieveNotification extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_330101 = "330101";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_330301 = "330301";

  /** In case  REST request to FC has not succeeded even after the upper limit number of times of retries. */
  private static final String ERROR_CODE_330302 = "330302";

  /** In case the corresponding device information does not exist in DB. */
  private static final String ERROR_CODE_330303 = "330303";

  /** In case the acquired value from SNMP is invalid. */
  private static final String ERROR_CODE_330304 = "330304";

  /** Other exceptions. */
  private static final String ERROR_CODE_330399 = "330399";
  /** Device Status: Running. */
  private static final int NODE_STATE_OPERATION = 0;
  /** Device Status: Out of order. */
  private static final int NODE_STATE_MALFUNCTION = 7;

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public SNMPTrapSignalRecieveNotification(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.SNMPTrapSignalRecieveNotification);
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_330101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      SnmpController snmpController = new SnmpController();

      NotifyReceiveSnmpTrap snmpTrap = (NotifyReceiveSnmpTrap) getInData();

      Nodes nodesDb = session.searchNodes(null, snmpTrap.getSrcHostIp());

      boolean snmpflg = false;

      if (nodesDb == null) {
        logger.debug("There is no node info in DB.");
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_330303);
      }

      String ifName = "";
      if (nodesDb.getEquipments().getSnmptrap_if_name_oid() == null) {
        int ifIndex = SnmpController.getIfIndexForTrap(((NotifyReceiveSnmpTrap) getInData()).getVarbind());

        if (ifIndex == SnmpController.IFINDEX_NOT_FOUND) {
          logger.debug("Return values from SNMP is wrong.");
          return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_330304);
        }

        try {
          ifName = snmpController.getIfName(nodesDb.getEquipments(), nodesDb, ifIndex);
        } catch (DevctrlException de) {
          logger.debug("SNMP error", de);
          snmpflg = true;
        }

      } else {
        ifName = SnmpController.getIfNameForTrap(((NotifyReceiveSnmpTrap) getInData()).getVarbind(),
            nodesDb.getEquipments().getSnmptrap_if_name_oid());
      }

      String statusStr = null;
      if (getUriKeyMap().get(KEY_LINK_STATUS).equals(LINK_STATUS_LINKUP)) {
        statusStr = IF_STATE_OK_STRING;
      } else if (getUriKeyMap().get(KEY_LINK_STATUS).equals(LINK_STATUS_LINKDOWN)) {
        statusStr = IF_STATE_NG_STRING;
      }

      List<Nodes> nodesList = new ArrayList<Nodes>();
      for (Nodes elem : session.getNodesList()) {
        if (elem.getManagement_if_address().equals(snmpTrap.getSrcHostIp())) {
          nodesList.add(elem);
        }
      }

      String ifType = null;
      String ifId = null;
      PhysicalIfs physicalIfs = null;
      LagIfs lagIfs = null;
      BreakoutIfs breakoutIfs = null;
      List<VlanIfs> vlanIfsList = new ArrayList<VlanIfs>();
      if (!snmpflg) {
        searchNodesList: for (Nodes nodesElem : nodesList) {
          for (PhysicalIfs ifinfo : nodesElem.getPhysicalIfsList()) {
            if ((ifinfo.getIf_name() != null) && ifinfo.getIf_name().equals(ifName)) {
              ifType = IF_TYPE_PHYSICAL_IF;
              ifId = ifinfo.getPhysical_if_id();
              physicalIfs = ifinfo;
              physicalIfs.setIf_status(LogicalPhysicalConverter.toIntegerIFState(statusStr));
              break searchNodesList;
            }

            for (BreakoutIfs boIfs : ifinfo.getBreakoutIfsList()) {
              if ((boIfs.getIf_name() != null) && boIfs.getIf_name().equals(ifName)) {
                ifType = IF_TYPE_BREAKOUT_IF;
                ifId = boIfs.getBreakout_if_id();
                breakoutIfs = new BreakoutIfs();
                breakoutIfs.setNode_id(ifinfo.getNode_id());
                breakoutIfs.setBreakout_if_id(ifId);
                breakoutIfs.setIf_status(LogicalPhysicalConverter.toIntegerIFState(statusStr));
                break searchNodesList;
              }
            }
          }

          for (LagIfs ifinfo : nodesElem.getLagIfsList()) {
            if ((ifinfo.getIf_name() != null) && ifinfo.getIf_name().equals(ifName)) {
              ifType = IF_TYPE_LAG_IF;
              ifId = ifinfo.getFc_lag_if_id();
              lagIfs = ifinfo;
              lagIfs.setIf_status(LogicalPhysicalConverter.toIntegerIFState(statusStr));
              break searchNodesList;
            }
          }
        }

        for (Nodes nodesElem : nodesList) {
          for (VlanIfs dbElem : session.getVlanIfsList(nodesElem.getNode_id())) {
            VlanIfs vlanIfs = new VlanIfs();
            if (nodesElem.getVpn_type() != null) {
              if (nodesElem.getVpn_type().equals(CommonDefinitions.VPNTYPE_L2)) {
                if (dbElem.getIf_name().equals(ifName)) {
                  vlanIfs = dbElem;
                  vlanIfs.setIf_status(LogicalPhysicalConverter.toIntegerIFState(statusStr));
                  vlanIfsList.add(vlanIfs);
                }
              } else if (nodesElem.getVpn_type().equals(CommonDefinitions.VPNTYPE_L3)) {
                String tmpIfName = dbElem.getIf_name() + nodesElem.getEquipments().getUnit_connector()
                    + dbElem.getVlan_id();
                if (tmpIfName.equals(ifName)) {
                  vlanIfs = dbElem;
                  vlanIfs.setIf_status(LogicalPhysicalConverter.toIntegerIFState(statusStr));
                  vlanIfsList.add(vlanIfs);
                }
              }
            }
          }
          if (!vlanIfsList.isEmpty()) {
            break;
          }
        }
      }

      session.startTransaction();

      if ((nodesDb.getNode_state() != NODE_STATE_MALFUNCTION && snmpflg)
          || ((nodesDb.getNode_state() == NODE_STATE_MALFUNCTION) && !snmpflg) && null != ifType) {
        String nodeid = nodesDb.getNode_id();
        int nodestate = nodesDb.getNode_state();
        if (nodesDb.getNode_state() != NODE_STATE_MALFUNCTION && snmpflg) {
          nodestate = NODE_STATE_MALFUNCTION;
        } else if (nodesDb.getNode_state() == NODE_STATE_MALFUNCTION && !snmpflg) {
          nodestate = NODE_STATE_OPERATION;
        }
        session.updateNodeState(nodeid, nodestate);

      }
      if (!snmpflg &&  null != ifType) {
        session.updateNodeIfState(physicalIfs, lagIfs, null, breakoutIfs);
        for (VlanIfs elem : vlanIfsList) {
          session.updateNodeIfState(null, null, elem, null);
        }
      }

      session.commit();

      if (ifType != null) {
        Operations snmpTrapData = RestMapper.toSnmpTrapNotificationInfo(nodesDb.getNode_id(), ifType, ifId, vlanIfsList,
            statusStr, snmpflg);

        if (snmpTrapData == null) {
          logger.debug("Data Mapping error");
          return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_330399);
        }

        RestClient restClient = new RestClient();
        restClient.request(RestClient.OPERATION, new HashMap<String, String>(), snmpTrapData,
            CommonResponseFromFc.class);
      }
      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (DBAccessException dbe) {
      logger.debug("DB access error", dbe);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_330301);
    } catch (RestClientException re) {
      logger.debug("Rest request failed", re);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_330302);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    NotifyReceiveSnmpTrap notifyRecieveSnmpTrapRest = (NotifyReceiveSnmpTrap) getInData();

    if (getUriKeyMap() == null) {
      checkResult = false;
    } else if ((!getUriKeyMap().containsKey(KEY_LINK_STATUS))
        || ((!getUriKeyMap().get(KEY_LINK_STATUS).equals(LINK_STATUS_LINKUP))
            && (!getUriKeyMap().get(KEY_LINK_STATUS).equals(LINK_STATUS_LINKDOWN)))) {
      checkResult = false;
    } else {
      try {
        notifyRecieveSnmpTrapRest.check(new OperationType(getOperationType()));
      } catch (CheckDataException cde) {
        logger.warn("check error :", cde);
        checkResult = false;
      }
    }

    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

}
