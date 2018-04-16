/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.traffic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;

/**
 * Traffic Information Collection Periodic Execution Job Class Definition. Class which defines periodic execution job of traffic information collection.
 */
public class TrafficGatherJob implements Job {

  /**
   * Logger
   */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  @Override
  public void execute(JobExecutionContext arg0) throws JobExecutionException {

    boolean startChecker = true;
    synchronized (TrafficDataGatheringManager.getInstance()) {
      startChecker = TrafficDataGatheringManager.getInstance().getExecuteThreadHolder().isEmpty();
    }

    if (startChecker) {
      TrafficDataGatheringManager.getInstance().startGathering();
    } else {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_407066));
      logger.debug("ExecuteThreadHolder=" + TrafficDataGatheringManager.getInstance().getExecuteThreadHolder());
      logger.debug(
          "ExecuteThreadHolderSize=" + TrafficDataGatheringManager.getInstance().getExecuteThreadHolder().size());

    }
  }

}
