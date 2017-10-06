
package msf.ecmm.ope.execute.cp;

import static msf.ecmm.convert.LogicalPhysicalConverter.*;
import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.L3Cps;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.L3SliceAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.BulkCreateL3cp;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.parts.CpsCreateL3cp;

public class AllL3CPCreate extends Operation {

	private static final String ERROR_CODE_010102 = "010102";

	private static final String ERROR_CODE_010401 = "010401";

	private static final String ERROR_CODE_010403 = "010403";

	public AllL3CPCreate(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.AllL3CPCreate);
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

			BulkCreateL3cp inputData = (BulkCreateL3cp) getInData();

			List<Nodes> nodesDbList = new ArrayList<Nodes>();

			for (CpsCreateL3cp cps : inputData.getCps()) {

				int fabricType = toIntegerNodeType(cps.getBaseIf().getNodeType());

				Nodes nodesDb = session.searchNodes(fabricType, cps.getBaseIf().getNodeId(), null);

				if (nodesDb == null) {
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
					return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010102);
				}

				nodesDbList.add(nodesDb);

			}

			Integer clusterId = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.CLUSTER_ID);

			List<L3Cps> l3CpsDbList = DbMapper.toL3CPCreate(inputData, nodesDbList);

			session.addL3Cps(l3CpsDbList);

			L3SliceAddDelete l3SliceAddDeleteEm = EmMapper.toL3CPCreate(inputData, nodesDbList, clusterId);

			AbstractMessage result = EmController.getInstance().request(l3SliceAddDeleteEm);

			if (!result.isResult()) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
				return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010402);
			}

			session.commit();

			response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed.") ,e);
			switch (e.getCode()) {
			case DOUBLE_REGISTRATION:
				response = makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_010301);
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
			BulkCreateL3cp inputData = (BulkCreateL3cp) getInData();

			inputData.check(OperationType.AllL2CPCreate);
		} catch (CheckDataException e) {
			logger.warn("check error :", e);
			result = false;
		}

		logger.trace(CommonDefinitions.END);
		return result;
	}

}
