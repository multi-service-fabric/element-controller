/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.UpdateNodeInfo;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.NodeInfoUpdate;

/**
 * Updating node information.
 */
public class NodeOsUpdate extends Operation {

  /** In case input paramter check is NG. */
  private static final String ERROR_CODE_590101 = "590101";

  /** In case information to be upadated does not exist. */
  private static final String ERROR_CODE_590201 = "590201";

  /** In case  disconnection with EM was detected or timeout occurred while requesting  to EM. */
  private static final String ERROR_CODE_590402 = "590402";

  /** In case error has been received from EM while requesting to EM. */
  private static final String ERROR_CODE_590403 = "590403";

  /** In case DB commit failed after EM access is successful. */
  private static final String ERROR_CODE_900404 = "900404";

  /** In case failure  occurred during DB access. */
  private static final String ERROR_CODE_590401 = "590401";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key infomation
   */
  public NodeOsUpdate(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.NodeUpdate);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;


    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_590101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      NodeInfoUpdate inputData = (NodeInfoUpdate) getInData();

      Nodes nodesDb = session.searchNodes(nodeId, null);
      if (nodesDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db. [nodes]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_590201);
      }

      Equipments newEqDb = session.searchEquipments(inputData.getNode().getEquipmentTypeId());
      if (newEqDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db. [equipments]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_590201);
      }

      session.startTransaction();

      session.updateNodeEquipment(nodeId, inputData.getNode().getEquipmentTypeId());

      UpdateNodeInfo updateNodeInfo = EmMapper.toNodeInfoUpdate(nodesDb, newEqDb);

      AbstractMessage result = EmController.getInstance().request(updateNodeInfo);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_590403);
      }

      session.commit();

      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      if (dbae.getCode() == DBAccessException.COMMIT_FAILURE) {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900404);
      } else {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_590401);
      }
    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_590402);

    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      NodeInfoUpdate inputData = (NodeInfoUpdate) getInData();

      inputData.check(new OperationType(getOperationType()));

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }
}
