
package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentAddNode;
import msf.ecmm.ope.receiver.pojo.parts.IfAddNode;
import msf.ecmm.ope.receiver.pojo.parts.NodeAddNode;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodeAddLeaf;
import msf.ecmm.ope.receiver.pojo.parts.VpnAddLeaf;

public class AddLeaf extends AbstractRestMessage {

	private NodeAddNode node;

	private ArrayList<OppositeNodeAddLeaf> oppositeNodes = new ArrayList<OppositeNodeAddLeaf>();

	public EquipmentAddNode getEquipment() {
		return equipment;
	}

	public void setEquipment(EquipmentAddNode equipment) {
		this.equipment = equipment;
	}

	public NodeAddNode getNode() {
		return node;
	}

	public void setNode(NodeAddNode node) {
		this.node = node;
	}

	public IfAddNode getIfs() {
		return ifs;
	}

	public void setIfs(IfAddNode ifs) {
		this.ifs = ifs;
	}

	public ArrayList<OppositeNodeAddLeaf> getOppositeNodes() {
		return oppositeNodes;
	}

	public void setOppositeNodes(ArrayList<OppositeNodeAddLeaf> oppositeNodes) {
		this.oppositeNodes = oppositeNodes;
	}

	public VpnAddLeaf getVpn() {
		return vpn;
	}

	public void setVpn(VpnAddLeaf vpn) {
		this.vpn = vpn;
	}

	@Override
	public String toString() {
		return "AddLeaf [equipment=" + equipment + ", node=" + node + ", ifs=" + ifs + ", oppositeNodes="
				+ oppositeNodes + ", vpn=" + vpn + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (equipment == null) {
			throw new CheckDataException();
		} else {
			equipment.check(ope);
		}
		if (node == null) {
			throw new CheckDataException();
		} else {
			node.check(ope);
		}
		if (ifs == null) {
			throw new CheckDataException();
		} else {
			ifs.check(ope);
		}
		for (OppositeNodeAddLeaf nodeAddLeaf : oppositeNodes) {
			nodeAddLeaf.check(ope);
		}
		if (vpn == null) {
			throw new CheckDataException();
		} else {
			vpn.check(ope);
		}
	}

}
