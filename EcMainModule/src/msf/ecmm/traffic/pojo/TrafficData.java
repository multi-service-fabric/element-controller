package msf.ecmm.traffic.pojo;

public class TrafficData {

	private double ifHclnOctets = -1.0;

	public TrafficData() {
		super();
	}

	public String getIfname() {
		return ifName;
	}

	public void setIfname(String ifName) {
		this.ifName = ifName;
	}

	public double getIfHclnOctets() {
		return ifHclnOctets;
	}

	public void setIfHclnOctets(double ifHclnOctets) {
		this.ifHclnOctets = ifHclnOctets;
	}

	public double getIfHcOutOctets() {
		return ifHcOutOctets;
	}

	public void setIfHcOutOctets(double ifHcOutOctets) {
		this.ifHcOutOctets = ifHcOutOctets;
	}

	@Override
	public String toString() {
		return "TrafficData [ifName=" + ifName + ", ifHclnOctets="
				+ ifHclnOctets + ", ifHcOutOctets=" + ifHcOutOctets + "]";
	}



}
