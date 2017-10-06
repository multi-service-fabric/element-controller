package msf.ecmm.ope.control;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.convert.LogicalPhysicalConverter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.NodesStartupNotification;
import msf.ecmm.db.pojo.SystemStatus;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.NotifyNodeStartUpToFc;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OperationControlManager {

	private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	private ECMainState ecMainState;

	private boolean ifIntegrityExecution;

	static private OperationControlManager instance = null;

	private boolean unsentNodeStateNotificationSendingState;

	private final int MAX_OPERATION_ID = Integer.MAX_VALUE;

	private OperationControlManager() {
		logger.debug("OperationControlManager initialize member.");

		executeOperationHolder = new HashMap<EcSession, Operation>();
		ecMainState = ECMainState.Stop;
		ecMainObstruction = false;
		ifIntegrityExecution = false;
		lastOperationId = MIN_OPERATION_ID;
		queueMap = new HashMap<AbstractQueueEntryKey, LinkedList<OperationQueueEntry>>();
	}

	private void initialize() throws DBAccessException {
		logger.trace(CommonDefinitions.START);

		ECMainState ecMain;
		boolean obst;
		int obstint;

		String mainState = EcConfiguration.getInstance().get(String.class, EcConfiguration.SERVICE_STATUS);
		String obstructionState = EcConfiguration.getInstance().get(String.class, EcConfiguration.BLOCKADE_STATUS);
		ECMainState configState;
		if (mainState.equals("startready")) {
			logger.debug("Loading state is start ready.");
			configState = ECMainState.StartReady;
		} else {
			logger.debug("Loading state is change over.");
			configState = ECMainState.ChangeOver;
		}

		ECMainState state = getEcMainState(true);

		if (state == null) {
			logger.debug("First boot.");
			if (obstructionState.equals("busy")) {
				logger.debug("Loading obstructionState is busy.");
				obst = true;
				obstint = CommonDefinitions.EC_BUSY_VALUE;
			} else {
				logger.debug("Loading obstructionState is in-service.");
				obst = false;
				obstint = CommonDefinitions.EC_IN_SERVICE_VALUE;
			}

			ecMain = configState;
			logger.debug("Not first boot.");
			obst = getEcMainObstraction(true);
			obstint = LogicalPhysicalConverter.toIntegerECObstructionState(obst);

			if (state != ECMainState.Stop) {
				ecMain = ECMainState.ChangeOver;
			} else {
				ecMain = configState;
			}
		}

		ecMainState = ecMain;
		ecMainObstruction = obst;

		try (DBAccessManager session = new DBAccessManager()) {

			session.startTransaction();

			if (state != null) {
				session.updateSystemStatus(ecMain.getValue(), obstint);
			} else {
				SystemStatus systemStatus = new SystemStatus();
				systemStatus.setBlockade_status(obstint);
				systemStatus.setService_status(ecMain.getValue());
				session.addSystemStatus(systemStatus);
			}

			session.commit();

		}

		logger.trace(CommonDefinitions.END);
	}

	private EcSession paidOutOperationId() {

		logger.trace(CommonDefinitions.START);

		lastOperationId++;

		if ((lastOperationId >= MAX_OPERATION_ID) || (lastOperationId < MIN_OPERATION_ID)) {
			logger.debug("Paid operation id was initialized.");
			lastOperationId = MIN_OPERATION_ID;
		} else {
		}

		EcSession ret = new EcSession(lastOperationId);

		logger.trace(CommonDefinitions.END + ", return=" + ret);

		return ret;
	}

	private void registerExecutingOperation(EcSession opeId, Operation ope) {

		logger.trace(CommonDefinitions.START);

			executeOperationHolder.put(opeId, ope);
		} else {
			logger.debug("This operation id was already registered. :" + ope);
			throw new IllegalArgumentException();
		}

		logger.trace(CommonDefinitions.END);

		return;
	}

	protected void terminateOperation(EcSession opeId) {

		logger.trace(CommonDefinitions.START);

			executeOperationHolder.remove(opeId);

			synchronized (queueMap) {
				for (Entry<AbstractQueueEntryKey, LinkedList<OperationQueueEntry>> queue : queueMap.entrySet()) {

					OperationQueueEntry target = null;
					for (OperationQueueEntry entry : queue.getValue()) {
						if (entry.getOperationId().getOperationId() == opeId.getOperationId()) {
							target = entry;
							break;
						}
					}

					if (target != null) {
						queue.getValue().remove(target);
						if (queue.getValue().size() == 0) {
							queueMap.remove(queue.getKey());
							break;
						}
					}
				}
			}

		} else {
			logger.debug("This operation id could not be remove from executing operation list. :" + opeId);
			throw new IllegalArgumentException();
		}

		logger.trace(CommonDefinitions.END);
	}

	public boolean updateEcMainState(boolean todb, ECMainState state) throws DBAccessException {

		logger.trace(CommonDefinitions.START + ",todb=" + todb);

		switch (ecMainState) {
		case Stop:
			if (state == ECMainState.StartReady) {
				break;
			} else {
				return false;
			}

		case StartReady:
			if (state == ECMainState.StopReady) {
				break;
			} else if (state == ECMainState.InService) {
				break;
			} else {
				return false;
			}

		case StopReady:
			if (state == ECMainState.Stop) {
				break;
			} else {
				return false;
			}

		case ChangeOver:
			if (state == ECMainState.StopReady) {
				break;
			} else if (state == ECMainState.InService) {
				break;
			} else {
				return false;
			}

		case InService:
			if (state == ECMainState.StopReady) {
				break;
			} else if (state == ECMainState.ChangeOver) {
				break;
			} else {
				return false;
			}

		default:
			return false;
		}

		if (todb) {
			logger.debug("Update db.");

			try (DBAccessManager session = new DBAccessManager()) {

				session.startTransaction();
				session.updateSystemStatus(state.getValue(), -1);
				session.commit();

				ecMainState = state;

				logger.trace(CommonDefinitions.END + ", return=true");

				return true;
			}
			ecMainState = state;

			logger.trace(CommonDefinitions.END + ", return=true");

			return true;
		}
	}

	public boolean updateobstructionState(boolean todb, boolean obstruction) throws DBAccessException {

		logger.trace(CommonDefinitions.START + ",todb=" + todb);

		if (todb) {
			logger.debug("Update db.");

			try (DBAccessManager session = new DBAccessManager()) {

				session.startTransaction();
				session.updateSystemStatus(-1, LogicalPhysicalConverter.toIntegerECObstructionState(obstruction));
				session.commit();

				ecMainObstruction = obstruction;

				logger.trace(CommonDefinitions.END + ", return=true");

				return true;
			}
			ecMainObstruction = obstruction;

			logger.trace(CommonDefinitions.END + ", return=true");

			return true;
		}
	}

	public boolean judgeExecution(OperationType operationType) {

		logger.trace(CommonDefinitions.START);
		logger.debug("operationType=" + operationType);

		try {
				logger.debug("IF state integrity check.");
				return !(OperationControlManager.getInstance().isIfIntegrityExecution());

				logger.debug("EC main state is stop.");

				return false;
			} else {
				switch (operationType) {
				case ECMainStopper:
				case ECMainStateConfirm:
					logger.debug("Priority operation");
					return true;

				case L3CPChange:
				case AllL3CPCreate:
				case AllL2CPCreate:
				case AllL3CPRemove:
				case AllL2CPRemove:
				case SNMPTrapSignalRecieveNotification:
				case TrafficDataAcquisition:
				case DeviceInfoRegistration:
				case DeviceInfoAcquisition:
				case DeviceInfoRemove:
				case LeafInfoRegistration:
				case SpineInfoRegistration:
				case LeafAddition:
				case SpineAddition:
				case LeafRemove:
				case SpineRemove:
				case NodeAddedNotification:
				case PhysicalIfInfoAcquisition:
				case PhysicalIfInfoChange:
				case LagCreate:
				case LagInfoAcquisition:
				case LagRemove:
				case AllDeviceTypeInfoAcquisition:
				case AllIfInfoAcquisition:
				case AllPhysicalIfInfoAcquisition:
				case AllLagInfoAcquisition:
				case TrafficDataGathering:
				case IFStateIntegrity:
					logger.debug("Normal operation");
					if ((OperationControlManager.getInstance().getEcMainState(false) == ECMainState.InService)
							&& !(OperationControlManager.getInstance().getEcMainObstraction(false))) {
						return true;
					} else {
						logger.debug("Normal operation rejected.");
						return false;
					}
					logger.debug("Not defined operation");
					return false;
				}
			}
		} catch (DBAccessException e) {
			logger.debug("Internal Error.", e);
			return false;
		}
	}

	public boolean recievestartIfIngIntegrity() {

		logger.trace(CommonDefinitions.START);

		if (judgeExecution(OperationType.IFStateIntegrity)) {

			ifIntegrityExecution = true;

			logger.trace(CommonDefinitions.END + "result=true");

			return true;
		} else {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_402055));

			logger.trace(CommonDefinitions.END + "result=false");

			return false;
		}
	}

	public void recieveEndIfIntegrity() {
		logger.trace(CommonDefinitions.START);

		ifIntegrityExecution = false;

		logger.trace(CommonDefinitions.END);
	}

	public boolean isIfIntegrityExecution() {
		return ifIntegrityExecution;
	}

	public EcSession startOperation(Operation operation) {

		EcSession oid = paidOutOperationId();

		if (oid != null) {
			registerExecutingOperation(oid, operation);

			try {
				if (isLockTarget(operation.getOperationType())) {
					int timeout = EcConfiguration.getInstance().get(Integer.class,
							EcConfiguration.OPERATION_QUEUE_TIMEOUT);

					OperationLockKey key = new OperationLockKey(operation.getOperationType().getValue(),
							operation.getNodeId(), operation.getFabricType());
					OperationQueueEntry entry = new OperationQueueEntry(oid);

					LinkedList<OperationQueueEntry> queue = null;
					synchronized (queueMap) {
						queue = queueMap.get(key);
						if (queue == null) {
							queue = new LinkedList<OperationQueueEntry>();
							queueMap.put(key, queue);
						}
						queue.add(entry);
					}

					for (;;) {
						Date now = new Date();
						if ((now.getTime() - entry.getTimestamp().getTime() > timeout * 1000)
								|| (((operation.getOperationType() != OperationType.ECMainStateConfirm)
										&& (operation.getOperationType() != OperationType.ECMainStopper)
										&& (operation.getOperationType() != OperationType.ObstructionStateController))
										&& ((ecMainState == ECMainState.StopReady) || (ecMainState == ECMainState.ChangeOver)))) {
							synchronized (queueMap) {
								queue.remove(entry);
								if (queue.size() == 0) {
									queueMap.remove(key);
								}
							}
							terminateOperation(oid);
							logger.debug("timeout.");
						}

						synchronized (queueMap) {
								break;
							}
						}
						CommonUtil.sleep();
					}
				}

			} catch (IllegalArgumentException e) {
				logger.debug("Operation ID was duplicated.", e);
				oid = null;
			}

			if (operation.getOperationType() == OperationType.ECMainStopper) {
				for (EcSession compareOid : executeOperationHolder.keySet()) {
					if ((executeOperationHolder.get(compareOid).getOperationType() == OperationType.ECMainStopper)
							&& (compareOid != oid)) {
						terminateOperation(oid);
						oid = null;
						break;
					} else {
					}
				}
			} else {
			}
		} else {
		}

		return oid;
	}

	private boolean isLockTarget(OperationType operationType) {
		switch (operationType) {
		case L3CPChange:
		case AllL3CPCreate:
		case AllL2CPCreate:
		case AllL3CPRemove:
		case AllL2CPRemove:
		case SNMPTrapSignalRecieveNotification:
		case DeviceInfoRegistration:
		case DeviceInfoRemove:
		case LeafInfoRegistration:
		case SpineInfoRegistration:
		case LeafAddition:
		case SpineAddition:
		case LeafRemove:
		case SpineRemove:
		case NodeAddedNotification:
		case PhysicalIfInfoChange:
		case LagCreate:
		case TrafficDataGathering:
		case IFStateIntegrity:
			return true;
		default:

			return false;
		}
	}

	public int getNumberOfExecuteOperations() {
		return executeOperationHolder.size();
	}

	public ECMainState getEcMainState(boolean fromdb) throws DBAccessException {

		logger.trace(CommonDefinitions.START + ",fromdb=" + fromdb);

		if (fromdb) {
			logger.debug("Read db.");

			try (DBAccessManager session = new DBAccessManager()) {

				ECMainState ret = null;
				SystemStatus dbstate = session.getSystemStatus();

				if (dbstate != null) {
					ret = ECMainState.getState(dbstate.getService_status());
				} else {
				}

				logger.trace(CommonDefinitions.END + ", return=" + ret);

				return ret;
			}
		} else {
			logger.trace(CommonDefinitions.END + ", return=" + ecMainState);

			return ecMainState;
		}
	}

	public boolean getEcMainObstraction(boolean fromdb) throws DBAccessException {

		logger.trace(CommonDefinitions.START + ",fromdb=" + fromdb);

		if (fromdb) {
			logger.debug("Read db.");

			try (DBAccessManager session = new DBAccessManager()) {
				boolean ret = LogicalPhysicalConverter.toBooleanECObstructionState(session.getSystemStatus()
						.getBlockade_status());

				logger.trace(CommonDefinitions.END + ", return=" + ret);
				return ret;

			}
		} else {
			logger.trace(CommonDefinitions.END + ", return=" + ecMainObstruction);

			return ecMainObstruction;
		}
	}

	public static synchronized OperationControlManager boot() {

		logger.trace(CommonDefinitions.START);

		if (instance == null) {
			instance = new OperationControlManager();

			try {
				instance.initialize();
			} catch (DBAccessException e) {
				logger.debug("DB access error occured in OperationControlManager intializing.", e);
				instance = null;
			}

			logger.trace(CommonDefinitions.END);

			return instance;
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_402056));
			return null;
		}
	}

	public static OperationControlManager getInstance() {
		return instance;
	}

	public boolean isUnsentNodeStateNotificationSendingState() {
		return unsentNodeStateNotificationSendingState;
	}

	protected void setUnsentNodeStateNotificationSendingState(boolean unsentNodeStateNotificationSendingState) {
		this.unsentNodeStateNotificationSendingState = unsentNodeStateNotificationSendingState;
	}

	protected NodeStateNotificationSender getNodeStateNotificationSenderHolder() {
		return nodeStateNotificationSenderHolder;
	}

	protected void setNodeStateNotificationSenderHolder(NodeStateNotificationSender nodeStateNotificationSenderHolder) {
		this.nodeStateNotificationSenderHolder = nodeStateNotificationSenderHolder;
	}

	public void sendUnsentNodeStateNotification() {
		logger.trace(CommonDefinitions.START);

		List<NodesStartupNotification> list;

		try (DBAccessManager session = new DBAccessManager()) {

			String clusterId = EcConfiguration.getInstance().get(String.class, EcConfiguration.CLUSTER_ID);

			list = session.getNodesStartupNotificationList();

			for (NodesStartupNotification nsn : list) {
				RestClient rc = new RestClient();
				HashMap<String, String> keyMap = new HashMap<String, String>();
				NotifyNodeStartUpToFc nnsu = new NotifyNodeStartUpToFc();

				keyMap.put(KEY_CLUSTER_ID, clusterId);
				keyMap.put(KEY_NODE_ID, nsn.getNode_id());

				if (nsn.getNotification_reception_status() == CommonDefinitions.RECV_OK_NOTIFICATION) {
					nnsu.setStatus(CommonDefinitions.RECV_OK_NOTIFICATION_STRING);
				} else if (nsn.getNotification_reception_status() == CommonDefinitions.RECV_NG_NOTIFICATION) {
					nnsu.setStatus(CommonDefinitions.RECV_NG_NOTIFICATION_STRING);
				} else {
					nnsu.setStatus(CommonDefinitions.WAIT_NOTIFICATION_STRING);
				}

				try {
					if (nsn.getNode_type() == RestClient.NOTIFY_LEAF_STARTUP) {
						rc.request(RestClient.NOTIFY_LEAF_STARTUP, keyMap, nnsu, CommonResponseFromFc.class);
					} else {
						rc.request(RestClient.NOTIFY_SPINE_STARTUP, keyMap, nnsu, CommonResponseFromFc.class);
					}
				} catch (RestClientException e) {
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_402048, "REST request"), e);
				}

			}
		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_402048, "DB access"), e);
			list = null;
		}

		if (list != null) {
			try {
				DBAccessManager session2 = new DBAccessManager();

				session2.startTransaction();

				for (NodesStartupNotification nsn : list) {
					try {
						session2.deleteNodesStartupNotification(nsn.getNode_type(), nsn.getNode_id());
					} catch (DBAccessException e) {
						logger.warn(LogFormatter.out.format(LogFormatter.MSG_402048, "DB access"), e);
					}
				}

				try {
					session2.commit();
				} catch (DBAccessException e) {
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_402048, "DB access"), e);
				}
				session2.close();
			} catch (DBAccessException e0) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_402048, "DB access"), e0);
			}
		} else {
		}

		if (isUnsentNodeStateNotificationSendingState()) {
			setUnsentNodeStateNotificationSendingState(false);
		} else {
		}

		logger.trace(CommonDefinitions.END);

		return;
	}

	public void startSendingUnsentNodeStateNotification() {

		logger.trace(CommonDefinitions.START);

		setUnsentNodeStateNotificationSendingState(true);

		NodeStateNotificationSender thread = new NodeStateNotificationSender();

		setNodeStateNotificationSenderHolder(thread);

		thread.start();

		logger.trace(CommonDefinitions.END);

		return;
	}

	@Override
	public String toString() {
		return "OperationControlManager [executeOperationHolder="
				+ executeOperationHolder + ", ecMainState=" + ecMainState
				+ ", ecMainObstruction=" + ecMainObstruction
				+ ", ifIntegrityExecution=" + ifIntegrityExecution
				+ ", lastOperationId=" + lastOperationId + ", queueMap="
				+ queueMap + "]";
	}

}
