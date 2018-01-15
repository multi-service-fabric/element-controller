/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.LagMembers;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.BreakoutIfAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.DeleteBreakoutIf;

/**
 * breakoutIF Deletion Class Definition<br>
 * Deleting breakoutIF.
 */
public class BreakoutIfDelete extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_010101 = "010101";

  /** In case the number of pieces of information of breakoutIF deletion is zero. */
  private static final String ERROR_CODE_010102 = "010102";

  /** In case there is no information to be deleted. */
  private static final String ERROR_CODE_010202 = "010202";

  /** In case the CP in which breakoutIF is to be deleted has not been deleted. */
  private static final String ERROR_CODE_010304 = "010304";

  /** Desconnection or connection timeout with EM has occurred while requesting breakoutIF deletion to EM. */
  private static final String ERROR_CODE_010401 = "010401";

  /** Error has occurred while requesting breakoutIF deletion to EM (error response received). */
  private static final String ERROR_CODE_010402 = "010402";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_010403 = "010403";

  /** In case DB commitment failed after successful EM access. */
  private static final String ERROR_CODE_900404 = "900404";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public BreakoutIfDelete(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.BreakoutIfDelete);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    DeleteBreakoutIf inputData = (DeleteBreakoutIf) getInData();
    String nodeId = inputData.getNodeId();
    List<String> breakoutIfIds = inputData.getBreakoutIfId();

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      List<BreakoutIfs> breakoutIfsDbList = session.getBreakoutIfsList(nodeId);

      if (breakoutIfsDbList.isEmpty()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [BreakoutIfs]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_010202);
      }

      for (VlanIfs vlanIfs : session.getVlanIfsList(nodeId)) {
        for (String boIfId : breakoutIfIds) {
          if ((vlanIfs.getBreakout_if_id() != null) && vlanIfs.getBreakout_if_id().equals(boIfId)) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "VLANIF data is found."));
            return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_010304);
          }
        }
      }

      for (LagIfs lagIfs : session.getLagIfsList(nodeId)) {
        if (lagIfs.getLagMembersList() != null) {
          for (LagMembers lagmembers : lagIfs.getLagMembersList()) {
            for (String boIfId : breakoutIfIds) {
              if ((lagmembers.getBreakout_if_id() != null) && lagmembers.getBreakout_if_id().equals(boIfId)) {
                logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "breakoutIF data is found."));
                return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_010304);
              }
            }
          }
        }
      }

      Nodes nodesDb = session.searchNodes(nodeId, null);
      if (nodesDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010102);
      }

      BreakoutIfAddDelete breakoutIfAddDeleteEm = EmMapper.toBreakoutIfDelete(inputData, breakoutIfsDbList, nodesDb);

      session.startTransaction();

      for (String ifId : inputData.getBreakoutIfId()) {
        session.deletebreakoutIfs(nodeId, ifId);
      }

      AbstractMessage result = EmController.getInstance().request(breakoutIfAddDeleteEm);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010402);
      }

      session.commit();

      response = makeSuccessResponse(RESP_NOCONTENTS_204, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      switch (dbae.getCode()) {
        case NO_DELETE_TARGET:
          response = makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_010202);
          break;
        case COMMIT_FAILURE:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900404);
          break;
        default:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010403);
          break;
      }
    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010401);
    } catch (IllegalArgumentException iae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), iae);
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      DeleteBreakoutIf inputData = (DeleteBreakoutIf) getInData();

      inputData.check(OperationType.BreakoutIfDelete);

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

}
