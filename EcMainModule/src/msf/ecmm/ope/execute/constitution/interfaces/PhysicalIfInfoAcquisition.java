
package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.convert.LogicalPhysicalConverter.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetPhysicalInterface;

public class PhysicalIfInfoAcquisition extends Operation {

	private static final String ERROR_CODE_190201 = "190201";

	public PhysicalIfInfoAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.PhysicalIfInfoAcquisition);
	}

	@Override
	public AbstractResponseMessage execute() {

		logger.trace(CommonDefinitions.START);

		GetPhysicalInterface getPhysicalInterfaceRest = null;

		AbstractResponseMessage response = null;

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
			return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_190101);
		}

		try (DBAccessManager session = new DBAccessManager()) {

			int fabricType = toIntegerNodeType(getUriKeyMap().get(KEY_FABRIC_TYPE));

			PhysicalIfs physicalIfsDb = session.searchPhysicalIfs(fabricType, getUriKeyMap().get(KEY_NODE_ID),
					getUriKeyMap().get(KEY_PHYSICAL_IF_ID));

			if (physicalIfsDb == null) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [PhysicalIfs]"));
				return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_190201);
			}
			getPhysicalInterfaceRest = RestMapper.toPhyInInfo(physicalIfsDb);

			response = makeSuccessResponse(RESP_OK_200, getPhysicalInterfaceRest);

		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_190401);
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
		} else {
			if (getUriKeyMap().get(KEY_FABRIC_TYPE) == null) {
				result = false;
			} else {

				String fabricType = getUriKeyMap().get(KEY_FABRIC_TYPE);

				if (!fabricType.equals("leafs") && !fabricType.equals("spines")) {
					result = false;
				} else if (getUriKeyMap().get(KEY_NODE_ID) == null) {
					result = false;
				} else if (getUriKeyMap().get(KEY_PHYSICAL_IF_ID) == null) {
					result = false;
				}
			}
		}

		logger.trace(CommonDefinitions.END);

		return result;
	}
}
