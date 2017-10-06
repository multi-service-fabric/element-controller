
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class OppositeNodeAddSpine {

	private String vpnType;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getVpnType() {
		return vpnType;
	}

	public void setVpnType(String vpnType) {
		this.vpnType = vpnType;
	}

	public InternalLinkIfAddNode getInternalLinkIfs() {
		return internalLinkIf;
	}

	public void setInternalLinkIfs(InternalLinkIfAddNode internalLinkIf) {
		this.internalLinkIf = internalLinkIf;
	}

	@Override
	public String toString() {
		return "OppositeNodeAddSpine [nodeId=" + nodeId + ", vpnType=" + vpnType + ", internalLinkIf="
				+ internalLinkIf + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (nodeId == null) {
			throw new CheckDataException();
		}
		if (vpnType == null) {
			throw new CheckDataException();
		}
		if (internalLinkIf == null) {
			throw new CheckDataException();
		} else {
			internalLinkIf.check(ope);
		}
	}

}
