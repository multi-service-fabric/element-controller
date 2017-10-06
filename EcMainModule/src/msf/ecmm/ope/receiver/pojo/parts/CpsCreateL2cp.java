
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class CpsCreateL2cp {

	private BaseIfCreateL2cp baseIf;

	private String portMode;

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	public BaseIfCreateL2cp getBaseIf() {
		return baseIf;
	}

	public void setBaseIf(BaseIfCreateL2cp baseIf) {
		this.baseIf = baseIf;
	}

	public Integer getVlanId() {
		return vlanId;
	}

	public void setVlanId(Integer vlanId) {
		this.vlanId = vlanId;
	}

	public String getPortMode() {
		return portMode;
	}

	public void setPortMode(String portMode) {
		this.portMode = portMode;
	}

	@Override
	public String toString() {
		return "CpsCreateL2cp [cpId=" + cpId + ", baseIf=" + baseIf + ", vlanId=" + vlanId + ", portMode=" + portMode
				+ "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (cpId == null) {
			throw new CheckDataException();
		}
		if (baseIf == null) {
			throw new CheckDataException();
		} else {
			baseIf.check(ope);
		}
		if (vlanId == null) {
			throw new CheckDataException();
		}
		if (portMode == null || (!portMode.equals("access") && !portMode.equals("trunk"))) {
			throw new CheckDataException();
		}
	}

}
