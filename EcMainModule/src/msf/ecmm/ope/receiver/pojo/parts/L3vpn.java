
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class L3vpn {

	private As as;

	public BgpAddNode getBgp() {
		return bgp;
	}

	public void setBgp(BgpAddNode bgp) {
		this.bgp = bgp;
	}

	public As getAs() {
		return as;
	}

	public void setAs(As as) {
		this.as = as;
	}

	@Override
	public String toString() {
		return "L3vpn [bgp=" + bgp + ", as=" + as + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (bgp == null) {
			throw new CheckDataException();
		} else {
			bgp.check(ope);
		}
		if (as == null) {
			throw new CheckDataException();
		} else {
			as.check(ope);
		}
	}

}
