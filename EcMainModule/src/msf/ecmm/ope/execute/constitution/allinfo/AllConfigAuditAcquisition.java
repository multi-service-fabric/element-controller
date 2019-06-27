/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.allinfo;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetConfigAuditList;

/**
 * The Config-Audit list is acquired.
 */
public class AllConfigAuditAcquisition extends Operation {

  /** The name of the extended function operation. */
  String operationName = "AllConfigAuditAcquisition";

  /** In case the disconnection with the EM  or  the timeout error have occurred during  */
  /** when waiting for  the response to the acquisition request of the Config-Audit listt.    */
  private static final String ERROR_CODE_620301 = "620301";

  /** In case an error has occurred when the DB is accessed. */
  private static final String ERROR_CODE_620302 = "620302";

  /**
   * In case the error message has been received from EM
   * when waiting for the response to the acquisition request of the Config-Audit list.
   * (In case the equiment is available which detects the failure of the Config-Audit acquisition. 
   */
  private static final String ERROR_CODE_620303 = "620303";

  /**
   * Constructor.
   *
   * @param idt
   *          The input data
   * @param ukm
   *          URI key information 
   */
  public AllConfigAuditAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(operationName).getOperationTypeId());
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    try {

      AllConfigAuditUtility allConfigAuditUtility = new AllConfigAuditUtility();
      GetConfigAuditList outputData = allConfigAuditUtility.execute();

      response = makeSuccessResponse(RESP_OK_200, outputData);

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_620302);
    } catch (RestClientException rce) {
      if (rce.getCode() == RestClientException.ERROR_RESPONSE) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST request"), rce);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_620303);
      } else {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST timeout"), rce);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_620301);
      }
    }
    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {
    return true;
  }
}
