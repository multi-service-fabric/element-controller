
package msf.ecmm.db.pojo;

import java.io.Serializable;
import java.util.Set;

public class LagIfs implements Serializable {

    private String node_id = null;
    private String if_name = null;

    private Set<InternalLinkIfs> internalLinkIfsList;

    public LagIfs() {
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

    public String getLag_if_id() {
        return lag_if_id;
    }

    public void setLag_if_id(String lag_if_id) {
        this.lag_if_id = lag_if_id;
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

    public Set<InternalLinkIfs> getInternalLinkIfsList() {
        return internalLinkIfsList;
    }

    public void setInternalLinkIfsList(Set<InternalLinkIfs> internalLinkIfsList) {
        this.internalLinkIfsList = internalLinkIfsList;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        Integer tmp = this.node_type;
        hashCode ^= tmp.hashCode();
        if (node_id != null) {
            hashCode ^= node_id.hashCode();
        }
        if (lag_if_id != null) {
            hashCode ^= lag_if_id.hashCode();
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

        LagIfs target = (LagIfs) obj;
        if (this.node_type == target.getNode_type() && this.node_id.equals(target.getNode_id()) && this.lag_if_id.equals(target.getLag_if_id())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "LagIfs [node_type=" + node_type + ", node_id=" + node_id + ", lag_if_id=" + lag_if_id + ", if_name=" + if_name + ", internalLinkIfsList="
                + internalLinkIfsList + "]";
    }
}
