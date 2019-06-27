/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.allinfo;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetEquipmentTypeList;

/**
 * Model List Information Acquisition
 */
public class AllDeviceTypeInfoAcquisition extends Operation {

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_030201 = "030201";

  /**
   * Constructor
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public AllDeviceTypeInfoAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.AllDeviceTypeInfoAcquisition);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    GetEquipmentTypeList outputData = null;

    try (DBAccessManager session = new DBAccessManager()) {

      List<Equipments> equipments = session.getEquipmentsList();

      outputData = RestMapper.toEquipmentList(equipments);

      response = makeSuccessResponse(RESP_OK_200, outputData);

    } catch (DBAccessException e) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_030201);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {
    return true;
  }

}
