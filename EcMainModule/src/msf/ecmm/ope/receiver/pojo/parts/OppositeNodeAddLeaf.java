
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class OppositeNodeAddLeaf {

	private InternalLinkIfAddNode internalLinkIf;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public InternalLinkIfAddNode getInternalLinkIf() {
		return internalLinkIf;
	}

	public void setInternalLinkIf(InternalLinkIfAddNode internalLinkIf) {
		this.internalLinkIf = internalLinkIf;
	}

	@Override
	public String toString() {
		return "OppositeNodeAddLeaf [nodeId=" + nodeId + ", internalLinkIf=" + internalLinkIf + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (nodeId == null) {
			throw new CheckDataException();
		}
		if (internalLinkIf == null) {
			throw new CheckDataException();
		} else {
			internalLinkIf.check(ope);
		}
	}

}
