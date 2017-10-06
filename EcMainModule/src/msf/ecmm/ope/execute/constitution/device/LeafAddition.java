
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
import msf.ecmm.emctrl.pojo.LeafAddDelete;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.AddLeaf;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodeAddLeaf;

public class LeafAddition extends NodeAddition {

	private static final String ERROR_CODE_080102 = "080102";
	private static final String ERROR_CODE_080403 = "080403";
	private static final String ERROR_CODE_080405 = "080405";
	private static final String ERROR_CODE_080407 = "080407";
	private static final String ERROR_CODE_080409 = "080409";
	public LeafAddition(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.LeafAddition);
	}

	@Override
	protected boolean checkInData() {
		logger.trace(CommonDefinitions.START);

		boolean checkResult = true;

		AddLeaf addLeafRest = (AddLeaf) getInData();

		if (!getUriKeyMap().containsKey(KEY_NODE_ID)) {
			checkResult = false;
		} else {
			try {
				addLeafRest.check(getOperationType());
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
		return CommonDefinitions.NODE_TYPE_LEAF;
	}

	@Override
	protected int getOppoNodeType() {
		return CommonDefinitions.NODE_TYPE_SPINE;
	}

	@Override
	protected ArrayList<String> getNeighborAddrList() {
		logger.trace(CommonDefinitions.START);
		ArrayList<String> neighborAddrList = new ArrayList<String>();
		AddLeaf addLeafRest = (AddLeaf) getInData();
		ArrayList<OppositeNodeAddLeaf> oppositeNodeAddNodeList = addLeafRest.getOppositeNodes();
		for (OppositeNodeAddLeaf oppositeNodeAddNode : oppositeNodeAddNodeList) {
			neighborAddrList.add(oppositeNodeAddNode.getInternalLinkIf().getLagIf().getLinkIpAddress());
		}
		logger.trace(CommonDefinitions.END);
		return neighborAddrList;
	}

	@Override
	protected String getEquipmentTypeId() {
		AddLeaf addLeafRest = (AddLeaf) getInData();
		return addLeafRest.getEquipment().getEquipmentTypeId();
	}

	@Override
	protected List<Nodes> toNodeAdd(int clusterId, Equipments equipments, Nodes node, ArrayList<Nodes> oppoNodeList) {
		return DbMapper.toLeafAdd((AddLeaf) getInData(), clusterId, equipments, node, oppoNodeList);
	}

	@Override
	protected boolean isNormalRegisterMode() {
		AddLeaf addLeafRest = (AddLeaf) getInData();
		return addLeafRest.getNode().getProvisioning();
	}

	@Override
	protected ArrayList<String> getOppositeNodeIdList() {
		logger.trace(CommonDefinitions.START);
		AddLeaf addLeafRest = (AddLeaf) getInData();
		ArrayList<String> oppositeNodeIdList = new ArrayList<String>();
		ArrayList<OppositeNodeAddLeaf> oppositeNodeAddNodeList = addLeafRest.getOppositeNodes();
		for (OppositeNodeAddLeaf oppositeNodeAddNode : oppositeNodeAddNodeList) {
			oppositeNodeIdList.add(oppositeNodeAddNode.getNodeId());
		}
		logger.trace(CommonDefinitions.END);
		return oppositeNodeIdList;
	}

	@Override
	protected boolean executeAddNode(Nodes nodes, String ecmainIpaddr) throws EmctrlException, IllegalArgumentException {
		logger.trace(CommonDefinitions.START);

		LeafAddDelete leafAddEm = EmMapper.toLeafInfoNodeCreate((AddLeaf) getInData(), nodes, ecmainIpaddr);

		EmController emController = EmController.getInstance();
		AbstractMessage ret = emController.request(leafAddEm, false);

		logger.trace(CommonDefinitions.END);
		return ret.isResult();
	}

	@Override
	protected boolean executeAddLag(ArrayList<Nodes> oppoNodeList) throws EmctrlException, IllegalArgumentException {
		logger.trace(CommonDefinitions.START);

		InternalLinkLagAddDelete internalLagAddEm = EmMapper.toLeafInfoLagCreate((AddLeaf) getInData(), oppoNodeList);

		EmController emController = EmController.getInstance();
		AbstractMessage ret = emController.request(internalLagAddEm, false);

		logger.trace(CommonDefinitions.END);
		return ret.isResult();
	}

	@Override
	protected boolean isOppositeNodesInfo() {
		return !((AddLeaf) getInData()).getOppositeNodes().isEmpty();
	}

	@Override
	protected String getInputDataErrorCode() {
		return ERROR_CODE_080101;
	}

	@Override
	protected String getNotFoundSubDataErrorCode() {
		return ERROR_CODE_080102;
	}

	@Override
	protected String getDoubleRegisterErrorCode() {
		return ERROR_CODE_080301;
	}

	@Override
	protected String getEm1ToErrorCode() {
		return ERROR_CODE_080403;
	}

	@Override
	protected String getEm1ErrorCode() {
		return ERROR_CODE_080404;
	}

	@Override
	protected String getEm2ToErrorCode() {
		return ERROR_CODE_080405;
	}

	@Override
	protected String getEm2ErrorCode() {
		return ERROR_CODE_080406;
	}

	@Override
	protected String getOspfNeighbourUpErrorCode() {
		return ERROR_CODE_080407;
	}

	@Override
	protected String getDbErrorCode() {
		return ERROR_CODE_080408;
	}

	@Override
	protected String getEmLockErrorCode() {
		return ERROR_CODE_080409;
	}

	@Override
	protected String getCommitErrorCode() {
		return ERROR_CODE_900410;
	}
}
