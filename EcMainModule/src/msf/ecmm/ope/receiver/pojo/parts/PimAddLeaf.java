
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class PimAddLeaf {

	public String getRpAddress() {
		return rpAddress;
	}

	public void setRpAddress(String rpAddress) {
		this.rpAddress = rpAddress;
	}

	@Override
	public String toString() {
		return "PimAddLeaf [rpAddress=" + rpAddress + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (rpAddress == null) {
			throw new CheckDataException();
		}
	}

}
