
package msf.ecmm.fcctrl.pojo.parts;

import java.util.ArrayList;

public class Slice {

	private ArrayList<VpnLogical> l3vpn = new ArrayList<VpnLogical>();

	public ArrayList<VpnLogical> getL2vpn() {
		return l2vpn;
	}

	public void setL2vpn(ArrayList<VpnLogical> l2vpn) {
		this.l2vpn = l2vpn;
	}

	public ArrayList<VpnLogical> getL3vpn() {
		return l3vpn;
	}

	public void setL3vpn(ArrayList<VpnLogical> l3vpn) {
		this.l3vpn = l3vpn;
	}

	@Override
	public String toString() {
		return "Slice [l2vpn=" + l2vpn + ", l3vpn=" + l3vpn + "]";
	}

}
