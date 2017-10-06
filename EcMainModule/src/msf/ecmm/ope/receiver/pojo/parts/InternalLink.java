
package msf.ecmm.ope.receiver.pojo.parts;

public class InternalLink {

	private Double receiveRate;

	public String getInternalLinkIfId() {
		return internalLinkIfId;
	}

	public void setInternalLinkIfId(String internalLinkIfId) {
		this.internalLinkIfId = internalLinkIfId;
	}

	public Double getReceiveRate() {
		return receiveRate;
	}

	public void setReceiveRate(Double receiveRate) {
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
		return "InternalLink [internalLinkIfId=" + internalLinkIfId + ", receiveRate=" + receiveRate + ", sendRate="
				+ sendRate + "]";
	}
}
