
package msf.ecmm.ope.execute.notification;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.CpsList;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.SnmpController;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.Operations;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.NotifyReceiveSnmpTrap;

public class SNMPTrapSignalRecieveNotification extends Operation {

	private static final String ERROR_CODE_330301 = "330301";

	private static final String ERROR_CODE_330303 = "330303";

	private static final String ERROR_CODE_330305 = "330305";

	public SNMPTrapSignalRecieveNotification(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
		super.setOperationType(OperationType.SNMPTrapSignalRecieveNotification);
	}

	@Override
	public AbstractResponseMessage execute() {
		logger.trace(CommonDefinitions.START);

		AbstractResponseMessage response = null;

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
			return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_330101);
		}

		try (DBAccessManager session = new DBAccessManager()) {

			SnmpController snmpController = new SnmpController();

			NotifyReceiveSnmpTrap snmpTrap = (NotifyReceiveSnmpTrap) getInData();

			Nodes nodesDb = session.searchNodes(-1, null, snmpTrap.getSrcHostIp());

			if (nodesDb == null) {
				logger.debug("There is no node info in DB.");
				return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_330303);
			}

			String ifName = "";
			if (nodesDb.getEquipments().getSnmptrap_if_name_oid() == null) {
				int ifIndex = SnmpController.getIfIndexForTrap(((NotifyReceiveSnmpTrap) getInData()).getVarbind());

				if (ifIndex == SnmpController.IFINDEX_NOT_FOUND) {
					logger.debug("Return values from SNMP is wrong.");
					return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_330304);
				} else {
				}

				ifName = snmpController.getIfName(nodesDb.getEquipments(), nodesDb, ifIndex);

			} else {
				ifName = SnmpController.getIfNameForTrap(((NotifyReceiveSnmpTrap) getInData()).getVarbind(), nodesDb
						.getEquipments().getSnmptrap_if_name_oid());
			}

			boolean pyhsFlug = false;
			for (PhysicalIfs ifinfo : nodesDb.getPhysicalIfsList()) {
				if ((ifinfo.getIf_name() != null)&&(ifinfo.getIf_name().equals(ifName))) {
					pyhsFlug = true;
					break;
				} else {
				}
			}

			CpsList cpsList = session.getCpsList(nodesDb.getNode_type(), nodesDb.getNode_id(), ifName);
			LagIfs internalIF = null;

			if (!pyhsFlug) {
				if ((cpsList.getL2CpsList().isEmpty()) && (cpsList.getL3CpsList().isEmpty())) {
					for (LagIfs ifinfo : nodesDb.getLagIfsList()) {
						if ((ifinfo.getIf_name() != null)&&(ifinfo.getIf_name().equals(ifName))) {
							internalIF = ifinfo;
							break;
						} else {
						}
					}
				} else {
				}
			} else {
			}

			Operations snmpTrapData = RestMapper.toSnmpTrapNotificationInfo(nodesDb, cpsList, internalIF,getUriKeyMap().get(KEY_LINK_STATUS));

			if (snmpTrapData == null) {
				logger.debug("Data Mapping error");
				return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_330399);
			}

			RestClient resultFC = new RestClient();
			resultFC.request(RestClient.OPERATION, new HashMap<String, String>(), snmpTrapData,
					CommonResponseFromFc.class);

			response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

		} catch (DBAccessException dbe) {
			logger.debug("DB access error", dbe);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_330301);
		} catch (DevctrlException de) {
			logger.debug("SNMP error", de);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_330305);
		} catch (RestClientException re) {
			logger.debug("Rest request failed", re);
			response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_330302);
		}

		logger.trace(CommonDefinitions.END);

		return response;
	}

	@Override
	protected boolean checkInData() {
		logger.trace(CommonDefinitions.START);

		boolean checkResult = true;

		NotifyReceiveSnmpTrap notifyRecieveSnmpTrapRest = (NotifyReceiveSnmpTrap) getInData();

		if (getUriKeyMap() == null) {
			checkResult = false;
		}else if ((!getUriKeyMap().containsKey(KEY_LINK_STATUS))
				|| ((!getUriKeyMap().get(KEY_LINK_STATUS).equals("linkup")) && (!getUriKeyMap().get(KEY_LINK_STATUS).equals("linkdown")))) {
			checkResult = false;
		} else {
			try {
				notifyRecieveSnmpTrapRest.check(getOperationType());
			} catch (CheckDataException e) {
				logger.warn("check error :", e);
				checkResult = false;
			}
		}

		logger.trace(CommonDefinitions.END);
		return checkResult;
	}

}
