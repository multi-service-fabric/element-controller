package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.convert.LogicalPhysicalConverter.*;
import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.CeLagAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.CreateLagInterface;

public class LagCreate extends Operation {

	private static final String ERROR_CODE_230102 = "230102";

	private static final String ERROR_CODE_230302 = "230302";

	private static final String ERROR_CODE_230304 = "230304";

	public LagCreate(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.LagCreate);
	}

	@Override
	public AbstractResponseMessage execute() {

		logger.trace(CommonDefinitions.START);

		AbstractResponseMessage response = null;

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
			return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_230101);
		}

		try (DBAccessManager session = new DBAccessManager()) {

			session.startTransaction();

			CreateLagInterface inputData = (CreateLagInterface) getInData();

			int fabricType = toIntegerNodeType(getUriKeyMap().get(KEY_FABRIC_TYPE));

			Nodes nodesDb = session.searchNodes(fabricType, getUriKeyMap().get(KEY_NODE_ID), null);

			if (nodesDb == null) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db."));
				return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_230102);
			}

			LagIfs lagIfsDb = DbMapper.toLagIfCreate(nodesDb, inputData);

			session.addLagIfs(lagIfsDb);

			CeLagAddDelete ceLagAddDeleteEm = EmMapper.toLagIfCreate(nodesDb, inputData);

			AbstractMessage result = EmController.getInstance().request(ceLagAddDeleteEm);

			if (!result.isResult()) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
				return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_230304);
			}

			session.commit();

			response = makeSuccessResponse(RESP_CREATED_201, new CommonResponse());

		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
			switch (e.getCode()) {
			case DOUBLE_REGISTRATION:
				response = makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_230201);
				break;
			case COMMIT_FAILURE:
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900305);
				break;
			default:
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_230303);
				break;
			}
		} catch (EmctrlException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), e);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_230302);

		} catch (IllegalArgumentException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), e);
			response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_230101);
		}

		logger.trace(CommonDefinitions.END);

		return response;
	}

	@Override
	protected boolean checkInData() {

		logger.trace(CommonDefinitions.START);

		boolean result = true;

		try {

			CreateLagInterface inputData = (CreateLagInterface) getInData();

			inputData.check(OperationType.LagCreate);

		} catch (CheckDataException e) {
			logger.warn("check error :", e);
			result = false;
		}

		if (result) {
			if (getUriKeyMap() == null) {
				result = false;
			} else if (getUriKeyMap().get(KEY_NODE_ID) == null) {
				result = false;
			} else if (getUriKeyMap().get(KEY_FABRIC_TYPE) == null || (!getUriKeyMap().get(KEY_FABRIC_TYPE).equals("leafs"))) {
				result = false;
			}
		}

		logger.trace(CommonDefinitions.END);

		return result;
	}

}
