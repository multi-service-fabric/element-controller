package msf.ecmm.devctrl.pojo;

public class DhcpInfo {
	private String initialConfig;

	private String macAddress;

	private String ntpServerAddress;

	private int prefix;

	public DhcpInfo(String configTemplete, String initialConfig, String hostname, String macAddress,
			String tftpHostname, String ntpServerAddress, String eqManagementAddress, int prefix) {
		super();
		this.configTemplete = configTemplete;
		this.initialConfig = initialConfig;
		this.hostname = hostname;
		this.macAddress = macAddress;
		this.tftpHostname = tftpHostname;
		this.ntpServerAddress = ntpServerAddress;
		this.eqManagementAddress = eqManagementAddress;
		this.prefix = prefix;
	}

	public String getConfigTemplete() {
		return configTemplete;
	}

	public void setConfigTemplete(String configTemplete) {
		this.configTemplete = configTemplete;
	}

	public String getInitialConfig() {
		return initialConfig;
	}

	public void setInitialConfig(String initialConfig) {
		this.initialConfig = initialConfig;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getTftpHostname() {
		return tftpHostname;
	}

	public void setTftpHostname(String tftpHostname) {
		this.tftpHostname = tftpHostname;
	}

	public String getNtpServerAddress() {
		return ntpServerAddress;
	}

	public void setNtpServerAddress(String ntpServerAddress) {
		this.ntpServerAddress = ntpServerAddress;
	}

	public String getEqManagementAddress() {
		return eqManagementAddress;
	}

	public void setEqManagementAddress(String eqManagementAddress) {
		this.eqManagementAddress = eqManagementAddress;
	}

	public int getPrefix() {
		return prefix;
	}

	public void setPrefix(int prefix) {
		this.prefix = prefix;
	}

	@Override
	public String toString() {
		return "DhcpInfo [configTemplete=" + configTemplete + ", initialConfig=" + initialConfig + ", hostname="
				+ hostname + ", macAddress=" + macAddress + ", tftpHostname=" + tftpHostname + ", ntpServerAddress="
				+ ntpServerAddress + ", eqManagementAddress=" + eqManagementAddress + ", prefix=" + prefix + "]";
	}

}
