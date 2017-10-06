
package msf.ecmm.db.pojo;

import java.io.Serializable;

public class InternalLinkIfs implements Serializable {

    private int node_type = 0;
    private String lag_if_id = null;

    private LagIfs lagIfs;

    public InternalLinkIfs() {
        super();
    }

    public String getInternal_link_if_id() {
        return internal_link_if_id;
    }

    public void setInternal_link_if_id(String internal_link_if_id) {
        this.internal_link_if_id = internal_link_if_id;
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

    public Nodes getNodes() {
        return nodes;
    }

    public void setNodes(Nodes nodes) {
        this.nodes = nodes;
    }

    public LagIfs getLagIfs() {
        return lagIfs;
    }

    public void setLagIfs(LagIfs LagIfs) {
        this.lagIfs = LagIfs;
    }

    @Override
    public int hashCode() {
        int hashCode = 0;
        if (internal_link_if_id != null) {
            hashCode ^= internal_link_if_id.hashCode();
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

        InternalLinkIfs target = (InternalLinkIfs) obj;
        if (this.internal_link_if_id.equals(target.getInternal_link_if_id())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "InternalLinkIfs [internal_link_if_id=" + internal_link_if_id + ", node_type=" + node_type + ", node_id=" + node_id + ", lag_if_id=" + lag_if_id
                + "]";
    }
}
