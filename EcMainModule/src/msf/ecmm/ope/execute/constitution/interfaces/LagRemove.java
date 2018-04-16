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
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.CeLagAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

/**
 * LagIF Deletion Class Definition<br>
 * Deleting LagIF.
 */
public class LagRemove extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_260101 = "260101";

  /** In case the information to be deleted does not exist. */
  private static final String ERROR_CODE_260201 = "260201";

  /** In case the CP requires LAG has not been deleted. */
  private static final String ERROR_CODE_260302 = "260302";

  /** Disconnection or connection timeout with EM has occurred while requesting LagIF deletion to EM. */
  private static final String ERROR_CODE_260402 = "260402";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_260403 = "260403";

  /** Error has occurred from EM while requesting LagIF deletion to EM (error response received). */
  private static final String ERROR_CODE_260404 = "260404";

  /** In case DB commitment failed after successful EM access. */
  private static final String ERROR_CODE_900405 = "900405";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public LagRemove(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.LagRemove);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_260101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      String nodeId = getUriKeyMap().get(KEY_NODE_ID);
      String lagIfId = getUriKeyMap().get(KEY_LAG_IF_ID);

      Nodes nodesDb = session.searchNodes(nodeId, null);
      if (nodesDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_260201);
      }
      LagIfs lagIfsDb = null;
      if (nodesDb.getLagIfsList() != null) {
        for (LagIfs listElem : nodesDb.getLagIfsList()) {
          if (listElem.getFc_lag_if_id().equals(lagIfId)) {
            lagIfsDb = listElem;
            break;
          }
        }
      }
      if (lagIfsDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [LagIfs]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_260201);
      }

      for (VlanIfs vlanIfs : session.getVlanIfsList(nodeId)) {
        if ((vlanIfs.getLag_if_id() != null) && vlanIfs.getLag_if_id().equals(lagIfId)) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "VLANIF data is found."));
          return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_260302);
        }
      }

      CeLagAddDelete ceLagAddDeleteEm = EmMapper.toLagIfDelete(nodesDb, lagIfsDb);

      session.startTransaction();

      session.deleteLagIfs(nodeId, lagIfsDb.getLag_if_id());

      AbstractMessage result = EmController.getInstance().request(ceLagAddDeleteEm);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_260404);
      }

      session.commit();

      response = makeSuccessResponse(RESP_NOCONTENTS_204, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      switch (dbae.getCode()) {
        case NO_DELETE_TARGET:
          response = makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_260201);
          break;
        case COMMIT_FAILURE:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900405);
          break;
        default:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_260403);
          break;
      }
    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_260402);
    } catch (IllegalArgumentException iae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), iae);
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_260101);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    if (getUriKeyMap() == null) {
      result = false;
    } else {
      if (!(getUriKeyMap().containsKey(KEY_NODE_ID)) || getUriKeyMap().get(KEY_NODE_ID) == null) {
        result = false;
      }
      if (!(getUriKeyMap().containsKey(KEY_LAG_IF_ID)) || getUriKeyMap().get(KEY_LAG_IF_ID) == null) {
        result = false;
      }
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

}
