/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.control;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.execute.OperationType.*;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.config.ExpandOperationDetailInfo;
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
import msf.ecmm.ope.execute.constitution.device.NodeAdditionThread;

/**
 * Operation Management Class Definition. Managing the operations.
 */
public class OperationControlManager {

  /**
   * logger.
   */
  private static final MsfLogger logger = new MsfLogger();

  /** Running Operation List. */
  private HashMap<EcSession, Operation> executeOperationHolder;

  /** EC Status. */
  private ECMainState ecMainState;

  /** EC Blockage Status. */
  private boolean ecMainObstruction;

  /** IF Integrity Execution Status. */
  private boolean ifIntegrityExecution;

  /** Final Payout Operation ID. */
  private int lastOperationId;

  /** Own Instance Holder. */
  private static OperationControlManager instance = null;

  /** Operation Queue. */
  private HashMap<AbstractQueueEntryKey, LinkedList<OperationQueueEntry>> queueMap;

  /** Unsent Device Start-up Notification Sending Flag. */
  private boolean unsentNodeStateNotificationSendingState;

  /** Device Start-up Notification Management Instance. */
  private NodeStateNotificationSender nodeStateNotificationSenderHolder;

  /** Thread for Device Extention. */
  private NodeAdditionThread nodeAdditionThread;

  /** Additional service recovery executing flag. */
  private boolean recoverExecutionFlag = false;

  /** Operation ID Max. Value. */
  private static final int MAX_OPERATION_ID = Integer.MAX_VALUE;

  /** Operation ID Min. Value. */
  private static final int MIN_OPERATION_ID = 0;

  /** Extended information. */
  private ConcurrentHashMap<String, Object> operationInfo = new ConcurrentHashMap<>();

  /** Flag indicating Config-Audit is running. */
  private boolean configAuditExecution;

  /**  Flag indicating Resource-monitoring is running. */
  private boolean resourceCheckExecution;

  /**
   *  Constructor.
   */
  private OperationControlManager() {
    logger.debug("OperationControlManager initialize member.");

    executeOperationHolder = new HashMap<EcSession, Operation>();
    ecMainState = ECMainState.Stop;
    ecMainObstruction = false;
    ifIntegrityExecution = false;
    configAuditExecution = false;
    resourceCheckExecution = false;
    lastOperationId = MIN_OPERATION_ID;
    queueMap = new HashMap<AbstractQueueEntryKey, LinkedList<OperationQueueEntry>>();
  }

  /**
   * Getting operation information.
   *
   * @param key
   *         key
   * @return extended information
   */
  public Object getOperationInfo(String key) {
    logger.trace(CommonDefinitions.START);
    logger.debug("key=" + key);
    Object ret = operationInfo.get(key);
    logger.debug("ret=" + ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Setting operation information.
   *
   * @param key
   *          key
   * @param value
   *          extended information
   */
  public void setOperationInfo(String key, Object value) {
    logger.trace(CommonDefinitions.START);
    logger.debug("key=" + key + " value=" + value);
    operationInfo.put(key, value);
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Deleting operation information..
   *
   * @param key
   *          key
   */
  public void clearOperationInfo(String key) {
    logger.trace(CommonDefinitions.START);
    if (operationInfo.containsKey(key)) {
      operationInfo.remove(key);
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Initializing operation management function.
   *
   * @throws DBAccessException
   *           DB Access Error
   */
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
    } else { 
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

  /**
   * Issueing Operation ID. Issuing operation ID to the new operation.
   *
   * @return operation ID
   */
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

  /**
   * Running Operation Registration. Registering the corresponding operation to the running operation list.
   *
   * @param opeId
   *          operation ID
   * @param ope
   *          operation instance
   */
  private void registerExecutingOperation(EcSession opeId, Operation ope) {

    logger.trace(CommonDefinitions.START);

    if (!executeOperationHolder.containsKey(opeId)) { 
      executeOperationHolder.put(opeId, ope);
    } else {
      logger.debug("This operation id was already registered. :" + ope);
      throw new IllegalArgumentException();
    }

    logger.trace(CommonDefinitions.END);

    return;
  }

  /**
   * Running Operation Deletion. Deleting the corresponding operation from the runnin operation list.
   *
   * @param opeId
   *          operation ID
   */
  protected void terminateOperation(EcSession opeId) {

    logger.trace(CommonDefinitions.START);

    if (executeOperationHolder.containsKey(opeId)) {
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

  /**
   * EC Status Change. Changing EC status.
   *
   * @param todb
   *          whether chage the DB value or not
   * @param state
   *          EC status
   * @return change success/fail
   * @throws DBAccessException
   *           DB access error
   */
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
    } else { 
      ecMainState = state;

      logger.trace(CommonDefinitions.END + ", return=true");

      return true;
    }
  }

  /**
   * EC Blockage Status Change. Changing EC blockage status.
   *
   * @param todb
   *          whether change the DB value or not
   * @param obstruction
   *          blockage status after change
   * @return change success/fail
   * @throws DBAccessException
   *           DB access error
   */
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
    } else { 
      ecMainObstruction = obstruction;

      logger.trace(CommonDefinitions.END + ", return=true");

      return true;
    }
  }

  /**
   * Execution Determination. Judge whether the operation could be executed or not.
   *
   * @param operationType
   *          operation type
   * @return executable yes/no
   */
  public boolean judgeExecution(int operationType) {

    logger.trace(CommonDefinitions.START);
    logger.debug("operationType=" + OperationType.name(operationType));

    try {
      ExpandOperationDetailInfo detailInfo = ExpandOperation.getInstance().get(OperationType.name(operationType));
      if (detailInfo != null) {
        if (detailInfo.getOperationPriority().equals(CommonDefinitions.EXPAND_CONF_PRIMARY)) {
          operationType = OperationType.__ExpandPrimaryOperation;
        } else {
          operationType = OperationType.__ExpandNormalOperation;
        }
      }

      if (operationType == OperationType.IFStateIntegrity) { 
        logger.debug("IF state integrity check.");
        return !(OperationControlManager.getInstance().isIfIntegrityExecution());
      } else if (operationType == OperationType.ConfigAuditNotification) { 
        logger.debug("Config Audit notification check.");
        return !(OperationControlManager.getInstance().isConfigAudit());
      } else if (operationType == OperationType.MonitoringResource) { 
        logger.debug("Monitoring Resource check.");
        return !(OperationControlManager.getInstance().isResourceCheckExecution());
      } else if (OperationControlManager.getInstance().getEcMainState(false) == ECMainState.Stop) { 

        logger.debug("EC main state is stop.");

        return false;
      } else {
        switch (operationType) {
          case ECMainStopper:
          case ECMainStateConfirm:
          case ObstructionStateController:
          case __ExpandPrimaryOperation:
            logger.debug("Priority operation");
            return true;

          case VlanIfChange:
          case AllL3VlanIfCreate:
          case AllL2VlanIfCreate:
          case AllL3VlanIfRemove:
          case AllL2VlanIfRemove:
          case AllL2VlanIfChange:
          case AllL3VlanIfChange:
          case SNMPTrapSignalRecieveNotification:
          case TrafficDataAcquisition:
          case DeviceInfoRegistration:
          case DeviceInfoAcquisition:
          case DeviceInfoRemove:
          case LeafAddition:
          case SpineAddition:
          case LeafRemove:
          case SpineRemove:
          case NodeRecover:
          case NodeAddedNotification:
          case PhysicalIfInfoAcquisition:
          case PhysicalIfInfoChange:
          case LagCreate:
          case LagInfoAcquisition:
          case LagRemove:
          case LagIfChange:
          case AllDeviceTypeInfoAcquisition:
          case AllIfInfoAcquisition:
          case AllPhysicalIfInfoAcquisition:
          case AllLagInfoAcquisition:
          case TrafficDataGathering:
          case IFStateIntegrity:
          case BLeafAddition:
          case BLeafRemove:
          case NodeInfoAcquisition:
          case NodeInfoRegistration:
          case AcceptNodeRecover:
          case NodeUpdate:
          case LeafChange:
          case BreakoutIfCreate:
          case BreakoutIfDelete:
          case BetweenClustersLinkCreate:
          case BetweenClustersLinkDelete:
          case VlanIfInfoAcquisition:
          case BreakoutIfInfoAcquisition:
          case IfBlockAndOpen:
          case AllVlanIfInfoAcquisition:
          case AllBreakoutIfInfoAcquisition:
          case AllDeviceInfoAcquisition:
          case ECMainLogAcquisition:
          case ControllerStateSendNotification:
          case ControllerStateSendNotificationLog:
          case ControllerStateSendNotificationServer:
          case TrafficDataAllAcquisition:

          case __ExpandNormalOperation:

            logger.debug("Normal operation");
            if ((OperationControlManager.getInstance().getEcMainState(false) == ECMainState.InService)
                && !(OperationControlManager.getInstance().getEcMainObstraction(false))) {
              return true;
            } else {
              logger.debug("Normal operation rejected.");
              return false;
            }
          default: 
            logger.debug("Not defined operation");
            return false;
        }
      }
    } catch (DBAccessException dbae) {
      logger.debug("Internal Error.", dbae);
      return false;
    }
  }

  /**
   * IF Status Integration Start-up Notification. Doing start-up process of IF status integration.
   *
   * @return Start OK/NG
   */
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

  /**
   * IF Status Intgration Termination. Doing termination process of IF status integration.
   */
  public void recieveEndIfIntegrity() {
    logger.trace(CommonDefinitions.START);

    ifIntegrityExecution = false;

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Getting IF integration execution status.
   *
   * @return IF integration execution status
   */
  public boolean isIfIntegrityExecution() {
    return ifIntegrityExecution;
  }

  /**
   * Operation Start. Processing for the operation start.
   *
   * @param operation
   *          operation to be started
   * @return operation ID
   */
  public EcSession startOperation(Operation operation) {

    EcSession oid = paidOutOperationId();

    if (oid != null) {
      registerExecutingOperation(oid, operation);

      try {
        if (isLockTarget(operation.getOperationType())) {
          int timeout = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.OPERATION_QUEUE_TIMEOUT);

          OperationLockKey key = new OperationLockKey(operation.getOperationType(), operation.getNodeId());
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
              return null; // T.O
            }

            synchronized (queueMap) {
              if (queue.getFirst() == entry) { 
                break;
              }
            }
            CommonUtil.sleep();
          }
        }

      } catch (IllegalArgumentException iae) {
        logger.debug("Operation ID was duplicated.", iae);
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

  /**
   * Determining the operation to be locked.
   *
   * @param operationType
   *          operation type
   * @return true: to be locked
   */
  private boolean isLockTarget(int operationType) {

    ExpandOperationDetailInfo detailInfo = ExpandOperation.getInstance().get(OperationType.name(operationType));
    if (detailInfo != null) {
      if (detailInfo.isLock()) {
        operationType = __ExpandNeedLockOperation;
      }
    }

    switch (operationType) {
      case VlanIfChange:
      case AllL3VlanIfCreate:
      case AllL2VlanIfCreate:
      case AllL3VlanIfRemove:
      case AllL2VlanIfRemove:
      case SNMPTrapSignalRecieveNotification:
      case DeviceInfoRegistration:
      case DeviceInfoRemove:
      case LeafAddition:
      case SpineAddition:
      case LeafRemove:
      case SpineRemove:
      case NodeAddedNotification:
      case AcceptNodeRecover:
      case NodeRecover:
      case NodeUpdate:
      case PhysicalIfInfoChange:
      case LagCreate:
      case LagRemove:
      case LagIfChange:
      case TrafficDataGathering:
      case IFStateIntegrity:
      case BLeafAddition:
      case BLeafRemove:
      case NodeInfoRegistration:
      case LeafChange:
      case BetweenClustersLinkCreate:
      case BetweenClustersLinkDelete:
      case BreakoutIfCreate:
      case BreakoutIfDelete:
      case IfBlockAndOpen:
      case __ExpandNeedLockOperation:
        return true;
      default:

        return false;
    }
  }

  /**
   * Confirmaing the Number of Running Operations. Confirming the number of running operations.
   *
   * @return the number of running operations
   */
  public int getNumberOfExecuteOperations() {
    return executeOperationHolder.size();
  }

  /**
   * EC Status acquisition. Getting EC status.
   *
   * @param fromdb
   *          whether the DB value is acquired or not
   * @return EC status
   * @throws DBAccessException
   *           DB access error
   */
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

  /**
   * EC Blockage Status Acquisition. Getting EC blockage status.
   *
   * @param fromdb
   *          whether the DB value is acquired or not
   * @return EC blockage status
   * @throws DBAccessException
   *           DB access error
   */
  public boolean getEcMainObstraction(boolean fromdb) throws DBAccessException {

    logger.trace(CommonDefinitions.START + ",fromdb=" + fromdb);

    if (fromdb) {
      logger.debug("Read db.");

      try (DBAccessManager session = new DBAccessManager()) {
        boolean ret = LogicalPhysicalConverter
            .toBooleanECObstructionState(session.getSystemStatus().getBlockade_status());

        logger.trace(CommonDefinitions.END + ", return=" + ret);
        return ret;

      }
    } else {
      logger.trace(CommonDefinitions.END + ", return=" + ecMainObstruction);

      return ecMainObstruction;
    }
  }

  /**
   * Operation Management Functional Part Start-up. Starting up the operation management functional part.
   *
   * @return Operation Management Class Instance
   */
  public static synchronized OperationControlManager boot() {

    logger.trace(CommonDefinitions.START);

    if (instance == null) {
      instance = new OperationControlManager();

      try {
        instance.initialize();
      } catch (DBAccessException dbae) {
        logger.debug("DB access error occured in OperationControlManager intializing.", dbae);
        instance = null;
      }

      logger.trace(CommonDefinitions.END);

      return instance;
    } else {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_402056));
      return null;
    }
  }

  /**
   * Returning the own instance.
   *
   * @return oeration management class instance
   */
  public static OperationControlManager getInstance() {
    return instance;
  }

  /**
   * Getting unsent device start-up notification sending flag.
   *
   * @return unsent device start-up notification sending flag
   */
  public boolean isUnsentNodeStateNotificationSendingState() {
    return unsentNodeStateNotificationSendingState;
  }

  /**
   * Setting unsent device start-up notification sending flag.
   *
   * @param unsentNodeStateNotificationSendingState
   *          unsent device start-up notification sending flag
   */
  protected void setUnsentNodeStateNotificationSendingState(boolean unsentNodeStateNotificationSendingState) {
    this.unsentNodeStateNotificationSendingState = unsentNodeStateNotificationSendingState;
  }

  /**
   * Getting device start-up notification management instance.
   *
   * @return device start-up notification management instance
   */
  protected NodeStateNotificationSender getNodeStateNotificationSenderHolder() {
    return nodeStateNotificationSenderHolder;
  }

  /**
   * Setting device start-up notification management instance.
   *
   * @param nodeStateNotificationSenderHolder
   *          device start-up notification management instance
   */
  protected void setNodeStateNotificationSenderHolder(NodeStateNotificationSender nodeStateNotificationSenderHolder) {
    this.nodeStateNotificationSenderHolder = nodeStateNotificationSenderHolder;
  }

  /**
   * Unsent Device Start-up Notification Sending. Sending unsent device start-up notification stored in DB to FC.
   */
  public void sendUnsentNodeStateNotification() {
    logger.trace(CommonDefinitions.START);

    List<NodesStartupNotification> list;

    try (DBAccessManager session = new DBAccessManager()) {

      list = session.getNodesStartupNotificationList();

      for (NodesStartupNotification nsn : list) {
        RestClient rc = new RestClient();
        HashMap<String, String> keyMap = new HashMap<String, String>();
        NotifyNodeStartUpToFc nnsu = new NotifyNodeStartUpToFc();

        keyMap.put(KEY_NODE_ID, nsn.getNode_id());

        if (nsn.getNotification_reception_status() == CommonDefinitions.RECV_OK_NOTIFICATION) {
          nnsu.setStatus(CommonDefinitions.RECV_OK_NOTIFICATION_STRING);
        } else if (nsn.getNotification_reception_status() == CommonDefinitions.RECV_NG_NOTIFICATION) {
          nnsu.setStatus(CommonDefinitions.RECV_NG_NOTIFICATION_STRING);
        } else {
          nnsu.setStatus(CommonDefinitions.WAIT_NOTIFICATION_STRING);
        }

        try {
          rc.request(RestClient.NOTIFY_NODE_ADDITION, keyMap, nnsu, CommonResponseFromFc.class);
        } catch (RestClientException rce) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_402048, "REST request"), rce);
        }

      }
    } catch (DBAccessException rce) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_402048, "DB access"), rce);
      list = null;
    }

    if (list != null) {
      try {
        DBAccessManager session2 = new DBAccessManager();

        session2.startTransaction();

        for (NodesStartupNotification nsn : list) {
          try {
            session2.deleteNodesRelation(nsn.getNode_id());
          } catch (DBAccessException dbae) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_402048, "DB access"), dbae);
          }
        }

        try {
          session2.commit();
        } catch (DBAccessException dbae) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_402048, "DB access"), dbae);
        }
        session2.close();
      } catch (DBAccessException dbae) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_402048, "DB access"), dbae);
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

  /**
   * Unsent Device Start-up Notification Sending Start. Start sending unsent device start-up notification.
   */
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
    return "OperationControlManager [executeOperationHolder=" + executeOperationHolder + ", ecMainState=" + ecMainState
        + ", ecMainObstruction=" + ecMainObstruction + ", ifIntegrityExecution=" + ifIntegrityExecution
        + ", lastOperationId=" + lastOperationId + ", queueMap=" + queueMap
        + ", unsentNodeStateNotificationSendingState=" + unsentNodeStateNotificationSendingState
        + ", nodeStateNotificationSenderHolder=" + nodeStateNotificationSenderHolder + ", nodeAdditionThread="
        + nodeAdditionThread + ", recoverExecutionFlag=" + recoverExecutionFlag + ", operationInfo=" + operationInfo
        + ", configAuditExecution=" + configAuditExecution + "]";
  }

  /**
   * Device Extention Status Update.
   *
   * @param nodeStatus
   *          device extention status
   * @param nodeId
   *          device ID
   * @return result
   */
  public boolean updateNodeAdditionState(NodeAdditionState nodeStatus, String nodeId) {
    try (DBAccessManager session = new DBAccessManager()) {
      session.startTransaction();
      session.updateNodeState(nodeId, nodeStatus.getValue());
      session.commit();
      return true;
    } catch (DBAccessException dbae) {
      logger.debug("updateNodeStatus error", dbae);
      return false;
    }
  }

  /**
   * Thread for Device Extention Acquisition.
   *
   * @return thread for device extention
   */
  public NodeAdditionThread getNodeAdditionInfo() {
    logger.trace(CommonDefinitions.START);
    NodeAdditionThread ret = this.nodeAdditionThread;
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Thread for Device Extention Clear.
   *
   */
  public void clearNodeAdditionInfo() {
    logger.trace(CommonDefinitions.START);
    this.nodeAdditionThread = null;
    logger.trace(CommonDefinitions.END);
    return;
  }

  /**
   * Thread for Device Extention Registration.
   *
   * @param nodeAdditionThread
   *          Thread for Device Extention
   */
  public void registerNodeAdditionInfo(NodeAdditionThread nodeAdditionThread) {
    logger.trace(CommonDefinitions.START);

    if (this.nodeAdditionThread != null) {
      logger.debug("already registered. overwrite. " + this.nodeAdditionThread);
    }
    this.nodeAdditionThread = nodeAdditionThread;
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Extende Device Information Rollback.
   *
   * @param nodeId
   *          device ID to be rolled back
   */
  public void rollbackAddedNodeInfo(String nodeId) {
    logger.trace(CommonDefinitions.START);
    logger.debug(nodeId);
    try (DBAccessManager session = new DBAccessManager()) {
      session.startTransaction();
      session.deleteNodesRelation(nodeId);
      session.commit();
    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_402086, nodeId));
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Additional service recovery executing flag Acquisition.
   *
   * @return recoverExecutionFlag
   */
  public boolean getRecoverExecution() {
    return recoverExecutionFlag;
  }

  /**
   * Setting Additional service recovery executing flag.
   *
   * @param recoverExecutionFlag
   *          set recoverExecutionFlag
   */
  public void setRecoverExecution(boolean recoverExecutionFlag) {
    this.recoverExecutionFlag = recoverExecutionFlag;
  }

  /**
   * Config-Audit start-up Notification.
   *
   * @return start-up pssibility
   */
  public boolean startConfigAudit() {

    logger.trace(CommonDefinitions.START);

    if (judgeExecution(OperationType.ConfigAuditNotification)) {
      configAuditExecution = true;

      logger.trace(CommonDefinitions.END + "result=true");
      return true;

    } else {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_404106));
      logger.trace(CommonDefinitions.END + "result=false");
      return false;
    }
  }

  /**
   * Config-Audit termination Notification.
   */
  public void endConfigAudit() {
    logger.trace(CommonDefinitions.START);

    configAuditExecution = false;

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Config-Audit start-up status Acquisition.
   *
   * @return Config-Audi  start-up status
   */
  public boolean isConfigAudit() {
    return configAuditExecution;
  }

  /**
   * Resource monitoring start-up Notification.
   *
   * @return start-up possiblity
   */
  public boolean startResourceCheck() {

    logger.trace(CommonDefinitions.START);

    if (judgeExecution(OperationType.MonitoringResource)) {
      resourceCheckExecution = true;

      logger.trace(CommonDefinitions.END + "result=true");
      return true;

    } else {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403120));
      logger.trace(CommonDefinitions.END + "result=false");
      return false;
    }
  }

  /**
   * Resource monitoring termination Notification
   */
  public void endResourceCheck() {
    logger.trace(CommonDefinitions.START);

    resourceCheckExecution = false;

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Resource monitoring start-up status Acquisition.
   *
   * @return  Resource monitoring start-up status
   */
  public boolean isResourceCheckExecution() {
    return resourceCheckExecution;
  }

}
