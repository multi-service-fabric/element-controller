package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="ce-interface")
public class CeInterface {

	private Integer prefix=null;

	private Integer prefix6=null;

	public CeInterface() {
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

	public String getAddress6() {
	    return address6;
	}

	public void setAddress6(String address6) {
	    this.address6 = address6;
	}

	public Integer getPrefix6() {
	    return prefix6;
	}

	public void setPrefix6(Integer prefix6) {
	    this.prefix6 = prefix6;
	}

	public Long getMtu() {
	    return mtu;
	}

	public void setMtu(Long mtu) {
	    this.mtu = mtu;
	}

	@Override
	public String toString() {
		return "L3SliceCeInterface [address=" + address + ", prefix=" + prefix
				+ ", address6=" + address6 + ", prefix6=" + prefix6 + ", mtu="
				+ mtu + "]";
	}
}
