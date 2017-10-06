
package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

public class TrafficValue {

	private ArrayList<L2Cp> l2Cps = new ArrayList<L2Cp>();

	public ArrayList<L3Cp> getL3Cps() {
		return l3Cps;
	}

	public void setL3Cps(ArrayList<L3Cp> l3Cps) {
		this.l3Cps = l3Cps;
	}

	public ArrayList<L2Cp> getL2Cps() {
		return l2Cps;
	}

	public void setL2Cps(ArrayList<L2Cp> l2Cps) {
		this.l2Cps = l2Cps;
	}

	public ArrayList<InternalLink> getInternalLinks() {
		return internalLinks;
	}

	public void setInternalLinks(ArrayList<InternalLink> internalLinks) {
		this.internalLinks = internalLinks;
	}

	@Override
	public String toString() {
		return "TrafficValue [l3Cps=" + l3Cps + ", l2Cps=" + l2Cps + ", internalLinks=" + internalLinks + "]";
	}

}
