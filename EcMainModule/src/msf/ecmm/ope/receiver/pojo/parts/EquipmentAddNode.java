
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class EquipmentAddNode {

	private String platform;

	private String firmware;

	public String getEquipmentTypeId() {
		return equipmentTypeId;
	}

	public void setEquipmentTypeId(String equipmentTypeId) {
		this.equipmentTypeId = equipmentTypeId;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getFirmware() {
		return firmware;
	}

	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}

	@Override
	public String toString() {
		return "EquipmentAddNode [equipmentTypeId=" + equipmentTypeId + ", platform=" + platform + ", os=" + os
				+ ", firmware=" + firmware + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (equipmentTypeId == null) {
			throw new CheckDataException();
		}
		if (platform == null) {
			throw new CheckDataException();
		}
		if (os == null) {
			throw new CheckDataException();
		}
		if (firmware == null) {
			throw new CheckDataException();
		}
	}

}
