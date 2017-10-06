package msf.ecmm.traffic;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class IntegrityJob implements Job{

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		InterfaceIntegrityValidationManager.getInstance().startIntegrity();
	}

}
