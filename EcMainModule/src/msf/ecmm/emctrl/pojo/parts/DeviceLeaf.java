package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="device-leaf")
public class DeviceLeaf {

	private Vrf vrf=null;

	public DeviceLeaf() {
		super();
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public Vrf getVrf() {
	    return vrf;
	}

	public void setVrf(Vrf vrf) {
	    this.vrf = vrf;
	}

	public List<Cp> getCpList() {
	    return cpList;
	}

	public void setCpList(List<Cp> cpList) {
	    this.cpList = cpList;
	}

	@Override
	public String toString() {
		return "DeviceLeaf [name=" + name + ", vrf=" + vrf + ", cpList="
				+ cpList + "]";
	}
}
