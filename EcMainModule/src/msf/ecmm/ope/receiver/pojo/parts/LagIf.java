
package msf.ecmm.ope.receiver.pojo.parts;

public class LagIf {

	private String ifName;

	public String getLagIfId() {
		return lagIfId;
	}

	public void setLagIfId(String lagIfId) {
		this.lagIfId = lagIfId;
	}

	public String getIfName() {
		return ifName;
	}

	public void setIfName(String ifName) {
		this.ifName = ifName;
	}

	@Override
	public String toString() {
		return "LagIf [lagIfId=" + lagIfId + ", ifName=" + ifName + "]";
	}

}
