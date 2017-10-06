
package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class LagIfDeleteNode {

	private ArrayList<PhysicalIfNode> physicalIfs = new ArrayList<PhysicalIfNode>();

	public String getLagIfId() {
		return lagIfId;
	}

	public void setLagIfId(String lagIfId) {
		this.lagIfId = lagIfId;
	}

	public ArrayList<PhysicalIfNode> getPhysicalIfs() {
		return physicalIfs;
	}

	public void setPhysicalIfs(ArrayList<PhysicalIfNode> physicalIfs) {
		this.physicalIfs = physicalIfs;
	}

	public Integer getMinimumLinks() {
		return minimumLinks;
	}

	public void setMinimumLinks(Integer minimumLinks) {
		this.minimumLinks = minimumLinks;
	}

	@Override
	public String toString() {
		return "LagIfDeleteNode [lagIfId=" + lagIfId + ", physicalIfs=" + physicalIfs + ", minimumLinks="
				+ minimumLinks + "]";
	}

	public void check(OperationType ope) throws CheckDataException {

		if (lagIfId == null) {
			throw new CheckDataException();
		}

		if (physicalIfs.isEmpty()) {
			throw new CheckDataException();
		} else {
			for (PhysicalIfNode physicalIfNode : physicalIfs) {
				physicalIfNode.check(ope);
			}
		}

		if (minimumLinks == null){
			throw new CheckDataException();
		}
	}
}
