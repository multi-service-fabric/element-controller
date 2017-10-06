
package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.convert.LogicalPhysicalConverter.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.UpdatePhysicalInterface;

public class PhysicalIfInfoChange extends Operation {

	private static final String ERROR_CODE_200102 = "200102";

	private static final String ERROR_CODE_200401 = "200401";

	public PhysicalIfInfoChange(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.PhysicalIfInfoChange);
	}

	@Override
	public AbstractResponseMessage execute() {

		logger.trace(CommonDefinitions.START);

		AbstractResponseMessage response = null;

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
			return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_200101);
		}

		try (DBAccessManager session = new DBAccessManager()) {

			session.startTransaction();

			UpdatePhysicalInterface inputData = (UpdatePhysicalInterface) getInData();

			int fabricType = toIntegerNodeType(getUriKeyMap().get(KEY_FABRIC_TYPE));

			String nodeId = getUriKeyMap().get(KEY_NODE_ID);
			String physicalIfId = getUriKeyMap().get(KEY_PHYSICAL_IF_ID);

			PhysicalIfs physicalIfs = session.searchPhysicalIfs(fabricType, nodeId, physicalIfId);
			if (physicalIfs == null) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [PhysicalIfs]"));
				return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_200201);
			}

			if (inputData.getAction().equals("speed_set")) {
				if(inputData.getSpeed() == null){
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
					return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_200101);
				}

				Equipments equpimentsDb = session.searchEquipments(physicalIfs.getNodes().getEquipments().getEquipment_type_id());
				if (equpimentsDb == null) {
					logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db."));
					return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_200102);
				}

				physicalIfs = DbMapper.toPhyIfChange(inputData, fabricType, nodeId, physicalIfId,
						equpimentsDb);
			} else {
				physicalIfs.setIf_name(null);
			}

			session.updatePhysicalIfs(physicalIfs);

			session.commit();
			response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_200401);
		} catch (IllegalArgumentException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), e);
			response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_200101);
		}

		logger.trace(CommonDefinitions.END);

		return response;
	}

	@Override
	protected boolean checkInData() {

		logger.trace(CommonDefinitions.START);

		boolean result = true;

		try {

			UpdatePhysicalInterface inputData = (UpdatePhysicalInterface) getInData();

			inputData.check(OperationType.PhysicalIfInfoChange);

		} catch (CheckDataException e) {
			logger.warn("check error :", e);
			result = false;
		}

		if (getUriKeyMap() == null) {
			result = false;
		} else if (getUriKeyMap().get(KEY_FABRIC_TYPE) == null) {
			result = false;
		} else if (!(getUriKeyMap().get(KEY_FABRIC_TYPE).equals("leafs"))) {
			result = false;
		} else if (getUriKeyMap().get(KEY_NODE_ID) == null) {
			result = false;
		} else if (getUriKeyMap().get(KEY_PHYSICAL_IF_ID) == null) {
			result = false;
		}

		logger.trace(CommonDefinitions.END);

		return result;
	}

}
