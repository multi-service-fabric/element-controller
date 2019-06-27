/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.ControllerFileUpgradeConfiguration;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.emctrl.RestClientToEm;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

/**
 * Class for executing the switch-over process in the controller
 */
public class ControllerSwitch extends Operation {

  /** The name of the extended function operation. */

  String operationName = "ControllerSwitch";

  /** In case the check of the input parameter is NG(json is not OK). */
  private static final String ERROR_CODE_610101 = "610101";
  /** In case an error has occurred in EM when the controller status is acquired in EM. */ 
  private static final String ERROR_CODE_610402 = "610402";
  /** In case the disconnection with EM has occurred when the controller status is acquired in EM. */
  private static final String ERROR_CODE_610401 = "610401";
  /** In case of the normal response from EM and the failure of the EC switch-over initiation. */
  private static final String ERROR_CODE_610403 = "610403";
  /** In case of the failure of the EC switch-over initiation only when EC is switched-over. */	
  private static final String ERROR_CODE_610404 = "610404";

  /** The contrller type */
  /** EC (in ACT). */
  private static final String EC = "ec";
  /** EC(in ACT). */
  private static final String EM = "em";

  /** The URI for the request the switch-over in EM **/
  private static final String EM_SWITCH_URI = "/v1/internal/em_ctrl/ctrl-switch";

  /**
   * Constructor
   *
   * @param idt
   *          The input data
   * @param ukm
   *          URI key information
   */
  public ControllerSwitch(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(operationName).getOperationTypeId());
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);
    AbstractResponseMessage response = null;


    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_610101);
    }

    boolean isAllSwitch = false;
    if (getUriKeyMap().get(KEY_CONTROLLER) == null || (!getUriKeyMap().get(KEY_CONTROLLER).contains(EM)
        && !getUriKeyMap().get(KEY_CONTROLLER).contains(EC))) {
      isAllSwitch = true;
    }
    if (isAllSwitch || getUriKeyMap().get(KEY_CONTROLLER).contains(EM)) {
      try {
        EcConfiguration config = EcConfiguration.getInstance();
        String emIpaddr = config.get(String.class, EcConfiguration.EM_ADDRESS);
        String emPort = config.get(String.class, EcConfiguration.EM_REST_PORT);
        new RestClient().request(emIpaddr, emPort, RestClientToEm.POST, EM_SWITCH_URI, null,
            CommonResponseFromFc.class);
      } catch (RestClientException rce) {
        if (rce.getCode() == RestClientException.ERROR_RESPONSE) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST request"), rce);
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_610402);
          return response;
        } else {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST timeout"), rce);
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_610401);
          return response;
        }
      }
    }

    if (isAllSwitch || getUriKeyMap().get(KEY_CONTROLLER).contains(EC)) {
      String scriptPath = ControllerFileUpgradeConfiguration.getInstance().get(String.class,
          ControllerFileUpgradeConfiguration.SWITCH_SCRIPT_PATH);
      if (!CommonUtil.isFile(scriptPath)) {
        if (getUriKeyMap().get(KEY_CONTROLLER).contains(EM)) {
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_610403);
        } else {
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_610404);
        }
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403115) + "Script file not found.");
        return response;
      }
      try {
        ControllerSwitchThread switchThread = new ControllerSwitchThread();
        switchThread.start();
      } catch (IllegalThreadStateException thredEx) {
        if (isAllSwitch || getUriKeyMap().get(KEY_CONTROLLER).contains(EM)) {
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_610403);
        } else {
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_610404);
        }
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403115) + "Thread creation failed.", thredEx);
        return response;
      }
    }
    response = makeSuccessResponse(RESP_ACCEPTED_202, new CommonResponse());

    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean result = true;
    if (!getUriKeyMap().containsKey(KEY_CONTROLLER)) {
      result = false;
    }

    logger.trace(CommonDefinitions.END);
    return result;
  }

}
