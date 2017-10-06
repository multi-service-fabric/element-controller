
package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.SwitchTraffic;

public class GetNodeTraffic extends AbstractResponseMessage {

	private Boolean isSuccess;

	private Integer interval;

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public Boolean getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}

	public ArrayList<SwitchTraffic> getSwitchTraffic() {
		return switchTraffic;
	}

	public void setSwitchTraffic(ArrayList<SwitchTraffic> switchTraffic) {
		this.switchTraffic = switchTraffic;
	}

	@Override
	public String toString() {
		return "GetNodeTraffic [clusterId=" + clusterId + ", isSuccess=" + isSuccess + ", time=" + time + ", interval="
				+ interval + ", switchTraffic=" + switchTraffic + "]";
	}
}
