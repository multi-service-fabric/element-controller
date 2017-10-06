
package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.InternalLinkLagAddDelete;
import msf.ecmm.emctrl.pojo.SpineAddDelete;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.DeleteSpine;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodeDeleteNode;

public class SpineRemove extends NodeRemove {

	private static final String ERROR_CODE_130102 = "130102";
	private static final String ERROR_CODE_130201 = "130201";
	private static final String ERROR_CODE_800401 = "800401";
	private static final String ERROR_CODE_800403 = "800403";
	private static final String ERROR_CODE_800405 = "800405";
	private static final String ERROR_CODE_800408 = "800408";
	private static final String ERROR_CODE_900410 = "900410";

	public SpineRemove(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.SpineRemove);
	}

	@Override
	protected boolean checkInData() {
		logger.trace(CommonDefinitions.START);

		boolean checkResult = true;

		DeleteSpine deleteSpineRest = (DeleteSpine) getInData();

		if (!getUriKeyMap().containsKey(KEY_NODE_ID)) {
			checkResult = false;
		} else {
			try {
				deleteSpineRest.check(getOperationType());
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
	protected List<Nodes> toNodeOppositeNodesReduced() {
		return DbMapper.toSpineOppositeNodesReduced((DeleteSpine) getInData());
	}

	@Override
	protected ArrayList<String> getOppositeNodeIdList() {
		logger.trace(CommonDefinitions.START);
		DeleteSpine deleteSpineRest = (DeleteSpine) getInData();
		ArrayList<String> oppositeNodeIdList = new ArrayList<String>();
		ArrayList<OppositeNodeDeleteNode> oppositeNodeDeleteNodeList = deleteSpineRest.getOppositeNodes();
		for (OppositeNodeDeleteNode oppositeNodeDeleteNode : oppositeNodeDeleteNodeList) {
			oppositeNodeIdList.add(oppositeNodeDeleteNode.getNodeId());
		}
		logger.trace(CommonDefinitions.END);
		return oppositeNodeIdList;
	}

	@Override
	protected boolean executeDeleteNode(Nodes targetNodeDb) throws EmctrlException {
		logger.trace(CommonDefinitions.START);

		SpineAddDelete spineAddDeleteEm = EmMapper.toSpineInfoNodeDelete(targetNodeDb.getNode_name());

		EmController emController = EmController.getInstance();
		AbstractMessage ret = emController.request(spineAddDeleteEm, false);

		logger.trace(CommonDefinitions.END);
		return ret.isResult();
	}

	@Override
	protected boolean executeDeleteLag(ArrayList<Nodes> oppoNodeList) throws EmctrlException, IllegalArgumentException {
		logger.trace(CommonDefinitions.START);

		InternalLinkLagAddDelete internalLagDelEm = EmMapper.toSpineInfoLagDelete((DeleteSpine) getInData(),
				oppoNodeList);

		EmController emController = EmController.getInstance();
		AbstractMessage ret = emController.request(internalLagDelEm, false);

		logger.trace(CommonDefinitions.END);
		return ret.isResult();
	}

	@Override
	protected boolean isOppositeNodesInfo() {
		return !((DeleteSpine) getInData()).getOppositeNodes().isEmpty();
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
	protected String getNotFoundDataErrorCode() {
		return ERROR_CODE_130201;
	}

	@Override
	protected String getExsistCpErrorCode() {
		return ERROR_CODE_800303;
	}

	@Override
	protected String getDhcpStopErrorCode() {
		return ERROR_CODE_800401;
	}

	@Override
	protected String getSyslogStopErrorCode() {
		return ERROR_CODE_800402;
	}

	@Override
	protected String getEm1ToErrorCode() {
		return ERROR_CODE_800403;
	}

	@Override
	protected String getEm1ErrorCode() {
		return ERROR_CODE_130404;
	}

	@Override
	protected String getEm2ToErrorCode() {
		return ERROR_CODE_800405;
	}

	@Override
	protected String getEm2ErrorCode() {
		return ERROR_CODE_130406;
	}

	@Override
	protected String getDbErrorCode() {
		return ERROR_CODE_800408;
	}

	@Override
	protected String getEmLockErrorCode() {
		return ERROR_CODE_800409;
	}

	@Override
	protected String getCommitErrorCode() {
		return ERROR_CODE_900410;
	}

	@Override
	protected String getInconsistentErrorCode() {
		return ERROR_CODE_800103;
	}
}
