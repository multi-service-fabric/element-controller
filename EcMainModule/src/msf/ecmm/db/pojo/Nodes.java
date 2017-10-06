
package msf.ecmm.db.pojo;

import java.io.Serializable;
import java.util.Set;

public class Nodes implements Serializable {

    private String node_id = null;
    private String node_name = null;
    private String snmp_community = null;

    private Set<NodesStartupNotification> nodesStartupNotificationList;
    private Set<LagIfs> lagIfsList;
    private Set<L3Cps> l3CpsList;

    public Nodes() {
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

    public String getEquipment_type_id() {
        return equipment_type_id;
    }

    public void setEquipment_type_id(String equipment_type_id) {
        this.equipment_type_id = equipment_type_id;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getManagement_if_address() {
        return management_if_address;
    }

    public void setManagement_if_address(String management_if_address) {
        this.management_if_address = management_if_address;
    }

    public String getSnmp_community() {
        return snmp_community;
    }

    public void setSnmp_community(String snmp_community) {
        this.snmp_community = snmp_community;
    }

    public Equipments getEquipments() {
        return equipments;
    }

    public void setEquipments(Equipments equipments) {
        this.equipments = equipments;
    }

    public Set<NodesStartupNotification> getNodesStartupNotificationList() {
        return nodesStartupNotificationList;
    }

    public void setNodesStartupNotificationList(Set<NodesStartupNotification> nodesStartupNotificationList) {
        this.nodesStartupNotificationList = nodesStartupNotificationList;
    }

    public Set<PhysicalIfs> getPhysicalIfsList() {
        return physicalIfsList;
    }

    public void setPhysicalIfsList(Set<PhysicalIfs> physicalIfsList) {
        this.physicalIfsList = physicalIfsList;
    }

    public Set<LagIfs> getLagIfsList() {
        return lagIfsList;
    }

    public void setLagIfsList(Set<LagIfs> lagIfsList) {
        this.lagIfsList = lagIfsList;
    }

    public Set<L2Cps> getL2CpsList() {
        return l2CpsList;
    }

    public void setL2CpsList(Set<L2Cps> l2CpsList) {
        this.l2CpsList = l2CpsList;
    }

    public Set<L3Cps> getL3CpsList() {
        return l3CpsList;
    }

    public void setL3CpsList(Set<L3Cps> l3CpsList) {
        this.l3CpsList = l3CpsList;
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

        Nodes target = (Nodes) obj;
        if (this.node_type == target.getNode_type() && this.node_id.equals(target.getNode_id())) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Nodes [node_type=" + node_type + ", node_id=" + node_id + ", equipment_type_id=" + equipment_type_id + ", node_name=" + node_name
                + ", management_if_address=" + management_if_address + ", snmp_community=" + snmp_community + ", nodesStartupNotificationList="
                + nodesStartupNotificationList + ", physicalIfsList=" + physicalIfsList + ", lagIfsList=" + lagIfsList + ", l2CpsList=" + l2CpsList + ", l3CpsList="
                + l3CpsList + "]";
    }
}
