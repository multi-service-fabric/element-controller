
package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.Equipment;

public class RegisterEquipmentType extends AbstractRestMessage {

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	@Override
	public String toString() {
		return "RegiterEquipmentType [equipment=" + equipment + "]";
	}

	public void check(OperationType ope) throws CheckDataException {

		if (equipment == null) {
			throw new CheckDataException();
		} else {
			equipment.check(ope);
		}
	}
}
