/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.convert;

import static msf.ecmm.common.CommonDefinitions.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.db.pojo.BGPOptions;
import msf.ecmm.db.pojo.BootErrorMessages;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.DummyVlanIfs;
import msf.ecmm.db.pojo.EgressQueueMenus;
import msf.ecmm.db.pojo.EquipmentIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.IRBInstanceInfo;
import msf.ecmm.db.pojo.IfNameRules;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.LagMembers;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.RemarkMenus;
import msf.ecmm.db.pojo.StaticRouteOptions;
import msf.ecmm.db.pojo.VRRPOptions;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.emctrl.restpojo.ControllerLog;
import msf.ecmm.fcctrl.pojo.ConfigAuditNotification;
import msf.ecmm.fcctrl.pojo.ControllerStatusToFc;
import msf.ecmm.fcctrl.pojo.LogNotificationToFc;
import msf.ecmm.fcctrl.pojo.Operations;
import msf.ecmm.fcctrl.pojo.ServerNotificationToFc;
import msf.ecmm.fcctrl.pojo.UpdateLogicalIfStatus;
import msf.ecmm.fcctrl.pojo.parts.Controller;
import msf.ecmm.fcctrl.pojo.parts.ControllerLogToFc;
import msf.ecmm.fcctrl.pojo.parts.ControllerServerToFc;
import msf.ecmm.fcctrl.pojo.parts.CpuToFc;
import msf.ecmm.fcctrl.pojo.parts.DevicesToFc;
import msf.ecmm.fcctrl.pojo.parts.DiffToFc;
import msf.ecmm.fcctrl.pojo.parts.DiskToFc;
import msf.ecmm.fcctrl.pojo.parts.FailureInfoToFc;
import msf.ecmm.fcctrl.pojo.parts.IfsLogical;
import msf.ecmm.fcctrl.pojo.parts.LatestEmConfigToFc;
import msf.ecmm.fcctrl.pojo.parts.MemoryToFc;
import msf.ecmm.fcctrl.pojo.parts.NeConfigToFc;
import msf.ecmm.fcctrl.pojo.parts.NodeConfigToFc;
import msf.ecmm.fcctrl.pojo.parts.NodeLogical;
import msf.ecmm.ope.receiver.pojo.CheckEcMainModuleStatus;
import msf.ecmm.ope.receiver.pojo.EmControllerReceiveStatus;
import msf.ecmm.ope.receiver.pojo.GetAllNodeTraffic;
import msf.ecmm.ope.receiver.pojo.GetBreakoutInterface;
import msf.ecmm.ope.receiver.pojo.GetBreakoutInterfaceList;
import msf.ecmm.ope.receiver.pojo.GetConfigAuditList;
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
import msf.ecmm.ope.receiver.pojo.NotifyEmStatusLog;
import msf.ecmm.ope.receiver.pojo.NotifyEmStatusServer;
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
import msf.ecmm.ope.receiver.pojo.parts.Egress;
import msf.ecmm.ope.receiver.pojo.parts.EmStatus;
import msf.ecmm.ope.receiver.pojo.parts.Equipment;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentIf;
import msf.ecmm.ope.receiver.pojo.parts.IfNameRule;
import msf.ecmm.ope.receiver.pojo.parts.IfSearchIf;
import msf.ecmm.ope.receiver.pojo.parts.Informations;
import msf.ecmm.ope.receiver.pojo.parts.InterfaceInfoTraffic;
import msf.ecmm.ope.receiver.pojo.parts.IrbCapabilities;
import msf.ecmm.ope.receiver.pojo.parts.IrbUpdateValue;
import msf.ecmm.ope.receiver.pojo.parts.LagIf;
import msf.ecmm.ope.receiver.pojo.parts.LagMember;
import msf.ecmm.ope.receiver.pojo.parts.LagMembersBreakoutIfs;
import msf.ecmm.ope.receiver.pojo.parts.LagMembersPhysicalIfs;
import msf.ecmm.ope.receiver.pojo.parts.LogConditions;
import msf.ecmm.ope.receiver.pojo.parts.LogData;
import msf.ecmm.ope.receiver.pojo.parts.LogInformation;
import msf.ecmm.ope.receiver.pojo.parts.Memory;
import msf.ecmm.ope.receiver.pojo.parts.Node;
import msf.ecmm.ope.receiver.pojo.parts.NodeConfigAll;
import msf.ecmm.ope.receiver.pojo.parts.OsInfo;
import msf.ecmm.ope.receiver.pojo.parts.PhysicalIf;
import msf.ecmm.ope.receiver.pojo.parts.QInQCapabilities;
import msf.ecmm.ope.receiver.pojo.parts.QosCapabilities;
import msf.ecmm.ope.receiver.pojo.parts.QosConfigValues;
import msf.ecmm.ope.receiver.pojo.parts.QosGetVlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.QosRegisterEquipment;
import msf.ecmm.ope.receiver.pojo.parts.Remark;
import msf.ecmm.ope.receiver.pojo.parts.Shaping;
import msf.ecmm.ope.receiver.pojo.parts.StaticRoute;
import msf.ecmm.ope.receiver.pojo.parts.SwitchTraffic;
import msf.ecmm.ope.receiver.pojo.parts.Traffic;
import msf.ecmm.ope.receiver.pojo.parts.TrafficValue;
import msf.ecmm.ope.receiver.pojo.parts.VlanIf;
import msf.ecmm.ope.receiver.pojo.parts.Vrrp;
import msf.ecmm.ope.receiver.pojo.parts.Ztp;
import msf.ecmm.traffic.pojo.NodeKeySet;
import msf.ecmm.traffic.pojo.TrafficData;

/**
 * REST Data Mapping.
 */
public class RestMapper {

  /** logger. */
  private static final MsfLogger logger = new MsfLogger();

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
   * REST Data Mapping_Device List Information Acquisition.
   *
   * @param nodesList
   *          Device List Information
   * @return Device List Information (for sending to REST)
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
   * REST Data Mapping_Device List Information Acquisition.
   *
   * @param nodesList
   *          Device list information
   * @return Device list information (for sending to REST)
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
   * REST Data Mapping_Model List Information Acquisition.
   *
   * @param equipmentsList
   *          device list information
   * @return device list informaition (for sending to REST)
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
   * REST Data Mapping_Physical IF Information Acquisition.
   *
   * @param physicalIfs
   *          physical IF information
   * @param equipments
   *          model information
   * @return physical IF information (for sending to REST)
   */
  public static GetPhysicalInterface toPhyInInfo(PhysicalIfs physicalIfs, Equipments equipments) {

    logger.trace(CommonDefinitions.START);
    logger.debug(physicalIfs);

    GetPhysicalInterface ret = new GetPhysicalInterface();
    ret.setPhysicalIf(getPhysicalIf(physicalIfs, equipments));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_Physical IF Information List Acquisition.
   *
   * @param physicalIfs
   *          physical IF information list
   * @param equipments
   *          model information
   * @return physical IF information list (for sending to REST)
   */
  public static GetPhysicalInterfaceList toPhyInInfoList(List<PhysicalIfs> physicalIfs, Equipments equipments) {

    logger.trace(CommonDefinitions.START);
    logger.debug(physicalIfs);

    GetPhysicalInterfaceList ret = new GetPhysicalInterfaceList();
    ret.setPhysicalIfs(new ArrayList<PhysicalIf>());
    for (PhysicalIfs dbPhysicalIfs : physicalIfs) {
      ret.getPhysicalIfs().add(getPhysicalIf(dbPhysicalIfs, equipments));
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
   * @param equipments
   *          model information
   * @return LagIF information (for sending to REST)
   */
  public static GetLagInterface toLagIfInfo(LagIfs lagIfs, List<BreakoutIfs> breakoutIfsList, Equipments equipments) {

    logger.trace(CommonDefinitions.START);
    logger.debug(lagIfs + ", " + breakoutIfsList);

    GetLagInterface ret = new GetLagInterface();
    ret.setLagIfs(getLagIf(lagIfs, breakoutIfsList, equipments));

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
   * @param equipments
   *          model information
   * @return LagIF information list (for sending to REST)
   */
  public static GetLagInterfaceList toLagIfInfoList(List<LagIfs> lagIfs, List<BreakoutIfs> breakoutIfsList,
      Equipments equipments) {

    logger.trace(CommonDefinitions.START);
    logger.debug(lagIfs);

    GetLagInterfaceList ret = new GetLagInterfaceList();
    ret.setLagIfs(new ArrayList<LagIf>());
    for (LagIfs lag : lagIfs) {
      ret.getLagIfs().add(getLagIf(lag, breakoutIfsList, equipments));
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
   * @param equipments
   *          model information
   * @return BreakoutIF information (for sending to REST)
   */
  public static GetBreakoutInterface toBreakoutIfsInfo(BreakoutIfs breakoutIfsDb, Equipments equipments) {
    logger.trace(CommonDefinitions.START);
    logger.debug(breakoutIfsDb);

    GetBreakoutInterface ret = new GetBreakoutInterface();
    ret.setBreakoutIf(getBreakoutIf(breakoutIfsDb, equipments));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_BreakoutIF Information List Acquisition.
   *
   * @param breakoutIfsList
   *          BreakoutIF information list
   * @param equipments
   *          model information
   * @return BreakoutIF information list (for sending to REST)
   */
  public static GetBreakoutInterfaceList toBreakoutIfsInfoList(List<BreakoutIfs> breakoutIfsList,
      Equipments equipments) {
    logger.trace(CommonDefinitions.START);
    logger.debug(breakoutIfsList);

    GetBreakoutInterfaceList ret = new GetBreakoutInterfaceList();
    ret.setBreakoutIfs(new ArrayList<BreakoutIf>());

    for (BreakoutIfs breakoutIfs : breakoutIfsList) {
      ret.getBreakoutIfs().add(getBreakoutIf(breakoutIfs, equipments));
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
   * @param nodes
   *          device information
   * @param irbInstance
   *          IRB instance information
   * @return VLAN IF information (for sending to REST)
   */
  public static GetVlanInterface toVlanIfsInfo(VlanIfs vlanIfsDb, Nodes nodes, IRBInstanceInfo irbInstance) {
    logger.trace(CommonDefinitions.START);
    logger.debug(vlanIfsDb);

    GetVlanInterface ret = new GetVlanInterface();
    ret.setVlanIf(getVlanIf(vlanIfsDb, nodes, irbInstance));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST data mapping_dummy VLAN IF information acquisition.
   *
   * @param dummyIfsDb
   *          dummy VLAN IF information
   * @param nodes
   *          device information
   * @param irbInstance
   *          IRB instance information
   * @return VLAN IF information (for sending to REST)
   */
  public static GetVlanInterface toVlanIfsInfoFromDummyIfs(DummyVlanIfs dummyIfsDb, Nodes nodes,
      IRBInstanceInfo irbInstance) {
    logger.trace(CommonDefinitions.START);
    logger.debug(dummyIfsDb);

    GetVlanInterface ret = new GetVlanInterface();
    ret.setVlanIf(getVlanIfFromDummy(dummyIfsDb, nodes, irbInstance));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST Data Mapping_VLANIF Information List Acquisition.
   *
   * @param vlanIfsList
   *          VALNIF information list
   * @param nodes
   *          device information
   * @param irbList
   *          IRB instance information list
   * @return VLANIF information (for sending to REST)
   */
  public static GetVlanInterfaceList toVlanIfsInfoList(List<VlanIfs> vlanIfsList, Nodes nodes,
      List<IRBInstanceInfo> irbList) {
    logger.trace(CommonDefinitions.START);
    logger.debug(vlanIfsList);

    GetVlanInterfaceList ret = new GetVlanInterfaceList();
    ret.setVlanIf(new ArrayList<VlanIf>());

    IRBInstanceInfo irbInfo = null;
    for (VlanIfs vlanIfs : vlanIfsList) {
      for (IRBInstanceInfo checIrb : irbList) {
        if (checIrb.getIrb_instance_id().equals(vlanIfs.getIrb_instance_id())) {
          irbInfo = checIrb;
          break;
        }
      }
      ret.getVlanIfs().add(getVlanIf(vlanIfs, nodes, irbInfo));
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * REST data mapping_dummy VLANIF information list.
   *
   * @param vlanIfsList
   *          VLANIF information list
   * @param nodes
   *          device information
   * @param irbList
   *          IRB instance information list
   * @return VLANIF information (for sending to REST)
   */
  public static GetVlanInterfaceList toVlanIfsInfoListFromDummyIfs(List<DummyVlanIfs> vlanIfsList, Nodes nodes,
      List<IRBInstanceInfo> irbList) {
    logger.trace(CommonDefinitions.START);
    logger.debug(vlanIfsList);

    GetVlanInterfaceList ret = new GetVlanInterfaceList();
    ret.setVlanIf(new ArrayList<VlanIf>());

    IRBInstanceInfo irbInfo = null;
    for (DummyVlanIfs vlanIfs : vlanIfsList) {
      for (IRBInstanceInfo checIrb : irbList) {
        if (checIrb.getIrb_instance_id().equals(vlanIfs.getIrb_instance_id())) {
          irbInfo = checIrb;
          break;
        }
      }
      ret.getVlanIfs().add(getVlanIfFromDummy(vlanIfs, nodes, irbInfo));
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
  public static GetInterfaceList toIfList(Nodes nodesDb) {

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
        ret.getIfs().getPhysicalIfs().add(getPhysicalIf(physicalIfs, nodesDb.getEquipments()));
      }

      for (LagIfs lagIfs : nodesDb.getLagIfsList()) {
        ret.getIfs().getLagIfs().add(getLagIf(lagIfs, breakoutIfsDbList, nodesDb.getEquipments()));
      }

      for (BreakoutIfs breakoutIfs : breakoutIfsDbList) {
        ret.getIfs().getBreakoutIf().add(getBreakoutIf(breakoutIfs, nodesDb.getEquipments()));
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
    dbDataLoop: for (EquipmentIfs eifs : equipmentsDb.getEquipmentIfsList()) {
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

    ret.setQos(new QosRegisterEquipment());
    ret.getQos().setShaping(new Shaping());
    ret.getQos().getShaping().setEnable(equipmentsDb.getQos_shaping_flg());
    ret.getQos().setRemark(new Remark());
    ret.getQos().getRemark().setEnable(equipmentsDb.getQos_remark_flg());
    if (equipmentsDb.getQos_remark_flg()) {
      ret.getQos().getRemark().setDefaultValue(equipmentsDb.getDefault_remark_menu());
      ret.getQos().getRemark().setMenu(new ArrayList<String>());
      for (RemarkMenus remarkMenus : equipmentsDb.getRemarkMenusList()) {
        ret.getQos().getRemark().getMenu().add(remarkMenus.getRemark_menu());
      }
    } else {
      ret.getQos().getRemark().setDefaultValue(null);
      ret.getQos().getRemark().setMenu(null);
    }
    if (equipmentsDb.getQos_shaping_flg()) {
      ret.getQos().setEgress(new Egress());
      ret.getQos().getEgress().setDefaultValue(equipmentsDb.getDefault_egress_queue_menu());
      ret.getQos().getEgress().setMenu(new ArrayList<String>());
      for (EgressQueueMenus egressQueueMenus : equipmentsDb.getEgressQueueMenusList()) {
        ret.getQos().getEgress().getMenu().add(egressQueueMenus.getEgress_queue_menu());
      }
    } else {
      ret.getQos().setEgress(null);
    }
    ret.getCapabilities().setIrb(new IrbCapabilities());
    ret.getCapabilities().getIrb().setAsymmetric(equipmentsDb.getIrb_asymmetric_capability());
    ret.getCapabilities().getIrb().setSymmetric(equipmentsDb.getIrb_symmetric_capability());
    ret.setSameVlanNumberTrafficTotalValueFlag(equipmentsDb.getSame_vlan_number_traffic_total_value_flag());
    ret.setVlanTrafficCapability(equipmentsDb.getVlan_traffic_capability());
    ret.setVlanTrafficCounterNameMibOid(equipmentsDb.getVlan_traffic_counter_name_mib_oid());
    ret.setVlanTrafficCounterValueMibOid(equipmentsDb.getVlan_traffic_counter_value_mib_oid());
    ret.setCliExecPath(equipmentsDb.getCli_exec_path());
    ret.getCapabilities().setqInQ(new QInQCapabilities());
    ret.getCapabilities().getqInQ().setSelectableByNode(equipmentsDb.getQ_in_q_selectable_by_node_capability());
    ret.getCapabilities().getqInQ().setSelectableByVlanIf(equipmentsDb.getQ_in_q_selectable_by_vlan_if_capability());

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
    if (nodesDb.getNode_state() == NODE_STATE_FAILURE_SETTING) {
      ret.setNodeState(NODE_STATE_FAILURE_SETTING_STRING);
    }
    if (nodesDb.getNode_state() == NODE_STATE_FAILURE_NODE_RESETTING) {
      ret.setNodeState(NODE_STATE_FAILURE_NODE_RESETTING_STRING);
    }
    if (nodesDb.getNode_state() == NODE_STATE_FAILURE_SERVICE_SETTING) {
      ret.setNodeState(NODE_STATE_FAILURE_SERVICE_SETTING_STRING);
    }
    if (nodesDb.getNode_state() == NODE_STATE_FAILURE_OTHER) {
      ret.setNodeState(NODE_STATE_FAILURE_OTHER_STRING);
    }
    if (nodesDb.getNode_state() == NODE_STATE_FAILURE_RECOVER_NODE) {
      ret.setNodeState(NODE_STATE_FAILURE_RECOVER_NODE_STRING);
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
    ret.setIrbType(nodesDb.getIrb_type());
    ret.setqInQType(nodesDb.getQ_in_q_type());

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Physical IF Information Acquisition.
   *
   * @param lagIfs
   *          Physical IF information
   * @param equipments
   *          model information
   * @return LagIF information (for sending to REST)
   */
  private static PhysicalIf getPhysicalIf(PhysicalIfs physicalIfsDb, Equipments equipments) {
    logger.trace(CommonDefinitions.START);
    logger.debug(physicalIfsDb);

    PhysicalIf ret = new PhysicalIf();
    ret.setPhysicalIfId(physicalIfsDb.getPhysical_if_id());

    ret.setBreakoutIf(new ArrayList<BreakoutIfId>());
    for (BreakoutIfs breakoutIfs : physicalIfsDb.getBreakoutIfsList()) {
      BreakoutIfId ifId = new BreakoutIfId();
      ifId.setBreakoutIfId(breakoutIfs.getBreakout_if_id());
      ret.getBreakoutIf().add(ifId);
    }

    if (physicalIfsDb.getIf_speed() == null) {
      ret.setIfName(null);
      ret.setIfState(null);
      ret.setLinkSpeed(null);
    } else {
      ret.setIfName(physicalIfsDb.getIf_name());
      String ifState = LogicalPhysicalConverter.toStringIFState(physicalIfsDb.getIf_status());
      if (!ifState.equals(CommonDefinitions.IF_STATE_UNKNOWN_STRING)) {
        ret.setIfState(ifState);
      } else {
        ret.setIfState(CommonDefinitions.IF_STATE_NG_STRING);
      }
      ret.setLinkSpeed(physicalIfsDb.getIf_speed());
    }
    ret.setIpv4Address(physicalIfsDb.getIpv4_address());
    if (physicalIfsDb.getIpv4_prefix() != null && physicalIfsDb.getIpv4_prefix() != 0) {
      ret.setIpv4Prefix(String.valueOf(physicalIfsDb.getIpv4_prefix()));
    }

    ret.setQos(getQosCapabilities(equipments));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * LagIF Information Acquisition.
   *
   * @param lagIfs
   *          LagIF information
   * @param breakoutIfs
   *          breakoutIF information list
   * @param equipments
   *          model information
   * @return LagIF information (for sending to REST)
   */
  private static LagIf getLagIf(LagIfs lagIfs, List<BreakoutIfs> breakoutIfsList, Equipments equipments) {

    logger.trace(CommonDefinitions.START);
    logger.debug(lagIfs + ", " + breakoutIfsList);

    LagIf ret = new LagIf();
    ret.setLagIfId(lagIfs.getFc_lag_if_id());
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

    ret.setQos(getQosCapabilities(equipments));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * BreakoutIF Information Acquisition.
   *
   * @param breakoutIfs
   *          breakoutIF information
   * @param equipments
   *          model information
   * @return breakoutIF information(for sending REST)
   */
  private static BreakoutIf getBreakoutIf(BreakoutIfs breakoutIfsDb, Equipments equipments) {
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

    ret.setQos(getQosCapabilities(equipments));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * VLAN IF Information Acquisition.
   *
   * @param vlanIfsDb
   *          VLAN IF information
   * @param nodes
   *          device information
   * @param irbInstance
   *          IRB instance information
   * @return VLAN IF information(for sending REST)
   */
  private static VlanIf getVlanIf(VlanIfs vlanIfsDb, Nodes nodes, IRBInstanceInfo irbInstance) {
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
      if (nodes.getLagIfsList() != null) {
        for (LagIfs elem : nodes.getLagIfsList()) {
          if (elem.getLag_if_id().equals(vlanIfsDb.getLag_if_id())) {
            ret.getBaseIf().setIfId(elem.getFc_lag_if_id());
            break;
          }
        }
      }
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

    ret.setQos(new QosGetVlanIfs());
    ret.getQos().setCapability(getQosCapabilities(nodes.getEquipments()));
    if (nodes.getEquipments().getQos_shaping_flg()) {
      ret.getQos().setSetValue(new QosConfigValues());
      ret.getQos().getSetValue().setInflowShapingRate(vlanIfsDb.getInflow_shaping_rate());
      ret.getQos().getSetValue().setOutflowShapingRate(vlanIfsDb.getOutflow_shaping_rate());
      ret.getQos().getSetValue().setEgressMenu(vlanIfsDb.getEgress_queue_menu());
    }
    if (nodes.getEquipments().getQos_remark_flg()) {
      if (ret.getQos().getSetValue() == null) {
        ret.getQos().setSetValue(new QosConfigValues());
      }
      ret.getQos().getSetValue().setRemarkMenu(vlanIfsDb.getRemark_menu());
    }
    if (ret.getQos().getSetValue() != null && ret.getQos().getSetValue().getInflowShapingRate() == null
        && ret.getQos().getSetValue().getOutflowShapingRate() == null
        && ret.getQos().getSetValue().getRemarkMenu() == null && ret.getQos().getSetValue().getEgressMenu() == null) {
      ret.getQos().setSetValue(null);
    }
    if (null != irbInstance) {
      ret.setIrbValue(new IrbUpdateValue());
      ret.getIrbValue().setIpv4Address(irbInstance.getIrb_ipv4_address());
      ret.getIrbValue().setIpv4Prefix(irbInstance.getIrb_ipv4_prefix());
      ret.getIrbValue().setVirtualGatewayAddress(irbInstance.getVirtual_gateway_address());
    }
    ret.setqInQ(vlanIfsDb.getQ_in_q());

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;

  }

  /**
   * Dummy VLAN IF information acquisition.
   *
   * @param vlanIfsDb
   *          VLAN IF information
   * @param nodes
   *          device information
   * @return VLAN IF information (for sending to REST)
   */
  private static VlanIf getVlanIfFromDummy(DummyVlanIfs vlanIfsDb, Nodes nodes, IRBInstanceInfo irbInstance) {
    logger.trace(CommonDefinitions.START);
    logger.debug(vlanIfsDb);

    VlanIf ret = new VlanIf();

    ret.setVlanIfId(vlanIfsDb.getVlan_if_id());
    if (vlanIfsDb.getVlan_id() != null && !vlanIfsDb.getVlan_id().equals("0")) {
      ret.setVlanId(vlanIfsDb.getVlan_id());
    } else {
      ret.setVlanId(null);
    }

    if (null != irbInstance) {
      ret.setIrbValue(new IrbUpdateValue());
      ret.getIrbValue().setIpv4Address(irbInstance.getIrb_ipv4_address());
      ret.getIrbValue().setIpv4Prefix(irbInstance.getIrb_ipv4_prefix());
      ret.getIrbValue().setVirtualGatewayAddress(null);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;

  }

  /**
   * QoS configuration capabilities information Acquisition(for sending to REST).
   *
   * @param equipments
   *          model information
   * @return  QoS configuration capabilities Information(for sending to REST)
   */
  private static QosCapabilities getQosCapabilities(Equipments equipments) {
    QosCapabilities ret = new QosCapabilities();

    ret.setShaping(equipments.getQos_shaping_flg());
    ret.setRemark(equipments.getQos_remark_flg());
    if (equipments.getQos_remark_flg()) {
      ret.setRemarkMenuList(new ArrayList<String>());
      for (RemarkMenus remarkMenus : equipments.getRemarkMenusList()) {
        ret.getRemarkMenuList().add(remarkMenus.getRemark_menu());
      }
    } else {
      ret.setRemarkMenuList(null);
    }
    if (equipments.getQos_shaping_flg()) {
      ret.setEgressMenuList(new ArrayList<String>());
      for (EgressQueueMenus egressQueueMenus : equipments.getEgressQueueMenusList()) {
        ret.getEgressMenuList().add(egressQueueMenus.getEgress_queue_menu());
      }
    } else {
      ret.setEgressMenuList(null);
    }

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
      HashMap<NodeKeySet, HashMap<String, TrafficData>> trafficData, Timestamp time, int gatheringCycle,
      List<BreakoutIfs> breakoutIfsDbList) {

    logger.trace(CommonDefinitions.START);
    logger.debug("nodeList=" + nodeKey + ", trafficData=" + trafficData + ", time=" + time + ", gatheringCycle="
        + gatheringCycle);
    GetNodeTraffic ret = new GetNodeTraffic();
    ret.setIsSuccess(true);
    SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_HHmmss");
    ret.setTime(date.format(time.getTime()));
    ret.setInterval(gatheringCycle * 60);

    NodeKeySet key = nodeKey;
    SwitchTraffic tmpNode = setTrafficDataInternal(key, vlanIfsTable, trafficData, breakoutIfsDbList);

    if (!(tmpNode.getTrafficValue() == null)) {
      if (0 == tmpNode.getTrafficValue().size()) {
        tmpNode.setTrafficValue(null);
      }
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
  public static GetAllNodeTraffic toNodeTrafficDataList(ArrayList<NodeKeySet> nodeList,
      List<List<VlanIfs>> vlanIfsTable, HashMap<NodeKeySet, HashMap<String, TrafficData>> trafficData, Timestamp time,
      int gatheringCycle, List<BreakoutIfs> breakoutIfsDbList) {

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
      SwitchTraffic tmpNode = setTrafficDataInternal(key, vlanIfsTable, trafficData, breakoutIfsDbList);

      if (!(tmpNode.getTrafficValue() == null)) {
        if (0 == tmpNode.getTrafficValue().size()) {
          tmpNode.setTrafficValue(null);
        }
        ret.getSwitchTraffics().add(tmpNode);
      }
    }

    logger.trace(CommonDefinitions.END);

    logger.debug("ret=" + ret);

    return ret;
  }

  /**
   * Internal process for getting traffic information.
   *
   * @param nodeKey
   *          node infomation
   * @param vlanIfsTable
   *          Table list of VLAN IF information
   * @param trafficData
   *          traffic information
   * @param breakoutIfsDbList
   *          breakoutIF list (DB)
   * @return traffic information(for response)
   */
  private static SwitchTraffic setTrafficDataInternal(NodeKeySet nodeKey, List<List<VlanIfs>> vlanIfsTable,
      HashMap<NodeKeySet, HashMap<String, TrafficData>> trafficData, List<BreakoutIfs> breakoutIfsDbList) {

    SwitchTraffic tmpNode = new SwitchTraffic();
    tmpNode.setNodeId(nodeKey.getEquipmentsData().getNode_id());

    if ((trafficData.get(nodeKey) != null) && (trafficData.get(nodeKey).size() > 0)) {
      HashMap<String, TrafficData> nodeTrafficData = trafficData.get(nodeKey);

      for (PhysicalIfs ifs : nodeKey.getEquipmentsData().getPhysicalIfsList()) {
        setTrafficDataInternalParts(nodeTrafficData.get(ifs.getIf_name()),
            ifs.getIf_name(), CommonDefinitions.IF_TYPE_PHYSICAL_IF, ifs.getPhysical_if_id(), tmpNode);
      }

      for (LagIfs ifs : nodeKey.getEquipmentsData().getLagIfsList()) {
        setTrafficDataInternalParts(nodeTrafficData.get(ifs.getIf_name()),
            ifs.getIf_name(), CommonDefinitions.IF_TYPE_LAG_IF, ifs.getFc_lag_if_id(), tmpNode);
      }

      for (BreakoutIfs boIfs : breakoutIfsDbList) {
        if (!nodeKey.getEquipmentsData().getNode_id().equals(boIfs.getNode_id())) {
          continue;
        }

        setTrafficDataInternalParts(nodeTrafficData.get(boIfs.getIf_name()),
            boIfs.getIf_name(), CommonDefinitions.IF_TYPE_BREAKOUT_IF, boIfs.getBreakout_if_id(), tmpNode);
      }

      for (List<VlanIfs> vlanIfsList : vlanIfsTable) {
        for (VlanIfs vlanIfs : vlanIfsList) {
          if (!nodeKey.getEquipmentsData().getNode_id().equals(vlanIfs.getNode_id())) {
            continue;
          }

          String vpnType = nodeKey.getEquipmentsData().getVpn_type();
          String ifNameTmp = "";
          if (vpnType == null) {
            ifNameTmp = vlanIfs.getIf_name();
          } else {
            if (vpnType.equals(CommonDefinitions.VPNTYPE_L3) && !vlanIfs.getVlan_id().equals("0")) {
              ifNameTmp = vlanIfs.getIf_name() + nodeKey.getEquipmentsType().getUnit_connector() + vlanIfs.getVlan_id();
            } else {
              if ((null != nodeKey.getEquipmentsType().getVlan_traffic_capability() && null != vlanIfs.getPort_mode())
                  && ((VLAN_PORTMODE_TRUNK == vlanIfs.getPort_mode()
                      && ((nodeKey.getEquipmentsType().getVlan_traffic_capability().equals(VLAN_TRAFFIC_TYPE_CLI)
                          || (nodeKey.getEquipmentsType().getVlan_traffic_capability()
                              .equals(VLAN_TRAFFIC_TYPE_MIB))))))) {
                ifNameTmp = vlanIfs.getIf_name() + nodeKey.getEquipmentsType().getUnit_connector() + vlanIfs.getVlan_id();
              } else {
                ifNameTmp = vlanIfs.getIf_name();
              }
            }
          }

          setTrafficDataInternalParts(nodeTrafficData.get(ifNameTmp),
              ifNameTmp, CommonDefinitions.IF_TYPE_VLAN_IF, vlanIfs.getVlan_if_id(), tmpNode);
        }
      }
    }

    return tmpNode;
  }

  /**
   * Converting traffic information.
   *
   * @param trafficData
   *          traffic information
   * @param ifName
   *          IF name
   * @param ifType
   *          IF type
   * @param ifId
   *          IF ID
   * @param nodeTrafficDataList
   *          List for storing node traffic (to save conversion result)
   */
  private static void setTrafficDataInternalParts(TrafficData trafficData,
      String ifName, String ifType, String ifId, SwitchTraffic nodeTrafficDataList) {

    TrafficValue trfValue = new TrafficValue();

    if ((ifName != null) && (trafficData != null)) {
      trfValue.setIfType(ifType);
      trfValue.setIfId(ifId);
      trfValue.setReceiveRate(trafficData.getIfHclnOctets());
      trfValue.setSendRate(trafficData.getIfHcOutOctets());
      nodeTrafficDataList.getTrafficValue().add(trfValue);
    } else if (ifName != null) {
      trfValue.setIfType(ifType);
      trfValue.setIfId(ifId);
      trfValue.setReceiveRate(0.0);
      trfValue.setSendRate(0.0);
      nodeTrafficDataList.getTrafficValue().add(trfValue);
    }

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
    if (ecControllerLog != null && ecControllerLog.getEm_log() != null) {
      ret.getEcemLog().setEc_log(new LogInformation());
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

    if (emControllerLog != null && emControllerLog.getEm_log() != null) {
      ret.getEcemLog().setEm_log(new LogInformation());
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
   *  Controller Status Acquisition REST Response Acquisition.
   *
   * @param ecMainObstraction
   *
   * @param ecMainState
   *          EC-service start-up status
   * @param emMainState
   *          EC-service start-up status
   * @param informationList
   *          OS or acquired controller infomationOS
   * @return controller status acquisition REST response
   */
  public static CheckEcMainModuleStatus toControllerStatus(boolean ecMainObstraction, String ecMainState,
      String emMainState, ArrayList<Informations> informationList) {
    logger.trace(CommonDefinitions.START);

    CheckEcMainModuleStatus ret = new CheckEcMainModuleStatus();

    EcStatus ecStatus = new EcStatus();
    ecStatus.setStatus(ecMainState);
    if (ecMainObstraction == true) {
      ecStatus.setBusy(EC_BUSY_STRING);
    } else {
      ecStatus.setBusy(EC_IN_SERVICE_STRING);
    }

    EmStatus emStatus = new EmStatus();
    emStatus.setStatus(emMainState);

    ret.setEc_status(ecStatus);
    ret.setEm_status(emStatus);
    ret.setInformations(informationList);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;
  }

  /**
   *  Controller Status Acquisition REST Response Acquisition(only inforamtion-part).
   *
   * @param controllerType
   *          controllerType
   * @param getinfo
   *          acquisition parameter
   * @param serverAddress
   *          physical server address
   * @param receiveCount
   *          number of received REST
   * @param sendCount
   *          number of sent REST
   * @param stdList
   *          command execution result
   * @return status acquisition REST respons
   */
  public static Informations createInformations(String controllerType, String getinfo, String serverAddress,
      int receiveCount, int sendCount, List<String> stdList) {
    logger.trace(CommonDefinitions.START);

    boolean allSetFlg = false;

    Informations ecInformations = new Informations();
    Gson gson = new Gson();
    Map<String, Object> scriptResultList = gson.fromJson(stdList.get(0), new TypeToken<HashMap<String, Object>>() {
    }.getType());

    ecInformations.setControllerType(controllerType);
    ecInformations.setHostName((String) scriptResultList.get("hostname"));
    ecInformations.setManagementIpAddress(serverAddress);

    String os = String.valueOf(scriptResultList.get("top"));
    String[] osList = os.split(",");
    Float id = (float) 0.0;
    int free = 0;
    int used = 0;
    int buffers = 0;
    int swapUsed = 0;
    int res = 0;
    int nproc = 0;
    Float cpu = (float) 0;

    if (getinfo == null || getinfo.isEmpty()) {
      allSetFlg = true;
    }
    if (allSetFlg || getinfo.contains("os-cpu") || getinfo.contains("os-mem") || getinfo.contains("os-disk")
        || getinfo.contains("ctr-cpu") || getinfo.contains("ctr-mem")) {
      try {
        try {
          id = Float.valueOf(osList[0].split("=")[1]);
        } catch (NumberFormatException nfe) {
          id = (float) -1;
        }
        try {
          free = (int) (float) Float.valueOf(osList[1].split("=")[1]);
        } catch (NumberFormatException nfe) {
          free = -1;
        }
        try {
          used = (int) (float) Float.valueOf(osList[2].split("=")[1]);
        } catch (NumberFormatException nfe) {
          used = -1;
        }
        try {
          buffers = (int) (float) Float.valueOf(osList[3].split("=")[1]);
        } catch (NumberFormatException nfe) {
          buffers = -1;
        }
        try {
          swapUsed = (int) (float) Float.valueOf(osList[4].split("=")[1]);
        } catch (NumberFormatException nfe) {
          swapUsed = -1;
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
        free = -1;
        used = -1;
        buffers = -1;
        swapUsed = -1;
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

    if (allSetFlg || getinfo.contains("ctr-cpu")) {
      try {
        nproc = Math.round(Float.valueOf(String.valueOf(scriptResultList.get("nproc"))));
      } catch (NumberFormatException nfe) {
        logger.debug("get nproc error", scriptResultList.get("nproc"));
      }
      logger.debug("nproc=" + nproc);
    }

    if (allSetFlg || getinfo.contains("os-cpu")) {
      ecInformations.setOs(new OsInfo());
      ecInformations.getOs().setCpu(new Cpu());
      if (id != -1) {
        ecInformations.getOs().getCpu().setUseRate((float) 100 - id);
      } else {
        ecInformations.getOs().getCpu().setUseRate((float) -1);
      }
    }
    if (allSetFlg || getinfo.contains("os-mem")) {
      if (ecInformations.getOs() == null) {
        ecInformations.setOs(new OsInfo());
      }
      ecInformations.getOs().setMemory(new Memory());
      ecInformations.getOs().getMemory().setUsed(used);
      ecInformations.getOs().getMemory().setFree(free);
      ecInformations.getOs().getMemory().setBuffCache(buffers);
      ecInformations.getOs().getMemory().setSwpd(swapUsed);
    }
    if (allSetFlg || getinfo.contains("os-disk")) {
      ArrayList<DeviceInfo> devices = new ArrayList<DeviceInfo>();
      String[] dfList = String.valueOf(scriptResultList.get("df")).split(",");
      for (String info : dfList) {
        try {
          String info2 = info.replaceAll("\\[", "");
          String info3 = info2.replaceAll("\\]", "");
          String[] infoList = info3.trim().split(" ");
          DeviceInfo devinfo = new DeviceInfo();
          devinfo.setAvail(Integer.parseInt(infoList[3]));
          devinfo.setFileSystem(infoList[0]);
          devinfo.setMountedOn(infoList[5]);
          devinfo.setSize(Integer.parseInt(infoList[1]));
          devinfo.setUsed(Integer.parseInt(infoList[2]));
          devices.add(devinfo);
        } catch (ArrayIndexOutOfBoundsException aio) {
          logger.debug("DfResultError", aio);
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
    if (allSetFlg || getinfo.contains("os-traffic")) {
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

    if (allSetFlg || getinfo.contains("ctr-cpu")) {
      ecInformations.setController(new ControllerInfo());
      if (cpu != -1 && nproc != 0) {
        ecInformations.getController().setCpu(cpu / (float) nproc);
      } else {
        ecInformations.getController().setCpu((float) -1);
      }
    }
    if (allSetFlg || getinfo.contains("ctr-mem")) {
      if (ecInformations.getController() == null) {
        ecInformations.setController(new ControllerInfo());
      }
      ecInformations.getController().setMemory(res);
    }
    if (allSetFlg || getinfo.contains("ctr-receive_rest_req")) {
      if (ecInformations.getController() == null) {
        ecInformations.setController(new ControllerInfo());
      }
      ecInformations.getController().setReceiveRestRequest(receiveCount);
    }
    if (allSetFlg || getinfo.contains("ctr-send_rest_req")) {
      if (ecInformations.getController() == null) {
        ecInformations.setController(new ControllerInfo());
      }
      ecInformations.getController().setSendRestRequest(sendCount);
    }

    return ecInformations;
  }

  /**
   * REST data mapping : controller status  notification(server) (for EC).
   *
   * @param controllerType
   *          controller type
   * @param systemType
   *          system type
   * @param cpuToFc
   *          CPU
   * @param memoryToFc
   *          meomory
   * @param diskToFc
   *          disk usage
   * @return  information on controller status notification(server)(for sending REST)
   */
  public static ServerNotificationToFc toServerNotificationEc(String controllerType, String systemType, CpuToFc cpuToFc,
      MemoryToFc memoryToFc, DiskToFc diskToFc) {
    logger.trace(CommonDefinitions.START);

    ControllerServerToFc controllerServerToFc = new ControllerServerToFc();
    controllerServerToFc.setControllerType(controllerType);
    controllerServerToFc.setSystemType(systemType);

    FailureInfoToFc failureInfoToFc = new FailureInfoToFc();
    if (cpuToFc == null && memoryToFc == null && diskToFc == null) {
      return null;
    }

    if (cpuToFc != null) {
      failureInfoToFc.setCpu(cpuToFc);
    }
    if (memoryToFc != null) {
      failureInfoToFc.setMemory(memoryToFc);
    }
    if (diskToFc != null) {
      failureInfoToFc.setDisk(diskToFc);
    }

    controllerServerToFc.setFailureInfo(failureInfoToFc);
    ServerNotificationToFc ret = new ServerNotificationToFc();
    ret.setController(controllerServerToFc);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;
  }

  /**
   * REST data mapping : controller status  notification(server) (for EM).
   *
   * @param emConStatusSrv
   *          EM controller status (server)
   * @return  information on controller status notification(server)(for sending REST)
   */
  public static ServerNotificationToFc toServerNotificationEm(NotifyEmStatusServer emConStatusSrv) {
    logger.trace(CommonDefinitions.START);

    final String emAct = "em_act";
    final String emSby = "em_sby";

    ControllerServerToFc controllerServerToFc = new ControllerServerToFc();

    controllerServerToFc.setControllerType(emAct);
    if (emConStatusSrv.getControllerServer().getControllerType().equals(emSby)) {
      controllerServerToFc.setControllerType(emSby);
    }
    controllerServerToFc.setSystemType(emConStatusSrv.getControllerServer().getSystemType());

    CpuToFc cpuToFc = null;
    if (emConStatusSrv.getControllerServer().getFailureInfo().getCpu() != null) {
      cpuToFc = new CpuToFc();
      cpuToFc.setUseRate(emConStatusSrv.getControllerServer().getFailureInfo().getCpu().getUseRate());
    }

    MemoryToFc memoryToFc = null;
    if (emConStatusSrv.getControllerServer().getFailureInfo().getMemory() != null) {
      memoryToFc = new MemoryToFc();
      memoryToFc.setUsed(emConStatusSrv.getControllerServer().getFailureInfo().getMemory().getUsed());
      if (emConStatusSrv.getControllerServer().getSystemType().equals(CommonDefinitions.SYSTEM_TYPE_CTL_PROCESS)) {
        memoryToFc.setFree(null);
      } else {
        memoryToFc.setFree(emConStatusSrv.getControllerServer().getFailureInfo().getMemory().getFree());
      }
    }

    DiskToFc diskToFc = null;
    if (emConStatusSrv.getControllerServer().getFailureInfo().getDisk() != null) {
      List<DevicesToFc> devicesToFcs = new ArrayList<DevicesToFc>();
      for (DeviceInfo deviceInfo : emConStatusSrv.getControllerServer().getFailureInfo().getDisk().getDevices()) {
        DevicesToFc devicesToFc = new DevicesToFc();
        devicesToFc.setFileSystem(deviceInfo.getFileSystem());
        devicesToFc.setMountedOn(deviceInfo.getMountedOn());
        devicesToFc.setSize(deviceInfo.getSize());
        devicesToFc.setUsed(deviceInfo.getUsed());
        devicesToFcs.add(devicesToFc);
      }
      diskToFc = new DiskToFc();
      diskToFc.setDevices(devicesToFcs);
    }

    FailureInfoToFc failureInfoToFc = new FailureInfoToFc();
    failureInfoToFc.setCpu(cpuToFc);
    failureInfoToFc.setMemory(memoryToFc);
    failureInfoToFc.setDisk(diskToFc);
    controllerServerToFc.setFailureInfo(failureInfoToFc);

    ServerNotificationToFc ret = new ServerNotificationToFc();
    ret.setController(controllerServerToFc);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;
  }

  /**
   * REST data mapping : controller status  notification(log) (for EM).
   *
   * @param emConStatusLog
*          EM controller status(log)
   * @return i nformation on controller status notification(log)(for sending REST)
   */
  public static LogNotificationToFc toLogNotificationEm(NotifyEmStatusLog emConStatusLog) {
    logger.trace(CommonDefinitions.START);

    final String emAct = "em_act";

    ControllerLogToFc controllerLogToFc = new ControllerLogToFc();

    controllerLogToFc.setControllerType(emAct);
    controllerLogToFc.setLogLevel(emConStatusLog.getControllerLog().getLogLevel());
    List<String> logs = new ArrayList<String>();
    for (String log : emConStatusLog.getControllerLog().getMessage()) {
      logs.add(log);
    }
    controllerLogToFc.setLog(logs);

    LogNotificationToFc ret = new LogNotificationToFc();
    ret.setController(controllerLogToFc);
    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;
  }

  /**
   * REST data mapping: Config-Audit difference information notification.
   *
   * @param emresp
   *          Config-Audit difference information
   * @return Config-Audit difference information notification(for sending REST)
   */
  public static ConfigAuditNotification toConfigAuditNotification(GetConfigAuditList emresp) {
    logger.trace(CommonDefinitions.START);

    ConfigAuditNotification ret = new ConfigAuditNotification();

    for (NodeConfigAll result : emresp.getNodes()) {

      NodeConfigToFc nodeConfig = new NodeConfigToFc();
      nodeConfig.setNodeId(result.getNode().getNodeId());

      LatestEmConfigToFc latestEmConfig = null;
      if (result.getNode().getLatestEmConfig() != null) {
        latestEmConfig = new LatestEmConfigToFc();
        latestEmConfig.setDate(result.getNode().getLatestEmConfig().getDate());
        latestEmConfig.setServerName(result.getNode().getLatestEmConfig().getServerName());
        latestEmConfig.setConfig(result.getNode().getLatestEmConfig().getConfig());
      }

      NeConfigToFc neConfig = new NeConfigToFc();
      neConfig.setDate(result.getNode().getNeConfig().getDate());
      neConfig.setConfig(result.getNode().getNeConfig().getConfig());

      DiffToFc diff = new DiffToFc();
      diff.setDiffDataUnified(result.getNode().getDiff().getDiffDataUnified());

      nodeConfig.setLatestEmConfig(latestEmConfig);
      nodeConfig.setNeConfig(neConfig);
      nodeConfig.setDiff(diff);

      ret.getNodes().add(nodeConfig);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }
}
