
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class BaseIfCreateL2cp {

	private String nodeId;

	private String ifId;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIfId() {
		return ifId;
	}

	public void setIfId(String ifId) {
		this.ifId = ifId;
	}

	@Override
	public String toString() {
		return "BaseIfCreateL2cp [nodeType=" + nodeType + ", nodeId=" + nodeId + ", type=" + type + ", ifId=" + ifId
				+ "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (nodeType == null || !nodeType.equals("leaf")) {
			throw new CheckDataException();
		}
		if (nodeId == null) {
			throw new CheckDataException();
		}
		if (type == null || (!type.equals("lag") && !type.equals("physical"))) {
			throw new CheckDataException();
		}
		if (ifId == null) {
			throw new CheckDataException();
		}
	}

}
