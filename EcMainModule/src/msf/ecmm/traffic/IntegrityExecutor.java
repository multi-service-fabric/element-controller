/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.traffic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.LogicalPhysicalConverter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.SnmpController;
import msf.ecmm.devctrl.pojo.SnmpIfOperStatus;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.Operations;
import msf.ecmm.fcctrl.pojo.UpdateLogicalIfStatus;
import msf.ecmm.fcctrl.pojo.parts.IfsLogical;
import msf.ecmm.fcctrl.pojo.parts.NodeLogical;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.traffic.pojo.DeviceInformationSet;
import msf.ecmm.traffic.pojo.NodeKeySet;

/**
 * IF Status Integrity Execution Class Definition. Integrate IF status.
 */
public class IntegrityExecutor extends Thread {

  /**
   * Logger
   */
  private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** IF Information (Device Origin) */
  private HashMap<NodeKeySet, ArrayList<SnmpIfOperStatus>> ifStateFromDevice;

  /** Device Information */
  private HashMap<NodeKeySet, DeviceInformationSet> deviceDetail;

  /** Device Status: Out Of Order. */
  private static final int NODE_STATE_MALFUNCTION = 7;

  /** Device Status: Running. */
  private static final int NODE_STATE_OPERATION = 0;

  /**
   * Constructor
   */
  public IntegrityExecutor() {
    ifStateFromDevice = new HashMap<NodeKeySet, ArrayList<SnmpIfOperStatus>>();
    deviceDetail = new HashMap<NodeKeySet, DeviceInformationSet>();

  }

  /**
   * IF Status Integrit.
   */
  public void run() {

    logger.trace(CommonDefinitions.START);

    boolean startResult = OperationControlManager.getInstance().recievestartIfIngIntegrity();

    try {
      if (startResult) {

        try (DBAccessManager session = new DBAccessManager()) {
          List<Equipments> equipmentList = session.getEquipmentsList();
          Map<Equipments, List<Nodes>> nodesMap = new HashMap<>();
          for (Equipments eq : equipmentList) {
            nodesMap.put(eq, session.searchNodesByEquipmentId(eq.getEquipment_type_id()));
            for (Nodes nd : nodesMap.get(eq)) {
              NodeKeySet nodeKey = new NodeKeySet();
              DeviceInformationSet di = new DeviceInformationSet();

              nodeKey.setEquipmentsData(nd);
              nodeKey.setEquipmentsType(eq);
              di.setEquipmentsData(nd);
              di.setEquipmentsType(eq);
              deviceDetail.put(nodeKey, di);

              List<VlanIfs> vlanIfList = session.getVlanIfsList(nd.getNode_id());
              List<LagIfs> lagIfList = session.getLagIfsList(nd.getNode_id());
              List<PhysicalIfs> physicalIfList = session.getPhysicalIfsList(nd.getNode_id());
              List<BreakoutIfs> breakoutIfList = session.getBreakoutIfsList(nd.getNode_id());

              deviceDetail.get(nodeKey).setVlanIfData(new HashMap<>());
              for (VlanIfs vl : vlanIfList) {
                deviceDetail.get(nodeKey).getVlanIfData().put(Integer.valueOf(vl.getVlan_if_id()), vl);
              }

              deviceDetail.get(nodeKey).setLagIfData(new HashMap<>());
              for (LagIfs li : lagIfList) {
                deviceDetail.get(nodeKey).getLagIfData().put(Integer.valueOf(li.getFc_lag_if_id()), li);
              }

              deviceDetail.get(nodeKey).setPhysicalIfData(new HashMap<>());
              for (PhysicalIfs pi : physicalIfList) {
                deviceDetail.get(nodeKey).getPhysicalIfData().put(Integer.valueOf(pi.getPhysical_if_id()), pi);
              }

              deviceDetail.get(nodeKey).setBreakoutIfData(new HashMap<>());
              for (BreakoutIfs bi : breakoutIfList) {
                deviceDetail.get(nodeKey).getBreakoutIfData().put(bi.getBreakout_if_id(), bi);
              }
            }
          }

          Operations integrityResult = new Operations();
          UpdateLogicalIfStatus check = executeIntegrity();
          integrityResult.setUpdateLogicalIfStatusOption(check);
          integrityResult.setAction("update_logical_if_status");

          if (check != null) {
            if (check.getIfs() != null) {
              try (DBAccessManager updateSession = new DBAccessManager()) {
                for (IfsLogical ifs : check.getIfs()) {
                  logger.trace("IF status DB update START");
                  session.startTransaction();
                  PhysicalIfs physicalIfs = new PhysicalIfs();
                  LagIfs lagIfs = new LagIfs();
                  BreakoutIfs breakoutIfs = new BreakoutIfs();
                  VlanIfs vlanIf = new VlanIfs();
                  switch (ifs.getIfType()) {
                    case CommonDefinitions.IF_TYPE_PHYSICAL_IF:
                      physicalIfs.setNode_id(ifs.getNodeId());
                      physicalIfs.setPhysical_if_id(ifs.getIfId());
                      physicalIfs.setIf_status(LogicalPhysicalConverter.toIntegerIFState(ifs.getStatus()));
                      session.updateNodeIfState(physicalIfs, null, null, null);
                      break;

                    case CommonDefinitions.IF_TYPE_LAG_IF:
                      lagIfs.setNode_id(ifs.getNodeId());
                      for (NodeKeySet nodeElem : deviceDetail.keySet()) {
                        if (nodeElem.getEquipmentsData().getNode_id().equals(ifs.getNodeId())
                            && nodeElem.getEquipmentsData().getLagIfsList() != null) {
                          for (LagIfs lagElem : nodeElem.getEquipmentsData().getLagIfsList()) {
                            if (lagElem.getFc_lag_if_id().equals(ifs.getIfId())) {
                              lagIfs.setLag_if_id(lagElem.getLag_if_id());
                            }
                          }
                        }
                      }
                      lagIfs.setFc_lag_if_id(ifs.getIfId());
                      lagIfs.setIf_status(LogicalPhysicalConverter.toIntegerIFState(ifs.getStatus()));
                      session.updateNodeIfState(null, lagIfs, null, null);
                      break;

                    case CommonDefinitions.IF_TYPE_BREAKOUT_IF:
                      breakoutIfs.setNode_id(ifs.getNodeId());
                      breakoutIfs.setBreakout_if_id(ifs.getIfId());
                      breakoutIfs.setIf_status(LogicalPhysicalConverter.toIntegerIFState(ifs.getStatus()));
                      session.updateNodeIfState(null, null, null, breakoutIfs);
                      break;

                    case CommonDefinitions.IF_TYPE_VLAN_IF:
                      vlanIf.setNode_id(ifs.getNodeId());
                      vlanIf.setVlan_if_id(ifs.getIfId());
                      vlanIf.setIf_status(LogicalPhysicalConverter.toIntegerIFState(ifs.getStatus()));
                      session.updateNodeIfState(null, null, vlanIf, null);
                      break;
                    default:
                      break;
                  }
                  session.commit();
                }
                logger.trace("IF Status DB update END");
              } catch (DBAccessException db) {
                logger.debug("IF Status DB update DB ERROR", db);
              } catch (Exception db) {
                logger.debug("IF Status DB update other ERROR", db);
              }
            }
            RestClient resultFc = new RestClient();
            resultFc.request(RestClient.OPERATION, new HashMap<String, String>(), integrityResult,
                CommonResponseFromFc.class);
          } else {
          }
        }
      } else {
      }
    } catch (Exception e1) {
      logger.trace("Error Contents", e1);
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_407043));
    }

    OperationControlManager.getInstance().recieveEndIfIntegrity();

    synchronized (InterfaceIntegrityValidationManager.getInstance().getExecuteThreadHolder()) {
      InterfaceIntegrityValidationManager.getInstance().getExecuteThreadHolder()
          .remove(InterfaceIntegrityValidationManager.getInstance().getExecuteThreadHolder().indexOf(this));
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * IF Status Integrity<br>
   * Integrating IF status.
   *
   * @return IF information
   */
  private UpdateLogicalIfStatus executeIntegrity() {

    logger.trace(CommonDefinitions.START);

    UpdateLogicalIfStatus ret = new UpdateLogicalIfStatus();

    ret.setNodes(new ArrayList<NodeLogical>());
    ret.setIfs(new ArrayList<IfsLogical>());

    ret = executeNodeIntegrity(ret);

    if ((!ret.getNodes().isEmpty()) || (!ret.getIfs().isEmpty())) {

      if (ret.getNodes().isEmpty()) {
        ret.setNodes(null);
      }

      if (ret.getIfs().isEmpty()) {
        ret.setIfs(null);
      }

      logger.trace(CommonDefinitions.END);

      return ret;
    } else {
      logger.trace(CommonDefinitions.END);

      return null;
    }
  }

  /**
   * Device IF Status Integrity<br>
   * Integrating device IF.
   *
   * @param ret
   *          IF status integration result
   * @return IF status integration result
   */
  private UpdateLogicalIfStatus executeNodeIntegrity(UpdateLogicalIfStatus ret) {
    logger.trace(CommonDefinitions.START);

    SnmpController snmp = new SnmpController();

    boolean getSnmp = true;
    NodeKeySet tmpNode = null;
    try (DBAccessManager session = new DBAccessManager()) {
      for (NodeKeySet node : deviceDetail.keySet()) {
        if (getSnmp) {
          try {
            ifStateFromDevice.put(node, snmp.getIfOperStatus(node.getEquipmentsType(), node.getEquipmentsData()));
            if (NODE_STATE_MALFUNCTION == node.getEquipmentsData().getNode_state()) {
              String nodeid = node.getEquipmentsData().getNode_id();
              int nodestate = NODE_STATE_OPERATION;
              session.startTransaction();
              session.updateNodeState(nodeid, nodestate);
              session.commit();
              NodeLogical tmpstate = new NodeLogical();
              tmpstate.setNodeId(nodeid);
              tmpstate.setFailureStatus(CommonDefinitions.IF_NODE_UP_STRING);
              ret.getNodes().add(tmpstate);
            }
          } catch (DevctrlException e1) {
            logger.trace("Node Failure");
            if (NODE_STATE_OPERATION == node.getEquipmentsData().getNode_state()) {
              String nodeid = node.getEquipmentsData().getNode_id();
              int nodestate = NODE_STATE_MALFUNCTION;
              session.startTransaction();
              session.updateNodeState(nodeid, nodestate);
              session.commit();
              NodeLogical tmpstate = new NodeLogical();
              tmpstate.setNodeId(nodeid);
              tmpstate.setFailureStatus(CommonDefinitions.IF_NODE_DOWN_STRING);
              ret.getNodes().add(tmpstate);
            }
          }
        } else { 
          ifStateFromDevice.put(node, ifStateFromDevice.get(tmpNode));
        }
        if (node.getEquipmentsType().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
          getSnmp = false;
          break;
        }
      }
    } catch (DBAccessException dbe) {
      logger.debug("DB access error", dbe);

    }

    for (NodeKeySet nl : deviceDetail.keySet()) {

      String nodeId = nl.getEquipmentsData().getNode_id();

      NodeKeySet target = null;
      for (NodeKeySet key : ifStateFromDevice.keySet()) {
        if (key.getEquipmentsData().getNode_id().equals(nl.getEquipmentsData().getNode_id())) {
          target = key;

          break;
        } else {
        }
      }
      if (target != null) {
        ArrayList<SnmpIfOperStatus> ifList = ifStateFromDevice.get(target);
        for (PhysicalIfs phif : deviceDetail.get(nl).getPhysicalIfData().values()) {
          if (phif.getIf_name() == null) {
            continue;
          }
          for (SnmpIfOperStatus is : ifList) {
            if (is.getIfName() == null) {
              continue;
            }
            if (is.getIfName().equals(phif.getIf_name())) {
              String beforeState = LogicalPhysicalConverter.toStringIFState(phif.getIf_status());
              String afterState = LogicalPhysicalConverter.toStringIFState(is.getIfOperStatus());
              if (!beforeState.equals(afterState)) {
                IfsLogical tmpState = new IfsLogical();
                tmpState.setNodeId(nodeId);
                tmpState.setIfId(phif.getPhysical_if_id());
                tmpState.setIfType(CommonDefinitions.IF_TYPE_PHYSICAL_IF);
                tmpState.setStatus(afterState);
                ret.getIfs().add(tmpState);
              }
              break;
            }
          }
        }

        for (LagIfs lagif : deviceDetail.get(nl).getLagIfData().values()) {
          for (SnmpIfOperStatus is : ifList) {
            if (is.getIfName() == null) {
              continue;
            }
            if (is.getIfName().equals(lagif.getIf_name())) {
              String beforeState = LogicalPhysicalConverter.toStringIFState(lagif.getIf_status());
              String afterState = LogicalPhysicalConverter.toStringIFState(is.getIfOperStatus());
              if (!beforeState.equals(afterState)) {
                IfsLogical tmpState = new IfsLogical();
                tmpState.setNodeId(nodeId);
                tmpState.setIfId(lagif.getFc_lag_if_id());
                tmpState.setIfType(CommonDefinitions.IF_TYPE_LAG_IF);
                tmpState.setStatus(afterState);
                ret.getIfs().add(tmpState);
              }
              break;
            }
          }
        }

        for (BreakoutIfs boif : deviceDetail.get(nl).getBreakoutIfData().values()) {
          for (SnmpIfOperStatus is : ifList) {
            if (is.getIfName() == null) {
              continue;
            }
            if (is.getIfName().equals(boif.getIf_name())) {
              String beforeState = LogicalPhysicalConverter.toStringIFState(boif.getIf_status());
              String afterState = LogicalPhysicalConverter.toStringIFState(is.getIfOperStatus());
              if (!beforeState.equals(afterState)) {
                IfsLogical tmpState = new IfsLogical();
                tmpState.setNodeId(nodeId);
                tmpState.setIfId(boif.getBreakout_if_id());
                tmpState.setIfType(CommonDefinitions.IF_TYPE_BREAKOUT_IF);
                tmpState.setStatus(afterState);
                ret.getIfs().add(tmpState);
              }
              break;
            }
          }
        }

        for (VlanIfs ilif : deviceDetail.get(nl).getVlanIfData().values()) {

          for (SnmpIfOperStatus is : ifList) {
            if (is.getIfName() == null) {
              continue;
            }

            String vlanIfName = ilif.getIf_name();

            if ((nl.getEquipmentsData().getVpn_type().equals(CommonDefinitions.VPNTYPE_L3))
                && (Integer.valueOf(ilif.getVlan_id()) != 0)) {
              vlanIfName = ilif.getIf_name() + nl.getEquipmentsType().getUnit_connector() + ilif.getVlan_id();
            }

            if (is.getIfName().equals(vlanIfName)) {
              String beforeState = LogicalPhysicalConverter.toStringIFState(ilif.getIf_status());
              String afterState = LogicalPhysicalConverter.toStringIFState(is.getIfOperStatus());
              if (!beforeState.equals(afterState)) {
                IfsLogical tmpState = new IfsLogical();
                tmpState.setNodeId(nodeId);
                tmpState.setIfId(ilif.getVlan_if_id());
                tmpState.setIfType(CommonDefinitions.IF_TYPE_VLAN_IF);
                tmpState.setStatus(afterState);
                ret.getIfs().add(tmpState);
              }
              break;
            }
          }
        }
      } else {
        logger.warn(
            LogFormatter.out.format(LogFormatter.MSG_407059, deviceDetail.get(nl).getEquipmentsData().getNode_id(),
                deviceDetail.get(nl).getEquipmentsType().getRouter_type(), "none"));
      }
    }

    logger.trace(CommonDefinitions.END);

    return ret;
  }

  /**
   * Getting IF status (device origin).
   *
   * @return IF status (device origin)
   */
  protected HashMap<NodeKeySet, ArrayList<SnmpIfOperStatus>> getIfStateFromDevice() {
    return ifStateFromDevice;
  }

  /**
   * Getting device information.
   *
   * @return device information
   */
  protected HashMap<NodeKeySet, DeviceInformationSet> getDeviceDetail() {
    return deviceDetail;
  }

  @Override
  public String toString() {
    return "IntegrityExecutor [ifStateFromDevice=" + ifStateFromDevice + ", deviceDetail=" + deviceDetail + "]";
  }

}
