
package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.CpsCreateL3cp;

public class BulkCreateL3cp extends AbstractRestMessage {

	private String sliceId;

	private Integer plane;

	public ArrayList<CpsCreateL3cp> getCps() {
		return cps;
	}

	public void setCps(ArrayList<CpsCreateL3cp> cps) {
		this.cps = cps;
	}

	public String getSliceId() {
		return sliceId;
	}

	public void setSliceId(String sliceId) {
		this.sliceId = sliceId;
	}

	public Integer getVrfId() {
		return vrfId;
	}

	public void setVrfId(Integer vrfId) {
		this.vrfId = vrfId;
	}

	public Integer getPlane() {
		return plane;
	}

	public void setPlane(Integer plane) {
		this.plane = plane;
	}

	@Override
	public String toString() {
		return "BulkCreateL3cp [cps=" + cps + ", sliceId=" + sliceId + ", vrfId=" + vrfId + ", plane=" + plane + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (cps.isEmpty()) {
			throw new CheckDataException();
		} else {
			for (CpsCreateL3cp l3cps : cps) {
				l3cps.check(ope);
			}
		}
		if (sliceId == null) {
			throw new CheckDataException();
		}
		if (vrfId == null) {
			throw new CheckDataException();
		}
		if (plane == null) {
			throw new CheckDataException();
		}
	}

}
