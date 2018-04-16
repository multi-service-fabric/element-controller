/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.allinfo;

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
import msf.ecmm.ope.receiver.pojo.GetInterfaceList;

/**
 * IF Information List Acquisition.
 */
public class AllIfInfoAcquisition extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_170101 = "170101";
  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_170301 = "170301";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public AllIfInfoAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.AllIfInfoAcquisition);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    GetInterfaceList outputData = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_170101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      Nodes nodes = session.searchNodes(getUriKeyMap().get(KEY_NODE_ID), null);

      if (nodes == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_170101);
      }

      outputData = RestMapper.toIfList(nodes);

      response = makeSuccessResponse(RESP_OK_200, outputData);

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_170301);
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
