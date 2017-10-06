
package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class CpsCreateL3cp {

	private BaseIfCreateL3cp baseIf;

	private Integer mtu;

	private String ipv6Address;

	private Integer ipv6Prefix;

	private Ospf ospf;

	private Vrrp vrrp;

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public BaseIfCreateL3cp getBaseIf() {
		return baseIf;
	}

	public void setBaseIf(BaseIfCreateL3cp baseIf) {
		this.baseIf = baseIf;
	}

	public Integer getVlanId() {
		return vlanId;
	}

	public void setVlanId(Integer vlanId) {
		this.vlanId = vlanId;
	}

	public Integer getMtu() {
		return mtu;
	}

	public void setMtu(Integer mtu) {
		this.mtu = mtu;
	}

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

	public BgpCreateL3cp getBgp() {
		return bgp;
	}

	public void setBgp(BgpCreateL3cp bgp) {
		this.bgp = bgp;
	}

	public Ospf getOspf() {
		return ospf;
	}

	public void setOspf(Ospf ospf) {
		this.ospf = ospf;
	}

	public ArrayList<StaticRoute> getStaticRoutes() {
		return staticRoutes;
	}

	public void setStaticRoutes(ArrayList<StaticRoute> staticRoutes) {
		this.staticRoutes = staticRoutes;
	}

	public Vrrp getVrrp() {
		return vrrp;
	}

	public void setVrrp(Vrrp vrrp) {
		this.vrrp = vrrp;
	}

	@Override
	public String toString() {
		return "CpsCreateL3cp [cpId=" + cpId + ", baseIf=" + baseIf + ", vlanId=" + vlanId + ", mtu=" + mtu
				+ ", ipv4Address=" + ipv4Address + ", ipv6Address=" + ipv6Address + ", ipv4Prefix=" + ipv4Prefix
				+ ", ipv6Prefix=" + ipv6Prefix + ", bgp=" + bgp + ", ospf=" + ospf
				+ ", staticRoutes=" + staticRoutes + ", vrrp=" + vrrp + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (cpId == null) {
			throw new CheckDataException();
		}
		if (baseIf == null) {
			throw new CheckDataException();
		} else {
			baseIf.check(ope);
		}
		if (vlanId == null) {
			throw new CheckDataException();
		}
		if (mtu == null) {
			throw new CheckDataException();
		}
		if (ipv4Address == null && ipv6Address == null) {
			throw new CheckDataException();
		}
		if (bgp != null) {
			bgp.check(ope);
		}
		if (ospf != null) {
			ospf.check(ope);
		}
		for (StaticRoute sr : staticRoutes) {
			sr.check(ope);
		}
		if (vrrp != null) {
			vrrp.check(ope);
		}
	}

}
