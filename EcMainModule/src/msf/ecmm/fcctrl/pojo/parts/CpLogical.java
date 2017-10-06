
package msf.ecmm.fcctrl.pojo.parts;

public class CpLogical {

	private String status;

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CpLogical [cpId=" + cpId + ", status=" + status + "]";
	}

}
