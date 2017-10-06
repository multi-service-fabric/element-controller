
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class As {

	public String getAsNumber() {
		return asNumber;
	}

	public void setAsNumber(String asNumber) {
		this.asNumber = asNumber;
	}

	@Override
	public String toString() {
		return "As [asNumber=" + asNumber + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (asNumber == null) {
			throw new CheckDataException();
		}
	}

}
