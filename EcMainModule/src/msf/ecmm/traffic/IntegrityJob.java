/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.traffic;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * IF Status Integrity Periodic Execution Job Class Definition. Class which defines periodic execution job of IF status integrity.
 */
public class IntegrityJob implements Job {

  @Override
  public void execute(JobExecutionContext arg0) throws JobExecutionException {
    InterfaceIntegrityValidationManager.getInstance().startIntegrity();
  }

}
