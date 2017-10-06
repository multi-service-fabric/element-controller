
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class L2vpnAddSpine {

	public PimAddSpine getPim() {
		return pim;
	}

	public void setPim(PimAddSpine pim) {
		this.pim = pim;
	}

	@Override
	public String toString() {
		return "L2vpnAddSpine [pim=" + pim + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (pim == null) {
			throw new CheckDataException();
		}
	}

}
