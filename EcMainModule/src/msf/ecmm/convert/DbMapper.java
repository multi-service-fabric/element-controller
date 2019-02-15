/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.convert;

import static msf.ecmm.convert.LogicalPhysicalConverter.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
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
import msf.ecmm.db.pojo.NodesStartupNotification;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.RemarkMenus;
import msf.ecmm.db.pojo.StaticRouteOptions;
import msf.ecmm.db.pojo.VRRPOptions;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.ope.control.NodeAdditionState;
import msf.ecmm.ope.receiver.pojo.AddNode;
import msf.ecmm.ope.receiver.pojo.CreateBreakoutIf;
import msf.ecmm.ope.receiver.pojo.CreateLagInterface;
import msf.ecmm.ope.receiver.pojo.DeleteNode;
import msf.ecmm.ope.receiver.pojo.RecoverNodeService;
import msf.ecmm.ope.receiver.pojo.RegisterEquipmentType;
import msf.ecmm.ope.receiver.pojo.UpdatePhysicalInterface;
import msf.ecmm.ope.receiver.pojo.parts.BreakoutBaseIf;
import msf.ecmm.ope.receiver.pojo.parts.CreateVlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentIf;
import msf.ecmm.ope.receiver.pojo.parts.IfNameRule;
import msf.ecmm.ope.receiver.pojo.parts.InterfaceInfo;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkIf;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkInfo;
import msf.ecmm.ope.receiver.pojo.parts.NodeInterface;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodesDeleteNode;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodesInterface;
import msf.ecmm.ope.receiver.pojo.parts.PhysicalIfsCreateLagIf;
import msf.ecmm.ope.receiver.pojo.parts.QosRegisterEquipment;
import msf.ecmm.ope.receiver.pojo.parts.QosUpdateVlanIf;
import msf.ecmm.ope.receiver.pojo.parts.StaticRoute;
import msf.ecmm.ope.receiver.pojo.parts.UpdateStaticRoute;
import msf.ecmm.ope.receiver.pojo.parts.UpdateVlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsCreateL3VlanIf;

/**
 * DB Data Mapping.
 *
 */
public class DbMapper {

  /** logger. */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /**
   * DB data mapping_generate LagIF<br>
   * Converting LagIF generation information into DB storable format.
   *
   * @param input
   *          LagIf generation information
   * @param nodes
   *          device information
   * @param lagIfId
   *          LagIf id
   * @return information for generating LagIF (for DB storage)
   */
  public static LagIfs toLagIfCreate(CreateLagInterface input, Nodes nodes, String lagIfId) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodes);

    LagIfs ret = new LagIfs();

    String lagIfName = toLagIfName(nodes.getEquipments().getLag_prefix(), lagIfId);

    ret.setNode_id(nodes.getNode_id());
    ret.setLag_if_id(lagIfId);
    ret.setFc_lag_if_id(input.getLagIf().getLagIfId());
    ret.setIf_name(lagIfName);
    ret.setMinimum_link_num(input.getLagIf().getPhysicalIfs().size());
    ret.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);

    Set<LagMembers> membersList = new HashSet<LagMembers>();
    List<String> speedList = new ArrayList<String>();
    for (PhysicalIfsCreateLagIf phys : input.getLagIf().getPhysicalIfs()) {
      LagMembers members = new LagMembers();
      members.setNode_id(nodes.getNode_id());
      members.setLag_if_id(lagIfId);
      if (phys.getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
        members.setPhysical_if_id(phys.getIfId());
        for (PhysicalIfs ifs : nodes.getPhysicalIfsList()) {
          if (ifs.getPhysical_if_id().equals(phys.getIfId())) {
            speedList.add(ifs.getIf_speed());
            break;
          }
        }
      } else if (phys.getIfType().equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
        members.setBreakout_if_id(phys.getIfId());
        searchSpd: for (PhysicalIfs physIfs : nodes.getPhysicalIfsList()) {
          if (physIfs.getBreakoutIfsList() != null) {
            for (BreakoutIfs boIfs : physIfs.getBreakoutIfsList()) {
              if (boIfs.getBreakout_if_id().equals(input.getLagIf().getPhysicalIfs().get(0).getIfId())) {
                speedList.add(boIfs.getSpeed());
                break searchSpd;
              }
            }
          }
        }
      } else {
        throw new IllegalArgumentException();
      }
      membersList.add(members);
    }
    if (speedList.isEmpty()) {
      throw new IllegalArgumentException();
    } else {
      String speed = speedList.get(0);
      List<String> tmp = new ArrayList<String>(1);
      Collections.addAll(tmp, speed);
      speedList.removeAll(tmp);
      if (speedList.isEmpty()) {
        ret.setIf_speed(speed);
      } else {
        throw new IllegalArgumentException();
      }
    }

    ret.setLagMembersList(membersList);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DB Data Mapping_Model Information Registration<br>
   * Convert model information into DB storable format.
   *
   * @param registerEquipmentTypeRest
   *          model information
   * @return model information (for DB storage)
   */
  public static Equipments toEqInfoCreate(RegisterEquipmentType registerEquipmentTypeRest) {

    logger.trace(CommonDefinitions.START);
    logger.debug(registerEquipmentTypeRest);

    Equipments ret = new Equipments();

    ret.setEquipment_type_id(registerEquipmentTypeRest.getEquipment().getEquipmentTypeId());
    ret.setLag_prefix(registerEquipmentTypeRest.getEquipment().getLagPrefix());
    ret.setUnit_connector(registerEquipmentTypeRest.getEquipment().getUnitConnector());
    ret.setIf_name_oid(registerEquipmentTypeRest.getEquipment().getIfNameOid());
    ret.setSnmptrap_if_name_oid(registerEquipmentTypeRest.getEquipment().getSnmptrapIfNameOid());
    ret.setMax_repetitions(registerEquipmentTypeRest.getEquipment().getMaxRepetitions());
    ret.setPlatform_name(registerEquipmentTypeRest.getEquipment().getPlatform());
    ret.setOs_name(registerEquipmentTypeRest.getEquipment().getOs());
    ret.setFirmware_version(registerEquipmentTypeRest.getEquipment().getFirmware());
    if (registerEquipmentTypeRest.getEquipment().getRouterType().equals(CommonDefinitions.ROUTER_TYPE_NORMAL_STRING)) {
      ret.setRouter_type(CommonDefinitions.ROUTER_TYPE_NORMAL);
    } else if (registerEquipmentTypeRest.getEquipment().getRouterType()
        .equals(CommonDefinitions.ROUTER_TYPE_COREROUTER_STRING)) {
      ret.setRouter_type(CommonDefinitions.ROUTER_TYPE_COREROUTER);
    }
    ret.setCore_router_physical_if_name_format(registerEquipmentTypeRest.getEquipment().getPhysicalIfNameSyntax());
    ret.setBreakout_if_name_syntax(registerEquipmentTypeRest.getEquipment().getBreakoutIfNameSyntax());
    ret.setBreakout_if_name_suffix_list(registerEquipmentTypeRest.getEquipment().getBreakoutIfNameSuffixList());
    ret.setDhcp_template(registerEquipmentTypeRest.getEquipment().getZtp().getDhcpTemplate());
    ret.setConfig_template(registerEquipmentTypeRest.getEquipment().getZtp().getConfigTemplate());
    ret.setInitial_config(registerEquipmentTypeRest.getEquipment().getZtp().getInitialConfig());
    ret.setBoot_complete_msg(registerEquipmentTypeRest.getEquipment().getZtp().getBootCompleteMsg());
    if (registerEquipmentTypeRest.getEquipment().getCapabilities() != null) {
      if (registerEquipmentTypeRest.getEquipment().getCapabilities().isEvpn() != null) {
        ret.setEvpn_capability(registerEquipmentTypeRest.getEquipment().getCapabilities().isEvpn());
      } else {
        ret.setEvpn_capability(false);
      }
      if (registerEquipmentTypeRest.getEquipment().getCapabilities().isL2vpn() != null) {
        ret.setL2vpn_capability(registerEquipmentTypeRest.getEquipment().getCapabilities().isL2vpn());
      } else {
        ret.setL2vpn_capability(false);
      }
      if (registerEquipmentTypeRest.getEquipment().getCapabilities().isL3vpn() != null) {
        ret.setL3vpn_capability(registerEquipmentTypeRest.getEquipment().getCapabilities().isL3vpn());
      } else {
        ret.setL3vpn_capability(false);
      }
      if (registerEquipmentTypeRest.getEquipment().getCapabilities().getIrb() != null) {
        if (registerEquipmentTypeRest.getEquipment().getCapabilities().getIrb().getAsymmetric() != null) {
          ret.setIrb_asymmetric_capability(
              registerEquipmentTypeRest.getEquipment().getCapabilities().getIrb().getAsymmetric());
        } else {
          ret.setIrb_asymmetric_capability(false);
        }
        if (registerEquipmentTypeRest.getEquipment().getCapabilities().getIrb().getSymmetric() != null) {
          ret.setIrb_symmetric_capability(
              registerEquipmentTypeRest.getEquipment().getCapabilities().getIrb().getSymmetric());
        } else {
          ret.setIrb_symmetric_capability(false);
        }
      }else {
        ret.setIrb_asymmetric_capability(false);
        ret.setIrb_symmetric_capability(false);
      }
    } else {
      ret.setEvpn_capability(false);
      ret.setL2vpn_capability(false);
      ret.setL3vpn_capability(false);
    }
    QosRegisterEquipment qos = registerEquipmentTypeRest.getEquipment().getQos();
    if (qos != null) {
      if (qos.getShaping().getEnable() == null) {
        ret.setQos_shaping_flg(false);
      } else {
        ret.setQos_shaping_flg(qos.getShaping().getEnable());
      }
      if (ret.getQos_shaping_flg() && qos.getEgress() != null) {
        ret.setDefault_egress_queue_menu(qos.getEgress().getDefaultValue());
        ret.setEgressQueueMenusList(new HashSet<EgressQueueMenus>());
        for (String menu : qos.getEgress().getMenu()) {
          EgressQueueMenus egressQueueMenus = new EgressQueueMenus();
          egressQueueMenus.setEquipment_type_id(registerEquipmentTypeRest.getEquipment().getEquipmentTypeId());
          egressQueueMenus.setEgress_queue_menu(menu);
          ret.getEgressQueueMenusList().add(egressQueueMenus);
        }
      }
      if (qos.getRemark().getEnable() == null) {
        ret.setQos_remark_flg(false);
      } else {
        ret.setQos_remark_flg(qos.getRemark().getEnable());
      }
      if (ret.getQos_remark_flg()) {
        ret.setDefault_remark_menu(qos.getRemark().getDefaultValue());
        if (qos.getRemark().getMenu() != null) {
          ret.setRemarkMenusList(new HashSet<RemarkMenus>());
          for (String menu : qos.getRemark().getMenu()) {
            RemarkMenus remarkMenus = new RemarkMenus();
            remarkMenus.setEquipment_type_id(registerEquipmentTypeRest.getEquipment().getEquipmentTypeId());
            remarkMenus.setRemark_menu(menu);
            ret.getRemarkMenusList().add(remarkMenus);
          }
        }
      }
    } else {
      ret.setQos_shaping_flg(false);
      ret.setQos_remark_flg(false);
    }

    Set<EquipmentIfs> equipmentIfsList = new HashSet<EquipmentIfs>();
    for (EquipmentIf eif : registerEquipmentTypeRest.getEquipment().getEquipmentIfs()) {
      for (String spdType : eif.getPortSpeedType()) {
        EquipmentIfs equipmentIfs = new EquipmentIfs();
        equipmentIfs.setEquipment_type_id(registerEquipmentTypeRest.getEquipment().getEquipmentTypeId());
        equipmentIfs.setPhysical_if_id(eif.getPhysicalIfId());
        equipmentIfs.setIf_slot(eif.getIfSlot());
        equipmentIfs.setPort_speed(spdType);
        equipmentIfsList.add(equipmentIfs);
      }
    }
    ret.setEquipmentIfsList(equipmentIfsList);

    Set<IfNameRules> ifNameRulesList = new HashSet<IfNameRules>();
    for (IfNameRule inr : registerEquipmentTypeRest.getEquipment().getIfNameRules()) {
      IfNameRules ifNameRules = new IfNameRules();
      ifNameRules.setEquipment_type_id(registerEquipmentTypeRest.getEquipment().getEquipmentTypeId());
      ifNameRules.setSpeed(inr.getSpeed());
      ifNameRules.setPort_prefix(inr.getPortPrefix());
      ifNameRulesList.add(ifNameRules);
    }
    ret.setIfNameRulesList(ifNameRulesList);

    Set<BootErrorMessages> bootErrorMsgsList = new HashSet<BootErrorMessages>();
    if (registerEquipmentTypeRest.getEquipment().getZtp().getBootErrorMsgs() != null) {
      for (String bem : registerEquipmentTypeRest.getEquipment().getZtp().getBootErrorMsgs()) {
        BootErrorMessages bootErrorMsgs = new BootErrorMessages();
        bootErrorMsgs.setEquipment_type_id(registerEquipmentTypeRest.getEquipment().getEquipmentTypeId());
        bootErrorMsgs.setBoot_error_msgs(bem);
        bootErrorMsgsList.add(bootErrorMsgs);
      }
    }
    ret.setBootErrorMessagesList(bootErrorMsgsList);

    if (null == registerEquipmentTypeRest.getEquipment().getSameVlanNumberTrafficTotalValueFlag()) {
      ret.setSame_vlan_number_traffic_total_value_flag(false);
    } else {
      ret.setSame_vlan_number_traffic_total_value_flag(
          registerEquipmentTypeRest.getEquipment().getSameVlanNumberTrafficTotalValueFlag());
    }
    if ( null != registerEquipmentTypeRest.getEquipment().getVlanTrafficCapability()) {
      ret.setVlan_traffic_capability(registerEquipmentTypeRest.getEquipment().getVlanTrafficCapability());
      if (registerEquipmentTypeRest.getEquipment().getVlanTrafficCapability()
          .equals(CommonDefinitions.VLAN_TRAFFIC_TYPE_MIB)) {
        ret.setVlan_traffic_counter_name_mib_oid(
            registerEquipmentTypeRest.getEquipment().getVlanTrafficCounterNameMibOid());
        ret.setVlan_traffic_counter_value_mib_oid(
            registerEquipmentTypeRest.getEquipment().getVlanTrafficCounterValueMibOid());
      } else if (registerEquipmentTypeRest.getEquipment().getVlanTrafficCapability()
          .equals(CommonDefinitions.VLAN_TRAFFIC_TYPE_CLI)) {
        ret.setCli_exec_path(registerEquipmentTypeRest.getEquipment().getCliExecPath());
      }
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DB Data Mapping_Device Information Registration; Convert Leaf extention information into DB storable format.
   *
   * @param addNodeRest
   *          extention device information (REST)
   * @param equipments
   *          device information (DB)
   * @return device inofrmation (for DB storage)
   */
  public static Nodes toAddNode(AddNode addNodeRest, Equipments equipments) {
    logger.trace(CommonDefinitions.START);
    logger.debug("addNodeRest=" + addNodeRest);

    Nodes ret = new Nodes();

    ret.setNode_id(addNodeRest.getCreateNode().getNodeId());
    ret.setEquipment_type_id(addNodeRest.getEquipment().getEquipmentTypeId());
    ret.setNode_name(addNodeRest.getCreateNode().getHostname());
    ret.setHost_name(addNodeRest.getCreateNode().getHostname()); 
    ret.setManagement_if_address(addNodeRest.getCreateNode().getManagementInterface().getAddress());
    ret.setMng_if_addr_prefix(addNodeRest.getCreateNode().getManagementInterface().getPrefix());
    ret.setSnmp_community(addNodeRest.getCreateNode().getSnmpCommunity());
    ret.setNode_state(NodeAdditionState.UnInitilized.getValue()); 
    ret.setProvisioning(addNodeRest.getCreateNode().getProvisioning());
    if (addNodeRest.getCreateNode().getPlane() == null) {
      ret.setPlane(null);
    } else {
      ret.setPlane(new Integer(addNodeRest.getCreateNode().getPlane()));
    }
    if (addNodeRest.getCreateNode().getVpn() != null) {
      ret.setVpn_type(addNodeRest.getCreateNode().getVpn().getVpnType());
      if (addNodeRest.getCreateNode().getVpn().getL2vpn() != null) {
        ret.setAs_number(addNodeRest.getCreateNode().getVpn().getL2vpn().getAs().getAsNumber());
      } else if (addNodeRest.getCreateNode().getVpn().getL3vpn() != null) {
        ret.setAs_number(addNodeRest.getCreateNode().getVpn().getL3vpn().getAs().getAsNumber());
      }
    }
    ret.setLoopback_if_address(addNodeRest.getCreateNode().getLoopbackInterface().getAddress());
    ret.setUsername(addNodeRest.getCreateNode().getUsername());
    ret.setPassword(addNodeRest.getCreateNode().getPassword());
    ret.setNtp_server_address(addNodeRest.getCreateNode().getNtpServerAddress());
    ret.setMac_addr(addNodeRest.getCreateNode().getMacAddress());

    Set<PhysicalIfs> physicalSet = new HashSet<PhysicalIfs>();
    for (EquipmentIfs list : equipments.getEquipmentIfsList()) {
      PhysicalIfs physicalIfs = new PhysicalIfs();
      physicalIfs.setNode_id(addNodeRest.getCreateNode().getNodeId());
      physicalIfs.setPhysical_if_id(list.getPhysical_if_id());
      physicalIfs.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);
      physicalSet.add(physicalIfs);
    }
    ret.setPhysicalIfsList(physicalSet);

    Set<NodesStartupNotification> nodesStartupNotificationSet = new HashSet<NodesStartupNotification>();
    NodesStartupNotification nodesStartupNotification = new NodesStartupNotification();
    nodesStartupNotification.setNode_id(addNodeRest.getCreateNode().getNodeId());
    nodesStartupNotification.setNotification_reception_status(CommonDefinitions.WAIT_NOTIFICATION);
    nodesStartupNotificationSet.add(nodesStartupNotification);
    ret.setNodesStartupNotificationList(nodesStartupNotificationSet);

    ret.setIrb_type(addNodeRest.getCreateNode().getIrbType());

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DB Data Mapping_Physical IF Information Change<br>
   * Convert physical IF information into DB storable format.
   *
   * @param input
   *          input information
   * @param nodesDb
   *          device information
   * @param physicalIfId
   *          physical IF ID
   * @return physical IF information (for DB storage)
   * @throws IllegalArgumentException
   *           There is no specified port speed or physical IF ID in the model related information.
   */
  public static PhysicalIfs toPhyIfChange(UpdatePhysicalInterface input, Nodes nodesDb, String physicalIfId)
      throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesDb + ", physicalIfId=" + physicalIfId);

    PhysicalIfs ret = new PhysicalIfs();
    ret.setNode_id(nodesDb.getNode_id());
    ret.setPhysical_if_id(physicalIfId);

    if (nodesDb.getEquipments() == null) {
      throw new IllegalArgumentException();
    } else {
      if (nodesDb.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_NORMAL) {
        String prefix = null;
        String slot = null;
        for (IfNameRules ifNameRules : nodesDb.getEquipments().getIfNameRulesList()) {
          if (ifNameRules.getSpeed().equals(input.getSpeed())) {
            prefix = ifNameRules.getPort_prefix();
            break;
          }
        }
        for (EquipmentIfs equipmentIfs : nodesDb.getEquipments().getEquipmentIfsList()) {
          if (equipmentIfs.getPhysical_if_id().equals(physicalIfId)) {
            slot = equipmentIfs.getIf_slot();
            break;
          }
        }
        if (prefix != null && slot != null) {
          ret.setIf_name(toPhysicalIfName(prefix, slot));
        } else {
          throw new IllegalArgumentException();
        }
      } else if (nodesDb.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
        ret.setIf_name(toCoreRouterIfName(nodesDb, physicalIfId, input.getSpeed()));
      } else {
        throw new IllegalArgumentException();
      }
    }

    ret.setIf_speed(input.getSpeed());
    ret.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DB Data Mapping_Device Extention; Converting device information into DB storable format.
   *
   * @param inData
   *          device extention information
   * @param equipments
   *          device information (configure only when new device information is registered)
   * @param nodesDb
   *          device information (configure only when the device information has been already registered)
   * @param oppoNodeDbList
   *          opposing device information
   * @return device information list (physical IF info, LAG info, internal link IF info) (for DB storage)
   * @throws IllegalArgumentException
   *           physical IF name generation failure, etc.
   */
  public static List<Nodes> toAddNodeRelation(AddNode inData, Equipments equipments, Nodes nodesDb,
      List<Nodes> oppoNodeDbList) throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(inData + ", " + equipments + ", " + nodesDb);

    Nodes preNodes = new Nodes();
    preNodes.setNode_id(nodesDb.getNode_id());
    preNodes.setNode_name(nodesDb.getNode_name());
    preNodes.setEquipment_type_id(nodesDb.getEquipment_type_id());
    preNodes.setManagement_if_address(nodesDb.getManagement_if_address());
    preNodes.setSnmp_community(nodesDb.getSnmp_community());
    preNodes.setNode_state(nodesDb.getNode_state());
    preNodes.setProvisioning(nodesDb.getProvisioning());
    preNodes.setPlane(nodesDb.getPlane());
    preNodes.setVpn_type(nodesDb.getVpn_type());
    preNodes.setLoopback_if_address(nodesDb.getLoopback_if_address());
    preNodes.setUsername(nodesDb.getUsername());
    preNodes.setPassword(nodesDb.getPassword());
    preNodes.setNtp_server_address(nodesDb.getNtp_server_address());
    preNodes.setHost_name(nodesDb.getHost_name());
    preNodes.setMac_addr(nodesDb.getMac_addr());
    preNodes.setEquipments(equipments);
    preNodes.setPhysicalIfsList(nodesDb.getPhysicalIfsList());
    preNodes.setNodesStartupNotificationList(nodesDb.getNodesStartupNotificationList());
    if (nodesDb.getLagIfsList() == null) {
      preNodes.setLagIfsList(new HashSet<LagIfs>());
    } else {
      preNodes.setLagIfsList(nodesDb.getLagIfsList());
    }

    Nodes retNodes = setRelatedNodes(inData, preNodes, equipments);

    List<Nodes> ret = setOppositeNodes(inData, oppoNodeDbList);

    ret.add(retNodes);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Setting up related information for extended device information.
   *
   * @param inData
   *          Leaf/Spine device expansioin information
   * @param nodes
   *          device information
   * @param equipments
   *          model information
   * @return device information (configure the physical IF info, LagIF info and breakoutIF info)
   * @throws IllegalArgumentException
   *           physical IF name generation failure, etc.
   */
  private static Nodes setRelatedNodes(AddNode inData, Nodes nodes, Equipments equipments)
      throws IllegalArgumentException {
    logger.trace(CommonDefinitions.START);
    logger.debug(inData + " , " + nodes + " , " + equipments);

    NodeInterface nodeInterface = inData.getCreateNode().getIfInfo();

    Nodes ret = nodes;

    List<Set<BreakoutIfs>> breakoutIfsSetList = createBreakoutIfSetList(
        inData.getCreateNode().getIfInfo().getBreakoutBaseIfs(), ret);

    relateBreakoutIftoPhysicalIf(breakoutIfsSetList, ret.getPhysicalIfsList());

    for (InternalLinkInfo internalLinkIfs : nodeInterface.getInternalLinkIfs()) {

      InternalLinkIf internalLinkIf = internalLinkIfs.getInternalLinkIf();

      ret = setRelatedNodesCommon(inData.getCreateNode().getNodeId(), internalLinkIf, equipments, ret);

    } 

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Device Related Information Configuration (Physical IF[IF Name Configuration]/LagIF).
   *
   * @param nodeId
   *          device ID
   * @param internalLinkIf
   *          REST specified internal link IF information
   * @param equipments
   *          model information (DB)
   * @param nodes
   *          device information (DB)
   * @return device information in which related information is configured
   * @throws IllegalArgumentException
   *           physical IF name generation failure, etc.
   */
  private static Nodes setRelatedNodesCommon(String nodeId,
      InternalLinkIf internalLinkIf, Equipments equipments, Nodes nodes) throws IllegalArgumentException {
    logger.trace(CommonDefinitions.START);
    logger.debug(nodeId + " , " + internalLinkIf + " , " + equipments + " , " + nodes);

    Nodes ret = nodes;

    String targetNodeId = nodes.getNode_id();

    if (internalLinkIf.getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {

      for (PhysicalIfs physi : ret.getPhysicalIfsList()) {
        if (physi.getPhysical_if_id().equals(internalLinkIf.getIfId())) {


          physi.setIf_name(createPhysicalIfName(internalLinkIf.getIfId(), internalLinkIf.getLinkSpeed(), equipments));
          physi.setIf_speed(internalLinkIf.getLinkSpeed());
          physi.setIpv4_address(internalLinkIf.getLinkIpAddress());
          physi.setIpv4_prefix(internalLinkIf.getPrefix());
        }
      }

    } else if (internalLinkIf.getIfType().equals(CommonDefinitions.IF_TYPE_LAG_IF)) {

      LagIfs lagIfs = new LagIfs();
      lagIfs.setNode_id(targetNodeId);
      if (equipments.getRouter_type() == 0) {
        lagIfs.setLag_if_id(getLagIfId(nodes));
      } else {
        lagIfs.setLag_if_id(internalLinkIf.getIfId());
      }
      lagIfs.setFc_lag_if_id(internalLinkIf.getIfId());
      lagIfs.setIf_name(toLagIfName(equipments.getLag_prefix(), lagIfs.getLag_if_id()));
      lagIfs.setMinimum_link_num(internalLinkIf.getLagMember().size());
      if (internalLinkIf.getLinkSpeed() != null) {
        lagIfs.setIf_speed(internalLinkIf.getLinkSpeed());
      } else {
        GET_SPEED: for (PhysicalIfs physi : nodes.getPhysicalIfsList()) {
          for (BreakoutIfs boif : physi.getBreakoutIfsList()) {
            for (InterfaceInfo lagMember : internalLinkIf.getLagMember()) {
              if (boif.getBreakout_if_id().equals(lagMember.getIfId())) {
                lagIfs.setIf_speed(boif.getSpeed());
                break GET_SPEED;
              }
            }
          }
        }
        if (lagIfs.getIf_speed() == null) {
          logger.warn("Get speed error");
          throw new IllegalArgumentException();
        }
      }
      lagIfs.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);
      lagIfs.setIpv4_address(internalLinkIf.getLinkIpAddress());
      lagIfs.setIpv4_prefix(internalLinkIf.getPrefix());

      Set<LagMembers> lagMembersList = new HashSet<>();
      for (InterfaceInfo memberIf : internalLinkIf.getLagMember()) {
        LagMembers lagMemberDb = new LagMembers();
        lagMemberDb.setNode_id(targetNodeId);
        lagMemberDb.setLag_if_id(lagIfs.getLag_if_id());
        if (memberIf.getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
          lagMemberDb.setPhysical_if_id(memberIf.getIfId());
          for (PhysicalIfs physi : ret.getPhysicalIfsList()) {
            if (physi.getPhysical_if_id().equals(memberIf.getIfId())) {
              physi.setIf_name(createPhysicalIfName(memberIf.getIfId(), internalLinkIf.getLinkSpeed(), equipments));
              physi.setIf_speed(internalLinkIf.getLinkSpeed());
              break; 
            }
          }
        } else { 
          lagMemberDb.setBreakout_if_id(memberIf.getIfId());
        }
        lagMembersList.add(lagMemberDb);
      }
      lagIfs.setLagMembersList(lagMembersList);
      Set<LagIfs> lagIfsSet = ret.getLagIfsList();
      if (lagIfsSet == null) {
        lagIfsSet = new HashSet<>();
      }
      lagIfsSet.add(lagIfs);

    } else {

      for (PhysicalIfs physi : ret.getPhysicalIfsList()) {
        for (BreakoutIfs boif : physi.getBreakoutIfsList()) {
          if (boif.getBreakout_if_id().equals(internalLinkIf.getIfId())) {
            boif.setIpv4_address(internalLinkIf.getLinkIpAddress());
            boif.setIpv4_prefix(internalLinkIf.getPrefix());
            break;
          }
        }
      }
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * breakoutIF Set List Creation.
   * Generating the list of breakoutIF information set in DB-POJO format based on the breakoutIF information (REST) specified from FC
   *
   * @param breakoutBaseIfList
   *          breakoutIF information (REST) specified from FC
   * @param nodes
   *          device information (DB)
   * @return generted list of breakoutIF information set in B-POJO format
   */
  private static List<Set<BreakoutIfs>> createBreakoutIfSetList(List<BreakoutBaseIf> breakoutBaseIfList, Nodes nodes) {
    logger.trace(CommonDefinitions.START);
    logger.debug(breakoutBaseIfList + ", " + nodes);

    List<Set<BreakoutIfs>> breakoutIfsSetList = new ArrayList<>();
    for (BreakoutBaseIf breakoutBaseIf : breakoutBaseIfList) {
      Set<BreakoutIfs> breakoutIfsSet = new HashSet<>();
      int breakoutIfIdx = 0;
      String basePhysicalIfId = breakoutBaseIf.getBasePhysicalIfId();
      String speed = breakoutBaseIf.getSpeed();
      for (String breakoutIfId : breakoutBaseIf.getBreakoutIfIds()) {
        BreakoutIfs breakoutIfs = new BreakoutIfs();
        breakoutIfs.setNode_id(nodes.getNode_id());
        breakoutIfs.setPhysical_if_id(basePhysicalIfId);
        breakoutIfs.setBreakout_if_id(breakoutIfId);
        breakoutIfs.setSpeed(speed);
        breakoutIfs.setBreakout_if_index(breakoutIfIdx);
        breakoutIfs.setIf_name(toBreakoutIfName(nodes, basePhysicalIfId, breakoutIfId, speed, breakoutIfIdx));
        breakoutIfs.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);
        breakoutIfsSet.add(breakoutIfs);
        breakoutIfIdx++;
      }
      breakoutIfsSetList.add(breakoutIfsSet);
    }
    logger.debug(breakoutIfsSetList);
    logger.trace(CommonDefinitions.END);
    return breakoutIfsSetList;
  }

  /**
   * Setting each breakoutIF information set to the physical IF information to be devided.
   *
   * @param breakoutIfsSetList
   *          breakoutIF information of which this device has
   * @param physicalSet
   *          physical IF information set of which this device has
   * @return physical IF information set held by the device of which breakoutIF Set is configured in related physical data
   * @throws IllegalArgumentException
   *           when there is any breakoutIF information which has no related pysical IF inofrmation
   */
  private static Set<PhysicalIfs> relateBreakoutIftoPhysicalIf(List<Set<BreakoutIfs>> breakoutIfsSetList,
      Set<PhysicalIfs> physicalSet) throws IllegalArgumentException {
    logger.trace(CommonDefinitions.START);
    logger.debug(breakoutIfsSetList + " , " + physicalSet);
    boolean found = false;

    for (Set<BreakoutIfs> breakoutIfsSet : breakoutIfsSetList) {
      found = false;

      String physicalIfId = breakoutIfsSet.iterator().next().getPhysical_if_id();

      for (PhysicalIfs physicalIfs : physicalSet) {
        if (physicalIfs.getPhysical_if_id().equals(physicalIfId)) {
          if (physicalIfs.getBreakoutIfsList() == null || physicalIfs.getBreakoutIfsList().isEmpty()) {
            if (physicalIfs.getBreakoutIfsList() == null) {
              logger.debug("DB data error.");
              throw new IllegalArgumentException();
            }
            physicalIfs.getBreakoutIfsList().clear();
            physicalIfs.getBreakoutIfsList().addAll(breakoutIfsSet);
          }
          found = true;
          break;
        }
      }
      if (found == false) {
        logger.debug("Not found physicalIf");
        throw new IllegalArgumentException();
      }
    }

    logger.debug(physicalSet);
    logger.trace(CommonDefinitions.END);
    return physicalSet;
  }

  /**
   * Physical IF Name Generation.
   *
   * @param physicalIfId
   *          physical IF ID
   * @param speed
   *          IF speed
   * @param equipments
   *          model information
   * @return physical IF name
   * @throws IllegalArgumentException
   *           there is no applicable information in the model information
   */
  private static String createPhysicalIfName(String physicalIfId, String speed, Equipments equipments)
      throws IllegalArgumentException {
    logger.trace(CommonDefinitions.START);
    logger.debug("physicalIfId=" + physicalIfId + " speed=" + speed + " equipments=" + equipments);

    String physicalIfName = null;

    String prefix = null;
    for (IfNameRules ifNameRules : equipments.getIfNameRulesList()) {
      if (ifNameRules.getSpeed().equals(speed)) {
        prefix = ifNameRules.getPort_prefix();
        break;
      }
    }

    String slot = null;
    for (EquipmentIfs equipmentIfs : equipments.getEquipmentIfsList()) {
      if (equipmentIfs.getPhysical_if_id().equals(physicalIfId) && equipmentIfs.getPort_speed().equals(speed)) {
        slot = equipmentIfs.getIf_slot();
        break;
      }
    }
    if (prefix != null && slot != null) {
      physicalIfName = toPhysicalIfName(prefix, slot); 
    } else {
      logger.warn("Create physical IF name error");
      throw new IllegalArgumentException();
    }

    logger.debug("physicalIfName=" + physicalIfName);
    logger.trace(CommonDefinitions.END);
    return physicalIfName;
  }

  /**
   * Setting Related Information to the Opposing Device Information.
   *
   * @param inData
   *          REST input data
   * @param oppoNodeDbList
   *          opposing device infromation list (DB)
   * @return opposing device information for DB configuration
   * @throws IllegalArgumentException
   *           physical IF name generation failure, etc.
   */
  private static List<Nodes> setOppositeNodes(AddNode inData, List<Nodes> oppoNodeDbList)
      throws IllegalArgumentException {
    logger.trace(CommonDefinitions.START);
    logger.debug(inData + " + " + oppoNodeDbList);

    List<Nodes> retList = new ArrayList<>();

    for (OppositeNodesInterface oppoIf : inData.getCreateNode().getOppositeNodes()) {

      InternalLinkIf internalLinkIf = oppoIf.getInternalLinkIf();

      Nodes oppoNode = null;
      for (Nodes oppoNodeDb : oppoNodeDbList) {
        if (oppoIf.getNodeId().equals(oppoNodeDb.getNode_id())) {
          oppoNode = oppoNodeDb;
          break;
        }
      }
      if (oppoNode == null) {
        logger.warn("Get oppositeNodes error");
        throw new IllegalArgumentException();
      }

      List<Set<BreakoutIfs>> breakoutIfsSetList = createBreakoutIfSetList(oppoIf.getBreakoutBaseIfs(), oppoNode);

      relateBreakoutIftoPhysicalIf(breakoutIfsSetList, oppoNode.getPhysicalIfsList());

      Nodes ret = setRelatedNodesCommon(oppoNode.getNode_id(), internalLinkIf, oppoNode.getEquipments(), oppoNode);

      retList.add(ret);
    }

    logger.debug(retList);
    logger.trace(CommonDefinitions.END);
    return retList;
  }

  /**
   * DB Data Mapping_Leaf Device Removal.
   *
   * @param inData
   *          Leaf device removal information list
   * @param oppoNodesListDb
   *          opposing device information list (DB)
   * @return device information list
   */
  public static List<Nodes> toNodeOppositeNodesReduced(DeleteNode inData, ArrayList<Nodes> oppoNodesListDb) {

    logger.trace(CommonDefinitions.START);
    logger.debug(inData + " + " + oppoNodesListDb);

    List<Nodes> nodeList = new ArrayList<Nodes>();

    for (OppositeNodesDeleteNode oppositeNodes : inData.getDeleteNodes().getOppositeNodes()) {

      Nodes oppoNodesDb = null;
      for (Nodes nodesDb : oppoNodesListDb) {
        if (oppositeNodes.getNodeId().equals(nodesDb.getNode_id())) {
          oppoNodesDb = (Nodes) CommonUtil.deepCopy(nodesDb);
          break;
        }
      }
      if (oppoNodesDb == null) {
        logger.debug("Not found node");
        throw new IllegalArgumentException();
      }

      Nodes nodes = new Nodes();

      nodes.setNode_id(oppositeNodes.getNodeId());

      nodes = setDeleteOppositeNodes(oppositeNodes.getInternalLinkIf().getIfInfo(), oppoNodesDb, nodes);
      nodeList.add(nodes);
    }

    logger.debug(nodeList);
    logger.trace(CommonDefinitions.END);

    return nodeList;
  }

  /**
   * Setting device related information when removing devices.
   *
   * @param interfaceInfoRest
   *          internal link information (device related information)
   * @param oppoNodesDb
   *          opposing device information (DB) in which related information are going to be set now
   * @param nodes
   *          device information (only ID is set) which is correlated to the device related information
   * @return device information (set physical IF info, LagIF info and internal link IF info)
   */
  private static Nodes setDeleteOppositeNodes(InterfaceInfo interfaceInfoRest, Nodes oppoNodesDb, Nodes nodes) {

    logger.debug("interfaceInfoRest=" + interfaceInfoRest);
    logger.debug("oppoNodesDb=" + oppoNodesDb);
    Nodes ret = nodes;

    if (interfaceInfoRest.getIfType().equals(CommonDefinitions.IF_TYPE_LAG_IF)) {

      LagIfs lagIfs = new LagIfs();

      LagIfs lagIfsDb = null;
      for (LagIfs lagIfsDbTmp : oppoNodesDb.getLagIfsList()) {
        if (interfaceInfoRest.getIfId().equals(lagIfsDbTmp.getFc_lag_if_id())) {
          lagIfsDb = lagIfsDbTmp;
          break;
        }
      }

      if (lagIfsDb == null) {
        logger.debug("Not found LAG IF ID " + interfaceInfoRest.getIfId());
        throw new IllegalArgumentException();
      }
      lagIfs.setNode_id(nodes.getNode_id());
      lagIfs.setLag_if_id(lagIfsDb.getLag_if_id());
      lagIfs.setLagMembersList(lagIfsDb.getLagMembersList());

      Set<LagIfs> lagIfsSet = new HashSet<>();
      lagIfsSet.add(lagIfs); 

      Set<PhysicalIfs> physicalIfsSet = new HashSet<>();

      for (LagMembers lagMembers : lagIfsDb.getLagMembersList()) {
        logger.debug("lagMembers=" + lagMembers);

        if (lagMembers.getPhysical_if_id() != null) {
          PhysicalIfs physicalIfs = new PhysicalIfs();
          physicalIfs.setNode_id(nodes.getNode_id());
          physicalIfs.setPhysical_if_id(lagMembers.getPhysical_if_id());
          physicalIfs.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);
          physicalIfsSet.add(physicalIfs);

        } else {

          PhysicalIfs physicalIfsDb = null;
          FOUND_BASE_PHYSICAL_IF: for (PhysicalIfs physicalIfsDbTmp : oppoNodesDb.getPhysicalIfsList()) {
            for (BreakoutIfs bifs : physicalIfsDbTmp.getBreakoutIfsList()) {
              if (lagMembers.getBreakout_if_id().equals(bifs.getBreakout_if_id())) {
                physicalIfsDb = new PhysicalIfs();
                physicalIfsDb.setIpv4_address(physicalIfsDbTmp.getIpv4_address());
                physicalIfsDb.setIpv4_prefix(physicalIfsDbTmp.getIpv4_prefix());
                physicalIfsDb.setIf_name(physicalIfsDbTmp.getIf_name());
                physicalIfsDb.setIf_speed(physicalIfsDbTmp.getIf_speed());
                physicalIfsDb.setIf_status(physicalIfsDbTmp.getIf_status());
                physicalIfsDb.setNode_id(physicalIfsDbTmp.getNode_id());
                physicalIfsDb.setPhysical_if_id(physicalIfsDbTmp.getPhysical_if_id());

                break FOUND_BASE_PHYSICAL_IF;
              }
            }
          }
          if (physicalIfsDb == null) {
            logger.debug("Not found BASE PHYSICAL IF ID " + lagMembers.getBreakout_if_id());
            throw new IllegalArgumentException();
          }

          BreakoutIfs breakoutIfs = new BreakoutIfs();
          breakoutIfs.setNode_id(nodes.getNode_id());
          breakoutIfs.setBreakout_if_id(lagMembers.getBreakout_if_id());
          breakoutIfs.setPhysical_if_id(physicalIfsDb.getPhysical_if_id());
          breakoutIfs.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);
          Set<BreakoutIfs> breakoutSet = new HashSet<>();
          breakoutSet.add(breakoutIfs);

          physicalIfsDb.setBreakoutIfsList(breakoutSet);
          physicalIfsSet.add(physicalIfsDb);
        }
      }

      ret.setLagIfsList(lagIfsSet);
      ret.setPhysicalIfsList(physicalIfsSet);

    } else if (interfaceInfoRest.getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {

      PhysicalIfs physicalIfs = new PhysicalIfs();

      physicalIfs.setNode_id(nodes.getNode_id());
      physicalIfs.setPhysical_if_id(interfaceInfoRest.getIfId());
      physicalIfs.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);
      Set<PhysicalIfs> physicalIfsSet = new HashSet<>();
      physicalIfsSet.add(physicalIfs);

      ret.setPhysicalIfsList(physicalIfsSet);
      ret.setLagIfsList(new HashSet<>());

    } else { 

      PhysicalIfs physicalIfsDb = null;
      FOUND_BASE_PHYSICAL_IF: for (PhysicalIfs physicalIfsDbTmp : oppoNodesDb.getPhysicalIfsList()) {
        for (BreakoutIfs bifs : physicalIfsDbTmp.getBreakoutIfsList()) {
          if (interfaceInfoRest.getIfId().equals(bifs.getBreakout_if_id())) {
            physicalIfsDb = physicalIfsDbTmp;
            break FOUND_BASE_PHYSICAL_IF;
          }
        }
      }
      if (physicalIfsDb == null) {
        logger.debug("Not found BASE PHYSICAL IF ID " + interfaceInfoRest.getIfId());
        throw new IllegalArgumentException();
      }

      BreakoutIfs breakoutIfs = new BreakoutIfs();
      breakoutIfs.setNode_id(nodes.getNode_id());
      breakoutIfs.setBreakout_if_id(interfaceInfoRest.getIfId());
      breakoutIfs.setPhysical_if_id(physicalIfsDb.getPhysical_if_id());
      breakoutIfs.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);
      Set<BreakoutIfs> breakoutSet = new HashSet<>();
      breakoutSet.add(breakoutIfs);

      physicalIfsDb.setBreakoutIfsList(breakoutSet);
      Set<PhysicalIfs> physicalIfsSet = new HashSet<>();
      physicalIfsSet.add(physicalIfsDb);
      ret.setPhysicalIfsList(physicalIfsSet);
      ret.setLagIfsList(new HashSet<>());
    }

    logger.debug("ret=" + ret);
    return ret;
  }

  /**
   * DB Data Mapping_Generating/changing L2VLAN IF In A Lump<br>
   * Convert L2VLAN IF information into DB storable format.
   *
   * @param input
   *          L2VLAN IF generation information
   * @param nodesDb
   *          device information
   * @param irbInstanceId
   *          IRB instance ID
   * @return L2VLAN IFDbMapper.java
   */
  public static VlanIfs toL2VlanIfCreate(CreateVlanIfs input, Nodes nodesDb, String irbInstanceId) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesDb);

    String baseIfType = input.getBaseIf().getIfType();
    String baseIfId = input.getBaseIf().getIfId();
    VlanIfs vlanIfs = new VlanIfs();
    vlanIfs.setNode_id(input.getBaseIf().getNodeId());
    vlanIfs.setVlan_if_id(input.getVlanIfId());
    if (baseIfType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
      vlanIfs.setPhysical_if_id(baseIfId);
    } else if (baseIfType.equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
      if (nodesDb.getLagIfsList() != null) {
        for (LagIfs elem : nodesDb.getLagIfsList()) {
          if (elem.getFc_lag_if_id().equals(baseIfId)) {
            vlanIfs.setLag_if_id(elem.getLag_if_id());
            break;
          }
        }
      }
    } else if (baseIfType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      vlanIfs.setBreakout_if_id(baseIfId);
    }
    vlanIfs.setIf_name(getIfName(baseIfType, baseIfId, nodesDb));
    vlanIfs.setVlan_id(input.getVlanId().toString());
    vlanIfs.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);
    if (input.getPortMode().equals(CommonDefinitions.VLAN_PORTMODE_ACCESS_STRING)) {
      vlanIfs.setPort_mode(CommonDefinitions.VLAN_PORTMODE_ACCESS);
    } else if (input.getPortMode().equals(CommonDefinitions.VLAN_PORTMODE_TRUNK_STRING)) {
      vlanIfs.setPort_mode(CommonDefinitions.VLAN_PORTMODE_TRUNK);
    }
    if (input.getQos() != null) {
      vlanIfs.setInflow_shaping_rate(input.getQos().getInflowShapingRate());
      vlanIfs.setOutflow_shaping_rate(input.getQos().getOutflowShapingRate());
      vlanIfs.setRemark_menu(input.getQos().getRemarkMenu());
      vlanIfs.setEgress_queue_menu(input.getQos().getEgressQueue());
    }
     vlanIfs.setIrb_instance_id(irbInstanceId);

    logger.debug(vlanIfs);
    logger.trace(CommonDefinitions.END);
    return vlanIfs;
  }

  /**
   * DB Data Mapping_Generating L3VLAN IF In A Lump<br>
   * Convert L3VLAN IF generation information into DB storable format.
   *
   * @param input
   *          L3VLAN IF generation information
   * @param nodesDb
   *          device information list
   * @param bgpId
   *          BGP option information ID
   * @param vrrpId
   *          VRRP option information ID
   * @return L3VLAN IF genertion information (for DB storage)
   */
  public static VlanIfs toL3VlanIfCreate(VlanIfsCreateL3VlanIf input, Nodes nodesDb, String bgpId, String vrrpId) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesDb);

    String baseIfType = input.getBaseIf().getIfType();
    String baseIfId = input.getBaseIf().getIfId();
    VlanIfs vlanIfs = new VlanIfs();
    vlanIfs.setNode_id(input.getBaseIf().getNodeId());
    vlanIfs.setVlan_if_id(input.getVlanIfId());
    if (baseIfType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
      vlanIfs.setPhysical_if_id(baseIfId);
    } else if (baseIfType.equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
      if (nodesDb.getLagIfsList() != null) {
        for (LagIfs elem : nodesDb.getLagIfsList()) {
          if (elem.getFc_lag_if_id().equals(baseIfId)) {
            vlanIfs.setLag_if_id(elem.getLag_if_id());
            break;
          }
        }
      }
    } else if (baseIfType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      vlanIfs.setBreakout_if_id(baseIfId);
    }
    vlanIfs.setIf_name(getIfName(baseIfType, baseIfId, nodesDb));
    vlanIfs.setVlan_id(input.getVlanId().toString());
    vlanIfs.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);
    vlanIfs.setIpv4_address(input.getIpv4Address());
    vlanIfs.setIpv4_prefix(input.getIpv4Prefix());
    vlanIfs.setIpv6_address(input.getIpv6Address());
    vlanIfs.setIpv6_prefix(input.getIpv6Prefix());
    vlanIfs.setMtu(input.getMtu());
    vlanIfs.setBgp_id(bgpId);
    vlanIfs.setVrrp_id(vrrpId);
    if (input.getQos() != null) {
      vlanIfs.setInflow_shaping_rate(input.getQos().getInflowShapingRate());
      vlanIfs.setOutflow_shaping_rate(input.getQos().getOutflowShapingRate());
      vlanIfs.setRemark_menu(input.getQos().getRemarkMenu());
      vlanIfs.setEgress_queue_menu(input.getQos().getEgressQueue());
    }

    if (input.getBgp() != null) {
      vlanIfs.setBgpOptionsList(new HashSet<BGPOptions>());
      BGPOptions bgpOpt = new BGPOptions();
      bgpOpt.setBgp_id(bgpId);
      if (input.getBgp().getRole().equals(CommonDefinitions.BGP_ROLE_MASTER_STRING)) {
        bgpOpt.setBgp_role(CommonDefinitions.BGP_ROLE_MASTER);
      } else if (input.getBgp().getRole().equals(CommonDefinitions.BGP_ROLE_SLAVE_STRING)) {
        bgpOpt.setBgp_role(CommonDefinitions.BGP_ROLE_SLAVE);
      }
      bgpOpt.setBgp_neighbor_as(input.getBgp().getNeighborAs());
      bgpOpt.setBgp_neighbor_ipv4_address(input.getBgp().getNeighborIpv4Address());
      bgpOpt.setBgp_neighbor_ipv6_address(input.getBgp().getNeighborIpv6Address());
      bgpOpt.setNode_id(input.getBaseIf().getNodeId());
      bgpOpt.setVlan_if_id(input.getVlanIfId());
      vlanIfs.getBgpOptionsList().add(bgpOpt);
    }

    if (input.getVrrp() != null) {
      vlanIfs.setVrrpOptionsList(new HashSet<VRRPOptions>());
      VRRPOptions vrrpOpt = new VRRPOptions();
      vrrpOpt.setVrrp_id(vrrpId);
      vrrpOpt.setVrrp_group_id(input.getVrrp().getGroupId());
      if (input.getVrrp().getRole().equals(CommonDefinitions.VRRP_ROLE_MASTER_STRING)) {
        vrrpOpt.setVrrp_role(CommonDefinitions.VRRP_ROLE_MASTER);
      } else if (input.getVrrp().getRole().equals(CommonDefinitions.VRRP_ROLE_SLAVE_STRING)) {
        vrrpOpt.setVrrp_role(CommonDefinitions.VRRP_ROLE_SLAVE);
      }
      vrrpOpt.setVrrp_virtual_ipv4_address(input.getVrrp().getVirtualIpv4Address());
      vrrpOpt.setVrrp_virtual_ipv6_address(input.getVrrp().getVirtualIpv6Address());
      vrrpOpt.setNode_id(input.getBaseIf().getNodeId());
      vrrpOpt.setVlan_if_id(input.getVlanIfId());
      vlanIfs.getVrrpOptionsList().add(vrrpOpt);
    }

    if (input.getStaticRoutes() != null) {
      vlanIfs.setStaticRouteOptionsList(new HashSet<StaticRouteOptions>());
      for (StaticRoute route : input.getStaticRoutes()) {
        StaticRouteOptions srOpt = new StaticRouteOptions();
        srOpt.setNode_id(input.getBaseIf().getNodeId());
        srOpt.setVlan_if_id(input.getVlanIfId());
        if (route.getAddressType().equals(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV4_STRING)) {
          srOpt.setStatic_route_address_type(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV4);
        } else if (route.getAddressType().equals(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV6_STRING)) {
          srOpt.setStatic_route_address_type(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV6);
        }
        srOpt.setStatic_route_destination_address(route.getAddress());
        srOpt.setStatic_route_prefix(route.getPrefix());
        srOpt.setStatic_route_nexthop_address(route.getNextHop());
        vlanIfs.getStaticRouteOptionsList().add(srOpt);
      }
    }

    logger.debug(vlanIfs);
    logger.trace(CommonDefinitions.END);
    return vlanIfs;
  }

  /**
   * Searching the each of IF information list of device information with IF ID as a key, and getting the IF name.
   *
   * @param ifType
   *          IF type
   * @param ifId
   *          IF ID
   * @param nodes
   *          device information
   * @return IF name
   */
  private static String getIfName(String ifType, String ifId, Nodes nodes) {
    String ret = null;

    if (ifType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
      for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
        if (physicalIfs.getPhysical_if_id().equals(ifId)) {
          ret = physicalIfs.getIf_name();
          break;
        }
      }
    } else if (ifType.equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
      for (LagIfs lagIfs : nodes.getLagIfsList()) {
        if (lagIfs.getFc_lag_if_id().equals(ifId)) {
          ret = lagIfs.getIf_name();
          break;
        }
      }
    } else if (ifType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      phyLoop: for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
        for (BreakoutIfs breakoutIfs : physicalIfs.getBreakoutIfsList()) {
          if (breakoutIfs.getBreakout_if_id().equals(ifId)) {
            ret = breakoutIfs.getIf_name();
            break phyLoop;
          }
        }
      }
    }

    return ret;
  }

  /**
   * DB Data Mapping_VLAN IF Change<br>
   * Converting VLAN IF change into DB storable format.
   *
   * @param input
   *          VLAN IF change information
   * @param nodeId
   *          device ID
   * @param vlanIfId
   *          VLAN IF ID
   * @return VLAN IF change information (for DB storage)
   */
  public static StaticRouteOptions toVlanIfStaticRouteChange(UpdateStaticRoute input, String nodeId, String vlanIfId) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input);

    StaticRouteOptions ret = new StaticRouteOptions();

    ret.setNode_id(nodeId);
    ret.setVlan_if_id(vlanIfId);
    if (input.getAddressType().equals(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV4_STRING)) {
      ret.setStatic_route_address_type(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV4);
    } else if (input.getAddressType().equals(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV6_STRING)) {
      ret.setStatic_route_address_type(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV6);
    }
    ret.setStatic_route_destination_address(input.getAddress());
    ret.setStatic_route_prefix(input.getPrefix());
    ret.setStatic_route_nexthop_address(input.getNextHop());

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DB Data Mapping_VLAN IF Change（QoS setting）<br>
   * Converting VLAN IF change into DB storable format.
   *
   * @param input
   *          VLAN IF change information
   * @param inputVlanIfs
   *          VLAN IF information for update
   * @return VLAN IF change information (for DB storage)
   */
  public static VlanIfs toVlanIfQosChange(QosUpdateVlanIf input, VlanIfs inputVlanIfs) {
    logger.trace(CommonDefinitions.START);
    logger.debug(input);

    VlanIfs ret = inputVlanIfs;

    if (input == null) {
      ret.setInflow_shaping_rate(null);
      ret.setOutflow_shaping_rate(null);
      ret.setEgress_queue_menu(null);
    } else {
      ret.setInflow_shaping_rate(input.getInflowShapingRate());
      ret.setOutflow_shaping_rate(input.getOutflowShapingRate());
      ret.setEgress_queue_menu(input.getEgressQueue());
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DB Data Mapping_VLAN IF Batch Change（QoS setting）<br>
   * Converting VLAN IF change into DB storable format.
   *
   * @param input
   *          Remark menu information
   * @param inputVlanIfs
   *          VLAN IF information for update
   * @return VLAN IF change information (for DB storage)
   */
  public static VlanIfs toVlanIfQosBulkChange(String input, VlanIfs inputVlanIfs) {
    logger.trace(CommonDefinitions.START);
    logger.debug("remark_menu=" + input);

    VlanIfs ret = inputVlanIfs;

    ret.setRemark_menu(input);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Checking inflow shaping rate value.
   *
   * @param input
   *          inflow shaping rate value
   * @param ifSpeed
   *          Speed of IF to which QoS is set
   * @param capability
   *          Inflow shaping possible/ impossible
   * @return Setting of inflow shaping rate to DB/EM is possible：true<br>
   *         Setting of inflow shaping rate to DB/EM is impossibe：false
   */
  public static boolean checkQosShapingRateValue(Float input, int ifSpeed, boolean capability) {

    boolean ret = true;

    if (input != null) {
      if (capability) {
        if (input >= 0 && input <= ifSpeed) {
          ret = true;
        } else {
          ret = false;
        }
      } else {
        ret = false;
      }
    }

    return ret;

  }

  /**
   * Checking remark menu value to be set to DB.
   *
   * @param input
   *          remark menu value transferred from FC
   * @param equipmentDb
   *          Defice information
   * @return Setting of remark menu value to DB/EM is possible：true<br>
   *         Setting of remark menu value to DB/EM is impossible：false
   */
  public static boolean checkQosRemarkMenuValue(String input, Equipments equipmentDb) {

    boolean ret = true;

    if (equipmentDb.getQos_remark_flg()) {
      if (input != null) {
        RemarkMenus inputMenus = new RemarkMenus();
        inputMenus.setEquipment_type_id(equipmentDb.getEquipment_type_id());
        inputMenus.setRemark_menu(input);
        if (equipmentDb.getRemarkMenusList().contains(inputMenus)) {
          ret = true;
        } else {
          ret = false;
        }
      }
    } else {
      if (input != null) {
        ret = false;
      }
    }

    return ret;

  }

  /**
   * Getting egress queue menu value to be set to DB.
   *
   * @param input
   *          Egress queue menu value transferred from FC
   * @param equipmentDb
   *          Device information
   * @return Setting of Egress queue menu to DB/EM is possible：true<br>
   *         Setting of Egress queue menu to DB/EM is impossible：false
   */
  public static boolean checkQosEgressQueueMenuValue(String input, Equipments equipmentDb) {

    boolean ret = true;

    if (equipmentDb.getQos_shaping_flg()) {
      if (input != null) {
        EgressQueueMenus inputMenus = new EgressQueueMenus();
        inputMenus.setEquipment_type_id(equipmentDb.getEquipment_type_id());
        inputMenus.setEgress_queue_menu(input);
        if (equipmentDb.getEgressQueueMenusList().contains(inputMenus)) {
          ret = true;
        } else {
          ret = false;
        }
      }
    } else {
      if (input != null) {
        ret = false;
      }
    }

    return ret;

  }

/**
 * DB Data Mapping_breakoutIF generation<br>
 * Converting the information for generating breakoutIF into DB storable format.
 *
 * @param input
 *          information for generating breakoutIF
 * @param nodes
 *          device information
 * @return information for generating breakoutIF (for DB storage)
 */
  public static List<BreakoutIfs> tobreakoutIfCreate(CreateBreakoutIf input, Nodes nodes) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodes);

    List<BreakoutIfs> ret = new ArrayList<BreakoutIfs>();

    String ifId = input.getBasePhysicalIfId();
    String ifSpeed = input.getSpeed();
    for (int i = 0; i < input.getBreakoutIfId().size(); i++) {
      BreakoutIfs breakoutIfsDb = new BreakoutIfs();
      breakoutIfsDb.setNode_id(input.getNodeId());
      breakoutIfsDb.setBreakout_if_id(input.getBreakoutIfId().get(i));
      breakoutIfsDb.setPhysical_if_id(ifId);
      breakoutIfsDb.setSpeed(input.getSpeed());
      breakoutIfsDb.setIf_name(toBreakoutIfName(nodes, ifId, input.getBreakoutIfId().get(i), ifSpeed, i));
      breakoutIfsDb.setBreakout_if_index(i);
      breakoutIfsDb.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);

      ret.add(breakoutIfsDb);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DB Data Mapping_Physical IF IP Address Change<br>
   * Converting physical IF IP address into DB storable format.
   *
   * @param input
   *          information for changing physical IF IP address
   * @param ipAddr
   *          IP address
   * @param prefix
   *          IP address prefix
   * @return information for changing physical IF IP address (for DB storage)
   */
  public static PhysicalIfs toPhysicalIfIpAddrChange(PhysicalIfs input, String ipAddr, Integer prefix) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", ipAddr=" + ipAddr + ", prefix=" + prefix);

    PhysicalIfs ret = null;

    if (input != null) {
      ret = input;
      ret.setIpv4_address(ipAddr);
      ret.setIpv4_prefix(prefix);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DB Data Mapping_LAGIF IP Address Change<br>
   * Converting the information for changing LAGIF IP address into DB storable format.
   *
   * @param input
   *          Information for changing LAGIF IP address
   * @param ipAddr
   *          IP address
   * @param prefix
   *          IP address prefix
   * @return information for changing LAGIF IP address (for DB storage)
   */
  public static LagIfs toLagIfIpAddrChange(LagIfs input, String ipAddr, Integer prefix) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", ipAddr=" + ipAddr + ", prefix=" + prefix);

    LagIfs ret = null;

    if (input != null) {
      ret = input;
      ret.setIpv4_address(ipAddr);
      ret.setIpv4_prefix(prefix);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DB Data Mapping_breakoutIF IP Address Change<br>
   * Converting the information for changing breakoutIF IP address into DB storable format.
   *
   * @param input
   *          information for changing breakoutIF IP address
   * @param ipAddr
   *          IP address
   * @param prefix
   *          IP address prefix
   * @return information for changing breakoutIF IP address (for DB storage)
   */
  public static BreakoutIfs toBreakoutIfIpAddrChange(BreakoutIfs input, String ipAddr, Integer prefix) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", ipAddr=" + ipAddr + ", prefix=" + prefix);

    BreakoutIfs ret = null;

    if (input != null) {
      ret = input;
      ret.setIpv4_address(ipAddr);
      ret.setIpv4_prefix(prefix);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DB Data Mapping_Additional service recovery<br>
   * Generating DB-POJO to be used for change processing of device information and IF information (including addition and deletion of physical interface)<br>
   * Creating conversion table from IF name before restoration to IF name after restoration.
   *
   * @param input
   *          input rest information(Additional service recovery)
   * @param inputNodes
   *          Device information to be recovered（including physical IF、LAGIF、breakoutIF）
   * @param inputVlanIfsList
   *          Vlan list registered in the device to be recovered
   * @param inputEquipments
   *          Device information after recovery（including IF information）
   * @param retAddPhysicalIfsList
   *          Generate DB-POJO <Physical IF to be added> (out parameters)
   * @param retDelPhysicalIfsList
   *          Generate DB-POJO <Physical IF to be deleted> (out parameters)
   * @param retPhysicalIfNamesMap
   *          Conversion table from recovered physical IF name to recovered physical IF name (out parameters)
   * @param retLagIfNamesMap
   *          Conversion table from recovered LAG IF name to recovered LAG IF name (out parameters)
   * @param retVlanIfsList
   *          VlanIF Information table updating POJO (out parameters)
   * @return Nodes generated DB-POJO＜node information including update IF information＞
   */
  public static Nodes toRecoverUpdate(RecoverNodeService input, Nodes inputNodes, List<VlanIfs> inputVlanIfsList,
      Equipments inputEquipments, List<PhysicalIfs> retAddPhysicalIfsList, List<PhysicalIfs> retDelPhysicalIfsList,
      Map<String, String> retPhysicalIfNamesMap, Map<String, String> retLagIfNamesMap, List<VlanIfs> retVlanIfsList) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input);
    logger.debug(inputNodes);
    logger.debug(inputVlanIfsList);
    logger.debug(inputEquipments);
    Nodes ret = new Nodes();

    ret.setNode_id(inputNodes.getNode_id());
    ret.setNode_name(inputNodes.getNode_name());
    ret.setEquipment_type_id(input.getEquipment().getEquipmentTypeId());
    ret.setManagement_if_address(inputNodes.getManagement_if_address());
    ret.setSnmp_community(inputNodes.getSnmp_community());
    ret.setNode_state(inputNodes.getNode_state());
    ret.setProvisioning(inputNodes.getProvisioning());
    ret.setPlane(inputNodes.getPlane());
    ret.setVpn_type(inputNodes.getVpn_type());
    ret.setAs_number(inputNodes.getAs_number());
    ret.setLoopback_if_address(inputNodes.getLoopback_if_address());
    ret.setUsername(input.getNode().getUsername());
    ret.setPassword(input.getNode().getPassword());
    ret.setNtp_server_address(inputNodes.getNtp_server_address());
    ret.setHost_name(inputNodes.getHost_name());
    ret.setMac_addr(input.getNode().getMacAddr());

    ret.setEquipments(inputEquipments);
    List<String> equipIfIdList = new ArrayList<String>();
    for (EquipmentIfs eqIfs : inputEquipments.getEquipmentIfsList()) {
      if (!equipIfIdList.contains(eqIfs.getPhysical_if_id())) {
        equipIfIdList.add(eqIfs.getPhysical_if_id());
      }
    }

    ret.setPhysicalIfsList(new HashSet<PhysicalIfs>());
    String ifName = null;
    for (PhysicalIfs physiIfs : inputNodes.getPhysicalIfsList()) {
      PhysicalIfs recoverPhysiIfs = null;
      for (EquipmentIfs equipIfs : inputEquipments.getEquipmentIfsList()) {
        if (equipIfs.getPhysical_if_id().equals(physiIfs.getPhysical_if_id())) {
          recoverPhysiIfs = new PhysicalIfs();
          recoverPhysiIfs.setNode_id(physiIfs.getNode_id());
          recoverPhysiIfs.setPhysical_if_id(physiIfs.getPhysical_if_id());
          equipIfIdList.remove(physiIfs.getPhysical_if_id());
          break;
        }
      }
      if (recoverPhysiIfs == null) {
        PhysicalIfs delPhysiIfs = new PhysicalIfs();
        delPhysiIfs.setNode_id(physiIfs.getNode_id());
        delPhysiIfs.setPhysical_if_id(physiIfs.getPhysical_if_id());
        retDelPhysicalIfsList.add(delPhysiIfs);
        continue;
      }
      if (physiIfs.getIf_name() != null && physiIfs.getIf_speed() != null) {
        ifName = toPhysicalIfName(physiIfs.getPhysical_if_id(), physiIfs.getIf_speed(), ret);
        recoverPhysiIfs.setIf_name(ifName);
        retPhysicalIfNamesMap.put(ifName, physiIfs.getIf_name());
        recoverPhysiIfs.setIf_speed(physiIfs.getIf_speed());
        recoverPhysiIfs.setIf_status(physiIfs.getIf_status());
        recoverPhysiIfs.setIpv4_address(physiIfs.getIpv4_address());
        recoverPhysiIfs.setIpv4_prefix(physiIfs.getIpv4_prefix());
      }

      recoverPhysiIfs.setBreakoutIfsList(new HashSet<BreakoutIfs>());
      if (physiIfs.getBreakoutIfsList() != null && !physiIfs.getBreakoutIfsList().isEmpty()) {
        String baseIfId = physiIfs.getBreakoutIfsList().iterator().next().getPhysical_if_id();
        String ifSpeedStr = physiIfs.getBreakoutIfsList().iterator().next().getSpeed();
        Integer baseIfSpeed = Integer.parseInt(ifSpeedStr.replace(CommonDefinitions.SPEED_UNIT_GIGA, ""))
            * physiIfs.getBreakoutIfsList().size();
        String baseIfSpeedStr = baseIfSpeed.toString() + CommonDefinitions.SPEED_UNIT_GIGA;
        String baseIfName = toPhysicalIfName(baseIfId, baseIfSpeedStr, ret);
        String baseIfOldName = toPhysicalIfName(baseIfId, baseIfSpeedStr, inputNodes);
        retPhysicalIfNamesMap.put(baseIfName, baseIfOldName);
        for (BreakoutIfs boIfs : physiIfs.getBreakoutIfsList()) {
          BreakoutIfs recoverBoIfs = new BreakoutIfs();
          recoverBoIfs.setNode_id(boIfs.getNode_id());
          recoverBoIfs.setBreakout_if_id(boIfs.getBreakout_if_id());
          recoverBoIfs.setPhysical_if_id(boIfs.getPhysical_if_id());
          recoverBoIfs.setSpeed(boIfs.getSpeed());
          ifName = toBreakoutIfName(ret, boIfs.getPhysical_if_id(), boIfs.getBreakout_if_id(), boIfs.getSpeed(),
              boIfs.getBreakout_if_index());
          recoverBoIfs.setIf_name(ifName);
          retPhysicalIfNamesMap.put(ifName, boIfs.getIf_name());
          recoverBoIfs.setBreakout_if_index(boIfs.getBreakout_if_index());
          recoverBoIfs.setIf_status(boIfs.getIf_status());
          recoverBoIfs.setIpv4_address(boIfs.getIpv4_address());
          recoverBoIfs.setIpv4_prefix(boIfs.getIpv4_prefix());
          recoverPhysiIfs.getBreakoutIfsList().add(recoverBoIfs);
        }
      }

      ret.getPhysicalIfsList().add(recoverPhysiIfs);
    }
    for (String equipIfId : equipIfIdList) {
      PhysicalIfs addPhysiIfs = new PhysicalIfs();
      addPhysiIfs.setNode_id(inputNodes.getNode_id());
      addPhysiIfs.setPhysical_if_id(equipIfId);
      retAddPhysicalIfsList.add(addPhysiIfs);
    }

    ret.setLagIfsList(new HashSet<LagIfs>());
    if (inputNodes.getLagIfsList() != null) {
      for (LagIfs lagIfs : inputNodes.getLagIfsList()) {
        LagIfs recoverLagIfs = new LagIfs();
        recoverLagIfs.setNode_id(lagIfs.getNode_id());
        recoverLagIfs.setLag_if_id(lagIfs.getLag_if_id());
        recoverLagIfs.setFc_lag_if_id(lagIfs.getFc_lag_if_id());
        ifName = toLagIfName(inputEquipments.getLag_prefix(), lagIfs.getLag_if_id());
        recoverLagIfs.setIf_name(ifName);
        retLagIfNamesMap.put(ifName, lagIfs.getIf_name());
        recoverLagIfs.setMinimum_link_num(lagIfs.getMinimum_link_num());
        recoverLagIfs.setIf_speed(lagIfs.getIf_speed());
        recoverLagIfs.setIf_status(lagIfs.getIf_status());
        recoverLagIfs.setIpv4_address(lagIfs.getIpv4_address());
        recoverLagIfs.setIpv4_prefix(lagIfs.getIpv4_prefix());
        ret.getLagIfsList().add(recoverLagIfs);
      }
    }

    String ifType = null;
    String ifId = null;
    for (VlanIfs vlanIfs : inputVlanIfsList) {
      VlanIfs recoverVlanIfs = new VlanIfs();
      recoverVlanIfs.setNode_id(vlanIfs.getNode_id());
      recoverVlanIfs.setVlan_if_id(vlanIfs.getVlan_if_id());
      if (vlanIfs.getPhysical_if_id() != null) {
        ifType = CommonDefinitions.IF_TYPE_PHYSICAL_IF;
        ifId = vlanIfs.getPhysical_if_id();
        recoverVlanIfs.setPhysical_if_id(ifId);
      } else if (vlanIfs.getLag_if_id() != null) {
        ifType = CommonDefinitions.IF_TYPE_LAG_IF;
        for (LagIfs elem : ret.getLagIfsList()) {
          if (elem.getLag_if_id().equals(vlanIfs.getLag_if_id())) {
            ifId = elem.getFc_lag_if_id();
            break;
          }
        }
        recoverVlanIfs.setLag_if_id(vlanIfs.getLag_if_id());
      } else if (vlanIfs.getBreakout_if_id() != null) {
        ifType = CommonDefinitions.IF_TYPE_BREAKOUT_IF;
        ifId = vlanIfs.getBreakout_if_id();
        recoverVlanIfs.setBreakout_if_id(ifId);
      }
      recoverVlanIfs.setIf_name(getIfName(ifType, ifId, ret));
      recoverVlanIfs.setIf_status(vlanIfs.getIf_status());
      recoverVlanIfs.setIpv4_address(vlanIfs.getIpv4_address());
      recoverVlanIfs.setIpv4_prefix(vlanIfs.getIpv4_prefix());
      recoverVlanIfs.setIpv6_address(vlanIfs.getIpv6_address());
      recoverVlanIfs.setIpv6_prefix(vlanIfs.getIpv6_prefix());
      recoverVlanIfs.setMtu(vlanIfs.getMtu());
      recoverVlanIfs.setPort_mode(vlanIfs.getPort_mode());
      recoverVlanIfs.setBgp_id(vlanIfs.getBgp_id());
      recoverVlanIfs.setVrrp_id(vlanIfs.getVrrp_id());

      recoverVlanIfs.setInflow_shaping_rate(vlanIfs.getInflow_shaping_rate());
      recoverVlanIfs.setOutflow_shaping_rate(vlanIfs.getOutflow_shaping_rate());
      recoverVlanIfs.setRemark_menu(vlanIfs.getRemark_menu());
      recoverVlanIfs.setEgress_queue_menu(vlanIfs.getEgress_queue_menu());
      Boolean shapingFlag = inputEquipments.getQos_shaping_flg();
      Boolean remarkFlag = inputEquipments.getQos_remark_flg();
      Boolean oldShapingFlag = inputNodes.getEquipments().getQos_shaping_flg();
      Boolean oldRemarkFlag = inputNodes.getEquipments().getQos_remark_flg();
      if (!shapingFlag.equals(oldShapingFlag) || !remarkFlag.equals(oldRemarkFlag)) {
        if (!shapingFlag.equals(oldShapingFlag)) {
          recoverVlanIfs.setInflow_shaping_rate(null);
          recoverVlanIfs.setOutflow_shaping_rate(null);
          recoverVlanIfs.setEgress_queue_menu(null);
        }
        if (!remarkFlag.equals(oldRemarkFlag)) {
          recoverVlanIfs.setRemark_menu(null);
        }
      }
      retVlanIfsList.add(recoverVlanIfs);
    }

    logger.debug(ret);
    logger.debug(retAddPhysicalIfsList);
    logger.debug(retDelPhysicalIfsList);
    logger.debug("retPhysicalIfNamesMap = " + retPhysicalIfNamesMap);
    logger.debug("retLagIfNamesMap = " + retLagIfNamesMap);
    logger.debug(retVlanIfsList);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DB data mapping L2VLAN IF batch generation/change<br>
   * Converting the dummy VLAN IF generation information in DB storable format.
   *
   * @param input
   *          VLAN IF generation information
   * @param nodesDb
   *          Device information
   * @param irbInstanceId
   *          IRB instance ID
   * @return Dummy VLAN IF generation information (for DB  storage)
   */
  public static DummyVlanIfs toDummyVlanIfCreate(CreateVlanIfs input, Nodes nodesDb, String irbInstanceId) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesDb + "," + irbInstanceId);

    DummyVlanIfs dummyVlanIfs = new DummyVlanIfs();
    dummyVlanIfs.setNode_id(nodesDb.getNode_id());
    dummyVlanIfs.setVlan_if_id(input.getVlanIfId());
    dummyVlanIfs.setVlan_id(input.getVlanId().toString());
    dummyVlanIfs.setIrb_instance_id(irbInstanceId);

    logger.debug(dummyVlanIfs);
    logger.trace(CommonDefinitions.END);
    return dummyVlanIfs;
  }

  /**
   * DB data maping L2VLAN IF batch generatin/change<br>
   * Converting the IRB instance inforation generation information in DB storable format.
   *
   * @param input
   *          VLAN IF generation information
   * @param nodesDb
   *          Device information
   * @param irbInstanceId
   *          IRB instance ID
   * @return IRB instance generation information (for DB storage)
   */
  public static IRBInstanceInfo toIrbInstanceInfoCreate(CreateVlanIfs input, Nodes nodesDb, String irbInstanceId) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesDb + "," + irbInstanceId);

    IRBInstanceInfo irbInstance = new IRBInstanceInfo();
    irbInstance.setNode_id(nodesDb.getNode_id());
    irbInstance.setVlan_id(input.getVlanId().toString());
    irbInstance.setIrb_instance_id(irbInstanceId);
    irbInstance.setIrb_vni(input.getIrbValue().getVni().toString());
    irbInstance.setIrb_ipv4_address(input.getIrbValue().getIpv4Address());
    irbInstance.setIrb_ipv4_prefix(input.getIrbValue().getIpv4Prefix());
    irbInstance.setVirtual_gateway_address(input.getIrbValue().getVirtualGatewayAddress());

    logger.debug(irbInstance);
    logger.trace(CommonDefinitions.END);
    return irbInstance;
  }

  /**
   * DB data mapping L2VLAN IF batch generation/ change<br>
   * Converting the L2VLAN IF generation information from updated information into DB storable format.
   *
   * @param input
   *          L2VLAN IF update information
   * @param nodesDb
   *          Device information
   * @param irbInstanceId
   *          IRB instance ID
   * @param vlanId
   *          VLAN ID
   * @return L2VLAN IF generation information (for DB storage)
   */
  public static VlanIfs toL2VlanIfCreateFromUpdate(UpdateVlanIfs input, Nodes nodesDb, String irbInstanceId,
      String vlanId) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesDb);

    String baseIfType = input.getBaseIf().getIfType();
    String baseIfId = input.getBaseIf().getIfId();
    VlanIfs vlanIfs = new VlanIfs();
    vlanIfs.setNode_id(input.getBaseIf().getNodeId());
    vlanIfs.setVlan_if_id(input.getVlanIfId());
    if (baseIfType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
      vlanIfs.setPhysical_if_id(baseIfId);
    } else if (baseIfType.equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
      if (nodesDb.getLagIfsList() != null) {
        for (LagIfs elem : nodesDb.getLagIfsList()) {
          if (elem.getFc_lag_if_id().equals(baseIfId)) {
            vlanIfs.setLag_if_id(elem.getLag_if_id());
            break;
          }
        }
      }
    } else if (baseIfType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      vlanIfs.setBreakout_if_id(baseIfId);
    }
    vlanIfs.setIf_name(getIfName(baseIfType, baseIfId, nodesDb));
    vlanIfs.setVlan_id(vlanId);
    vlanIfs.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);
    if (input.getPortMode().equals(CommonDefinitions.VLAN_PORTMODE_ACCESS_STRING)) {
      vlanIfs.setPort_mode(CommonDefinitions.VLAN_PORTMODE_ACCESS);
    } else if (input.getPortMode().equals(CommonDefinitions.VLAN_PORTMODE_TRUNK_STRING)) {
      vlanIfs.setPort_mode(CommonDefinitions.VLAN_PORTMODE_TRUNK);
    }
    if (input.getQos() != null) {
      vlanIfs.setInflow_shaping_rate(input.getQos().getInflowShapingRate());
      vlanIfs.setOutflow_shaping_rate(input.getQos().getOutflowShapingRate());
      vlanIfs.setRemark_menu(input.getQos().getRemarkMenu());
      vlanIfs.setEgress_queue_menu(input.getQos().getEgressQueue());
    }
    vlanIfs.setIrb_instance_id(irbInstanceId);

    logger.debug(vlanIfs);
    logger.trace(CommonDefinitions.END);
    return vlanIfs;
  }
}
