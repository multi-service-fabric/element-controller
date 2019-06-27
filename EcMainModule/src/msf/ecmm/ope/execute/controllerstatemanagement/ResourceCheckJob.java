/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.controllerstatemanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.config.DiskThresholdConfiguration;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.ServerNotificationToFc;
import msf.ecmm.fcctrl.pojo.parts.CpuToFc;
import msf.ecmm.fcctrl.pojo.parts.DevicesToFc;
import msf.ecmm.fcctrl.pojo.parts.DiskToFc;
import msf.ecmm.fcctrl.pojo.parts.MemoryToFc;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.CheckEcMainModuleStatus;
import msf.ecmm.ope.receiver.pojo.parts.DeviceInfo;
import msf.ecmm.ope.receiver.pojo.parts.Informations;

/**
 * Executing cyclic controller staus monitoring job (server).
 */
public class ResourceCheckJob implements Job {

  /**
   * Logger.
   */
  private static final MsfLogger logger = new MsfLogger();

  /** Controller to be monitored. */
  public static final String KEY_CONTROLLER = "controller";

  /** OS. */
  public static final String SYSTEM_TYPE_OS = "operating_system";
  /** CTL process. */
  public static final String SYSTEM_TYPE_CONTROL = "ctl_process";

  /** URI for ACT_EC. */
  public static final String EC_ACT_URI = "ec";
  /** URI for SBY_EC. */
  public static final String EC_SBY_URI = "ec_sby";
  /** string concatenated with URI. */
  public static final String BOND_URI = " ";

  /** Controller type EC_ACT. */
  public static final String EC = "ec";
  /** Controller type EM_ACT. */
  public static final String EM = "em";

  /** EC(ACT). */
  public static final String EC_ACT = "ec_act";
  /** EC(SBY). */
  public static final String EC_SBY = "ec_sby";
  /** EM(ACT). */
  public static final String EM_ACT = "em_act";
  /** EM(SBY). */
  public static final String EM_SBY = "em_sby";

  /** Act. **/
  private static final int TYPE_ACT = 0;
  /** Sby. **/
  private static final int TYPE_SBY = 1;

  @Override
  public void execute(JobExecutionContext arg0) throws JobExecutionException {
    logger.trace(CommonDefinitions.START);
    logger.info(LogFormatter.out.format(LogFormatter.MSG_303119));

    boolean startResult = OperationControlManager.getInstance().startResourceCheck();
    boolean stopFlg = ResourceCheckCycleManager.getInstance().isStopFlag();

    try {
      if (startResult && !stopFlg) {

        String getTargetEcSrv = EC_ACT_URI;
        String serverAddress = EcConfiguration.getInstance().get(String.class, EcConfiguration.SBY_IP_ADDRESS);
        Integer actCpuTlv = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.ACT_CPU_THRESHOLD);
        Integer actMemoryTlv = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.ACT_MEMORY_THRESHOLD);
        Integer conCpuTlv = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.CONTROLLER_CPU_THRESHOLD);
        Integer conMemoryTlv = EcConfiguration.getInstance().get(Integer.class,
            EcConfiguration.CONTROLLER_MEMORY_THRESHOLD);

        Integer actDiskTlv = 0;
        Integer sbyCpuTlv = null;
        Integer sbyMemoryTlv = null;
        Integer sbyDiskTlv = null;

        if (!(serverAddress == null || serverAddress.equals(""))) {
          getTargetEcSrv += BOND_URI;
          getTargetEcSrv += EC_SBY_URI;

          sbyCpuTlv = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.SBY_CPU_THRESHOLD);
          sbyMemoryTlv = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.SBY_MEMORY_THRESHOLD);
          sbyDiskTlv = 0;

        }

        HashMap<String, String> getTargetSvr = new HashMap<>();
        getTargetSvr.put(KEY_CONTROLLER, getTargetEcSrv);
        getTargetSvr.put(CommonDefinitions.KEY_GET_INFO, null);

        ECStateManagement ecStateManagement = new ECStateManagement(null, getTargetSvr);
        AbstractResponseMessage abstractResponseMessage = ecStateManagement.execute();
        if (abstractResponseMessage.getResponseCode() != msf.ecmm.ope.receiver.ReceiverDefinitions.RESP_OK_200) {
          logger.debug("ECStateManagement is abnormalEnd");
          throw new Exception();
        }
        CheckEcMainModuleStatus checkEcMainModuleStatus = (CheckEcMainModuleStatus) abstractResponseMessage;

        for (Informations informations : checkEcMainModuleStatus.getInformations()) {

          boolean overCpuTlvFlg = false;
          boolean overMemoryTlvFlg = false;

          CpuToFc cpuToFc = null;
          MemoryToFc memoryToFc = null;
          DiskToFc diskToFc = null;

          if (EC_ACT_URI.equals(informations.getControllerType())) {
            if (conCpuTlv < informations.getController().getCpu()) {
              cpuToFc = new CpuToFc();
              cpuToFc.setUseRate(informations.getController().getCpu());
            }
            if (conMemoryTlv < informations.getController().getMemory()) {
              memoryToFc = new MemoryToFc();
              memoryToFc.setUsed(informations.getController().getMemory());
            }

            if (cpuToFc != null || memoryToFc != null) {
              ServerNotificationToFc serverNotificationToFc = RestMapper.toServerNotificationEc(EC_ACT,
                  SYSTEM_TYPE_CONTROL, cpuToFc, memoryToFc, diskToFc);
              RestClient rc = new RestClient();
              HashMap<String, String> keyMap = new HashMap<String, String>();
              rc.request(RestClient.CONTROLLER_STATE_NOTIFICATION_SERVER, keyMap, serverNotificationToFc,
                  CommonResponseFromFc.class);
            }

            cpuToFc = null;
            memoryToFc = null;
            diskToFc = null;
          }

          String controllerType = null;

          if (EC_ACT_URI.equals(informations.getControllerType())) {
            if (actCpuTlv < informations.getOs().getCpu().getUseRate()) {
              overCpuTlvFlg = true;
            }
            if (actMemoryTlv < informations.getOs().getMemory().getUsed()) {
              overMemoryTlvFlg = true;
            }
            controllerType = EC_ACT;

          } else if (EC_SBY_URI.equals(informations.getControllerType())) {
            if (sbyCpuTlv < informations.getOs().getCpu().getUseRate()) {
              overCpuTlvFlg = true;
            }
            if (sbyMemoryTlv < informations.getOs().getMemory().getUsed()) {
              overMemoryTlvFlg = true;
            }
            controllerType = EC_SBY;
          }

          if (overCpuTlvFlg) {
            cpuToFc = new CpuToFc();
            cpuToFc.setUseRate(informations.getOs().getCpu().getUseRate());
          }

          if (overMemoryTlvFlg) {
            memoryToFc = new MemoryToFc();
            memoryToFc.setUsed(informations.getOs().getMemory().getUsed());
            memoryToFc.setFree(informations.getOs().getMemory().getFree());
          }

          List<DevicesToFc> devicesToFcs = new ArrayList<DevicesToFc>();
          for (DeviceInfo deviceInfo : informations.getOs().getDisk().getDevices()) {
            boolean overDiskTlvFlg = false;

            if (EC.equals(informations.getControllerType())) {
              actDiskTlv = DiskThresholdConfiguration.getInstance().get(TYPE_ACT).get(deviceInfo.getMountedOn());
              if (actDiskTlv == null) {
                continue;
              } else {
                if (actDiskTlv < deviceInfo.getUsed()) {
                  overDiskTlvFlg = true;
                }
              }
            } else {
              sbyDiskTlv = DiskThresholdConfiguration.getInstance().get(TYPE_SBY).get(deviceInfo.getMountedOn());
              if (sbyDiskTlv == null) {
                continue;
              } else {
                if (sbyDiskTlv < deviceInfo.getUsed()) {
                  overDiskTlvFlg = true;
                }
              }
            }

            if (overDiskTlvFlg) {
              DevicesToFc devicesToFc = new DevicesToFc();
              devicesToFc.setFileSystem(deviceInfo.getFileSystem());
              devicesToFc.setMountedOn(deviceInfo.getMountedOn());
              devicesToFc.setSize(deviceInfo.getSize());
              devicesToFc.setUsed(deviceInfo.getUsed());
              devicesToFcs.add(devicesToFc);
            }
          }

          if (!devicesToFcs.isEmpty()) {
            diskToFc = new DiskToFc();
            diskToFc.setDevices(devicesToFcs);
          }

          ServerNotificationToFc serverNotificationToFc = RestMapper.toServerNotificationEc(controllerType,
              SYSTEM_TYPE_OS, cpuToFc, memoryToFc, diskToFc);

          if (serverNotificationToFc != null) {
            RestClient rc = new RestClient();
            HashMap<String, String> keyMap = new HashMap<String, String>();

            rc.request(RestClient.CONTROLLER_STATE_NOTIFICATION_SERVER, keyMap, serverNotificationToFc,
                CommonResponseFromFc.class);
          }
        }
      }
    } catch (Exception exp) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403117), exp);
    }

    if (startResult && !stopFlg) {
      OperationControlManager.getInstance().endResourceCheck();
      logger.debug("Resource Check stop.");
    } else {
      logger.debug("Resource Check skip.");
    }

    logger.trace(CommonDefinitions.END);
  }
}
