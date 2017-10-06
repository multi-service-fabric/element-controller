package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="loopback-interface")
public class LoopbackInterface {

	private Integer prefix=null;

	public LoopbackInterface() {
		super();
	}

	public String getAddress() {
	    return address;
	}

	public void setAddress(String address) {
	    this.address = address;
	}

	public Integer getPrefix() {
	    return prefix;
	}

	public void setPrefix(Integer prefix) {
	    this.prefix = prefix;
	}

	@Override
	public String toString() {
		return "SpineLoopbackInterface [address=" + address + ", prefix="
				+ prefix + "]";
	}
}
