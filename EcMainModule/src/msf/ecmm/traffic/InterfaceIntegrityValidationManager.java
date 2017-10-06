package msf.ecmm.traffic;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.ope.control.OperationControlManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class InterfaceIntegrityValidationManager {

	private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	private boolean stopFlug;

	private static InterfaceIntegrityValidationManager instance = null;

	private InterfaceIntegrityValidationManager() {
		this.stopFlug = false;
		this.executeThreadHolder = new ArrayList<IntegrityExecutor>();
		this.watchDogThreadHolder = null;
	}

	private void controlIntegrityCycle() throws SchedulerException {

		logger.trace(CommonDefinitions.START);

		int integrityCycle = Integer.parseInt(EcConfiguration.getInstance().get(String.class, EcConfiguration.FAILURE_MIB_INTERVAL));

        StdSchedulerFactory factory = new StdSchedulerFactory();

    	watchDogThreadHolder = factory.getScheduler();

		JobDetail job1 = JobBuilder.newJob(IntegrityJob.class)
		        .withIdentity("integrityJob", "integrity")
		        .build();

		Trigger cron1 = TriggerBuilder
		        .newTrigger()
		        .withIdentity("integrityTrigger", "integrity")
		        .startNow()
		        .withSchedule(
		            SimpleScheduleBuilder.simpleSchedule()
		                                 .withIntervalInSeconds(integrityCycle)
		                                 .repeatForever())
		        .build();

		watchDogThreadHolder.scheduleJob(job1, cron1);
		watchDogThreadHolder.start();


		logger.trace(CommonDefinitions.END);

	}

	protected void startIntegrity() {

		logger.trace(CommonDefinitions.START);

		if(!stopFlug){
			logger.info(LogFormatter.out.format(LogFormatter.MSG_307053));
			IntegrityExecutor ie = new IntegrityExecutor();
			executeThreadHolder.add(ie);
			ie.start();
		}else{
			logger.debug("Stop flug on.");
		}

		logger.trace(CommonDefinitions.END);

	}

	public void stopIntegrityCycle() {

		this.stopFlug = true;

		long timeoutLimit = Long.parseLong(EcConfiguration.getInstance().get(String.class, EcConfiguration.GATHER_MIB_STOP_TIMEOUT));
		long checkInterval = Long.parseLong(EcConfiguration.getInstance().get(String.class, EcConfiguration.GATHER_MIB_STOP_INTERVAL));

		long timeCheker = 0;

		while(true){
			if(!OperationControlManager.getInstance().isIfIntegrityExecution() || (timeCheker > timeoutLimit * 1000)){
				break;
			}else{
				CommonUtil.sleep(checkInterval);
				timeCheker += checkInterval;
			}
		}

		if(watchDogThreadHolder != null){
			try {
				watchDogThreadHolder.clear();
			} catch (SchedulerException e) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_407064),e);
			}
		}else{
		}
		watchDogThreadHolder = null;
	}

	public static synchronized boolean boot() {
		logger.trace(CommonDefinitions.START);

		if(instance == null){
			instance = new InterfaceIntegrityValidationManager();

			try{
				instance.controlIntegrityCycle();
				CommonUtil.sleep(1000);
			}catch(SchedulerException e){
				logger.error(LogFormatter.out.format(LogFormatter.MSG_507062),e);
			}

			logger.trace(CommonDefinitions.END);

			return true;
			logger.info(LogFormatter.out.format(LogFormatter.MSG_407052));
			return false;
		}
	}

	protected boolean isStopFlug() {
		return stopFlug;
	}

	public List<IntegrityExecutor> getExecuteThreadHolder() {
		return executeThreadHolder;
	}

	public static InterfaceIntegrityValidationManager getInstance() {
		return instance;
	}

	@Override
	public String toString() {
		return "InterfaceIntegrityValidationManager [stopFlug=" + stopFlug + "]";
	}

}
