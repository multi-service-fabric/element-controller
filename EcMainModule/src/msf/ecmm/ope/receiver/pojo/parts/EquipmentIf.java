
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class EquipmentIf {

	private String ifSlot;

	public String getPhysicalIfId() {
		return physicalIfId;
	}

	public void setPhysicalIfId(String physicalIfId) {
		this.physicalIfId = physicalIfId;
	}

	public String getIfSlot() {
		return ifSlot;
	}

	public void setIfSlot(String ifSlot) {
		this.ifSlot = ifSlot;
	}

	@Override
	public String toString() {
		return "EquipmentIf [physicalIfId=" + physicalIfId + ", ifSlot=" + ifSlot + "]";
	}

	public void check(OperationType operationType) throws CheckDataException {
		if (physicalIfId == null) {
			throw new CheckDataException();
		}
		if (ifSlot == null) {
			throw new CheckDataException();
		}
	}

}
