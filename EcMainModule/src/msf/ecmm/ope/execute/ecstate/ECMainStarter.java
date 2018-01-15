/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.ecstate;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.devctrl.DhcpController;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.ControllerStatusToFc;
import msf.ecmm.fcctrl.pojo.parts.Controller;
import msf.ecmm.ope.control.ECMainState;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.receiver.RestServer;
import msf.ecmm.traffic.InterfaceIntegrityValidationManager;
import msf.ecmm.traffic.TrafficDataGatheringManager;

/**
 * EC Start-up Class Definition. Start up EC.
 */
public class ECMainStarter {

  /**
   * Logger
   */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** Stop Flar for Test. */
  public static boolean nonstop = true;

  /** Systems Switch Completion. */
  private static final String ENDSYSTEMSWITCHING = "end system switching";

  /** Controller Type (EC). */
  private static final String CONTROLLER_TYPE = "ec";

  /**
   * EC Start-up
   *
   * @param args
   *          configuration file path
   */
  public static void main(String[] args) {

    logger.info(LogFormatter.out.format(LogFormatter.MSG_303050));

    try {
      EcConfiguration.getInstance().read(args[0]);
    } catch (Exception e) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503038, e));
      System.exit(1);
      return;
    }

    OperationControlManager check1 = OperationControlManager.boot();
    if (check1 == null) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503039, "OperationControlManager"));
      System.exit(1);
      return;
    } else {
    }

    boolean check2 = RestServer.initialize();
    if (!check2) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503039, "RestServer"));
      try {
        if (OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver) {
          logger.debug("EC main module state is ChangeOver.");
        } else {
          logger.debug("EC main module state is StartReady.");
        }
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
      } catch (Exception e) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e);
      }
      System.exit(1);
      return;
    } else {
    }

    boolean check3 = InterfaceIntegrityValidationManager.boot();
    if (!check3) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503039,
          "InterfaceIntegrityValidationManager"));
      try {
        if (OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver) {
          logger.debug("EC main module state is ChangeOver.");
        } else {
          logger.debug("EC main module state is StartReady.");
        }
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
      } catch (Exception e) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e);
      }
      System.exit(1);
      return;
    } else {
    }

    boolean check4 = TrafficDataGatheringManager.boot();
    if (!check4) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503039, "TrafficDataGatheringManager"));
      try {
        if (OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver) {
          logger.debug("EC main module state is ChangeOver.");
        } else {
          logger.debug("EC main module state is StartReady.");
        }
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
      } catch (Exception e) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e);
      }
      System.exit(1);
      return;
    } else {
    }

    boolean check5 = EmController.getInstance().initialize();
    if (!check5) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503039, "EmController"));
      try {
        if (OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver) {
          logger.debug("EC main module state is ChangeOver.");
        } else {
          logger.debug("EC main module state is StartReady.");
        }
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
      } catch (Exception e) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e);
      }
      System.exit(1);
      return;
    } else {
    }

    boolean check6 = DhcpController.getInstance().initialize();
    if (!check6) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503039, "DhcpController"));
      try {
        if (OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver) {
          logger.debug("EC main module state is ChangeOver.");
        } else {
          logger.debug("EC main module state is StartReady.");
        }
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
      } catch (Exception e) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e);
      }
      System.exit(1);
      return;
    } else {
    }

    try {
      OperationControlManager.getInstance().updateEcMainState(true, ECMainState.InService);
    } catch (DBAccessException e) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503039, "State updete to in-service"));
      try {
        if (OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver) {
          logger.debug("EC main module state is ChangeOver.");
        } else {
          logger.debug("EC main module state is StartReady.");
        }
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
      } catch (Exception e1) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e);
      }
      System.exit(1);
      return;
    }

    OperationControlManager.getInstance().startSendingUnsentNodeStateNotification();

    RestClient rc = new RestClient();
    HashMap<String, String> keyMap = new HashMap<String, String>();
    ControllerStatusToFc outputData = new ControllerStatusToFc();
    Controller ecController = new Controller();
    ecController.setController_type(CONTROLLER_TYPE);
    ecController.setEvent(ENDSYSTEMSWITCHING);
    outputData.setController(ecController);
    try {
      rc.request(RestClient.CONTROLLER_STATE_NOTIFICATION,
          keyMap, outputData, CommonResponseFromFc.class);

    } catch (RestClientException rce) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_513031, "REST request"), rce);
    }

    logger.info(LogFormatter.out.format(LogFormatter.MSG_303067));

    while (nonstop) {
      CommonUtil.sleep(10000);
    }
  }

}
