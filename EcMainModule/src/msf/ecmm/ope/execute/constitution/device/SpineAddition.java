
package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.InternalLinkLagAddDelete;
import msf.ecmm.emctrl.pojo.SpineAddDelete;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.AddSpine;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodeAddSpine;

public class SpineAddition extends NodeAddition {

	private static final String ERROR_CODE_130102 = "130102";
	private static final String ERROR_CODE_130403 = "130403";
	private static final String ERROR_CODE_130405 = "130405";
	private static final String ERROR_CODE_130407 = "130407";
	private static final String ERROR_CODE_130409 = "130409";
	public SpineAddition(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.SpineAddition);
	}

	@Override
	protected boolean checkInData() {
		logger.trace(CommonDefinitions.START);

		boolean checkResult = true;

		AddSpine addSpineRest = (AddSpine) getInData();

		if (!getUriKeyMap().containsKey(KEY_NODE_ID)) {
			checkResult = false;
		} else {
			try {
				addSpineRest.check(getOperationType());
			} catch (CheckDataException e) {
				logger.warn("check error :", e);
				checkResult = false;
			}
		}

		logger.trace(CommonDefinitions.END);
		return checkResult;
	}

	@Override
	protected int getNodeType() {
		return CommonDefinitions.NODE_TYPE_SPINE;
	}

	@Override
	protected int getOppoNodeType() {
		return CommonDefinitions.NODE_TYPE_LEAF;
	}

	@Override
	protected ArrayList<String> getNeighborAddrList() {
		logger.trace(CommonDefinitions.START);
		ArrayList<String> neighborAddrList = new ArrayList<String>();
		AddSpine addSpineRest = (AddSpine) getInData();
		ArrayList<OppositeNodeAddSpine> oppositeNodeAddNodeList = addSpineRest.getOppositeNodes();
		for (OppositeNodeAddSpine oppositeNodeAddNode : oppositeNodeAddNodeList) {
			neighborAddrList.add(oppositeNodeAddNode.getInternalLinkIfs().getLagIf().getLinkIpAddress());
		}
		logger.trace(CommonDefinitions.END);
		return neighborAddrList;
	}

	@Override
	protected String getEquipmentTypeId() {
		AddSpine addSpineRest = (AddSpine) getInData();
		return addSpineRest.getEquipment().getEquipmentTypeId();
	}

	@Override
	protected List<Nodes> toNodeAdd(int clusterId, Equipments equipments, Nodes node, ArrayList<Nodes> oppoNodeList) {
		return DbMapper.toSpineAdd((AddSpine) getInData(), clusterId, equipments, node, oppoNodeList);
	}

	@Override
	protected boolean isNormalRegisterMode() {
		AddSpine addSpineRest = (AddSpine) getInData();
		return addSpineRest.getNode().getProvisioning();
	}

	@Override
	protected ArrayList<String> getOppositeNodeIdList() {
		logger.trace(CommonDefinitions.START);
		AddSpine addSpineRest = (AddSpine) getInData();
		ArrayList<String> oppositeNodeIdList = new ArrayList<String>();
		ArrayList<OppositeNodeAddSpine> oppositeNodeAddNodeList = addSpineRest.getOppositeNodes();
		for (OppositeNodeAddSpine oppositeNodeAddNode : oppositeNodeAddNodeList) {
			oppositeNodeIdList.add(oppositeNodeAddNode.getNodeId());
		}
		logger.trace(CommonDefinitions.END);
		return oppositeNodeIdList;
	}

	@Override
	protected boolean executeAddNode(Nodes nodes, String ecmainIpaddr) throws EmctrlException, IllegalArgumentException {
		logger.trace(CommonDefinitions.START);

		SpineAddDelete spineAddEm = EmMapper.toSpineInfoNodeCreate((AddSpine) getInData(), nodes, ecmainIpaddr);

		EmController emController = EmController.getInstance();
		AbstractMessage ret = emController.request(spineAddEm, false);

		logger.trace(CommonDefinitions.END);
		return ret.isResult();
	}

	@Override
	protected boolean executeAddLag(ArrayList<Nodes> oppoNodeList) throws EmctrlException, IllegalArgumentException {
		logger.trace(CommonDefinitions.START);

		InternalLinkLagAddDelete internalLagAddEm = EmMapper.toSpineInfoLagCreate((AddSpine) getInData(), oppoNodeList);

		EmController emController = EmController.getInstance();
		AbstractMessage ret = emController.request(internalLagAddEm, false);

		logger.trace(CommonDefinitions.END);
		return ret.isResult();
	}

	@Override
	protected boolean isOppositeNodesInfo() {
		return !((AddSpine) getInData()).getOppositeNodes().isEmpty();
	}

	@Override
	protected String getInputDataErrorCode() {
		return ERROR_CODE_130101;
	}

	@Override
	protected String getNotFoundSubDataErrorCode() {
		return ERROR_CODE_130102;
	}

	@Override
	protected String getDoubleRegisterErrorCode() {
		return ERROR_CODE_130301;
	}

	@Override
	protected String getEm1ToErrorCode() {
		return ERROR_CODE_130403;
	}

	@Override
	protected String getEm1ErrorCode() {
		return ERROR_CODE_130404;
	}

	@Override
	protected String getEm2ToErrorCode() {
		return ERROR_CODE_130405;
	}

	@Override
	protected String getEm2ErrorCode() {
		return ERROR_CODE_130406;
	}

	@Override
	protected String getOspfNeighbourUpErrorCode() {
		return ERROR_CODE_130407;
	}

	@Override
	protected String getDbErrorCode() {
		return ERROR_CODE_130408;
	}

	@Override
	protected String getEmLockErrorCode() {
		return ERROR_CODE_130409;
	}

	@Override
	protected String getCommitErrorCode() {
		return ERROR_CODE_900410;
	}
}
