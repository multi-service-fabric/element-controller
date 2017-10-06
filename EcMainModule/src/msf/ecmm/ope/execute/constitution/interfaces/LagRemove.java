
package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.convert.LogicalPhysicalConverter.*;
import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.CpsList;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.CeLagAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

public class LagRemove extends Operation {

	private static final String ERROR_CODE_260201 = "260201";

	private static final String ERROR_CODE_260402 = "260402";

	private static final String ERROR_CODE_260404 = "260404";

	public LagRemove(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.LagRemove);
	}

	@Override
	public AbstractResponseMessage execute() {

		logger.trace(CommonDefinitions.START);

		AbstractResponseMessage response = null;

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
			return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_260101);
		}

		try (DBAccessManager session = new DBAccessManager()) {

			session.startTransaction();

			int fabricType = toIntegerNodeType(getUriKeyMap().get(KEY_FABRIC_TYPE));

			String nodeId = getUriKeyMap().get(KEY_NODE_ID);

			String lagIfId = getUriKeyMap().get(KEY_LAG_IF_ID);

			LagIfs lagIfsDb = session.searchLagIfs(fabricType, nodeId, lagIfId);

			if (lagIfsDb == null) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [LagIfs]"));
				return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_260201);
			}

			CpsList cpsListDb = session.getCpsList(fabricType, nodeId, lagIfsDb.getIf_name());

			if ((!cpsListDb.getL2CpsList().isEmpty()) || (!cpsListDb.getL3CpsList().isEmpty())) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "CP data is found."));
				return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_260302);
			}

			session.deleteLagIfs(fabricType, nodeId, lagIfId);

			CeLagAddDelete ceLagAddDeleteEm = EmMapper.toLagIfDelete(lagIfsDb);

			AbstractMessage result = EmController.getInstance().request(ceLagAddDeleteEm);

			if (!result.isResult()) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
				return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_260404);
			}

			session.commit();

			response = makeSuccessResponse(RESP_NOCONTENTS_204, new CommonResponse());

		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
			switch (e.getCode()) {
			case NO_DELETE_TARGET:
				response = makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_260201);
				break;
			case COMMIT_FAILURE:
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900405);
				break;
			default:
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_260403);
				break;
			}
		} catch (EmctrlException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), e);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_260402);
		} catch (IllegalArgumentException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), e);
			response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_260101);
		}

		logger.trace(CommonDefinitions.END);

		return response;
	}

	@Override
	protected boolean checkInData() {

		logger.trace(CommonDefinitions.START);

		boolean result = true;

		if (getUriKeyMap() == null) {
			result = false;
		} else if (getUriKeyMap().get(KEY_FABRIC_TYPE) == null
				|| (!getUriKeyMap().get(KEY_FABRIC_TYPE).equals("leafs"))) {
			result = false;
		} else if (getUriKeyMap().get(KEY_NODE_ID) == null) {
			result = false;
		} else if (getUriKeyMap().get(KEY_LAG_IF_ID) == null) {
			result = false;
		}

		logger.trace(CommonDefinitions.END);

		return result;
	}

}
