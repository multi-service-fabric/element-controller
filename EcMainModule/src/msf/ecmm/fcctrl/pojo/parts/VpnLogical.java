
package msf.ecmm.fcctrl.pojo.parts;

import java.util.ArrayList;

public class VpnLogical {

	private ArrayList<CpLogical> cps = new ArrayList<CpLogical>();

	public String getSliceId() {
		return sliceId;
	}

	public void setSliceId(String sliceId) {
		this.sliceId = sliceId;
	}

	public ArrayList<CpLogical> getCps() {
		return cps;
	}

	public void setCps(ArrayList<CpLogical> cps) {
		this.cps = cps;
	}

	@Override
	public String toString() {
		return "VpnLogical [sliceId=" + sliceId + ", cps=" + cps + "]";
	}

}
