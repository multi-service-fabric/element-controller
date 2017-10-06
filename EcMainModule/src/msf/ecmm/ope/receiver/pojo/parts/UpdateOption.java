
package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class UpdateOption {

	private String ipv6Address;

	private Integer ipv6Prefix;

	private String operationType;

	private ArrayList<StaticRoute> staticRoutes = new ArrayList<StaticRoute>();

	public String getIpv4Address() {
		return ipv4Address;
	}

	public void setIpv4Address(String ipv4Address) {
		this.ipv4Address = ipv4Address;
	}

	public String getIpv6Address() {
		return ipv6Address;
	}

	public void setIpv6Address(String ipv6Address) {
		this.ipv6Address = ipv6Address;
	}

	public Integer getIpv4Prefix() {
		return ipv4Prefix;
	}

	public void setIpv4Prefix(Integer ipv4Prefix) {
		this.ipv4Prefix = ipv4Prefix;
	}

	public Integer getIpv6Prefix() {
		return ipv6Prefix;
	}

	public void setIpv6Prefix(Integer ipv6Prefix) {
		this.ipv6Prefix = ipv6Prefix;
	}

	public Integer getVrfId() {
		return vrfId;
	}

	public void setVrfId(Integer vrfId) {
		this.vrfId = vrfId;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getRouterId() {
		return routerId;
	}

	public void setRouterId(String routerId) {
		this.routerId = routerId;
	}

	public ArrayList<StaticRoute> getStaticRoutes() {
		return staticRoutes;
	}

	public void setStaticRoutes(ArrayList<StaticRoute> staticRoutes) {
		this.staticRoutes = staticRoutes;
	}

	@Override
	public String toString() {
		return "UpdateOption [ipv4Address=" + ipv4Address + ", ipv6Address=" + ipv6Address + ", ipv4Prefix="
				+ ipv4Prefix + ", ipv6Prefix=" + ipv6Prefix + ", vrfId=" + vrfId + ", operationType=" + operationType
				+ ", routerId=" + routerId + ", staticRoutes=" + staticRoutes + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (ipv4Address == null && ipv6Address == null) {
			throw new CheckDataException();
		}
		if (vrfId == null) {
			throw new CheckDataException();
		}
		if (operationType == null) {
			throw new CheckDataException();
		}
		if (!operationType.equals("add") && !operationType.equals("delete")) {
			throw new CheckDataException();
		}
		if (routerId == null) {
			throw new CheckDataException();
		}
		if (staticRoutes.isEmpty()) {
			throw new CheckDataException();
		} else {
			for (StaticRoute sr : staticRoutes) {
				sr.check(ope);
			}
		}
	}

}
