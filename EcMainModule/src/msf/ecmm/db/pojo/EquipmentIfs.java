package msf.ecmm.db.pojo;

import java.io.Serializable;

public class EquipmentIfs implements Serializable {

	private String physical_if_id = null;
	private Equipments equipments;

	public EquipmentIfs() {
		super();
	}

	public String getEquipment_type_id() {
	    return equipment_type_id;
	}

	public void setEquipment_type_id(String equipment_type_id) {
	    this.equipment_type_id = equipment_type_id;
	}

	public String getPhysical_if_id() {
	    return physical_if_id;
	}

	public void setPhysical_if_id(String physical_if_id) {
	    this.physical_if_id = physical_if_id;
	}

	public String getIf_slot() {
	    return if_slot;
	}

	public void setIf_slot(String if_slot) {
	    this.if_slot = if_slot;
	}

	public Equipments getEquipments() {
	    return equipments;
	}

	public void setEquipments(Equipments equipments) {
	    this.equipments = equipments;
	}

	@Override
	public synchronized int hashCode() {
		int hashCode=0;
		if (equipment_type_id != null) {
			hashCode ^= equipment_type_id.hashCode();
		}
		if (physical_if_id != null) {
			hashCode ^= physical_if_id.hashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}

		if(obj == null || this.hashCode() != obj.hashCode()){
			return false;
		}

		EquipmentIfs target = (EquipmentIfs)obj;
		if (this.equipment_type_id.equals(target.getEquipment_type_id()) &&
			this.physical_if_id.equals(target.getPhysical_if_id())) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "EquipmentIfs [equipment_type_id=" + equipment_type_id
				+ ", physical_if_id=" + physical_if_id + ", if_slot=" + if_slot
				+ "]";
	}
}
