
package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.LagIfCreateLagIf;

public class CreateLagInterface extends AbstractRestMessage {

	public LagIfCreateLagIf getLagIf() {
		return lagIf;
	}

	public void setLagIf(LagIfCreateLagIf lagIf) {
		this.lagIf = lagIf;
	}

	@Override
	public String toString() {
		return "CreateLagInterface [lagIf=" + lagIf + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (lagIf == null) {
			throw new CheckDataException();
		} else {
			lagIf.check(ope);
		}
	}

}
