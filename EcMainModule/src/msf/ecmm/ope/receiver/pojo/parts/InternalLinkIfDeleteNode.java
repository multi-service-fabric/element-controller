
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class InternalLinkIfDeleteNode {

	private LagIfDeleteNode lagIf;

	public String getInternalLinkIfs() {
		return internalLinkIfId;
	}

	public void setInternalLinkIfs(String internalLinkIfId) {
		this.internalLinkIfId = internalLinkIfId;
	}

	public LagIfDeleteNode getLagIf() {
		return lagIf;
	}

	public void setLagIf(LagIfDeleteNode lagIf) {
		this.lagIf = lagIf;
	}

	@Override
	public String toString() {
		return "InternalLinkIfDeleteNode [internalLinkIfId=" + internalLinkIfId + ", lagIf=" + lagIf + "]";
	}

	public void check(OperationType ope) throws CheckDataException {

		if (internalLinkIfId == null){
			throw new CheckDataException();
		}
		if (lagIf == null){
			throw new CheckDataException();
		} else {
			lagIf.check(ope);
		}
	}

}
