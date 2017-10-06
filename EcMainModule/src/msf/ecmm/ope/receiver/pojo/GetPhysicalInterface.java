
package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.PhysicalIf;

public class GetPhysicalInterface extends AbstractResponseMessage {

	public PhysicalIf getPhysicalIf() {
		return physicalIf;
	}

	public void setPhysicalIf(PhysicalIf physicalIf) {
		this.physicalIf = physicalIf;
	}

	@Override
	public String toString() {
		return "GetPhysicalInterface [physicalIf=" + physicalIf + "]";
	}

}
