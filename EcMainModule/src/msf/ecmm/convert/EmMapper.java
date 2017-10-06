package msf.ecmm.convert;

import static msf.ecmm.convert.LogicalPhysicalConverter.*;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.pojo.EquipmentIfs;
import msf.ecmm.db.pojo.IfNameRules;
import msf.ecmm.db.pojo.L2Cps;
import msf.ecmm.db.pojo.L3Cps;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.emctrl.pojo.CeLagAddDelete;
import msf.ecmm.emctrl.pojo.InternalLinkLagAddDelete;
import msf.ecmm.emctrl.pojo.L2SliceAddDelete;
import msf.ecmm.emctrl.pojo.L3SliceAddDelete;
import msf.ecmm.emctrl.pojo.LeafAddDelete;
import msf.ecmm.emctrl.pojo.SpineAddDelete;
import msf.ecmm.emctrl.pojo.parts.CeInterface;
import msf.ecmm.emctrl.pojo.parts.CeLagInterface;
import msf.ecmm.emctrl.pojo.parts.Cp;
import msf.ecmm.emctrl.pojo.parts.Device;
import msf.ecmm.emctrl.pojo.parts.DeviceLeaf;
import msf.ecmm.emctrl.pojo.parts.Equipment;
import msf.ecmm.emctrl.pojo.parts.InternalInterface;
import msf.ecmm.emctrl.pojo.parts.InternalLag;
import msf.ecmm.emctrl.pojo.parts.L2Vpn;
import msf.ecmm.emctrl.pojo.parts.L2VpnPim;
import msf.ecmm.emctrl.pojo.parts.L3SliceBgp;
import msf.ecmm.emctrl.pojo.parts.L3SliceOspf;
import msf.ecmm.emctrl.pojo.parts.L3SliceStatic;
import msf.ecmm.emctrl.pojo.parts.L3Vpn;
import msf.ecmm.emctrl.pojo.parts.L3VpnAs;
import msf.ecmm.emctrl.pojo.parts.L3VpnBgp;
import msf.ecmm.emctrl.pojo.parts.L3VpnNeighbor;
import msf.ecmm.emctrl.pojo.parts.LeafInterface;
import msf.ecmm.emctrl.pojo.parts.LoopbackInterface;
import msf.ecmm.emctrl.pojo.parts.ManagementInterface;
import msf.ecmm.emctrl.pojo.parts.Msdp;
import msf.ecmm.emctrl.pojo.parts.MsdpPeer;
import msf.ecmm.emctrl.pojo.parts.Ntp;
import msf.ecmm.emctrl.pojo.parts.Route;
import msf.ecmm.emctrl.pojo.parts.Snmp;
import msf.ecmm.emctrl.pojo.parts.Track;
import msf.ecmm.emctrl.pojo.parts.TrackInterface;
import msf.ecmm.emctrl.pojo.parts.Vrf;
import msf.ecmm.emctrl.pojo.parts.Vrrp;
import msf.ecmm.ope.receiver.pojo.AddLeaf;
import msf.ecmm.ope.receiver.pojo.AddSpine;
import msf.ecmm.ope.receiver.pojo.BulkCreateL2cp;
import msf.ecmm.ope.receiver.pojo.BulkCreateL3cp;
import msf.ecmm.ope.receiver.pojo.BulkDeleteL2cp;
import msf.ecmm.ope.receiver.pojo.BulkDeleteL3cp;
import msf.ecmm.ope.receiver.pojo.CreateLagInterface;
import msf.ecmm.ope.receiver.pojo.DeleteLeaf;
import msf.ecmm.ope.receiver.pojo.DeleteSpine;
import msf.ecmm.ope.receiver.pojo.UpdateL3cp;
import msf.ecmm.ope.receiver.pojo.parts.CpsCreateL2cp;
import msf.ecmm.ope.receiver.pojo.parts.CpsCreateL3cp;
import msf.ecmm.ope.receiver.pojo.parts.CpsDeleteCps;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentAddNode;
import msf.ecmm.ope.receiver.pojo.parts.IfAddNode;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkIfAddNode;
import msf.ecmm.ope.receiver.pojo.parts.NodeAddNode;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodeAddLeaf;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodeAddSpine;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodeDeleteNode;
import msf.ecmm.ope.receiver.pojo.parts.PhysicalIfNode;
import msf.ecmm.ope.receiver.pojo.parts.StaticRoute;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EmMapper {

	public static CeLagAddDelete toLagIfCreate(Nodes nodes, CreateLagInterface lag_if_info)
			throws IllegalArgumentException {

		logger.trace(CommonDefinitions.START);
		logger.debug(nodes + ", " + lag_if_info);

		CeLagAddDelete ret = new CeLagAddDelete();

		ret.setName("ce-lag");
		ret.setDevice(new ArrayList<Device>());

		Device device = new Device();
		device.setName(nodes.getNode_name());
		device.setCeLagInterfaceList(new ArrayList<CeLagInterface>());

		String ifName = toLagIfName(nodes.getEquipments().getLag_prefix(), lag_if_info.getLagIf().getLagIfId());

		device.getCeLagInterfaceList().add(new CeLagInterface());
		device.getCeLagInterfaceList().get(0).setName(ifName);
		device.getCeLagInterfaceList().get(0).setLinkSpeed(lag_if_info.getLagIf().getLinkSpeed());
		device.getCeLagInterfaceList().get(0).setMinimumLinks((long) lag_if_info.getLagIf().getMinimumLinks());
		device.getCeLagInterfaceList().get(0).setLeafInterfaceList(new ArrayList<LeafInterface>());

		for (String phys : lag_if_info.getLagIf().getPhysicalIfIds()) {
			LeafInterface tmp = new LeafInterface();
			String physIfName = null;

			for (PhysicalIfs physIf : nodes.getPhysicalIfsList()) {
				if (phys.equals(physIf.getPhysical_if_id())) {
					physIfName = physIf.getIf_name();
					break;
				} else {
				}
			}

			if (physIfName == null) {
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

	public static CeLagAddDelete toLagIfDelete(LagIfs lagIfs) {

		logger.trace(CommonDefinitions.START);
		logger.debug(lagIfs);

		CeLagAddDelete ret = new CeLagAddDelete();

		ret.setName("ce-lag");
		ret.setDevice(new ArrayList<Device>());

		Device device = new Device();
		device.setName(lagIfs.getNodes().getNode_name());
		device.setCeLagInterfaceList(new ArrayList<CeLagInterface>());

		device.getCeLagInterfaceList().add(new CeLagInterface());
		device.getCeLagInterfaceList().get(0).setName(lagIfs.getIf_name());
		device.getCeLagInterfaceList().get(0).setMinimumLinks(0L);
		device.getCeLagInterfaceList().get(0).setLeafInterfaceList(new ArrayList<LeafInterface>());
		device.getCeLagInterfaceList().get(0).getLeafInterfaceList().add(new LeafInterface());

		device.getCeLagInterfaceList().get(0).getLeafInterfaceList().get(0).setOperation("delete");

		ret.getDevice().add(device);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static L2SliceAddDelete toL2CPCreate(BulkCreateL2cp bulkCreateL2cp, List<Nodes> nodesList)
			throws IllegalArgumentException {

		logger.trace(CommonDefinitions.START);
		logger.debug(bulkCreateL2cp + ", " + nodesList);

		L2SliceAddDelete ret = new L2SliceAddDelete();
		ret.setName(toSliceName(bulkCreateL2cp.getSliceId()));

		List<DeviceLeaf> deviceLeafList = new ArrayList<DeviceLeaf>();

		for (CpsCreateL2cp cpsCreateL2cp : bulkCreateL2cp.getCps()) {

			DeviceLeaf deviceLeaf = null;
			for (Nodes nodes : nodesList) {
				if (cpsCreateL2cp.getBaseIf().getNodeId().equals(nodes.getNode_id())) {
					deviceLeaf = new DeviceLeaf();
					deviceLeaf.setName(nodes.getNode_name());

					List<Cp> cpList = new ArrayList<Cp>();
					if (cpsCreateL2cp.getBaseIf().getType().equals("lag")) {
						for (LagIfs lagIfs : nodes.getLagIfsList()) {
							if (cpsCreateL2cp.getBaseIf().getIfId().equals(lagIfs.getLag_if_id())) {
								Cp cp = new Cp();
								cp.setName(lagIfs.getIf_name());
								cp.setVlanId(cpsCreateL2cp.getVlanId().longValue());
								cp.setPortMode(cpsCreateL2cp.getPortMode());
								cp.setVni(bulkCreateL2cp.getVrfId().longValue());
								cp.setMulticastGroup(bulkCreateL2cp.getIpv4MulticastAddress());
								cpList.add(cp);
							}
						}
					}
					else {
						for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
							if (cpsCreateL2cp.getBaseIf().getIfId().equals(physicalIfs.getPhysical_if_id())) {
								Cp cp = new Cp();
								cp.setName(physicalIfs.getIf_name());
								cp.setVlanId(cpsCreateL2cp.getVlanId().longValue());
								cp.setPortMode(cpsCreateL2cp.getPortMode());
								cp.setVni(bulkCreateL2cp.getVrfId().longValue());
								cp.setMulticastGroup(bulkCreateL2cp.getIpv4MulticastAddress());
								cpList.add(cp);
							}
						}
					}
					deviceLeaf.setCpList(cpList);
				}
			}

			if (deviceLeaf == null) {
				throw new IllegalArgumentException();
			}

			boolean alreadyRegister = false;
			int index = 0;
			for (DeviceLeaf dl : deviceLeafList) {
				if (dl.getName().equals(deviceLeaf.getName())) {
					alreadyRegister = true;
					index = deviceLeafList.indexOf(dl);
					break;
				}
			}

			if (!alreadyRegister) {
				deviceLeafList.add(deviceLeaf);
			}
			else {
				deviceLeafList.get(index).getCpList().addAll(deviceLeaf.getCpList());
			}
		}
		ret.setDeviceLeafList(deviceLeafList);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static L3SliceAddDelete toL3CPCreate(BulkCreateL3cp bulkCreateL3cp, List<Nodes> nodesList, Integer clusterId)
			throws IllegalArgumentException {

		logger.trace(CommonDefinitions.START);
		logger.debug(bulkCreateL3cp + ", " + nodesList + ", clusterId=" + clusterId);

		L3SliceAddDelete ret = new L3SliceAddDelete();
		ret.setName(toSliceName(bulkCreateL3cp.getSliceId()));

		List<DeviceLeaf> deviceLeafList = new ArrayList<DeviceLeaf>();

		for (CpsCreateL3cp cpsCreateL3cp : bulkCreateL3cp.getCps()) {

			DeviceLeaf deviceLeaf = null;
			for (Nodes nodes : nodesList) {

				if (cpsCreateL3cp.getBaseIf().getNodeId().equals(nodes.getNode_id()) &&
						toIntegerNodeType(cpsCreateL3cp.getBaseIf().getNodeType()) == nodes.getNode_type()) {

					deviceLeaf = new DeviceLeaf();
					deviceLeaf.setName(nodes.getNode_name());

					List<Cp> cpList = new ArrayList<Cp>();
					Vrf vrf = new Vrf();
					vrf.setVrfName(toVRF(bulkCreateL3cp.getVrfId()));
					vrf.setRt(toRouteTarget(bulkCreateL3cp.getVrfId(), bulkCreateL3cp.getPlane()));
					vrf.setRd(toRouteDistinguisher(bulkCreateL3cp.getVrfId(),
							clusterId,
							cpsCreateL3cp.getBaseIf().getNodeId()));
					vrf.setRouterId(cpsCreateL3cp.getBaseIf().getRouterId());
					deviceLeaf.setVrf(vrf);

					List<TrackInterface> trackInterfaceList = new ArrayList<TrackInterface>();
					if (cpsCreateL3cp.getVrrp() != null && cpsCreateL3cp.getVrrp().getRole().equals("master")) {
						if (nodes.getLagIfsList() != null) {
							for (LagIfs lagIfs : nodes.getLagIfsList()) {
								TrackInterface trackInterface = null;
								if (lagIfs.getInternalLinkIfsList() != null
										&& lagIfs.getInternalLinkIfsList().size() > 0) {
									trackInterface = new TrackInterface();
									trackInterface.setName(lagIfs.getIf_name());
									trackInterfaceList.add(trackInterface);
								}
							}
						}
					}

					Cp cp = null;
					if (cpsCreateL3cp.getBaseIf().getType().equals("lag")) {
						for (LagIfs lagIfs : nodes.getLagIfsList()) {
							if (cpsCreateL3cp.getBaseIf().getIfId().equals(lagIfs.getLag_if_id())) {
								cp = new Cp();
								cp.setName(lagIfs.getIf_name());

								setl3SliceCpInfo(cp, cpsCreateL3cp);
							}
						}
					}
					else {
						for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
							if (cpsCreateL3cp.getBaseIf().getIfId().equals(physicalIfs.getPhysical_if_id())) {
								cp = new Cp();
								cp.setName(physicalIfs.getIf_name());

								setl3SliceCpInfo(cp, cpsCreateL3cp);
								break;
							}
						}
					}
					if (cp == null) {
						throw new IllegalArgumentException();
					}
					Track track = null;
					if (trackInterfaceList.size() > 0) {
						track = new Track();
						track.setTrackInterfaceList(trackInterfaceList);
						cp.getVrrp().setTrack(track);
					}
					cpList.add(cp);

					deviceLeaf.setCpList(cpList);
				}
			}

			if (deviceLeaf == null) {
				throw new IllegalArgumentException();
			}

			boolean alreadyRegister = false;
			int index = 0;
			for (DeviceLeaf dl : deviceLeafList) {
				if (dl.getName().equals(deviceLeaf.getName())) {
					alreadyRegister = true;
					index = deviceLeafList.indexOf(dl);
					break;
				}
			}

			if (!alreadyRegister) {
				deviceLeafList.add(deviceLeaf);
			}
			else {
				deviceLeafList.get(index).getCpList().addAll(deviceLeaf.getCpList());
			}
		}
		ret.setDeviceLeafList(deviceLeafList);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	private static void setl3SliceCpInfo(Cp cp, CpsCreateL3cp cpsCreateL3cp) {

		logger.trace(CommonDefinitions.START);

		cp.setVlanId(cpsCreateL3cp.getVlanId().longValue());

		CeInterface ceInterface = new CeInterface();
		ceInterface.setAddress(cpsCreateL3cp.getIpv4Address());
		ceInterface.setPrefix(cpsCreateL3cp.getIpv4Prefix());
		ceInterface.setAddress6(cpsCreateL3cp.getIpv6Address());
		ceInterface.setPrefix6(cpsCreateL3cp.getIpv6Prefix());
		ceInterface.setMtu(cpsCreateL3cp.getMtu().longValue());
		cp.setCeInterface(ceInterface);

		if (cpsCreateL3cp.getVrrp() != null) {
			Vrrp vrrp = new Vrrp();
			vrrp.setGroupId(cpsCreateL3cp.getVrrp().getGroupId().longValue());
			vrrp.setVirtualAddress(cpsCreateL3cp.getVrrp().getVirtualIpv4Address());
			vrrp.setVirtualAddress6(cpsCreateL3cp.getVrrp().getVirtualIpv6Address());
			if (cpsCreateL3cp.getVrrp().getRole().equals("slave")) {
				vrrp.setPriority(95);
			} else {
				vrrp.setPriority(100);
			}
			cp.setVrrp(vrrp);
		}

		if (cpsCreateL3cp.getBgp() != null) {
			L3SliceBgp l3SliceBgp = new L3SliceBgp();
			if (cpsCreateL3cp.getBgp().getRole().equals("master")) {
				l3SliceBgp.addMaster();
			}
			l3SliceBgp.setRemoteAsNumber(cpsCreateL3cp.getBgp().getNeighborAs().longValue());
			l3SliceBgp.setLocalAddress(cpsCreateL3cp.getIpv4Address());
			l3SliceBgp.setRemoteAddress(cpsCreateL3cp.getBgp().getNeighborIpv4Address());
			l3SliceBgp.setLocalAddress6(cpsCreateL3cp.getIpv6Address());
			l3SliceBgp.setRemoteAddress6(cpsCreateL3cp.getBgp().getNeighborIpv6Address());
			cp.setL3SliceBgp(l3SliceBgp);
		}

		if (cpsCreateL3cp.getStaticRoutes().size() != 0) {
			L3SliceStatic l3SliceStatic = new L3SliceStatic();
			List<Route> routeList = new ArrayList<Route>();
			List<Route> routeList6 = new ArrayList<Route>();
			for (StaticRoute staticRoute : cpsCreateL3cp.getStaticRoutes()) {
				Route route = new Route();
				route.setAddress(staticRoute.getAddress());
				route.setPrefix(staticRoute.getPrefix());
				route.setNextHop(staticRoute.getNextHop());
				if (staticRoute.getAddressType().equals("ipv4")) {
					routeList.add(route);
				}
				else {
					routeList6.add(route);
				}
			}
			if (routeList.size() > 0) {
				l3SliceStatic.setRouteList(routeList);
			}
			if (routeList6.size() > 0) {
				l3SliceStatic.setRouteList6(routeList6);
			}
			cp.setL3SliceStatic(l3SliceStatic);
		}

		if (cpsCreateL3cp.getOspf() != null) {
			L3SliceOspf l3SliceOspf = new L3SliceOspf();
			l3SliceOspf.setMetric(cpsCreateL3cp.getOspf().getMetric().longValue());
			cp.setL3SliceOspf(l3SliceOspf);
		}
		logger.trace(CommonDefinitions.END);
	}

	public static L2SliceAddDelete toL2CPDelete(BulkDeleteL2cp bulkDeleteL2cp, List<L2Cps> l2CpsList) {

		logger.trace(CommonDefinitions.START);
		logger.debug(bulkDeleteL2cp + ", " + l2CpsList);

		L2SliceAddDelete ret = new L2SliceAddDelete();
		ret.setName(toSliceName(bulkDeleteL2cp.getSliceId()));

		List<DeviceLeaf> deviceLeafList = new ArrayList<DeviceLeaf>();

		for (CpsDeleteCps cpsDeleteCps : bulkDeleteL2cp.getCps()) {
			for (L2Cps l2Cps : l2CpsList) {
				if (cpsDeleteCps.getCpId().equals(l2Cps.getCp_id())) {
					Cp cp = new Cp();
					cp.setOperation("delete");
					cp.setName(l2Cps.getIf_name());
					cp.setVlanId(new Integer(l2Cps.getVlan_id()).longValue());

					if (0 == deviceLeafList.size()) {
						DeviceLeaf deviceLeaf = new DeviceLeaf();
						deviceLeaf.setName(l2Cps.getNodes().getNode_name());

						List<Cp> cpList = new ArrayList<Cp>();
						cpList.add(cp);
						deviceLeaf.setCpList(cpList);
						deviceLeafList.add(deviceLeaf);
					}
					else {
						boolean addFlg = false;
						for (DeviceLeaf regDl : deviceLeafList) {
							if (regDl.getName().equals(l2Cps.getNodes().getNode_name())) {
								regDl.getCpList().add(cp);
								addFlg = true;
							}
						}
						if (addFlg != true) {
							DeviceLeaf deviceLeaf = new DeviceLeaf();
							deviceLeaf.setName(l2Cps.getNodes().getNode_name());

							List<Cp> cpList = new ArrayList<Cp>();
							cpList.add(cp);
							deviceLeaf.setCpList(cpList);
							deviceLeafList.add(deviceLeaf);
						}
					}
					break;
				}
			}
		}
		ret.setDeviceLeafList(deviceLeafList);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static L3SliceAddDelete toL3CPDelete(BulkDeleteL3cp bulkDeleteL3cp, List<L3Cps> l3CpsList) {

		logger.trace(CommonDefinitions.START);
		logger.debug(bulkDeleteL3cp + ", " + l3CpsList);

		L3SliceAddDelete ret = new L3SliceAddDelete();
		ret.setName(toSliceName(bulkDeleteL3cp.getSliceId()));

		List<DeviceLeaf> deviceLeafList = new ArrayList<DeviceLeaf>();

		for (CpsDeleteCps cpsDeleteCps : bulkDeleteL3cp.getCps()) {
			for (L3Cps l3Cps : l3CpsList) {
				if (cpsDeleteCps.getCpId().equals(l3Cps.getCp_id())) {
					Cp cp = new Cp();
					cp.setOperation("delete");
					cp.setName(l3Cps.getIf_name());
					cp.setVlanId(new Integer(l3Cps.getVlan_id()).longValue());

					if (0 == deviceLeafList.size()) {
						DeviceLeaf deviceLeaf = new DeviceLeaf();
						deviceLeaf.setName(l3Cps.getNodes().getNode_name());

						List<Cp> cpList = new ArrayList<Cp>();
						cpList.add(cp);
						deviceLeaf.setCpList(cpList);
						deviceLeafList.add(deviceLeaf);
					}
					else {
						boolean addFlg = false;
						for (DeviceLeaf regDl : deviceLeafList) {
							if (regDl.getName().equals(l3Cps.getNodes().getNode_name())) {
								regDl.getCpList().add(cp);
								addFlg = true;
							}
						}
						if (addFlg != true) {
							DeviceLeaf deviceLeaf = new DeviceLeaf();
							deviceLeaf.setName(l3Cps.getNodes().getNode_name());

							List<Cp> cpList = new ArrayList<Cp>();
							cpList.add(cp);
							deviceLeaf.setCpList(cpList);
							deviceLeafList.add(deviceLeaf);
						}
					}
					break;
				}
			}
		}
		ret.setDeviceLeafList(deviceLeafList);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static L3SliceAddDelete toL3CPChange(UpdateL3cp updateL3cp, L3Cps l3Cps, Integer clusterId) {

		logger.trace(CommonDefinitions.START);
		logger.debug(updateL3cp + ", " + l3Cps + ", clusterId=" + clusterId);

		L3SliceAddDelete ret = new L3SliceAddDelete();
		ret.setName(toSliceName(l3Cps.getSlice_id()));

		List<DeviceLeaf> deviceLeafList = new ArrayList<DeviceLeaf>();
		DeviceLeaf deviceLeaf = new DeviceLeaf();
		deviceLeaf.setName(l3Cps.getNodes().getNode_name());

		Vrf vrf = new Vrf();
		vrf.setVrfName(toVRF(updateL3cp.getUpdateOption().getVrfId()));
		vrf.setRt(toRouteTarget(updateL3cp.getUpdateOption().getVrfId(), 1));
		vrf.setRd(toRouteDistinguisher(updateL3cp.getUpdateOption().getVrfId(), clusterId, l3Cps.getNodes()
				.getNode_id()));
		vrf.setRouterId(updateL3cp.getUpdateOption().getRouterId());
		deviceLeaf.setVrf(vrf);

		List<Cp> cpList = new ArrayList<Cp>();
		Cp cp = new Cp();
		cp.setName(l3Cps.getIf_name());
		cp.setVlanId((long) l3Cps.getVlan_id());

		CeInterface ceInterface = new CeInterface();
		ceInterface.setAddress(updateL3cp.getUpdateOption().getIpv4Address());
		ceInterface.setPrefix(updateL3cp.getUpdateOption().getIpv4Prefix());
		ceInterface.setAddress6(updateL3cp.getUpdateOption().getIpv6Address());
		ceInterface.setPrefix6(updateL3cp.getUpdateOption().getIpv6Prefix());
		cp.setCeInterface(ceInterface);

		L3SliceStatic l3SliceStatic = new L3SliceStatic();
		List<Route> routeList = new ArrayList<Route>();
		List<Route> routeList6 = new ArrayList<Route>();
		for (StaticRoute staticRoute : updateL3cp.getUpdateOption().getStaticRoutes()) {
			Route route = new Route();
			if (updateL3cp.getUpdateOption().getOperationType().equals("delete")) {
				route.setOperation("delete");
			}
			route.setAddress(staticRoute.getAddress());
			route.setPrefix(staticRoute.getPrefix());
			route.setNextHop(staticRoute.getNextHop());
			if (staticRoute.getAddressType().equals("ipv4")) {
				routeList.add(route);
			}
			else {
				routeList6.add(route);
			}
		}
		if (routeList.size() > 0) {
			l3SliceStatic.setRouteList(routeList);
		}
		if (routeList6.size() > 0) {
			l3SliceStatic.setRouteList6(routeList6);
		}
		cp.setL3SliceStatic(l3SliceStatic);

		cpList.add(cp);
		deviceLeaf.setCpList(cpList);
		deviceLeafList.add(deviceLeaf);
		ret.setDeviceLeafList(deviceLeafList);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static LeafAddDelete toLeafInfoNodeCreate(AddLeaf addLeaf, Nodes nodes, String ecmainIpaddr)
			throws IllegalArgumentException {

		logger.trace(CommonDefinitions.START);
		logger.debug(addLeaf + ", " + nodes + ", ecmainIpaddr=" + ecmainIpaddr);

		LeafAddDelete ret = new LeafAddDelete();
		ret.setName("leaf");

		Device device = addNodeInfo(addLeaf.getEquipment(), addLeaf.getNode(), addLeaf.getIfs(), nodes, ecmainIpaddr);

		if (addLeaf.getVpn().getVpnType().equals("l3")) {
			L3Vpn l3Vpn = new L3Vpn();
			L3VpnBgp l3VpnBgp = new L3VpnBgp();
			List<L3VpnNeighbor> l3VpnNeighborList = new ArrayList<L3VpnNeighbor>();
			if (addLeaf.getVpn().getL3vpn() != null) {
				for (String addr : addLeaf.getVpn().getL3vpn().getBgp().getNeighbor().getAddresses()) {
					L3VpnNeighbor l3VpnNeighbor = new L3VpnNeighbor();
					l3VpnNeighbor.setAddress(addr);
					l3VpnNeighborList.add(l3VpnNeighbor);
				}
				l3VpnBgp.setL3VpnNeighbor(l3VpnNeighborList);
				l3VpnBgp.setCommunity(addLeaf.getVpn().getL3vpn().getBgp().getCommunity());
				l3VpnBgp.setCommunityWildcard(addLeaf.getVpn().getL3vpn().getBgp().getCommunityWildcard());
				l3Vpn.setL3VpnBgp(l3VpnBgp);

				L3VpnAs l3VpnAs = new L3VpnAs();
				l3VpnAs.setAsNumber(Long.parseLong(addLeaf.getVpn().getL3vpn().getAs().getAsNumber()));
				l3Vpn.setL3VpnAs(l3VpnAs);
				device.setL3Vpn(l3Vpn);
			}
		}
		else {
			L2Vpn l2Vpn = new L2Vpn();
			L2VpnPim l2VpnPim = new L2VpnPim();
			if (addLeaf.getVpn().getL2vpn() != null) {
				l2VpnPim.setRpAddress(addLeaf.getVpn().getL2vpn().getPim().getRpAddress());
				l2Vpn.setL2VpnPim(l2VpnPim);
				device.setL2Vpn(l2Vpn);
			}
		}
		ret.setDevice(device);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static InternalLinkLagAddDelete toLeafInfoLagCreate(AddLeaf addLeaf, ArrayList<Nodes> nodesList)
			throws IllegalArgumentException {

		logger.trace(CommonDefinitions.START);
		logger.debug(addLeaf + ", " + nodesList);

		InternalLinkLagAddDelete ret = new InternalLinkLagAddDelete();
		ret.setName("internal-lag");

		List<Device> deviceList = new ArrayList<Device>();
		for (OppositeNodeAddLeaf opNode : addLeaf.getOppositeNodes()) {

			Device device = null;
			for (Nodes nodes : nodesList) {

				if (opNode.getNodeId().equals(nodes.getNode_id())) {

					device = new Device();
					device.setName(nodes.getNode_name());

					List<InternalLag> internalLagList = new ArrayList<InternalLag>();
					InternalLag internalLag = new InternalLag();
					internalLag.setName(toLagIfName(nodes.getEquipments().getLag_prefix(),
							opNode.getInternalLinkIf().getLagIf().getLagIfId()));
					internalLag.setMinimumLinks(opNode.getInternalLinkIf().getLagIf().getMinimumLinks().longValue());
					internalLag.setLinkSpeed(opNode.getInternalLinkIf().getLagIf().getLinkSpeed());
					internalLag.setAddress(opNode.getInternalLinkIf().getLagIf().getLinkIpAddress());
					internalLag.setPrefix(opNode.getInternalLinkIf().getLagIf().getPrefix());

					List<InternalInterface> internalInterfaceList = new ArrayList<InternalInterface>();
					for (PhysicalIfNode physicalIfNode : opNode.getInternalLinkIf().getLagIf().getPhysicalIfs()) {
						InternalInterface internalInterface = new InternalInterface();

						String prefix = null;
						String slot = null;
						for (IfNameRules ifNameRules : nodes.getEquipments().getIfNameRulesList()) {
							if (ifNameRules.getSpeed().equals(opNode.getInternalLinkIf().getLagIf().getLinkSpeed())) {
								prefix = ifNameRules.getPort_prefix();
								break;
							}
						}
						for (EquipmentIfs equipmentIfs : nodes.getEquipments().getEquipmentIfsList()) {
							if (equipmentIfs.getPhysical_if_id().equals(physicalIfNode.getPhysicalIfId())) {
								slot = equipmentIfs.getIf_slot();
								break;
							}
						}
						if (prefix != null && slot != null) {
							internalInterface.setName(toPhysicalIfName(prefix, slot));
						}
						else {
							throw new IllegalArgumentException();
						}

						internalInterfaceList.add(internalInterface);
					}
					internalLag.setInternalInterface(internalInterfaceList);

					internalLagList.add(internalLag);
					device.setInternalLagList(internalLagList);
					break;
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

	public static InternalLinkLagAddDelete toLeafInfoLagDelete(DeleteLeaf deleteLeaf, ArrayList<Nodes> nodesList)
			throws IllegalArgumentException {
		logger.trace(CommonDefinitions.START);
		logger.debug(deleteLeaf + ", " + nodesList);

		InternalLinkLagAddDelete ret = new InternalLinkLagAddDelete();
		ret.setName("internal-lag");

		List<Device> deviceList = new ArrayList<Device>();

		for (OppositeNodeDeleteNode opNode : deleteLeaf.getOppositeNodes()) {

			Device device = delInternalLagInfo(opNode, nodesList);
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

	public static SpineAddDelete toSpineInfoNodeCreate(AddSpine addSpine, Nodes nodes, String ecmainIpaddr)
			throws IllegalArgumentException {

		logger.trace(CommonDefinitions.START);
		logger.debug(addSpine + ", " + nodes + ", ecmainIpaddr=" + ecmainIpaddr);

		SpineAddDelete ret = new SpineAddDelete();
		ret.setName("spine");

		Device device = addNodeInfo(addSpine.getEquipment(), addSpine.getNode(), addSpine.getIfs(), nodes, ecmainIpaddr);

		if (addSpine.getMsdp() != null) {
			Msdp msdp = new Msdp();
			MsdpPeer msdpPeer = new MsdpPeer();
			msdpPeer.setAddress(addSpine.getMsdp().getPeer().getAddress());
			msdpPeer.setLocalAddress(addSpine.getMsdp().getPeer().getLocalAddress());
			msdp.setMsdpPeer(msdpPeer);
			device.setMsdp(msdp);
		}

		L2Vpn l2Vpn = new L2Vpn();
		L2VpnPim l2VpnPim = new L2VpnPim();
		l2VpnPim.setOtherRpAddress(addSpine.getVpn().getL2vpn().getPim().getOtherRpAddress());
		l2VpnPim.setSelfRpAddress(addSpine.getVpn().getL2vpn().getPim().getSelfRpAddress());
		l2Vpn.setL2VpnPim(l2VpnPim);
		device.setL2Vpn(l2Vpn);

		ret.setDevice(device);

		logger.debug(ret);
		logger.trace(CommonDefinitions.END);
		return ret;
	}

	public static InternalLinkLagAddDelete toSpineInfoLagCreate(AddSpine addSpine, ArrayList<Nodes> nodesList)
			throws IllegalArgumentException {

		logger.trace(CommonDefinitions.START);
		logger.debug(addSpine + ", " + nodesList);

		InternalLinkLagAddDelete ret = new InternalLinkLagAddDelete();
		ret.setName("internal-lag");

		List<Device> deviceList = new ArrayList<Device>();
		for (OppositeNodeAddSpine opNode : addSpine.getOppositeNodes()) {

			Device device = null;
			for (Nodes nodes : nodesList) {

				if (opNode.getNodeId().equals(nodes.getNode_id())) {

					device = new Device();

					device.setVpnType(opNode.getVpnType());
					device.setName(nodes.getNode_name());

					List<InternalLag> internalLagList = new ArrayList<InternalLag>();
					InternalLag internalLag = new InternalLag();
					internalLag.setName(toLagIfName(nodes.getEquipments().getLag_prefix(),
							opNode.getInternalLinkIfs().getLagIf().getLagIfId()));
					internalLag.setMinimumLinks(opNode.getInternalLinkIfs().getLagIf().getMinimumLinks().longValue());
					internalLag.setLinkSpeed(opNode.getInternalLinkIfs().getLagIf().getLinkSpeed());
					internalLag.setAddress(opNode.getInternalLinkIfs().getLagIf().getLinkIpAddress());
					internalLag.setPrefix(opNode.getInternalLinkIfs().getLagIf().getPrefix());

					List<InternalInterface> internalInterfaceList = new ArrayList<InternalInterface>();
					for (PhysicalIfNode physicalIfNode : opNode.getInternalLinkIfs().getLagIf().getPhysicalIfs()) {

						InternalInterface internalInterface = new InternalInterface();

						String prefix = null;
						String slot = null;
						for (IfNameRules ifNameRules : nodes.getEquipments().getIfNameRulesList()) {
							if (ifNameRules.getSpeed().equals(opNode.getInternalLinkIfs().getLagIf().getLinkSpeed())) {
								prefix = ifNameRules.getPort_prefix();
								break;
							}
						}
						for (EquipmentIfs equipmentIfs : nodes.getEquipments().getEquipmentIfsList()) {
							if (equipmentIfs.getPhysical_if_id().equals(physicalIfNode.getPhysicalIfId())) {
								slot = equipmentIfs.getIf_slot();
								break;
							}
						}
						if (prefix != null && slot != null) {
							internalInterface.setName(toPhysicalIfName(prefix, slot));
						}
						else {
							throw new IllegalArgumentException();
						}

						internalInterfaceList.add(internalInterface);
					}
					internalLag.setInternalInterface(internalInterfaceList);
					internalLagList.add(internalLag);

					device.setInternalLagList(internalLagList);
					break;
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

	public static InternalLinkLagAddDelete toSpineInfoLagDelete(DeleteSpine deleteSpine, ArrayList<Nodes> nodesList)
			throws IllegalArgumentException {
		logger.trace(CommonDefinitions.START);
		logger.debug(deleteSpine + ", " + nodesList);

		InternalLinkLagAddDelete ret = new InternalLinkLagAddDelete();
		ret.setName("internal-lag");

		List<Device> deviceList = new ArrayList<Device>();

		for (OppositeNodeDeleteNode opNode : deleteSpine.getOppositeNodes()) {

			Device device = delInternalLagInfo(opNode, nodesList);
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

	private static Device addNodeInfo(EquipmentAddNode addEquipment, NodeAddNode addNode, IfAddNode addIfs,
			Nodes nodes, String ecmainIpaddr) throws IllegalArgumentException {

		logger.trace(CommonDefinitions.START);
		logger.debug(addEquipment + ", " + addNode + ", " + addIfs + ", " + nodes + ", ipadd=" + ecmainIpaddr);

		Device device = new Device();
		device.setName(nodes.getNode_name());

		Equipment equipment = new Equipment();
		equipment.setPlatform(addEquipment.getPlatform());
		equipment.setOs(addEquipment.getOs());
		equipment.setFirmware(addEquipment.getFirmware());
		equipment.setLoginid(addNode.getUsername());
		equipment.setPassword(addNode.getPassword());
		if (addNode.getProvisioning() == true) {
			equipment.addNewlyEstablish();
		}
		device.setEquipment(equipment);

		if (addIfs.getInternalLinkIfs().size() != 0) {
			List<InternalLag> internalLagList = new ArrayList<>();
			for (InternalLinkIfAddNode internalLinkIfAddNode : addIfs.getInternalLinkIfs()) {
				InternalLag internalLag = new InternalLag();

				for (LagIfs lagDb : nodes.getLagIfsList()) {
					if (lagDb.getLag_if_id().equals(internalLinkIfAddNode.getLagIf().getLagIfId())) {
						internalLag.setName(lagDb.getIf_name());
						internalLag.setMinimumLinks(internalLinkIfAddNode.getLagIf().getMinimumLinks().longValue());
						internalLag.setLinkSpeed(internalLinkIfAddNode.getLagIf().getLinkSpeed());
						internalLag.setAddress(internalLinkIfAddNode.getLagIf().getLinkIpAddress());
						internalLag.setPrefix(internalLinkIfAddNode.getLagIf().getPrefix());
					}
				}

				List<InternalInterface> internalInterfaceList = new ArrayList<InternalInterface>();
				for (PhysicalIfNode physicalIfNode : internalLinkIfAddNode.getLagIf().getPhysicalIfs()) {
					InternalInterface internalInterface = new InternalInterface();

					for (PhysicalIfs physiBd : nodes.getPhysicalIfsList()) {
						if (physiBd.getPhysical_if_id().equals(physicalIfNode.getPhysicalIfId())) {
							internalInterface.setName(physiBd.getIf_name());
							internalInterfaceList.add(internalInterface);
						}
					}
				}
				if (internalInterfaceList.isEmpty() || internalInterfaceList.size() == 0) {
					throw new IllegalArgumentException();
				}
				internalLag.setInternalInterface(internalInterfaceList);
				internalLagList.add(internalLag);
			}
			device.setInternalLagList(internalLagList);
		}

		ManagementInterface managementInterface = new ManagementInterface();
		managementInterface.setAddress(addNode.getManagementInterface().getAddress());
		managementInterface.setPrefix(addNode.getManagementInterface().getPrefix());
		device.setManagementInterface(managementInterface);

		LoopbackInterface loopbackInterface = new LoopbackInterface();
		loopbackInterface.setAddress(addNode.getLoopbackInterface().getAddress());
		loopbackInterface.setPrefix(addNode.getLoopbackInterface().getPrefix());
		device.setLoopbackInterface(loopbackInterface);

		Snmp snmp = new Snmp();
		snmp.setServerAdddress(ecmainIpaddr);
		snmp.setCommunity(addNode.getSnmpCommunity());
		device.setSnmp(snmp);

		Ntp ntp = new Ntp();
		ntp.setServerAdddress(addNode.getNtpServerAddress());
		device.setNtp(ntp);

		logger.debug(device);
		logger.trace(CommonDefinitions.END);
		return device;
	}

	private static Device delInternalLagInfo(OppositeNodeDeleteNode opNode, ArrayList<Nodes> nodesList)
			throws IllegalArgumentException {

		logger.trace(CommonDefinitions.START);
		logger.debug(opNode + ", " + nodesList);

		Device device = null;
		for (Nodes nodes : nodesList) {

			if (opNode.getNodeId().equals(nodes.getNode_id())) {

				device = new Device();
				device.setName(nodes.getNode_name());

				List<InternalLag> internalLagList = new ArrayList<InternalLag>();
				InternalLag internalLag = null;
				for (LagIfs lagIfs : nodes.getLagIfsList()) {

					if (opNode.getInternalLinkIfs().getLagIf().getLagIfId().equals(lagIfs.getLag_if_id())) {

						internalLag = new InternalLag();
						internalLag.setName(lagIfs.getIf_name());
						internalLag.setMinimumLinks(
								opNode.getInternalLinkIfs().getLagIf().getMinimumLinks().longValue());
						break;
					}
				}
				if (internalLag == null) {
					throw new IllegalArgumentException();
				}

				List<InternalInterface> internalInterfaceList = new ArrayList<InternalInterface>();
				for (PhysicalIfNode physicalIfNode : opNode.getInternalLinkIfs().getLagIf().getPhysicalIfs()) {

					InternalInterface internalInterface = null;
					for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {

						if (physicalIfNode.getPhysicalIfId().equals(physicalIfs.getPhysical_if_id())) {

							internalInterface = new InternalInterface();
							internalInterface.setOperation("delete");
							internalInterface.setName(physicalIfs.getIf_name());
							break;
						}
					}
					if (internalInterface == null) {
						throw new IllegalArgumentException();
					}
					internalInterfaceList.add(internalInterface);
				}
				internalLag.setInternalInterface(internalInterfaceList);
				internalLagList.add(internalLag);
				device.setInternalLagList(internalLagList);
				break;
			}
		}
		logger.debug(device);
		logger.trace(CommonDefinitions.END);
		return device;
	}
}
