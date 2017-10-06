
package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class LagIfAddNode {

	private ArrayList<PhysicalIfNode> physicalIfs = new ArrayList<PhysicalIfNode>();

	private String linkSpeed;

	private Integer prefix;

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

	public String getLinkSpeed() {
		return linkSpeed;
	}

	public void setLinkSpeed(String linkSpeed) {
		this.linkSpeed = linkSpeed;
	}

	public String getLinkIpAddress() {
		return linkIpAddress;
	}

	public void setLinkIpAddress(String linkIpAddress) {
		this.linkIpAddress = linkIpAddress;
	}

	public Integer getPrefix() {
		return prefix;
	}

	public void setPrefix(Integer prefix) {
		this.prefix = prefix;
	}

	@Override
	public String toString() {
		return "LagIfAddNode [lagIfId=" + lagIfId + ", physicalIfs=" + physicalIfs + ", minimumLinks=" + minimumLinks
				+ ", linkSpeed=" + linkSpeed + ", linkIpAddress=" + linkIpAddress + ", prefix=" + prefix + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (lagIfId == null) {
			throw new CheckDataException();
		}
		if (physicalIfs.isEmpty()) {
			throw new CheckDataException();
		} else {
			for (PhysicalIfNode ifNode : physicalIfs) {
				ifNode.check(ope);
			}
		}
		if (minimumLinks == null) {
			throw new CheckDataException();
		}
		if (linkSpeed == null) {
			throw new CheckDataException();
		}
		if (linkIpAddress == null) {
			throw new CheckDataException();
		}
		if (prefix == null) {
			throw new CheckDataException();
		}
	}
}
