package msf.ecmm.ope.execute.ecstate;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

import org.hibernate.HibernateException;

public class ObstructionStateController extends Operation {

	private static final String ERROR_CODE_320301 = "320301";

	public ObstructionStateController(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.ObstructionStateController);
	}

	@Override
	public AbstractResponseMessage execute() {

		logger.trace(CommonDefinitions.START);

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));

			return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_320101);
		} else {
		}

		boolean obst;
		if (this.getUriKeyMap().get(KEY_INSTRUCTION_TYPE).equals(CommonDefinitions.EC_BUSY_STRING)) {
			obst = true;
		} else {
			obst = false;
		}

		try {
			OperationControlManager.getInstance().updateobstructionState(true, obst);
		} catch (DBAccessException | HibernateException dae) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."),dae);

			return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_320301);
		}

		logger.trace(CommonDefinitions.END);

		return makeSuccessResponse(RESP_OK_200, new CommonResponse());
	}

	@Override
	protected boolean checkInData() {

		logger.trace(CommonDefinitions.START);

		boolean checkResult = true;


		if (checkResult) {
			if (getUriKeyMap() == null) {
				checkResult = false;
			} else if (getUriKeyMap().get(KEY_INSTRUCTION_TYPE) == null) {
				checkResult = false;
			} else if (!getUriKeyMap().get(KEY_INSTRUCTION_TYPE).equals(CommonDefinitions.EC_BUSY_STRING)
					&& !getUriKeyMap().get(KEY_INSTRUCTION_TYPE).equals(CommonDefinitions.EC_IN_SERVICE_STRING)) {
				checkResult = false;
			}
		} else {
		}

		logger.trace(CommonDefinitions.END + ", checkResult=" + checkResult);

		return checkResult;
	}

}
