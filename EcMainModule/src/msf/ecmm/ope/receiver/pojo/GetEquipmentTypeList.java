
package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.Equipment;

public class GetEquipmentTypeList extends AbstractResponseMessage {

	public ArrayList<Equipment> getEquipment() {
		return equipments;
	}

	public void setEquipment(ArrayList<Equipment> equipments) {
		this.equipments = equipments;
	}

	@Override
	public String toString() {
		return "GetEquipmentTypeList [equipments=" + equipments + "]";
	}

}
