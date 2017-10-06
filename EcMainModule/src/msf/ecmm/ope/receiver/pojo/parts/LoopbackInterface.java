
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class LoopbackInterface {

	private Integer prefix;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPrefix() {
		return prefix;
	}

	public void setPrefix(Integer prefix) {
		this.prefix = prefix;
	}

	@Override
	public String toString() {
		return "LoopbackInterface [address=" + address + ", prefix=" + prefix + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (address == null) {
			throw new CheckDataException();
		}
		if (prefix == null) {
			throw new CheckDataException();
		}
	}

}
