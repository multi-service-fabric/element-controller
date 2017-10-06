
package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.CpsCreateL2cp;

public class BulkCreateL2cp extends AbstractRestMessage {

	private String sliceId;

	private String ipv4MulticastAddress;

	public ArrayList<CpsCreateL2cp> getCps() {
	    return cps;
	}

	public void setCps(ArrayList<CpsCreateL2cp> cps) {
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

	public String getIpv4MulticastAddress() {
	    return ipv4MulticastAddress;
	}

	public void setIpv4MulticastAddress(String ipv4MulticastAddress) {
	    this.ipv4MulticastAddress = ipv4MulticastAddress;
	}

	@Override
	public String toString() {
		return "BulkCreateL2cp [cps=" + cps + ", sliceId=" + sliceId + ", vrfId=" + vrfId + ", ipv4MulticastAddress="
				+ ipv4MulticastAddress + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (cps.isEmpty()) {
			throw new CheckDataException();
		} else {
			for (CpsCreateL2cp l2cps : cps) {
				l2cps.check(ope);
			}
		}
		if (sliceId == null) {
			throw new CheckDataException();
		}
		if (vrfId == null) {
			throw new CheckDataException();
		}
		if (ipv4MulticastAddress == null) {
			throw new CheckDataException();
		}
	}

}
