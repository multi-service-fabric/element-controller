
package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.devctrl.DevctrlCommon;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.DhcpController;
import msf.ecmm.devctrl.pojo.DhcpInfo;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

public abstract class NodeInfoRegistration extends Operation {

	public NodeInfoRegistration(AbstractRestMessage idt, HashMap<String, String> ukm) {
		super(idt, ukm);
	}

	@Override
	public AbstractResponseMessage execute() {
		logger.trace(CommonDefinitions.START);

		AbstractResponseMessage response = null;

		if (!checkInData()) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
			return makeFailedResponse(RESP_BADREQUEST_400, getInputDataErrorCode());
		}

		boolean dhcpOkFlag = false;

		try (DBAccessManager session = new DBAccessManager()) {

			DhcpInfo dhcpInfo = createDhcpInfo();
			DhcpController dhcpController = DhcpController.getInstance();
			dhcpController.start(dhcpInfo);
			dhcpOkFlag = true;

			startSyslogWatch();

			int clusterId = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.CLUSTER_ID);

			Equipments equipments = session.searchEquipments(getEquipmentTypeId());
			if (equipments == null) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Equipments]"));
				return makeFailedResponse(RESP_BADREQUEST_400, getNotFoundSubDataErrorCode());
			}

			Nodes nodesDb = toNodeinfoCreate(clusterId, equipments);

			session.startTransaction();

			session.addNodes(nodesDb);

			session.commit();

			response = makeSuccessResponse(RESP_CREATED_201, new CommonResponse());

			needCleanUpFlag = false;

		} catch (DevctrlException e) {
			if (dhcpOkFlag == false) {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "DHCP startup was failed."), e);
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getDhcpStartErrorCode());
			} else {
				logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Watch syslog was failed."),e );
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getSyslogStartErrorCode());
			}
		} catch (DBAccessException e) {
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), e);
			if (e.getCode() == DBAccessException.DOUBLE_REGISTRATION) {
				response = makeFailedResponse(RESP_CONFLICT_409, getDoubleRegisterErrorCode());
			} else {
				response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, getDbErrorCode());
			}
		} finally {
			if (needCleanUpFlag == true) {
				DevctrlCommon.cleanUp();
			}
		}

		logger.trace(CommonDefinitions.END);
		return response;
    }

	@Override
	protected abstract boolean checkInData();

	protected abstract DhcpInfo createDhcpInfo();

	protected abstract void startSyslogWatch() throws DevctrlException;

	protected abstract String getEquipmentTypeId();

	protected abstract Nodes toNodeinfoCreate(int clusterId, Equipments equipments);

    protected abstract String getInputDataErrorCode();

    protected abstract String getDoubleRegisterErrorCode();

    protected abstract String getDhcpStartErrorCode();

    protected abstract String getSyslogStartErrorCode();

    protected abstract String getDbErrorCode();

	protected abstract String getNotFoundSubDataErrorCode();

}
