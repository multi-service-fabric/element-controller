
package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.EquipmentAddNode;
import msf.ecmm.ope.receiver.pojo.parts.IfAddNode;
import msf.ecmm.ope.receiver.pojo.parts.Msdp;
import msf.ecmm.ope.receiver.pojo.parts.NodeAddNode;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodeAddSpine;
import msf.ecmm.ope.receiver.pojo.parts.VpnAddSpine;

public class AddSpine extends AbstractRestMessage {

	private NodeAddNode node;

	private ArrayList<OppositeNodeAddSpine> oppositeNodes = new ArrayList<OppositeNodeAddSpine>();

	private VpnAddSpine vpn;

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

	public ArrayList<OppositeNodeAddSpine> getOppositeNodes() {
		return oppositeNodes;
	}

	public void setOppositeNodes(ArrayList<OppositeNodeAddSpine> oppositeNodes) {
		this.oppositeNodes = oppositeNodes;
	}

	public Msdp getMsdp() {
		return msdp;
	}

	public void setMsdp(Msdp msdp) {
		this.msdp = msdp;
	}

	public VpnAddSpine getVpn() {
		return vpn;
	}

	public void setVpn(VpnAddSpine vpn) {
		this.vpn = vpn;
	}

	@Override
	public String toString() {
		return "AddSpine [equipment=" + equipment + ", node=" + node + ", ifs=" + ifs + ", oppositeNodes="
				+ oppositeNodes + ", msdp=" + msdp + ", vpn=" + vpn + "]";
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
		for (OppositeNodeAddSpine nodeAddSpine : oppositeNodes) {
			nodeAddSpine.check(ope);
		}
		if (msdp != null) {
			msdp.check(ope);
		}
		if (vpn == null) {
			throw new CheckDataException();
		} else {
			vpn.check(ope);
		}
	}

}
