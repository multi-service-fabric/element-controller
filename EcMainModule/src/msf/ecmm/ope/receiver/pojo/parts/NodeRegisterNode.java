
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class NodeRegisterNode {

	private String hostName;

	private String ntpServerAddress;

	private String snmpCommunity;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
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

	public String getSnmpCommunity() {
		return snmpCommunity;
	}

	public void setSnmpCommunity(String snmpCommunity) {
		this.snmpCommunity = snmpCommunity;
	}

	@Override
	public String toString() {
		return "NodeRegisterNode [nodeId=" + nodeId + ", hostName=" + hostName + ", macAddr=" + macAddr
				+ ", ntpServerAddress=" + ntpServerAddress
				+ ", managementInterface=" + managementInterface + ", snmpCommunity=" + snmpCommunity + "]";
	}

	public void check(OperationType ope) throws CheckDataException {

		if(nodeId == null){
			throw new CheckDataException();
		}
		if(hostName == null){
			throw new CheckDataException();
		}
		if(macAddr == null){
			throw new CheckDataException();
		}
		if(ntpServerAddress == null){
			throw new CheckDataException();
		}
		if(managementInterface == null){
			throw new CheckDataException();
		} else {
			managementInterface.check(ope);
		}
		if(snmpCommunity == null){
			throw new CheckDataException();
		}
	}

}
