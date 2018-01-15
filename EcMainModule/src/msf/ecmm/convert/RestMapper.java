/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.convert;

import static msf.ecmm.common.CommonDefinitions.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.pojo.BGPOptions;
import msf.ecmm.db.pojo.BootErrorMessages;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.EquipmentIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.IfNameRules;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.LagMembers;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.StaticRouteOptions;
import msf.ecmm.db.pojo.VRRPOptions;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.emctrl.restpojo.ControllerLog;
import msf.ecmm.emctrl.restpojo.ControllerStatusFromEm;
import msf.ecmm.fcctrl.pojo.ControllerStatusToFc;
import msf.ecmm.fcctrl.pojo.Operations;
import msf.ecmm.fcctrl.pojo.UpdateLogicalIfStatus;
import msf.ecmm.fcctrl.pojo.parts.Controller;
import msf.ecmm.fcctrl.pojo.parts.IfsLogical;
import msf.ecmm.fcctrl.pojo.parts.NodeLogical;
import msf.ecmm.ope.receiver.pojo.CheckEcMainModuleStatus;
import msf.ecmm.ope.receiver.pojo.EmControllerReceiveStatus;
import msf.ecmm.ope.receiver.pojo.GetAllNodeTraffic;
import msf.ecmm.ope.receiver.pojo.GetBreakoutInterface;
import msf.ecmm.ope.receiver.pojo.GetBreakoutInterfaceList;
import msf.ecmm.ope.receiver.pojo.GetControllerLog;
import msf.ecmm.ope.receiver.pojo.GetEquipmentType;
import msf.ecmm.ope.receiver.pojo.GetEquipmentTypeList;
import msf.ecmm.ope.receiver.pojo.GetInterfaceList;
import msf.ecmm.ope.receiver.pojo.GetLagInterface;
import msf.ecmm.ope.receiver.pojo.GetLagInterfaceList;
import msf.ecmm.ope.receiver.pojo.GetNode;
import msf.ecmm.ope.receiver.pojo.GetNodeList;
import msf.ecmm.ope.receiver.pojo.GetNodeTraffic;
import msf.ecmm.ope.receiver.pojo.GetPhysicalInterface;
import msf.ecmm.ope.receiver.pojo.GetPhysicalInterfaceList;
import msf.ecmm.ope.receiver.pojo.GetVlanInterface;
import msf.ecmm.ope.receiver.pojo.GetVlanInterfaceList;
import msf.ecmm.ope.receiver.pojo.parts.BaseIf;
import msf.ecmm.ope.receiver.pojo.parts.Bgp;
import msf.ecmm.ope.receiver.pojo.parts.BreakoutIf;
import msf.ecmm.ope.receiver.pojo.parts.BreakoutIfId;
import msf.ecmm.ope.receiver.pojo.parts.Capabilities;
import msf.ecmm.ope.receiver.pojo.parts.ControllerInfo;
import msf.ecmm.ope.receiver.pojo.parts.Cpu;
import msf.ecmm.ope.receiver.pojo.parts.DeviceInfo;
import msf.ecmm.ope.receiver.pojo.parts.Disk;
import msf.ecmm.ope.receiver.pojo.parts.EcEmLog;
import msf.ecmm.ope.receiver.pojo.parts.EcStatus;
import msf.ecmm.ope.receiver.pojo.parts.EmStatus;
import msf.ecmm.ope.receiver.pojo.parts.Equipment;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentIf;
import msf.ecmm.ope.receiver.pojo.parts.IfNameRule;
import msf.ecmm.ope.receiver.pojo.parts.IfSearchIf;
import msf.ecmm.ope.receiver.pojo.parts.Informations;
import msf.ecmm.ope.receiver.pojo.parts.InterfaceInfoTraffic;
import msf.ecmm.ope.receiver.pojo.parts.LagIf;
import msf.ecmm.ope.receiver.pojo.parts.LagMember;
import msf.ecmm.ope.receiver.pojo.parts.LagMembersBreakoutIfs;
import msf.ecmm.ope.receiver.pojo.parts.LagMembersPhysicalIfs;
import msf.ecmm.ope.receiver.pojo.parts.LogConditions;
import msf.ecmm.ope.receiver.pojo.parts.LogData;
import msf.ecmm.ope.receiver.pojo.parts.LogInformation;
import msf.ecmm.ope.receiver.pojo.parts.Memory;
import msf.ecmm.ope.receiver.pojo.parts.Node;
import msf.ecmm.ope.receiver.pojo.parts.OsInfo;
import msf.ecmm.ope.receiver.pojo.parts.PhysicalIf;
import msf.ecmm.ope.receiver.pojo.parts.StaticRoute;
import msf.ecmm.ope.receiver.pojo.parts.SwitchTraffic;
import msf.ecmm.ope.receiver.pojo.parts.Traffic;
import msf.ecmm.ope.receiver.pojo.parts.TrafficValue;
import msf.ecmm.ope.receiver.pojo.parts.VlanIf;
import msf.ecmm.ope.receiver.pojo.parts.Vrrp;
import msf.ecmm.ope.receiver.pojo.parts.Ztp;
import msf.ecmm.traffic.pojo.NodeKeySet;
import msf.ecmm.traffic.pojo.TrafficData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * REST Data Mapping.
 */
public class RestMapper {

  /** Logger. */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /**
   * REST Data Mapping_Model Information Acquisition.
   *
   * @param equipmentsDb
   *          model information
   * @return model information (for sending to REST)
   */
  public static GetEquipmentType toEqInfo(Equipments equipmentsDb) {

    logger.trace(CommonDefinitions.START);
    logger.debug(equipmentsDb);

    GetEquipmentType ret = new GetEquipmentType();
    ret.setEquipment(getEquipmentInfo(equipmentsDb));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_Model List Information Acquisition.
   *
   * @param equipmentsList
   *          model list information
   * @return model list informaition (for sending to REST)
   */
  public static GetEquipmentTypeList toEquipmentList(List<Equipments> equipmentsList) {

    logger.trace(CommonDefinitions.START);
    logger.debug(equipmentsList);

    GetEquipmentTypeList ret = new GetEquipmentTypeList();
    ret.setEquipment(new ArrayList<Equipment>());

    for (Equipments equipments : equipmentsList) {
      ret.getEquipment().add(getEquipmentInfo(equipments));
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;
  }

  /**
   * REST Data Mapping_Device Information.
   *
   * @param nodesDb
   *          device information
   * @return device information (for sending to REST)
   */
  public static GetNode toNodeInfo(Nodes nodesDb) {
    logger.trace(CommonDefinitions.START);
    logger.debug(nodesDb);

    GetNode ret = new GetNode();
    ret.setNode(getNodeInfo(nodesDb));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_Device List Information Acquisition.
   *
   * @param nodesList
   *          Device List Information
   * @return Device List Information (for sending to REST)
   */
  public static GetNodeList toNodeInfoList(List<Nodes> nodesList) {
    logger.trace(CommonDefinitions.START);
    logger.debug(nodesList);

    GetNodeList ret = new GetNodeList();
    ret.setNode(new ArrayList<Node>());

    for (Nodes nodes : nodesList) {
      ret.getNode().add(getNodeInfo(nodes));
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_Physical IF Informtion Acquisition.
   *
   * @param physicalIfs
   *          physical IF information
   * @return physical IF information (for sending to REST)
   */
  public static GetPhysicalInterface toPhyInInfo(PhysicalIfs physicalIfs) {

    logger.trace(CommonDefinitions.START);
    logger.debug(physicalIfs);

    GetPhysicalInterface ret = new GetPhysicalInterface();
    ret.setPhysicalIf(getPhysicalIf(physicalIfs));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_Physical IF Information List Acquisition.
   *
   * @param physicalIfs
   *          physical IF information list
   * @return physical IF information list (for sending to REST)
   */
  public static GetPhysicalInterfaceList toPhyInInfoList(List<PhysicalIfs> physicalIfs) {

    logger.trace(CommonDefinitions.START);
    logger.debug(physicalIfs);

    GetPhysicalInterfaceList ret = new GetPhysicalInterfaceList();
    ret.setPhysicalIfs(new ArrayList<PhysicalIf>());
    for (PhysicalIfs dbPhysicalIfs : physicalIfs) {
      ret.getPhysicalIfs().add(getPhysicalIf(dbPhysicalIfs));
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_LagIF Information Acquisition.
   *
   * @param lagIfs
   *          LagIF information
   * @param breakoutIfsList
   *          breakoutIF information list
   * @return LagIF information (for sending to REST)
   */
  public static GetLagInterface toLagIfInfo(LagIfs lagIfs, List<BreakoutIfs> breakoutIfsList) {

    logger.trace(CommonDefinitions.START);
    logger.debug(lagIfs + ", " + breakoutIfsList);

    GetLagInterface ret = new GetLagInterface();
    ret.setLagIfs(getLagIf(lagIfs, breakoutIfsList));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_LagIF Information List Acquisition.
   *
   * @param lagIfs
   *          LagIF information list
   * @param breakoutIfsList
   *          breakoutIF information list
   * @return LagIF information list (for sending to REST)
   */
  public static GetLagInterfaceList toLagIfInfoList(List<LagIfs> lagIfs, List<BreakoutIfs> breakoutIfsList) {

    logger.trace(CommonDefinitions.START);
    logger.debug(lagIfs);

    GetLagInterfaceList ret = new GetLagInterfaceList();
    ret.setLagIfs(new ArrayList<LagIf>());
    for (LagIfs lag : lagIfs) {
      ret.getLagIfs().add(getLagIf(lag, breakoutIfsList));
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_BreakoutIF Information Acquisition.
   *
   * @param breakoutIfsDb
   *          BreakoutIF information
   * @return BreakoutIF information (for sending to REST)
   */
  public static GetBreakoutInterface toBreakoutIfsInfo(BreakoutIfs breakoutIfsDb) {
    logger.trace(CommonDefinitions.START);
    logger.debug(breakoutIfsDb);

    GetBreakoutInterface ret = new GetBreakoutInterface();
    ret.setBreakoutIf(getBreakoutIf(breakoutIfsDb));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_BreakoutIF Information List Acquisition.
   *
   * @param breakoutIfsList
   *          BreakoutIF information list
   * @return BreakoutIF information list (for sending to REST)
   */
  public static GetBreakoutInterfaceList toBreakoutIfsInfoList(List<BreakoutIfs> breakoutIfsList) {
    logger.trace(CommonDefinitions.START);
    logger.debug(breakoutIfsList);

    GetBreakoutInterfaceList ret = new GetBreakoutInterfaceList();
    ret.setBreakoutIfs(new ArrayList<BreakoutIf>());

    for (BreakoutIfs breakoutIfs : breakoutIfsList) {
      ret.getBreakoutIfs().add(getBreakoutIf(breakoutIfs));
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_VLAN IF Information Acquisition.
   *
   * @param vlanIfsDb
   *          VLAN IF information
   * @return VLAN IF information (for sending to REST)
   */
  public static GetVlanInterface toVlanIfsInfo(VlanIfs vlanIfsDb) {
    logger.trace(CommonDefinitions.START);
    logger.debug(vlanIfsDb);

    GetVlanInterface ret = new GetVlanInterface();
    ret.setVlanIf(getVlanIf(vlanIfsDb));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_VLANIF Information List Acquisition.
   *
   * @param vlanIfsList
   *          VALNIF information list
   * @return VLANIF information list (for sending to REST)
   */
  public static GetVlanInterfaceList toVlanIfsInfoList(List<VlanIfs> vlanIfsList) {
    logger.trace(CommonDefinitions.START);
    logger.debug(vlanIfsList);

    GetVlanInterfaceList ret = new GetVlanInterfaceList();
    ret.setVlanIf(new ArrayList<VlanIf>());

    for (VlanIfs vlanIfs : vlanIfsList) {
      ret.getVlanIfs().add(getVlanIf(vlanIfs));
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_IF Information List Acquisition.
   *
   * @param nodesDb
   *          IF information list (Null might be entered)
   * @return IF information list (for sending to REST)
   */
  public static GetInterfaceList toIFList(Nodes nodesDb) {

    logger.trace(CommonDefinitions.START);
    logger.debug(nodesDb);

    GetInterfaceList ret = new GetInterfaceList();
    ret.setIfs(new IfSearchIf());
    ret.getIfs().setPhysicalIfs(new ArrayList<PhysicalIf>());
    ret.getIfs().setLagIfs(new ArrayList<LagIf>());
    ret.getIfs().setBreakoutIf(new ArrayList<BreakoutIf>());

    if (nodesDb != null) {
      List<BreakoutIfs> breakoutIfsDbList = new ArrayList<BreakoutIfs>();
      for (PhysicalIfs physDbElem : nodesDb.getPhysicalIfsList()) {
        for (BreakoutIfs boDbElem : physDbElem.getBreakoutIfsList()) {
          breakoutIfsDbList.add(boDbElem);
        }
      }

      for (PhysicalIfs physicalIfs : nodesDb.getPhysicalIfsList()) {
        ret.getIfs().getPhysicalIfs().add(getPhysicalIf(physicalIfs));
      }

      for (LagIfs lagIfs : nodesDb.getLagIfsList()) {
        ret.getIfs().getLagIfs().add(getLagIf(lagIfs, breakoutIfsDbList));
      }

      for (BreakoutIfs breakoutIfs : breakoutIfsDbList) {
        ret.getIfs().getBreakoutIf().add(getBreakoutIf(breakoutIfs));
      }
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Model Information Acquisition.
   *
   * @param equipmentsDb
   *          model information
   * @return model information (for sending to REST)
   */
  private static Equipment getEquipmentInfo(Equipments equipmentsDb) {
    logger.trace(CommonDefinitions.START);
    logger.debug(equipmentsDb);

    Equipment ret = new Equipment();

    ret.setEquipmentTypeId(equipmentsDb.getEquipment_type_id());
    ret.setLagPrefix(equipmentsDb.getLag_prefix());
    ret.setUnitConnector(equipmentsDb.getUnit_connector());
    ret.setIfNameOid(equipmentsDb.getIf_name_oid());
    ret.setSnmptrapIfNameOid(equipmentsDb.getSnmptrap_if_name_oid());
    ret.setMaxRepetitions(equipmentsDb.getMax_repetitions());

    ret.setPlatform(equipmentsDb.getPlatform_name());
    ret.setOs(equipmentsDb.getOs_name());
    ret.setFirmware(equipmentsDb.getFirmware_version());
    if (equipmentsDb.getRouter_type() == ROUTER_TYPE_NORMAL) {
      ret.setRouterType(ROUTER_TYPE_NORMAL_STRING);
    } else if (equipmentsDb.getRouter_type() == ROUTER_TYPE_COREROUTER) {
      ret.setRouterType(ROUTER_TYPE_COREROUTER_STRING);
    }
    ret.setPhysicalIfNameSyntax(equipmentsDb.getCore_router_physical_if_name_format());
    ret.setBreakoutIfNameSyntax(equipmentsDb.getBreakout_if_name_syntax());
    ret.setBreakoutIfNameSuffixList(equipmentsDb.getBreakout_if_name_suffix_list());

    ArrayList<EquipmentIf> equipmentIfList = new ArrayList<EquipmentIf>();
    dbDataLoop : for (EquipmentIfs eifs : equipmentsDb.getEquipmentIfsList()) {
      EquipmentIf equipmentIf = new EquipmentIf();
      equipmentIf.setPhysicalIfId(eifs.getPhysical_if_id());
      equipmentIf.setIfSlot(eifs.getIf_slot());
      equipmentIf.setPortSpeedType(new ArrayList<String>());
      equipmentIf.getPortSpeedType().add(eifs.getPort_speed());

      for (EquipmentIf elem : equipmentIfList) {
        if (elem.getPhysicalIfId().equals(eifs.getPhysical_if_id()) && elem.getIfSlot().equals(eifs.getIf_slot())) {
          elem.getPortSpeedType().add(eifs.getPort_speed());
          continue dbDataLoop;
        }
      }

      equipmentIfList.add(equipmentIf);
    }
    ret.setEquipmentIfs(equipmentIfList);

    ret.setIfNameRules(new ArrayList<IfNameRule>());
    for (IfNameRules inrs : equipmentsDb.getIfNameRulesList()) {
      IfNameRule ifNameRule = new IfNameRule();
      ifNameRule.setSpeed(inrs.getSpeed());
      ifNameRule.setPortPrefix(inrs.getPort_prefix());
      ret.getIfNameRules().add(ifNameRule);
    }

    ret.setZtp(new Ztp());
    ret.getZtp().setDhcpTemplate(equipmentsDb.getDhcp_template());
    ret.getZtp().setConfigTemplate(equipmentsDb.getConfig_template());
    ret.getZtp().setInitialConfig(equipmentsDb.getInitial_config());
    ret.getZtp().setBootCompleteMsg(equipmentsDb.getBoot_complete_msg());
    ArrayList<String> bootErrorMsgsList = new ArrayList<String>();
    for (BootErrorMessages bems : equipmentsDb.getBootErrorMessagesList()) {
      bootErrorMsgsList.add(bems.getBoot_error_msgs());
    }
    ret.getZtp().setBootErrorMsgs(bootErrorMsgsList);

    ret.setCapabilities(new Capabilities());
    ret.getCapabilities().setEvpn(equipmentsDb.getEvpn_capability());
    ret.getCapabilities().setL2vpn(equipmentsDb.getL2vpn_capability());
    ret.getCapabilities().setL3vpn(equipmentsDb.getL3vpn_capability());

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Device Information Acquisition.
   *
   * @param nodesDb
   *          device information
   * @return device information (for sending to REST)
   */
  private static Node getNodeInfo(Nodes nodesDb) {
    logger.trace(CommonDefinitions.START);
    logger.debug(nodesDb);

    Node ret = new Node();

    ret.setNodeId(nodesDb.getNode_id());
    ret.setEquipmentTypeId(nodesDb.getEquipment_type_id());
    if (nodesDb.getNode_state() == NODE_STATE_IN_SERVICE) {
      ret.setNodeState(NODE_STATE_IN_SERVICE_STRING);
    }
    if (nodesDb.getNode_state() == NODE_STATE_BEFORE_SETTING) {
      ret.setNodeState(NODE_STATE_BEFORE_SETTING_STRING);
    }
    if (nodesDb.getNode_state() == NODE_STATE_ZTP_COMPLETE) {
      ret.setNodeState(NODE_STATE_ZTP_COMPLETE_STRING);
    }
    if (nodesDb.getNode_state() == NODE_STATE_NODE_RESETTING_COMPLETE) {
      ret.setNodeState(NODE_STATE_NODE_RESETTING_COMPLETE_STRING);
    }
    if (nodesDb.getNode_state() == NODE_STATE_FAILER_SETTING) {
      ret.setNodeState(NODE_STATE_FAILER_SETTING_STRING);
    }
    if (nodesDb.getNode_state() == NODE_STATE_FAILER_NODE_RESETTING) {
      ret.setNodeState(NODE_STATE_FAILER_NODE_RESETTING_STRING);
    }
    if (nodesDb.getNode_state() == NODE_STATE_FAILER_SERVICE_SETTING) {
      ret.setNodeState(NODE_STATE_FAILER_SERVICE_SETTING_STRING);
    }
    if (nodesDb.getNode_state() == NODE_STATE_FAILER_OTHER) {
      ret.setNodeState(NODE_STATE_FAILER_OTHER_STRING);
    }
    ret.setManagementIfAddress(nodesDb.getManagement_if_address());
    ret.setLoopbackIfAddress(nodesDb.getLoopback_if_address());
    ret.setSnmpCommunity(nodesDb.getSnmp_community());
    ret.setHostName(nodesDb.getHost_name());
    ret.setMacAddr(nodesDb.getMac_addr());
    ret.setNtpServerAddress(nodesDb.getNtp_server_address());
    ret.setVpnType(nodesDb.getVpn_type());
    ret.setUsername(nodesDb.getUsername());
    ret.setPassword(nodesDb.getPassword());
    ret.setProvisioning(nodesDb.getProvisioning());
    if (nodesDb.getPlane() != null && (nodesDb.getPlane() == NODE_PLANE_A || nodesDb.getPlane() == NODE_PLANE_B)) {
      ret.setPlane(Integer.toString(nodesDb.getPlane()));
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Physical IF Information Acquisition.
   *
   * @param physicalIfsDb
   *          physical IF information
   * @return physical IF information (for sending to REST)
   */
  private static PhysicalIf getPhysicalIf(PhysicalIfs physicalIfsDb) {
    logger.trace(CommonDefinitions.START);
    logger.debug(physicalIfsDb);

    PhysicalIf ret = new PhysicalIf();
    ret.setPhysicalIfId(physicalIfsDb.getPhysical_if_id());
    ret.setIfName(physicalIfsDb.getIf_name());

    ret.setBreakoutIf(new ArrayList<BreakoutIfId>());
    for (BreakoutIfs breakoutIfs : physicalIfsDb.getBreakoutIfsList()) {
      BreakoutIfId ifId = new BreakoutIfId();
      ifId.setBreakoutIfId(breakoutIfs.getBreakout_if_id());
      ret.getBreakoutIf().add(ifId);
    }

    String ifState = LogicalPhysicalConverter.toStringIFState(physicalIfsDb.getIf_status());
    if (!ifState.equals(CommonDefinitions.IF_STATE_UNKNOWN_STRING)) {
      ret.setIfState(ifState);
    } else {
      ret.setIfState(CommonDefinitions.IF_STATE_NG_STRING);
    }
    ret.setLinkSpeed(physicalIfsDb.getIf_speed());
    ret.setIpv4Address(physicalIfsDb.getIpv4_address());
    if (physicalIfsDb.getIpv4_prefix() != null && physicalIfsDb.getIpv4_prefix() != 0) {
      ret.setIpv4Prefix(String.valueOf(physicalIfsDb.getIpv4_prefix()));
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * LagIF Information Acquisition.
   *
   * @param lagIfs
   *          LagIF information
   * @param breakoutIfsList
   *          breakoutIF information list
   * @return LagIF informatio (for sending to REST)
   */
  private static LagIf getLagIf(LagIfs lagIfs, List<BreakoutIfs> breakoutIfsList) {

    logger.trace(CommonDefinitions.START);
    logger.debug(lagIfs + ", " + breakoutIfsList);

    LagIf ret = new LagIf();
    ret.setLagIfId(lagIfs.getLag_if_id());
    ret.setIfName(lagIfs.getIf_name());
    ret.setLagMember(new LagMember());
    ret.getLagMember().setPhysicalIfs(new ArrayList<LagMembersPhysicalIfs>());
    ret.getLagMember().setBreakoutIfs(new ArrayList<LagMembersBreakoutIfs>());
    String ifState = LogicalPhysicalConverter.toStringIFState(lagIfs.getIf_status());
    if (!ifState.equals(CommonDefinitions.IF_STATE_UNKNOWN_STRING)) {
      ret.setIfState(ifState);
    } else {
      ret.setIfState(CommonDefinitions.IF_STATE_NG_STRING);
    }
    ret.setLinkSpeed(lagIfs.getIf_speed());
    ret.setIpv4Address(lagIfs.getIpv4_address());
    if (lagIfs.getIpv4_prefix() != null && lagIfs.getIpv4_prefix() != 0) {
      ret.setIpv4Prefix(Integer.toString(lagIfs.getIpv4_prefix()));
    }

    for (LagMembers member : lagIfs.getLagMembersList()) {
      if (member.getPhysical_if_id() != null) {
        LagMembersPhysicalIfs physIf = new LagMembersPhysicalIfs();
        physIf.setPhysicalIfId(member.getPhysical_if_id());
        ret.getLagMember().getPhysicalIfs().add(physIf);
      } else if (member.getBreakout_if_id() != null) {
        LagMembersBreakoutIfs boIf = new LagMembersBreakoutIfs();
        for (BreakoutIfs breakoutIfs : breakoutIfsList) {
          if (breakoutIfs.getBreakout_if_id().equals(member.getBreakout_if_id())) {
            boIf.setPhysicalIfId(breakoutIfs.getPhysical_if_id());
            break;
          }
        }
        boIf.setBreakoutIfId(member.getBreakout_if_id());
        ret.getLagMember().getBreakoutIfs().add(boIf);
      }
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * breakoutIF Information Acquisition.
   *
   * @param breakoutIfs
   *          breakoutIF information
   * @return breakoutIF information (for sending to REST)
   */
  private static BreakoutIf getBreakoutIf(BreakoutIfs breakoutIfsDb) {
    logger.trace(CommonDefinitions.START);
    logger.debug(breakoutIfsDb);

    BreakoutIf ret = new BreakoutIf();

    ret.setPhysicalIfId(breakoutIfsDb.getPhysical_if_id());
    ret.setBreakoutIfId(breakoutIfsDb.getBreakout_if_id());
    ret.setIfName(breakoutIfsDb.getIf_name());

    String ifState = LogicalPhysicalConverter.toStringIFState(breakoutIfsDb.getIf_status());
    if (!ifState.equals(CommonDefinitions.IF_STATE_UNKNOWN_STRING)) {
      ret.setIfState(ifState);
    } else {
      ret.setIfState(CommonDefinitions.IF_STATE_NG_STRING);
    }
    ret.setLinkSpeed(breakoutIfsDb.getSpeed());
    ret.setIpv4Address(breakoutIfsDb.getIpv4_address());
    if (breakoutIfsDb.getIpv4_prefix() != null && breakoutIfsDb.getIpv4_prefix() != 0) {
      ret.setIpv4Prefix(String.valueOf(breakoutIfsDb.getIpv4_prefix()));
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * VLAN IF Information Acquisition.
   *
   * @param vlanIfsDb
   *          VLAN IF information
   * @return VLAN IF information (for sending to REST)
   */
  private static VlanIf getVlanIf(VlanIfs vlanIfsDb) {
    logger.trace(CommonDefinitions.START);
    logger.debug(vlanIfsDb);

    VlanIf ret = new VlanIf();

    ret.setVlanIfId(vlanIfsDb.getVlan_if_id());
    if (vlanIfsDb.getVlan_id() != null && !vlanIfsDb.getVlan_id().equals("0")) {
      ret.setVlanId(vlanIfsDb.getVlan_id());
    } else {
      ret.setVlanId(null);
    }
    ret.setIfName(vlanIfsDb.getIf_name());
    ret.setIfState(LogicalPhysicalConverter.toStringIFState(vlanIfsDb.getIf_status()));

    ret.setBaseIf(new BaseIf());
    if (vlanIfsDb.getPhysical_if_id() != null) {
      ret.getBaseIf().setIfType(IF_TYPE_PHYSICAL_IF);
      ret.getBaseIf().setIfId(vlanIfsDb.getPhysical_if_id());
    }
    if (vlanIfsDb.getLag_if_id() != null) {
      ret.getBaseIf().setIfType(IF_TYPE_LAG_IF);
      ret.getBaseIf().setIfId(vlanIfsDb.getLag_if_id());
    }
    if (vlanIfsDb.getBreakout_if_id() != null) {
      ret.getBaseIf().setIfType(IF_TYPE_BREAKOUT_IF);
      ret.getBaseIf().setIfId(vlanIfsDb.getBreakout_if_id());
    }

    ret.setMtu(vlanIfsDb.getMtu());
    ret.setIpv4Address(vlanIfsDb.getIpv4_address());
    ret.setIpv6Address(vlanIfsDb.getIpv6_address());
    if (vlanIfsDb.getIpv4_address() != null) {
      ret.setIpv4Prefix(String.valueOf(vlanIfsDb.getIpv4_prefix()));
    }
    if (vlanIfsDb.getIpv6_address() != null) {
      ret.setIpv6Prefix(String.valueOf(vlanIfsDb.getIpv6_prefix()));
    }
    if (vlanIfsDb.getPort_mode() != null) {
      if (vlanIfsDb.getPort_mode().intValue() == VLAN_PORTMODE_ACCESS) {
        ret.setPortMode(VLAN_PORTMODE_ACCESS_STRING);
      }
      if (vlanIfsDb.getPort_mode().intValue() == VLAN_PORTMODE_TRUNK) {
        ret.setPortMode(VLAN_PORTMODE_TRUNK_STRING);
      }
    }

    if (vlanIfsDb.getBgpOptionsList().isEmpty()) {
      ret.setBgp(null);
    } else {
      ret.setBgp(new Bgp());
      for (BGPOptions bp : vlanIfsDb.getBgpOptionsList()) {
        if (bp.getBgp_role() == BGP_ROLE_MASTER) {
          ret.getBgp().setRole(BGP_ROLE_MASTER_STRING);
        }
        if (bp.getBgp_role() == BGP_ROLE_SLAVE) {
          ret.getBgp().setRole(BGP_ROLE_SLAVE_STRING);
        }
        ret.getBgp().setNeighborAs(bp.getBgp_neighbor_as());
        ret.getBgp().setNeighborIpv4Address(bp.getBgp_neighbor_ipv4_address());
        ret.getBgp().setNeighborIpv6Address(bp.getBgp_neighbor_ipv6_address());
      }
    }

    if (vlanIfsDb.getStaticRouteOptionsList().isEmpty()) {
      ret.setStaticRoutes(null);
    } else {
      ret.setStaticRoutes(new ArrayList<StaticRoute>());
      for (StaticRouteOptions srp : vlanIfsDb.getStaticRouteOptionsList()) {
        StaticRoute staticRoute = new StaticRoute();
        if (srp.getStatic_route_address_type() == STATIC_ROUTEADDRESS_TYPE_IPV4) {
          staticRoute.setAddressType(STATIC_ROUTEADDRESS_TYPE_IPV4_STRING);
        } else if (srp.getStatic_route_address_type() == STATIC_ROUTEADDRESS_TYPE_IPV6) {
          staticRoute.setAddressType(STATIC_ROUTEADDRESS_TYPE_IPV6_STRING);
        }
        staticRoute.setAddress(srp.getStatic_route_destination_address());
        staticRoute.setPrefix(srp.getStatic_route_prefix());
        staticRoute.setNextHop(String.valueOf(srp.getStatic_route_nexthop_address()));
        ret.getStaticRoutes().add(staticRoute);
      }
    }

    if (vlanIfsDb.getVrrpOptionsList().isEmpty()) {
      ret.setVrrp(null);
    } else {
      ret.setVrrp(new Vrrp());
      for (VRRPOptions vrrpOpt : vlanIfsDb.getVrrpOptionsList()) {
        ret.getVrrp().setGroupId(vrrpOpt.getVrrp_group_id());
        if (vrrpOpt.getVrrp_role() == VRRP_ROLE_MASTER) {
          ret.getVrrp().setRole(VRRP_ROLE_MASTER_STRING);
        }
        if (vrrpOpt.getVrrp_role() == VRRP_ROLE_SLAVE) {
          ret.getVrrp().setRole(VRRP_ROLE_SLAVE_STRING);
        }
        ret.getVrrp().setVirtualIpv4Address(vrrpOpt.getVrrp_virtual_ipv4_address());
        ret.getVrrp().setVirtualIpv6Address(vrrpOpt.getVrrp_virtual_ipv6_address());
      }
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;

  }

  /**
   * REST Data Mapping_Receiving SNMPTrap Notification.
   *
   * @param nodeId
   *          device ID
   * @param ifType
   *          IF type
   * @param ifId
   *          IF ID
   * @param vlanIfsList
   *          VLAN IF information
   * @param status
   *          IF status after change
   * @param mibFailedFlag
   *          MIB access failure flag to the device
   * @return data for sending REST request
   */
  public static Operations toSnmpTrapNotificationInfo(String nodeId, String ifType, String ifId,
      List<VlanIfs> vlanIfsList, String status, boolean mibFailedFlag) {

    logger.trace(CommonDefinitions.START);
    logger.debug("nodeId=" + nodeId + ", ifType=" + ifType + ", ifId=" + ifId + ", " + vlanIfsList + ", status="
        + status + "mibFailedFlag=" + mibFailedFlag);

    Operations ret = new Operations();
    ret.setUpdateLogicalIfStatusOption(new UpdateLogicalIfStatus());
    ret.setAction("update_logical_if_status");

    ret.getUpdateLogicalIfStatusOption().setNodes(new ArrayList<NodeLogical>());
    ret.getUpdateLogicalIfStatusOption().getNodes().add(new NodeLogical());
    ret.getUpdateLogicalIfStatusOption().getNodes().get(0).setNodeId(nodeId);
    if (mibFailedFlag == true) {
      ret.getUpdateLogicalIfStatusOption().getNodes().get(0).setFailureStatus(CommonDefinitions.IF_STATE_NG_STRING);
    } else {
      ret.getUpdateLogicalIfStatusOption().getNodes().get(0).setFailureStatus(CommonDefinitions.IF_STATE_OK_STRING);
    }

    ret.getUpdateLogicalIfStatusOption().setIfs(new ArrayList<IfsLogical>());
    IfsLogical ifsLogical = new IfsLogical();
    if (ifType != null) {
      ifsLogical.setNodeId(nodeId);
      ifsLogical.setIfType(ifType);
      ifsLogical.setIfId(ifId);
      ifsLogical.setStatus(status);
      ret.getUpdateLogicalIfStatusOption().getIfs().add(ifsLogical);
    }

    for (VlanIfs vlanIfs : vlanIfsList) {
      ifsLogical = new IfsLogical();
      ifsLogical.setNodeId(nodeId);
      ifsLogical.setIfType(IF_TYPE_VLAN_IF);
      ifsLogical.setIfId(vlanIfs.getVlan_if_id());
      ifsLogical.setStatus(status);
      ret.getUpdateLogicalIfStatusOption().getIfs().add(ifsLogical);
    }

    return ret;

  }

  /**
   * REST Data Mapping_Traffic Information Acquisition.
   *
   * @param nodeKey
   *          device information list
   * @param vlanIfsTable
   *          list of VLAN IF information list
   * @param trafficData
   *          traffic information
   * @param time
   *          traffic data collection time
   * @param gatheringCycle
   *          traffic data collection cycle
   * @param breakoutIfsDbList
   *          breakoutIF list (DB)
   * @return traffic information (for response)
   */
  public static GetNodeTraffic toNodeTrafficData(NodeKeySet nodeKey, List<List<VlanIfs>> vlanIfsTable,
      HashMap<NodeKeySet, ArrayList<TrafficData>> trafficData, Timestamp time, int gatheringCycle, List<BreakoutIfs> breakoutIfsDbList) {

    logger.trace(CommonDefinitions.START);
    logger.debug("nodeList=" + nodeKey + ", trafficData=" + trafficData + ", time=" + time + ", gatheringCycle="
            + gatheringCycle);
    GetNodeTraffic ret = new GetNodeTraffic();
    ret.setIsSuccess(true);
    SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_HHmmss");
    ret.setTime(date.format(time.getTime()));
    ret.setInterval(gatheringCycle * 60);

    NodeKeySet key = nodeKey;
    SwitchTraffic tmpNode = new SwitchTraffic();
    tmpNode.setNodeId(key.getEquipmentsData().getNode_id());
    TrafficValue tmpTrfValue = null;
    TrafficValue tmpTrfValueVlan = null;

    if (trafficData.get(key) != null) {
      for (TrafficData td : trafficData.get(key)) {
        tmpTrfValue = new TrafficValue();
        tmpTrfValueVlan = new TrafficValue();
        boolean nextCheckFlug = false;

        for (PhysicalIfs ifs : key.getEquipmentsData().getPhysicalIfsList()) {
          if ((td.getIfname() != null) && td.getIfname().equals(ifs.getIf_name())) {
            tmpTrfValue.setIfType(CommonDefinitions.IF_TYPE_PHYSICAL_IF);
            tmpTrfValue.setIfId(ifs.getPhysical_if_id());
            tmpTrfValue.setReceiveRate(td.getIfHclnOctets());
            tmpTrfValue.setSendRate(td.getIfHcOutOctets());
            nextCheckFlug = true;
            break;
          }
        }

        if(!nextCheckFlug){
          for (LagIfs ifs : key.getEquipmentsData().getLagIfsList()) {
            if ((td.getIfname() != null) && td.getIfname().equals(ifs.getIf_name())) {
              tmpTrfValue.setIfType(CommonDefinitions.IF_TYPE_LAG_IF);
              tmpTrfValue.setIfId(ifs.getLag_if_id());
              tmpTrfValue.setReceiveRate(td.getIfHclnOctets());
              tmpTrfValue.setSendRate(td.getIfHcOutOctets());
              nextCheckFlug = true;
              break;
            }
          }
        }

        if(!nextCheckFlug){
          for (BreakoutIfs boIfs : breakoutIfsDbList) {
            if ((td.getIfname() != null) && td.getIfname().equals(boIfs.getIf_name())) {
              tmpTrfValue.setIfType(CommonDefinitions.IF_TYPE_BREAKOUT_IF);
              tmpTrfValue.setIfId(boIfs.getBreakout_if_id());
              tmpTrfValue.setReceiveRate(td.getIfHclnOctets());
              tmpTrfValue.setSendRate(td.getIfHcOutOctets());
              nextCheckFlug = true;
              break;
            }
          }
        }

        if(nextCheckFlug){
          tmpNode.getTrafficValue().add(tmpTrfValue);
          nextCheckFlug = false;
        }

        for (List<VlanIfs> vlanIfsList : vlanIfsTable) {
          for (VlanIfs vlanIfs : vlanIfsList) {
            if (!key.getEquipmentsData().getNode_id().equals(vlanIfs.getNode_id())) {
              continue;
            }
            String vpnType = key.getEquipmentsData().getVpn_type();
            String ifNameTmp = "";
            if (vpnType == null) {
              ifNameTmp = vlanIfs.getIf_name();
            } else {
              if (vpnType.equals(CommonDefinitions.VPNTYPE_L3) && !vlanIfs.getVlan_id().equals("0")) {
                ifNameTmp = vlanIfs.getIf_name() + key.getEquipmentsType().getUnit_connector() + vlanIfs.getVlan_id();
              } else {
                ifNameTmp = vlanIfs.getIf_name();
              }
            }

            if (td.getIfname().equals(ifNameTmp)) {
              for (PhysicalIfs ifs : key.getEquipmentsData().getPhysicalIfsList()) {
                if ((vlanIfs.getPhysical_if_id() != null)
                    && (vlanIfs.getPhysical_if_id().equals(ifs.getPhysical_if_id()))) {
                  tmpTrfValueVlan.setIfType(CommonDefinitions.IF_TYPE_VLAN_IF);
                  tmpTrfValueVlan.setIfId(vlanIfs.getVlan_if_id());
                  nextCheckFlug = true;
                  break;
                }
              }
              if (!nextCheckFlug) {
                for (LagIfs ifs : key.getEquipmentsData().getLagIfsList()) {
                  if ((vlanIfs.getLag_if_id() != null) && (vlanIfs.getLag_if_id().equals(ifs.getLag_if_id()))) {
                    tmpTrfValueVlan.setIfType(CommonDefinitions.IF_TYPE_VLAN_IF);
                    tmpTrfValueVlan.setIfId(vlanIfs.getVlan_if_id());
                    nextCheckFlug = true;
                    break;
                  }
                }
              }
              if (!nextCheckFlug) {
                for (PhysicalIfs physIfs : key.getEquipmentsData().getPhysicalIfsList()) {
                  for (BreakoutIfs boIfs : physIfs.getBreakoutIfsList()) {
                    if ((vlanIfs.getBreakout_if_id() != null)
                        && (vlanIfs.getBreakout_if_id().equals(boIfs.getBreakout_if_id()))) {
                      tmpTrfValueVlan.setIfType(CommonDefinitions.IF_TYPE_VLAN_IF);
                      tmpTrfValueVlan.setIfId(vlanIfs.getVlan_if_id());
                      nextCheckFlug = true;
                      break;
                    }
                  }
                  if (nextCheckFlug) {
                    break;
                  }
                }
              }

              tmpTrfValueVlan.setReceiveRate(td.getIfHclnOctets());
              tmpTrfValueVlan.setSendRate(td.getIfHcOutOctets());
              break;
            }
          }
          if (nextCheckFlug) {
            break;
          }
        }
        if(nextCheckFlug){
          tmpNode.getTrafficValue().add(tmpTrfValueVlan);
        }
      }
    }

    if (!(tmpNode.getTrafficValue() == null)) {
      ret.setSwitchTraffic(tmpNode);
    }

    logger.trace(CommonDefinitions.END);

    logger.debug("ret=" + ret);

    return ret;
  }

  /**
   * REST Data Mapping_Traffic Information List Acquisition.
   *
   * @param nodeList
   *          device information list
   * @param vlanIfsTable
   *          list of VLAN IF information list
   * @param trafficData
   *          traffic information
   * @param time
   *          traffic data collection time
   * @param gatheringCycle
   *          traffic data collection cycle
   * @param breakoutIfsDbList
   *           breakoutIF list (DB)
   * @return traffic information (for response)
   */
  public static GetAllNodeTraffic toNodeTrafficDataList(ArrayList<NodeKeySet> nodeList, List<List<VlanIfs>> vlanIfsTable,
      HashMap<NodeKeySet, ArrayList<TrafficData>> trafficData, Timestamp time, int gatheringCycle, List<BreakoutIfs> breakoutIfsDbList) {

    logger.trace(CommonDefinitions.START);
    logger.debug("nodeList=" + nodeList + ", trafficData=" + trafficData + ", time=" + time + ", gatheringCycle="
        + gatheringCycle);

    GetAllNodeTraffic ret = new GetAllNodeTraffic();

    ret.setIsSuccess(true);
    SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_HHmmss");
    ret.setTime(date.format(time.getTime()));
    ret.setInterval(gatheringCycle * 60);
    ret.setSwitchTraffics(new ArrayList<SwitchTraffic>());

    for (NodeKeySet key : nodeList) {
      SwitchTraffic tmpNode = new SwitchTraffic();
      tmpNode.setNodeId(key.getEquipmentsData().getNode_id());
      TrafficValue tmpTrfValue = null;
      TrafficValue tmpTrfValueVlan = null;

      if (trafficData.get(key) != null) {
        for (TrafficData td : trafficData.get(key)) {
          tmpTrfValue = new TrafficValue();
          tmpTrfValueVlan = new TrafficValue();
          boolean nextCheckFlug = false;

          for (PhysicalIfs ifs : key.getEquipmentsData().getPhysicalIfsList()) {
            if ((td.getIfname() != null) && td.getIfname().equals(ifs.getIf_name())) {
              tmpTrfValue.setIfType(CommonDefinitions.IF_TYPE_PHYSICAL_IF);
              tmpTrfValue.setIfId(ifs.getPhysical_if_id());
              tmpTrfValue.setReceiveRate(td.getIfHclnOctets());
              tmpTrfValue.setSendRate(td.getIfHcOutOctets());
              nextCheckFlug = true;
              break;
            }
          }

          if(!nextCheckFlug){
            for (LagIfs ifs : key.getEquipmentsData().getLagIfsList()) {
              if ((td.getIfname() != null) && td.getIfname().equals(ifs.getIf_name())) {
                tmpTrfValue.setIfType(CommonDefinitions.IF_TYPE_LAG_IF);
                tmpTrfValue.setIfId(ifs.getLag_if_id());
                tmpTrfValue.setReceiveRate(td.getIfHclnOctets());
                tmpTrfValue.setSendRate(td.getIfHcOutOctets());
                nextCheckFlug = true;
                break;
              }
            }
          }

          if(!nextCheckFlug){
            for (BreakoutIfs boIfs : breakoutIfsDbList) {
              if ((td.getIfname() != null) && td.getIfname().equals(boIfs.getIf_name())) {
                tmpTrfValue.setIfType(CommonDefinitions.IF_TYPE_BREAKOUT_IF);
                tmpTrfValue.setIfId(boIfs.getBreakout_if_id());
                tmpTrfValue.setReceiveRate(td.getIfHclnOctets());
                tmpTrfValue.setSendRate(td.getIfHcOutOctets());
                nextCheckFlug = true;
                break;
              }
            }
          }

          if(nextCheckFlug){
            tmpNode.getTrafficValue().add(tmpTrfValue);
            nextCheckFlug = false;
          }

          for (List<VlanIfs> vlanIfsList : vlanIfsTable) {
            for (VlanIfs vlanIfs : vlanIfsList) {
              if (!key.getEquipmentsData().getNode_id().equals(vlanIfs.getNode_id())) {
                continue;
              }
              String vpnType = key.getEquipmentsData().getVpn_type();
              String ifNameTmp = "";
              if (vpnType == null) {
                ifNameTmp = vlanIfs.getIf_name();
              } else {
                if (vpnType.equals(CommonDefinitions.VPNTYPE_L3) && !vlanIfs.getVlan_id().equals("0")) {
                  ifNameTmp = vlanIfs.getIf_name() + key.getEquipmentsType().getUnit_connector() + vlanIfs.getVlan_id();
                } else {
                  ifNameTmp = vlanIfs.getIf_name();
                }
              }

              if (td.getIfname().equals(ifNameTmp)) {
                for (PhysicalIfs ifs : key.getEquipmentsData().getPhysicalIfsList()) {
                  if ((vlanIfs.getPhysical_if_id() != null)
                      && (vlanIfs.getPhysical_if_id().equals(ifs.getPhysical_if_id()))) {
                    tmpTrfValueVlan.setIfType(CommonDefinitions.IF_TYPE_VLAN_IF);
                    tmpTrfValueVlan.setIfId(vlanIfs.getVlan_if_id());
                    nextCheckFlug = true;
                    break;
                  }
                }
                if (!nextCheckFlug) {
                  for (LagIfs ifs : key.getEquipmentsData().getLagIfsList()) {
                    if ((vlanIfs.getLag_if_id() != null) && (vlanIfs.getLag_if_id().equals(ifs.getLag_if_id()))) {
                      tmpTrfValueVlan.setIfType(CommonDefinitions.IF_TYPE_VLAN_IF);
                      tmpTrfValueVlan.setIfId(vlanIfs.getVlan_if_id());
                      nextCheckFlug = true;
                      break;
                    }
                  }
                }
                if (!nextCheckFlug) {
                  for (PhysicalIfs physIfs : key.getEquipmentsData().getPhysicalIfsList()) {
                    for (BreakoutIfs boIfs : physIfs.getBreakoutIfsList()) {
                      if ((vlanIfs.getBreakout_if_id() != null)
                          && (vlanIfs.getBreakout_if_id().equals(boIfs.getBreakout_if_id()))) {
                        tmpTrfValueVlan.setIfType(CommonDefinitions.IF_TYPE_VLAN_IF);
                        tmpTrfValueVlan.setIfId(vlanIfs.getVlan_if_id());
                        nextCheckFlug = true;
                        break;
                      }
                    }
                    if (nextCheckFlug) {
                      break;
                    }
                  }
                }

                tmpTrfValueVlan.setReceiveRate(td.getIfHclnOctets());
                tmpTrfValueVlan.setSendRate(td.getIfHcOutOctets());
                break;
              }
            }
            if (nextCheckFlug) {
              break;
            }
          }
          if(nextCheckFlug){
            tmpNode.getTrafficValue().add(tmpTrfValueVlan);
          }
        }
      }

      if (!(tmpNode.getTrafficValue() == null)) {
        ret.getSwitchTraffics().add(tmpNode);
      }
    }

    logger.trace(CommonDefinitions.END);

    logger.debug("ret=" + ret);

    return ret;
  }

  /**
   * REST Data Mapping_Controller Log Acquisition.
   *
   * @param limitNumber
   *          the upper limit of the number of acqired logs (the number of lines)
   * @param endDate
   *          the end date of log collection period
   * @param startDate
   *          the beginning date of log collection period
   * @param ecControllerLog
   *          EC controller log information
   * @param emControllerLog
   *          EM controller log information
   * @return controller log information (for sending to REST)
   */
  public static GetControllerLog toControllerLog(String startDate, String endDate, int limitNumber,
      ControllerLog ecControllerLog, ControllerLog emControllerLog) {

    logger.trace(CommonDefinitions.START);

    GetControllerLog ret = new GetControllerLog();
    ret.setEcem_log(new EcEmLog());

    ret.getEcemLog().setConditions(new LogConditions());
    ret.getEcemLog().getConditions().setStart_date(startDate);
    ret.getEcemLog().getConditions().setEnd_date(endDate);
    ret.getEcemLog().getConditions().setLimit_number(limitNumber);
    ret.getEcemLog().setEc_log(new LogInformation());
    if (ecControllerLog != null && ecControllerLog.getEm_log() != null) {
      ArrayList<LogData> eclogdatalist = new ArrayList<LogData>();
      ret.getEcemLog().getEc_log().setDataNumber(ecControllerLog.getEm_log().getData_number());
      ret.getEcemLog().getEc_log().setOverLimitNumber(ecControllerLog.getEm_log().isOver_limit_number());
      ret.getEcemLog().getEc_log().setServerName(ecControllerLog.getEm_log().getServer_name());
      for (msf.ecmm.emctrl.restpojo.parts.LogData logdata : ecControllerLog.getEm_log().getLog_data()) {
        LogData log = new LogData();
        log.setMessage(logdata.getMessage());
        eclogdatalist.add(log);
      }
      ret.getEcemLog().getEc_log().setLog_data(eclogdatalist);
    }

    ret.getEcemLog().setEm_log(new LogInformation());
    if (emControllerLog != null && emControllerLog.getEm_log() != null) {
      ArrayList<LogData> emlogdatalist = new ArrayList<LogData>();
      ret.getEcemLog().getEm_log().setDataNumber(emControllerLog.getEm_log().getData_number());
      ret.getEcemLog().getEm_log().setOverLimitNumber(emControllerLog.getEm_log().isOver_limit_number());
      ret.getEcemLog().getEm_log().setServerName(emControllerLog.getEm_log().getServer_name());
      for (msf.ecmm.emctrl.restpojo.parts.LogData logdata : emControllerLog.getEm_log().getLog_data()) {
        LogData log = new LogData();
        log.setMessage(logdata.getMessage());
        emlogdatalist.add(log);
      }
      ret.getEcemLog().getEm_log().setLog_data(emlogdatalist);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_Controller Status Notification.
   *
   * @param emcontrollerstatus
   *          EM controller status notification information
   * @return controller status notification information (for sending to REST)
   */
  public static ControllerStatusToFc toControllerStatusSend(EmControllerReceiveStatus emcontrollerstatus) {

    logger.trace(CommonDefinitions.START);

    ControllerStatusToFc ret = new ControllerStatusToFc();
    Controller controller = new Controller();
    controller.setController_type(emcontrollerstatus.getController().getController_type());
    controller.setEvent(emcontrollerstatus.getController().getEvent());
    ret.setController(controller);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Controller Status Acquisition REST Response Acquisition.
   *
   * @param getinfo
   *          acquisition parameter
   * @param ecMainStateToLabel
   *          service start-up status
   * @param serverAddress
   *          physical server address
   * @param ecMainObstraction
   *          block status
   * @param receiveCount
   *          the number of receiving REST
   * @param sendCount
   *          the number of sending REST
   * @param stdList
   *          command execution result
   * @param emControllerInformations
   *          EM response
   * @return controller status acquisition REST response
   */
  public static CheckEcMainModuleStatus toControllerStatus(String getinfo, String ecMainStateToLabel,
      String serverAddress, boolean ecMainObstraction, int receiveCount, int sendCount, List<String> stdList,
      ControllerStatusFromEm emControllerInformations) {
    logger.trace(CommonDefinitions.START);

    CheckEcMainModuleStatus ret = new CheckEcMainModuleStatus();
    ArrayList<Informations> infolist = new ArrayList<Informations>();
    if (ecMainStateToLabel.length() != 0) {

      boolean AllSetFlg = false;

      EcStatus ecStatus = new EcStatus();
      ecStatus.setStatus(ecMainStateToLabel);
      if (ecMainObstraction == true) {
        ecStatus.setBusy(EC_BUSY_STRING);
      } else {
        ecStatus.setBusy(EC_IN_SERVICE_STRING);
      }
      ret.setEc_status(ecStatus);

      Informations ecInformations = new Informations();

      Gson gson = new Gson();
      Map<String, Object> scriptResultList = gson.fromJson(stdList.get(0),
          new TypeToken<HashMap<String, Object>>() {
          }.getType());

      ecInformations.setControllerType("ec");
      ecInformations.setHostName((String) scriptResultList.get("hostname"));
      ecInformations.setManagementIpAddress(serverAddress);

      String os = String.valueOf(scriptResultList.get("top"));
      String[] osList = os.split(",");
      Float id = (float) 0.0;
      Float free = (float) 0;
      Float used = (float) 0;
      Float buffers = (float) 0;
      Float swapUsed = (float) 0;
      int res = 0;
      int nproc = 0;
      Float cpu = (float) 0;

      if (getinfo == null || getinfo.isEmpty()) {
        AllSetFlg = true;
      }
      if (AllSetFlg || getinfo.contains("os-cpu")
          || getinfo.contains("os-mem") || getinfo.contains("os-disk")
          || getinfo.contains("ctr-cpu") || getinfo.contains("ctr-mem")) {
        try {
          try {
            id = Float.valueOf(osList[0].split("=")[1]);
          } catch (NumberFormatException nfe) {
            id = (float) -1;
          }
          try {
            free = Float.valueOf(osList[1].split("=")[1]);
          } catch (NumberFormatException nfe) {
            free = (float) -1;
          }
          try {
            used = Float.valueOf(osList[2].split("=")[1]);
          } catch (NumberFormatException nfe) {
            used = (float) -1;
          }
          try {
            buffers = Float.valueOf(osList[3].split("=")[1]);
          } catch (NumberFormatException nfe) {
            buffers = (float) -1;
          }
          try {
            swapUsed = Float.valueOf(osList[4].split("=")[1]);
          } catch (NumberFormatException nfe) {
            swapUsed = (float) -1;
          }
          try {
            res = Math.round(Float.valueOf(osList[5].split("=")[1]));
          } catch (NumberFormatException nfe) {
            res = -1;
          }
          try {
            cpu = Float.valueOf(osList[6].split("=")[1].replaceAll("}", ""));
          } catch (NumberFormatException nfe) {
            cpu = (float) -1;
          }

        } catch (ArrayIndexOutOfBoundsException ae) {
          id = (float) -1;
          free = (float) -1;
          used = (float) -1;
          buffers = (float) -1;
          swapUsed = (float) -1;
          res = -1;
          cpu = (float) -1;
        }
        logger.debug(id);
        logger.debug(free);
        logger.debug(used);
        logger.debug(buffers);
        logger.debug(swapUsed);
        logger.debug(res);
        logger.debug(cpu);


      }
      if (AllSetFlg || getinfo.contains("ctr-cpu")) {
        try {
          nproc = Math.round(Float.valueOf(String.valueOf(scriptResultList.get("nproc"))));
        } catch (NumberFormatException nfe) {
          logger.debug("get nproc error", scriptResultList.get("nproc"));
        }
        logger.debug("nproc=" + nproc);
      }

      if (AllSetFlg || getinfo.contains("os-cpu")) {
        ecInformations.setOs(new OsInfo());
        ecInformations.getOs().setCpu(new Cpu());
        if (id != -1) {
          ecInformations.getOs().getCpu().setUseRate((float)100 - id);
        } else {
          ecInformations.getOs().getCpu().setUseRate((float)-1);
        }
      }
      if (AllSetFlg || getinfo.contains("os-mem")) {
        if (ecInformations.getOs() == null) {
          ecInformations.setOs(new OsInfo());
        }
        ecInformations.getOs().setMemory(new Memory());
        ecInformations.getOs().getMemory().setUsed(used);
        ecInformations.getOs().getMemory().setFree(free);
        ecInformations.getOs().getMemory().setBuffCache(buffers);
        ecInformations.getOs().getMemory().setSwpd(swapUsed);
      }
      if (AllSetFlg || getinfo.contains("os-disk")) {
        ArrayList<DeviceInfo> devices = new ArrayList<DeviceInfo>();
        String[] dfList = String.valueOf(scriptResultList.get("df")).split(",");
        for (String info : dfList) {
          try {
            String info2 = info.replaceAll("\\[", "");
            String info3 = info2.replaceAll("\\]", "");
            String[] infoList = info3.split(" ");
            DeviceInfo devinfo = new DeviceInfo();
            devinfo.setAvail(Integer.parseInt(infoList[2]));
            devinfo.setFileSystem(infoList[1]);
            devinfo.setMountedOn(infoList[6]);
            devinfo.setSize(Integer.parseInt(infoList[4]));
            devinfo.setUsed(Integer.parseInt(infoList[3]));
            devices.add(devinfo);
          } catch (ArrayIndexOutOfBoundsException e) {
            continue;
          }
        }
        if (!devices.isEmpty()) {
          if (ecInformations.getOs() == null) {
            ecInformations.setOs(new OsInfo());
          }
          ecInformations.getOs().setDisk(new Disk());
          ecInformations.getOs().getDisk().setDevices(devices);
        }
      }
      if (AllSetFlg || getinfo.contains("os-traffic")) {
        ArrayList<InterfaceInfoTraffic> interfaceInfos = new ArrayList<InterfaceInfoTraffic>();
        String[] sarList = String.valueOf(scriptResultList.get("sar")).split(",");
        for (String info : sarList) {
          try {
            String info2 = info.replaceAll("\\[", "");
            String info3 = info2.replaceAll("\\]", "");
            String[] infoList = info3.trim().split(" ");
            InterfaceInfoTraffic interfaceInfo = new InterfaceInfoTraffic();
            interfaceInfo.setIfname(infoList[1]);
            interfaceInfo.setRxpck(Float.valueOf(infoList[2]));
            interfaceInfo.setTxpck(Float.valueOf(infoList[3]));
            interfaceInfo.setRxkb(Float.valueOf(infoList[4]));
            interfaceInfo.setTxkb(Float.valueOf(infoList[5]));
            interfaceInfos.add(interfaceInfo);
          } catch (ArrayIndexOutOfBoundsException aio) {
            logger.debug("SarResultError", aio);
            continue;
          }
        }
        if (!interfaceInfos.isEmpty()) {
          if (ecInformations.getOs() == null) {
            ecInformations.setOs(new OsInfo());
          }
          ecInformations.getOs().setTraffic(new Traffic());
          ecInformations.getOs().getTraffic().setInterfaces(interfaceInfos);
        }
      }
      if (AllSetFlg || getinfo.contains("ctr-cpu")) {
        ecInformations.setController(new ControllerInfo());
        if (cpu != -1 && nproc != 0) {
          ecInformations.getController().setCpu(cpu / (float)nproc);
        } else {
          ecInformations.getController().setCpu((float)-1);
        }
      }
      if (AllSetFlg || getinfo.contains("ctr-mem")) {
        if (ecInformations.getController() == null) {
          ecInformations.setController(new ControllerInfo());
        }
        ecInformations.getController().setMemory(res);
      }
      if (AllSetFlg || getinfo.contains("ctr-receive_req")) {
        if (ecInformations.getController() == null) {
          ecInformations.setController(new ControllerInfo());
        }
        ecInformations.getController().setReceiveRestRequest(receiveCount);
      }
      if (AllSetFlg || getinfo.contains("ctr-send_req")) {
        if (ecInformations.getController() == null) {
          ecInformations.setController(new ControllerInfo());
        }
        ecInformations.getController().setSendRestRequest(sendCount);
      }

      infolist.add(ecInformations);
    }

    if (emControllerInformations.getStatus() != null && !emControllerInformations.getStatus().isEmpty()) {
      EmStatus emstatus = new EmStatus();
      emstatus.setStatus(emControllerInformations.getStatus());
      ret.setEm_status(emstatus);
    }
    if (emControllerInformations.getInformations() != null && emControllerInformations.getInformations().size() != 0) {
      emControllerInformations.getInformations().get(0).setControllerType("em");
      infolist.add(emControllerInformations.getInformations().get(0));
    }
    if (!infolist.isEmpty()) {
      ret.setInformations(infolist);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;

  }
}
