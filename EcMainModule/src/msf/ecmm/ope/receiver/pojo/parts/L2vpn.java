
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class L2vpn {

	public PimAddLeaf getPim() {
		return pim;
	}

	public void setPim(PimAddLeaf pim) {
		this.pim = pim;
	}

	@Override
	public String toString() {
		return "L2vpn [pim=" + pim + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (pim == null) {
			throw new CheckDataException();
		} else {
			pim.check(ope);
		}
	}

}
