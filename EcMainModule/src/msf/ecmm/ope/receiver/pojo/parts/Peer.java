
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class Peer {

	private String localAddress;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLocalAddress() {
		return localAddress;
	}

	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}

	@Override
	public String toString() {
		return "Peer [address=" + address + ", localAddress=" + localAddress + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (address == null) {
			throw new CheckDataException();
		}
		if (localAddress == null) {
			throw new CheckDataException();
		}
	}

}
