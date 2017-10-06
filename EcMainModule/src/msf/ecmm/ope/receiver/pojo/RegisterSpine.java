
package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentRegisterNode;
import msf.ecmm.ope.receiver.pojo.parts.NodeRegisterNode;

	private EquipmentRegisterNode equipment;

	public EquipmentRegisterNode getEquipment() {
		return equipment;
	}

	public void setEquipment(EquipmentRegisterNode equipment) {
		this.equipment = equipment;
	}

	public NodeRegisterNode getNode() {
		return node;
	}

	public void setNode(NodeRegisterNode node) {
		this.node = node;
	}

	@Override
	public String toString() {
		return "RegisterSpine [equipment=" + equipment + ", node=" + node + "]";
	}

	public void check(OperationType operationType) throws CheckDataException {

		if (equipment == null) {
			throw new CheckDataException();
		} else {
			equipment.check(operationType);
		}
		if (node == null) {
			throw new CheckDataException();
		} else {
			node.check(operationType);
		}

	}

}
