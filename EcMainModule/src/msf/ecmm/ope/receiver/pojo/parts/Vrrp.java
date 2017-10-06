
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class Vrrp {

	private String role;

	private String virtualIpv6Address;

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getVirtualIpv4Address() {
		return virtualIpv4Address;
	}

	public void setVirtualIpv4Address(String virtualIpv4Address) {
		this.virtualIpv4Address = virtualIpv4Address;
	}

	public String getVirtualIpv6Address() {
		return virtualIpv6Address;
	}

	public void setVirtualIpv6Address(String virtualIpv6Address) {
		this.virtualIpv6Address = virtualIpv6Address;
	}

	@Override
	public String toString() {
		return "Vrrp [groupId=" + groupId + ", role=" + role + ", virtualIpv4Address=" + virtualIpv4Address
				+ ", virtualIpv6Address=" + virtualIpv6Address + "]";
	}

	public void check(OperationType ope) throws CheckDataException {

		if (groupId == null) {
			throw new CheckDataException();
		}
		if (role == null) {
			throw new CheckDataException();
		}
		if (!role.equals("master") && !role.equals("slave")) {
			throw new CheckDataException();
		}
		if (virtualIpv4Address == null && virtualIpv6Address == null) {
			throw new CheckDataException();
		}
	}

}
