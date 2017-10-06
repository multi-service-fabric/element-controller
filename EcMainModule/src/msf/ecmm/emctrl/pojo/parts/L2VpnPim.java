package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="pim")
public class L2VpnPim {

	@XmlElement (name="self-rp-address")
	private String selfRpAddress=null;

	public L2VpnPim() {
		super();
	}

	public String getOtherRpAddress() {
	    return otherRpAddress;
	}

	public void setOtherRpAddress(String otherRpAddress) {
	    this.otherRpAddress = otherRpAddress;
	}

	public String getSelfRpAddress() {
	    return selfRpAddress;
	}

	public void setSelfRpAddress(String selfRpAddress) {
	    this.selfRpAddress = selfRpAddress;
	}

	public String getRpAddress() {
	    return rpAddress;
	}

	public void setRpAddress(String rpAddress) {
	    this.rpAddress = rpAddress;
	}

	@Override
	public String toString() {
		return "L2VpnPim [otherRpAddress=" + otherRpAddress
				+ ", selfRpAddress=" + selfRpAddress + ", rpAddress="
				+ rpAddress + "]";
	}
}
