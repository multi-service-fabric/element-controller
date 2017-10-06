package msf.ecmm.ope.execute.ecstate;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.devctrl.DhcpController;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.ope.control.ECMainState;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.receiver.RestServer;
import msf.ecmm.traffic.InterfaceIntegrityValidationManager;
import msf.ecmm.traffic.TrafficDataGatheringManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ECMainStarter {

	private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	public static void main(String[] args) {

		logger.info(LogFormatter.out.format(LogFormatter.MSG_303050));

		try {
			EcConfiguration.getInstance().read(args[0]);
		} catch (Exception e) {
			logger.error(LogFormatter.out.format(LogFormatter.MSG_503038,e));
			System.exit(1);
			return;
		}

		OperationControlManager check1 = OperationControlManager.boot();
		if(check1 == null){
			logger.error(LogFormatter.out.format(LogFormatter.MSG_503039,"OperationControlManager"));
			System.exit(1);
			return;
		}else{
		}

		boolean check2 = RestServer.initialize();
		if(!check2){
			logger.error(LogFormatter.out.format(LogFormatter.MSG_503039,"RestServer"));
			try{
				if(OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver){
					logger.debug("EC main module state is ChangeOver.");
				}else{
					logger.debug("EC main module state is StartReady.");
				}
				OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
				OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
			}catch(Exception e){
				logger.error(LogFormatter.out.format(LogFormatter.MSG_503068),e);
			}
			System.exit(1);
			return;
		}else{
		}

		boolean check3 = InterfaceIntegrityValidationManager.boot();
		if(!check3){
			logger.error(LogFormatter.out.format(LogFormatter.MSG_503039,"InterfaceIntegrityValidationManager"));
			try{
				if(OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver){
					logger.debug("EC main module state is ChangeOver.");
				}else{
					logger.debug("EC main module state is StartReady.");
				}
				OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
				OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
			}catch(Exception e){
				logger.error(LogFormatter.out.format(LogFormatter.MSG_503068),e);
			}
			System.exit(1);
			return;
		}else{
		}

		boolean check4 = TrafficDataGatheringManager.boot();
		if(!check4){
			logger.error(LogFormatter.out.format(LogFormatter.MSG_503039,"TrafficDataGatheringManager"));
			try{
				if(OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver){
					logger.debug("EC main module state is ChangeOver.");
				}else{
					logger.debug("EC main module state is StartReady.");
				}
				OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
				OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
			}catch(Exception e){
				logger.error(LogFormatter.out.format(LogFormatter.MSG_503068),e);
			}
			System.exit(1);
			return;
		}else{
		}

		boolean check5 = EmController.getInstance().initialize();
		if(!check5){
			logger.error(LogFormatter.out.format(LogFormatter.MSG_503039,"EmController"));
			try{
				if(OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver){
					logger.debug("EC main module state is ChangeOver.");
				}else{
					logger.debug("EC main module state is StartReady.");
				}
				OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
				OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
			}catch(Exception e){
				logger.error(LogFormatter.out.format(LogFormatter.MSG_503068),e);
			}
			System.exit(1);
			return;
		}else{
		}

		boolean check6 = DhcpController.getInstance().initialize();
		if(!check6){
			logger.error(LogFormatter.out.format(LogFormatter.MSG_503039,"DhcpController"));
			try{
				if(OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver){
					logger.debug("EC main module state is ChangeOver.");
				}else{
					logger.debug("EC main module state is StartReady.");
				}
				OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
				OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
			}catch(Exception e){
				logger.error(LogFormatter.out.format(LogFormatter.MSG_503068),e);
			}
			System.exit(1);
			return;
		}else{
		}

		try{
			OperationControlManager.getInstance().updateEcMainState(true, ECMainState.InService);
		}catch(DBAccessException e){
			logger.error(LogFormatter.out.format(LogFormatter.MSG_503039,"State updete to in-service"));
			try{
				if(OperationControlManager.getInstance().getEcMainState(false) == ECMainState.ChangeOver){
					logger.debug("EC main module state is ChangeOver.");
				}else{
					logger.debug("EC main module state is StartReady.");
				}
				OperationControlManager.getInstance().updateEcMainState(true, ECMainState.StopReady);
				OperationControlManager.getInstance().updateEcMainState(true, ECMainState.Stop);
			}catch(Exception e1){
				logger.error(LogFormatter.out.format(LogFormatter.MSG_503068),e);
			}
			System.exit(1);
			return;
		}

		OperationControlManager.getInstance().startSendingUnsentNodeStateNotification();

		logger.info(LogFormatter.out.format(LogFormatter.MSG_303067));

		while(nonstop){
			CommonUtil.sleep(10000);
		}
	}

}
