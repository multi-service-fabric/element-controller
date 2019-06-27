/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.ecstate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommandExecutor;
import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.config.DiskThresholdConfiguration;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.config.ExpandConfigurationBase;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.SystemStatus;
import msf.ecmm.devctrl.DhcpController;
import msf.ecmm.emctrl.ConfigAuditCycleManager;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.ControllerStatusToFc;
import msf.ecmm.fcctrl.pojo.parts.Controller;
import msf.ecmm.ope.control.ECMainState;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.execute.controllerstatemanagement.ResourceCheckCycleManager;
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
  private static final MsfLogger logger = new MsfLogger();

  /** Stop Flag for Test. */
  public static boolean nonstop = true;

  /** Systems Switch Completion. */
  private static final String ENDSYSTEMSWITCHING = "end system switching";

  /** Controller Type (EC). */
  private static final String CONTROLLER_TYPE = "ec";

  /** EC status. */
  private static int status = ECMainState.StartReady.getValue();

  /** Script name for reource unlock confirmation. */
  private static final String CHECK_RESOURCE_LOCK = "check_resource_lock.sh";

  /** Script name for reource unlock. */
  private static final String RELEASE_RESOURCE_LOCK = "resource_lock_release.sh";


  /**
   * EC Start-up
   *
   * @param args
   *          configuration file path
   */
  public static void main(String[] args) {

    logger.simpleLogInfo(LogFormatter.out.format(LogFormatter.MSG_303050));

    try {
      EcConfiguration.getInstance().read(args[0]);
    } catch (Exception e1) {
      logger.simpleLogError(LogFormatter.out.format(LogFormatter.MSG_503038, e1));
      System.exit(1);
      return;
    }
    try {
      DiskThresholdConfiguration.getInstance().read();
    } catch (Exception e1) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503038, e1));
      System.exit(1);
      return;
    }
    if (args.length == 2) {
      try {
        ExpandOperation.getInstance().read(args[1]);
      } catch (Exception e1) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503038, e1));
        System.exit(1);
        return;
      }
      try {
        ExpandConfigurationBase.readExpandConfiguration();
      } catch (Exception e1) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503038, e1));
        System.exit(1);
        return;
      }
    }

    OperationType.init();

    logger.debug("Read db.");
    try (DBAccessManager session = new DBAccessManager()) {
      SystemStatus dbstate = session.getSystemStatus();
      if (dbstate == null) {
        status = ECMainState.Stop.getValue();
      } else {
        status = dbstate.getService_status();
      }
    } catch (Throwable e2) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503090, e2));
      System.exit(1);
      logger.trace(CommonDefinitions.END);
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
      } catch (Exception e1) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e1);
      }
      System.exit(1);
      return;
    } else {
    }

    boolean check3 = InterfaceIntegrityValidationManager.boot();
    if (!check3) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503039, "InterfaceIntegrityValidationManager"));
      try {
        if (OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver) {
          logger.debug("EC main module state is ChangeOver.");
        } else {
          logger.debug("EC main module state is StartReady.");
        }
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
      } catch (Exception e1) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e1);
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
      } catch (Exception e1) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e1);
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
      } catch (Exception e1) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e1);
      }
      System.exit(1);
      return;
    } else {
    }

    boolean check7 = ConfigAuditCycleManager.getInstance().startCycle();
    if (!check7) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503039, "Config-Audit"));
      try {
        if (OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver) {
          logger.debug("EC main module state is ChangeOver.");
        } else {
          logger.debug("EC main module state is StartReady.");
        }
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
      } catch (Exception e1) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e1);
      }
      System.exit(1);
      return;
    } else {
    }

    boolean check8 = ResourceCheckCycleManager.getInstance().startCycle();
    if (!check8) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503039, "ResourceCheckCycleManager"));
      try {
        if (OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver) {
          logger.debug("EC main module state is ChangeOver.");
        } else {
          logger.debug("EC main module state is StartReady.");
        }
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
      } catch (Exception e1) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e1);
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
      } catch (Exception e1) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e1);
      }
      System.exit(1);
      return;
    } else {
    }

    try {
      OperationControlManager.getInstance().updateEcMainState(true, ECMainState.InService);
    } catch (DBAccessException e1) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_503039, "State updete to in-service"));
      try {
        if (OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver) {
          logger.debug("EC main module state is ChangeOver.");
        } else {
          logger.debug("EC main module state is StartReady.");
        }
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
        OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
      } catch (Exception e2) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503068), e2);
      }
      System.exit(1);
      return;
    }

    RestClient rc = new RestClient();
    HashMap<String, String> keyMap = new HashMap<String, String>();
    ControllerStatusToFc outputData = new ControllerStatusToFc();
    Controller ecController = new Controller();
    ecController.setController_type(CONTROLLER_TYPE);
    if (ECMainState.getState(status) == ECMainState.ChangeOver) {
      boolean isLocked = true;
      String sbyIp = EcConfiguration.getInstance().get(String.class, EcConfiguration.SBY_IP_ADDRESS);
      if (sbyIp != null) {
        int interval = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.RESOURCE_CHECK_INTERVAL);
        int times = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.NUMBER_OF_RESOURCE_CHECK);
        List<String> stdList = new ArrayList<String>();
        List<String> errList = new ArrayList<String>();
        String scriptPath = EcConfiguration.getInstance().get(String.class, EcConfiguration.SCRIPT_PATH);
        String grpEc = EcConfiguration.getInstance().get(String.class, EcConfiguration.EC_RESOURCE_GROUP_NAME);
        String[] params = { "bash", scriptPath + "/" + CHECK_RESOURCE_LOCK, grpEc };
        int canCheckExcec = 0;
        for (int i = 0; i < times; i++) {
          stdList.clear();
          errList.clear();
          params[1] = scriptPath + "/" + CHECK_RESOURCE_LOCK;
          canCheckExcec = CommandExecutor.exec(params, stdList, errList);
          if (canCheckExcec == 0 && stdList.isEmpty() && errList.isEmpty()) {
            isLocked = false;
            break;
          } else {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403124) + scriptPath + "/" + CHECK_RESOURCE_LOCK);
            if (canCheckExcec == 0 && !stdList.isEmpty() && errList.isEmpty()) {
              stdList.clear();
              errList.clear();
              int canReleaseExcec = 0;
              params[1] = scriptPath + "/" + RELEASE_RESOURCE_LOCK;
              canReleaseExcec = CommandExecutor.exec(params, stdList, errList);
              if (canReleaseExcec == 0 && stdList.isEmpty() && errList.isEmpty()) {
                CommonUtil.sleep(interval * 1000);
                stdList.clear();
                errList.clear();
                params[1] = scriptPath + "/" + CHECK_RESOURCE_LOCK;
                canCheckExcec = CommandExecutor.exec(params, stdList, errList);
                if (canCheckExcec == 0 && stdList.isEmpty() && errList.isEmpty()) {
                  isLocked = false;
                  break;
                }
              } else {
                logger
                    .warn(LogFormatter.out.format(LogFormatter.MSG_403124) + scriptPath + "/" + RELEASE_RESOURCE_LOCK);
              }
            }
          }
        }
        if (isLocked) {
          logger.error(LogFormatter.out.format(LogFormatter.MSG_503125));
        } else {
          logger.info(LogFormatter.out.format(LogFormatter.MSG_303126));
        }
      }
      ecController.setEvent(ENDSYSTEMSWITCHING);
      outputData.setController(ecController);
      try {
        rc.request(RestClient.CONTROLLER_STATE_NOTIFICATION, keyMap, outputData, CommonResponseFromFc.class);

      } catch (RestClientException rce) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_513031, "REST request"), rce);
      }
      logger.info(LogFormatter.out.format(LogFormatter.MSG_303091, "Complete to system switch notification:[end]"));
    }

    logger.info(LogFormatter.out.format(LogFormatter.MSG_303067));

    OperationControlManager.getInstance().startSendingUnsentNodeStateNotification();

    while (nonstop) {
      CommonUtil.sleep(10000);
    }

  }

}
