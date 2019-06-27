/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl;

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
 * Class which manages the cyclic Config-Audit monitoring.
 */
public class ConfigAuditCycleManager {

  /** Logger. */
  private static final MsfLogger logger = new MsfLogger();

  /** The instance which manages the cyclic Config-Audit monitoring */
  private static ConfigAuditCycleManager instance = new ConfigAuditCycleManager();

  /** The job scheduler. */
  private Scheduler scheduler = null;

  /** The flag for indicating  the cyclic Config-Audit monitoring has stopped. */
  private boolean stopFlag;

  /**
   * Constructor (The initialization process).
   */
  private ConfigAuditCycleManager() {
    this.scheduler = null;
    this.stopFlag = false;
  }

  public static ConfigAuditCycleManager getInstance() {
    return instance;
  }

  /**
   * The Config-Audit monitoring is initiated.
   *
   * @return The initiation is OK or NG.
   */
  public synchronized boolean startCycle() {
    logger.trace(CommonDefinitions.START);

    if (scheduler == null) {

      try {
        instance.controlConfigAuditCycle();
        CommonUtil.sleep(1000);
      } catch (SchedulerException se) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_504107), se);
      }

      logger.trace(CommonDefinitions.END);
      return true;

    } else { 
      logger.info(LogFormatter.out.format(LogFormatter.MSG_304104));
      return false;
    }
  }

  /**
   * The cyclic Config-Audit monitoring starts
   *
   * @throws SchedulerException
   *           The iniation of the scheduler failed.
   */
  public void controlConfigAuditCycle() throws SchedulerException {

    logger.trace(CommonDefinitions.START);

    int configAuditCycle = EcConfiguration.getInstance().get(Integer.class,
        EcConfiguration.CONFIG_AUDIT_MONITOR_INTERVAL);

    if (configAuditCycle > 0) {

      StdSchedulerFactory factory = new StdSchedulerFactory();
      scheduler = factory.getScheduler();

      JobDetail job1 = JobBuilder.newJob(ConfigAuditJob.class).withIdentity("configAuditJob", "configAudit").build();

      Trigger cron1 = TriggerBuilder.newTrigger().withIdentity("configAuditTrigger", "configAudit").startNow()
          .withSchedule(
              SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(configAuditCycle * 60).repeatForever())
          .build();

      scheduler.scheduleJob(job1, cron1);
      scheduler.start();

    }

    logger.trace(CommonDefinitions.END);

  }

  /**
   * The Config-Audit monitoring is terminated.
   */
  public void stopCycle() {

    stopFlag = true;

    long timeoutLimit = Long
        .parseLong(EcConfiguration.getInstance().get(String.class, EcConfiguration.GATHER_MIB_STOP_TIMEOUT));
    long checkInterval = Long
        .parseLong(EcConfiguration.getInstance().get(String.class, EcConfiguration.GATHER_MIB_STOP_INTERVAL));

    long timeCheker = 0;

    while (true) {
      if (!OperationControlManager.getInstance().isConfigAudit() || (timeCheker > timeoutLimit * 1000)) {
        break;
      } else {
        CommonUtil.sleep(checkInterval);
        timeCheker += checkInterval;
      }
    }

    if (scheduler != null) {
      try {
        scheduler.clear();
      } catch (SchedulerException se) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_404108), se);
      }
    }
    scheduler = null;
  }

  /**
   * The flag which indicates the Config-Audit monitoring termination is acquired.
   *
   * @return The flag which indicates the Config-Audit monitoring termination
   */
  public boolean isStopFlag() {
    return stopFlag;
  }

}
