/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.BLeafAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.ChangeNode;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

/**
 * Device Change Base.
 */
public abstract class NodeChange extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_080101 = "080101";
  /** In case the number of pieces of information for Leaf device extention/removal is zero (data acquisition from DB is failed in the previous step of process). */
  private static final String ERROR_CODE_080201 = "080201";
  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_080408 = "080408";
  /** Disconnection or connection timeout to EM has occurred in Leaf update request to EM. */
  private static final String ERROR_CODE_080414 = "080414";
  /** Error has occurred in EM in Leaf update request to EM (error response received). */
  private static final String ERROR_CODE_080415 = "080415";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public NodeChange(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_080101);
    }
    ChangeNode changeNode = (ChangeNode) getInData();

    try (DBAccessManager session = new DBAccessManager()) {

      Nodes node = session.searchNodes(changeNode.getNode().getNodeId(), null);
      if (node == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_080201);
      }

      Nodes pairNode = null;
      if (changeNode.getPairNode() != null) {
        pairNode = session.searchNodes(changeNode.getPairNode().getNodeId(), null);
        if (pairNode == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Pair Nodes]"));
          return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_080201);
        }
      }

      BLeafAddDelete bLeafAddDelete = EmMapper.toNodeChange(changeNode, node, pairNode);

      EmController emController = EmController.getInstance();
      AbstractMessage ret = emController.request(bLeafAddDelete, true);
      if (ret.isResult() == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080415);
      }

      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080408);
    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_080414);
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    ChangeNode changeNode = (ChangeNode) getInData();
    try {
      changeNode.check(new OperationType(getOperationType()));
    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      checkResult = false;
    }
    logger.trace(CommonDefinitions.END);
    return checkResult;
  }
}
