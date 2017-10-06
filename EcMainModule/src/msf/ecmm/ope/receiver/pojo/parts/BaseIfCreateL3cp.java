
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class BaseIfCreateL3cp {

	private String nodeId;

	private String type;

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

	public String getRouterId() {
		return routerId;
	}

	public void setRouterId(String routerId) {
		this.routerId = routerId;
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
		return "BaseIfCreateL3cp [nodeType=" + nodeType + ", nodeId=" + nodeId + ", routerId=" + routerId + ", type="
				+ type + ", ifId=" + ifId + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (nodeType == null) {
			throw new CheckDataException();
		}
		if (!nodeType.equals("leaf")) {
			throw new CheckDataException();
		}
		if (nodeId == null) {
			throw new CheckDataException();
		}
		if (routerId == null) {
			throw new CheckDataException();
		}
		if (type == null) {
			throw new CheckDataException();
		}
		if (!type.equals("lag") && !type.equals("physical")) {
			throw new CheckDataException();
		}
		if (ifId == null) {
			throw new CheckDataException();
		}
	}

}
