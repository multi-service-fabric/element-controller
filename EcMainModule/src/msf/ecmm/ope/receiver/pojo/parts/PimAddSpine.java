
package msf.ecmm.ope.receiver.pojo.parts;

public class PimAddSpine {

	private String selfRpAddress;

	public String getOtherRpAddress() {
		return otherRpAddress;
	}

	public void setOtherRpAddress(String otherRpAddress) {
		this.otherRpAddress = otherRpAddress;
	}

	public String getSelfRpAddress() {
		return selfRpAddress;
	}

	public void setSelfRpAddress(String selfRpAddress) {
		this.selfRpAddress = selfRpAddress;
	}

	@Override
	public String toString() {
		return "PimAddSpine [otherRpAddress=" + otherRpAddress + ", selfRpAddress=" + selfRpAddress + "]";
	}

}
