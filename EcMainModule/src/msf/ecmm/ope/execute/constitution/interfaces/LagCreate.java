/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.convert.LogicalPhysicalConverter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.LagMembers;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.CeLagAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.CreateLagInterface;
import msf.ecmm.ope.receiver.pojo.parts.PhysicalIfsCreateLagIf;

/**
 * LagIF Generation Class Definition<br>
 * Generating LagIF.
 */
public class LagCreate extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_230101 = "230101";

  /** In case the number of pieces of information for LagIF generation is zero (data acquisition from DBs failed in the previous step process). */
  private static final String ERROR_CODE_230102 = "230102";

  /** In case the information to be registered already exists. */
  private static final String ERROR_CODE_230201 = "230201";

  /** Disconnection or connection timeout with EM has occurred while requesting IF generation to EM. */
  private static final String ERROR_CODE_230302 = "230302";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_230303 = "230303";

  /** Error has occurred from EM while requesing LagIF generation to EM (error response received). */
  private static final String ERROR_CODE_230304 = "230304";

  /** In case DB commitment failed after successful EM access. */
  private static final String ERROR_CODE_900305 = "900305";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public LagCreate(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.LagCreate);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_230101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      CreateLagInterface inputData = (CreateLagInterface) getInData();

      Nodes nodesDb = session.searchNodes(getUriKeyMap().get(KEY_NODE_ID), null);

      if ((nodesDb == null) || (nodesDb.getPhysicalIfsList() == null)) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db."));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_230102);
      }

      if (nodesDb.getLagIfsList() != null) {
        for (LagIfs lagIfs : nodesDb.getLagIfsList()) {
          if (lagIfs.getLagMembersList() != null) {
            for (LagMembers members : lagIfs.getLagMembersList()) {
              for (PhysicalIfsCreateLagIf phys : inputData.getLagIf().getPhysicalIfs()) {
                if (phys.getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
                  if ((members.getPhysical_if_id() != null) && members.getPhysical_if_id().equals(phys.getIfId())) {
                    logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "already registered as a LagMember."));
                    return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_230201);
                  }
                } else if (phys.getIfType().equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
                  if ((members.getBreakout_if_id() != null) && members.getBreakout_if_id().equals(phys.getIfId())) {
                    logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "already registered as a LagMember."));
                    return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_230201);
                  }
                }
              }
            }
          }
        }
      }

      String lagIfId = null;
      if (nodesDb.getEquipments().getRouter_type() == 0) {
        lagIfId = LogicalPhysicalConverter.getLagIfId(nodesDb);
      } else {
        lagIfId = inputData.getLagIf().getLagIfId();
      }

      LagIfs lagIfsDb = DbMapper.toLagIfCreate(inputData, nodesDb, lagIfId);

      session.startTransaction();

      session.addLagIfs(lagIfsDb);

      CeLagAddDelete ceLagAddDeleteEm = EmMapper.toLagIfCreate(inputData, nodesDb, lagIfId);

      AbstractMessage result = EmController.getInstance().request(ceLagAddDeleteEm);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_230304);
      }

      session.commit();

      response = makeSuccessResponse(RESP_CREATED_201, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      switch (dbae.getCode()) {
        case DOUBLE_REGISTRATION:
          response = makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_230201);
          break;
        case COMMIT_FAILURE:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900305);
          break;
        default:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_230303);
          break;
      }
    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_230302);

    } catch (IllegalArgumentException iae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), iae);
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_230101);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      CreateLagInterface inputData = (CreateLagInterface) getInData();

      inputData.check(new OperationType(OperationType.LagCreate));

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    if (getUriKeyMap() == null) {
      result = false;
    } else {
      if (!(getUriKeyMap().containsKey(KEY_NODE_ID)) || getUriKeyMap().get(KEY_NODE_ID) == null) {
        result = false;
      }
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

}
