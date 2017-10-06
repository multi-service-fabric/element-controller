package msf.ecmm.fcctrl.pojo;

public class GetLogicalIfStatusRequest extends AbstractRequest {

	public String getClusterId() {
	    return clusterId;
	}

	public void setClusterId(String clusterId) {
	    this.clusterId = clusterId;
	}

	@Override
	public String toString() {
		return "GetLogicalIfStatusRequest [clusterId=" + clusterId + "]";
	}
}
