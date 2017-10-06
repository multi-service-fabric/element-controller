
package msf.ecmm.ope.execute.cp;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.L3Cps;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.L3SliceAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.UpdateL3cp;

public class L3CPChange extends Operation {

	private static final String ERROR_CODE_270201 = "270201";

	private static final String ERROR_CODE_270402 = "270402";

	public L3CPChange(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.L3CPChange);
	}

	@Override
	public AbstractResponseMessage execute() {

		logger.trace(CommonDefinitions.START);

		AbstractResponseMessage response = null;

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
			return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_270101);
		}

		try (DBAccessManager session = new DBAccessManager()) {

			UpdateL3cp inputData = (UpdateL3cp) getInData();

			L3Cps l3CpsDb = session.searchL3Cps(getUriKeyMap().get(KEY_SLICE_ID), getUriKeyMap().get(KEY_CP_ID));

			if (l3CpsDb == null) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [L3Cps]"));
				return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_270201);
			}

			Integer clusterId = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.CLUSTER_ID);

			L3SliceAddDelete l3SliceAddDeleteEm = EmMapper.toL3CPChange(inputData, l3CpsDb, clusterId);

			AbstractMessage result = EmController.getInstance().request(l3SliceAddDeleteEm);

			if (!result.isResult()) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
				return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_270402);
			}

			response = makeSuccessResponse(200, new CommonResponse());

		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_270403);

		} catch (EmctrlException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), e);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_270401);
		} catch (IllegalArgumentException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), e);
			response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_270101);
		}

		logger.trace(CommonDefinitions.END);

		return response;

	}

	@Override
	protected boolean checkInData() {

		logger.trace(CommonDefinitions.START);

		boolean result = true;

		try {

			UpdateL3cp inputData = (UpdateL3cp) getInData();

			inputData.check(OperationType.L3CPChange);

		} catch (CheckDataException e) {
			logger.warn("check error :", e);
			result = false;
		}

		if (getUriKeyMap().get(KEY_SLICE_ID) == null) {
			result = false;
		} else if (getUriKeyMap().get(KEY_CP_ID) == null) {
			result = false;
		}

		logger.trace(CommonDefinitions.END);

		return result;
	}
}
