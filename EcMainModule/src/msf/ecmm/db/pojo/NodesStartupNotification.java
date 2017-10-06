
package msf.ecmm.db.pojo;

import java.io.Serializable;

public class NodesStartupNotification implements Serializable {

	private String node_id = null;
	private Nodes nodes;

	public NodesStartupNotification() {
		super();
	}

	public int getNode_type() {
		return node_type;
	}

	public void setNode_type(int node_type) {
		this.node_type = node_type;
	}

	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	public int getNotification_reception_status() {
		return notification_reception_status;
	}

	public void setNotification_reception_status(int notification_reception_status) {
		this.notification_reception_status = notification_reception_status;
	}

	public Nodes getNodes() {
		return nodes;
	}

	public void setNodes(Nodes nodes) {
		this.nodes = nodes;
	}

	@Override
	public int hashCode() {
		int hashCode = 0;
		Integer tmp = this.node_type;
		hashCode ^= tmp.hashCode();
		if (node_id != null) {
			hashCode ^= node_id.hashCode();
		}
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || this.hashCode() != obj.hashCode()) {
			return false;
		}

		NodesStartupNotification target = (NodesStartupNotification) obj;
		if (this.node_type == target.getNode_type() && this.node_id.equals(target.getNode_id())) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "NodesStartupNotification [node_type=" + node_type + ", node_id=" + node_id
				+ ", notification_reception_status=" + notification_reception_status + "]";
	}

}
