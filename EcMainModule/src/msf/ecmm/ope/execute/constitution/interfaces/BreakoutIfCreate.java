/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
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
import msf.ecmm.ope.receiver.pojo.CreateBreakoutIf;

/**
 * breakoutIF Addition Class Definition<br>
 * Adding breakoutIF.
 */
public class BreakoutIfCreate extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_010101 = "010101";

  /** In case the number of pieces of information of process execution is zero. */
  private static final String ERROR_CODE_010102 = "010102";

  /** In case the information to be registered already exists. */
  private static final String ERROR_CODE_010303 = "010303";

  /** Disconnection or connection timeout with EM has occurred while requesting breakoutIF addition to EM. */
  private static final String ERROR_CODE_010401 = "010401";

  /** Error has occurred from EM while requesting breakoutIF addition to EM (error response received). */
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
  public BreakoutIfCreate(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.BreakoutIfCreate);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      CreateBreakoutIf inputData = (CreateBreakoutIf) getInData();

      Nodes nodesDb = session.searchNodes(inputData.getNodeId(), null);
      if ((nodesDb == null) || (nodesDb.getPhysicalIfsList() == null)) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db."));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010102);
      }

      for (PhysicalIfs physicalIfs : nodesDb.getPhysicalIfsList()) {
        if (physicalIfs.getPhysical_if_id().equals(inputData.getBasePhysicalIfId())
            && (physicalIfs.getBreakoutIfsList() != null) && !physicalIfs.getBreakoutIfsList().isEmpty()) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "already breakouted IF."));
          return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_010303);
        }
      }

      List<BreakoutIfs> breakoutIfsDbList = DbMapper.tobreakoutIfCreate(inputData, nodesDb);

      session.startTransaction();

      for (BreakoutIfs breakoutIfsDb : breakoutIfsDbList) {
        session.addBreakoutIfs(breakoutIfsDb);
      }

      BreakoutIfAddDelete breakoutIfAddDeleteEm = EmMapper.toBreakoutIfCreate(inputData, nodesDb);

      AbstractMessage result = EmController.getInstance().request(breakoutIfAddDeleteEm);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010402);
      }

      session.commit();

      response = makeSuccessResponse(RESP_CREATED_201, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      switch (dbae.getCode()) {
        case DOUBLE_REGISTRATION:
          response = makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_010303);
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

      CreateBreakoutIf inputData = (CreateBreakoutIf) getInData();

      inputData.check(new OperationType(OperationType.BreakoutIfCreate));

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

}
