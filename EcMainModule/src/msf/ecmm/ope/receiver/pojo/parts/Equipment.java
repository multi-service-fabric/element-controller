
package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class Equipment {

	private String lagPrefix;

	private String ifNameOid;

	private Integer maxRepetitions;

	private ArrayList<EquipmentIf> equipmentIfs = new ArrayList<EquipmentIf>();

	public String getEquipmentTypeId() {
		return equipmentTypeId;
	}

	public void setEquipmentTypeId(String equipmentTypeId) {
		this.equipmentTypeId = equipmentTypeId;
	}

	public String getLagPrefix() {
		return lagPrefix;
	}

	public void setLagPrefix(String lagPrefix) {
		this.lagPrefix = lagPrefix;
	}

	public String getUnitConnector() {
		return unitConnector;
	}

	public void setUnitConnector(String unitConnector) {
		this.unitConnector = unitConnector;
	}

	public String getIfNameOid() {
		return ifNameOid;
	}

	public void setIfNameOid(String ifNameOid) {
		this.ifNameOid = ifNameOid;
	}

	public String getSnmptrapIfNameOid() {
		return snmptrapIfNameOid;
	}

	public void setSnmptrapIfNameOid(String snmptrapIfNameOid) {
		this.snmptrapIfNameOid = snmptrapIfNameOid;
	}

	public Integer getMaxRepetitions() {
		return maxRepetitions;
	}

	public void setMaxRepetitions(Integer maxRepetitions) {
		this.maxRepetitions = maxRepetitions;
	}

	public ArrayList<IfNameRule> getIfNameRules() {
		return ifNameRules;
	}

	public void setIfNameRules(ArrayList<IfNameRule> ifNameRules) {
		this.ifNameRules = ifNameRules;
	}

	public ArrayList<EquipmentIf> getEquipmentIfs() {
		return equipmentIfs;
	}

	public void setEquipmentIfs(ArrayList<EquipmentIf> equipmentIfs) {
		this.equipmentIfs = equipmentIfs;
	}

	@Override
	public String toString() {
		return "Equipment [equipmentTypeId=" + equipmentTypeId + ", lagPrefix=" + lagPrefix + ", unitConnector="
				+ unitConnector + ", ifNameOid=" + ifNameOid + ", snmptrapIfNameOid=" + snmptrapIfNameOid
				+ ", maxRepetitions=" + maxRepetitions + ", ifNameRules=" + ifNameRules + ", equipmentIfs="
				+ equipmentIfs + "]";
	}

	public void check(OperationType operationType) throws CheckDataException {

		if (equipmentTypeId == null) {
			throw new CheckDataException();
		}
		if (lagPrefix == null) {
			throw new CheckDataException();
		}
		if (unitConnector == null) {
			throw new CheckDataException();
		}
		if (ifNameOid == null) {
			throw new CheckDataException();
		}
		if (maxRepetitions == null) {
			throw new CheckDataException();
		}

		if (ifNameRules.isEmpty()) {
			throw new CheckDataException();
		} else {
			for (IfNameRule ifNameRule : ifNameRules) {
				ifNameRule.check(operationType);
			}
		}

		if (equipmentIfs.isEmpty()) {
			throw new CheckDataException();
		} else {
			for (EquipmentIf equipmentIf : equipmentIfs) {
				equipmentIf.check(operationType);
			}
		}
	}
}
