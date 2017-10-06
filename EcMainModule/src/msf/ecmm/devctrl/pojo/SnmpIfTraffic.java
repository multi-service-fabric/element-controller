package msf.ecmm.devctrl.pojo;

public class SnmpIfTraffic {

	private long inOctets;

	public SnmpIfTraffic(String ifName, long inOctets, long outOctets) {
		super();
		this.ifName = ifName;
		this.inOctets = inOctets;
		this.outOctets = outOctets;
	}

	public String getIfName() {
		return ifName;
	}

	public void setIfName(String ifName) {
		this.ifName = ifName;
	}

	public long getInOctets() {
		return inOctets;
	}

	public void setInOctets(long inOctets) {
		this.inOctets = inOctets;
	}

	public long getOutOctets() {
		return outOctets;
	}

	public void setOutOctets(long outOctets) {
		this.outOctets = outOctets;
	}

	@Override
	public String toString() {
		return "SnmpIfTraffic [ifName=" + ifName + ", inOctets=" + inOctets + ", outOctets=" + outOctets + "]";
	}

}
