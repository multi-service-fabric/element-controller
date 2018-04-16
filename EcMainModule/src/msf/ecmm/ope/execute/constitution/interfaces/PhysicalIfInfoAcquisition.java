/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetPhysicalInterface;

/**
 * Physical IF Information Acquisition.
 */
public class PhysicalIfInfoAcquisition extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_190101 = "190101";

  /** In case the number of acquired results is zero. */
  private static final String ERROR_CODE_190201 = "190201";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_190401 = "190401";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public PhysicalIfInfoAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.PhysicalIfInfoAcquisition);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    GetPhysicalInterface getPhysicalInterfaceRest = null;

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_190101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      PhysicalIfs physicalIfsDb = session.searchPhysicalIfs(getUriKeyMap().get(KEY_NODE_ID),
          getUriKeyMap().get(KEY_PHYSICAL_IF_ID));
      Nodes nodes = session.searchNodes(getUriKeyMap().get(KEY_NODE_ID), null);

      if (physicalIfsDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [PhysicalIfs]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_190201);
      }
      if (nodes == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [nodes]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_190201);
      }
      getPhysicalInterfaceRest = RestMapper.toPhyInInfo(physicalIfsDb, nodes.getEquipments());

      response = makeSuccessResponse(RESP_OK_200, getPhysicalInterfaceRest);

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_190401);
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
      if (!(getUriKeyMap().containsKey(KEY_PHYSICAL_IF_ID)) || getUriKeyMap().get(KEY_PHYSICAL_IF_ID) == null) {
        result = false;
      }
    }

    logger.trace(CommonDefinitions.END);
    return result;
  }
}
