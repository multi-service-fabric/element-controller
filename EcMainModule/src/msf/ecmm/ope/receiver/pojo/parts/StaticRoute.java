
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class StaticRoute {

	private String address;

	private String nextHop;

	public String getAddressType() {
		return addressType;
	}

	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

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

	public String getNextHop() {
		return nextHop;
	}

	public void setNextHop(String nextHop) {
		this.nextHop = nextHop;
	}

	@Override
	public String toString() {
		return "StaticRoute [addressType=" + addressType + ", address=" + address + ", prefix=" + prefix
				+ ", nextHop=" + nextHop + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (addressType == null) {
			throw new CheckDataException();
		}
		if (!addressType.equals("ipv4") && !addressType.equals("ipv6")) {
			throw new CheckDataException();
		}
		if (address == null) {
			throw new CheckDataException();
		}
		if (prefix == null) {
			throw new CheckDataException();
		}
		if (nextHop == null) {
			throw new CheckDataException();
		}
	}

}
