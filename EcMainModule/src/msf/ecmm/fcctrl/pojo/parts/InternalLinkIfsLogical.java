
package msf.ecmm.fcctrl.pojo.parts;

public class InternalLinkIfsLogical {

	private String status;

	public String getInternalLinkIfId() {
		return internalLinkIfId;
	}

	public void setInternalLinkIfId(String internalLinkIfId) {
		this.internalLinkIfId = internalLinkIfId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "InternalLinkIfsLogical [internalLinkIfId=" + internalLinkIfId + ", status=" + status + "]";
	}

}
