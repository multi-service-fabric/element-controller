
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class VpnAddSpine {

	public L2vpnAddSpine getL2vpn() {
	    return l2vpn;
	}

	public void setL2vpn(L2vpnAddSpine l2vpn) {
	    this.l2vpn = l2vpn;
	}

	@Override
	public String toString() {
		return "VpnAddSpine [l2vpn=" + l2vpn + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (l2vpn == null) {
			throw new CheckDataException();
		} else {
			l2vpn.check(ope);
		}
	}
}
