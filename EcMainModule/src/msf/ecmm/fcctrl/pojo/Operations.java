package msf.ecmm.fcctrl.pojo;

public class Operations extends AbstractRequest {

	private GetLogicalIfStatusRequest getLogicalIfStatusOption;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public GetLogicalIfStatusRequest getGetLogicalIfStatusOption() {
	    return getLogicalIfStatusOption;
	}

	public void setGetLogicalIfStatusOption(GetLogicalIfStatusRequest getLogicalIfStatusOption) {
	    this.getLogicalIfStatusOption = getLogicalIfStatusOption;
	}

	public UpdateLogicalIfStatus getUpdateLogicalIfStatusOption() {
		return updateLogicalIfStatusOption;
	}

	public void setUpdateLogicalIfStatusOption(UpdateLogicalIfStatus updateLogicalIfStatusOption) {
		this.updateLogicalIfStatusOption = updateLogicalIfStatusOption;
	}

	@Override
	public String toString() {
		return "Operations [action=" + action + ", getLogicalIfStatusOption=" + getLogicalIfStatusOption
				+ ", updateLogicalIfStatusOption=" + updateLogicalIfStatusOption + "]";
	}

}
