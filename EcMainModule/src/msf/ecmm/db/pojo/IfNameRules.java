package msf.ecmm.db.pojo;

import java.io.Serializable;

public class IfNameRules implements Serializable {

	private String speed = null;
	private Equipments equipments;

	public IfNameRules() {
		super();
	}

	public String getEquipment_type_id() {
	    return equipment_type_id;
	}

	public void setEquipment_type_id(String equipment_type_id) {
	    this.equipment_type_id = equipment_type_id;
	}

	public String getSpeed() {
	    return speed;
	}

	public void setSpeed(String speed) {
	    this.speed = speed;
	}

	public String getPort_prefix() {
	    return port_prefix;
	}

	public void setPort_prefix(String port_prefix) {
	    this.port_prefix = port_prefix;
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
		if (speed != null) {
			hashCode ^= speed.hashCode();
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

		IfNameRules target = (IfNameRules)obj;
		if (this.equipment_type_id.equals(target.getEquipment_type_id()) &&
			this.speed.equals(target.getSpeed())) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "IfNameRules [equipment_type_id=" + equipment_type_id
				+ ", speed=" + speed + ", port_prefix=" + port_prefix + "]";
	}
}
