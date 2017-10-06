package msf.ecmm.db.pojo;

import java.util.List;

public class CpsList {

	private List<L3Cps> l3CpsList;

	public CpsList() {
		super();
	}

	public List<L2Cps> getL2CpsList() {
	    return l2CpsList;
	}

	public void setL2CpsList(List<L2Cps> l2CpsList) {
	    this.l2CpsList = l2CpsList;
	}

	public List<L3Cps> getL3CpsList() {
	    return l3CpsList;
	}

	public void setL3CpsList(List<L3Cps> l3CpsList) {
	    this.l3CpsList = l3CpsList;
	}

	@Override
	public String toString() {
		return "CpsList [l2CpsList=" + l2CpsList + ", l3CpsList=" + l3CpsList
				+ "]";
	}
}
