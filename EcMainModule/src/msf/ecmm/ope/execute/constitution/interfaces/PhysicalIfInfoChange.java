/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.UpdatePhysicalInterface;

/**
 * Physical IF Information Change.
 */
public class PhysicalIfInfoChange extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_200101 = "200101";

  /** In case the number of pieces of information for physical IF information change is zero. */
  private static final String ERROR_CODE_200102 = "200102";

  /** In case the information to be changed does not exist. */
  private static final String ERROR_CODE_200201 = "200201";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_200401 = "200401";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public PhysicalIfInfoChange(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.PhysicalIfInfoChange);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_200101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      UpdatePhysicalInterface inputData = (UpdatePhysicalInterface) getInData();
      String nodeId = getUriKeyMap().get(KEY_NODE_ID);
      String physicalIfId = getUriKeyMap().get(KEY_PHYSICAL_IF_ID);

      PhysicalIfs physicalIfs = session.searchPhysicalIfs(nodeId, physicalIfId);
      if (physicalIfs == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [PhysicalIfs]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_200201);
      }

      session.startTransaction();

      if (inputData.getAction().equals("speed_set")) {
        Nodes nodesDb = session.searchNodes(nodeId, null);
        if (nodesDb == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db."));
          return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_200102);
        }

        physicalIfs = DbMapper.toPhyIfChange(inputData, nodesDb, physicalIfId);
      } else {
        physicalIfs.setIf_name(null);
        physicalIfs.setIf_speed(null);
      }

      session.updatePhysicalIfs(physicalIfs);

      session.commit();
      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_200401);
    } catch (IllegalArgumentException iae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), iae);
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_200101);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      UpdatePhysicalInterface inputData = (UpdatePhysicalInterface) getInData();

      inputData.check(OperationType.PhysicalIfInfoChange);

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
      if (!(getUriKeyMap().containsKey(KEY_PHYSICAL_IF_ID)) || getUriKeyMap().get(KEY_PHYSICAL_IF_ID) == null) {
        result = false;
      }
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

}
