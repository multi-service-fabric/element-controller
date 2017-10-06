package msf.ecmm.traffic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.convert.LogicalPhysicalConverter;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.CpsList;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.InternalLinkIfs;
import msf.ecmm.db.pojo.L2Cps;
import msf.ecmm.db.pojo.L3Cps;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.devctrl.SnmpController;
import msf.ecmm.devctrl.pojo.SnmpIfOperStatus;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.GetLogicalIfStatus;
import msf.ecmm.fcctrl.pojo.GetLogicalIfStatusRequest;
import msf.ecmm.fcctrl.pojo.Operations;
import msf.ecmm.fcctrl.pojo.OperationsResponse;
import msf.ecmm.fcctrl.pojo.UpdateLogicalIfStatus;
import msf.ecmm.fcctrl.pojo.parts.CpLogical;
import msf.ecmm.fcctrl.pojo.parts.InternalLinkIfsLogical;
import msf.ecmm.fcctrl.pojo.parts.NodeLogical;
import msf.ecmm.fcctrl.pojo.parts.Slice;
import msf.ecmm.fcctrl.pojo.parts.VpnLogical;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.traffic.pojo.DeviceInformationSet;
import msf.ecmm.traffic.pojo.NodeKeySet;
import msf.ecmm.traffic.pojo.SliceKeySet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegrityExecutor extends Thread {

	private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	private HashMap<NodeKeySet, ArrayList<SnmpIfOperStatus>> ifStateFromDevice;

	public IntegrityExecutor(){
		ifStateFromDevice = new HashMap<NodeKeySet, ArrayList<SnmpIfOperStatus>>();
		ifStateFromFC = new GetLogicalIfStatus();
		deviceDetail = new HashMap<NodeKeySet, DeviceInformationSet>();

	}

	public void run(){

		logger.trace(CommonDefinitions.START);

		boolean startResult =OperationControlManager.getInstance().recievestartIfIngIntegrity();

		try{
			if(startResult){

				try(DBAccessManager session = new DBAccessManager()){
					List<Equipments> equipmentList = session.getEquipmentsList();
					Map<Equipments,List<Nodes>> nodesMap = new HashMap<>();
					for(Equipments eq:equipmentList){
						nodesMap.put(eq,session.searchNodesByEquipmentId(eq.getEquipment_type_id()));
						for(Nodes nd:nodesMap.get(eq)){
							NodeKeySet nodeKey = new NodeKeySet();
							DeviceInformationSet di = new DeviceInformationSet();

							nodeKey.setEquipmentsData(nd);
							nodeKey.setEquipmentsType(eq);
							di.setEquipmentsData(nd);
							di.setEquipmentsType(eq);
							deviceDetail.put(nodeKey, di);

							CpsList cpList = session.getCpsList(nd.getNode_type(), nd.getNode_id(), null);
							List<InternalLinkIfs> internalIfList = session.getInternalLinkIfsList(nd.getNode_type(), nd.getNode_id());

							deviceDetail.get(nodeKey).setL2SliceData(new HashMap<>());
							for(L2Cps cp2:cpList.getL2CpsList()){
								SliceKeySet tmpKey = new SliceKeySet();
								tmpKey.setCpId(cp2.getCp_id());
								tmpKey.setSliceId(cp2.getSlice_id());

								deviceDetail.get(nodeKey).getL2SliceData().put(tmpKey,cp2);
							}

							deviceDetail.get(nodeKey).setL3SliceData(new HashMap<>());
							for(L3Cps cp3:cpList.getL3CpsList()){
								SliceKeySet tmpKey = new SliceKeySet();
								tmpKey.setCpId(cp3.getCp_id());
								tmpKey.setSliceId(cp3.getSlice_id());

								deviceDetail.get(nodeKey).getL3SliceData().put(tmpKey,cp3);
							}

							deviceDetail.get(nodeKey).setInternalLinkIfData(new HashMap<>());
							for(InternalLinkIfs il:internalIfList){
								deviceDetail.get(nodeKey).getInternalLinkIfData().put(Integer.valueOf(il.getInternal_link_if_id()),il);
							}
						}
					}

					String switchClusterId = EcConfiguration.getInstance().get(String.class, EcConfiguration.CLUSTER_ID);

					Operations requestGetFCData =  new Operations();
					requestGetFCData.setGetLogicalIfStatusOption(new GetLogicalIfStatusRequest());
					requestGetFCData.getGetLogicalIfStatusOption().setClusterId(switchClusterId);
					requestGetFCData.setAction("get_logical_if_status");

					RestClient getFC = new RestClient();
					OperationsResponse response = (OperationsResponse) getFC.request(RestClient.OPERATION, new HashMap<String,String>(), requestGetFCData, OperationsResponse.class);

					SnmpController snmp = new SnmpController();

					for(NodeKeySet node:deviceDetail.keySet()){
						ifStateFromDevice.put(node,snmp.getIfOperStatus(node.getEquipmentsType(), node.getEquipmentsData()));
					}

					Operations integrityResult =  new Operations();
					UpdateLogicalIfStatus check = executeIntegrity();
					integrityResult.setUpdateLogicalIfStatusOption(check);
					integrityResult.setAction("update_logical_if_status");

					if(check != null){
						check.setClusterId(switchClusterId);

						RestClient resultFC = new RestClient();
						resultFC.request(RestClient.OPERATION, new HashMap<String,String>(), integrityResult, CommonResponseFromFc.class);
					}else{
					}
				}
			}else{
			}
		}catch (Exception e) {
			   logger.warn(LogFormatter.out.format(LogFormatter.MSG_407043),e);
		}

		OperationControlManager.getInstance().recieveEndIfIntegrity();

		InterfaceIntegrityValidationManager.getInstance().getExecuteThreadHolder().remove(InterfaceIntegrityValidationManager.getInstance().getExecuteThreadHolder().indexOf(this));

		logger.trace(CommonDefinitions.END);
	}

	private UpdateLogicalIfStatus executeIntegrity() {

		logger.trace(CommonDefinitions.START);

		UpdateLogicalIfStatus ret = new UpdateLogicalIfStatus();

		ret.setClusterId(ifStateFromFC.getClusterId());
		ret.setNodes(new ArrayList<NodeLogical>());
		ret.setSlices(new Slice());
		ret.getSlices().setL2vpn(new ArrayList<VpnLogical>());
		ret.getSlices().setL3vpn(new ArrayList<VpnLogical>());

		ret = executeNodeIntegrity(ret);

		if(ifStateFromFC.getSlices() != null){
			ret = executeL2cpIntegrity(ret);

			ret = executeL3cpIntegrity(ret);
		}else{
		}

		if((!ret.getNodes().isEmpty())||(!ret.getSlices().getL2vpn().isEmpty())||(!ret.getSlices().getL3vpn().isEmpty())){

			if (ret.getNodes().isEmpty()) {
				ret.setNodes(null);
			}

			if(ret.getSlices().getL2vpn().isEmpty() && ret.getSlices().getL3vpn().isEmpty()){
				ret.setSlices(null);
			}else if(!ret.getSlices().getL2vpn().isEmpty() && ret.getSlices().getL3vpn().isEmpty()){
				ret.getSlices().setL3vpn(null);
			}else if(ret.getSlices().getL2vpn().isEmpty() && !ret.getSlices().getL3vpn().isEmpty()){
				ret.getSlices().setL2vpn(null);
			}else{
			}

			logger.trace(CommonDefinitions.END);

			return ret;
		}else{
			logger.trace(CommonDefinitions.END);

			return null;
		}
	}

	private UpdateLogicalIfStatus executeNodeIntegrity(UpdateLogicalIfStatus ret){
		logger.trace(CommonDefinitions.START);
		logger.debug("ifStateFromFC = "+ifStateFromFC);
		for(NodeLogical nl:ifStateFromFC.getNodes()){

			NodeLogical chgnode = new NodeLogical();
			chgnode.setNodeId(nl.getNodeId());
			chgnode.setNodeType(nl.getNodeType());
			chgnode.setInternalLinkIfs(new ArrayList<InternalLinkIfsLogical>());

			int node_type = LogicalPhysicalConverter.toIntegerNodeType(nl.getNodeType());

			NodeKeySet target = null;
			for(NodeKeySet key:ifStateFromDevice.keySet()){
				if((key.getEquipmentsData().getNode_id().equals(nl.getNodeId()))&&(node_type == key.getEquipmentsData().getNode_type())){
					target = key;
					break;
				}else{
				}
			}

			if(target != null){
				ArrayList<SnmpIfOperStatus> innerIfList = ifStateFromDevice.get(target);

				for(InternalLinkIfsLogical ilif:nl.getInternalLinkIfs()){

					String ifname = null;
					String ifid = null;
					if(deviceDetail.get(target) != null){logger.debug("deviceDetail.get(target)= "+deviceDetail.get(target) + "\n ,ilif=" + ilif);
						ifid = deviceDetail.get(target).getInternalLinkIfData().get(Integer.valueOf(ilif.getInternalLinkIfId())).getLag_if_id();
						for(LagIfs lagif:deviceDetail.get(target).getEquipmentsData().getLagIfsList()){
							if(lagif.getLag_if_id().equals(ifid)){
								ifname = lagif.getIf_name();
								break;
							}else{
							}
						}
					}else{
					}

					if(ifname == null){
						   logger.warn(LogFormatter.out.format(LogFormatter.MSG_407059,nl.getNodeId(),nl.getNodeType(),ifid));
						continue;
					}else{
					}

					int ifstate = LogicalPhysicalConverter.toIntegerIFState(ilif.getStatus());

					for(SnmpIfOperStatus ifdev:innerIfList){


								String tmpstate = LogicalPhysicalConverter.toStringIFState(ifdev.getIfOperStatus());

								InternalLinkIfsLogical chgil = new InternalLinkIfsLogical();
								chgil.setInternalLinkIfId(ilif.getInternalLinkIfId());
								chgil.setStatus(tmpstate);
								chgnode.getInternalLinkIfs().add(chgil);
							}
							break;
						}
					}
				}
			}else{
				   logger.warn(LogFormatter.out.format(LogFormatter.MSG_407059,nl.getNodeId(),nl.getNodeType(),"none"));
			}

			if(!chgnode.getInternalLinkIfs().isEmpty()){
				ret.getNodes().add(chgnode);
			}else{
			}
		}

		logger.trace(CommonDefinitions.END);

		return ret;
	}

	private UpdateLogicalIfStatus executeL2cpIntegrity(UpdateLogicalIfStatus ret){

		logger.trace(CommonDefinitions.START);

		for(VpnLogical vpn :ifStateFromFC.getSlices().getL2vpn()){
			VpnLogical chgvpn = new VpnLogical();
			chgvpn.setSliceId(vpn.getSliceId());
			chgvpn.setCps(new ArrayList<CpLogical>());

			for(CpLogical cp:vpn.getCps()){

				SliceKeySet slicekey = new SliceKeySet();
				slicekey.setCpId(cp.getCpId());
				slicekey.setSliceId(vpn.getSliceId());

				for(DeviceInformationSet dis:deviceDetail.values()){
					L2Cps lcp = dis.getL2SliceData().get(slicekey);

					if(lcp != null){
						integrateCpState(dis,chgvpn,vpn,cp,lcp.getIf_name());

						break;

					}else{
						logger.warn(LogFormatter.out.format(LogFormatter.MSG_407060,cp.getCpId(),vpn.getSliceId()));
					}
				}
			}

			if(!chgvpn.getCps().isEmpty()){
				ret.getSlices().getL2vpn().add(chgvpn);
			}else{
			}
		}

		logger.trace(CommonDefinitions.END);

		return ret;
	}

	private UpdateLogicalIfStatus executeL3cpIntegrity(UpdateLogicalIfStatus ret){

		logger.trace(CommonDefinitions.START);

		for(VpnLogical vpn :ifStateFromFC.getSlices().getL3vpn()){
			VpnLogical chgvpn = new VpnLogical();
			chgvpn.setSliceId(vpn.getSliceId());
			chgvpn.setCps(new ArrayList<CpLogical>());

			for(CpLogical cp:vpn.getCps()){

				SliceKeySet slicekey = new SliceKeySet();
				slicekey.setCpId(cp.getCpId());
				slicekey.setSliceId(vpn.getSliceId());

				for(DeviceInformationSet dis:deviceDetail.values()){
					L3Cps lcp = dis.getL3SliceData().get(slicekey);

					if(lcp != null){
						integrateCpState(dis,chgvpn,vpn,cp,lcp.getIf_name() + dis.getEquipmentsType().getUnit_connector() + String.valueOf(lcp.getVlan_id()));

						break;

					}else{
						logger.warn(LogFormatter.out.format(LogFormatter.MSG_407060,cp.getCpId(),vpn.getSliceId()));
					}
				}
			}

			if(!chgvpn.getCps().isEmpty()){
				ret.getSlices().getL3vpn().add(chgvpn);
			}else{
			}
		}

		logger.trace(CommonDefinitions.END);

		return ret;
	}

	private void integrateCpState(DeviceInformationSet dis,VpnLogical chgvpn,VpnLogical vpn,CpLogical cp,String ifname){

		logger.trace(CommonDefinitions.START);
		logger.debug("dis=" + dis + ", chgvpn=" + chgvpn + ", vpn=" + vpn + ", cp=" + cp + ", ifname=" + ifname);

		NodeKeySet target = new NodeKeySet();
		target.setEquipmentsData(dis.getEquipmentsData());
		target.setEquipmentsType(dis.getEquipmentsType());

		int ifstate = LogicalPhysicalConverter.toIntegerIFState(cp.getStatus());

		if(ifStateFromDevice.get(target) != null){
			for(SnmpIfOperStatus ifdev:ifStateFromDevice.get(target)){


					if(ifdev.getIfOperStatus() == SnmpIfOperStatus.IF_OPER_STATUS_LOWER_LAYER_DOWN){
						ifdev.setIfOperStatus(SnmpIfOperStatus.IF_OPER_STATUS_DOWN);
					}

						String tmpstate = LogicalPhysicalConverter.toStringIFState(ifdev.getIfOperStatus());

						CpLogical chgcp = new CpLogical();
						chgcp.setCpId(cp.getCpId());
						chgcp.setStatus(tmpstate);
						chgvpn.getCps().add(chgcp);
					}
					break;
				}else{
				}
			}
		}else{
		}

		logger.trace(CommonDefinitions.END);

		return;
	}

	protected GetLogicalIfStatus getIfStateFromFC() {
		return ifStateFromFC;
	}

	protected HashMap<NodeKeySet, ArrayList<SnmpIfOperStatus>> getIfStateFromDevice() {
		return ifStateFromDevice;
	}

	protected HashMap<NodeKeySet, DeviceInformationSet> getDeviceDetail() {
		return deviceDetail;
	}

	@Override
	public String toString() {
		return "IntegrityExecutor [ifStateFromFC=" + ifStateFromFC
				+ ", ifStateFromDevice=" + ifStateFromDevice
				+ ", deviceDetail=" + deviceDetail + "]";
	}

}
