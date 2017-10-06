
package msf.ecmm.fcctrl.pojo.parts;

import java.util.ArrayList;

public class NodeLogical {

	private String nodeId;

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public ArrayList<InternalLinkIfsLogical> getInternalLinkIfs() {
		return internalLinkIfs;
	}

	public void setInternalLinkIfs(ArrayList<InternalLinkIfsLogical> internalLinkIfs) {
		this.internalLinkIfs = internalLinkIfs;
	}

	@Override
	public String toString() {
		return "NodeLogical [nodeType=" + nodeType + ", nodeId=" + nodeId + ", internalLinkIfs=" + internalLinkIfs
				+ "]";
	}

}
