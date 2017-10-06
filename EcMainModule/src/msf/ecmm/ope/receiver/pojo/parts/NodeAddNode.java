
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class NodeAddNode {

	private String username;

	private Boolean provisioning;

	private ManagementInterface managementInterface;

	private String snmpCommunity;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getProvisioning() {
		return provisioning;
	}

	public void setProvisioning(Boolean provisioning) {
		this.provisioning = provisioning;
	}

	public String getNtpServerAddress() {
		return ntpServerAddress;
	}

	public void setNtpServerAddress(String ntpServerAddress) {
		this.ntpServerAddress = ntpServerAddress;
	}

	public ManagementInterface getManagementInterface() {
		return managementInterface;
	}

	public void setManagementInterface(ManagementInterface managementInterface) {
		this.managementInterface = managementInterface;
	}

	public LoopbackInterface getLoopbackInterface() {
		return loopbackInterface;
	}

	public void setLoopbackInterface(LoopbackInterface loopbackInterface) {
		this.loopbackInterface = loopbackInterface;
	}

	public String getSnmpCommunity() {
		return snmpCommunity;
	}

	public void setSnmpCommunity(String snmpCommunity) {
		this.snmpCommunity = snmpCommunity;
	}

	@Override
	public String toString() {
		return "NodeAddNode [nodeId=" + nodeId + ", username=" + username + ", password=" + password
				+ ", provisioning=" + provisioning + ", ntpServerAddress=" + ntpServerAddress
				+ ", managementInterface=" + managementInterface + ", loopbackInterface=" + loopbackInterface
				+ ", snmpCommunity=" + snmpCommunity + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (nodeId == null) {
			throw new CheckDataException();
		}
		if (username == null) {
			throw new CheckDataException();
		}
		if (password == null) {
			throw new CheckDataException();
		}
		if (provisioning == null) {
			throw new CheckDataException();
		}
		if (ntpServerAddress == null) {
			throw new CheckDataException();
		}
		if (managementInterface == null) {
			throw new CheckDataException();
		} else {
			managementInterface.check(ope);
		}
		if (loopbackInterface == null) {
			throw new CheckDataException();
		} else {
			loopbackInterface.check(ope);
		}
		if (snmpCommunity == null) {
			throw new CheckDataException();
		}
	}
}
