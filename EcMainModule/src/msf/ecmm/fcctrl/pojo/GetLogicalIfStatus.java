package msf.ecmm.fcctrl.pojo;

import java.util.ArrayList;

import msf.ecmm.fcctrl.pojo.parts.NodeLogical;
import msf.ecmm.fcctrl.pojo.parts.Slice;

public class GetLogicalIfStatus extends AbstractResponse {

	private ArrayList<NodeLogical> nodes = new ArrayList<NodeLogical>();

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public ArrayList<NodeLogical> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<NodeLogical> nodes) {
		this.nodes = nodes;
	}

	public Slice getSlices() {
		return slices;
	}

	public void setSlices(Slice slices) {
		this.slices = slices;
	}

	@Override
	public String toString() {
		return "GetLogicalIfStatus [clusterId=" + clusterId + ", nodes=" + nodes + ", slices=" + slices + "]";
	}

}
