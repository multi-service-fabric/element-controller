package msf.ecmm.fcctrl.pojo;

public class OperationsResponse extends AbstractResponse {

	GetLogicalIfStatus getLogicalIfStatus;

	public GetLogicalIfStatus getGetLogicalIfStatus() {
	    return getLogicalIfStatus;
	}

	public void setGetLogicalIfStatus(GetLogicalIfStatus getLogicalIfStatus) {
	    this.getLogicalIfStatus = getLogicalIfStatus;
	}

	@Override
	public String toString() {
		return "OperationsResponse [getLogicalIfStatus=" + getLogicalIfStatus + "]";
	}
}
