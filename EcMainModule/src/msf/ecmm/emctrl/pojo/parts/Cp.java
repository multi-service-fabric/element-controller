
package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Cp {

    private String name = null;

    @XmlElement(name = "port-mode")
    private String portMode = null;

    @XmlElement(name = "multicast-group")
    private String multicastGroup = null;

    private Vrrp vrrp = null;

    @XmlElement(name = "static")
    private L3SliceStatic l3SliceStatic = null;

    public Cp() {
        super();
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getVlanId() {
        return vlanId;
    }

    public void setVlanId(Long vlanId) {
        this.vlanId = vlanId;
    }

    public String getPortMode() {
        return portMode;
    }

    public void setPortMode(String portMode) {
        this.portMode = portMode;
    }

    public Long getVni() {
        return vni;
    }

    public void setVni(Long vni) {
        this.vni = vni;
    }

    public String getMulticastGroup() {
        return multicastGroup;
    }

    public void setMulticastGroup(String multicastGroup) {
        this.multicastGroup = multicastGroup;
    }

    public CeInterface getCeInterface() {
        return ceInterface;
    }

    public void setCeInterface(CeInterface ceInterface) {
        this.ceInterface = ceInterface;
    }

    public Vrrp getVrrp() {
        return vrrp;
    }

    public void setVrrp(Vrrp vrrp) {
        this.vrrp = vrrp;
    }

    public L3SliceBgp getL3SliceBgp() {
        return l3SliceBgp;
    }

    public void setL3SliceBgp(L3SliceBgp l3SliceBgp) {
        this.l3SliceBgp = l3SliceBgp;
    }

    public L3SliceStatic getL3SliceStatic() {
        return l3SliceStatic;
    }

    public void setL3SliceStatic(L3SliceStatic l3SliceStatic) {
        this.l3SliceStatic = l3SliceStatic;
    }

    public L3SliceOspf getL3SliceOspf() {
        return l3SliceOspf;
    }

    public void setL3SliceOspf(L3SliceOspf l3SliceOspf) {
        this.l3SliceOspf = l3SliceOspf;
    }

    @Override
    public String toString() {
        return "Cp [name=" + name + ", vlanId=" + vlanId + ", portMode=" + portMode + ", vni=" + vni + ", multicastGroup=" + multicastGroup + ", ceInterface="
                + ceInterface + ", vrrp=" + vrrp + ", l3SliceBgp=" + l3SliceBgp + ", l3SliceStatic=" + l3SliceStatic + ", l3SliceOspf=" + l3SliceOspf + "]";
    }
}
