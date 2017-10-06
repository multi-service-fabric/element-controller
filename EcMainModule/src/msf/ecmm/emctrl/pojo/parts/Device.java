
package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "device")
public class Device {

	@XmlElement(name = "vpn-type")
	private String vpnType = null;

	private Equipment equipment = null;

	@XmlElement(name = "management-interface")
	private ManagementInterface managementInterface = null;

	@XmlElement(name = "ce-lag-interface")
	private List<CeLagInterface> ceLagInterfaceList = null;

	private Ntp ntp = null;

	@XmlElement(name = "l3-vpn")
	private L3Vpn l3Vpn = null;

	public Device() {
		super();
	}

	public String getOperation() {
		return operation;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getVpnType() {
		return vpnType;
	}

	public void setVpnType(String vpnType) {
		this.vpnType = vpnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public List<InternalLag> getInternalLagList() {
		return internalLagList;
	}

	public void setInternalLagList(List<InternalLag> internalLagList) {
		this.internalLagList = internalLagList;
	}

	public ManagementInterface getManagementInterface() {
		return managementInterface;
	}

	public void setManagementInterface(ManagementInterface managementInterface) {
		this.managementInterface = managementInterface;
	}

	public LoopbackInterface getLoopbackInterface() {
		return loopbackInterface;
	}

	public void setLoopbackInterface(LoopbackInterface loopbackInterface) {
		this.loopbackInterface = loopbackInterface;
	}

	public List<CeLagInterface> getCeLagInterfaceList() {
		return ceLagInterfaceList;
	}

	public void setCeLagInterfaceList(List<CeLagInterface> ceLagInterfaceList) {
		this.ceLagInterfaceList = ceLagInterfaceList;
	}

	public Snmp getSnmp() {
		return snmp;
	}

	public void setSnmp(Snmp snmp) {
		this.snmp = snmp;
	}

	public Ntp getNtp() {
		return ntp;
	}

	public void setNtp(Ntp ntp) {
		this.ntp = ntp;
	}

	public Msdp getMsdp() {
		return msdp;
	}

	public void setMsdp(Msdp msdp) {
		this.msdp = msdp;
	}

	public L3Vpn getL3Vpn() {
		return l3Vpn;
	}

	public void setL3Vpn(L3Vpn l3Vpn) {
		this.l3Vpn = l3Vpn;
	}

	public L2Vpn getL2Vpn() {
		return l2Vpn;
	}

	public void setL2Vpn(L2Vpn l2Vpn) {
		this.l2Vpn = l2Vpn;
	}

	@Override
	public String toString() {
		return "Device [operation=" + operation + ", vpnType=" + vpnType + ", name=" + name + ", equipment="
				+ equipment + ", internalLagList="
				+ internalLagList + ", managementInterface=" + managementInterface + ", loopbackInterface="
				+ loopbackInterface + ", ceLagInterfaceList="
				+ ceLagInterfaceList + ", snmp=" + snmp + ", ntp=" + ntp + ", msdp=" + msdp + ", l3Vpn=" + l3Vpn
				+ ", l2Vpn=" + l2Vpn + "]";
	}
}
