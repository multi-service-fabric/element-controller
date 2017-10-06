package msf.ecmm.convert;

import static msf.ecmm.convert.LogicalPhysicalConverter.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.pojo.EquipmentIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.IfNameRules;
import msf.ecmm.db.pojo.InternalLinkIfs;
import msf.ecmm.db.pojo.L2Cps;
import msf.ecmm.db.pojo.L3Cps;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.NodesStartupNotification;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.AddLeaf;
import msf.ecmm.ope.receiver.pojo.AddSpine;
import msf.ecmm.ope.receiver.pojo.BulkCreateL2cp;
import msf.ecmm.ope.receiver.pojo.BulkCreateL3cp;
import msf.ecmm.ope.receiver.pojo.CreateLagInterface;
import msf.ecmm.ope.receiver.pojo.DeleteLeaf;
import msf.ecmm.ope.receiver.pojo.DeleteSpine;
import msf.ecmm.ope.receiver.pojo.RegisterEquipmentType;
import msf.ecmm.ope.receiver.pojo.RegisterLeaf;
import msf.ecmm.ope.receiver.pojo.RegisterSpine;
import msf.ecmm.ope.receiver.pojo.UpdatePhysicalInterface;
import msf.ecmm.ope.receiver.pojo.parts.CpsCreateL2cp;
import msf.ecmm.ope.receiver.pojo.parts.CpsCreateL3cp;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentIf;
import msf.ecmm.ope.receiver.pojo.parts.IfAddNode;
import msf.ecmm.ope.receiver.pojo.parts.IfNameRule;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkIfAddNode;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkIfDeleteNode;
import msf.ecmm.ope.receiver.pojo.parts.NodeAddNode;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodeAddLeaf;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodeAddSpine;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodeDeleteNode;
import msf.ecmm.ope.receiver.pojo.parts.PhysicalIfNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DbMapper {

	public static LagIfs toLagIfCreate(Nodes nodes, CreateLagInterface createLagIfs) {

		logger.trace(CommonDefinitions.START);
		logger.debug(nodes + ", " + createLagIfs);

		LagIfs ret = new LagIfs();
		Nodes regNodes = new Nodes();

		String ifName = toLagIfName(nodes.getEquipments().getLag_prefix(), createLagIfs.getLagIf().getLagIfId());

		regNodes.setNode_type(nodes.getNode_type());
		regNodes.setNode_id(nodes.getNode_id());

		ret.setNode_type(nodes.getNode_type());
		ret.setNode_id(nodes.getNode_id());
		ret.setLag_if_id(createLagIfs.getLagIf().getLagIfId());
		ret.setIf_name(ifName);
		ret.setNodes(regNodes);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static List<L2Cps> toL2CPCreate(BulkCreateL2cp bulkCreateL2cp, List<Nodes> nodesList) {

		logger.trace(CommonDefinitions.START);
		logger.debug(bulkCreateL2cp + ", " + nodesList);

		List<L2Cps> ret = new ArrayList<L2Cps>();

		for (CpsCreateL2cp cpsCreateL2cp : bulkCreateL2cp.getCps()) {

			Nodes regNodes = new Nodes();
			regNodes.setNode_type(toIntegerNodeType(cpsCreateL2cp.getBaseIf().getNodeType()));
			regNodes.setNode_id(cpsCreateL2cp.getBaseIf().getNodeId());
			L2Cps l2Cps = new L2Cps();
			l2Cps.setNodes(regNodes);
			l2Cps.setSlice_id(bulkCreateL2cp.getSliceId());
			l2Cps.setCp_id(cpsCreateL2cp.getCpId());
			l2Cps.setNode_type(toIntegerNodeType(cpsCreateL2cp.getBaseIf().getNodeType()));
			l2Cps.setNode_id(cpsCreateL2cp.getBaseIf().getNodeId());
			if (cpsCreateL2cp.getBaseIf().getType().equals("lag")) {
				l2Cps.setBase_lag_if_id(cpsCreateL2cp.getBaseIf().getIfId());
			}
			else {
				l2Cps.setBase_physical_if_id(cpsCreateL2cp.getBaseIf().getIfId());
			}
			for (Nodes nodes : nodesList) {
				if (cpsCreateL2cp.getBaseIf().getNodeId().equals(nodes.getNode_id()) &&
						toIntegerNodeType(cpsCreateL2cp.getBaseIf().getNodeType()) == nodes.getNode_type()) {
					if (cpsCreateL2cp.getBaseIf().getType().equals("lag")) {
						for (LagIfs lagIfs : nodes.getLagIfsList()) {
							if (cpsCreateL2cp.getBaseIf().getIfId().equals(lagIfs.getLag_if_id())) {
								l2Cps.setIf_name(lagIfs.getIf_name());
							}
						}
					}
					else {
						for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
							if (cpsCreateL2cp.getBaseIf().getIfId().equals(physicalIfs.getPhysical_if_id())) {
								l2Cps.setIf_name(physicalIfs.getIf_name());
							}
						}
					}
				}
			}
			l2Cps.setVlan_id(cpsCreateL2cp.getVlanId());
			ret.add(l2Cps);
		}

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static List<L3Cps> toL3CPCreate(BulkCreateL3cp bulkCreateL3cp, List<Nodes> nodesList) {

		logger.trace(CommonDefinitions.START);
		logger.debug(bulkCreateL3cp + ", " + nodesList);

		List<L3Cps> ret = new ArrayList<L3Cps>();

		for (CpsCreateL3cp cpsCreateL3cp : bulkCreateL3cp.getCps()) {

			Nodes regNodes = new Nodes();
			regNodes.setNode_type(toIntegerNodeType(cpsCreateL3cp.getBaseIf().getNodeType()));
			regNodes.setNode_id(cpsCreateL3cp.getBaseIf().getNodeId());
			L3Cps l3Cps = new L3Cps();
			l3Cps.setNodes(regNodes);
			l3Cps.setSlice_id(bulkCreateL3cp.getSliceId());
			l3Cps.setCp_id(cpsCreateL3cp.getCpId());
			l3Cps.setNode_type(toIntegerNodeType(cpsCreateL3cp.getBaseIf().getNodeType()));
			l3Cps.setNode_id(cpsCreateL3cp.getBaseIf().getNodeId());
			if (cpsCreateL3cp.getBaseIf().getType().equals("lag")) {
				l3Cps.setBase_lag_if_id(cpsCreateL3cp.getBaseIf().getIfId());
			}
			else {
				l3Cps.setBase_physical_if_id(cpsCreateL3cp.getBaseIf().getIfId());
			}
			for (Nodes nodes : nodesList) {
				if (cpsCreateL3cp.getBaseIf().getNodeId().equals(nodes.getNode_id()) &&
						toIntegerNodeType(cpsCreateL3cp.getBaseIf().getNodeType()) == nodes.getNode_type()) {

					if (cpsCreateL3cp.getBaseIf().getType().equals("lag")) {
						for (LagIfs lagIfs : nodes.getLagIfsList()) {
							if (cpsCreateL3cp.getBaseIf().getIfId().equals(lagIfs.getLag_if_id())) {
								l3Cps.setIf_name(lagIfs.getIf_name());
							}
						}
					}
					else {
						for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
							if (cpsCreateL3cp.getBaseIf().getIfId().equals(physicalIfs.getPhysical_if_id())) {
								l3Cps.setIf_name(physicalIfs.getIf_name());
							}
						}
					}
				}
			}
			l3Cps.setVlan_id(cpsCreateL3cp.getVlanId());
			ret.add(l3Cps);
		}

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

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

		Set<EquipmentIfs> equipmentIfsList = new HashSet<EquipmentIfs>();
		for (EquipmentIf eif : registerEquipmentTypeRest.getEquipment().getEquipmentIfs()) {
			EquipmentIfs equipmentIfs = new EquipmentIfs();
			equipmentIfs.setEquipment_type_id(registerEquipmentTypeRest.getEquipment().getEquipmentTypeId());
			equipmentIfs.setPhysical_if_id(eif.getPhysicalIfId());
			equipmentIfs.setIf_slot(eif.getIfSlot());
			equipmentIfs.setEquipments(ret);
			equipmentIfsList.add(equipmentIfs);
		}
		ret.setEquipmentIfsList(equipmentIfsList);

		Set<IfNameRules> ifNameRulesList = new HashSet<IfNameRules>();
		for (IfNameRule inr : registerEquipmentTypeRest.getEquipment().getIfNameRules()) {
			IfNameRules ifNameRules = new IfNameRules();
			ifNameRules.setEquipment_type_id(registerEquipmentTypeRest.getEquipment().getEquipmentTypeId());
			ifNameRules.setSpeed(inr.getSpeed());
			ifNameRules.setPort_prefix(inr.getPortPrefix());
			ifNameRules.setEquipments(ret);
			ifNameRulesList.add(ifNameRules);
		}
		ret.setIfNameRulesList(ifNameRulesList);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static Nodes toLeafinfoCreate(RegisterLeaf registerLeafRest, int clusterId, Equipments equipments) {
		logger.trace(CommonDefinitions.START);
		logger.debug(registerLeafRest + ", clusterId=" + clusterId);

		Nodes ret = new Nodes();


		ret.setEquipments(equipments);
		ret.setNode_type(toIntegerNodeType("leafs"));
		ret.setNode_id(registerLeafRest.getNode().getNodeId());
		ret.setEquipment_type_id(registerLeafRest.getEquipment().getEquipmentTypeId());
		ret.setNode_name(toNodeName("Leaf", clusterId, registerLeafRest.getNode().getNodeId()));
		ret.setManagement_if_address(registerLeafRest.getNode().getManagementInterface().getAddress());
		ret.setSnmp_community(registerLeafRest.getNode().getSnmpCommunity());

		Set<NodesStartupNotification> nodesStartupNotificationSet = new HashSet<NodesStartupNotification>();
		NodesStartupNotification nodesStartupNotification = new NodesStartupNotification();
		nodesStartupNotification.setNode_type(toIntegerNodeType("leafs"));
		nodesStartupNotification.setNode_id(registerLeafRest.getNode().getNodeId());
		nodesStartupNotification.setNotification_reception_status(3);
		nodesStartupNotification.setNodes(ret);
		nodesStartupNotificationSet.add(nodesStartupNotification);
		ret.setNodesStartupNotificationList(nodesStartupNotificationSet);

		Set<PhysicalIfs> physicalList = new HashSet<PhysicalIfs>();

		for (EquipmentIfs list : equipments.getEquipmentIfsList()) {
			PhysicalIfs physicalIfs = new PhysicalIfs();
			physicalIfs.setNode_type(toIntegerNodeType("leafs"));
			physicalIfs.setNode_id(registerLeafRest.getNode().getNodeId());
			physicalIfs.setPhysical_if_id(list.getPhysical_if_id());
			physicalIfs.setNodes(ret);
			physicalList.add(physicalIfs);
		}

		ret.setPhysicalIfsList(physicalList);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static Nodes toSpineinfoCreate(RegisterSpine registerSpineRest, int clusterId, Equipments equipments) {
		logger.trace(CommonDefinitions.START);
		logger.debug(registerSpineRest + ", clusterId=" + clusterId);

		Nodes ret = new Nodes();


		ret.setEquipments(equipments);
		ret.setNode_type(toIntegerNodeType("spines"));
		ret.setNode_id(registerSpineRest.getNode().getNodeId());
		ret.setEquipment_type_id(registerSpineRest.getEquipment().getEquipmentTypeId());
		ret.setNode_name(toNodeName("Spine", clusterId, registerSpineRest.getNode().getNodeId()));
		ret.setManagement_if_address(registerSpineRest.getNode().getManagementInterface().getAddress());
		ret.setSnmp_community(registerSpineRest.getNode().getSnmpCommunity());

		Set<NodesStartupNotification> nodesStartupNotificationSet = new HashSet<NodesStartupNotification>();
		NodesStartupNotification nodesStartupNotification = new NodesStartupNotification();
		nodesStartupNotification.setNode_type(toIntegerNodeType("spines"));
		nodesStartupNotification.setNode_id(registerSpineRest.getNode().getNodeId());
		nodesStartupNotification.setNotification_reception_status(3);
		nodesStartupNotification.setNodes(ret);
		nodesStartupNotificationSet.add(nodesStartupNotification);
		ret.setNodesStartupNotificationList(nodesStartupNotificationSet);

		Set<PhysicalIfs> physicalList = new HashSet<PhysicalIfs>();

		for (EquipmentIfs list : equipments.getEquipmentIfsList()) {
			PhysicalIfs physicalIfs = new PhysicalIfs();
			physicalIfs.setNode_type(toIntegerNodeType("spines"));
			physicalIfs.setNode_id(registerSpineRest.getNode().getNodeId());
			physicalIfs.setPhysical_if_id(list.getPhysical_if_id());
			physicalIfs.setNodes(ret);
			physicalList.add(physicalIfs);
		}

		ret.setPhysicalIfsList(physicalList);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static PhysicalIfs toPhyIfChange(UpdatePhysicalInterface updatePhysicalInterface, int nodeType,
			String nodeId, String physicalIfId, Equipments equipments) throws IllegalArgumentException {

		logger.trace(CommonDefinitions.START);
		logger.debug(updatePhysicalInterface + ", nodeType=" + nodeType + ", nodeId" + nodeId +
				", physicalIfId=" + physicalIfId + ", " + equipments);

		Nodes nodes = new Nodes();
		nodes.setNode_type(nodeType);
		nodes.setNode_id(nodeId);
		PhysicalIfs ret = new PhysicalIfs();
		ret.setNodes(nodes);
		ret.setNode_type(nodeType);
		ret.setNode_id(nodeId);
		ret.setPhysical_if_id(physicalIfId);

		String prefix = null;
		String slot = null;
		for (IfNameRules ifNameRules : equipments.getIfNameRulesList()) {
			if (ifNameRules.getSpeed().equals(updatePhysicalInterface.getSpeed())) {
				prefix = ifNameRules.getPort_prefix();
				break;
			}
		}
		for (EquipmentIfs equipmentIfs : equipments.getEquipmentIfsList()) {
			if (equipmentIfs.getPhysical_if_id().equals(physicalIfId)) {
				slot = equipmentIfs.getIf_slot();
				break;
			}
		}
		if (prefix != null && slot != null) {
			ret.setIf_name(toPhysicalIfName(prefix, slot));
		}
		else {
			throw new IllegalArgumentException();
		}

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static List<Nodes> toLeafAdd(AddLeaf inData, int clusterId, Equipments equipments, Nodes nodes,
			List<Nodes> oppoNodeList)
			throws IllegalArgumentException {

		logger.trace(CommonDefinitions.START);
		logger.debug(inData + ", clusterId=" + clusterId + ", " + equipments + ", " + nodes);

		List<Nodes> ret = new ArrayList<Nodes>();
		Nodes retNodes = null;
		Nodes preNodes = null;
		String nodeType = "leafs";
		String oppositeNodeType = "spines";

		Equipments regEquipments = new Equipments();
		regEquipments.setEquipment_type_id(inData.getEquipment().getEquipmentTypeId());

		if (clusterId != CommonDefinitions.NOT_SET) {

			preNodes = new Nodes();
			preNodes.setEquipments(regEquipments);
			preNodes.setNode_type(toIntegerNodeType(nodeType));
			preNodes.setNode_id(inData.getNode().getNodeId());
			preNodes.setEquipment_type_id(inData.getEquipment().getEquipmentTypeId());
			preNodes.setNode_name(toNodeName("Leaf", clusterId, inData.getNode().getNodeId()));
			preNodes.setManagement_if_address(inData.getNode().getManagementInterface().getAddress());
			preNodes.setSnmp_community(inData.getNode().getSnmpCommunity());


			Set<PhysicalIfs> physicalList = new HashSet<PhysicalIfs>();

			for (EquipmentIfs list : equipments.getEquipmentIfsList()) {
				PhysicalIfs physicalIfs = new PhysicalIfs();
				physicalIfs.setNode_type(toIntegerNodeType("leafs"));
				physicalIfs.setNode_id(inData.getNode().getNodeId());
				physicalIfs.setPhysical_if_id(list.getPhysical_if_id());
				physicalIfs.setNodes(preNodes);
				physicalList.add(physicalIfs);
			}
			preNodes.setPhysicalIfsList(physicalList);

			retNodes = setRelatedNodes(inData, preNodes, equipments, nodeType);

			ret = setOppositeNodes(inData, oppositeNodeType, clusterId, oppoNodeList);
		}
		else {

			preNodes = new Nodes();
			preNodes.setEquipments(nodes.getEquipments());
			preNodes.setNode_type(nodes.getNode_type());
			preNodes.setNode_id(nodes.getNode_id());
			preNodes.setEquipment_type_id(nodes.getEquipment_type_id());
			preNodes.setNode_name(nodes.getNode_name());
			preNodes.setManagement_if_address(nodes.getManagement_if_address());
			preNodes.setSnmp_community(nodes.getSnmp_community());
			Set<PhysicalIfs> physicalList = new HashSet<PhysicalIfs>();
			for(PhysicalIfs physi : nodes.getPhysicalIfsList()){
				PhysicalIfs physicalIfs = new PhysicalIfs();
				physicalIfs.setNode_type(nodes.getNode_type());
				physicalIfs.setNode_id(nodes.getNode_id());
				physicalIfs.setPhysical_if_id(physi.getPhysical_if_id());
				physicalIfs.setNodes(preNodes);
				physicalList.add(physicalIfs);
			}
			preNodes.setPhysicalIfsList(physicalList);

			retNodes = setRelatedNodes(inData, preNodes, nodes.getEquipments(), nodeType);

			ret = setOppositeNodes(inData, oppositeNodeType, clusterId, oppoNodeList);
		}
		ret.add(retNodes);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static List<Nodes> toSpineAdd(AddSpine inData, int clusterId, Equipments equipments, Nodes nodes,
			List<Nodes> oppoNodeList)
			throws IllegalArgumentException {

		logger.trace(CommonDefinitions.START);
		logger.debug(inData + ", clusterId=" + clusterId + ", " + equipments + ", " + nodes);

		List<Nodes> ret = new ArrayList<Nodes>();
		Nodes retNodes = null;
		Nodes preNodes = null;
		String nodeType = "spines";
		String oppositeNodeType = "leafs";

		Equipments regEquipments = new Equipments();
		regEquipments.setEquipment_type_id(inData.getEquipment().getEquipmentTypeId());

		if (clusterId != CommonDefinitions.NOT_SET) {

			preNodes = new Nodes();
			preNodes.setEquipments(regEquipments);
			preNodes.setNode_type(toIntegerNodeType(nodeType));
			preNodes.setNode_id(inData.getNode().getNodeId());
			preNodes.setEquipment_type_id(inData.getEquipment().getEquipmentTypeId());
			preNodes.setNode_name(toNodeName("Spine", clusterId, inData.getNode().getNodeId()));
			preNodes.setManagement_if_address(inData.getNode().getManagementInterface().getAddress());
			preNodes.setSnmp_community(inData.getNode().getSnmpCommunity());


			Set<PhysicalIfs> physicalList = new HashSet<PhysicalIfs>();

			for (EquipmentIfs list : equipments.getEquipmentIfsList()) {
				PhysicalIfs physicalIfs = new PhysicalIfs();
				physicalIfs.setNode_type(toIntegerNodeType("leafs"));
				physicalIfs.setNode_id(inData.getNode().getNodeId());
				physicalIfs.setPhysical_if_id(list.getPhysical_if_id());
				physicalIfs.setNodes(preNodes);
				physicalList.add(physicalIfs);
			}
			preNodes.setPhysicalIfsList(physicalList);

			retNodes = setRelatedNodes(inData, preNodes, equipments, nodeType);

			ret = setOppositeNodes(inData, oppositeNodeType, clusterId, oppoNodeList);
		}
		else {

			preNodes = new Nodes();
			preNodes.setEquipments(nodes.getEquipments());
			preNodes.setNode_type(nodes.getNode_type());
			preNodes.setNode_id(nodes.getNode_id());
			preNodes.setEquipment_type_id(nodes.getEquipment_type_id());
			preNodes.setNode_name(nodes.getNode_name());
			preNodes.setManagement_if_address(nodes.getManagement_if_address());
			preNodes.setSnmp_community(nodes.getSnmp_community());
			Set<PhysicalIfs> physicalList = new HashSet<PhysicalIfs>();
			for(PhysicalIfs physi : nodes.getPhysicalIfsList()){
				PhysicalIfs physicalIfs = new PhysicalIfs();
				physicalIfs.setNode_type(nodes.getNode_type());
				physicalIfs.setNode_id(nodes.getNode_id());
				physicalIfs.setPhysical_if_id(physi.getPhysical_if_id());
				physicalIfs.setNodes(preNodes);
				physicalList.add(physicalIfs);
			}
			preNodes.setPhysicalIfsList(physicalList);

			retNodes = setRelatedNodes(inData, preNodes, nodes.getEquipments(), nodeType);

			ret = setOppositeNodes(inData, oppositeNodeType, clusterId, oppoNodeList);
		}
		ret.add(retNodes);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	private static Nodes setRelatedNodes(AbstractRestMessage inData, Nodes nodes, Equipments equipments, String nodeType) {

		logger.trace(CommonDefinitions.START);

		NodeAddNode nodeAddNode = null;
		IfAddNode ifAddNode = null;

		if (inData instanceof AddLeaf) {
			AddLeaf addLeaf = (AddLeaf) inData;
			nodeAddNode = addLeaf.getNode();
			ifAddNode = addLeaf.getIfs();
		}
		else {
			AddSpine addSpine = (AddSpine) inData;
			nodeAddNode = addSpine.getNode();
			ifAddNode = addSpine.getIfs();
		}

		Nodes ret = nodes;

		Set<LagIfs> lagIfsSet = new HashSet<LagIfs>();

		for (InternalLinkIfAddNode internalLinkIfAddNode : ifAddNode.getInternalLinkIfs()) {

			for (PhysicalIfNode physicalIfNode : internalLinkIfAddNode.getLagIf().getPhysicalIfs()) {
				for (PhysicalIfs physi : ret.getPhysicalIfsList()) {
					if (physi.getPhysical_if_id().equals(physicalIfNode.getPhysicalIfId())) {

						String prefix = null;
						String slot = null;
						for (IfNameRules ifNameRules : equipments.getIfNameRulesList()) {
							if (ifNameRules.getSpeed().equals(internalLinkIfAddNode.getLagIf().getLinkSpeed())) {
								prefix = ifNameRules.getPort_prefix();
								break;
							}
						}
						for (EquipmentIfs equipmentIfs : equipments.getEquipmentIfsList()) {
							if (equipmentIfs.getPhysical_if_id().equals(physicalIfNode.getPhysicalIfId())) {
								slot = equipmentIfs.getIf_slot();
								break;
							}
						}
						if (prefix != null && slot != null) {
							physi.setIf_name(toPhysicalIfName(prefix, slot));
						}
						else {
							throw new IllegalArgumentException();
						}
					}
				}
			}

			LagIfs lagIfs = new LagIfs();
			lagIfs.setNode_type(toIntegerNodeType(nodeType));
			lagIfs.setNode_id(nodeAddNode.getNodeId());
			lagIfs.setLag_if_id(internalLinkIfAddNode.getLagIf().getLagIfId());
			lagIfs.setIf_name(toLagIfName(equipments.getLag_prefix(), internalLinkIfAddNode.getLagIf().getLagIfId()));
			lagIfs.setNodes(ret);

			Set<InternalLinkIfs> internalLinkIfsSet = new HashSet<InternalLinkIfs>();
			InternalLinkIfs internalLinkIfs = new InternalLinkIfs();
			internalLinkIfs.setInternal_link_if_id(internalLinkIfAddNode.getInternalLinkIfId());
			internalLinkIfs.setNode_type(toIntegerNodeType(nodeType));
			internalLinkIfs.setNode_id(nodeAddNode.getNodeId());
			internalLinkIfs.setLag_if_id(internalLinkIfAddNode.getLagIf().getLagIfId());
			internalLinkIfs.setLagIfs(lagIfs);
			internalLinkIfs.setNodes(ret);

			internalLinkIfsSet.add(internalLinkIfs);

			lagIfs.setInternalLinkIfsList(internalLinkIfsSet);

			lagIfsSet.add(lagIfs);
		}
		ret.setLagIfsList(lagIfsSet);

		logger.trace(CommonDefinitions.END);
		return ret;
	}

	private static List<Nodes> setOppositeNodes(AbstractRestMessage inData, String oppositeNodeType, int clusterId,
			List<Nodes> oppoNodeList) {

		logger.trace(CommonDefinitions.START);

		List<Nodes> ret = new ArrayList<Nodes>();
		String nodeNamePrefix = null;
		NodeAddNode nodeAddNode = null;
		ArrayList<OppositeNodeAddLeaf> opNodeAddLeaf = new ArrayList<OppositeNodeAddLeaf>();
		ArrayList<OppositeNodeAddSpine> opNodeAddSpine = new ArrayList<OppositeNodeAddSpine>();

		if (inData instanceof AddLeaf) {
			AddLeaf addLeaf = (AddLeaf) inData;
			nodeAddNode = addLeaf.getNode();
			opNodeAddLeaf = addLeaf.getOppositeNodes();
			nodeNamePrefix = "Spine";
		}
		else {
			AddSpine addSpine = (AddSpine) inData;
			nodeAddNode = addSpine.getNode();
			opNodeAddSpine = addSpine.getOppositeNodes();
			nodeNamePrefix = "Leaf";
		}

		for (OppositeNodeAddLeaf opNode : opNodeAddLeaf) {

			int opNodeType = toIntegerNodeType(oppositeNodeType);

			Nodes nodes = new Nodes();
			nodes.setNode_type(opNodeType);
			nodes.setNode_id(opNode.getNodeId());

			Equipments regEquipments = new Equipments();

			for (Nodes oppoNodes : oppoNodeList) {
				if (oppoNodes.getNode_id().equals(opNode.getNodeId()) && (oppoNodes.getNode_type() == opNodeType)) {
					regEquipments.setEquipment_type_id(oppoNodes.getEquipments().getEquipment_type_id());
					nodes.setEquipment_type_id(oppoNodes.getEquipments().getEquipment_type_id());
					nodes.setEquipments(regEquipments);

					if (clusterId != CommonDefinitions.NOT_SET) {
						nodes.setNode_name(toNodeName(nodeNamePrefix, clusterId, opNode.getNodeId()));
						nodes.setManagement_if_address(nodeAddNode.getManagementInterface().getAddress());
						nodes.setSnmp_community(nodeAddNode.getSnmpCommunity());
					}

					Set<NodesStartupNotification> nodesStartupNotificationSet = new HashSet<NodesStartupNotification>();
					NodesStartupNotification nodesStartupNotification = new NodesStartupNotification();
					nodesStartupNotification.setNode_type(opNodeType);
					nodesStartupNotification.setNode_id(opNode.getNodeId());
					nodesStartupNotification.setNotification_reception_status(3);
					nodesStartupNotification.setNodes(nodes);
					nodesStartupNotificationSet.add(nodesStartupNotification);
					nodes.setNodesStartupNotificationList(nodesStartupNotificationSet);

					Set<LagIfs> lagIfsSet = new HashSet<LagIfs>();
					LagIfs lagIfs = new LagIfs();
					lagIfs.setNode_type(opNodeType);
					lagIfs.setNode_id(opNode.getNodeId());
					lagIfs.setLag_if_id(opNode.getInternalLinkIf().getLagIf().getLagIfId());
					lagIfs.setIf_name(toLagIfName(oppoNodes.getEquipments().getLag_prefix(),
							opNode.getInternalLinkIf().getLagIf().getLagIfId()));
					lagIfs.setNodes(nodes);

					Set<PhysicalIfs> physicalList = new HashSet<PhysicalIfs>();
					for (PhysicalIfNode physiList : opNode.getInternalLinkIf().getLagIf().getPhysicalIfs()) {
						PhysicalIfs physicalIfs = new PhysicalIfs();
						physicalIfs.setNode_type(opNodeType);
						physicalIfs.setNode_id(opNode.getNodeId());
						physicalIfs.setPhysical_if_id(physiList.getPhysicalIfId());
						physicalIfs.setNodes(nodes);

						String prefix = null;
						String slot = null;
						for (IfNameRules ifNameRules : oppoNodes.getEquipments().getIfNameRulesList()) {
							if (ifNameRules.getSpeed().equals(opNode.getInternalLinkIf().getLagIf().getLinkSpeed())) {
								prefix = ifNameRules.getPort_prefix();
								break;
							}
						}
						for (EquipmentIfs equipmentIfs : oppoNodes.getEquipments().getEquipmentIfsList()) {
							if (equipmentIfs.getPhysical_if_id().equals(physiList.getPhysicalIfId())) {
								slot = equipmentIfs.getIf_slot();
								break;
							}
						}
						if (prefix != null && slot != null) {
							physicalIfs.setIf_name(toPhysicalIfName(prefix, slot));
						}
						else {
							throw new IllegalArgumentException();
						}

						physicalList.add(physicalIfs);
					}

					Set<InternalLinkIfs> internalLinkIfsSet = new HashSet<InternalLinkIfs>();
					InternalLinkIfs internalLinkIfs = new InternalLinkIfs();
					internalLinkIfs.setInternal_link_if_id(opNode.getInternalLinkIf().getInternalLinkIfId());
					internalLinkIfs.setNode_type(opNodeType);
					internalLinkIfs.setNode_id(opNode.getNodeId());
					internalLinkIfs.setLag_if_id(opNode.getInternalLinkIf().getLagIf().getLagIfId());
					internalLinkIfs.setLagIfs(lagIfs);
					internalLinkIfs.setNodes(nodes);
					internalLinkIfsSet.add(internalLinkIfs);

					lagIfs.setInternalLinkIfsList(internalLinkIfsSet);
					lagIfsSet.add(lagIfs);

					nodes.setLagIfsList(lagIfsSet);
					nodes.setPhysicalIfsList(physicalList);
					ret.add(nodes);
				}
			}
		}

		for (OppositeNodeAddSpine opNode : opNodeAddSpine) {

			int opNodeType = toIntegerNodeType(oppositeNodeType);

			Nodes nodes = new Nodes();
			nodes.setNode_type(opNodeType);
			nodes.setNode_id(opNode.getNodeId());

			Equipments regEquipments = new Equipments();

			for (Nodes oppoNodes : oppoNodeList) {
				if (oppoNodes.getNode_id().equals(opNode.getNodeId()) && (oppoNodes.getNode_type() == opNodeType)) {

					regEquipments.setEquipment_type_id(oppoNodes.getEquipments().getEquipment_type_id());
					nodes.setEquipments(regEquipments);
					nodes.setEquipment_type_id(oppoNodes.getEquipments().getEquipment_type_id());

					if (clusterId != CommonDefinitions.NOT_SET) {
						nodes.setNode_name(toNodeName(nodeNamePrefix, clusterId, opNode.getNodeId()));
						nodes.setManagement_if_address(nodeAddNode.getManagementInterface().getAddress());
						nodes.setSnmp_community(nodeAddNode.getSnmpCommunity());
					}
					Set<NodesStartupNotification> nodesStartupNotificationSet = new HashSet<NodesStartupNotification>();
					NodesStartupNotification nodesStartupNotification = new NodesStartupNotification();
					nodesStartupNotification.setNode_type(opNodeType);
					nodesStartupNotification.setNode_id(opNode.getNodeId());
					nodesStartupNotification.setNotification_reception_status(3);
					nodesStartupNotification.setNodes(nodes);
					nodesStartupNotificationSet.add(nodesStartupNotification);
					nodes.setNodesStartupNotificationList(nodesStartupNotificationSet);

					Set<LagIfs> lagIfsSet = new HashSet<LagIfs>();
					LagIfs lagIfs = new LagIfs();
					lagIfs.setNode_type(opNodeType);
					lagIfs.setNode_id(opNode.getNodeId());
					lagIfs.setLag_if_id(opNode.getInternalLinkIfs().getLagIf().getLagIfId());
					lagIfs.setIf_name(toLagIfName(oppoNodes.getEquipments().getLag_prefix(),
							opNode.getInternalLinkIfs().getLagIf().getLagIfId()));
					lagIfs.setNodes(nodes);

					Set<PhysicalIfs> physicalList = new HashSet<PhysicalIfs>();
					for (PhysicalIfNode physiList : opNode.getInternalLinkIfs().getLagIf().getPhysicalIfs()) {
						PhysicalIfs physicalIfs = new PhysicalIfs();
						physicalIfs.setNode_type(opNodeType);
						physicalIfs.setNode_id(opNode.getNodeId());
						physicalIfs.setPhysical_if_id(physiList.getPhysicalIfId());
						physicalIfs.setNodes(nodes);

						String prefix = null;
						String slot = null;
						for (IfNameRules ifNameRules : oppoNodes.getEquipments().getIfNameRulesList()) {
							if (ifNameRules.getSpeed().equals(opNode.getInternalLinkIfs().getLagIf().getLinkSpeed())) {
								prefix = ifNameRules.getPort_prefix();
								break;
							}
						}
						for (EquipmentIfs equipmentIfs : oppoNodes.getEquipments().getEquipmentIfsList()) {
							if (equipmentIfs.getPhysical_if_id().equals(physiList.getPhysicalIfId())) {
								slot = equipmentIfs.getIf_slot();
								break;
							}
						}
						if (prefix != null && slot != null) {
							physicalIfs.setIf_name(toPhysicalIfName(prefix, slot));
						}
						else {
							throw new IllegalArgumentException();
						}

						physicalList.add(physicalIfs);
					}

					Set<InternalLinkIfs> internalLinkIfsSet = new HashSet<InternalLinkIfs>();
					InternalLinkIfs internalLinkIfs = new InternalLinkIfs();
					internalLinkIfs.setInternal_link_if_id(opNode.getInternalLinkIfs().getInternalLinkIfId());
					internalLinkIfs.setNode_type(opNodeType);
					internalLinkIfs.setNode_id(opNode.getNodeId());
					internalLinkIfs.setLag_if_id(opNode.getInternalLinkIfs().getLagIf().getLagIfId());
					internalLinkIfs.setLagIfs(lagIfs);
					internalLinkIfs.setNodes(nodes);
					internalLinkIfsSet.add(internalLinkIfs);

					lagIfs.setInternalLinkIfsList(internalLinkIfsSet);
					lagIfsSet.add(lagIfs);

					nodes.setLagIfsList(lagIfsSet);
					nodes.setPhysicalIfsList(physicalList);
					ret.add(nodes);
				}
			}
		}

		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static List<Nodes> toLeafOppositeNodesReduced(DeleteLeaf inData) {

		logger.trace(CommonDefinitions.START);
		logger.debug(inData);

		List<Nodes> nodeList = new ArrayList<Nodes>();

		for (OppositeNodeDeleteNode oppositeNodes : inData.getOppositeNodes()) {

			Nodes nodes = new Nodes();

			nodes.setNode_type(toIntegerNodeType("spines"));
			nodes.setNode_id(oppositeNodes.getNodeId());

			nodes = setDeleteOppositeNodes(oppositeNodes.getInternalLinkIfs(), nodes);
			nodeList.add(nodes);
		}

		logger.debug(nodeList);
		logger.trace(CommonDefinitions.END);

		return nodeList;
	}

	public static List<Nodes> toSpineOppositeNodesReduced(DeleteSpine inData) {

		logger.trace(CommonDefinitions.START);
		logger.debug(inData);

		List<Nodes> nodeList = new ArrayList<Nodes>();

		for (OppositeNodeDeleteNode oppositeNodes : inData.getOppositeNodes()) {

			Nodes nodes = new Nodes();

			nodes.setNode_type(toIntegerNodeType("leafs"));
			nodes.setNode_id(oppositeNodes.getNodeId());

			nodes = setDeleteOppositeNodes(oppositeNodes.getInternalLinkIfs(), nodes);
			nodeList.add(nodes);
		}

		logger.debug(nodeList);
		logger.trace(CommonDefinitions.END);

		return nodeList;
	}

	private static Nodes setDeleteOppositeNodes(InternalLinkIfDeleteNode internalLinkIfs, Nodes nodes) {

		Nodes ret = nodes;

		Set<InternalLinkIfs> internalLinkList = new HashSet<InternalLinkIfs>();
		Set<LagIfs> lagList = new HashSet<LagIfs>();
		Set<PhysicalIfs> physicalList = new HashSet<PhysicalIfs>();

		LagIfs lagIfs = new LagIfs();
		InternalLinkIfs internalLink = new InternalLinkIfs();

		lagIfs.setNode_type(nodes.getNode_type());
		lagIfs.setNode_id(nodes.getNode_id());
		lagIfs.setLag_if_id(internalLinkIfs.getLagIf().getLagIfId());
		lagIfs.setNodes(nodes);

		internalLink.setInternal_link_if_id(internalLinkIfs.getInternalLinkIfs());
		internalLink.setLagIfs(lagIfs);
		internalLink.setNodes(nodes);
		internalLinkList.add(internalLink);

		lagIfs.setInternalLinkIfsList(internalLinkList);
		lagList.add(lagIfs);

		for (PhysicalIfNode physicalIfNode : internalLinkIfs.getLagIf().getPhysicalIfs()) {

			PhysicalIfs physicalIfs = new PhysicalIfs();

			physicalIfs.setNode_type(nodes.getNode_type());
			physicalIfs.setNode_id(nodes.getNode_id());
			physicalIfs.setPhysical_if_id(physicalIfNode.getPhysicalIfId());
			physicalIfs.setIf_name(null);
			physicalIfs.setNodes(nodes);
			physicalList.add(physicalIfs);
		}

		ret.setLagIfsList(lagList);
		ret.setPhysicalIfsList(physicalList);

		return ret;
	}

}
