
package msf.ecmm.db.pojo;

import java.io.Serializable;

public class L2Cps implements Serializable {

    private String cp_id = null;
    private String node_id = null;
    private String base_lag_if_id = null;
    private int vlan_id = 0;

    public L2Cps() {
        super();
    }

    public String getSlice_id() {
        return slice_id;
    }

    public void setSlice_id(String slice_id) {
        this.slice_id = slice_id;
    }

    public String getCp_id() {
        return cp_id;
    }

    public void setCp_id(String cp_id) {
        this.cp_id = cp_id;
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

    public String getBase_physical_if_id() {
        return base_physical_if_id;
    }

    public void setBase_physical_if_id(String base_physical_if_id) {
        this.base_physical_if_id = base_physical_if_id;
    }

    public String getBase_lag_if_id() {
        return base_lag_if_id;
    }

    public void setBase_lag_if_id(String base_lag_if_id) {
        this.base_lag_if_id = base_lag_if_id;
    }

    public String getIf_name() {
        return if_name;
    }

    public void setIf_name(String if_name) {
        this.if_name = if_name;
    }

    public int getVlan_id() {
        return vlan_id;
    }

    public void setVlan_id(int vlan_id) {
        this.vlan_id = vlan_id;
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
        if (slice_id != null) {
            hashCode ^= slice_id.hashCode();
        }
        if (cp_id != null) {
            hashCode ^= cp_id.hashCode();
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

        L2Cps target = (L2Cps) obj;
        if (this.slice_id.equals(target.getSlice_id()) && this.cp_id.equals(target.getCp_id())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "L2Cps [slice_id=" + slice_id + ", cp_id=" + cp_id + ", node_type=" + node_type + ", node_id=" + node_id + ", base_physical_if_id="
                + base_physical_if_id + ", base_lag_if_id=" + base_lag_if_id + ", if_name=" + if_name + ", vlan_id=" + vlan_id + "]";
    }
}
