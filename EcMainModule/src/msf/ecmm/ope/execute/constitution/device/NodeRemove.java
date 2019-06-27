/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
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
import msf.ecmm.emctrl.pojo.InternalLinkAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.DeleteNode;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodesDeleteNode;

/**
 * Device Removal/Change Base.
 */
public abstract class NodeRemove extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_080101 = "080101";
  /** In case the number of pieces of information for Leaf device extention/removal is zero (Data acquisition from DBs is failed in the previous step of process). */
  private static final String ERROR_CODE_080102 = "080102";
  /** Detected inconsistency between input data and DB data. */
  private static final String ERROR_CODE_800103 = "800103";
  /** In case the subject to be deleted does not exist. */
  private static final String ERROR_CODE_080201 = "080201";
  /** In case CP requiring LAG has not been deleted (80: rollback at FC). */
  private static final String ERROR_CODE_800303 = "800303";
  /** In the case where it is additionally setting to the internal link IF of the target device to be reduced (or the opposing device) and can not be deleted (80: rollback at FC). */
  private static final String ERROR_CODE_800305 = "800305";
  /** DHCP termination failed while in removing (80: rollback at FC). */
  private static final String ERROR_CODE_800401 = "800401";
  /** Syslog monitoring termination failed while in removing (80: rollback at FC). */
  private static final String ERROR_CODE_800402 = "800402";
  /** Disconnection or connection timeout to EM has occurred in Leaf removal request to EM (80: rollback at FC). */
  private static final String ERROR_CODE_800403 = "800403";
  /** Error has occurred in EM at Leaf extention/removal request to EM (error response received). */
  private static final String ERROR_CODE_080404 = "080404";
  /** Disconnection or connection timeout to EM has occurred while in requesting LAG deletion for internal link to EM after the completion of Leaf removal to EM (80: rollback at FC). */
  private static final String ERROR_CODE_800405 = "800405";
  /** Error has occurred from EM in requesting LAG addition/deletion for internal link to EM after the completion of Leaf extention/removal to EM (error response received). */
  private static final String ERROR_CODE_080406 = "080406";
  /** In case error has occurred in DB access (80: rollback at FC). */
  private static final String ERROR_CODE_800408 = "800408";
  /** EM access manual lock acquisition failed  (80: rollback at FC). */
  private static final String ERROR_CODE_800409 = "800409";
  /** In case DB commitment failed after successful EM access (90: Notify EMEC status unmatch to FC). */
  private static final String ERROR_CODE_900410 = "900410";

  /**
   * Having exceptionally in this class for the process to continue processing when removal response EM error response has been received.
   */
  private AbstractResponseMessage response = null;

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public NodeRemove(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080101);
    }

    DeleteNode deleteNodeRest = (DeleteNode) getInData();

    String nodeId = deleteNodeRest.getDeleteNodes().getNodeId();

    boolean isOppositeNodesInfoFlag = isOppositeNodesInfo();

    boolean dhcpOkFlag = false;
    boolean emLockOkFlag = false;
    boolean em1FinFlag = false;
    boolean needCleanUpFlag = true; 

    try (DBAccessManager session = new DBAccessManager()) {

      DhcpController dhcpController = DhcpController.getInstance();
      dhcpController.stop(false);
      dhcpOkFlag = true;

      SyslogController syslogController = SyslogController.getInstance();
      syslogController.monitorStop(false);

      needCleanUpFlag = false;

      Nodes nodesDb = session.searchNodes(nodeId, null);
      if (nodesDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_080201);
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
      oppoNodeList.toString(); 

      if (!checkExpand(nodesDb)) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "expand function check NG."));
        return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_800305);
      }

      session.startTransaction();

      session.deleteNodesRelation(nodeId);

      List<Nodes> oppoNodeListFromRestInput = DbMapper.toNodeOppositeNodesReduced(deleteNodeRest, oppoNodeList);
      boolean isOppositeEmExecuteFlag = deleteOppositeNodes(session, oppoNodeListFromRestInput);

      boolean doNotNotifyEm = false;
      if (session.checkClusterType() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
        if (session.getNodesNum() > 0) {
          doNotNotifyEm = true; 
        }
      }

      AbstractMessage lockKey = new AbstractMessage();
      RequestQueueEntry entry = null;
      try {
        entry = EmController.getInstance().lock(lockKey);
        emLockOkFlag = true;

        if (doNotNotifyEm == false) {
          if (!executeDeleteNode(nodesDb)) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed. [Node]"));
            response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080404);
          }
          em1FinFlag = true;
        }

        if (isOppositeNodesInfoFlag && isOppositeEmExecuteFlag) {
          if (!executeDeleteInternalLink(oppoNodeList)) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed. [internalLink]"));
            response = makeFailedResponseWithoutOverwrite(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080406);
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

    } catch (DevctrlException de) {
      if (dhcpOkFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "DHCP stop was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_800401);
      } else {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Watch syslog stop was failed."), de);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_800402);
      }
    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      if (dbae.getCode() == DBAccessException.RELATIONSHIP_UNCONFORMITY) {
        response = makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_800303);
      } else if (dbae.getCode() == DBAccessException.NO_DELETE_TARGET) {
        response = makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_080201);
      } else if (dbae.getCode() == DBAccessException.COMMIT_FAILURE && response == null) {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900410);
      } else {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_800408);
      }
    } catch (IllegalArgumentException iae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."), iae);
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_800103);
    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      if (emLockOkFlag == false) {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_800409);
      } else if (em1FinFlag == false) {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_800403);
      } else {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_800405);
      }
    } finally {
      if (needCleanUpFlag == true) {
        DevctrlCommon.cleanUp();
      }
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Error Response Generation (without overwriting).
   *
   * @param rescode
   *          response code
   * @param errcode
   *          error code
   * @return error response generated
   */
  protected CommonResponse makeFailedResponseWithoutOverwrite(int rescode, String errcode) {
    if (response == null) {
      response = super.makeFailedResponse(rescode, errcode);
    }
    return (CommonResponse) response; 
  }

  /**
   * Input Check.
   *
   * @return check result
   */
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    DeleteNode deleteNodeRest = (DeleteNode) getInData();

    try {
      deleteNodeRest.check(new OperationType(OperationType.LeafRemove));
    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      checkResult = false;
    }

    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

  /**
   * Opposing Device ID List Acquisition.
   *
   * @return Opposing Device ID list
   */
  protected ArrayList<String> getOppositeNodeIdList() {
    logger.trace(CommonDefinitions.START);
    DeleteNode deleteNodeRest = (DeleteNode) getInData();
    ArrayList<String> oppositeNodesIdList = new ArrayList<String>();
    ArrayList<OppositeNodesDeleteNode> oppositeNodesDeleteNodeList = deleteNodeRest.getDeleteNodes().getOppositeNodes();
    for (OppositeNodesDeleteNode oppositeNodesDeleteNode : oppositeNodesDeleteNodeList) {
      oppositeNodesIdList.add(oppositeNodesDeleteNode.getNodeId());
    }
    logger.trace(CommonDefinitions.END);
    return oppositeNodesIdList;
  }

  /**
   * EM Access (Device Removal) Execution.
   *
   * @param targetNodeDb
   *          device information to be deleted (DB)
   * @return executability
   * @throws EmctrlException
   *           : EM exception
   * @throws IllegalArgumentException
   *           : mapper exception
   */
  protected abstract boolean executeDeleteNode(Nodes targetNodeDb) throws EmctrlException, IllegalArgumentException;

  /**
   * EM Access (Internal Link Deletion) Execution.
   *
   * @param oppoNodeList
   *          opposing device information list
   * @return executability
   * @throws EmctrlException
   *           EM exception
   * @throws IllegalArgumentException
   *           mapper exception
   */
  protected boolean executeDeleteInternalLink(ArrayList<Nodes> oppoNodeList)
      throws EmctrlException, IllegalArgumentException {
    logger.trace(CommonDefinitions.START);

    InternalLinkAddDelete internalLinkDelEm = EmMapper.toInternalLinkDelete((DeleteNode) getInData(), oppoNodeList);

    EmController emController = EmController.getInstance();
    AbstractMessage ret = emController.request(internalLinkDelEm, false);

    logger.trace(CommonDefinitions.END);
    return ret.isResult();

  }

  /**
   * Opposing Device Existence Acquisition.
   *
   * @return opposing device existence
   */
  protected boolean isOppositeNodesInfo() {
    return !((DeleteNode) getInData()).getDeleteNodes().getOppositeNodes().isEmpty();
  }

  protected boolean deleteOppositeNodes(DBAccessManager session, List<Nodes> nodesList) throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {
      session.deleteUpdateOppositeNodes(nodesList);
    } catch (DBAccessException dbae) {
      if (dbae.getCode() == DBAccessException.NO_DELETE_TARGET) {
        result = false;
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_409073));
      } else {
        throw dbae;
      }
    }

    logger.trace(CommonDefinitions.END);
    return result;
  }

  /**
   * Implement any check if required in extension function.
   *
   * @param nodes
   *          Removal target device information
   * @return Check result
   * @throws In case abnormality occurred in DBAccessException DB
   */
  protected boolean checkExpand(Nodes nodes) throws DBAccessException {
    return true;
  }

}
