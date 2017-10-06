
package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class IfAddNode {

	public ArrayList<InternalLinkIfAddNode> getInternalLinkIfs() {
		return internalLinkIfs;
	}

	public void setInternalLinkIfs(ArrayList<InternalLinkIfAddNode> internalLinkIfs) {
		this.internalLinkIfs = internalLinkIfs;
	}

	@Override
	public String toString() {
		return "IfAddNode [internalLinkIfs=" + internalLinkIfs + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		for (InternalLinkIfAddNode ifAddNode : internalLinkIfs) {
			ifAddNode.check(ope);
		}
	}

}
