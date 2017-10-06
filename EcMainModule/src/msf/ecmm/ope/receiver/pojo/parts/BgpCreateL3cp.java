
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class BgpCreateL3cp {

	private Integer neighborAs;

	private String neighborIpv6Address;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Integer getNeighborAs() {
		return neighborAs;
	}

	public void setNeighborAs(Integer neighborAs) {
		this.neighborAs = neighborAs;
	}

	public String getNeighborIpv4Address() {
		return neighborIpv4Address;
	}

	public void setNeighborIpv4Address(String neighborIpv4Address) {
		this.neighborIpv4Address = neighborIpv4Address;
	}

	public String getNeighborIpv6Address() {
		return neighborIpv6Address;
	}

	public void setNeighborIpv6Address(String neighborIpv6Address) {
		this.neighborIpv6Address = neighborIpv6Address;
	}

	@Override
	public String toString() {
		return "BgpCreateL3cp [role=" + role + ", neighborAs=" + neighborAs + ", neighborIpv4Address="
				+ neighborIpv4Address + ", neighborIpv6Address=" + neighborIpv6Address + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (role == null) {
			throw new CheckDataException();
		}
		if (!role.equals("master") && !role.equals("slave")) {
			throw new CheckDataException();
		}
		if (neighborAs == null) {
			throw new CheckDataException();
		}
		if (neighborIpv4Address == null && neighborIpv6Address == null) {
			throw new CheckDataException();
		}
	}

}
