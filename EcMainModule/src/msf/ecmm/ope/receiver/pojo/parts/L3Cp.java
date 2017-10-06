
package msf.ecmm.ope.receiver.pojo.parts;

public class L3Cp {

	private String cpId;

	private Double sendRate;

	public String getSliceId() {
		return sliceId;
	}

	public void setSliceId(String sliceId) {
		this.sliceId = sliceId;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public Double getReceiveRatel() {
		return receiveRate;
	}

	public void setReceiveRatel(Double receiveRate) {
		this.receiveRate = receiveRate;
	}

	public Double getSendRate() {
		return sendRate;
	}

	public void setSendRate(Double sendRate) {
		this.sendRate = sendRate;
	}

	@Override
	public String toString() {
		return "L3Cp [sliceId=" + sliceId + ", cpId=" + cpId + ", receiveRate=" + receiveRate + ", sendRate="
				+ sendRate + "]";
	}
}
