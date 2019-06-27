/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.ecstate;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import org.hibernate.HibernateException;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.emctrl.ConfigAuditCycleManager;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.ControllerStatusToFc;
import msf.ecmm.fcctrl.pojo.parts.Controller;
import msf.ecmm.ope.control.ECMainState;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.execute.controllerstatemanagement.ResourceCheckCycleManager;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.traffic.InterfaceIntegrityValidationManager;
import msf.ecmm.traffic.TrafficDataGatheringManager;

/**
 * EC Termination Class Definition. Terminate EC
 */
public class ECMainStopper extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_300101 = "300101";

  /** Process Waiting Time */
  private final int WAIT_TIME = 1000;

  /** Normal Termination */
  private final String NORMAL_STOP = "normal";

  /** Switch systems */
  private final String CHANGE_OVER = "chgover";

  /** Systems Switch Start. */
  private static final String STARTSYSTEMSWITCHING = "start system switching";

  /** Controller Type (EC). */
  private static final String CONTROLLER_TYPE = "ec";

  /**
   * Constructor
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public ECMainStopper(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.ECMainStopper);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));

      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_300101);
    } else {
    }

    ECMainState state;
    if (this.getUriKeyMap().get(KEY_STOP_TYPE).equals(NORMAL_STOP)) {
      state = ECMainState.StopReady;
    } else {
      state = ECMainState.ChangeOver;
    }

    try (DBAccessManager session = new DBAccessManager()) {

      session.startTransaction();

      session.updateSystemStatus(state.getValue(), -1);

      session.commit();

    } catch (DBAccessException | HibernateException dae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."));
    }

    synchronized (OperationControlManager.getInstance()) {
      try {
        OperationControlManager.getInstance().updateEcMainState(false, state);
      } catch (DBAccessException e1) {
      }
    }

    while ((OperationControlManager.getInstance().isUnsentNodeStateNotificationSendingState())
        || (OperationControlManager.getInstance().getNumberOfExecuteOperations() > 1)) {
      CommonUtil.sleep(WAIT_TIME);
    }

    OperationControlManager.getInstance().sendUnsentNodeStateNotification();

    TrafficDataGatheringManager.getInstance().stopGetheringCycle();

    InterfaceIntegrityValidationManager.getInstance().stopIntegrityCycle();

    ConfigAuditCycleManager.getInstance().stopCycle();

    ResourceCheckCycleManager.getInstance().stopCycle();

    AbstractResponseMessage ret = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    if (state == ECMainState.StopReady) {
      try (DBAccessManager session = new DBAccessManager()) {

        session.startTransaction();

        session.updateSystemStatus(ECMainState.Stop.getValue(), -1);

        session.commit();

        try {
          OperationControlManager.getInstance().updateEcMainState(false, ECMainState.Stop);
        } catch (DBAccessException e1) {
        }

      } catch (DBAccessException | HibernateException dae) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dae);
      }
    } else {
      RestClient rc = new RestClient();
      HashMap<String, String> keyMap = new HashMap<String, String>();
      ControllerStatusToFc outputData = new ControllerStatusToFc();
      Controller ecController = new Controller();
      ecController.setController_type(CONTROLLER_TYPE);
      ecController.setEvent(STARTSYSTEMSWITCHING);
      outputData.setController(ecController);
      try {
        rc.request(RestClient.CONTROLLER_STATE_NOTIFICATION, keyMap, outputData, CommonResponseFromFc.class);

      } catch (RestClientException rce) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_513031, "REST request"), rce);
      }
      logger.info(LogFormatter.out.format(LogFormatter.MSG_303091, "Complete to system switch notification:[start]"));

    }
    logger.trace(CommonDefinitions.END);

    return ret;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    if (checkResult) {
      if (getUriKeyMap() == null) {
        checkResult = false;
      } else if (getUriKeyMap().get(KEY_STOP_TYPE) == null) {
        checkResult = false;
      } else if (!getUriKeyMap().get(KEY_STOP_TYPE).equals(CHANGE_OVER)
          && !getUriKeyMap().get(KEY_STOP_TYPE).equals(NORMAL_STOP)) {
        checkResult = false;
      }
    } else {
    }

    logger.trace(CommonDefinitions.END + ", checkResult=" + checkResult);

    return checkResult;
  }

  /**
   * Process End<br>
   * Terminating the process.
   */
  public static void systemExit() {
    logger.info(LogFormatter.out.format(LogFormatter.MSG_303071));
    System.exit(0);
  }
}
