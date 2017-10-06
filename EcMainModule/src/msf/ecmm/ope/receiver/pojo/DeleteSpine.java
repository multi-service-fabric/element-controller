
package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodeDeleteNode;

public class DeleteSpine extends AbstractRestMessage {

	public ArrayList<OppositeNodeDeleteNode> getOppositeNodes() {
		return oppositeNodes;
	}

	public void setOppositeNodes(ArrayList<OppositeNodeDeleteNode> oppositeNodes) {
		this.oppositeNodes = oppositeNodes;
	}

	@Override
	public String toString() {
		return "DeleteSpine [oppositeNodes=" + oppositeNodes + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		for(OppositeNodeDeleteNode deleteNode : oppositeNodes){
			deleteNode.check(ope);
		}
	}

}
