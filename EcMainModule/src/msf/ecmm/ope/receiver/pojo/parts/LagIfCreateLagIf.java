
package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class LagIfCreateLagIf {

	private Integer minimumLinks;

	private ArrayList<String> physicalIfIds = new ArrayList<String>();

	public String getLagIfId() {
		return lagIfId;
	}

	public void setLagIfId(String lagIfId) {
		this.lagIfId = lagIfId;
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

	public ArrayList<String> getPhysicalIfIds() {
		return physicalIfIds;
	}

	public void setPhysicalIfIds(ArrayList<String> physicalIfIds) {
		this.physicalIfIds = physicalIfIds;
	}

	@Override
	public String toString() {
		return "LagIfCreateLagIf [lagIfId=" + lagIfId + ", minimumLinks=" + minimumLinks + ", linkSpeed=" + linkSpeed
				+ ", physicalIfIds=" + physicalIfIds + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (lagIfId == null) {
			throw new CheckDataException();
		}
		if (minimumLinks == null) {
			throw new CheckDataException();
		}
		if (linkSpeed == null) {
			throw new CheckDataException();
		}
		if (physicalIfIds.isEmpty()) {
			throw new CheckDataException();
		} else {
			for (String physi : physicalIfIds) {
				if (physi == null) {
					throw new CheckDataException();
				}
			}
		}
	}

}
