package msf.ecmm.ope.execute.ecstate;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.ope.control.ECMainState;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckEcMainModuleStatus;

public class ECMainStateConfirm extends Operation {

	private boolean ecMainObstraction;

	public ECMainStateConfirm(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.ECMainStateConfirm);
	}

	@Override
	public AbstractResponseMessage execute() {

		logger.trace(CommonDefinitions.START);

		try {
			ecMainState = OperationControlManager.getInstance().getEcMainState(false);
			ecMainObstraction = OperationControlManager.getInstance().getEcMainObstraction(false);
		} catch (DBAccessException e) {
		}

		logger.trace(CommonDefinitions.END);

		return makeSuccessResponse(RESP_OK_200,new CheckEcMainModuleStatus());
	}

	@Override
	protected boolean checkInData() {
		return true;
	}

	@Override
	protected AbstractResponseMessage makeSuccessResponse(int rescode,AbstractResponseMessage res){

		logger.debug("rescode=" + String.valueOf(rescode) + ", res=" + res);

		CheckEcMainModuleStatus conv = (CheckEcMainModuleStatus) res;

		conv.setStatus(ECMainState.ecMainStateToLabel(ecMainState));

		if(ecMainObstraction){
			conv.setBusy(CommonDefinitions.EC_BUSY_STRING);
		}else{
			conv.setBusy(CommonDefinitions.EC_IN_SERVICE_STRING);
		}

		conv.setResponseCode(rescode);

		logger.debug(conv);

		return conv;
	}

}
