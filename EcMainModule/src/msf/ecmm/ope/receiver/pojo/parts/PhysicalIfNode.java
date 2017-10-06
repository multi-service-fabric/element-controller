
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class PhysicalIfNode {

	public String getPhysicalIfId() {
		return physicalIfId;
	}

	public void setPhysicalIfId(String physicalIfId) {
		this.physicalIfId = physicalIfId;
	}

	@Override
	public String toString() {
		return "PhysicalIfNode [physicalIfId=" + physicalIfId + "]";
	}

	public void check(OperationType ope) throws CheckDataException {

		if (physicalIfId == null) {
			throw new CheckDataException();
		}
	}

}
