
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class IfNameRule {

	private String portPrefix;

	public String getSpeed() {
		return speed;
	}

	public void setSpeed(String speed) {
		this.speed = speed;
	}

	public String getPortPrefix() {
		return portPrefix;
	}

	public void setPortPrefix(String portPrefix) {
		this.portPrefix = portPrefix;
	}

	@Override
	public String toString() {
		return "IfNameRule [speed=" + speed + ", portPrefix=" + portPrefix + "]";
	}

	public void check(OperationType operationType) throws CheckDataException {
		if (speed == null) {
			throw new CheckDataException();
		}
		if (portPrefix == null) {
			throw new CheckDataException();
		}
	}
}
