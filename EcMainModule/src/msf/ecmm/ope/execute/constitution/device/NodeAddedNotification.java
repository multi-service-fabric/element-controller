
package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.NodesStartupNotification;
import msf.ecmm.devctrl.DevctrlCommon;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.DhcpController;
import msf.ecmm.devctrl.SyslogController;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.NotifyNodeStartUpToFc;
import msf.ecmm.ope.control.ECMainState;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.NotifyNodeStartUp;

public class NodeAddedNotification extends Operation {

	private static final String ERROR_CODE_290402 = "290402";
	private static final String ERROR_CODE_290404 = "290404";

	private static final String BOOT_RET_FAILED = "failed";
	private int bootNodeType = CommonDefinitions.NODE_TYPE_LEAF;

	public NodeAddedNotification(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.NodeAddedNotification);
	}

	@Override
	public boolean prepare() {

		logger.trace(CommonDefinitions.START);

		boolean result = false;

		boolean judge = false;
		synchronized(OperationControlManager.getInstance()){

			judge = OperationControlManager.getInstance().judgeExecution(getOperationType());
			if (judge) {
				operationId = OperationControlManager.getInstance().startOperation(this);
				if (operationId == null) {
					judge = false;
				}
			}


			do {
				if (judge == false && getEcMainState() != ECMainState.InService) {
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "NodeAddedNotification operation rejected."));
					reqNotifyFlag = false;
					reqDeleteFlag = false;
					break;
				}

				waitNodeStateNotificationSending();

				if (!checkInData()) {
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
					reqNotifyFlag = false;
					reqDeleteFlag = false;
					break;
				}

				if (getUriKeyMap().get(KEY_STATUS).equals(BOOT_RET_SUCCESS)) {
					bootStatus = true;
				} else {
					bootStatus = false;
				}

				clusterId = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.CLUSTER_ID);

				try (DBAccessManager session = new DBAccessManager()) {

					session.startTransaction();

					NotifyNodeStartUp notifyNodeStartUpRest = (NotifyNodeStartUp) getInData();
					Nodes bootNode = session.searchNodes(CommonDefinitions.NOT_SET, null,
							notifyNodeStartUpRest.getManagementIfAddress());
					if (bootNode == null) {
						logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
						break;
					}

					bootNodeType = bootNode.getNode_type();
					bootNodeId = bootNode.getNode_id();
					logger.debug("boot nodeType=" + bootNodeType + " boot nodeId=" + bootNodeId);

					NodesStartupNotification nodeStartupNoticeDb = createNodeStartupInfo(bootNode);
					session.updateNodesStartupNotification(nodeStartupNoticeDb);

					session.commit();

					if (judge == true) {
						result = true;
					}

				} catch (DBAccessException e) {
					if (e.getCode() == DBAccessException.NO_UPDATE_TARGET) {
						logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [NodesStartupNotification]"));
					} else {
						logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
					}
				}
			} while (false);

			if (result == false) {
				cleanUp(true, reqNotifyFlag, reqDeleteFlag);
			}

		logger.trace(CommonDefinitions.END);
		return result;
	}

	@Override
	public AbstractResponseMessage execute() {
		logger.trace(CommonDefinitions.START);

		AbstractResponseMessage response = null;

		boolean dhcpOkFlag = false;
		boolean syslogOkFlag = false;
		boolean reqNotifyFlag = true;
		boolean reqDeleteFlag = true;

		try {

			DhcpController dhcpController = DhcpController.getInstance();
			dhcpController.stop(true);
			dhcpOkFlag = true;

			SyslogController syslogController = SyslogController.getInstance();
			syslogOkFlag = true;

			requestToFc(true);
			reqNotifyFlag = false;

			deleteNodesStartupNotification();
			reqDeleteFlag = false;

			response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

			needCleanUpFlag = false;

		} catch (DevctrlException e) {
			if (dhcpOkFlag == false) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "DHCP stop was failed."), e);
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_290403);
			} else {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Watch syslog stop was failed."), e);
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_290404);
			}
		} catch (RestClientException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request rest was failed."), e);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_290401);
			reqNotifyFlag = false;
		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_290402);
			reqDeleteFlag = false;
		} finally {
			if (needCleanUpFlag == true) {
				cleanUp(!syslogOkFlag, reqNotifyFlag, reqDeleteFlag);
			}
		}

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@Override
	protected boolean checkInData() {
		logger.trace(CommonDefinitions.START);

		boolean checkResult = true;

		NotifyNodeStartUp notifyNodeStartUp = (NotifyNodeStartUp) getInData();

		if (!getUriKeyMap().containsKey(KEY_STATUS)) {
			checkResult = false;
		} else {
			String status = getUriKeyMap().get(KEY_STATUS);
			if (!status.equals("success") && !status.equals("failed")) {
				checkResult = false;
			}
		}
		if (checkResult) {
			try {
				notifyNodeStartUp.check(getOperationType());
			} catch (CheckDataException e) {
				logger.warn("check error :", e);
				checkResult = false;
			}
		}

		logger.trace(CommonDefinitions.END);
		return checkResult;
	}

	private NodesStartupNotification createNodeStartupInfo(Nodes node) {
		logger.trace(CommonDefinitions.START);
		logger.debug(node);
		NodesStartupNotification nodeStartupNoticeDb = new NodesStartupNotification();
		nodeStartupNoticeDb.setNode_id(node.getNode_id());
		nodeStartupNoticeDb.setNode_type(node.getNode_type());
		int notification = CommonDefinitions.RECV_NG_NOTIFICATION;
		if (bootStatus) {
			notification = CommonDefinitions.RECV_OK_NOTIFICATION;
		}
		nodeStartupNoticeDb.setNotification_reception_status(notification);
		nodeStartupNoticeDb.setNodes(node);
		logger.trace(CommonDefinitions.END);
		return nodeStartupNoticeDb;
	}

	private void requestToFc(boolean executeResult) throws RestClientException {
		logger.trace(CommonDefinitions.START);
		logger.debug(executeResult);

		NotifyNodeStartUpToFc sendMessage = new NotifyNodeStartUpToFc();
		String status = "";
		if (bootStatus == false) {
			status = BOOT_RET_FAILED;
		} else {
			if (executeResult == true) {
				status = BOOT_RET_SUCCESS;
			} else {
				status = BOOT_RET_CANCEL;
			}
		}
		sendMessage.setStatus(status);

		HashMap<String, String> keyMap = new HashMap<String, String>();
		keyMap.put(KEY_CLUSTER_ID, String.valueOf(clusterId));
		keyMap.put(KEY_NODE_ID, String.valueOf(bootNodeId));

		int requestType = CommonDefinitions.NOT_SET;
		if (bootNodeType == CommonDefinitions.NODE_TYPE_LEAF) {
			requestType = RestClient.NOTIFY_LEAF_STARTUP;
		} else {
			requestType = RestClient.NOTIFY_SPINE_STARTUP;
		}

		new RestClient().request(requestType, keyMap, sendMessage, CommonResponseFromFc.class);

		logger.trace(CommonDefinitions.END);
	}

	private void cleanUp(boolean reqDhcpSyslogFlag, boolean reqNotifyFlag, boolean reqDeleteFlag) {
		logger.trace(CommonDefinitions.START);
		logger.debug("reqDhcpSyslogFlag:" + reqDhcpSyslogFlag + " reqNotifyFlag:" + reqNotifyFlag + " reqDeleteFlag:" + reqDeleteFlag);

		try {
			if (reqDhcpSyslogFlag) {
				DevctrlCommon.cleanUp();
			}

			if (reqNotifyFlag) {
				requestToFc(false);
			}

			if (reqDeleteFlag) {
				deleteNodesStartupNotification();
			}

		} catch (RestClientException | DBAccessException e) {
			logger.debug("cleanUp error : ", e);
		}
		logger.trace(CommonDefinitions.END);
	}

	private void deleteNodesStartupNotification() throws DBAccessException {
		logger.trace(CommonDefinitions.START);

		try (DBAccessManager session = new DBAccessManager()) {
			session.startTransaction();
			session.deleteNodesStartupNotification(bootNodeType, bootNodeId);
			session.commit();
		} catch (DBAccessException e) {
			if (e.getCode() == DBAccessException.NO_DELETE_TARGET) {
				logger.debug("Not found record. node_type:" + bootNodeType + "node_id:" + bootNodeId);
			} else {
				throw e;
			}
		}
		logger.trace(CommonDefinitions.END);
	}

	private ECMainState getEcMainState() {
		logger.trace(CommonDefinitions.START);
		ECMainState status = null;

		try {
			status = OperationControlManager.getInstance().getEcMainState(false);
		} catch (DBAccessException e) {
			logger.debug("Internal Error.", e);
			status = ECMainState.Stop;
		}
		logger.trace(CommonDefinitions.END);
		return status;
	}

	private void waitNodeStateNotificationSending() {
		logger.trace(CommonDefinitions.START);
		while (OperationControlManager.getInstance().isUnsentNodeStateNotificationSendingState()) {
			CommonUtil.sleep();
		}
		logger.trace(CommonDefinitions.END);
		return;
	}
}
