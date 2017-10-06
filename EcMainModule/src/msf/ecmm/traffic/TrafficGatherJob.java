package msf.ecmm.traffic;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TrafficGatherJob implements Job{

	private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		if(TrafficDataGatheringManager.getInstance().getExecuteThreadHolder().isEmpty()){
			TrafficDataGatheringManager.getInstance().startGathering();
		}else{
			   logger.warn(LogFormatter.out.format(LogFormatter.MSG_407066));
		}
	}

}
