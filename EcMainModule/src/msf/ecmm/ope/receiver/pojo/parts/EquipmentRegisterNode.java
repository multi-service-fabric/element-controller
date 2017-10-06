
package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class EquipmentRegisterNode {

	private String configTemplate;

	private String bootCompleteMsg;

	public String getEquipmentTypeId() {
		return equipmentTypeId;
	}

	public void setEquipmentTypeId(String equipmentTypeId) {
		this.equipmentTypeId = equipmentTypeId;
	}

	public String getConfigTemplate() {
		return configTemplate;
	}

	public void setConfigTemplate(String configTemplate) {
		this.configTemplate = configTemplate;
	}

	public String getInitialConfig() {
		return initialConfig;
	}

	public void setInitialConfig(String initialConfig) {
		this.initialConfig = initialConfig;
	}

	public String getBootCompleteMsg() {
		return bootCompleteMsg;
	}

	public void setBootCompleteMsg(String bootCompleteMsg) {
		this.bootCompleteMsg = bootCompleteMsg;
	}

	public ArrayList<String> getBootErrorMsgs() {
		return bootErrorMsgs;
	}

	public void setBootErrorMsgs(ArrayList<String> bootErrorMsgs) {
		this.bootErrorMsgs = bootErrorMsgs;
	}

	@Override
	public String toString() {
		return "EquipmentRegisterNode [equipmentTypeId=" + equipmentTypeId + ", configTemplate=" + configTemplate
				+ ", initialConfig=" + initialConfig + ", bootCompleteMsg=" + bootCompleteMsg + ", bootErrorMsgs="
				+ bootErrorMsgs + "]";
	}

	public void check(OperationType ope) throws CheckDataException {

		if (equipmentTypeId == null) {
			throw new CheckDataException();
		}
		if (configTemplate == null) {
			throw new CheckDataException();
		}
		if (initialConfig == null) {
			throw new CheckDataException();
		}
		if (bootCompleteMsg == null) {
			throw new CheckDataException();
		}
	}

}
