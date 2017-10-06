
package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.receiver.pojo.parts.PhysicalIf;

public class GetPhysicalInterfaceList extends AbstractResponseMessage {

	public ArrayList<PhysicalIf> getPhysicalIfs() {
		return physicalIfs;
	}

	public void setPhysicalIfs(ArrayList<PhysicalIf> physicalIfs) {
		this.physicalIfs = physicalIfs;
	}

	@Override
	public String toString() {
		return "GetPhysicalInterfaceList [physicalIfs=" + physicalIfs + "]";
	}

}
