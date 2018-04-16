/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

/**
 * Model Information Deletion
 */
public class DeviceInfoRemove extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_050101 = "050101";
  /** In case the model information to be deleted does not exist. */
  private static final String ERROR_CODE_050201 = "050201";
  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_050401 = "050401";

  /**
   * Constructor
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public DeviceInfoRemove(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.DeviceInfoRemove);
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_050101);
    }

    String equipmentTypeId = getUriKeyMap().get(KEY_EQUIPMENT_TYPE_ID);

    try (DBAccessManager session = new DBAccessManager()) {

      session.startTransaction();

      session.deleteEquipments(equipmentTypeId);

      session.commit();

      response = makeSuccessResponse(RESP_NOCONTENTS_204, new CommonResponse());

    } catch (DBAccessException e) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
      if (e.getCode() == DBAccessException.NO_DELETE_TARGET) {
        response = makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_050201);
      } else {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_050401);
      }
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    if (!getUriKeyMap().containsKey(KEY_EQUIPMENT_TYPE_ID)) {
      checkResult = false;
    }

    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

}
