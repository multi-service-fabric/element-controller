
package msf.ecmm.ope.receiver.pojo.parts;

public class SwitchTraffic {

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

	public TrafficValue getTrafficValue() {
		return trafficValue;
	}

	public void setTrafficValue(TrafficValue trafficValue) {
		this.trafficValue = trafficValue;
	}

	@Override
	public String toString() {
		return "SwitchTraffic [nodeType=" + nodeType + ", nodeId=" + nodeId + ", trafficValue=" + trafficValue + "]";
	}

}
