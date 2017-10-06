
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class InternalLinkIfAddNode {

	private LagIfAddNode lagIf;

	public String getInternalLinkIfId() {
		return internalLinkIfId;
	}

	public void setInternalLinkIfId(String internalLinkIfId) {
		this.internalLinkIfId = internalLinkIfId;
	}

	public LagIfAddNode getLagIf() {
		return lagIf;
	}

	public void setLagIf(LagIfAddNode lagIf) {
		this.lagIf = lagIf;
	}

	@Override
	public String toString() {
		return "InternalLinkIfAddNode [internalLinkIfId=" + internalLinkIfId + ", lagIf=" + lagIf + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (internalLinkIfId == null) {
			throw new CheckDataException();
		}
		if (lagIf == null) {
			throw new CheckDataException();
		} else {
			lagIf.check(ope);
		}
	}

}
