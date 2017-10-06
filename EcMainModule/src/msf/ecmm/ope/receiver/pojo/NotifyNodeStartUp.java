
package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;

public class NotifyNodeStartUp extends AbstractRestMessage {

	public String getManagementIfAddress() {
		return managementIfAddress;
	}

	public void setManagementIfAddress(String managementIfAddress) {
		this.managementIfAddress = managementIfAddress;
	}

	@Override
	public String toString() {
		return "NotifyNodeStartUp [managementIfAddress=" + managementIfAddress + "]";
	}

	public void check(OperationType ope) throws CheckDataException {

		if (managementIfAddress == null) {
			throw new CheckDataException();
		}
	}

}
