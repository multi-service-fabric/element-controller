package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="neighbor")
public class L3VpnNeighbor {

	public L3VpnNeighbor() {
		super();
	}

	public String getAddress() {
	    return address;
	}

	public void setAddress(String address) {
	    this.address = address;
	}

	@Override
	public String toString() {
		return "L3VpnNeighbor [address=" + address + "]";
	}
}
