/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.RequestQueueEntry;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.InternalLinkAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.AddNode;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodesInterface;

/**
 * Device Extention/Change Base.
 */
public abstract class NodeAddition extends Operation {

  /** In case of invalid data. */
  private static final String ERROR_CODE_080101 = "080101";
  /** In case DB acquisition of necessary infromation is failed. */
  private static final String ERROR_CODE_080102 = "080102";
  /** In case the information to be stored in DB already exists. */
  private static final String ERROR_CODE_080301 = "080301";
  /** EM request "device extention" disconnection or connection timeout has occurred. */
  private static final String ERROR_CODE_080403 = "080403";
  /** EM request "device extention" error response has been received. */
  private static final String ERROR_CODE_080404 = "080404";
  /** EM request "internal link addition" disconnection or connection timeout has occurred. */
  private static final String ERROR_CODE_080405 = "080405";
  /** EM request "internal link addition" error response has been received. */
  private static final String ERROR_CODE_080406 = "080406";
  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_080408 = "080408";
  /** EM access manual lock acquisition failed (time out). */
  private static final String ERROR_CODE_080409 = "080409";
  /** In case DB commitment failed after EM access succeeded (90: Notify EMEC status unmatch to FC). */
  private static final String ERROR_CODE_900410 = "900410";

  /** Acquiring Successful Extention, configured device registration flag. */
  protected boolean normanRegistrationFlag = true;

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public NodeAddition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    AddNode addNode = (AddNode) getInData();

    String nodeId = addNode.getCreateNode().getNodeId();

    normanRegistrationFlag = addNode.getCreateNode().getProvisioning();

    boolean isOppositeNodesInfoFlag = !addNode.getCreateNode().getOppositeNodes().isEmpty();

    boolean emLockOkFlag = false;
    boolean em1OkFlag = false;

    try (DBAccessManager session = new DBAccessManager()) {

      Equipments equipments = session.searchEquipments(addNode.getEquipment().getEquipmentTypeId());
      if (equipments == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Equipments]"));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080102);
      }

      Nodes nodesFromDb = null;
      nodesFromDb = session.searchNodes(nodeId, null);
      if (nodesFromDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080102);
      }

      ArrayList<Nodes> oppoNodeList = new ArrayList<Nodes>();
      ArrayList<String> oppoIdList = getOppositeNodeIdList();
      for (String oppoId : oppoIdList) {
        Nodes oppoNodesDb = session.searchNodes(oppoId, null);
        if (oppoNodesDb == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [OppoNodes]"));
          return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080102);
        }
        oppoNodeList.add(oppoNodesDb);
      }

      List<Nodes> nodesListDbMapper = DbMapper.toAddNodeRelation(addNode, equipments, nodesFromDb, oppoNodeList);

      session.startTransaction();

      for (Nodes nodeDb : nodesListDbMapper) {
        ArrayList<PhysicalIfs> physiIfList = new ArrayList<PhysicalIfs>(nodeDb.getPhysicalIfsList());
        ArrayList<LagIfs> lagIfList = new ArrayList<LagIfs>(nodeDb.getLagIfsList());
        session.addNodesRelation(physiIfList, lagIfList, null);
      }

      boolean doNotNotifyEm = false;
      if (session.checkClusterType() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
        if (session.getNodesNum() > 1) {
          doNotNotifyEm = true;
        }
      }

      AbstractMessage lockKey = new AbstractMessage();
      RequestQueueEntry entry = null;
      try {
        entry = EmController.getInstance().lock(lockKey);
        emLockOkFlag = true;

        if (doNotNotifyEm == false) {
          String ecIpaddr = EcConfiguration.getInstance().get(String.class, EcConfiguration.DEVICE_EC_MANAGEMENT_IF);
          if (!executeAddNode(ecIpaddr, nodesListDbMapper)) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed. [Node]"));
            return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080404);
          }
          em1OkFlag = true;
        }

        if (isOppositeNodesInfoFlag) {
          if (!executeAddInternalLink(nodesListDbMapper)) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed. [internalLink]"));
            return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080406);
          }
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
      } else if (dbae.getCode() == DBAccessException.COMMIT_FAILURE && response == null) {
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
   * Opposing Device ID List Acquisition.
   *
   * @return opposing device ID list
   */
  private ArrayList<String> getOppositeNodeIdList() {
    logger.trace(CommonDefinitions.START);
    AddNode addNodeRest = (AddNode) getInData();
    ArrayList<String> oppositeNodeIdList = new ArrayList<String>();
    for (OppositeNodesInterface oppoIf : addNodeRest.getCreateNode().getOppositeNodes()) {
      oppositeNodeIdList.add(oppoIf.getNodeId());
    }
    logger.debug(oppositeNodeIdList);
    logger.trace(CommonDefinitions.END);
    return oppositeNodeIdList;
  }

  /**
   * EM Access (Device Extention) Execution.
   *
   * @param nodes
   *          device information
   * @param ecmainIpaddr
   *          IP address of EC
   * @param oppoNodeList
   *          opposing device information list (DB)
   * @return success/fail
   * @throws EmctrlException
   *           :EM exception
   * @throws IllegalArgumentException
   *           :mapper exception
   */
  protected abstract boolean executeAddNode(String ecmainIpaddr, List<Nodes> nodesListDbMapper)
      throws EmctrlException, IllegalArgumentException;

  /**
   * EM Access (Internal Link Addition) Execution.
   *
   * @param oppoNodeList
   *          opposing device list
   * @return success/fail
   * @throws EmctrlException
   *           :EM exception
   */
  private boolean executeAddInternalLink(List<Nodes> nodesListDbMapper) throws EmctrlException {
    logger.trace(CommonDefinitions.START);

    InternalLinkAddDelete internalLinkAddEm = EmMapper.toInternalLinkCreate((AddNode) getInData(), nodesListDbMapper);

    EmController emController = EmController.getInstance();
    AbstractMessage ret = emController.request(internalLinkAddEm, false);

    logger.trace(CommonDefinitions.END);
    return ret.isResult();
  }

  @Override
  protected boolean checkInData() {
    logger.debug("Never called.");
    return false;
  }

}
