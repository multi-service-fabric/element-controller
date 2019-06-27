/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.notification;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.IfStatusUpdate;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.IfBlockAndOpenRequest;

/**
 * Opening and closing IF.
 */
public class IfBlockAndOpen extends Operation {

  /** In case input paramter check is NG(json is NG). */
  private static final String ERROR_CODE_570101 = "570101";

  /** In case target IF or target node does not exist. */
  private static final String ERROR_CODE_570201 = "570201";

  /** In case disconnection with EM has been detected or timeout occurred while requesting EM to open/close IF. */
  private static final String ERROR_CODE_570401 = "570401";

  /** In case failure occurred during DB access. */
  private static final String ERROR_CODE_570402 = "570402";

  /** In case error has been received from EM while requesting EM to open/close IF. */
  private static final String ERROR_CODE_570403 = "570403";

  /** In case DB commit failed after EM access is successful. */
  private static final String ERROR_CODE_900404 = "900404";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public IfBlockAndOpen(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.IfBlockAndOpen);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_570101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      IfBlockAndOpenRequest inputData = (IfBlockAndOpenRequest) getInData();

      Nodes nodesDb = session.searchNodes(getUriKeyMap().get(KEY_NODE_ID), null);
      if (nodesDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db. [Nodes]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_570201);
      }

      String ifStatus = inputData.getStatus();
      int ifStatusNum = 0;
      if (ifStatus.equals(IF_STATE_OK_STRING)) {
        ifStatusNum = IF_STATE_OK;
      } else {
        ifStatusNum = IF_STATE_NG;
      }

      PhysicalIfs physicalIfs = null;
      LagIfs lagIfs = null;
      BreakoutIfs breakoutIfs = null;

      String ifName = null;
      String ifType = getUriKeyMap().get(KEY_IF_TYPE);
      String ifId = getUriKeyMap().get(KEY_IF_ID);

      if (ifType.equals(IF_TYPE_PHYSICAL_IFS)) {
        for (PhysicalIfs physicalIf : nodesDb.getPhysicalIfsList()) {
          if (physicalIf.getPhysical_if_id().equals(ifId)) {
            ifName = physicalIf.getIf_name();
            physicalIf.setIf_status(ifStatusNum);
            physicalIfs = physicalIf;
            break;
          }
        }
      } else if (ifType.equals(IF_TYPE_LAG_IFS)) {
        for (LagIfs lagIf : nodesDb.getLagIfsList()) {
          if (lagIf.getFc_lag_if_id().equals(ifId)) {
            ifName = lagIf.getIf_name();
            lagIf.setIf_status(ifStatusNum);
            lagIfs = lagIf;
            break;
          }
        }
      } else if (ifType.equals(IF_TYPE_BREAKOUT_IFS)) {
        FOUNDIF: for (PhysicalIfs physicalIf : nodesDb.getPhysicalIfsList()) {
          for (BreakoutIfs breakoutIf : physicalIf.getBreakoutIfsList()) {
            if (breakoutIf.getBreakout_if_id().equals(ifId)) {
              ifName = breakoutIf.getIf_name();
              breakoutIf.setIf_status(ifStatusNum);
              breakoutIfs = breakoutIf;
              break FOUNDIF;
            }
          }
        }
      }
      if (ifName == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db. [IF Name]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_570201);
      }

      IfStatusUpdate ifStatusUpdateEm = EmMapper.toIfStatusUpdate(nodesDb.getNode_name(), ifName, ifType, ifStatus);

      AbstractMessage result = EmController.getInstance().request(ifStatusUpdateEm);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_570403);
      }

      session.startTransaction();

      session.updateNodeIfState(physicalIfs, lagIfs, null, breakoutIfs);

      session.commit();

      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      switch (dbae.getCode()) {
        case COMMIT_FAILURE:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900404);
          break;
        default:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_570402);
      }
    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_570401);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      IfBlockAndOpenRequest inputData = (IfBlockAndOpenRequest) getInData();

      inputData.check(new OperationType(OperationType.IfBlockAndOpen));

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    if (getUriKeyMap() == null) {
      result = false;
    } else {
      if (!(getUriKeyMap().containsKey(KEY_NODE_ID)) || getUriKeyMap().get(KEY_NODE_ID) == null) {
        result = false;
      } else if (!(getUriKeyMap().containsKey(KEY_IF_TYPE)) || getUriKeyMap().get(KEY_IF_TYPE) == null) {
        result = false;
      } else if (!getUriKeyMap().get(KEY_IF_TYPE).equals(IF_TYPE_PHYSICAL_IFS)
          && !getUriKeyMap().get(KEY_IF_TYPE).equals(IF_TYPE_LAG_IFS)
          && !getUriKeyMap().get(KEY_IF_TYPE).equals(IF_TYPE_BREAKOUT_IFS)) {
        result = false;
      } else if (!(getUriKeyMap().containsKey(KEY_IF_ID)) || getUriKeyMap().get(KEY_IF_ID) == null) {
        result = false;
      }
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }
}
