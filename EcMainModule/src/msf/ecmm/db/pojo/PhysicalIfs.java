
package msf.ecmm.db.pojo;

import java.io.Serializable;

public class PhysicalIfs implements Serializable {

    private String node_id = null;
    private String if_name = null;

    public PhysicalIfs() {
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

    public String getPhysical_if_id() {
        return physical_if_id;
    }

    public void setPhysical_if_id(String physical_if_id) {
        this.physical_if_id = physical_if_id;
    }

    public String getIf_name() {
        return if_name;
    }

    public void setIf_name(String if_name) {
        this.if_name = if_name;
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
        if (physical_if_id != null) {
            hashCode ^= physical_if_id.hashCode();
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

        PhysicalIfs target = (PhysicalIfs) obj;
        if (this.node_type == target.getNode_type() && this.node_id.equals(target.getNode_id()) && this.physical_if_id.equals(target.getPhysical_if_id())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "PhysicalIfs [node_type=" + node_type + ", node_id=" + node_id + ", physical_if_id=" + physical_if_id + ", if_name=" + if_name + "]";
    }
}
