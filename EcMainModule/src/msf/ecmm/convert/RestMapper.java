package msf.ecmm.convert;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.db.pojo.CpsList;
import msf.ecmm.db.pojo.EquipmentIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.IfNameRules;
import msf.ecmm.db.pojo.InternalLinkIfs;
import msf.ecmm.db.pojo.L2Cps;
import msf.ecmm.db.pojo.L3Cps;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.fcctrl.pojo.Operations;
import msf.ecmm.fcctrl.pojo.UpdateLogicalIfStatus;
import msf.ecmm.fcctrl.pojo.parts.CpLogical;
import msf.ecmm.fcctrl.pojo.parts.InternalLinkIfsLogical;
import msf.ecmm.fcctrl.pojo.parts.NodeLogical;
import msf.ecmm.fcctrl.pojo.parts.Slice;
import msf.ecmm.fcctrl.pojo.parts.VpnLogical;
import msf.ecmm.ope.receiver.pojo.GetEquipmentType;
import msf.ecmm.ope.receiver.pojo.GetEquipmentTypeList;
import msf.ecmm.ope.receiver.pojo.GetInterfaceList;
import msf.ecmm.ope.receiver.pojo.GetLagInterface;
import msf.ecmm.ope.receiver.pojo.GetLagInterfaceList;
import msf.ecmm.ope.receiver.pojo.GetNodeTraffic;
import msf.ecmm.ope.receiver.pojo.GetPhysicalInterface;
import msf.ecmm.ope.receiver.pojo.GetPhysicalInterfaceList;
import msf.ecmm.ope.receiver.pojo.parts.Equipment;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentIf;
import msf.ecmm.ope.receiver.pojo.parts.IfNameRule;
import msf.ecmm.ope.receiver.pojo.parts.IfSearchIf;
import msf.ecmm.ope.receiver.pojo.parts.InternalLink;
import msf.ecmm.ope.receiver.pojo.parts.L2Cp;
import msf.ecmm.ope.receiver.pojo.parts.L3Cp;
import msf.ecmm.ope.receiver.pojo.parts.LagIf;
import msf.ecmm.ope.receiver.pojo.parts.PhysicalIf;
import msf.ecmm.ope.receiver.pojo.parts.SwitchTraffic;
import msf.ecmm.ope.receiver.pojo.parts.TrafficValue;
import msf.ecmm.traffic.pojo.NodeKeySet;
import msf.ecmm.traffic.pojo.TrafficData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestMapper {

	public static GetLagInterface toLagIfInfo(LagIfs lagIfs) {

		logger.trace(CommonDefinitions.START);
		logger.debug(lagIfs);

		GetLagInterface ret = new GetLagInterface();
		LagIf lagIf = new LagIf();
		lagIf.setLagIfId(lagIfs.getLag_if_id());
		lagIf.setIfName(lagIfs.getIf_name());
		ret.setLagIfs(lagIf);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static GetEquipmentTypeList toEquipmentList(List<Equipments> equipmentsList) {

		logger.trace(CommonDefinitions.START);
		logger.debug(equipmentsList);

		GetEquipmentTypeList ret = new GetEquipmentTypeList();
		ArrayList<Equipment> equipmentList = new ArrayList<Equipment>();

		for (Equipments equipments : equipmentsList) {
			Equipment equipment = new Equipment();
			equipment.setEquipmentTypeId(equipments.getEquipment_type_id());
			equipment.setLagPrefix(equipments.getLag_prefix());
			equipment.setUnitConnector(equipments.getUnit_connector());
			equipment.setIfNameOid(equipments.getIf_name_oid());
			equipment.setSnmptrapIfNameOid(equipments.getSnmptrap_if_name_oid());
			equipment.setMaxRepetitions(equipments.getMax_repetitions());

			ArrayList<EquipmentIf> equipmentIfList = new ArrayList<EquipmentIf>();
			for (EquipmentIfs eifs : equipments.getEquipmentIfsList()) {
				EquipmentIf equipmentIf = new EquipmentIf();
				equipmentIf.setPhysicalIfId(eifs.getPhysical_if_id());
				equipmentIf.setIfSlot(eifs.getIf_slot());
				equipmentIfList.add(equipmentIf);
			}
			equipment.setEquipmentIfs(equipmentIfList);

			ArrayList<IfNameRule> ifNameRuleList = new ArrayList<IfNameRule>();
			for (IfNameRules inrs : equipments.getIfNameRulesList()) {
				IfNameRule ifNameRule = new IfNameRule();
				ifNameRule.setSpeed(inrs.getSpeed());
				ifNameRule.setPortPrefix(inrs.getPort_prefix());
				ifNameRuleList.add(ifNameRule);
			}
			equipment.setIfNameRules(ifNameRuleList);

			equipmentList.add(equipment);
		}
		ret.setEquipment(equipmentList);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);

		return ret;
	}

	public static GetLagInterfaceList toLagIfInfoList(List<LagIfs> lagIfs) {

		logger.trace(CommonDefinitions.START);
		logger.debug(lagIfs);

		GetLagInterfaceList ret = new GetLagInterfaceList();
		ArrayList<LagIf> lagIfList = new ArrayList<LagIf>();
		for (LagIfs lag : lagIfs) {
			LagIf lagIf = new LagIf();
			lagIf.setLagIfId(lag.getLag_if_id());
			lagIf.setIfName(lag.getIf_name());
			lagIfList.add(lagIf);
		}
		ret.setLagIfs(lagIfList);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static GetPhysicalInterfaceList toPhyInInfoList(List<PhysicalIfs> physicalIfs) {

		logger.trace(CommonDefinitions.START);
		logger.debug(physicalIfs);

		GetPhysicalInterfaceList ret = new GetPhysicalInterfaceList();
		ArrayList<PhysicalIf> physicalIfList = new ArrayList<PhysicalIf>();
		for (PhysicalIfs dbPhysicalIfs : physicalIfs) {
			PhysicalIf physicalIf = new PhysicalIf();
			physicalIf.setPhysicalIfId(dbPhysicalIfs.getPhysical_if_id());
			physicalIf.setIfName(dbPhysicalIfs.getIf_name());
			physicalIfList.add(physicalIf);
		}
		ret.setPhysicalIfs(physicalIfList);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static GetInterfaceList toIFList(Nodes nodes) {

		logger.trace(CommonDefinitions.START);
		logger.debug(nodes);

		GetInterfaceList ret = new GetInterfaceList();
		IfSearchIf ifSearchIf = new IfSearchIf();

		ArrayList<PhysicalIf> physicalIfList = new ArrayList<PhysicalIf>();
		if (nodes != null && nodes.getPhysicalIfsList() != null) {
			for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
				PhysicalIf physicalIf = new PhysicalIf();
				physicalIf.setPhysicalIfId(physicalIfs.getPhysical_if_id());
				physicalIf.setIfName(physicalIfs.getIf_name());
				physicalIfList.add(physicalIf);
			}
		}
		ifSearchIf.setPhysicalIfs(physicalIfList);

		ArrayList<LagIf> lagIfList = new ArrayList<LagIf>();
		if (nodes != null && nodes.getLagIfsList() != null) {
			for (LagIfs lagIfs : nodes.getLagIfsList()) {
				LagIf lagIf = new LagIf();
				lagIf.setLagIfId(lagIfs.getLag_if_id());
				lagIf.setIfName(lagIfs.getIf_name());
				lagIfList.add(lagIf);
			}
		}
		ifSearchIf.setLagIfs(lagIfList);
		ret.setIfs(ifSearchIf);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static GetEquipmentType toEqInfo(Equipments equipmentsDb) {

		logger.trace(CommonDefinitions.START);
		logger.debug(equipmentsDb);

		GetEquipmentType ret = new GetEquipmentType();
		Equipment equipment = new Equipment();

		equipment.setEquipmentTypeId(equipmentsDb.getEquipment_type_id());
		equipment.setLagPrefix(equipmentsDb.getLag_prefix());
		equipment.setUnitConnector(equipmentsDb.getUnit_connector());
		equipment.setIfNameOid(equipmentsDb.getIf_name_oid());
		equipment.setSnmptrapIfNameOid(equipmentsDb.getSnmptrap_if_name_oid());
		equipment.setMaxRepetitions(equipmentsDb.getMax_repetitions());

		ArrayList<EquipmentIf> equipmentIfList = new ArrayList<EquipmentIf>();
		for (EquipmentIfs eifs : equipmentsDb.getEquipmentIfsList()) {
			EquipmentIf equipmentIf = new EquipmentIf();
			equipmentIf.setPhysicalIfId(eifs.getPhysical_if_id());
			equipmentIf.setIfSlot(eifs.getIf_slot());
			equipmentIfList.add(equipmentIf);
		}
		equipment.setEquipmentIfs(equipmentIfList);

		ArrayList<IfNameRule> ifNameRuleList = new ArrayList<IfNameRule>();
		for (IfNameRules inrs : equipmentsDb.getIfNameRulesList()) {
			IfNameRule ifNameRule = new IfNameRule();
			ifNameRule.setSpeed(inrs.getSpeed());
			ifNameRule.setPortPrefix(inrs.getPort_prefix());
			ifNameRuleList.add(ifNameRule);
		}
		equipment.setIfNameRules(ifNameRuleList);
		ret.setEquipment(equipment);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static GetPhysicalInterface toPhyInInfo(PhysicalIfs physicalIfs) {

		logger.trace(CommonDefinitions.START);
		logger.debug(physicalIfs);

		GetPhysicalInterface ret = new GetPhysicalInterface();
		PhysicalIf physicalIf = new PhysicalIf();
		physicalIf.setPhysicalIfId(physicalIfs.getPhysical_if_id());
		physicalIf.setIfName(physicalIfs.getIf_name());
		ret.setPhysicalIf(physicalIf);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;

	}

	public static Operations toSnmpTrapNotificationInfo(Nodes nodesDb, CpsList cpsList, LagIfs internalIF,
			String uristate) {

		logger.trace(CommonDefinitions.START);
		logger.debug("nodesDb=" + nodesDb + ", cpsList=" + cpsList + ", internalIF=" + internalIF + ", uristate="
				+ uristate);

		boolean trueFlug = false;

		String state;
		if (uristate.equals("linkup")) {
			state = "up";
		} else {
			state = "down";
		}

		Operations ret = new Operations();
		UpdateLogicalIfStatus ulis = new UpdateLogicalIfStatus();
		ret.setAction("update_logical_if_status");

		ulis.setClusterId(EcConfiguration.getInstance().get(String.class, EcConfiguration.CLUSTER_ID));
		ulis.setNodes(new ArrayList<NodeLogical>());
		ulis.getNodes().add(new NodeLogical());
		ulis.getNodes().get(0).setNodeId(nodesDb.getNode_id());
		ulis.getNodes().get(0).setNodeType(LogicalPhysicalConverter.toStringNodeType(nodesDb.getNode_type()));

		if ((internalIF != null) && (!internalIF.getInternalLinkIfsList().isEmpty())) {
			logger.debug("Internal IF exists in this notification.");
			trueFlug = true;
			ulis.getNodes().get(0).setInternalLinkIfs(new ArrayList<InternalLinkIfsLogical>());
			for (InternalLinkIfs inIF : internalIF.getInternalLinkIfsList()) {
				InternalLinkIfsLogical tmpInIF = new InternalLinkIfsLogical();
				tmpInIF.setInternalLinkIfId(inIF.getInternal_link_if_id());
				tmpInIF.setStatus(state);

				ulis.getNodes().get(0).getInternalLinkIfs().add(tmpInIF);
			}
		} else {
			logger.debug("Internal IF does not exist in this notification.");
		}

		if ((cpsList != null) &&
				((!cpsList.getL2CpsList().isEmpty()) || (!cpsList.getL3CpsList().isEmpty()))) {
			trueFlug = true;
			ulis.setSlices(new Slice());

			if (!cpsList.getL2CpsList().isEmpty()) {
				logger.debug("L2CP exists in this notification.");
				Map<String, ArrayList<L2Cps>> l2cpMap = new HashMap<String, ArrayList<L2Cps>>();
				for (L2Cps cps : cpsList.getL2CpsList()) {
					if (l2cpMap.get(cps.getSlice_id()) == null) {
						l2cpMap.put(cps.getSlice_id(), new ArrayList<L2Cps>());
					} else {
					}

					l2cpMap.get(cps.getSlice_id()).add(cps);
				}

				ulis.getSlices().setL2vpn(new ArrayList<VpnLogical>());
				for (String sliceId : l2cpMap.keySet()) {
					VpnLogical tmpVpn = new VpnLogical();
					tmpVpn.setSliceId(sliceId);
					tmpVpn.setCps(new ArrayList<CpLogical>());

					if (l2cpMap.get(sliceId) != null) {
						for (L2Cps cpdata : l2cpMap.get(sliceId)) {
							CpLogical tmpcp = new CpLogical();
							tmpcp.setCpId(cpdata.getCp_id());
							tmpcp.setStatus(state);

							tmpVpn.getCps().add(tmpcp);
						}
					} else {
					}

					ulis.getSlices().getL2vpn().add(tmpVpn);
				}
			} else {
			}

			if (!cpsList.getL3CpsList().isEmpty()) {
				logger.debug("L3CP exists in this notification.");
				Map<String, ArrayList<L3Cps>> l3cpMap = new HashMap<String, ArrayList<L3Cps>>();
				for (L3Cps cps : cpsList.getL3CpsList()) {
					if (l3cpMap.get(cps.getSlice_id()) == null) {
						l3cpMap.put(cps.getSlice_id(), new ArrayList<L3Cps>());
					} else {
					}

					l3cpMap.get(cps.getSlice_id()).add(cps);
				}

				ulis.getSlices().setL3vpn(new ArrayList<VpnLogical>());
				for (String sliceId : l3cpMap.keySet()) {
					VpnLogical tmpVpn = new VpnLogical();
					tmpVpn.setSliceId(sliceId);
					tmpVpn.setCps(new ArrayList<CpLogical>());

					if (l3cpMap.get(sliceId) != null) {
						for (L3Cps cpdata : l3cpMap.get(sliceId)) {
							CpLogical tmpcp = new CpLogical();
							tmpcp.setCpId(cpdata.getCp_id());
							tmpcp.setStatus(state);

							tmpVpn.getCps().add(tmpcp);
						}
					} else {
					}

					ulis.getSlices().getL3vpn().add(tmpVpn);
				}
			} else {
			}
		} else {
			logger.debug("CP does not exist in this notification.");
		}

		if (ulis.getNodes().isEmpty()) {
			ulis.setNodes(null);
		}

		if (ulis.getSlices() != null ){

			if(ulis.getSlices().getL2vpn().isEmpty()) {
				ulis.getSlices().setL2vpn(null);
			}
			if(ulis.getSlices().getL3vpn().isEmpty()) {
				ulis.getSlices().setL3vpn(null);
			}
		}

		ret.setUpdateLogicalIfStatusOption(ulis);

		logger.trace(CommonDefinitions.END);

		if (trueFlug) {
			logger.debug("ret=" + ret);
			return ret;
		} else {
			logger.debug("ret=" + null);
			return null;
		}
	}

	public static GetNodeTraffic toNodeTrafficData(ArrayList<NodeKeySet> nodeList,
			HashMap<NodeKeySet, ArrayList<TrafficData>> trafficData, Timestamp time, int gatheringCycle,
			String clusterId) {

		logger.trace(CommonDefinitions.START);
		logger.debug("nodeList=" + nodeList + ", trafficData=" + trafficData + ", time=" + time + ", gatheringCycle="
				+ gatheringCycle + ", clusterId=" + clusterId);

		GetNodeTraffic ret = new GetNodeTraffic();
		ret.setClusterId(clusterId);
		ret.setInterval(gatheringCycle * 60);
		SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd_HHmmss");
		ret.setTime(date.format(time.getTime()));
		ret.setSwitchTraffic(new ArrayList<SwitchTraffic>());
		ret.setIsSuccess(true);

		for (NodeKeySet key : nodeList) {
			SwitchTraffic tmpNode = new SwitchTraffic();
			tmpNode.setNodeId(key.getEquipmentsData().getNode_id());
			tmpNode.setNodeType(LogicalPhysicalConverter.toStringNodeType(key.getEquipmentsData().getNode_type()));
			tmpNode.setTrafficValue(new TrafficValue());
			tmpNode.getTrafficValue().setInternalLinks(new ArrayList<InternalLink>());
			tmpNode.getTrafficValue().setL2Cps(new ArrayList<L2Cp>());
			tmpNode.getTrafficValue().setL3Cps(new ArrayList<L3Cp>());

			if (trafficData.get(key) != null) {
				for (TrafficData td : trafficData.get(key)) {
					boolean nextCheckFlug = false;

					for (L3Cps l3cp : key.getEquipmentsData().getL3CpsList()) {
						if ((td.getIfname() != null) && (td.getIfname().equals(l3cp.getIf_name() + key.getEquipmentsType().getUnit_connector() + String.valueOf(l3cp.getVlan_id())))) {
							nextCheckFlug = true;
							L3Cp tmpCp = new L3Cp();
							tmpCp.setSliceId(l3cp.getSlice_id());
							tmpCp.setCpId(l3cp.getCp_id());

							tmpCp.setReceiveRatel(td.getIfHclnOctets());
							tmpCp.setSendRate(td.getIfHcOutOctets());

							tmpNode.getTrafficValue().getL3Cps().add(tmpCp);
						} else {
						}
					}

					if (nextCheckFlug) {
						continue;
					} else {
					}

					for (L2Cps l2cp : key.getEquipmentsData().getL2CpsList()) {
						if ((td.getIfname() != null) && (td.getIfname().equals(l2cp.getIf_name()))) {
							nextCheckFlug = true;
							L2Cp tmpCp = new L2Cp();
							tmpCp.setLagIfId(null);
							tmpCp.setPhysicalIfId(null);

							boolean pyhsFlug = false;
							for (PhysicalIfs ifinfo : key.getEquipmentsData().getPhysicalIfsList()) {
								if ((ifinfo.getIf_name() != null) && (ifinfo.getIf_name().equals(td.getIfname()))) {
									pyhsFlug = true;
									tmpCp.setIfType(CommonDefinitions.IF_TYPE_PHYSICAL);
									tmpCp.setPhysicalIfId(ifinfo.getPhysical_if_id());
									break;
								} else {
								}
							}

							if (!pyhsFlug) {
								tmpCp.setIfType(CommonDefinitions.IF_TYPE_LAG);
								for (LagIfs ifinfo : key.getEquipmentsData().getLagIfsList()) {
									if ((ifinfo.getIf_name() != null) && (ifinfo.getIf_name().equals(td.getIfname()))) {
										tmpCp.setLagIfId(ifinfo.getLag_if_id());
										break;
									} else {
									}
								}
							} else {
							}

								logger.debug("This IF did not found.:[" + td.getIfname() + "]");
								break;
							} else {
								tmpCp.setReceiveRate(td.getIfHclnOctets());
								tmpCp.setSendRate(td.getIfHcOutOctets());
							}

							tmpNode.getTrafficValue().getL2Cps().add(tmpCp);
							break;
						} else {
						}
					}

					if (nextCheckFlug) {
						continue;
					} else {
					}

					for (LagIfs lag : key.getEquipmentsData().getLagIfsList()) {
						if ((lag.getIf_name() != null) && (lag.getIf_name().equals(td.getIfname()))) {
							List<InternalLink> inLink = new ArrayList<InternalLink>();

							for (LagIfs ifinfo : key.getEquipmentsData().getLagIfsList()) {
								if ((ifinfo.getIf_name() != null) && (ifinfo.getIf_name().equals(td.getIfname()))) {
									for (InternalLinkIfs il : ifinfo.getInternalLinkIfsList()) {
										InternalLink tmp = new InternalLink();
										tmp.setInternalLinkIfId(il.getInternal_link_if_id());
										tmp.setReceiveRate(td.getIfHclnOctets());
										tmp.setSendRate(td.getIfHcOutOctets());

										inLink.add(tmp);
									}
									break;
								} else {
								}
							}

								logger.debug("This IF did not found.:[" + td.getIfname() + "]");
								break;
							} else {
							}

							tmpNode.getTrafficValue().getInternalLinks().addAll(inLink);
							break;
						} else {
						}
					}
				}
			} else {
			}

			ret.getSwitchTraffic().add(tmpNode);
		}

		for (SwitchTraffic switchTraffic : ret.getSwitchTraffic()) {
			if (switchTraffic.getTrafficValue().getL3Cps().isEmpty()) {
				switchTraffic.getTrafficValue().setL3Cps(null);
			}
			if (switchTraffic.getTrafficValue().getL2Cps().isEmpty()) {
				switchTraffic.getTrafficValue().setL2Cps(null);
			}
			if (switchTraffic.getTrafficValue().getInternalLinks().isEmpty()) {
				switchTraffic.getTrafficValue().setInternalLinks(null);
			}
		}

		logger.trace(CommonDefinitions.END);

		logger.debug("ret=" + ret);

		return ret;
	}
}
