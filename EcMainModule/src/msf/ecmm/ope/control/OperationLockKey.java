package msf.ecmm.ope.control;

public class OperationLockKey extends AbstractQueueEntryKey {

	private String node_id;

	public OperationLockKey(int opeType,String nid,String fbt){
		operationType =opeType;
		node_id = nid;
		fabricType = fbt;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fabricType == null) ? 0 : fabricType.hashCode());
		result = prime * result + ((node_id == null) ? 0 : node_id.hashCode());
		result = prime * result + operationType;
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
		OperationLockKey other = (OperationLockKey) obj;
		if (fabricType == null) {
			if (other.fabricType != null)
				return false;
		} else if (!fabricType.equals(other.fabricType))
			return false;
		if (node_id == null) {
			if (other.node_id != null)
				return false;
		} else if (!node_id.equals(other.node_id))
			return false;
		if (operationType != other.operationType)
			return false;
		return true;
	}

	public int getOperationType() {
		return operationType;
	}

	public String getNode_id() {
		return node_id;
	}

	public String getFabricType() {
		return fabricType;
	}

	@Override
	public String toString() {
		return "OperetionLockKey [operationType=" + operationType
				+ ", node_id=" + node_id + ", fabricType" + fabricType + "]";
	}

}
