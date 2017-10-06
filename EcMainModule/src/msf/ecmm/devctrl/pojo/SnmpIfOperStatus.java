package msf.ecmm.devctrl.pojo;

public class SnmpIfOperStatus {

	public static final int IF_OPER_STATUS_DOWN = 2;

	public static final int IF_OPER_STATUS_LOWER_LAYER_DOWN = 7;

	private int ifOperStatus;

	public SnmpIfOperStatus(String ifName, int ifOperStatus) {
		super();
		this.ifName = ifName;
		this.ifOperStatus = ifOperStatus;
	}

	public String getIfName() {
		return ifName;
	}

	public void setIfName(String ifName) {
		this.ifName = ifName;
	}

	public int getIfOperStatus() {
		return ifOperStatus;
	}

	public void setIfOperStatus(int ifOperStatus) {
		this.ifOperStatus = ifOperStatus;
	}

	@Override
	public String toString() {
		return "SnmpIfOperStatus [ifName=" + ifName + ", ifOperStatus=" + ifOperStatus + "]";
	}

}
