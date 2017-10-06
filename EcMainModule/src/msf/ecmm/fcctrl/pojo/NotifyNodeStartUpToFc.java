package msf.ecmm.fcctrl.pojo;

public class NotifyNodeStartUpToFc extends AbstractRequest {

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "NotifyNodeStartUp [status=" + status + "]";
	}

}
