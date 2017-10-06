package msf.ecmm.traffic.pojo;

import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;

public class NodeKeySet {


	private Nodes nodeId = null;

	public NodeKeySet() {
		super();
	}

	public Equipments getEquipmentsType() {
		return nodeType;
	}

	public void setEquipmentsType(Equipments nodeType) {
		this.nodeType = nodeType;
	}

	public Nodes getEquipmentsData() {
		return nodeId;
	}

	public void setEquipmentsData(Nodes nodeId) {
		this.nodeId = nodeId;
	}

	@Override
	public String toString() {
		return "NodeKeySet [nodeType=" + nodeType + ", nodeId=" + nodeId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result + ((nodeType == null) ? 0 : nodeType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeKeySet other = (NodeKeySet) obj;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		if (nodeType == null) {
			if (other.nodeType != null)
				return false;
		} else if (!nodeType.equals(other.nodeType))
			return false;
		return true;
	}
}
