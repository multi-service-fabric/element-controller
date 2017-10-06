
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class OppositeNodeDeleteNode {

	private InternalLinkIfDeleteNode internalLinkIfs;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public InternalLinkIfDeleteNode getInternalLinkIfs() {
		return internalLinkIfs;
	}

	public void setInternalLinkIfs(InternalLinkIfDeleteNode internalLinkIfs) {
		this.internalLinkIfs = internalLinkIfs;
	}

	@Override
	public String toString() {
		return "OppositeNodeDeleteNode [nodeId=" + nodeId + ", internalLinkIfs=" + internalLinkIfs + "]";
	}

	public void check(OperationType ope) throws CheckDataException {

		if (nodeId == null) {
			throw new CheckDataException();
		}
		if (internalLinkIfs == null) {
			throw new CheckDataException();
		} else {
			internalLinkIfs.check(ope);
		}
	}

}
