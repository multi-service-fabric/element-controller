
package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.Varbind;

public class NotifyReceiveSnmpTrap extends AbstractRestMessage {

	private ArrayList<Varbind> varbind = new ArrayList<Varbind>();

	public String getSrcHostIp() {
		return srcHostIp;
	}

	public void setSrcHostIp(String srcHostIp) {
		this.srcHostIp = srcHostIp;
	}

	public ArrayList<Varbind> getVarbind() {
		return varbind;
	}

	public void setVarbind(ArrayList<Varbind> varbind) {
		this.varbind = varbind;
	}

	@Override
	public String toString() {
		return "NotifyReceiveSnmpTrap [srcHostIp=" + srcHostIp + ", varbind=" + varbind + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (varbind.isEmpty()) {
			throw new CheckDataException();
		} else{
			for (Varbind snmpData : varbind) {
				snmpData.check(ope);
			}
		}
		if (srcHostIp == null) {
			throw new CheckDataException();
		} else {
		}
	}

}
