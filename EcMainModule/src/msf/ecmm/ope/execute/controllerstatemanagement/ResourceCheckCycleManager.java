/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.controllerstatemanagement;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.ope.control.OperationControlManager;

/**
 * Managing cyclic resource monitoring.
 */
public class ResourceCheckCycleManager {

  /**
   * Logger.
   */
  private static final MsfLogger logger = new MsfLogger();

  /** Instance managing cyclic resource monitoring. */
  private static ResourceCheckCycleManager instance = new ResourceCheckCycleManager();

  /** Job schduler. */
  private Scheduler scheduler = null;

  /** Flag to stop cyclic resource montoring. */
  private boolean stopFlg;

  /**
   * Construtor(initialization process).
   */
  private ResourceCheckCycleManager() {
    this.scheduler = null;
    this.stopFlg = false;
  }

  /**
   * Getting instance which manages the controller status notication(server)
   *
   * @return instance which manages the controller status notication(server)
   */
  public static ResourceCheckCycleManager getInstance() {
    return instance;
  }

  /**
   * Starting cyclic controller status monitoring(in server).
   *
   * @throws SchedulerException
   *           Schedule activation failed
   */
  public void controlNotifyingCycle() throws SchedulerException {

    logger.trace(CommonDefinitions.START);

    int notifyingCycle = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.CONTROLLER_STATUS_INTERVAL);

    if (notifyingCycle > 0) {
      StdSchedulerFactory factory = new StdSchedulerFactory();
      scheduler = factory.getScheduler();

      JobDetail job1 = JobBuilder.newJob(ResourceCheckJob.class)
          .withIdentity("serverNotificationJob", "serverNotification").build();

      Trigger cron1 = TriggerBuilder.newTrigger().withIdentity("serverNotificationTrigger", "serverNotification")
          .startNow()
          .withSchedule(
              SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(notifyingCycle / 1000).repeatForever())
          .build();

      scheduler.scheduleJob(job1, cron1);
      scheduler.start();
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Activating resource monitoring.
   *
   * @return activation result
   */
  public synchronized boolean startCycle() {
    logger.trace(CommonDefinitions.START);

    if (scheduler == null) {

      try {
        instance.controlNotifyingCycle();
        CommonUtil.sleep(1000);
      } catch (SchedulerException exp) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_503121), exp);
      }

      logger.trace(CommonDefinitions.END);
      return true;

    } else {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_303118));
      return false;
    }
  }

  /**
   * Stopping cyclic resource monitoring.
   */
  public void stopCycle() {

    stopFlg = true;

    long timeoutLimit = Long
        .parseLong(EcConfiguration.getInstance().get(String.class, EcConfiguration.GATHER_MIB_STOP_TIMEOUT));
    long checkInterval = Long
        .parseLong(EcConfiguration.getInstance().get(String.class, EcConfiguration.GATHER_MIB_STOP_INTERVAL));

    long timeCheker = 0;

    while (true) {
      if (!OperationControlManager.getInstance().isResourceCheckExecution() || (timeCheker > timeoutLimit * 1000)) {
        break;
      } else {
        CommonUtil.sleep(checkInterval);
        timeCheker += checkInterval;
      }
    }

    if (scheduler != null) {
      try {
        scheduler.clear();
      } catch (SchedulerException exp) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403122), exp);
      }
    }
    scheduler = null;
  }

  /**
   * Getting flag to stop cyclic resource monitoring.
   *
   * @return flag to stop cyclic resource monitoring
   */
  public boolean isStopFlag() {
    return stopFlg;
  }

}
