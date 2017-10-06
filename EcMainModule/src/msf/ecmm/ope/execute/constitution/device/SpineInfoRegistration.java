
package msf.ecmm.ope.execute.constitution.device;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.SyslogController;
import msf.ecmm.devctrl.pojo.DhcpInfo;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.RegisterSpine;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentRegisterNode;
import msf.ecmm.ope.receiver.pojo.parts.NodeRegisterNode;

public class SpineInfoRegistration extends NodeInfoRegistration {

	private static final String ERROR_CODE_120201 = "120201";
	private static final String ERROR_CODE_120302 = "120302";
	private static final String ERROR_CODE_120102 = "120102";

    public SpineInfoRegistration(AbstractRestMessage idt, HashMap<String, String> ukm) {
        super(idt, ukm);
		super.setOperationType(OperationType.SpineInfoRegistration);
    }

	@Override
	protected boolean checkInData() {
		logger.trace(CommonDefinitions.START);

		boolean checkResult = true;

		RegisterSpine registerLeafRest = (RegisterSpine) getInData();

		try {
			registerLeafRest.check(getOperationType());
		} catch (CheckDataException e) {
			logger.warn("check error :", e);
			checkResult = false;
		}

		logger.trace(CommonDefinitions.END);

		return checkResult;
    }

	@Override
	protected DhcpInfo createDhcpInfo() {
		logger.trace(CommonDefinitions.START);

		RegisterSpine registerSpineRest = (RegisterSpine) getInData();

		DhcpInfo dhcpInfo = new DhcpInfo(equipInfo.getConfigTemplate(), equipInfo.getInitialConfig(),
				nodeInfo.getHostName(), nodeInfo.getMacAddr(), "", nodeInfo.getNtpServerAddress(),
				nodeInfo.getManagementInterface().getAddress(), nodeInfo.getManagementInterface().getPrefix());

		logger.trace(CommonDefinitions.END);
		return dhcpInfo;
	}


	@Override
	protected void startSyslogWatch() throws DevctrlException {
		logger.trace(CommonDefinitions.START);

		RegisterSpine registerSpineRest = (RegisterSpine) getInData();

		SyslogController syslogController = SyslogController.getInstance();
		syslogController.monitorStart(nodeInfo.getManagementInterface().getAddress(),
				equipInfo.getBootCompleteMsg(), equipInfo.getBootErrorMsgs());

		logger.trace(CommonDefinitions.END);
	}

	@Override
	protected String getEquipmentTypeId() {
		RegisterSpine registerSpineRest = (RegisterSpine) getInData();
		return registerSpineRest.getEquipment().getEquipmentTypeId();
	}

	@Override
	protected Nodes toNodeinfoCreate(int clusterId, Equipments equipments) {
		RegisterSpine registerSpineRest = (RegisterSpine) getInData();
		return DbMapper.toSpineinfoCreate(registerSpineRest, clusterId, equipments);
	}

    @Override
    protected String getInputDataErrorCode() {
    	return ERROR_CODE_120101;
    }

	@Override
	protected String getDoubleRegisterErrorCode() {
		return ERROR_CODE_120201;
	}

	@Override
	protected String getDhcpStartErrorCode() {
		return ERROR_CODE_120301;
	}

	@Override
	protected String getSyslogStartErrorCode() {
		return ERROR_CODE_120302;
	}

	@Override
	protected String getDbErrorCode() {
		return ERROR_CODE_120303;
	}

	@Override
	protected String getNotFoundSubDataErrorCode() {
		return ERROR_CODE_120102;
	}
}
