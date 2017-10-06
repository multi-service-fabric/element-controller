
package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

public class IfSearchIf {

	private ArrayList<LagIf> lagIfs = new ArrayList<LagIf>();

	public ArrayList<PhysicalIf> getPhysicalIfs() {
		return physicalIfs;
	}

	public void setPhysicalIfs(ArrayList<PhysicalIf> physicalIfs) {
		this.physicalIfs = physicalIfs;
	}

	public ArrayList<LagIf> getLagIfs() {
		return lagIfs;
	}

	public void setLagIfs(ArrayList<LagIf> lagIfs) {
		this.lagIfs = lagIfs;
	}

	@Override
	public String toString() {
		return "IfSearchIf [physicalIfs=" + physicalIfs + ", lagIfs=" + lagIfs + "]";
	}

}
