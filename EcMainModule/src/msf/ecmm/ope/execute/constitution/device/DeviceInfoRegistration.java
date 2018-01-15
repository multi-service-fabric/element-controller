/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.RegisterEquipmentType;

/**
 * Model Information Registration
 */
public class DeviceInfoRegistration extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_020101 = "020101";
  /** In case the model information to be registered already exists. */
  private static final String ERROR_CODE_020201 = "020201";
  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_020301 = "020301";

  /**
   * Constructor
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public DeviceInfoRegistration(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.DeviceInfoRegistration);
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_020101);
    }

    RegisterEquipmentType regiterEquipmentTypeRest = (RegisterEquipmentType) getInData();

    try (DBAccessManager session = new DBAccessManager()) {

      Equipments equipmentsDb = DbMapper.toEqInfoCreate(regiterEquipmentTypeRest);

      session.startTransaction();

      session.addEquipments(equipmentsDb);

      session.commit();

      response = makeSuccessResponse(RESP_CREATED_201, new CommonResponse());

    } catch (DBAccessException e) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
      if (e.getCode() == DBAccessException.DOUBLE_REGISTRATION) {
        response = makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_020201);
      } else {
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_020301);
      }
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    RegisterEquipmentType regiterEquipmentTypeRest = (RegisterEquipmentType) getInData();

    try {
      regiterEquipmentTypeRest.check(getOperationType());
    } catch (CheckDataException e) {
      logger.warn("check error :", e);
      checkResult = false;
    }

    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

}
