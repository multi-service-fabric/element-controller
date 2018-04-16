/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.allinfo;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;
import java.util.List;

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
import msf.ecmm.ope.receiver.pojo.GetPhysicalInterfaceList;

/**
 * Physical IF Information List Acquisition.
 */
public class AllPhysicalIfInfoAcquisition extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_180101 = "180101";
  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_180301 = "180301";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public AllPhysicalIfInfoAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.AllPhysicalIfInfoAcquisition);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    GetPhysicalInterfaceList outputData = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_180101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      List<PhysicalIfs> physicalIfs = session.getPhysicalIfsList(getUriKeyMap().get(KEY_NODE_ID));

      Nodes nodes = session.searchNodes(getUriKeyMap().get(KEY_NODE_ID), null);

      outputData = RestMapper.toPhyInInfoList(physicalIfs, nodes.getEquipments());

      response = makeSuccessResponse(RESP_OK_200, outputData);

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_180301);
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
