
package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.SnmpController;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.RequestQueueEntry;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

public abstract class NodeAddition extends Operation {

	public NodeAddition(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
	}

	@Override
	public AbstractResponseMessage execute() {
		logger.trace(CommonDefinitions.START);

		AbstractResponseMessage response = null;

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
			return makeFailedResponse(RESP_BADREQUEST_400, getInputDataErrorCode());
		}

		String nodeId = getUriKeyMap().get(KEY_NODE_ID);

		normanRegistrationFlag = isNormalRegisterMode();

		boolean isOppositeNodesInfoFlag = isOppositeNodesInfo();

		boolean emLockOkFlag = false;
		boolean em1OkFlag = false;

		try (DBAccessManager session = new DBAccessManager()) {

			int clusterId = CommonDefinitions.NOT_SET;
			int successfulResponseCode = CommonDefinitions.NOT_SET;

			Equipments equipments = session.searchEquipments(getEquipmentTypeId());
			if (equipments == null) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Equipments]"));
				return makeFailedResponse(RESP_BADREQUEST_400, getNotFoundSubDataErrorCode());
			}

			Nodes nodesFromDb = null;
			if (normanRegistrationFlag) {
				nodesFromDb = session.searchNodes(getNodeType(), nodeId, null);
				if (nodesFromDb == null) {
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
					return makeFailedResponse(RESP_BADREQUEST_400, getNotFoundSubDataErrorCode());
				}
				successfulResponseCode = RESP_OK_200;
			} else {
				clusterId = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.CLUSTER_ID);
				successfulResponseCode = RESP_CREATED_201;
			}

			ArrayList<Nodes> oppoNodeList = new ArrayList<Nodes>();
			ArrayList<String> oppoIdList = getOppositeNodeIdList();
			for (String oppoId : oppoIdList) {
				Nodes oppoNodesDb = session.searchNodes(getOppoNodeType(), oppoId, null);
				if (oppoNodesDb == null) {
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
					return makeFailedResponse(RESP_BADREQUEST_400, getNotFoundSubDataErrorCode());
				}
				oppoNodeList.add(oppoNodesDb);
			}

			List<Nodes> nodesListDb = toNodeAdd(clusterId, equipments, nodesFromDb, oppoNodeList);

			session.startTransaction();


			for (Nodes nodeDb : nodesListDb) {
				if (nodeDb.getNode_type() == getNodeType() && nodeDb.getNode_id().equals(nodeId)) {
				}
				if (normanRegistrationFlag == false && nodeDb.getNode_type() == getNodeType() && nodeDb.getNode_id().equals(nodeId)) {
					session.addNodes(nodeDb);
				} else {
					ArrayList<PhysicalIfs> physiIfList = new ArrayList<PhysicalIfs>(nodeDb.getPhysicalIfsList());
					ArrayList<LagIfs> lagIfList = new ArrayList<LagIfs>(nodeDb.getLagIfsList());
					session.addNodesRelation(physiIfList, lagIfList, null);
				}
			}

			AbstractMessage lockKey = new AbstractMessage();
			RequestQueueEntry entry = null;
			try {
				entry = EmController.getInstance().lock(lockKey);
				emLockOkFlag = true;

				String ecmainIpaddr = EcConfiguration.getInstance().get(String.class,
						EcConfiguration.DEVICE_EC_MANAGEMENT_IF);
				if (!executeAddNode(targetNodes, ecmainIpaddr)) {
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed. [Node]"));
					return makeFailedResponse(RESP_INTERNALSERVERERROR_500, getEm1ErrorCode());
				}
				em1OkFlag = true;

				if (isOppositeNodesInfoFlag) {
					if (!executeAddLag(oppoNodeList)) {
						logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed. [internalLag]"));
						return makeFailedResponse(RESP_INTERNALSERVERERROR_500, getEm2ErrorCode());
					}
				}
			} finally {
				if (entry != null) {
					EmController.getInstance().unlock(entry);
				}
			}

			if (isOppositeNodesInfoFlag) {
				SnmpController snmpController = new SnmpController();
				boolean ospfRet = snmpController.isOspfNeighborFull(equipments, targetNodes, getNeighborAddrList());
				if (ospfRet == false) {
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "OSPF Neighbor up check was failed."));
					return makeFailedResponse(RESP_INTERNALSERVERERROR_500, getOspfNeighbourUpErrorCode());
				}
			}

			session.commit();

			response = makeSuccessResponse(successfulResponseCode, new CommonResponse());

		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
			if (e.getCode() == DBAccessException.DOUBLE_REGISTRATION) {
				response = makeFailedResponse(RESP_CONFLICT_409, getDoubleRegisterErrorCode());
			} else if (e.getCode() == DBAccessException.COMMIT_FAILURE && response == null) {
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getCommitErrorCode());
			} else {
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getDbErrorCode());
			}
		} catch (IllegalArgumentException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."), e);
			response = makeFailedResponse(RESP_BADREQUEST_400, getInputDataErrorCode());
		} catch (EmctrlException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), e);
			if (emLockOkFlag == false) {
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getEmLockErrorCode());
			} else if (em1OkFlag == false) {
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getEm1ToErrorCode());
			} else {
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getEm2ToErrorCode());
			}
		} catch (DevctrlException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "OSPF Neighbor up check was failed."), e);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getOspfNeighbourUpErrorCode());
		}

		logger.trace(CommonDefinitions.END);
		return response;
	}

	protected abstract boolean isNormalRegisterMode();

	protected abstract boolean checkInData();

	protected abstract int getNodeType();

	protected abstract int getOppoNodeType();

	protected abstract String getEquipmentTypeId();

	protected abstract List<Nodes> toNodeAdd(int clusterId, Equipments equipments, Nodes node, ArrayList<Nodes> oppoNodeList);

	protected abstract ArrayList<String> getNeighborAddrList();

	protected abstract ArrayList<String> getOppositeNodeIdList();

	protected abstract boolean executeAddNode(Nodes nodes, String ecmainIpaddr) throws EmctrlException,
			IllegalArgumentException;

	protected abstract boolean executeAddLag(ArrayList<Nodes> oppoNodeList) throws EmctrlException;

	protected abstract boolean isOppositeNodesInfo();

	protected abstract String getInputDataErrorCode();

	protected abstract String getNotFoundSubDataErrorCode();

	protected abstract String getDoubleRegisterErrorCode();

	protected abstract String getEm1ToErrorCode();

	protected abstract String getEm1ErrorCode();

	protected abstract String getEm2ToErrorCode();

	protected abstract String getEm2ErrorCode();

	protected abstract String getOspfNeighbourUpErrorCode();

	protected abstract String getDbErrorCode();

	protected abstract String getEmLockErrorCode();

	protected abstract String getCommitErrorCode();

}
