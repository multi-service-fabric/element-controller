
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
import msf.ecmm.ope.receiver.pojo.RegisterLeaf;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentRegisterNode;
import msf.ecmm.ope.receiver.pojo.parts.NodeRegisterNode;

public class LeafInfoRegistration extends NodeInfoRegistration {

	private static final String ERROR_CODE_070201 = "070201";
	private static final String ERROR_CODE_070302 = "070302";
	private static final String ERROR_CODE_070102 = "070102";

    public LeafInfoRegistration(AbstractRestMessage idt, HashMap<String, String> ukm) {
        super(idt, ukm);
		super.setOperationType(OperationType.LeafInfoRegistration);
    }

	@Override
	protected boolean checkInData() {
		logger.trace(CommonDefinitions.START);

		boolean checkResult = true;

		RegisterLeaf registerLeafRest = (RegisterLeaf) getInData();

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

		RegisterLeaf registerLeafRest = (RegisterLeaf) getInData();

		DhcpInfo dhcpInfo = new DhcpInfo(equipInfo.getConfigTemplate(), equipInfo.getInitialConfig(),
				nodeInfo.getHostName(), nodeInfo.getMacAddr(), "", nodeInfo.getNtpServerAddress(),
				nodeInfo.getManagementInterface().getAddress(), nodeInfo.getManagementInterface().getPrefix());

		logger.trace(CommonDefinitions.END);
		return dhcpInfo;
	}


	@Override
	protected void startSyslogWatch() throws DevctrlException {
		logger.trace(CommonDefinitions.START);

		RegisterLeaf registerLeafRest = (RegisterLeaf) getInData();

		SyslogController syslogController = SyslogController.getInstance();
		syslogController.monitorStart(nodeInfo.getManagementInterface().getAddress(),
				equipInfo.getBootCompleteMsg(), equipInfo.getBootErrorMsgs());

		logger.trace(CommonDefinitions.END);
	}



	@Override
	protected Nodes toNodeinfoCreate(int clusterId, Equipments equipments){
		RegisterLeaf registerLeafRest = (RegisterLeaf) getInData();
		return DbMapper.toLeafinfoCreate(registerLeafRest, clusterId, equipments);
	}

	@Override
	protected String getEquipmentTypeId() {
		RegisterLeaf registerLeafRest = (RegisterLeaf) getInData();
		return registerLeafRest.getEquipment().getEquipmentTypeId();
	}

    @Override
    protected String getInputDataErrorCode() {
    	return ERROR_CODE_070101;
    }

	@Override
	protected String getDoubleRegisterErrorCode() {
		return ERROR_CODE_070201;
	}

	@Override
	protected String getDhcpStartErrorCode() {
		return ERROR_CODE_070301;
	}

	@Override
	protected String getSyslogStartErrorCode() {
		return ERROR_CODE_070302;
	}

	@Override
	protected String getDbErrorCode() {
		return ERROR_CODE_070303;
	}

	@Override
	protected String getNotFoundSubDataErrorCode() {
		return ERROR_CODE_070102;
	}

}
