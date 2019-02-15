/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.RequestQueueEntry;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.RecoverUpdateNode;
import msf.ecmm.emctrl.pojo.RecoverUpdateService;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.RecoverNodeService;

/**
 * Additional service recovery.
 */
public class NodeRecover extends Operation {

  /** In case of invalid data. */
  private static final String ERROR_CODE_080101 = "080101";
  /** In case DB acquisition of necessary infromation is failed. */
  private static final String ERROR_CODE_080102 = "080102";
  /** In case the information to be stored in DB already exists. */
  private static final String ERROR_CODE_080301 = "080301";
  /** EM request "recover node" disconnection or connection timeout has occurred. */
  private static final String ERROR_CODE_080403 = "080403";
  /** EM request "recover node" error response has been received. */
  private static final String ERROR_CODE_080404 = "080404";
  /** EM request "recover service" disconnection or connection timeout has occurred. */
  private static final String ERROR_CODE_080405 = "080405";
  /** EM request "recover service" error response has been received. */
  private static final String ERROR_CODE_080406 = "080406";
  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_080408 = "080408";
  /** EM access manual lock acquisition failed (time out). */
  private static final String ERROR_CODE_080409 = "080409";
  /** In case DB commitment failed after EM access succeeded (90: Notify EMEC status unmatch to FC). */
  private static final String ERROR_CODE_900410 = "900410";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public NodeRecover(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.NodeRecover);
  }

  /**
   * Recovery extension execute.
   *
   * @see msf.ecmm.ope.execute.Operation#execute()
   */
  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    RecoverNodeService inputData = (RecoverNodeService) getInData();

    nodeId = getUriKeyMap().get(KEY_NODE_ID);

    boolean emLockOkFlag = false;
    boolean em1OkFlag = false;

    try (DBAccessManager session = new DBAccessManager()) {

      Equipments equipments = session.searchEquipments(inputData.getEquipment().getEquipmentTypeId());
      if (equipments == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Equipments]"));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080102);
      }

      Nodes inputNodesDb = null;
      inputNodesDb = session.searchNodes(nodeId, null);
      if (inputNodesDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080102);
      }

      session.startTransaction();

      List<VlanIfs> inputVlanIfsDb = null;
      List<PhysicalIfs> outAddPhysicalIfsList = new ArrayList<PhysicalIfs>();
      List<PhysicalIfs> outDelPhysicalIfsList = new ArrayList<PhysicalIfs>();
      Map<String, String> outPhysicalIfNamesMap = new HashMap<String, String>();
      Map<String, String> outLagIfNamesMap = new HashMap<String, String>();
      inputVlanIfsDb = session.getVlanIfsList(nodeId);
      List<VlanIfs> outVlanIfsList = new ArrayList<VlanIfs>();
      Nodes outNodesDb = DbMapper.toRecoverUpdate(inputData, inputNodesDb, inputVlanIfsDb, equipments,
          outAddPhysicalIfsList, outDelPhysicalIfsList, outPhysicalIfNamesMap, outLagIfNamesMap, outVlanIfsList);
      session.updateForRecover(outNodesDb, outAddPhysicalIfsList, outDelPhysicalIfsList, outVlanIfsList);

      boolean notifyEmFlag = true;
      if (equipments.getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
        notifyEmFlag = false;
      }

      AbstractMessage lockKey = new AbstractMessage();
      RequestQueueEntry entry = null;
      try {
        entry = EmController.getInstance().lock(lockKey);
        emLockOkFlag = true;

        if (notifyEmFlag) {
          if (!executeRecoverNode(inputData, outNodesDb, outPhysicalIfNamesMap, outLagIfNamesMap)) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed. [RecoverNode]"));
            return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080404);
          }
          em1OkFlag = true;
        }

        if (!executeRecoverService(inputData, inputNodesDb.getEquipments(), outNodesDb, outPhysicalIfNamesMap,
            outLagIfNamesMap)) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed. [internalLink]"));
          return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080406);
        }

      } finally {
        if (entry != null) {
          EmController.getInstance().unlock(entry);
        }
      }

      session.commit();

      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      if (dbae.getCode() == DBAccessException.DOUBLE_REGISTRATION) {
        response = makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_080301);
      } else if (dbae.getCode() == DBAccessException.COMMIT_FAILURE) {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900410);
      } else {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080408);
      }
    } catch (IllegalArgumentException iae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."), iae);
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080101);
    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      if (emLockOkFlag == false) {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080409);
      } else if (em1OkFlag == false) {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080403);
      } else {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080405);
      }
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * EM Access (recover node) Execution.
   *
   * @param input
   *          input information
   * @param nodes
   *          device information
   * @param physicalIfNamesMap
   *          Conversion Table from Recovery Physical IF Name to Before Recovery Physical IF Name
   * @param lagIfNamesMap
   *          Conversion Table from Recovery LAG IF Name to Before Recovery LAG IF Name
   * @return success/fail
   * @throws EmctrlException
   *           :EM exception
   * @throws IllegalArgumentException
   *           :mapper exception
   */
  private boolean executeRecoverNode(RecoverNodeService input, Nodes nodes, Map<String, String> physicalIfNamesMap,
      Map<String, String> lagIfNamesMap) throws EmctrlException, IllegalArgumentException {
    logger.trace(CommonDefinitions.START);

    RecoverUpdateNode recoverUpdateNode = EmMapper.toRecoverUpdateNode(input, nodes, physicalIfNamesMap, lagIfNamesMap);

    EmController emController = EmController.getInstance();
    AbstractMessage ret = emController.request(recoverUpdateNode, false);

    logger.trace(CommonDefinitions.END);
    return ret.isResult();
  }

  /**
   * EM Access (service reconfiguration) Execution.
   *
   * @param input
   *          input information
   * @param oldEquipments
   *          model information before recovery
   * @param nodes
   *          device information
   * @param physicalIfNamesMap
   *          Conversion Table from Recovery Physical IF Name to Before Recovery Physical IF Name
   * @param lagIfNamesMap
   *          Conversion Table from Recovery LAG IF Name to Before Recovery LAG IF Name
   * @return success/fail
   * @throws EmctrlException
   *           :EM exception
   */
  private boolean executeRecoverService(RecoverNodeService input, Equipments oldEquipments, Nodes nodes,
      Map<String, String> physicalIfNamesMap, Map<String, String> lagIfNamesMap) throws EmctrlException {
    logger.trace(CommonDefinitions.START);

    RecoverUpdateService recoverUpdateService = EmMapper.toRecoverUpdateService(input, oldEquipments, nodes,
        physicalIfNamesMap, lagIfNamesMap);

    EmController emController = EmController.getInstance();
    AbstractMessage ret = emController.request(recoverUpdateService, false);

    logger.trace(CommonDefinitions.END);
    return ret.isResult();
  }

  @Override
  protected boolean checkInData() {
    logger.debug("Never called.");
    return false;
  }

}
