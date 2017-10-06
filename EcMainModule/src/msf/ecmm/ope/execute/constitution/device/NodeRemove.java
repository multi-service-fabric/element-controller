
package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.devctrl.DevctrlCommon;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.DhcpController;
import msf.ecmm.devctrl.SyslogController;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.RequestQueueEntry;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

public abstract class NodeRemove extends Operation {

	private AbstractResponseMessage response = null;

	public NodeRemove(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
	}

	@Override
	public AbstractResponseMessage execute() {
		logger.trace(CommonDefinitions.START);

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
			return makeFailedResponse(RESP_BADREQUEST_400, getInputDataErrorCode());
		}

		String nodeId = getUriKeyMap().get(KEY_NODE_ID);

		boolean isOppositeNodesInfoFlag = isOppositeNodesInfo();

		boolean dhcpOkFlag = false;
		boolean emLockOkFlag = false;
		boolean em1FinFlag = false;

		try (DBAccessManager session = new DBAccessManager()) {

			DhcpController dhcpController = DhcpController.getInstance();
			dhcpController.stop(false);
			dhcpOkFlag = true;

			SyslogController syslogController = SyslogController.getInstance();
			syslogController.monitorStop(false);

			needCleanUpFlag = false;

			Nodes nodesDb = session.searchNodes(getNodeType(), nodeId, null);
			if (nodesDb == null) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
				return makeFailedResponse(RESP_NOTFOUND_404, getNotFoundDataErrorCode());
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

			session.startTransaction();

			session.deleteNodesRelation(getNodeType(), nodeId);

			List<Nodes> oppoNodeListFromRestInput = toNodeOppositeNodesReduced();
			boolean isOppositeEmExecuteFlag = deleteOppositeNodes(session, oppoNodeListFromRestInput);

			AbstractMessage lockKey = new AbstractMessage();
			RequestQueueEntry entry = null;
			try {
				entry = EmController.getInstance().lock(lockKey);
				emLockOkFlag = true;

				if (!executeDeleteNode(nodesDb)) {
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed. [Node]"));
					response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getEm1ErrorCode());
				}
				em1FinFlag = true;

				if (isOppositeNodesInfoFlag && isOppositeEmExecuteFlag) {
					if (!executeDeleteLag(oppoNodeList)) {
						logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041,
								"Request to EM was failed. [internalLag]"));
						response = makeFailedResponseWithoutOverwrite(RESP_INTERNALSERVERERROR_500, getEm2ErrorCode());
					}
				}
			} finally {
				if (entry != null) {
					EmController.getInstance().unlock(entry);
				}
			}

			session.commit();

			if (response == null) {
				response = makeSuccessResponse(RESP_NOCONTENTS_204, new CommonResponse());
			}

		} catch (DevctrlException e) {
			if (dhcpOkFlag == false) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "DHCP stop was failed."), e);
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getDhcpStopErrorCode());
			} else {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Watch syslog stop was failed."), e);
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getSyslogStopErrorCode());
			}
		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
			if (e.getCode() == DBAccessException.RELATIONSHIP_UNCONFORMITY) {
				response = makeFailedResponse(RESP_CONFLICT_409, getExsistCpErrorCode());
			} else if (e.getCode() == DBAccessException.NO_DELETE_TARGET) {
				response = makeFailedResponse(RESP_NOTFOUND_404, getNotFoundDataErrorCode());
			} else if (e.getCode() == DBAccessException.COMMIT_FAILURE && response == null) {
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getCommitErrorCode());
			} else {
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getDbErrorCode());
			}
		} catch (IllegalArgumentException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."), e);
			response = makeFailedResponse(RESP_BADREQUEST_400, getInconsistentErrorCode());
		} catch (EmctrlException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), e);
			if (emLockOkFlag == false) {
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getEmLockErrorCode());
			} else if (em1FinFlag == false) {
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getEm1ToErrorCode());
			} else {
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getEm2ToErrorCode());
			}
		} finally {
			if (needCleanUpFlag == true) {
				DevctrlCommon.cleanUp();
			}
		}

		logger.trace(CommonDefinitions.END);
		return response;
	}

	protected CommonResponse makeFailedResponseWithoutOverwrite(int rescode, String errcode) {
		if (response == null) {
			response = super.makeFailedResponse(rescode, errcode);
		}
	}

	protected abstract boolean checkInData();

	protected abstract int getNodeType();

	protected abstract int getOppoNodeType();

	protected abstract List<Nodes> toNodeOppositeNodesReduced();

	protected abstract ArrayList<String> getOppositeNodeIdList();

	protected abstract boolean executeDeleteNode(Nodes targetNodeDb) throws EmctrlException, IllegalArgumentException;

	protected abstract boolean executeDeleteLag(ArrayList<Nodes> oppoNodeList) throws EmctrlException,
			IllegalArgumentException;

	protected abstract boolean isOppositeNodesInfo();

	protected abstract String getInputDataErrorCode();

	protected abstract String getNotFoundSubDataErrorCode();

	protected abstract String getNotFoundDataErrorCode();

	protected abstract String getExsistCpErrorCode();

	protected abstract String getDhcpStopErrorCode();

	protected abstract String getSyslogStopErrorCode();

	protected abstract String getEm1ToErrorCode();

	protected abstract String getEm1ErrorCode();

	protected abstract String getEm2ToErrorCode();

	protected abstract String getEm2ErrorCode();

	protected abstract String getDbErrorCode();

	protected abstract String getEmLockErrorCode();

	protected abstract String getCommitErrorCode();

	protected abstract String getInconsistentErrorCode();

	protected boolean deleteOppositeNodes(DBAccessManager session, List<Nodes> nodesList) throws DBAccessException {
		logger.trace(CommonDefinitions.START);

		boolean result = true;

		try {
			session.deleteUpdateOppositeNodes(nodesList);
		} catch (DBAccessException e) {
			if (e.getCode() == DBAccessException.NO_DELETE_TARGET) {
				result = false;
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_409073));
			} else {
				throw e;
			}
		}

		logger.trace(CommonDefinitions.END);
		return result;
	}

}
