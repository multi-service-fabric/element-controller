/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetNode;

/**
 * Device Information Acquisition.
 */
public class NodeInfoAcquisition extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_100101 = "100101";
  /** In case the number of acquired results is zero. */
  private static final String ERROR_CODE_100201 = "100201";
  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_100401 = "100401";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public NodeInfoAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.NodeInfoAcquisition);
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_100101);
    }

    String nodeId = getUriKeyMap().get(KEY_NODE_ID);

    GetNode getNode = null;

    try (DBAccessManager session = new DBAccessManager()) {

      Nodes nodesDb = session.searchNodes(nodeId, null);
      if (nodesDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_100201);
      }

      getNode = RestMapper.toNodeInfo(nodesDb);

      response = makeSuccessResponse(RESP_OK_200, getNode);

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_100401);
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    if (getUriKeyMap() == null) {
      checkResult = false;
    } else {
      if (!(getUriKeyMap().containsKey(KEY_NODE_ID)) || getUriKeyMap().get(KEY_NODE_ID) == null) {
        checkResult = false;
      }
    }

    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

}
