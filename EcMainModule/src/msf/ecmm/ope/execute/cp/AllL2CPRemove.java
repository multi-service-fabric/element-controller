
package msf.ecmm.ope.execute.cp;

import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.L2Cps;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.L2SliceAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.BulkDeleteL2cp;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.parts.CpsDeleteCps;

public class AllL2CPRemove extends Operation {

	private static final String ERROR_CODE_010201 = "010201";

	private static final String ERROR_CODE_010402 = "010402";

	private static final String ERROR_CODE_900404 = "900404";

	public AllL2CPRemove(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.AllL2CPRemove);
	}

	@Override
	public AbstractResponseMessage execute() {

		logger.trace(CommonDefinitions.START);

		AbstractResponseMessage response = null;

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
			return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
		}

		try (DBAccessManager session = new DBAccessManager()) {

			session.startTransaction();

			BulkDeleteL2cp inputData = (BulkDeleteL2cp) getInData();

			List<L2Cps> l2CpsDbList = new ArrayList<L2Cps>();

			for (CpsDeleteCps cps : inputData.getCps()) {

				L2Cps l2CpsDb = session.searchL2Cps(inputData.getSliceId(), cps.getCpId());

				if (l2CpsDb == null) {
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [L2Cps]"));
					return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_010201);
				}

				l2CpsDbList.add(l2CpsDb);
			}

			session.deleteL2Cps(l2CpsDbList);

			L2SliceAddDelete l2SliceAddDeleteEm = EmMapper.toL2CPDelete(inputData, l2CpsDbList);

			AbstractMessage result = EmController.getInstance().request(l2SliceAddDeleteEm);

			if (!result.isResult()) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
				return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010402);
			}

			session.commit();

			response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
			switch (e.getCode()) {
			case NO_DELETE_TARGET:
				response = makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_010201);
				break;
			case COMMIT_FAILURE:
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900404);
				break;
			default:
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010403);
				break;
			}
		} catch (EmctrlException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), e);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010401);
		} catch (IllegalArgumentException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), e);
			response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
		}

		logger.trace(CommonDefinitions.END);

		return response;
	}

	@Override
	protected boolean checkInData() {

		logger.trace(CommonDefinitions.START);

		boolean result = true;

		try {

			BulkDeleteL2cp inputData = (BulkDeleteL2cp) getInData();

			inputData.check(OperationType.AllL2CPRemove);

		} catch (CheckDataException e) {
			logger.warn("check error :", e);
			result = false;
		}

		logger.trace(CommonDefinitions.END);

		return result;
	}

}
