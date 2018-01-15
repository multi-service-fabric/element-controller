/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.convert;

import static msf.ecmm.convert.LogicalPhysicalConverter.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.db.pojo.BGPOptions;
import msf.ecmm.db.pojo.BootErrorMessages;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.EquipmentIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.IfNameRules;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.LagMembers;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.NodesStartupNotification;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.StaticRouteOptions;
import msf.ecmm.db.pojo.VRRPOptions;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.ope.control.NodeAdditionState;
import msf.ecmm.ope.receiver.pojo.AddNode;
import msf.ecmm.ope.receiver.pojo.CreateBreakoutIf;
import msf.ecmm.ope.receiver.pojo.CreateLagInterface;
import msf.ecmm.ope.receiver.pojo.DeleteNode;
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
import msf.ecmm.ope.receiver.pojo.parts.StaticRoute;
import msf.ecmm.ope.receiver.pojo.parts.UpdateStaticRoute;
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
   * Convert LagIF generation information into DB storable format.
   *
   * @param input
   *          LagIf generation information
   * @param nodes
   *          device information
   * @return information for generating LagIF (for DB storage)
   */
  public static LagIfs toLagIfCreate(CreateLagInterface input, Nodes nodes) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodes);

    LagIfs ret = new LagIfs();

    String lagIfName = toLagIfName(nodes.getEquipments().getLag_prefix(), input.getLagIf().getLagIfId());

    ret.setNode_id(nodes.getNode_id());
    ret.setLag_if_id(input.getLagIf().getLagIfId());
    ret.setIf_name(lagIfName);
    ret.setMinimum_link_num(input.getLagIf().getPhysicalIfs().size());
    ret.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);

    Set<LagMembers> membersList = new HashSet<LagMembers>();
    List<String> speedList = new ArrayList<String>();
    for (PhysicalIfsCreateLagIf phys : input.getLagIf().getPhysicalIfs()) {
      LagMembers members = new LagMembers();
      members.setNode_id(nodes.getNode_id());
      members.setLag_if_id(input.getLagIf().getLagIfId());
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
    } else {
      ret.setEvpn_capability(false);
      ret.setL2vpn_capability(false);
      ret.setL3vpn_capability(false);
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
   * DB Data Mapping_Device Extention; Convert device information into DB storable format.
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

    for (InternalLinkInfo internalLinkIfs : nodeInterface.getInternalLinkIfs()) {

      InternalLinkIf internalLinkIf = internalLinkIfs.getInternalLinkIf();

      ret = setRelatedNodesCommon(inData.getCreateNode().getNodeId(),
          inData.getCreateNode().getIfInfo().getBreakoutBaseIfs(), internalLinkIf, equipments, ret);

    } 

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Device Related Information Configuration (Physical IF[IF Name Configuration]/LagIF/breakoutIF).
   *
   * @param nodeId
   *          device ID
   * @param breakoutBaseIfList
   *          REST specified  breakoutIF information
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
  private static Nodes setRelatedNodesCommon(String nodeId, List<BreakoutBaseIf> breakoutBaseIfList,
      InternalLinkIf internalLinkIf, Equipments equipments, Nodes nodes) throws IllegalArgumentException {
    logger.trace(CommonDefinitions.START);
    logger.debug(nodeId + " , " + breakoutBaseIfList + " , " + internalLinkIf + " , " + equipments + " , " + nodes);

    Nodes ret = nodes;

    String targetNodeId = nodes.getNode_id();

    List<Set<BreakoutIfs>> breakoutIfsSetList = createBreakoutIfSetList(breakoutBaseIfList, nodes);

    relateBreakoutIftoPhysicalIf(breakoutIfsSetList, nodes.getPhysicalIfsList());

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
      lagIfs.setLag_if_id(internalLinkIf.getIfId());
      lagIfs.setIf_name(toLagIfName(equipments.getLag_prefix(), internalLinkIf.getIfId()));
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
        lagMemberDb.setLag_if_id(internalLinkIf.getIfId());
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
        breakoutIfs.setIf_name(
            LogicalPhysicalConverter.toBreakoutIfName(nodes, basePhysicalIfId, breakoutIfId, speed, breakoutIfIdx));
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

      Nodes ret = setRelatedNodesCommon(oppoNode.getNode_id(), oppoIf.getBreakoutBaseIfs(), internalLinkIf,
          oppoNode.getEquipments(), oppoNode);
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

      lagIfs.setNode_id(nodes.getNode_id());
      lagIfs.setLag_if_id(interfaceInfoRest.getIfId());

      LagIfs lagIfsDb = null;
      for (LagIfs lagIfsDbTmp : oppoNodesDb.getLagIfsList()) {
        if (interfaceInfoRest.getIfId().equals(lagIfsDbTmp.getLag_if_id())) {
          lagIfsDb = lagIfsDbTmp;
          break;
        }
      }
      if (lagIfsDb == null) {
        logger.debug("Not found LAG IF ID " + interfaceInfoRest.getIfId());
        throw new IllegalArgumentException();
      }
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
   * @return L2VLAN IF generation information (for DB storage)
   */
  public static VlanIfs toL2VlanIfCreate(CreateVlanIfs input, Nodes nodesDb) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesDb);

    VlanIfs vlanIfs = new VlanIfs();
    vlanIfs.setNode_id(input.getBaseIf().getNodeId());
    vlanIfs.setVlan_if_id(input.getVlanIfId());
    if (input.getBaseIf().getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
      vlanIfs.setPhysical_if_id(input.getBaseIf().getIfId());
    } else if (input.getBaseIf().getIfType().equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
      vlanIfs.setLag_if_id(input.getBaseIf().getIfId());
    } else if (input.getBaseIf().getIfType().equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      vlanIfs.setBreakout_if_id(input.getBaseIf().getIfId());
    }
    vlanIfs.setIf_name(getIfName(input.getBaseIf().getIfType(), input.getBaseIf().getIfId(), nodesDb));
    vlanIfs.setVlan_id(input.getVlanId().toString());
    vlanIfs.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);
    if (input.getPortMode().equals(CommonDefinitions.VLAN_PORTMODE_ACCESS_STRING)) {
      vlanIfs.setPort_mode(CommonDefinitions.VLAN_PORTMODE_ACCESS);
    } else if (input.getPortMode().equals(CommonDefinitions.VLAN_PORTMODE_TRUNK_STRING)) {
      vlanIfs.setPort_mode(CommonDefinitions.VLAN_PORTMODE_TRUNK);
    }

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
   *          VRRP opetion information ID
   * @return L3VLAN IF genertion information (for DB storage)
   */
  public static VlanIfs toL3VlanIfCreate(VlanIfsCreateL3VlanIf input, Nodes nodesDb, String bgpId, String vrrpId) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesDb);

    VlanIfs vlanIfs = new VlanIfs();
    vlanIfs.setNode_id(input.getBaseIf().getNodeId());
    vlanIfs.setVlan_if_id(input.getVlanIfId());
    if (input.getBaseIf().getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
      vlanIfs.setPhysical_if_id(input.getBaseIf().getIfId());
    } else if (input.getBaseIf().getIfType().equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
      vlanIfs.setLag_if_id(input.getBaseIf().getIfId());
    } else if (input.getBaseIf().getIfType().equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      vlanIfs.setBreakout_if_id(input.getBaseIf().getIfId());
    }
    vlanIfs.setIf_name(getIfName(input.getBaseIf().getIfType(), input.getBaseIf().getIfId(), nodesDb));
    vlanIfs.setVlan_id(input.getVlanId().toString());
    vlanIfs.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);
    vlanIfs.setIpv4_address(input.getIpv4Address());
    vlanIfs.setIpv4_prefix(input.getIpv4Prefix());
    vlanIfs.setIpv6_address(input.getIpv6Address());
    vlanIfs.setIpv6_prefix(input.getIpv6Prefix());
    vlanIfs.setMtu(input.getMtu());
    vlanIfs.setBgp_id(bgpId);
    vlanIfs.setVrrp_id(vrrpId);

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
   * Search the each of IF information list of device information with IF ID as a key, and acquire the IF name.
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
    String ret = "";

    if (ifType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
      for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
        if (physicalIfs.getPhysical_if_id().equals(ifId)) {
          ret = physicalIfs.getIf_name();
          break;
        }
      }
    } else if (ifType.equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
      for (LagIfs lagIfs : nodes.getLagIfsList()) {
        if (lagIfs.getLag_if_id().equals(ifId)) {
          ret = lagIfs.getIf_name();
          break;
        }
      }
    } else if (ifType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
        for (BreakoutIfs breakoutIfs : physicalIfs.getBreakoutIfsList()) {
          if (breakoutIfs.getBreakout_if_id().equals(ifId)) {
            ret = breakoutIfs.getIf_name();
            break;
          }
        }
        if (!ret.isEmpty()) {
          break;
        }
      }
    }

    return ret;
  }

  /**
   * DB Data Mapping_VLAN IF Change<br>
   * Convert VLAN IF change into DB storable format.
   *
   * @param input
   *          VLAN IF change information
   * @param nodeId
   *          device ID
   * @param vlanIfId
   *          VLAN IF ID
   * @return VLAN IF change information (for DB storage)
   */
  public static StaticRouteOptions toVlanIfChange(UpdateStaticRoute input, String nodeId, String vlanIfId) {

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
   * DB Data Mapping_breakoutIF generation<br>
   * Convert the information for generating breakoutIF into DB storable format.
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
      breakoutIfsDb.setIf_name(
          LogicalPhysicalConverter.toBreakoutIfName(nodes, ifId, input.getBreakoutIfId().get(i), ifSpeed, i));
      breakoutIfsDb.setIf_status(CommonDefinitions.IF_STATE_UNKNOWN);

      ret.add(breakoutIfsDb);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * DB Data Mapping_Physical IF IP Address Change<br>
   * Convert physical IF IP address into DB storable format.
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
   * Convert the information for changing LAGIF IP address into DB storable format.
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
   * Convert the information for changing breakoutIF IP address into DB storable format.
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
}
