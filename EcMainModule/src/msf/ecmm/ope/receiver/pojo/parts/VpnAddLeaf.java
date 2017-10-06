
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class VpnAddLeaf {

	private L3vpn l3vpn;

	public String getVpnType() {
		return vpnType;
	}

	public void setVpnType(String vpnType) {
		this.vpnType = vpnType;
	}

	public L3vpn getL3vpn() {
		return l3vpn;
	}

	public void setL3vpn(L3vpn l3vpn) {
		this.l3vpn = l3vpn;
	}

	public L2vpn getL2vpn() {
		return l2vpn;
	}

	public void setL2vpn(L2vpn l2vpn) {
		this.l2vpn = l2vpn;
	}

	@Override
	public String toString() {
		return "VpnAddLeaf [vpnType=" + vpnType + ", l3vpn=" + l3vpn + ", l2vpn=" + l2vpn + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (vpnType == null) {
			throw new CheckDataException();
		}
		if (!vpnType.equals("l3") && !vpnType.equals("l2")) {
			throw new CheckDataException();
		}
		if ((l2vpn == null) && (l3vpn == null)) {
			throw new CheckDataException();
		}
		if (l3vpn != null) {
			l3vpn.check(ope);
		}
		if (l2vpn != null) {
			l2vpn.check(ope);
		}
	}

}
