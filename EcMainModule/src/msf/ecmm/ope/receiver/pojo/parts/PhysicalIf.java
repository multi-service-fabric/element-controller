
package msf.ecmm.ope.receiver.pojo.parts;

public class PhysicalIf {

	private String ifName;

	public String getPhysicalIfId() {
		return physicalIfId;
	}

	public void setPhysicalIfId(String physicalIfId) {
		this.physicalIfId = physicalIfId;
	}

	public String getIfName() {
		return ifName;
	}

	public void setIfName(String ifName) {
		this.ifName = ifName;
	}

	@Override
	public String toString() {
		return "PhysicalIf [physicalIfId=" + physicalIfId + ", ifName=" + ifName + "]";
	}

}
