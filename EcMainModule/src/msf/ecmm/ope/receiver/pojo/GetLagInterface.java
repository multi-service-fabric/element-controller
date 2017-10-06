
package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.receiver.pojo.parts.LagIf;

public class GetLagInterface extends AbstractResponseMessage {

	public LagIf getLagIfs() {
		return lagIf;
	}

	public void setLagIfs(LagIf lagIf) {
		this.lagIf = lagIf;
	}

	@Override
	public String toString() {
		return "GetLagInterface [lagIf=" + lagIf + "]";
	}
}
