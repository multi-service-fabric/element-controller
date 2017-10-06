
package msf.ecmm.ope.execute.constitution.allinfo;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.convert.LogicalPhysicalConverter.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;
import java.util.List;

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
import msf.ecmm.ope.receiver.pojo.GetPhysicalInterfaceList;

public class AllPhysicalIfInfoAcquisition extends Operation {

	private static final String ERROR_CODE_180301 = "180301";

	public AllPhysicalIfInfoAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.AllPhysicalIfInfoAcquisition);
	}

	@Override
	public AbstractResponseMessage execute() {

		logger.trace(CommonDefinitions.START);

		AbstractResponseMessage response = null;

		GetPhysicalInterfaceList outputData = null;

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
			return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_180101);
		}

		try (DBAccessManager session = new DBAccessManager()) {

			int fabricType = toIntegerNodeType(getUriKeyMap().get(KEY_FABRIC_TYPE));

			List<PhysicalIfs> physicalIfs = session.getPhysicalIfsList(fabricType, getUriKeyMap().get(KEY_NODE_ID));

			outputData = RestMapper.toPhyInInfoList(physicalIfs);

			response = makeSuccessResponse(RESP_OK_200, outputData);

		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_180301);
		}

		logger.trace(CommonDefinitions.END);

		return response;

	}

	@Override
	protected boolean checkInData() {

		logger.trace(CommonDefinitions.START);

		boolean checkResult = true;

		if (getUriKeyMap() == null) {
			checkResult = false;
		} else if (getUriKeyMap().get(KEY_FABRIC_TYPE) == null) {
			checkResult = false;
		} else if (!getUriKeyMap().get(KEY_FABRIC_TYPE).equals("leafs")
				&& !getUriKeyMap().get(KEY_FABRIC_TYPE).equals("spines")) {
			checkResult = false;
		} else if (getUriKeyMap().get(KEY_NODE_ID) == null) {
			checkResult = false;
		}

		logger.trace(CommonDefinitions.END);
		return checkResult;
	}

}
