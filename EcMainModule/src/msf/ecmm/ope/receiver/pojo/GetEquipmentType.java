
package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.Equipment;

public class GetEquipmentType extends AbstractResponseMessage {

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	@Override
	public String toString() {
		return "GetEquipmentType [equipment=" + equipment + "]";
	}

}
