/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.convert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.DummyVlanIfs;
import msf.ecmm.db.pojo.EquipmentIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.IRBInstanceInfo;
import msf.ecmm.db.pojo.IfNameRules;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.LagMembers;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.emctrl.pojo.BLeafAddDelete;
import msf.ecmm.emctrl.pojo.BetweenClustersLinkAddDelete;
import msf.ecmm.emctrl.pojo.BreakoutIfAddDelete;
import msf.ecmm.emctrl.pojo.CeLagAddDelete;
import msf.ecmm.emctrl.pojo.CeLagIfsChange;
import msf.ecmm.emctrl.pojo.IfStatusUpdate;
import msf.ecmm.emctrl.pojo.InternalLinkAddDelete;
import msf.ecmm.emctrl.pojo.InternalLinkLagIfsChange;
import msf.ecmm.emctrl.pojo.L2SliceAddDelete;
import msf.ecmm.emctrl.pojo.L3SliceAddDelete;
import msf.ecmm.emctrl.pojo.LeafAddDelete;
import msf.ecmm.emctrl.pojo.RecoverUpdateNode;
import msf.ecmm.emctrl.pojo.RecoverUpdateService;
import msf.ecmm.emctrl.pojo.SpineAddDelete;
import msf.ecmm.emctrl.pojo.UpdateNodeInfo;
import msf.ecmm.emctrl.pojo.parts.Anycast;
import msf.ecmm.emctrl.pojo.parts.AttributeOperation;
import msf.ecmm.emctrl.pojo.parts.BreakoutIf;
import msf.ecmm.emctrl.pojo.parts.CeInterface;
import msf.ecmm.emctrl.pojo.parts.CeLagInterface;
import msf.ecmm.emctrl.pojo.parts.Clag;
import msf.ecmm.emctrl.pojo.parts.ClagBackup;
import msf.ecmm.emctrl.pojo.parts.ClagHandOverInterface;
import msf.ecmm.emctrl.pojo.parts.ClagPeer;
import msf.ecmm.emctrl.pojo.parts.ClusterLink;
import msf.ecmm.emctrl.pojo.parts.Cp;
import msf.ecmm.emctrl.pojo.parts.Device;
import msf.ecmm.emctrl.pojo.parts.DeviceLeaf;
import msf.ecmm.emctrl.pojo.parts.DummyVlan;
import msf.ecmm.emctrl.pojo.parts.Equipment;
import msf.ecmm.emctrl.pojo.parts.EquipmentWithOperation;
import msf.ecmm.emctrl.pojo.parts.InterfaceNames;
import msf.ecmm.emctrl.pojo.parts.InterfaceStatusUpdate;
import msf.ecmm.emctrl.pojo.parts.InternalInterface;
import msf.ecmm.emctrl.pojo.parts.InternalInterfaceMember;
import msf.ecmm.emctrl.pojo.parts.Irb;
import msf.ecmm.emctrl.pojo.parts.L2L3VpnAs;
import msf.ecmm.emctrl.pojo.parts.L2L3VpnBgp;
import msf.ecmm.emctrl.pojo.parts.L2L3VpnNeighbor;
import msf.ecmm.emctrl.pojo.parts.L2Vpn;
import msf.ecmm.emctrl.pojo.parts.L3SliceBgp;
import msf.ecmm.emctrl.pojo.parts.L3SliceStatic;
import msf.ecmm.emctrl.pojo.parts.L3Vni;
import msf.ecmm.emctrl.pojo.parts.L3Vpn;
import msf.ecmm.emctrl.pojo.parts.LagMemberIf;
import msf.ecmm.emctrl.pojo.parts.LeafDevice;
import msf.ecmm.emctrl.pojo.parts.LeafInterface;
import msf.ecmm.emctrl.pojo.parts.Loopback;
import msf.ecmm.emctrl.pojo.parts.LoopbackInterface;
import msf.ecmm.emctrl.pojo.parts.ManagementInterface;
import msf.ecmm.emctrl.pojo.parts.MultiHoming;
import msf.ecmm.emctrl.pojo.parts.Ntp;
import msf.ecmm.emctrl.pojo.parts.Ospf;
import msf.ecmm.emctrl.pojo.parts.OspfAddNode;
import msf.ecmm.emctrl.pojo.parts.OspfVirtualLink;
import msf.ecmm.emctrl.pojo.parts.PhysicalIpAddress;
import msf.ecmm.emctrl.pojo.parts.Qos;
import msf.ecmm.emctrl.pojo.parts.Range;
import msf.ecmm.emctrl.pojo.parts.Route;
import msf.ecmm.emctrl.pojo.parts.Snmp;
import msf.ecmm.emctrl.pojo.parts.Track;
import msf.ecmm.emctrl.pojo.parts.TrackInterface;
import msf.ecmm.emctrl.pojo.parts.VirtualGateway;
import msf.ecmm.emctrl.pojo.parts.Vrf;
import msf.ecmm.emctrl.pojo.parts.Vrrp;
import msf.ecmm.emctrl.pojo.parts.XmlFloatElement;
import msf.ecmm.emctrl.pojo.parts.XmlStringElement;
import msf.ecmm.ope.receiver.pojo.AddNode;
import msf.ecmm.ope.receiver.pojo.BulkCreateL2VlanIf;
import msf.ecmm.ope.receiver.pojo.BulkCreateL3VlanIf;
import msf.ecmm.ope.receiver.pojo.BulkDeleteL2VlanIf;
import msf.ecmm.ope.receiver.pojo.BulkDeleteL3VlanIf;
import msf.ecmm.ope.receiver.pojo.BulkUpdateL2VlanIf;
import msf.ecmm.ope.receiver.pojo.BulkUpdateL3VlanIf;
import msf.ecmm.ope.receiver.pojo.ChangeNode;
import msf.ecmm.ope.receiver.pojo.CreateBetweenClustersLink;
import msf.ecmm.ope.receiver.pojo.CreateBreakoutIf;
import msf.ecmm.ope.receiver.pojo.CreateLagInterface;
import msf.ecmm.ope.receiver.pojo.DeleteBreakoutIf;
import msf.ecmm.ope.receiver.pojo.DeleteNode;
import msf.ecmm.ope.receiver.pojo.RecoverNodeService;
import msf.ecmm.ope.receiver.pojo.parts.BaseIfInfo;
import msf.ecmm.ope.receiver.pojo.parts.BreakoutBaseIf;
import msf.ecmm.ope.receiver.pojo.parts.CreateNode;
import msf.ecmm.ope.receiver.pojo.parts.CreateVlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentAddNode;
import msf.ecmm.ope.receiver.pojo.parts.InterfaceInfo;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkIf;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkInfo;
import msf.ecmm.ope.receiver.pojo.parts.L2VlanOption;
import msf.ecmm.ope.receiver.pojo.parts.L3VlanOption;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodesDeleteNode;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodesInterface;
import msf.ecmm.ope.receiver.pojo.parts.PhysicalIfsCreateLagIf;
import msf.ecmm.ope.receiver.pojo.parts.QosValues;
import msf.ecmm.ope.receiver.pojo.parts.RemoveUpdateVlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.StaticRoute;
import msf.ecmm.ope.receiver.pojo.parts.TrackingIf;
import msf.ecmm.ope.receiver.pojo.parts.UpdateStaticRoute;
import msf.ecmm.ope.receiver.pojo.parts.UpdateVlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsBulkUpdate;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsCreateL3VlanIf;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsDeleteVlanIf;

/**
 * EM data mapping.
 *
 */
public class EmMapper {

  /** logger. */
  private static final MsfLogger logger = new MsfLogger();

  /**
   * EM Data Mapping_Leaf Device Extention [Device]; Convert Leaf device extention information (device) into EM transmittable format.
   *
   * @param addNode
   *          Leaf device extention information
   * @param ecmainIpaddr
   *          IP address of EC
   * @param nodesListDbMapper
   *          device information list (DB mapper) (device to be extended/opposing device)
   * @param internalLinkVlanId
   *          Internal link VLANID
   * @return Leaf device extention information (device) (for sending to EM)
   * @throws IllegalArgumentException
   *           Device information entered from FC does not exist in DB.
   */
  public static LeafAddDelete toLeafInfoNodeCreate(AddNode addNode, String ecmainIpaddr, List<Nodes> nodesListDbMapper,
      int internalLinkVlanId) throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(addNode + ",  ecmainIpaddr=" + ecmainIpaddr + " , " + nodesListDbMapper);

    LeafAddDelete ret = new LeafAddDelete();
    ret.setName("leaf");

    Device device = addNodeInfo(addNode.getEquipment(), addNode.getCreateNode(), ecmainIpaddr, nodesListDbMapper,
        internalLinkVlanId);

    OspfAddNode ospf = new OspfAddNode();
    ospf.setAreaId(addNode.getCreateNode().getClusterArea());
    device.setOspfAddNode(ospf);

    if (addNode.getCreateNode().getVpn().getVpnType().equals("l3")) {
      device.setL3Vpn(createL3Vpn(addNode));
    } else {
      device.setL2Vpn(createL2Vpn(addNode));
    }
    ret.setDevice(device);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_Convert Spine Device Extention [Device]; Convert Spine device extention information (device) into EM transmittable format.
   *
   * @param addNode
   *          device extention information
   * @param ecmainIpaddr
   *          IP address of EC
   * @param nodesListDbMapper
   *          device information list (DB mapper) (device to be extended/opposing device)
   * @param internalLinkVlanId
   *          internal Link VLANID
   * @return Spine device extention information (device) (for sending to EM)
   * @throws IllegalArgumentException
   *           Model Information entered from FC does not exist in DB
   */
  public static SpineAddDelete toSpineInfoNodeCreate(AddNode addNode, String ecmainIpaddr,
      List<Nodes> nodesListDbMapper, int internalLinkVlanId) throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(addNode + ",  ecmainIpaddr=" + ecmainIpaddr + " , " + nodesListDbMapper);

    SpineAddDelete ret = new SpineAddDelete();
    ret.setName("spine");

    Device device = addNodeInfo(addNode.getEquipment(), addNode.getCreateNode(), ecmainIpaddr, nodesListDbMapper,
        internalLinkVlanId);

    OspfAddNode ospf = new OspfAddNode();
    ospf.setAreaId(addNode.getCreateNode().getClusterArea());
    device.setOspfAddNode(ospf);

    ret.setDevice(device);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_B-Leaf Device Extention [Device]; Convert B-Leaf device extention information (device) into EM transmittable format.
   *
   * @param addNode
   *          device extention information
   * @param ecmainIpaddr
   *          IP address of EC
   * @param nodesListDbMapper
   *           device information list (DB mapper) (device to be extended/opposing device)
   * @param pairNodes
   *           pair B-Leaf information (DB)
   * @param internalLinkVlanId
   *          Internal link VLANID
   * @return Spine device extention information (device) (for sending to EM)
   * @throws IllegalArgumentException
   *           Device information entered from FC does not exist in DB.
   */
  public static BLeafAddDelete toBLeafInfoNodeCreate(AddNode addNode, String ecmainIpaddr,
      List<Nodes> nodesListDbMapper, Nodes pairNodes, int internalLinkVlanId) throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(addNode + ",  ecmainIpaddr=" + ecmainIpaddr + " , " + nodesListDbMapper + " pairNodes=" + pairNodes);

    BLeafAddDelete ret = new BLeafAddDelete();
    ret.setName("b-leaf");

    if (pairNodes != null) {
      Device pairDevice = new Device();
      pairDevice.setName(pairNodes.getNode_name());
      OspfAddNode ospf = new OspfAddNode();
      ospf.setAreaId(addNode.getUpdateNode().getClusterArea());
      OspfVirtualLink virtualLink = new OspfVirtualLink();
      virtualLink.setRouterId(addNode.getCreateNode().getLoopbackInterface().getAddress());
      ospf.setVirtualLink(virtualLink);
      pairDevice.setOspfAddNode(ospf);
      ret.setPairDevice(pairDevice);
    }

    OspfAddNode ospf = new OspfAddNode();
    ospf.setAreaId(addNode.getCreateNode().getClusterArea());
    if (pairNodes != null) {
      OspfVirtualLink virtualLink = new OspfVirtualLink();
      virtualLink.setRouterId(pairNodes.getLoopback_if_address());
      ospf.setVirtualLink(virtualLink);
    }
    Range range = new Range();
    range.setAddress(addNode.getCreateNode().getRange().getAddress());
    range.setPrefix(addNode.getCreateNode().getRange().getPrefix());
    ospf.setRange(range);

    Device device = addNodeInfo(addNode.getEquipment(), addNode.getCreateNode(), ecmainIpaddr, nodesListDbMapper,
        internalLinkVlanId);

    device.setOspfAddNode(ospf);
    ret.setDevice(device);

    if (addNode.getCreateNode().getVpn().getVpnType().equals("l3")) {
      device.setL3Vpn(createL3Vpn(addNode));
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Setting Leaf/Spine/B-Leaf Device Information.
   *
   * @param addEquipment
   *          added model information
   * @param createNode
   *          added device information
   * @param nodes
   *          Device information(Specifying the device information generated by DbMapper)
   * @param ecmainIpaddr
   *          IP address
   * @param nodesListDbMapper
   *          Device information list
   * @param internalLinkVlanId
   *          Internal link VLANID
   * @return Common configuration device information
   * @throws IllegalArgumentException
   *           LagIFID/physical IF ID does not exist in DB
   */
  private static Device addNodeInfo(EquipmentAddNode addEquipment, CreateNode createNode, String ecmainIpaddr,
      List<Nodes> nodesListDbMapper, int internalLinkVlanId) throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(addEquipment + ", " + createNode + ", " + "ipadd=" + ecmainIpaddr);

    Nodes target = null;
    for (Nodes nodes : nodesListDbMapper) {
      if (createNode.getNodeId().equals(nodes.getNode_id())) {
        target = nodes;
      }
    }

    Device device = new Device();
    if (target.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
      device.setName(CommonDefinitions.COREROUTER_HOSTNAME);
    } else {
      device.setName(target.getNode_name());
    }
    Equipment equipment = new Equipment();
    equipment.setPlatform(target.getEquipments().getPlatform_name());
    equipment.setOs(target.getEquipments().getOs_name());
    equipment.setFirmware(target.getEquipments().getFirmware_version());
    equipment.setLoginid(createNode.getUsername());
    equipment.setPassword(createNode.getPassword());
    if (createNode.getProvisioning() == true) {
      equipment.addNewlyEstablish();
    }
    if (createNode.getqInQType() == null || createNode.getqInQType().equals(CommonDefinitions.Q_IN_Q_UNSUPPORT)) {
    } else if (createNode.getqInQType().equals(CommonDefinitions.Q_IN_Q_ONLY)) {
      equipment.setqInQ(CommonDefinitions.SELECTABLE_BY_NODE);
    } else if (createNode.getqInQType().equals(CommonDefinitions.Q_IN_Q_SUPPORT)) {
      equipment.setqInQ(CommonDefinitions.SELECTABLE_BY_VLAN_IF);
    }
    device.setEquipment(equipment);

    if (!createNode.getIfInfo().getBreakoutBaseIfs().isEmpty()) {
      device
          .setBreakoutIfList(createBreakoutIfList(createNode.getIfInfo().getBreakoutBaseIfs(), target.getEquipments()));
    }

    if (!createNode.getIfInfo().getInternalLinkIfs().isEmpty()) {

      device.setInternalLagList(createInternalInterfaceList(createNode.getIfInfo().getInternalLinkIfs(), createNode,
          nodesListDbMapper, target, false, internalLinkVlanId));
    }

    ManagementInterface managementInterface = new ManagementInterface();
    managementInterface.setAddress(createNode.getManagementInterface().getAddress());
    managementInterface.setPrefix(createNode.getManagementInterface().getPrefix());
    device.setManagementInterface(managementInterface);

    LoopbackInterface loopbackInterface = new LoopbackInterface();
    loopbackInterface.setAddress(createNode.getLoopbackInterface().getAddress());
    loopbackInterface.setPrefix(createNode.getLoopbackInterface().getPrefix());
    device.setLoopbackInterface(loopbackInterface);

    Snmp snmp = new Snmp();
    snmp.setServerAddress(ecmainIpaddr);
    snmp.setCommunity(createNode.getSnmpCommunity());
    device.setSnmp(snmp);

    Ntp ntp = new Ntp();
    ntp.setServerAddress(createNode.getNtpServerAddress());
    device.setNtp(ntp);

    logger.debug(device);
    logger.trace(CommonDefinitions.END);
    return device;
  }

  /**
   * Generating breakoutIF Information List (NETCONF).
   *
   * @param breaoutIfRestList
   *          breakoutIF information list (REST)
   * @param equipments
   *          model information (DB)
   * @return breakoutIF information list (NETCONF)
   */
  private static List<BreakoutIf> createBreakoutIfList(List<BreakoutBaseIf> breaoutIfRestList, Equipments equipments) {
    List<BreakoutIf> netconfBoList = new ArrayList<>();
    for (BreakoutBaseIf restBo : breaoutIfRestList) {
      BreakoutIf netconfBo = new BreakoutIf();
      int ifSpeed = Integer.parseInt(restBo.getSpeed().replaceAll("[^0-9]", "")) * restBo.getBreakoutIfIds().size();
      netconfBo.setBaseIfName(getBaseBreakoutIfName(equipments, ifSpeed, restBo.getBasePhysicalIfId()));
      netconfBo.setSpeed(restBo.getSpeed());
      netconfBo.setBreakoutNum(restBo.getBreakoutIfIds().size());
      netconfBoList.add(netconfBo);
    }
    return netconfBoList;
  }

  /**
   * Generating Internal Link IF Information (NETCONF) (for extended opposing device).
   *
   * @param internalLinkInfoRestList
   *          internal link IF information (REST)
   * @param nodes
   *          device information (DB)
   * @param oppoNodeList
   *          Opposing device information list（DB）
   * @param internalLinkVlanId
   *          Internal link VLANID
   * @return Internal link IF informaiton（NETCONF）
   */
  private static List<InternalInterface> createInternalInterfaceList(List<InternalLinkInfo> internalLinkInfoRestList,
      CreateNode createNode, List<Nodes> nodesListDbMapper, Nodes target, boolean oppoFlag, int internalLinkVlanId) {
    List<InternalInterface> internalInterfaceList = new ArrayList<>();
    int oppoInternalIfIndex = 0;
    for (InternalLinkInfo internalIfRest : internalLinkInfoRestList) {
      InternalLinkIf restIf = internalIfRest.getInternalLinkIf();
      InternalInterface internalIf = new InternalInterface();
      internalIf.setName(getIfName(restIf.getIfType(), restIf.getIfId(), target));

      if (restIf.getIfType().equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
        internalIf.setType(CommonDefinitions.IF_TYPE_PHYSICAL_IF);
      } else {
        internalIf.setType(restIf.getIfType());
      }
      internalIf.setOppositeNodeName(getOppoHostname(createNode, nodesListDbMapper, oppoInternalIfIndex, oppoFlag));
      if (restIf.getIfType().equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
        for (LagIfs lagIfs : target.getLagIfsList()) {
          if (lagIfs.getFc_lag_if_id().equals(restIf.getIfId())) {
            try {
              internalIf.setIfId(Integer.parseInt(lagIfs.getLag_if_id()));
            } catch (NumberFormatException nfe) {
              logger.debug("LagIfID is not number.");
              throw new IllegalArgumentException();
            }
            break;
          }
        }
        internalIf.setMinimumLinks((long) restIf.getLagMember().size());
      }
      internalIf.setLinkSpeed(getIfSpeed(restIf, target));
      internalIf.setAddress(restIf.getLinkIpAddress());
      internalIf.setPrefix(restIf.getPrefix());
      internalIf.setVlanId(internalLinkVlanId);
      oppoInternalIfIndex++;

      List<InternalInterfaceMember> lagMemberNetconfList = new ArrayList<InternalInterfaceMember>();
      for (InterfaceInfo lagMemberRest : restIf.getLagMember()) {
        String lagMemberIfname = getIfName(lagMemberRest.getIfType(), lagMemberRest.getIfId(), target);
        InternalInterfaceMember lagMemberNetconf = new InternalInterfaceMember();
        lagMemberNetconf.setName(lagMemberIfname);
        lagMemberNetconfList.add(lagMemberNetconf);
      }

      internalIf.setInternalInterfaceMember(lagMemberNetconfList);
      internalInterfaceList.add(internalIf);

    }
    return internalInterfaceList;
  }

  /**
   * Acquiring Opposing Device Name (NETCONF).
   *
   * @param createNode
   *          extention device information (REST)
   * @param nodesListDbMapper
   *          device information (DB)
   * @param oppoInternalIfIndex
   *          index of internal link information list
   * @param oppoFlag
   *          opposition flag
   * @return opposing device name (NETCONF)
   */
  private static String getOppoHostname(CreateNode createNode, List<Nodes> nodesListDbMapper, int oppoInternalIfIndex,
      boolean oppoFlag) {
    String oppoHostname = "";
    if (oppoFlag) {
      oppoHostname = createNode.getHostname();
    } else {
      OppositeNodesInterface oppo = createNode.getOppositeNodes().get(oppoInternalIfIndex);
      if (oppo == null) {
        logger.debug("getOppoHostname error");
        throw new IllegalArgumentException();
      }
      for (Nodes nodes : nodesListDbMapper) {
        if (oppo.getNodeId().equals(nodes.getNode_id())) {
          oppoHostname = nodes.getNode_name();
          break;
        }
      }
    }
    return oppoHostname;
  }

  /**
   * Acquiring IF Rate (NETCONF).
   *
   * @param restIf
   *          extention device internal link information (REST)
   * @param nodesListDbMapper
   *          device information (DB)
   * @return IF speed (NETCONF)
   */
  private static String getIfSpeed(InternalLinkIf restIf, Nodes target) {
    logger.debug("ifType=" + restIf.getIfType() + ", linkSpeed=" + restIf.getLinkSpeed());

    String ifSpeed = null;
    if (restIf.getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
      ifSpeed = restIf.getLinkSpeed();
    } else {
      if (restIf.getLinkSpeed() == null) {
        GET_SPEED: for (PhysicalIfs physi : target.getPhysicalIfsList()) {
          for (BreakoutIfs boif : physi.getBreakoutIfsList()) {
            if (restIf.getIfType().equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
              for (InterfaceInfo lagMember : restIf.getLagMember()) {
                if (boif.getBreakout_if_id().equals(lagMember.getIfId())) {
                  ifSpeed = boif.getSpeed();
                  break GET_SPEED;
                }
              }
            } else if (restIf.getIfType().equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
              if (boif.getBreakout_if_id().equals(restIf.getIfId())) {
                ifSpeed = boif.getSpeed();
                break GET_SPEED;
              }
            }
          }
        }
      } else {
        ifSpeed = restIf.getLinkSpeed();
      }
    }

    logger.debug("ifSpeed=" + ifSpeed);
    return ifSpeed;
  }

  /**
   * Creating L3VPN Information.
   *
   * @param addNode
   *          device information (REST)
   * @return L3VPN information
   */
  private static L3Vpn createL3Vpn(AddNode addNode) {
    logger.trace(CommonDefinitions.START);
    logger.debug(addNode);

    L3Vpn l3Vpn = null;
    if (addNode.getCreateNode().getVpn().getL3vpn() != null) {
      l3Vpn = new L3Vpn();
      L2L3VpnBgp l3VpnBgp = new L2L3VpnBgp();
      List<L2L3VpnNeighbor> l3VpnNeighborList = new ArrayList<L2L3VpnNeighbor>();
      for (String addr : addNode.getCreateNode().getVpn().getL3vpn().getBgp().getNeighbor().getAddresses()) {
        L2L3VpnNeighbor l3VpnNeighbor = new L2L3VpnNeighbor();
        l3VpnNeighbor.setAddress(addr);
        l3VpnNeighborList.add(l3VpnNeighbor);
      }
      l3VpnBgp.setVpnNeighbor(l3VpnNeighborList);
      l3VpnBgp.setCommunity(addNode.getCreateNode().getVpn().getL3vpn().getBgp().getCommunity());
      l3VpnBgp.setCommunityWildcard(addNode.getCreateNode().getVpn().getL3vpn().getBgp().getCommunityWildcard());
      l3Vpn.setBgp(l3VpnBgp);

      L2L3VpnAs l3VpnAs = new L2L3VpnAs();
      l3VpnAs.setAsNumber(Long.parseLong(addNode.getCreateNode().getVpn().getL3vpn().getAs().getAsNumber()));
      l3Vpn.setAs(l3VpnAs);
    }

    logger.debug(l3Vpn);
    logger.trace(CommonDefinitions.END);
    return l3Vpn;
  }

  /**
   * Creating L2VPN Information.
   *
   * @param addNode
   *          device informatio (REST)
   * @return L2VPN information
   */
  private static L2Vpn createL2Vpn(AddNode addNode) {
    logger.trace(CommonDefinitions.START);
    logger.debug(addNode);

    L2Vpn l2Vpn = null;
    if (addNode.getCreateNode().getVpn().getL2vpn() != null) {
      l2Vpn = new L2Vpn();
      L2L3VpnBgp l2VpnBgp = new L2L3VpnBgp();
      List<L2L3VpnNeighbor> l2VpnNeighborList = new ArrayList<L2L3VpnNeighbor>();
      for (String addr : addNode.getCreateNode().getVpn().getL2vpn().getBgp().getNeighbor().getAddresses()) {
        L2L3VpnNeighbor l2VpnNeighbor = new L2L3VpnNeighbor();
        l2VpnNeighbor.setAddress(addr);
        l2VpnNeighborList.add(l2VpnNeighbor);
      }
      l2VpnBgp.setVpnNeighbor(l2VpnNeighborList);
      l2VpnBgp.setCommunity(addNode.getCreateNode().getVpn().getL2vpn().getBgp().getCommunity());
      l2VpnBgp.setCommunityWildcard(addNode.getCreateNode().getVpn().getL2vpn().getBgp().getCommunityWildcard());
      l2Vpn.setBgp(l2VpnBgp);

      L2L3VpnAs l2VpnAs = new L2L3VpnAs();
      l2VpnAs.setAsNumber(Long.parseLong(addNode.getCreateNode().getVpn().getL2vpn().getAs().getAsNumber()));
      l2Vpn.setAs(l2VpnAs);
    }

    logger.debug(l2Vpn);
    logger.trace(CommonDefinitions.END);
    return l2Vpn;
  }

  /**
   * EM Data Mapping_Adding Internal Link; Convert additional internal link information into EM transmittable format.
   *
   * @param addNode
   *          Leaf device extention information
   * @param nodesListDbMapper
   *          device information list
   * @param internalLinkVlanId
   *          Internal link VLANID
   * @return Leaf device extention information (internal LAG) (for sending to EM)
   * @throws IllegalArgumentException
   *           Model information entered from FC does not exist in DB.
   */
  public static InternalLinkAddDelete toInternalLinkCreate(AddNode addNode, List<Nodes> nodesListDbMapper,
      int internalLinkVlanId) throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(addNode + ", " + nodesListDbMapper);

    InternalLinkAddDelete ret = new InternalLinkAddDelete();
    ret.setName("internal-link");

    List<Device> deviceList = new ArrayList<Device>();
    for (OppositeNodesInterface oppoNodeRest : addNode.getCreateNode().getOppositeNodes()) {

      Device device = null;
      for (Nodes nodes : nodesListDbMapper) {

        if (oppoNodeRest.getNodeId().equals(nodes.getNode_id())) {

          device = new Device();

          if (addNode.getCreateNode().getNodeType().equals(CommonDefinitions.NODETYPE_SPINE)) {
            device.setVpnType(nodes.getVpn_type());
          }

          device.setName(nodes.getNode_name());

          if (!oppoNodeRest.getBreakoutBaseIfs().isEmpty()) {
            device.setBreakoutIfList(createBreakoutIfList(oppoNodeRest.getBreakoutBaseIfs(), nodes.getEquipments()));
          }

          if (oppoNodeRest.getInternalLinkIf() != null) {
            List<InternalLinkInfo> internalLinkList = new ArrayList<>();
            InternalLinkInfo internalLinkInfo = new InternalLinkInfo();
            internalLinkInfo.setInternalLinkIf(oppoNodeRest.getInternalLinkIf());
            internalLinkList.add(internalLinkInfo);
            device.setInternalLagList(createInternalInterfaceList(internalLinkList, addNode.getCreateNode(),
                nodesListDbMapper, nodes, true, internalLinkVlanId));
          }
        }
      }
      if (device == null) {
        throw new IllegalArgumentException();
      }
      deviceList.add(device);
    }
    ret.setDevice(deviceList);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Generating B-Leaf Update Data.
   *
   * @param changeNode
   *          device change input data (REST)
   * @param node
   *          B-Leaf information (DB)
   * @param pairNode
   *          pair B-Leaf information (DB)
   * @return B-Leaf update data
   */
  public static BLeafAddDelete toNodeChange(ChangeNode changeNode, Nodes node, Nodes pairNode) {
    logger.trace(CommonDefinitions.START);
    logger.debug(changeNode + " , " + node + " , " + pairNode);

    BLeafAddDelete ret = null;

    if (changeNode.getAction().equals(CommonDefinitions.ADD_OSPF_ROUTE)) {
      ret = addOspfRoute(changeNode, node);
    } else if (changeNode.getAction().equals(CommonDefinitions.CHANGE_B_LEAF)) {
      ret = changeBLeaf(changeNode, node, pairNode);
    } else if (changeNode.getAction().equals(CommonDefinitions.CHANGE_LEAF)) {
      ret = changeLeaf(changeNode, node, pairNode);
    } else {
      ret = deleteOspfRoute(changeNode, node);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  private static BLeafAddDelete addOspfRoute(ChangeNode changeNode, Nodes node) {
    logger.trace(CommonDefinitions.START);

    BLeafAddDelete ret = new BLeafAddDelete();

    ret.setName("b-leaf");

    Device device = new Device();

    device.setName(node.getHost_name());
    OspfAddNode ospf = new OspfAddNode();
    ospf.setAreaId(changeNode.getNode().getClusterArea());
    Range range = new Range();
    range.setAddress(changeNode.getNode().getRange().getAddress());
    range.setPrefix(changeNode.getNode().getRange().getPrefix());
    ospf.setRange(range);
    device.setOspfAddNode(ospf);
    ret.setDevice(device);

    logger.trace(CommonDefinitions.END);
    return ret;
  }

  private static BLeafAddDelete changeBLeaf(ChangeNode changeNode, Nodes node, Nodes pairNode) {
    logger.trace(CommonDefinitions.START);

    BLeafAddDelete ret = new BLeafAddDelete();

    ret.setName("b-leaf");

    Device device = new Device();
    device.setOperation("replace");
    device.setName(node.getHost_name());
    OspfAddNode ospf = new OspfAddNode();
    ospf.setAreaId(changeNode.getNode().getClusterArea());
    OspfVirtualLink vl = new OspfVirtualLink();
    vl.setRouterId(pairNode.getLoopback_if_address());
    Range range = new Range();
    range.setAddress(changeNode.getNode().getRange().getAddress());
    range.setPrefix(changeNode.getNode().getRange().getPrefix());
    ospf.setVirtualLink(vl);
    ospf.setRange(range);
    device.setOspfAddNode(ospf);
    ret.setDevice(device);

    Device pairDevice = new Device();
    device.setOperation("replace");
    pairDevice.setName(pairNode.getHost_name());
    OspfAddNode pairOspf = new OspfAddNode();
    pairOspf.setAreaId(changeNode.getPairNode().getClusterArea());
    OspfVirtualLink pairVl = new OspfVirtualLink();
    pairVl.setRouterId(node.getLoopback_if_address());
    pairOspf.setVirtualLink(pairVl);
    pairDevice.setOspfAddNode(pairOspf);
    ret.setPairDevice(pairDevice);

    logger.trace(CommonDefinitions.END);
    return ret;
  }

  private static BLeafAddDelete changeLeaf(ChangeNode changeNode, Nodes node, Nodes pairNode) {
    logger.trace(CommonDefinitions.START);
    BLeafAddDelete ret = new BLeafAddDelete();

    ret.setName("b-leaf");

    Device device = new Device();
    device.setOperation("replace");
    device.setName(node.getHost_name());
    OspfAddNode ospf = new OspfAddNode();
    ospf.setAreaId(changeNode.getNode().getClusterArea());
    OspfVirtualLink vl = new OspfVirtualLink();
    vl.setOperation("delete");
    Range range = new Range();
    range.setOperation("delete");
    ospf.setVirtualLink(vl);
    ospf.setRange(range);
    device.setOspfAddNode(ospf);
    ret.setDevice(device);

    Device pairDevice = new Device();
    pairDevice.setOperation("replace");
    pairDevice.setName(pairNode.getHost_name());
    OspfAddNode pairOspf = new OspfAddNode();
    pairOspf.setAreaId(changeNode.getPairNode().getClusterArea());
    OspfVirtualLink pairVl = new OspfVirtualLink();
    pairVl.setOperation("delete");
    pairOspf.setVirtualLink(pairVl);
    pairDevice.setOspfAddNode(pairOspf);
    ret.setPairDevice(pairDevice);

    logger.trace(CommonDefinitions.END);
    return ret;
  }

  private static BLeafAddDelete deleteOspfRoute(ChangeNode changeNode, Nodes node) {
    logger.trace(CommonDefinitions.START);
    BLeafAddDelete ret = new BLeafAddDelete();

    ret.setName("b-leaf");

    Device device = new Device();
    device.setOperation("replace");
    device.setName(node.getHost_name());
    OspfAddNode ospf = new OspfAddNode();
    ospf.setAreaId(changeNode.getNode().getClusterArea());
    Range range = new Range();
    range.setOperation("delete");
    ospf.setRange(range);
    device.setOspfAddNode(ospf);
    ret.setDevice(device);

    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_Leaf Device Removal (Device)<br>
   * Convert Leaf device removal information (device) into EM transmittable format.
   *
   * @param nodeName
   *          device name
   * @return Leaf device removal information (device) (for sending to EM)
   */
  public static LeafAddDelete toLeafInfoNodeDelete(String nodeName) {
    logger.trace(CommonDefinitions.START);
    logger.debug("nodeName=" + nodeName);

    LeafAddDelete ret = new LeafAddDelete();
    ret.setName("leaf");

    Device device = new Device();
    device.setOperation("delete");
    device.setName(nodeName);
    ret.setDevice(device);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_Spine Device Removal <\Device>\ Convert Spine device removal information (device) into EM transmittable format.
   *
   * @param nodeName
   *          device name
   * @return Spine device removal information (device) (for sending to EM)
   */
  public static SpineAddDelete toSpineInfoNodeDelete(String nodeName) {
    logger.trace(CommonDefinitions.START);
    logger.debug("nodeName=" + nodeName);

    SpineAddDelete ret = new SpineAddDelete();
    ret.setName("spine");

    Device device = new Device();
    device.setOperation("delete");
    device.setName(nodeName);
    ret.setDevice(device);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_B-Leaf Device Removal (Device)<br>
   * Convert B-Leaf device removal information (device) into EM transmittable format.
   *
   * @param target
   *          target device (DB)
   * @param deleteNodeRest
   *          input REST information
   * @param pareNodesDb
   *          pair B-Leaf information (DB)
   * @return Leaf device removal information (device) (for sending to EM)
   */
  public static BLeafAddDelete toBLeafInfoNodeDelete(Nodes target, DeleteNode deleteNodeRest, Nodes pairNodesDb) {
    logger.trace(CommonDefinitions.START);
    logger.debug("target=" + target + " , " + deleteNodeRest + " , " + pairNodesDb);

    BLeafAddDelete ret = new BLeafAddDelete();
    ret.setName("b-leaf");

    if (pairNodesDb != null) {
      Device pairDevice = new Device();
      pairDevice.setName(pairNodesDb.getNode_name());
      OspfAddNode ospf = new OspfAddNode();
      ospf.setAreaId(deleteNodeRest.getUpdateNode().getClusterArea());
      AttributeOperation operation = new AttributeOperation();
      operation.setOperation("delete");
      ospf.setVirtualLinkOperation(operation);
      ospf.setRangeOperation(operation);
      pairDevice.setOspfAddNode(ospf);
      ret.setPairDevice(pairDevice);
    }

    Device device = new Device();
    device.setOperation("delete");
    if (target.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
      device.setName(CommonDefinitions.COREROUTER_HOSTNAME);
    } else {
      device.setName(target.getNode_name());
    }
    ret.setDevice(device);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_Leaf Device Removal (Internal Link)<br>
   * Convert Leaf device removal informatin (internal link) into EM transmittable format.
   *
   * @param deleteNode
   *          Leaf device removal information
   * @param oppoNodeList
   *          device information list
   * @return Leaf device removal information (internal link) (for sending to EM)
   * @throws IllegalArgumentException
   *           Device ID entered from FC does not exist in DB.
   */
  public static InternalLinkAddDelete toInternalLinkDelete(DeleteNode deleteNode, ArrayList<Nodes> oppoNodeList)
      throws IllegalArgumentException {
    logger.trace(CommonDefinitions.START);
    logger.debug(deleteNode + ", " + oppoNodeList);

    InternalLinkAddDelete ret = new InternalLinkAddDelete();
    ret.setName("internal-link");

    List<Device> deviceList = new ArrayList<Device>();

    for (OppositeNodesDeleteNode oppoNodeRest : deleteNode.getDeleteNodes().getOppositeNodes()) {

      Device device = delInternalLinkInfo(oppoNodeRest, oppoNodeList);
      deviceList.add(device);
    }
    ret.setDevice(deviceList);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Setting IF Delete Information for Device Removal Internal Link.
   *
   * @param oppoNodeRest
   *          IF information of opposing device for device removal
   * @param nodesList
   *          device information list (DB)
   * @return common configuration device information
   * @throws IllegalArgumentException
   *           LagIF ID/physical IF ID does not exist in DB
   */
  private static Device delInternalLinkInfo(OppositeNodesDeleteNode oppoNodeRest, ArrayList<Nodes> nodesList)
      throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(oppoNodeRest + ", " + nodesList);

    Device device = null;

    Nodes nodesDb = null;
    for (Nodes nodes : nodesList) {
      if (oppoNodeRest.getNodeId().equals(nodes.getNode_id())) {
        nodesDb = nodes;
        break;
      }
    }
    if (nodesDb == null) {
      logger.debug("Not found Nodes");
      throw new IllegalArgumentException();
    }

    device = new Device();

    device.setName(nodesDb.getNode_name());

    InternalInterface internalInterface = new InternalInterface();
    internalInterface.setName(getIfName(oppoNodeRest.getInternalLinkIf().getIfInfo().getIfType(),
        oppoNodeRest.getInternalLinkIf().getIfInfo().getIfId(), nodesDb));
    internalInterface.setOperation("delete");
    String type = oppoNodeRest.getInternalLinkIf().getIfInfo().getIfType();
    if (oppoNodeRest.getInternalLinkIf().getIfInfo().getIfType().equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      type = CommonDefinitions.IF_TYPE_PHYSICAL_IF;
    }
    internalInterface.setType(type);
    internalInterface.setMinimumLinks(0L);
    if (type.equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
      List<InternalInterfaceMember> internalLagList = new ArrayList<InternalInterfaceMember>();
      for (LagIfs lagIfs : nodesDb.getLagIfsList()) {
        if (lagIfs.getFc_lag_if_id().equals(oppoNodeRest.getInternalLinkIf().getIfInfo().getIfId())) {
          for (LagMembers member : lagIfs.getLagMembersList()) {
            String memberType = null;
            String memberIfId = null;
            if (member.getPhysical_if_id() != null) {
              memberType = CommonDefinitions.IF_TYPE_PHYSICAL_IF;
              memberIfId = member.getPhysical_if_id();
            } else {
              memberType = CommonDefinitions.IF_TYPE_BREAKOUT_IF;
              memberIfId = member.getBreakout_if_id();
            }
            String memberIfName = getIfName(memberType, memberIfId, nodesDb);
            InternalInterfaceMember memberEm = new InternalInterfaceMember();
            memberEm.setName(memberIfName);
            memberEm.setOperation("delete");
            internalLagList.add(memberEm);
          }
          break;
        }
      }
      internalInterface.setInternalInterfaceMember(internalLagList);
    }

    List<InternalInterface> internalInterfaceList = new ArrayList<>();
    internalInterfaceList.add(internalInterface);
    device.setInternalLagList(internalInterfaceList);

    logger.debug(device);
    logger.trace(CommonDefinitions.END);
    return device;
  }

  /**
   * EM Data Mapping_Generating LagIF<br>
   * Convert information for generating LagIF into EM transmittable format.
   *
   * @param input
   *          input information
   * @param nodes
   *          device information
   * @param lagIfId
   *          LagIf id
   * @return information for generating LagIF (for sending to EM)
   * @exception IllegalArgumentException
   *              Input physical IF does not exist in DB.
   */
  public static CeLagAddDelete toLagIfCreate(CreateLagInterface input, Nodes nodes, String lagIfId)
      throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodes);

    CeLagAddDelete ret = new CeLagAddDelete();

    ret.setName("ce-lag");
    ret.setDevice(new ArrayList<Device>());

    Device device = new Device();
    if (nodes.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
      device.setName(CommonDefinitions.COREROUTER_HOSTNAME);
    } else {
      device.setName(nodes.getNode_name());
    }
    device.setCeLagInterfaceList(new ArrayList<CeLagInterface>());

    device.getCeLagInterfaceList().add(new CeLagInterface());
    String lagIfName = LogicalPhysicalConverter.toLagIfName(nodes.getEquipments().getLag_prefix(), lagIfId);
    device.getCeLagInterfaceList().get(0).setName(lagIfName);
    device.getCeLagInterfaceList().get(0).setLagId(Integer.parseInt(lagIfId));
    device.getCeLagInterfaceList().get(0).setMinimumLinks((long) input.getLagIf().getPhysicalIfs().size());
    String speed = null;
    if (input.getLagIf().getPhysicalIfs().get(0).getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
      for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
        if (physicalIfs.getPhysical_if_id().equals(input.getLagIf().getPhysicalIfs().get(0).getIfId())) {
          speed = physicalIfs.getIf_speed();
          break;
        }
      }
    } else if (input.getLagIf().getPhysicalIfs().get(0).getIfType().equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
        for (BreakoutIfs breakoutIfs : physicalIfs.getBreakoutIfsList()) {
          if (breakoutIfs.getBreakout_if_id().equals(input.getLagIf().getPhysicalIfs().get(0).getIfId())) {
            speed = breakoutIfs.getSpeed();
            break;
          }
        }
        if (speed != null) {
          break;
        }
      }
    }
    if (speed == null) {
      throw new IllegalArgumentException();
    } else {
      device.getCeLagInterfaceList().get(0).setLinkSpeed(speed);
    }
    device.getCeLagInterfaceList().get(0).setLeafInterfaceList(new ArrayList<LeafInterface>());

    for (PhysicalIfsCreateLagIf phys : input.getLagIf().getPhysicalIfs()) {
      LeafInterface tmp = new LeafInterface();
      String physIfName = getIfName(phys.getIfType(), phys.getIfId(), nodes);
      if (physIfName.isEmpty()) {
        throw new IllegalArgumentException();
      } else {
        tmp.setName(physIfName);
        device.getCeLagInterfaceList().get(0).getLeafInterfaceList().add(tmp);
      }
    }
    ret.getDevice().add(device);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_LagIF Deletion<br>
   * Convert information for generating LagIF into EM transmittable format.
   *
   * @param nodes
   *          device information
   * @param lagIfs
   *          LAG information
   * @return information for deleting LagIF (for sending to EM)
   */
  public static CeLagAddDelete toLagIfDelete(Nodes nodes, LagIfs lagIfs) {

    logger.trace(CommonDefinitions.START);
    logger.debug(lagIfs);

    CeLagAddDelete ret = new CeLagAddDelete();

    ret.setName("ce-lag");

    ret.setDevice(new ArrayList<Device>());
    ret.getDevice().add(new Device());
    if (nodes.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
      ret.getDevice().get(0).setName(CommonDefinitions.COREROUTER_HOSTNAME);
    } else {
      ret.getDevice().get(0).setName(nodes.getNode_name());
    }
    ret.getDevice().get(0).setCeLagInterfaceList(new ArrayList<CeLagInterface>());

    ret.getDevice().get(0).getCeLagInterfaceList().add(new CeLagInterface());
    ret.getDevice().get(0).getCeLagInterfaceList().get(0).setName(lagIfs.getIf_name());
    ret.getDevice().get(0).getCeLagInterfaceList().get(0).setMinimumLinks(0L);
    ret.getDevice().get(0).getCeLagInterfaceList().get(0).setLeafInterfaceList(new ArrayList<LeafInterface>());

    for (LagMembers lagMem : lagIfs.getLagMembersList()) {
      String ifType = "";
      String ifId = "";
      if (lagMem.getPhysical_if_id() != null) {
        ifType = CommonDefinitions.IF_TYPE_PHYSICAL_IF;
        ifId = lagMem.getPhysical_if_id();
      } else {
        ifType = CommonDefinitions.IF_TYPE_BREAKOUT_IF;
        ifId = lagMem.getBreakout_if_id();
      }
      String ifName = getIfName(ifType, ifId, nodes);
      LeafInterface leafInterface = new LeafInterface();
      leafInterface.setOperation("delete");
      leafInterface.setName(ifName);
      ret.getDevice().get(0).getCeLagInterfaceList().get(0).getLeafInterfaceList().add(leafInterface);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_Generating Inter-Cluster Link<br>
   * Convert information for generating inter-cluster link into EM transmittable format.
   *
   * @param input
   *          input information
   * @param nodes
   *          device information
   * @return information for generating inter-cluster link (for sending to EM)
   * @exception IllegalArgumentException
   *              LAG member corresponding to LAGIF ID does not exist.
   */
  public static BetweenClustersLinkAddDelete toBetweenClustersLinkCreate(CreateBetweenClustersLink input, Nodes nodes)
      throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodes);

    BetweenClustersLinkAddDelete ret = new BetweenClustersLinkAddDelete();

    ret.setName("cluster-link");

    ret.setDevice(new LeafDevice());
    if (nodes.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
      ret.getDevice().setName(CommonDefinitions.COREROUTER_HOSTNAME);
    } else {
      ret.getDevice().setName(nodes.getNode_name());
    }
    ret.getDevice().setClusterLink(new ClusterLink());
    String ifType = input.getTargetIf().getIfType();
    String ifId = input.getTargetIf().getIfId();
    ret.getDevice().getClusterLink().setName(getIfName(ifType, ifId, nodes));

    ret.getDevice().getClusterLink().setAddress(input.getClusterLink().getAddress());
    ret.getDevice().getClusterLink().setPrefix(input.getClusterLink().getPrefix());

    ret.getDevice().getClusterLink().setOspf(new Ospf());
    ret.getDevice().getClusterLink().getOspf().setMetric(input.getClusterLink().getCost());

    ret.getDevice().getClusterLink().setLagMemberList(new ArrayList<LagMemberIf>());
    if (ifType.equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
      ret.getDevice().getClusterLink().setIfType(ifType);

      Set<LagMembers> lagMembersDbSet = null;
      for (LagIfs lagIfsDb : nodes.getLagIfsList()) {
        if (lagIfsDb.getFc_lag_if_id().equals(input.getTargetIf().getIfId())) {
          lagMembersDbSet = lagIfsDb.getLagMembersList();
          break;
        }
      }

      if (lagMembersDbSet != null) {
        for (LagMembers lagMembers : lagMembersDbSet) {
          LagMemberIf lagMemberIf = new LagMemberIf();
          if (lagMembers.getPhysical_if_id() == null || lagMembers.getBreakout_if_id() == null) {
            if (lagMembers.getPhysical_if_id() != null) {
              lagMemberIf
                  .setName(getIfName(CommonDefinitions.IF_TYPE_PHYSICAL_IF, lagMembers.getPhysical_if_id(), nodes));
              lagMemberIf.setCondition(input.getClusterLink().getCondition());
              ret.getDevice().getClusterLink().getLagMemberList().add(lagMemberIf);
            } else if (lagMembers.getBreakout_if_id() != null) {
              lagMemberIf
                  .setName(getIfName(CommonDefinitions.IF_TYPE_BREAKOUT_IF, lagMembers.getBreakout_if_id(), nodes));
              lagMemberIf.setCondition(input.getClusterLink().getCondition());
              ret.getDevice().getClusterLink().getLagMemberList().add(lagMemberIf);
            }
          }
        }
      } else {
        throw new IllegalArgumentException();
      }
    } else {
      ret.getDevice().getClusterLink().setIfType(CommonDefinitions.IF_TYPE_PHYSICAL_IF);
      ret.getDevice().getClusterLink().setLagMemberList(new ArrayList<LagMemberIf>(1));
      ret.getDevice().getClusterLink().getLagMemberList().add(new LagMemberIf());
      ret.getDevice().getClusterLink().getLagMemberList().get(0).setName(getIfName(ifType, ifId, nodes));
      ret.getDevice().getClusterLink().getLagMemberList().get(0).setCondition(input.getClusterLink().getCondition());
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;
  }

  /**
   * EM Data Mapping_Inter-Cluster Link Deletion<br>
   * Convert information for deleting inter-cluster link into EM transmittable format.
   *
   * @param nodes
   *          device information
   * @param ifType
   *          IF type
   * @param ifId
   *          IF ID
   * @return information for deleting inter-cluster link (for sending to EM)
   */

  public static BetweenClustersLinkAddDelete toBetweenClustersLinkDelete(Nodes nodes, String ifType, String ifId) {

    logger.trace(CommonDefinitions.START);
    logger.debug(nodes);

    BetweenClustersLinkAddDelete ret = new BetweenClustersLinkAddDelete();

    ret.setName("cluster-link");

    ret.setDevice(new LeafDevice());
    if (nodes.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
      ret.getDevice().setName(CommonDefinitions.COREROUTER_HOSTNAME);
    } else {
      ret.getDevice().setName(nodes.getNode_name());
    }

    ret.getDevice().setClusterLink(new ClusterLink());
    ret.getDevice().getClusterLink().setOperation("delete");
    ret.getDevice().getClusterLink().setName(getIfName(ifType, ifId, nodes));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;
  }

  /**
   * EM Data Mapping_Generating/Changing L2VLAN IF In A Lump<br>
   * Convert L2VLAN IF batch generation information into EM transmittable format.
   *
   * @param input
   *          L2VLAN IF batch generation information
   * @param nodesSet
   *          device information list
   * @param allVlanIfsMap
   *          all VLAN IF information list (mapping of VLAN IF information list with device ID as a key)
   * @param allPhysicalIfsMap
   *          Physical IF all information lists（Map of physical IF information list with device ID as key)
   * @param allDummyVlanIfsMap
   *          Dummy VLANIF list
   * @param irbInstanceInfoMap
   *          IRB instance information list
   * @return L2VLAN IF batch generation information (For EM transmission)
   * @throws IllegalArgumentException
   *           Device ID entered from FC does not exist in DB.
   */
  public static L2SliceAddDelete toL2VlanIfCreate(BulkCreateL2VlanIf input, Set<Nodes> nodesSet,
      Map<String, List<VlanIfs>> allVlanIfsMap, Map<String, List<PhysicalIfs>> allPhysicalIfsMap,
      Map<String, List<DummyVlanIfs>> allDummyVlanIfsMap, Map<String, IRBInstanceInfo> irbInstanceInfoMap)
      throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesSet);

    L2SliceAddDelete ret = new L2SliceAddDelete();
    if (null == input.getVrfId()) {
      ret.setName(LogicalPhysicalConverter.toSliceName(input.getVni().toString()));
    } else {
      ret.setName(LogicalPhysicalConverter.toSliceName(input.getVrfId().toString()));
    }

    ret.setDeviceLeafList(new ArrayList<DeviceLeaf>());
    DeviceLeaf deviceLeaf = null;
    Vrf vrf = null;
    Cp cp = null;
    DummyVlan dummyVlan = null;

    if (input.getCreateVlanIfs() != null) {
      for (CreateVlanIfs createVlanIfs : input.getCreateVlanIfs()) {

        deviceLeaf = null;

        for (Nodes nodes : nodesSet) {
          if (createVlanIfs.getNodeId().equals(nodes.getNode_id())) {
            deviceLeaf = new DeviceLeaf();
            deviceLeaf.setName(nodes.getNode_name());
            deviceLeaf.setCpList(new ArrayList<Cp>());
            deviceLeaf.setUpdCpList(new ArrayList<Cp>());
            deviceLeaf.setDummyVlanList(new ArrayList<DummyVlan>());

            if (null != input.getVrfId()) {
              Loopback loopback = new Loopback();
              L3Vni l3vni = new L3Vni();
              vrf = new Vrf();
              vrf.setVrfName("vrf" + input.getVrfId());
              vrf.setVrfId(input.getVrfId().toString());
              vrf.setRt("target:" + input.getVrfId() + ":" + input.getPlane());
              vrf.setRd(createVlanIfs.getRouteDistingusher());
              vrf.setRouterId(nodes.getLoopback_if_address());
              if (null != input.getLoopBackInterface()) {
                loopback.setAddress(input.getLoopBackInterface().getAddress());
                loopback.setPrefix(input.getLoopBackInterface().getPrefix().longValue());
                vrf.setLoopback(loopback);
              }
              if (null != input.getL3Vni()) {
                l3vni.setVlanId(input.getL3Vni().getVlanId().longValue());
                l3vni.setVniId(input.getL3Vni().getVniId().longValue());
                vrf.setL3Vni(l3vni);
              }
              deviceLeaf.setVrf(vrf);
            }

            if (null == createVlanIfs.getIsDummy() || !createVlanIfs.getIsDummy()) {
              cp = new Cp();
              cp.setName(getIfName(createVlanIfs.getBaseIf().getIfType(), createVlanIfs.getBaseIf().getIfId(), nodes));
              cp.setVlanId(createVlanIfs.getVlanId().longValue());
              cp.setPortMode(createVlanIfs.getPortMode());
              if (null != input.getVni()) {
                cp.setVni(input.getVni().longValue());
              } else {
                cp.setVni(createVlanIfs.getIrbValue().getVni().longValue());
              }
              if (createVlanIfs.getBaseIf().getIfType().equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
                cp.setEsi(createVlanIfs.getEsi());
                cp.setSystemId(createVlanIfs.getLacpSystemId());
              } else {
                if ((createVlanIfs.getEsi() != null) || (createVlanIfs.getLacpSystemId() != null)) {
                  throw new IllegalArgumentException();
                }
              }
              if (null != createVlanIfs.getClagId()) {
                cp.setClagId(createVlanIfs.getClagId().longValue());
              }

              if (createVlanIfs.getBaseIf().getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
                Boolean isSpeed = false;
                for (PhysicalIfs pifs : allPhysicalIfsMap.get(createVlanIfs.getNodeId())) {
                  if (pifs.getPhysical_if_id().equals(createVlanIfs.getBaseIf().getIfId())) {
                    cp.setSpeed(pifs.getIf_speed());
                    isSpeed = true;
                    break;
                  }
                }
                if (!isSpeed) {
                  throw new IllegalArgumentException();
                }
              }

              cp.setQos(setQos(createVlanIfs.getBaseIf(), createVlanIfs.getQos(), nodes));

              if (null != input.getVrfId()) {
                cp.setIrb(
                    getIrb(createVlanIfs.getIrbValue().getIpv4Address(), createVlanIfs.getIrbValue().getIpv4Prefix(),
                        createVlanIfs.getIrbValue().getVirtualGatewayAddress(), false));
              }

              if (input.getqInQ() == null || !input.getqInQ()) {
                cp.delQInQ();
              } else {
                cp.addQInQ();
              }

              deviceLeaf.getCpList().add(cp);
            } else {
              dummyVlan = new DummyVlan();
              dummyVlan.setVlanId(createVlanIfs.getVlanId().longValue());
              dummyVlan.setVni((long) createVlanIfs.getIrbValue().getVni());
              dummyVlan.setIrb(
                  getIrb(createVlanIfs.getIrbValue().getIpv4Address(), createVlanIfs.getIrbValue().getIpv4Prefix(),
                      createVlanIfs.getIrbValue().getVirtualGatewayAddress(), true));
              deviceLeaf.getDummyVlanList().add(dummyVlan);
            }
            if (null != createVlanIfs.getMultiHomingValue()) {
              MultiHoming multiHoming = new MultiHoming();
              Anycast anyCast = new Anycast();
              anyCast.setAddress(createVlanIfs.getMultiHomingValue().getAnycastAddress());
              multiHoming.setAnycast(anyCast);

              ClagHandOverInterface clagInterface = new ClagHandOverInterface();
              clagInterface.setAddress(createVlanIfs.getMultiHomingValue().getIfAddress());
              clagInterface.setPrefix(createVlanIfs.getMultiHomingValue().getIfPrefix().longValue());
              multiHoming.setClagInterface(clagInterface);

              Clag clag = new Clag();
              ClagBackup clagBackup = new ClagBackup();
              clagBackup.setAddress(createVlanIfs.getMultiHomingValue().getBackupAddress());
              ClagPeer clagPeer = new ClagPeer();
              clagPeer.setAddress(createVlanIfs.getMultiHomingValue().getPeerAddress());

              clag.setBackup(clagBackup);
              clag.setPeer(clagPeer);
              multiHoming.setClag(clag);

              deviceLeaf.setMultiHoming(multiHoming);
            }

            break;
          }
        }

        if (deviceLeaf == null) {
          throw new IllegalArgumentException();
        }

        boolean alreadyRegister = false;
        for (DeviceLeaf dl : ret.getDeviceLeafList()) {
          if (null == createVlanIfs.getIsDummy() || !createVlanIfs.getIsDummy()) {
            if (dl.getName().equals(deviceLeaf.getName())) {
              alreadyRegister = true;
              dl.getCpList().add(cp);
              break;
            }
          } else {
            if (dl.getName().equals(deviceLeaf.getName())) {
              alreadyRegister = true;
              dl.getDummyVlanList().add(dummyVlan);
              break;
            }
          }
        }

        if (!alreadyRegister) {
          ret.getDeviceLeafList().add(deviceLeaf);
        }
      }
    }

    if (input.getUpdateVlanIfs() != null) {
      for (UpdateVlanIfs updateVlanIfs : input.getUpdateVlanIfs()) {

        deviceLeaf = null;

        for (Nodes nodes : nodesSet) {
          if (updateVlanIfs.getNodeId().equals(nodes.getNode_id())) {
            deviceLeaf = new DeviceLeaf();
            deviceLeaf.setName(nodes.getNode_name());
            deviceLeaf.setCpList(new ArrayList<Cp>());
            deviceLeaf.setUpdCpList(new ArrayList<Cp>());
            deviceLeaf.setDummyVlanList(new ArrayList<DummyVlan>());
            if (null != input.getVrfId()) {
              Loopback loopback = new Loopback();
              L3Vni l3vni = new L3Vni();
              vrf = new Vrf();
              vrf.setVrfName("vrf" + input.getVrfId());
              vrf.setVrfId(input.getVrfId().toString());
              vrf.setRt("target:" + input.getVrfId() + ":" + input.getPlane());
              vrf.setRouterId(nodes.getLoopback_if_address());
              if (null != input.getLoopBackInterface()) {
                loopback.setAddress(input.getLoopBackInterface().getAddress());
                loopback.setPrefix(input.getLoopBackInterface().getPrefix().longValue());
                vrf.setLoopback(loopback);
              }
              if (null != input.getL3Vni()) {
                l3vni.setVlanId(input.getL3Vni().getVlanId().longValue());
                l3vni.setVniId(input.getL3Vni().getVniId().longValue());
                vrf.setL3Vni(l3vni);
              }
              deviceLeaf.setVrf(vrf);
            }

            cp = null;
            dummyVlan = null;
            for (VlanIfs ifs : allVlanIfsMap.get(updateVlanIfs.getNodeId())) {
              if (ifs.getVlan_if_id().equals(updateVlanIfs.getVlanIfId())) {
                if (ifs.getLag_if_id() == null && updateVlanIfs.getBaseIf() == null) {
                  throw new IllegalArgumentException();
                }
                cp = new Cp();
                cp.setName(ifs.getIf_name());
                cp.setVlanId(new Long(ifs.getVlan_id()));
                cp.setEsi(updateVlanIfs.getEsi());
                cp.setSystemId(updateVlanIfs.getLacpSystemId());

                if (null != updateVlanIfs.getClagId()) {
                  cp.setClagId(updateVlanIfs.getClagId().longValue());
                }
                break;
              }
            }

            for (DummyVlanIfs ifs : allDummyVlanIfsMap.get(updateVlanIfs.getNodeId())) {
              if (ifs.getVlan_if_id().equals(updateVlanIfs.getVlanIfId())) {
                cp = new Cp();
                cp.setName(
                    getIfName(updateVlanIfs.getBaseIf().getIfType(), updateVlanIfs.getBaseIf().getIfId(), nodes));
                cp.setVlanId(new Long(ifs.getVlan_id()));
                cp.setEsi(updateVlanIfs.getEsi());
                cp.setSystemId(updateVlanIfs.getLacpSystemId());

                if (null != updateVlanIfs.getClagId()) {
                  cp.setClagId(updateVlanIfs.getClagId().longValue());
                }
                if (null != updateVlanIfs.getBaseIf()) {
                  cp.setVni(Long.parseLong(irbInstanceInfoMap.get(ifs.getIrb_instance_id()).getIrb_vni()));
                  cp.setPortMode(updateVlanIfs.getPortMode());
                  if (updateVlanIfs.getBaseIf().getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
                    for (PhysicalIfs pifs : allPhysicalIfsMap.get(updateVlanIfs.getNodeId())) {
                      if (pifs.getPhysical_if_id().equals(updateVlanIfs.getBaseIf().getIfId())) {
                        cp.setSpeed(pifs.getIf_speed());
                      }
                    }
                  }
                  cp.setQos(setQos(updateVlanIfs.getBaseIf(), updateVlanIfs.getQos(), nodes));
                  if (null != updateVlanIfs.getIrbValue()) {
                    cp.setIrb(getIrb(updateVlanIfs.getIrbValue().getIpv4Address(),
                        updateVlanIfs.getIrbValue().getIpv4Prefix(),
                        updateVlanIfs.getIrbValue().getVirtualGatewayAddress(), false));
                  }

                  dummyVlan = new DummyVlan();
                  dummyVlan.setOperation("delete");
                  dummyVlan.setVlanId(Long.parseLong(ifs.getVlan_id()));
                }
                break;
              }
            }

            if (cp == null) {
              throw new IllegalArgumentException();
            } else {
              deviceLeaf.getUpdCpList().add(cp);
              if (null != dummyVlan) {
                deviceLeaf.getDummyVlanList().add(dummyVlan);
              }
              break;
            }
          }
        }

        if (deviceLeaf == null) {
          throw new IllegalArgumentException();
        }

        boolean alreadyRegister = false;
        for (DeviceLeaf dl : ret.getDeviceLeafList()) {
          if (dl.getName().equals(deviceLeaf.getName())) {
            alreadyRegister = true;
            dl.getUpdCpList().add(cp);
            if (dummyVlan != null) {
              dl.getDummyVlanList().add(dummyVlan);
            }
            break;
          }
        }

        if (!alreadyRegister) {
          ret.getDeviceLeafList().add(deviceLeaf);
        }
      }
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_Generating L3VLAN IF In A Lump<br>
   * Convert L3VLAN IF batch generation information into EM transmittable format.
   *
   * @param input
   *          L3VLAN IF batch generation information
   * @param nodesSet
   *          device information list
   * @return L3VLAN IF batch generation information (for sending to EM)
   * @throws IllegalArgumentException
   *           Device ID entered from FC does not exist in DB.
   */
  public static L3SliceAddDelete toL3VlanIfCreate(BulkCreateL3VlanIf input, Set<Nodes> nodesSet)
      throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesSet);

    L3SliceAddDelete ret = new L3SliceAddDelete();
    ret.setName(LogicalPhysicalConverter.toSliceName(input.getVrfId().toString()));
    ret.setDeviceLeafList(new ArrayList<DeviceLeaf>());

    for (VlanIfsCreateL3VlanIf inputVlanIfs : input.getVlanIfs()) {

      DeviceLeaf deviceLeaf = null;
      Cp cp = null;

      for (Nodes nodes : nodesSet) {

        if (inputVlanIfs.getBaseIf().getNodeId().equals(nodes.getNode_id())) {

          deviceLeaf = new DeviceLeaf();
          if (nodes.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
            deviceLeaf.setName(CommonDefinitions.COREROUTER_HOSTNAME);
          } else {
            deviceLeaf.setName(nodes.getNode_name());
          }

          deviceLeaf.setVrf(new Vrf());
          deviceLeaf.getVrf().setVrfName(LogicalPhysicalConverter.toVrf(input.getVrfId()));
          deviceLeaf.getVrf().setRt(LogicalPhysicalConverter.toRouteTarget(input.getVrfId(), input.getPlane()));
          deviceLeaf.getVrf().setRd(inputVlanIfs.getRouteDistingusher());
          deviceLeaf.getVrf().setRouterId(nodes.getLoopback_if_address());

          deviceLeaf.setCpList(new ArrayList<Cp>());

          String ifName = getIfName(inputVlanIfs.getBaseIf().getIfType(), inputVlanIfs.getBaseIf().getIfId(), nodes);
          boolean isIpv4 = true;
          boolean isIpv6 = true;
          if (!ifName.isEmpty()) {
            cp = new Cp();
            cp.setName(ifName);
            cp.setVlanId(inputVlanIfs.getVlanId().longValue());

            cp.setCeInterface(new CeInterface());
            if (inputVlanIfs.getIpv4Address() == null) {
              isIpv4 = false;
            } else {
              cp.getCeInterface().setAddress(inputVlanIfs.getIpv4Address());
              cp.getCeInterface().setPrefix(inputVlanIfs.getIpv4Prefix());
            }
            if (inputVlanIfs.getIpv6Address() == null) {
              isIpv6 = false;
            } else {
              cp.getCeInterface().setAddress6(inputVlanIfs.getIpv6Address());
              cp.getCeInterface().setPrefix6(inputVlanIfs.getIpv6Prefix());
            }
            cp.getCeInterface().setMtu(inputVlanIfs.getMtu().longValue());

            cp.setQos(setQos(inputVlanIfs.getBaseIf(), inputVlanIfs.getQos(), nodes));

            if (inputVlanIfs.getVrrp() != null) {
              cp.setVrrp(new Vrrp());
              cp.getVrrp().setGroupId(inputVlanIfs.getVrrp().getGroupId().longValue());
              if (isIpv4) {
                if (inputVlanIfs.getVrrp().getVirtualIpv4Address() == null) {
                  throw new IllegalArgumentException();
                } else {
                  cp.getVrrp().setVirtualAddress(inputVlanIfs.getVrrp().getVirtualIpv4Address());
                }
              }
              if (isIpv6) {
                if (inputVlanIfs.getVrrp().getVirtualIpv6Address() == null) {
                  throw new IllegalArgumentException();
                } else {
                  cp.getVrrp().setVirtualAddress6(inputVlanIfs.getVrrp().getVirtualIpv6Address());
                }
              }
              if (inputVlanIfs.getVrrp().getRole().equals(CommonDefinitions.VRRP_ROLE_SLAVE_STRING)) {
                cp.getVrrp().setPriority(95);
              } else if (inputVlanIfs.getVrrp().getRole().equals(CommonDefinitions.VRRP_ROLE_MASTER_STRING)) {
                cp.getVrrp().setPriority(100);
                cp.getVrrp().setTrack(new Track());
                cp.getVrrp().getTrack().setTrackInterfaceList(new ArrayList<TrackInterface>());
                for (TrackingIf trackingIf : inputVlanIfs.getVrrp().getTrackingIfList()) {
                  TrackInterface trackIf = new TrackInterface();
                  trackIf.setName(getIfName(trackingIf.getIfType(), trackingIf.getIfId(), nodes));
                  cp.getVrrp().getTrack().getTrackInterfaceList().add(trackIf);
                }
              }
            }

            if (inputVlanIfs.getBgp() != null) {
              cp.setL3SliceBgp(new L3SliceBgp());
              if (inputVlanIfs.getBgp().getRole().equals(CommonDefinitions.BGP_ROLE_MASTER_STRING)) {
                cp.getL3SliceBgp().addMaster();
              }
              if (isIpv4) {
                if (inputVlanIfs.getBgp().getNeighborIpv4Address() == null) {
                  throw new IllegalArgumentException();
                } else {
                  cp.getL3SliceBgp().setLocalAddress(inputVlanIfs.getIpv4Address());
                  cp.getL3SliceBgp().setRemoteAddress(inputVlanIfs.getBgp().getNeighborIpv4Address());
                }
              }
              if (isIpv6) {
                if (inputVlanIfs.getBgp().getNeighborIpv6Address() == null) {
                  throw new IllegalArgumentException();
                } else {
                  cp.getL3SliceBgp().setLocalAddress6(inputVlanIfs.getIpv6Address());
                  cp.getL3SliceBgp().setRemoteAddress6(inputVlanIfs.getBgp().getNeighborIpv6Address());
                }
              }
              cp.getL3SliceBgp().setRemoteAsNumber(inputVlanIfs.getBgp().getNeighborAs().longValue());
            }

            if (inputVlanIfs.getStaticRoutes() != null) {
              cp.setL3SliceStatic(new L3SliceStatic());
              List<Route> routeList = new ArrayList<Route>();
              List<Route> routeList6 = new ArrayList<Route>();
              for (StaticRoute staticRoute : inputVlanIfs.getStaticRoutes()) {
                Route route = new Route();
                route.setAddress(staticRoute.getAddress());
                route.setPrefix(staticRoute.getPrefix());
                route.setNextHop(staticRoute.getNextHop());
                if (staticRoute.getAddressType().equals(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV4_STRING)) {
                  routeList.add(route);
                } else if (staticRoute.getAddressType()
                    .equals(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV6_STRING)) {
                  routeList6.add(route);
                }
              }
              if (routeList.size() > 0) {
                cp.getL3SliceStatic().setRouteList(routeList);
              }
              if (routeList6.size() > 0) {
                cp.getL3SliceStatic().setRouteList6(routeList6);
              }
            }
          }

          if (cp == null) {
            throw new IllegalArgumentException();
          }

          deviceLeaf.getCpList().add(cp);

          break;
        }
      }

      if (deviceLeaf == null) {
        throw new IllegalArgumentException();
      }

      boolean alreadyRegister = false;
      for (DeviceLeaf dl : ret.getDeviceLeafList()) {
        if (dl.getName().equals(deviceLeaf.getName())) {
          alreadyRegister = true;
          dl.getCpList().add(cp);
          break;
        }
      }

      if (!alreadyRegister) {
        ret.getDeviceLeafList().add(deviceLeaf);
      }

    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Setting QOS in VLANIF batch generation.
   *
   * @param vlanIfs
   *          VLAN IF information
   * @param nodes
   *          device information
   * @return QoS cofiuration(for sendin gEM)
   */
  private static Qos setQos(BaseIfInfo baseIf, QosValues inputQos, Nodes nodes) {
    Qos qos = null;

    if (nodes.getEquipments().getQos_shaping_flg()) {
      qos = new Qos();
      int ifSpeed = LogicalPhysicalConverter.getIfSpeed(baseIf.getIfType(), baseIf.getIfId(), nodes);
      Float inflowShapingVal = null;
      Float outflowShapingVal = null;
      if (inputQos != null) {
        inflowShapingVal = inputQos.getInflowShapingRate();
        outflowShapingVal = inputQos.getOutflowShapingRate();
      }
      if (inflowShapingVal != null) {
        qos.setInflowShapingRate(new XmlFloatElement());
        qos.getInflowShapingRate().setValue((inflowShapingVal / ifSpeed) * 100);
      }
      if (outflowShapingVal != null) {
        qos.setOutflowShapingRate(new XmlFloatElement());
        qos.getOutflowShapingRate().setValue((outflowShapingVal / ifSpeed) * 100);
      }
      qos.setEgressMenu(new XmlStringElement());
      if (inputQos == null || inputQos.getEgressQueue() == null) {
        qos.getEgressMenu().setValue(nodes.getEquipments().getDefault_egress_queue_menu());
      } else {
        qos.getEgressMenu().setValue(inputQos.getEgressQueue());
      }
    }
    if (nodes.getEquipments().getQos_remark_flg()) {
      if (qos == null) {
        qos = new Qos();
      }
      qos.setRemarkMenu(new XmlStringElement());
      if (inputQos == null || inputQos.getRemarkMenu() == null) {
        qos.getRemarkMenu().setValue(nodes.getEquipments().getDefault_remark_menu());
      } else {
        qos.getRemarkMenu().setValue(inputQos.getRemarkMenu());
      }
    }

    return qos;
  }

    /**
   * EM Data Mapping_Deleting/Changing L2VLAN IF IN A Lump<br>
   * Convert L2VLAN IF batch deletion/change information into EM transmittable format.
   *
   * @param input
   *          L2VLAN IF batch deletion/change information
   * @param nodesSet
   *          device information list
   * @param allVlanIfsMap
   *         all VLAN IF information list (mapping of VLAN IF information list with device ID as a key)
   * @param allDummyVlanIfsMap
   *          Dummy VLAN IF all informaiton lists (VLAN IF information list map with device ID as key)
   * @param irbInstanceMap
   *          IRB instance information all lists
   * @return L2VLAN IF batch deletion/change information (for EM sending)
   */
  public static L2SliceAddDelete toL2VlanIfDelete(BulkDeleteL2VlanIf input, Set<Nodes> nodesSet,
      Map<String, List<VlanIfs>> allVlanIfsMap, Map<String, List<DummyVlanIfs>> allDummyVlanIfsMap,
      Map<String, IRBInstanceInfo> irbInstanceMap) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesSet + ", " + allVlanIfsMap);

    L2SliceAddDelete ret = new L2SliceAddDelete();
    if (null == input.getVrfId()) {
      ret.setName(LogicalPhysicalConverter.toSliceName(input.getVni().toString()));
    } else {
      ret.setName(LogicalPhysicalConverter.toSliceName(input.getVrfId().toString()));
    }
    ret.setDeviceLeafList(new ArrayList<DeviceLeaf>());
    DeviceLeaf deviceLeaf = null;
    Cp cp = null;
    DummyVlan dummyVlan = null;
    boolean isDummy = false;
    IRBInstanceInfo irbInstanceInfo = null;
    Vrf vrf = null;

    if (input.getDeleteVlanIfs() != null) {
      for (VlanIfsDeleteVlanIf delVlanIfs : input.getDeleteVlanIfs()) {
        deviceLeaf = null;
        isDummy = false;

        for (Nodes nodes : nodesSet) {
          if (delVlanIfs.getNodeId().equals(nodes.getNode_id())) {
            deviceLeaf = new DeviceLeaf();
            deviceLeaf.setName(nodes.getNode_name());
            deviceLeaf.setUpdCpList(new ArrayList<Cp>());
            deviceLeaf.setDummyVlanList(new ArrayList<DummyVlan>());
            if (null != input.getVrfId()) {
              vrf = new Vrf();
              vrf.setVrfName("vrf" + input.getVrfId());
              vrf.setVrfId(input.getVrfId().toString());
              vrf.setRouterId(nodes.getLoopback_if_address());
              deviceLeaf.setVrf(vrf);
            }
            cp = new Cp();
            cp.setOperation("delete");
            dummyVlan = new DummyVlan();
            dummyVlan.setOperation("delete");
            for (VlanIfs vlanIf : allVlanIfsMap.get(delVlanIfs.getNodeId())) {
              if (delVlanIfs.getVlanIfId().equals(vlanIf.getVlan_if_id())) {
                cp.setName(vlanIf.getIf_name());
                cp.setVlanId(new Long(vlanIf.getVlan_id()));
                deviceLeaf.getUpdCpList().add(cp);
                break;
              }
            }
            for (DummyVlanIfs dummyVlanIf : allDummyVlanIfsMap.get(delVlanIfs.getNodeId())) {
              if (delVlanIfs.getVlanIfId().equals(dummyVlanIf.getVlan_if_id())) {
                dummyVlan.setVlanId(new Long(dummyVlanIf.getVlan_id()));
                deviceLeaf.getDummyVlanList().add(dummyVlan);
                isDummy = true;
                break;
              }
            }
            break;
          }
        }

        if (deviceLeaf != null) {
          boolean addFlg = false;
          for (DeviceLeaf regDl : ret.getDeviceLeafList()) {
            if (regDl.getName().equals(deviceLeaf.getName())) {
              if (isDummy) {
                regDl.getDummyVlanList().add(dummyVlan);
              } else {
                regDl.getUpdCpList().add(cp);
              }
              addFlg = true;
              break;
            }
          }
          if (!addFlg) {
            ret.getDeviceLeafList().add(deviceLeaf);
          }
        }
      }
    }

    if (input.getUpdateVlanIfs() != null) {
      for (RemoveUpdateVlanIfs updVlanIfs : input.getUpdateVlanIfs()) {
        deviceLeaf = null;
        isDummy = false;

        for (Nodes nodes : nodesSet) {
          if (updVlanIfs.getNodeId().equals(nodes.getNode_id())) {
            deviceLeaf = new DeviceLeaf();
            deviceLeaf.setName(nodes.getNode_name());
            deviceLeaf.setUpdCpList(new ArrayList<Cp>());
            deviceLeaf.setDummyVlanList(new ArrayList<DummyVlan>());
            if (null != input.getVrfId()) {
              vrf = new Vrf();
              vrf.setVrfName("vrf" + input.getVrfId());
              vrf.setVrfId(input.getVrfId().toString());
              vrf.setRouterId(nodes.getLoopback_if_address());
              deviceLeaf.setVrf(vrf);
            }
            cp = new Cp();
            for (VlanIfs vlanIf : allVlanIfsMap.get(updVlanIfs.getNodeId())) {
              if (updVlanIfs.getVlanIfId().equals(vlanIf.getVlan_if_id())) {
                cp.setName(vlanIf.getIf_name());
                cp.setVlanId(new Long(vlanIf.getVlan_id()));
                if (updVlanIfs.getDummyFlag()) {
                  irbInstanceInfo = irbInstanceMap.get(vlanIf.getIrb_instance_id());
                  dummyVlan = new DummyVlan();
                  dummyVlan.setVlanId(Long.parseLong(vlanIf.getVlan_id()));
                  dummyVlan.setVni(new Long(irbInstanceInfo.getIrb_vni()));
                  cp.setOperation("delete");
                  dummyVlan.setIrb(getIrb(irbInstanceInfo.getIrb_ipv4_address(), irbInstanceInfo.getIrb_ipv4_prefix(),
                      irbInstanceInfo.getVirtual_gateway_address(), true));
                  deviceLeaf.getDummyVlanList().add(dummyVlan);
                }
                if (null != updVlanIfs.getEsi() && null != updVlanIfs.getLacpSystemId()
                    && null != updVlanIfs.getClagId()) {
                  if (updVlanIfs.getEsi().equals("0") && updVlanIfs.getLacpSystemId().equals("0")
                      && -1 == updVlanIfs.getClagId()) {
                    AttributeOperation attr = new AttributeOperation();
                    attr.setOperation("delete");
                    cp.setEsiAttr(attr);
                    cp.setSysIdAttr(attr);
                    cp.setClagId(new Long(updVlanIfs.getClagId()));
                  } else {
                    cp.setEsi(updVlanIfs.getEsi());
                    cp.setSystemId(updVlanIfs.getLacpSystemId());
                    cp.setClagId(new Long(updVlanIfs.getClagId()));
                  }
                }
                deviceLeaf.getUpdCpList().add(cp);
                break;
              }
            }
            break;
          }
        }

        if (deviceLeaf != null) {
          boolean addFlg = false;
          for (DeviceLeaf regDl : ret.getDeviceLeafList()) {
            if (regDl.getName().equals(deviceLeaf.getName())) {
              if (updVlanIfs.getDummyFlag()) {
                regDl.getUpdCpList().add(cp);
                regDl.getDummyVlanList().add(dummyVlan);
              } else {
                regDl.getUpdCpList().add(cp);
              }
              addFlg = true;
              break;
            }
          }
          if (!addFlg) {
            ret.getDeviceLeafList().add(deviceLeaf);
          }
        }
      }
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_Deleting L3VLAN IF In A Lump<br>
   * Convert L3VLAN IF batch deletion information into EM transmittable format.
   *
   * @param input
   *          L3VLAN IF batch deletion information
   * @param nodesSet
   *          device information list
   * @param allVlanIfsMap
   *          all VLAN IF information list (mapping of VLAN IF information list with device ID as a key)
   * @return L3VLAN IF batch deletion information (for sending to EM)
   */
  public static L3SliceAddDelete toL3VlanIfDelete(BulkDeleteL3VlanIf input, Set<Nodes> nodesSet,
      Map<String, List<VlanIfs>> allVlanIfsMap) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesSet + ", " + allVlanIfsMap);

    L3SliceAddDelete ret = new L3SliceAddDelete();
    ret.setName(LogicalPhysicalConverter.toSliceName(input.getVrfId()));
    ret.setDeviceLeafList(new ArrayList<DeviceLeaf>());

    for (VlanIfsDeleteVlanIf inputVlanIfs : input.getVlanIfs()) {
      DeviceLeaf deviceLeaf = null;
      Cp cp = null;

      for (Nodes nodesDb : nodesSet) {
        if (inputVlanIfs.getNodeId().equals(nodesDb.getNode_id())) {
          deviceLeaf = new DeviceLeaf();
          if (nodesDb.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
            deviceLeaf.setName(CommonDefinitions.COREROUTER_HOSTNAME);
          } else {
            deviceLeaf.setName(nodesDb.getNode_name());
          }
          deviceLeaf.setCpList(new ArrayList<Cp>());
          cp = new Cp();
          cp.setOperation("delete");
          for (VlanIfs vlanIf : allVlanIfsMap.get(inputVlanIfs.getNodeId())) {
            if (inputVlanIfs.getVlanIfId().equals(vlanIf.getVlan_if_id())) {
              cp.setName(vlanIf.getIf_name());
              cp.setVlanId(new Long(vlanIf.getVlan_id()));
              deviceLeaf.getCpList().add(cp);
              break;
            }
          }
          break;
        }
      }

      if (deviceLeaf != null) {
        boolean addFlg = false;
        for (DeviceLeaf regDl : ret.getDeviceLeafList()) {
          if (regDl.getName().equals(deviceLeaf.getName())) {
            regDl.getCpList().add(cp);
            addFlg = true;
            break;
          }
        }

        if (!addFlg) {
          ret.getDeviceLeafList().add(deviceLeaf);
        }
      }
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_Updating L2VLAN<br>
   * Convert L2VLAN IF updation information into EM transmittable format.
   *
   * @param input
   *          VLAN IF updation information
   * @param nodes
   *          device information
   * @param vlanIfs
   *          VLAN IF information
   * @return L2VLAN IF update information(for sending to EM)
   */
  public static L2SliceAddDelete toL2VlanIfChange(L2VlanOption input, Nodes nodes, VlanIfs vlanIfs) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input);
    logger.debug(nodes);
    logger.debug(vlanIfs);

    L2SliceAddDelete ret = new L2SliceAddDelete();
    if (null == input.getVrfId()) {
      ret.setName(LogicalPhysicalConverter.toSliceName(input.getVni().toString()));
    } else {
      ret.setName(LogicalPhysicalConverter.toSliceName(input.getVrfId().toString()));
    }

    ret.setDeviceLeafList(new ArrayList<DeviceLeaf>());
    ret.getDeviceLeafList().add(new DeviceLeaf());
    ret.getDeviceLeafList().get(0).setName(nodes.getNode_name());

    ret.getDeviceLeafList().get(0).setCpList(new ArrayList<Cp>());
    ret.getDeviceLeafList().get(0).getCpList().add(new Cp());
    ret.getDeviceLeafList().get(0).getCpList().get(0).setOperation("replace");
    ret.getDeviceLeafList().get(0).getCpList().get(0).setName(vlanIfs.getIf_name());
    ret.getDeviceLeafList().get(0).getCpList().get(0).setVlanId(new Long(vlanIfs.getVlan_id()));

    ret.getDeviceLeafList().get(0).getCpList().get(0).setQos(changeQos(vlanIfs, nodes));

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_L3VLAN IF Change<br>
   * Convert L3VLAN IF change information into EM transmittable format.
   *
   * @param input
   *          VLAN IF change information
   * @param nodes
   *          device information
   * @param vlanIfs
   *          VLAN IF information
   * @return L3VLAN IF change information (for sending to EM)
   */
  public static L3SliceAddDelete toL3VlanIfChange(L3VlanOption input, Nodes nodes, VlanIfs vlanIfs) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input);
    logger.debug(nodes);
    logger.debug(vlanIfs);

    L3SliceAddDelete ret = new L3SliceAddDelete();
    ret.setName(LogicalPhysicalConverter.toSliceName(input.getVrfId()));

    ret.setDeviceLeafList(new ArrayList<DeviceLeaf>());
    ret.getDeviceLeafList().add(new DeviceLeaf());
    ret.getDeviceLeafList().get(0).setName(nodes.getNode_name());

    ret.getDeviceLeafList().get(0).setCpList(new ArrayList<Cp>());
    ret.getDeviceLeafList().get(0).getCpList().add(new Cp());
    ret.getDeviceLeafList().get(0).getCpList().get(0).setOperation("replace");
    ret.getDeviceLeafList().get(0).getCpList().get(0).setName(vlanIfs.getIf_name());
    ret.getDeviceLeafList().get(0).getCpList().get(0).setVlanId(new Long(vlanIfs.getVlan_id()));

    if (input.getStaticRoutes() != null) {
      ret.getDeviceLeafList().get(0).getCpList().get(0).setL3SliceStatic(new L3SliceStatic());
      List<Route> routeList = new ArrayList<Route>();
      List<Route> routeList6 = new ArrayList<Route>();
      for (UpdateStaticRoute staticRoute : input.getStaticRoutes()) {
        Route route = new Route();
        if (staticRoute.getOperationType().equals("delete")) {
          route.setOperation("delete");
        }
        route.setAddress(staticRoute.getAddress());
        route.setPrefix(staticRoute.getPrefix());
        route.setNextHop(staticRoute.getNextHop());
        if (staticRoute.getAddressType().equals(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV4_STRING)) {
          routeList.add(route);
        } else if (staticRoute.getAddressType().equals(CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV6_STRING)) {
          routeList6.add(route);
        }
      }
      if (routeList.size() > 0) {
        ret.getDeviceLeafList().get(0).getCpList().get(0).getL3SliceStatic().setRouteList(routeList);
      }
      if (routeList6.size() > 0) {
        ret.getDeviceLeafList().get(0).getCpList().get(0).getL3SliceStatic().setRouteList6(routeList6);
      }
    }

    if (input.getQos() != null) {
      ret.getDeviceLeafList().get(0).getCpList().get(0).setQos(changeQos(vlanIfs, nodes));
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_L2VLAN IF Batch Change<br>
   * Convert L2VLAN IF batch change information into EM transmittable format.
   *
   * @param input
   *          L2VLAN IF batch change information
   * @param nodesMap
   *          Mapping the device ID and device information included in the L2 VLAN IF batch change information
   * @param vlanIfsMap
   *          Mapping of device ID and VLAN IF information included in L2VLAN IF batch change information
   * @return L2VLAN IF change information (for sending to EM)
   */
  public static L2SliceAddDelete toL2VlanIfBulkChange(BulkUpdateL2VlanIf input, Map<String, Nodes> nodesMap,
      Map<String, List<VlanIfs>> vlanIfsMap) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesMap + ", " + vlanIfsMap);

    L2SliceAddDelete ret = new L2SliceAddDelete();
    if (null == input.getVrfId()) {
      ret.setName(LogicalPhysicalConverter.toSliceName(input.getVni().toString()));
    } else {
      ret.setName(LogicalPhysicalConverter.toSliceName(input.getVrfId().toString()));
    }
    ret.setDeviceLeafList(new ArrayList<DeviceLeaf>());
    DeviceLeaf deviceLeaf = null;
    Cp cp = null;

    if (input.getVlanIfs() != null) {
      for (VlanIfsBulkUpdate inputVlanIfs : input.getVlanIfs()) {

        deviceLeaf = new DeviceLeaf();
        Nodes nodesDb = nodesMap.get(inputVlanIfs.getNodeId());
        deviceLeaf.setName(nodesDb.getNode_name());
        deviceLeaf.setCpList(new ArrayList<Cp>());

        VlanIfs vlanIfsDb = new VlanIfs();
        for (VlanIfs listElem : vlanIfsMap.get(inputVlanIfs.getNodeId())) {
          if (listElem.getVlan_if_id().equals(inputVlanIfs.getVlanIfId())) {
            vlanIfsDb = listElem;
            break;
          }
        }

        cp = new Cp();
        cp.setOperation("replace");
        cp.setName(vlanIfsDb.getIf_name());
        cp.setVlanId(new Long(vlanIfsDb.getVlan_id()));
        cp.setQos(changeQos(vlanIfsDb, nodesDb));
        deviceLeaf.getCpList().add(cp);

        boolean alreadyRegister = false;
        for (DeviceLeaf dl : ret.getDeviceLeafList()) {
          if (dl.getName().equals(deviceLeaf.getName())) {
            alreadyRegister = true;
            dl.getCpList().add(cp);
            break;
          }
        }

        if (!alreadyRegister) {
          ret.getDeviceLeafList().add(deviceLeaf);
        }
      }
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_L3VLAN IF Batch Change<br>
   * Convert L3VLAN IF batch change information into EM transmittable format.
   *
   * @param input
   *          L3VLAN IF batch change information
   * @param nodesMap
   *          Mapping the device ID and device information included in the L3 VLAN IF batch change information
   * @param vlanIfsMap
   *          Mapping of device ID and VLAN IF information included in L3VLAN IF batch change information
   * @return L3VLAN IF change information (for sending to EM)
   */
  public static L3SliceAddDelete toL3VlanIfBulkChange(BulkUpdateL3VlanIf input, Map<String, Nodes> nodesMap,
      Map<String, List<VlanIfs>> vlanIfsMap) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodesMap + ", " + vlanIfsMap);

    L3SliceAddDelete ret = new L3SliceAddDelete();
    ret.setName(LogicalPhysicalConverter.toSliceName(input.getVrfId().toString()));
    ret.setDeviceLeafList(new ArrayList<DeviceLeaf>());
    DeviceLeaf deviceLeaf = null;
    Cp cp = null;

    if (input.getVlanIfs() != null) {
      for (VlanIfsBulkUpdate inputVlanIfs : input.getVlanIfs()) {

        deviceLeaf = new DeviceLeaf();
        Nodes nodesDb = nodesMap.get(inputVlanIfs.getNodeId());
        deviceLeaf.setName(nodesDb.getNode_name());
        deviceLeaf.setCpList(new ArrayList<Cp>());

        VlanIfs vlanIfsDb = new VlanIfs();
        for (VlanIfs listElem : vlanIfsMap.get(inputVlanIfs.getNodeId())) {
          if (listElem.getVlan_if_id().equals(inputVlanIfs.getVlanIfId())) {
            vlanIfsDb = listElem;
            break;
          }
        }

        cp = new Cp();
        cp.setOperation("replace");
        cp.setName(vlanIfsDb.getIf_name());
        cp.setVlanId(new Long(vlanIfsDb.getVlan_id()));
        cp.setQos(changeQos(vlanIfsDb, nodesDb));
        deviceLeaf.getCpList().add(cp);

        boolean alreadyRegister = false;
        for (DeviceLeaf dl : ret.getDeviceLeafList()) {
          if (dl.getName().equals(deviceLeaf.getName())) {
            alreadyRegister = true;
            dl.getCpList().add(cp);
            break;
          }
        }

        if (!alreadyRegister) {
          ret.getDeviceLeafList().add(deviceLeaf);
        }
      }
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * QoS setting (VLANIF change／batch change).
   *
   * @param vlanIfs
   *          VLAN IF information
   * @param nodes
   *          device information
   * @return QoS setting(for sending to EM)
   */
  private static Qos changeQos(VlanIfs vlanIfs, Nodes nodes) {
    Qos qos = null;

    if (nodes.getEquipments().getQos_shaping_flg()) {
      qos = new Qos();
      int ifSpeed = LogicalPhysicalConverter.getVlanIfSpeed(vlanIfs, nodes);

      qos.setInflowShapingRate(new XmlFloatElement());
      qos.getInflowShapingRate().setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);
      if (vlanIfs.getInflow_shaping_rate() != null) {
        qos.getInflowShapingRate().setValue(vlanIfs.getInflow_shaping_rate() / (float) ifSpeed * (float) 100);
      }

      qos.setOutflowShapingRate(new XmlFloatElement());
      qos.getOutflowShapingRate().setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);
      if (vlanIfs.getOutflow_shaping_rate() != null) {
        qos.getOutflowShapingRate().setValue(vlanIfs.getOutflow_shaping_rate() / (float) ifSpeed * (float) 100);
      }

      qos.setEgressMenu(new XmlStringElement());
      qos.getEgressMenu().setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);
      if (vlanIfs.getEgress_queue_menu() == null) {
        qos.getEgressMenu().setValue(nodes.getEquipments().getDefault_egress_queue_menu());
      } else {
        qos.getEgressMenu().setValue(vlanIfs.getEgress_queue_menu());
      }
    }

    if (nodes.getEquipments().getQos_remark_flg()) {
      if (qos == null) {
        qos = new Qos();
      }
      qos.setRemarkMenu(new XmlStringElement());
      qos.getRemarkMenu().setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);
      if (vlanIfs.getRemark_menu() == null) {
        qos.getRemarkMenu().setValue(nodes.getEquipments().getDefault_remark_menu());
      } else {
        qos.getRemarkMenu().setValue(vlanIfs.getRemark_menu());
      }
    }

    return qos;
  }

  /**
   * EM Data Mapping_Generating breakoutIF<br>
   * Convert information for generating breakoutIF into EM transmittable format.
   *
   * @param input
   *          input information
   * @param nodes
   *          device information
   * @return information for generating breakout IF (for sending to EM)
   */
  public static BreakoutIfAddDelete toBreakoutIfCreate(CreateBreakoutIf input, Nodes nodes) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input + ", " + nodes);

    BreakoutIfAddDelete ret = new BreakoutIfAddDelete();

    ret.setName("breakout");
    ret.setDevice(new Device());

    ret.getDevice().setName(nodes.getNode_name());
    ret.getDevice().setBreakoutIfList(new ArrayList<BreakoutIf>());
    ret.getDevice().getBreakoutIfList().add(new BreakoutIf());

    int ifSpeed = Integer.parseInt(input.getSpeed().replaceAll("[^0-9]", "")) * input.getBreakoutIfId().size();
    String ifId = input.getBasePhysicalIfId();
    ret.getDevice().getBreakoutIfList().get(0)
        .setBaseIfName(getBaseBreakoutIfName(nodes.getEquipments(), ifSpeed, ifId));
    ret.getDevice().getBreakoutIfList().get(0).setSpeed(input.getSpeed());
    ret.getDevice().getBreakoutIfList().get(0).setBreakoutNum(input.getBreakoutIfId().size());

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * EM Data Mapping_breakoutIF Deletion<br>
   * Convert information for deleting breakoutIF into EM transmittable format.
   *
   * @param inputData
   *          REST input data
   * @param breakoutIfsList
   *          breakoutIF information list
   * @param nodes
   *          device information
   * @return information for deleting breakout IF (for sending to EM)
   */
  public static BreakoutIfAddDelete toBreakoutIfDelete(DeleteBreakoutIf inputData, List<BreakoutIfs> breakoutIfsList,
      Nodes nodes) {

    logger.trace(CommonDefinitions.START);
    logger.debug(breakoutIfsList + ", " + nodes);

    BreakoutIfAddDelete ret = new BreakoutIfAddDelete();

    ret.setName("breakout");
    ret.setDevice(new Device());
    ret.getDevice().setName(nodes.getNode_name());
    ret.getDevice().setBreakoutIfList(new ArrayList<BreakoutIf>());

    BreakoutIfs dbBoIfInfo = null;
    FIND: for (BreakoutIfs breakoutIfsDb : breakoutIfsList) {
      for (String boifid : inputData.getBreakoutIfId()) {
        if ((breakoutIfsDb.getBreakout_if_id().equals(boifid))) {
          dbBoIfInfo = breakoutIfsDb;
          break FIND;
        }
      }
    }
    if (dbBoIfInfo == null) {
      logger.debug("Not found breakoutIF information");
      throw new IllegalArgumentException();
    }

    int ifSpeed = Integer.parseInt(dbBoIfInfo.getSpeed().replaceAll("[^0-9]", "")) * inputData.getBreakoutIfId().size();
    String ifId = dbBoIfInfo.getPhysical_if_id();

    BreakoutIf breakoutIf = new BreakoutIf();
    breakoutIf.setOperation(CommonDefinitions.OPERATION_TYPE_DELETE);
    breakoutIf.setBaseIfName(getBaseBreakoutIfName(nodes.getEquipments(), ifSpeed, ifId));
    ret.getDevice().getBreakoutIfList().add(breakoutIf);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * breakout Original IF Name Acquisition.
   *
   * @param equipments
   *          model information DB
   * @param ifSpeed
   *          breakout original IF speed
   * @param ifId
   *          breakout original IF ID
   * @return breakout original IF name
   */
  private static String getBaseBreakoutIfName(Equipments equipments, int ifSpeed, String ifId) {
    logger.trace(CommonDefinitions.START);
    logger.debug(equipments);
    logger.debug(ifSpeed);
    logger.debug(ifId);

    String ret = null;

    String prefix = null;
    for (IfNameRules ifNameRules : equipments.getIfNameRulesList()) {
      int dbSpdInt = Integer.parseInt(ifNameRules.getSpeed().replaceAll("[^0-9]", ""));
      if (dbSpdInt == ifSpeed) {
        prefix = ifNameRules.getPort_prefix();
        break;
      }
    }

    String srotName = null;
    for (EquipmentIfs equipmentIfs : equipments.getEquipmentIfsList()) {
      int dbSpdInt = Integer.parseInt(equipmentIfs.getPort_speed().replaceAll("[^0-9]", ""));
      if (equipmentIfs.getPhysical_if_id().equals(ifId) && (dbSpdInt == ifSpeed)) {
        srotName = equipmentIfs.getIf_slot();
        break;
      }
    }

    if (prefix != null && srotName != null) {
      ret = prefix + srotName;
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.START);
    return ret;
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
  public static String getIfName(String ifType, String ifId, Nodes nodes) {
    String ret = "";

    logger.debug(ifType);
    logger.debug(ifId);
    logger.debug(nodes);

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
   * EM Data Mapping_Recovery node<br>
   *
   * @param input
   *          Input REST-POJO＜Additional service recovery＞
   * @param nodes
   *          Device information to be recovered（DB）
   * @param physicalIfNamesMap
   *          Mapping from physical IF name after restoration to pre-recovery physical IF name
   * @param lagIfNamesMap
   *          Mapping from physical IF name after restoration to pre-recovery LAG IF name
   * @return Generate NETCONF-POJO＜recovery node＞
   */
  public static RecoverUpdateNode toRecoverUpdateNode(RecoverNodeService input, Nodes nodes,
      Map<String, String> physicalIfNamesMap, Map<String, String> lagIfNamesMap) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input);
    logger.debug(nodes);
    logger.debug("physicalIfNamesMap = " + physicalIfNamesMap);
    logger.debug("lagIfNamesMap = " + lagIfNamesMap);

    RecoverUpdateNode ret = new RecoverUpdateNode();

    ret.setName("recover-node");
    ret.setDevice(new Device());
    if (nodes.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
      ret.getDevice().setName(CommonDefinitions.COREROUTER_HOSTNAME);
    } else {
      ret.getDevice().setName(nodes.getNode_name());
    }
    ret.getDevice().setNodeType(input.getNode().getNodeType());
    ret.getDevice().setEquipment(new Equipment());
    ret.getDevice().getEquipment().setPlatform(nodes.getEquipments().getPlatform_name());
    ret.getDevice().getEquipment().setOs(nodes.getEquipments().getOs_name());
    ret.getDevice().getEquipment().setFirmware(nodes.getEquipments().getFirmware_version());
    ret.getDevice().getEquipment().setLoginid(input.getNode().getUsername());
    ret.getDevice().getEquipment().setPassword(input.getNode().getPassword());

    ret.getDevice().setPhysiIfNames(new ArrayList<InterfaceNames>());
    for (String physiIfName : physicalIfNamesMap.keySet()) {
      InterfaceNames ifNameObj = new InterfaceNames();
      ifNameObj.setName(physiIfName);
      ifNameObj.setOldName(physicalIfNamesMap.get(physiIfName));
      ret.getDevice().getPhysiIfNames().add(ifNameObj);
    }
    if (ret.getDevice().getPhysiIfNames().isEmpty()) {
      ret.getDevice().setPhysiIfNames(null);
    }

    ret.getDevice().setLagIfNames(new ArrayList<InterfaceNames>());
    for (String lagIfsName : lagIfNamesMap.keySet()) {
      InterfaceNames ifNameObj = new InterfaceNames();
      ifNameObj.setName(lagIfsName);
      ifNameObj.setOldName(lagIfNamesMap.get(lagIfsName));
      ret.getDevice().getLagIfNames().add(ifNameObj);
    }
    if (ret.getDevice().getLagIfNames().isEmpty()) {
      ret.getDevice().setLagIfNames(null);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;

  }

  /**
   * EM Data Mapping_Recovery node<br>
   *
   * @param input
   *          Input REST-POJO＜Additional service recovery＞
   * @param oldEquipments
   *          Model information before recovery
   * @param nodes
   *          Device information to be recovered（DB）
   * @param physicalIfNamesMap
   *          Mapping from physical IF name after restoration to pre-recovery physical IF name
   * @param lagIfNamesMap
   *          Mapping from physical IF name after restoration to pre-recovery LAG IF name
   * @return Generate NETCONF-POJO＜recovery service＞
   */
  public static RecoverUpdateService toRecoverUpdateService(RecoverNodeService input, Equipments oldEquipments,
      Nodes nodes, Map<String, String> physicalIfNamesMap, Map<String, String> lagIfNamesMap) {

    logger.trace(CommonDefinitions.START);
    logger.debug(input);
    logger.debug(nodes);
    logger.debug("physicalIfNamesMap = " + physicalIfNamesMap);
    logger.debug("lagIfNamesMap = " + lagIfNamesMap);

    RecoverUpdateService ret = new RecoverUpdateService();

    ret.setName("recover-service");
    ret.setDevice(new Device());
    if (nodes.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
      ret.getDevice().setName(CommonDefinitions.COREROUTER_HOSTNAME);
    } else {
      ret.getDevice().setName(nodes.getNode_name());
    }
    ret.getDevice().setNodeType(input.getNode().getNodeType());
    ret.getDevice().setEquipment(new Equipment());
    ret.getDevice().getEquipment().setPlatform(nodes.getEquipments().getPlatform_name());
    ret.getDevice().getEquipment().setOs(nodes.getEquipments().getOs_name());
    ret.getDevice().getEquipment().setFirmware(nodes.getEquipments().getFirmware_version());
    ret.getDevice().getEquipment().setLoginid(input.getNode().getUsername());
    ret.getDevice().getEquipment().setPassword(input.getNode().getPassword());

    ret.getDevice().setPhysiIfNames(new ArrayList<InterfaceNames>());
    for (String physiIfName : physicalIfNamesMap.keySet()) {
      InterfaceNames ifNameObj = new InterfaceNames();
      ifNameObj.setName(physiIfName);
      ifNameObj.setOldName(physicalIfNamesMap.get(physiIfName));
      ret.getDevice().getPhysiIfNames().add(ifNameObj);
    }
    if (ret.getDevice().getPhysiIfNames().isEmpty()) {
      ret.getDevice().setPhysiIfNames(null);
    }

    ret.getDevice().setLagIfNames(new ArrayList<InterfaceNames>());
    for (String lagIfsName : lagIfNamesMap.keySet()) {
      InterfaceNames ifNameObj = new InterfaceNames();
      ifNameObj.setName(lagIfsName);
      ifNameObj.setOldName(lagIfNamesMap.get(lagIfsName));
      ret.getDevice().getLagIfNames().add(ifNameObj);
    }
    if (ret.getDevice().getLagIfNames().isEmpty()) {
      ret.getDevice().setLagIfNames(null);
    }

    Boolean shapingFlag = nodes.getEquipments().getQos_shaping_flg();
    Boolean remarkFlag = nodes.getEquipments().getQos_remark_flg();
    Boolean oldShapingFlag = oldEquipments.getQos_shaping_flg();
    Boolean oldRemarkFlag = oldEquipments.getQos_remark_flg();
    if (shapingFlag.equals(oldShapingFlag) && remarkFlag.equals(oldRemarkFlag)) {
      ret.getDevice().setQos(null);
    } else {
      ret.getDevice().setQos(new Qos());
    }
    if (!shapingFlag.equals(oldShapingFlag)) {
      ret.getDevice().getQos().setInflowShapingRate(new XmlFloatElement());
      ret.getDevice().getQos().setOutflowShapingRate(new XmlFloatElement());
      ret.getDevice().getQos().setEgressMenu(new XmlStringElement());
      if (shapingFlag) {
        ret.getDevice().getQos().setInflowShapingRate(null);
        ret.getDevice().getQos().setOutflowShapingRate(null);
        ret.getDevice().getQos().getEgressMenu().setValue(nodes.getEquipments().getDefault_egress_queue_menu());
      } else if (oldShapingFlag) {
        ret.getDevice().getQos().getInflowShapingRate().setOperation(CommonDefinitions.OPERATION_TYPE_DELETE);
        ret.getDevice().getQos().getOutflowShapingRate().setOperation(CommonDefinitions.OPERATION_TYPE_DELETE);
        ret.getDevice().getQos().getEgressMenu().setOperation(CommonDefinitions.OPERATION_TYPE_DELETE);
      }
    }
    if (!remarkFlag.equals(oldRemarkFlag)) {
      ret.getDevice().getQos().setRemarkMenu(new XmlStringElement());
      if (remarkFlag) {
        ret.getDevice().getQos().getRemarkMenu().setValue(nodes.getEquipments().getDefault_remark_menu());
      } else if (oldRemarkFlag) {
        ret.getDevice().getQos().getRemarkMenu().setOperation(CommonDefinitions.OPERATION_TYPE_DELETE);
      }
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);
    return ret;

  }

  /**
   * IRB instance generation POJO creation function.
   *
   * @param ipv4
   *          IP address
   * @param prefix
   *          ipv4 prefix
   * @param virtualGatewayAddress
   *          Virtual gateway address
   * @param isDummy
   *          dummy flag
   * @return  generation IRB pojo
   */
  private static Irb getIrb(String ipv4, int prefix, String virtualGatewayAddress, boolean isDummy) {
    StringBuilder buff = new StringBuilder();
    buff.append("8c:88");
    PhysicalIpAddress pip = new PhysicalIpAddress();
    VirtualGateway virtualGateway = new VirtualGateway();

    pip.setAddress(ipv4);
    pip.setPrefix((long) prefix);
    Irb irb = new Irb();
    irb.setPhysicalIpAddress(pip);

    if (null != virtualGatewayAddress) {
      String[] ip = ipv4.split("\\.");
      Integer.toHexString(Integer.parseInt(ip[0]));
      for (int i = 0; i < 4; i++) {
        buff.append(":");
        buff.append(CommonUtil.zeroPadding(Integer.toHexString(Integer.parseInt(ip[i])), 2));
      }
      irb.setVirtualMacAddress(buff.toString());
    }

    if (!isDummy) {
      virtualGateway.setAddress(virtualGatewayAddress);
      virtualGateway.setPrefix((long) prefix);
      irb.setVirtualGateway(virtualGateway);
    }

    return irb;
  }

  /**
   * EM data mapping to closed and open IF<br>
   * Converting IF open/closed information to EM-readable format
   *
   * @param nodeName
   *          node name
   * @param ifName
   *          IF name
   * @param ifType
   *          IF type
   * @param status
   *          IF status
   * @return information on IF open/closed（for EM sending）
   */
  public static IfStatusUpdate toIfStatusUpdate(String nodeName, String ifName, String ifType, String status) {
    logger.trace(CommonDefinitions.START);
    logger.debug("nodeName=" + nodeName + ", ifName=" + ifName + ", ifType=" + ifType + "status=" + status);

    IfStatusUpdate ret = new IfStatusUpdate();

    ret.setName("interface-condition");
    ret.setDevice(new Device());
    ret.getDevice().setName(nodeName);
    ret.getDevice().setIfStatusInfo(new InterfaceStatusUpdate());
    ret.getDevice().getIfStatusInfo().setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);
    ret.getDevice().getIfStatusInfo().setName(ifName);
    if (ifType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IFS)) {
      ret.getDevice().getIfStatusInfo().setType(CommonDefinitions.IF_TYPE_PHYSICAL_IFS);
    } else {
      ret.getDevice().getIfStatusInfo().setType(ifType);
    }
    ret.getDevice().getIfStatusInfo().setCondition(new XmlStringElement());
    ret.getDevice().getIfStatusInfo().getCondition().setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);

    if (status.equals(CommonDefinitions.IF_STATE_OK_STRING)) {
      ret.getDevice().getIfStatusInfo().getCondition().setValue(CommonDefinitions.PORT_CONDITION_ENABLE);
    } else {
      ret.getDevice().getIfStatusInfo().getCondition().setValue(CommonDefinitions.PORT_CONDITION_DISABLE);
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;
  }

  /**
   * EM data mapping to change LAG for CE<br>
   * Converting  LAG change information to  EM-readable format.
   *
   * @param action
   *           control type
   * @param nodes
   *          node information
   * @param lagIfs
   *          LAG information
   * @param minLinkNum
   *          minimum number of links
   * @param physicalIfsList
   *          physical IF information list
   * @param breakoutIfsList
   *          breakoutIF information list
   * @return LAG change information for CE (for sending to EM)
   */

  public static CeLagIfsChange toLagIfsChange(String action, Nodes nodes, LagIfs lagIfs, Integer minLinkNum,
      List<PhysicalIfs> physicalIfsList, List<BreakoutIfs> breakoutIfsList) {
    logger.trace(CommonDefinitions.START);
    logger.debug("action=" + action + ", nodes=" + nodes + ", lagIfs=" + lagIfs + ", minLinkNum=" + minLinkNum
        + ", physicalIfsList=" + physicalIfsList + ", breakoutIfsList=" + breakoutIfsList);

    List<LeafInterface> leafInterfaceList = new ArrayList<LeafInterface>();
    if (physicalIfsList != null) {
      for (PhysicalIfs physicalIfs : physicalIfsList) {
        LeafInterface leafInterface = new LeafInterface();
        if (action.equals(CommonDefinitions.OPERATION_TYPE_DELETE)) {
          leafInterface.setOperation(CommonDefinitions.OPERATION_TYPE_DELETE);
        }
        leafInterface.setName(physicalIfs.getIf_name());
        leafInterfaceList.add(leafInterface);
      }
    }
    if (breakoutIfsList != null) {
      for (BreakoutIfs breakoutIfs : breakoutIfsList) {
        LeafInterface leafInterface = new LeafInterface();
        if (action.equals(CommonDefinitions.OPERATION_TYPE_DELETE)) {
          leafInterface.setOperation(CommonDefinitions.OPERATION_TYPE_DELETE);
        }
        leafInterface.setName(breakoutIfs.getIf_name());
        leafInterfaceList.add(leafInterface);
      }
    }

    CeLagInterface ceLagInterface = new CeLagInterface();
    ceLagInterface.setName(lagIfs.getIf_name());
    ceLagInterface.setMinimumLinks(minLinkNum.longValue());
    ceLagInterface.setLeafInterfaceList(leafInterfaceList);
    ceLagInterface.setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);

    Device device = new Device();
    device.setName(nodes.getNode_name());
    List<Device> deviceList = new ArrayList<Device>();
    deviceList.add(device);
    List<CeLagInterface> ceLagInterfaceList = new ArrayList<CeLagInterface>();
    ceLagInterfaceList.add(ceLagInterface);
    device.setCeLagInterfaceList(ceLagInterfaceList);

    CeLagIfsChange ret = new CeLagIfsChange();
    final String ceLag = "ce-lag";
    ret.setName(ceLag);
    ret.setDevice(deviceList);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;
  }

  /**
   * EM data mapping to increse/decrease speed of internal link Lag <br>
   * Converting  information on LAG speed to  EM-readable format.
   *
   * @param action
   *          control type
   * @param nodes
   *          node information
   * @param lagIfs
   *          LAG information
   * @param minLinkNum
   *          minimum number of links
   * @param physicalIfsList
   *          phsical IF information list
   * @param breakoutIfsList
   *          breakoutIF information list
   * @return information to increse/decrease speed internal link（for sending to EM）
   */
  public static InternalLinkLagIfsChange toInternalLinkLagIfsChange(String action, Nodes nodes, LagIfs lagIfs,
      Integer minLinkNum, List<PhysicalIfs> physicalIfsList, List<BreakoutIfs> breakoutIfsList) {
    logger.trace(CommonDefinitions.START);
    logger.debug("action=" + action + ", nodes=" + nodes + ", lagIfs=" + lagIfs + ", minLinkNum=" + minLinkNum
        + ", physicalIfsList=" + physicalIfsList + ", breakoutIfsList=" + breakoutIfsList);

    List<InternalInterfaceMember> internalInterfaceMemberList = new ArrayList<InternalInterfaceMember>();
    if (physicalIfsList != null) {
      for (PhysicalIfs physicalIfs : physicalIfsList) {
        InternalInterfaceMember internalInterfaceMember = new InternalInterfaceMember();
        internalInterfaceMember.setName(physicalIfs.getIf_name());
        if (action.equals(CommonDefinitions.OPERATION_TYPE_DELETE)) {
          internalInterfaceMember.setOperation(CommonDefinitions.OPERATION_TYPE_DELETE);
        }
        internalInterfaceMemberList.add(internalInterfaceMember);
      }
    }
    if (breakoutIfsList != null) {
      for (BreakoutIfs breakoutIfs : breakoutIfsList) {
        InternalInterfaceMember internalInterfaceMember = new InternalInterfaceMember();
        internalInterfaceMember.setName(breakoutIfs.getIf_name());
        if (action.equals(CommonDefinitions.OPERATION_TYPE_DELETE)) {
          internalInterfaceMember.setOperation(CommonDefinitions.OPERATION_TYPE_DELETE);
        }
        internalInterfaceMemberList.add(internalInterfaceMember);
      }
    }

    InternalInterface internalInterface = new InternalInterface();
    internalInterface.setName(lagIfs.getIf_name());
    internalInterface.setType(CommonDefinitions.IF_TYPE_LAG_IF);
    internalInterface.setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);
    internalInterface.setMinimumLinks(minLinkNum.longValue());
    internalInterface.setInternalInterfaceMember(internalInterfaceMemberList);

    Device device = new Device();
    device.setName(nodes.getNode_name());
    List<InternalInterface> internalInterfaceList = new ArrayList<InternalInterface>();
    internalInterfaceList.add(internalInterface);
    device.setInternalLagList(internalInterfaceList);

    final String internalLink = "internal-link";
    InternalLinkLagIfsChange ret = new InternalLinkLagIfsChange();
    ret.setName(internalLink);
    List<Device> deviceList = new ArrayList<Device>();
    deviceList.add(device);
    ret.setDevice(deviceList);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;
  }

  /**
   * EM data mapping(to update node equiment information).
   *
   * @param nodesDb
   *          node information
   * @param newEqDb
   *          node equipment information
   * @return EM send message（to update node information）
   */
  public static UpdateNodeInfo toNodeInfoUpdate(Nodes nodesDb, Equipments newEqDb) {

    logger.trace(CommonDefinitions.START);

    UpdateNodeInfo updateNodeInfo = new UpdateNodeInfo();

    updateNodeInfo.setName("device-info");

    EquipmentWithOperation eq = new EquipmentWithOperation();

    XmlStringElement platform = new XmlStringElement();
    platform.setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);
    platform.setValue(newEqDb.getPlatform_name());
    eq.setPlatform(platform);
    XmlStringElement os = new XmlStringElement();
    os.setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);
    os.setValue(newEqDb.getOs_name());
    eq.setOs(os);
    XmlStringElement firmware = new XmlStringElement();
    firmware.setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);
    firmware.setValue(newEqDb.getFirmware_version());
    eq.setFirmware(firmware);
    Device device = new Device();
    device.setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);
    device.setName(nodesDb.getNode_name());
    device.setEquipmentWithOperation(eq);
    updateNodeInfo.setDevice(device);

    logger.debug(updateNodeInfo);
    logger.trace(CommonDefinitions.END);

    return updateNodeInfo;
  }
}
