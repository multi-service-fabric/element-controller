package msf.ecmm.ope.execute.ecstate;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.ope.control.ECMainState;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.traffic.InterfaceIntegrityValidationManager;
import msf.ecmm.traffic.TrafficDataGatheringManager;

import org.hibernate.HibernateException;

public class ECMainStopper extends Operation {

	private final int WAIT_TIME = 1000;

	private final String CHANGE_OVER = "chgover";

	public ECMainStopper(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.ECMainStopper);
	}

	@Override
	public AbstractResponseMessage execute() {

		logger.trace(CommonDefinitions.START);

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));

			return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_300101);
		} else {
		}

		ECMainState state;
		if (this.getUriKeyMap().get(KEY_STOP_TYPE).equals(NORMAL_STOP)) {
			state = ECMainState.StopReady;
		} else {
			state = ECMainState.ChangeOver;
		}

		try (DBAccessManager session = new DBAccessManager()) {

			session.startTransaction();

			session.updateSystemStatus(state.getValue(), -1);

			session.commit();

		} catch (DBAccessException | HibernateException dae) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."));
		}

		synchronized (OperationControlManager.getInstance()) {
			try {
				OperationControlManager.getInstance().updateEcMainState(false, state);
			} catch (DBAccessException e) {
			}
		}

		while ((OperationControlManager.getInstance().isUnsentNodeStateNotificationSendingState())
				|| (OperationControlManager.getInstance().getNumberOfExecuteOperations() > 1)) {
			CommonUtil.sleep(WAIT_TIME);
		}

		OperationControlManager.getInstance().sendUnsentNodeStateNotification();

		TrafficDataGatheringManager.getInstance().stopGetheringCycle();

		InterfaceIntegrityValidationManager.getInstance().stopIntegrityCycle();

		AbstractResponseMessage ret = makeSuccessResponse(RESP_OK_200, new CommonResponse());

		if (state == ECMainState.StopReady) {
			try (DBAccessManager session = new DBAccessManager()) {

				session.startTransaction();

				session.updateSystemStatus(ECMainState.Stop.getValue(), -1);

				session.commit();

			} catch (DBAccessException | HibernateException dae) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."),dae);
			}
		} else {
		}

		try {
			OperationControlManager.getInstance().updateEcMainState(false, ECMainState.Stop);
		} catch (DBAccessException e) {
		}

		logger.trace(CommonDefinitions.END);

		return ret;
	}

	@Override
	protected boolean checkInData() {

		logger.trace(CommonDefinitions.START);

		boolean checkResult = true;


		if (checkResult) {
			if (getUriKeyMap() == null) {
				checkResult = false;
			} else if (getUriKeyMap().get(KEY_STOP_TYPE) == null) {
				checkResult = false;
			} else if (!getUriKeyMap().get(KEY_STOP_TYPE).equals(CHANGE_OVER)
					&& !getUriKeyMap().get(KEY_STOP_TYPE).equals(NORMAL_STOP)) {
				checkResult = false;
			}
		} else {
		}

		logger.trace(CommonDefinitions.END + ", checkResult=" + checkResult);

		return checkResult;
	}

	public static void systemExit() {
		logger.info(LogFormatter.out.format(LogFormatter.MSG_303071));
		System.exit(0);
	}
}
