package msf.ecmm.db.pojo;

import java.io.Serializable;
import java.util.Set;

public class Equipments implements Serializable {

	private String lag_prefix = null;
	private String if_name_oid = null;
	private int max_repetitions = 0;

	private Set<IfNameRules> ifNameRulesList;

	public Equipments() {
		super();
	}

	public String getEquipment_type_id() {
		return equipment_type_id;
	}

	public void setEquipment_type_id(String equipment_type_id) {
		this.equipment_type_id = equipment_type_id;
	}

	public String getLag_prefix() {
		return lag_prefix;
	}

	public void setLag_prefix(String lag_prefix) {
		this.lag_prefix = lag_prefix;
	}

	public String getUnit_connector() {
		return unit_connector;
	}

	public void setUnit_connector(String unit_connector) {
		this.unit_connector = unit_connector;
	}

	public String getIf_name_oid() {
		return if_name_oid;
	}

	public void setIf_name_oid(String if_name_oid) {
		this.if_name_oid = if_name_oid;
	}

	public String getSnmptrap_if_name_oid() {
		return snmptrap_if_name_oid;
	}

	public void setSnmptrap_if_name_oid(String snmptrap_if_name_oid) {
		this.snmptrap_if_name_oid = snmptrap_if_name_oid;
	}

	public int getMax_repetitions() {
		return max_repetitions;
	}

	public void setMax_repetitions(int max_repetitions) {
		this.max_repetitions = max_repetitions;
	}

	public Set<EquipmentIfs> getEquipmentIfsList() {
		return equipmentIfsList;
	}

	public void setEquipmentIfsList(Set<EquipmentIfs> equipmentIfsList) {
		this.equipmentIfsList = equipmentIfsList;
	}

	public Set<IfNameRules> getIfNameRulesList() {
		return ifNameRulesList;
	}

	public void setIfNameRulesList(Set<IfNameRules> ifNameRulesList) {
		this.ifNameRulesList = ifNameRulesList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((equipment_type_id == null) ? 0 : equipment_type_id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Equipments other = (Equipments) obj;
		if (equipment_type_id == null) {
			if (other.equipment_type_id != null) {
				return false;
			}
		} else if (!equipment_type_id.equals(other.equipment_type_id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Equipments [equipment_type_id=" + equipment_type_id
				+ ", lag_prefix=" + lag_prefix + ", unit_connector="
				+ unit_connector + ", if_name_oid=" + if_name_oid
				+ ", snmptrap_if_name_oid=" + snmptrap_if_name_oid
				+ ", max_repetitions=" + max_repetitions
				+ ", equipmentIfsList=" + equipmentIfsList
				+ ", ifNameRulesList=" + ifNameRulesList + "]";
	}

	public String toSimpleString() {
		return "Equipments [equipment_type_id=" + equipment_type_id
				+ ", lag_prefix=" + lag_prefix + ", unit_connector="
				+ unit_connector + ", if_name_oid=" + if_name_oid
				+ ", snmptrap_if_name_oid=" + snmptrap_if_name_oid
				+ ", max_repetitions=" + max_repetitions + "]";
	}
}
