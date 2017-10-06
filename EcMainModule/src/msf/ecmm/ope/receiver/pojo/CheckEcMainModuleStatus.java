
package msf.ecmm.ope.receiver.pojo;

public class CheckEcMainModuleStatus extends AbstractResponseMessage {

	private String busy;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBusy() {
		return busy;
	}

	public void setBusy(String busy) {
		this.busy = busy;
	}

	@Override
	public String toString() {
		return "CheckEcMainModuleStatus [status=" + status + ", busy=" + busy + "]";
	}

}
