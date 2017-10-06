
package msf.ecmm.ope.receiver.pojo.parts;

public class L2Cp {

	private String physicalIfId;

	private Double receiveRate;

	public String getIfType() {
		return ifType;
	}

	public void setIfType(String ifType) {
		this.ifType = ifType;
	}

	public String getPhysicalIfId() {
		return physicalIfId;
	}

	public void setPhysicalIfId(String physicalIfId) {
		this.physicalIfId = physicalIfId;
	}

	public String getLagIfId() {
		return lagIfId;
	}

	public void setLagIfId(String lagIfId) {
		this.lagIfId = lagIfId;
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
		return "L2Cp [ifType=" + ifType + ", physicalIfId=" + physicalIfId + ", lagIfId=" + lagIfId + ", receiveRate="
				+ receiveRate + ", sendRate=" + sendRate + "]";
	}
}
