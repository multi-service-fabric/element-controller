package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="l3-vpn")
public class L3Vpn {

	@XmlElement (name="as")
	private L3VpnAs l3VpnAs=null;

	public L3Vpn() {
		super();
	}

	public L3VpnBgp getL3VpnBgp() {
	    return l3VpnBgp;
	}

	public void setL3VpnBgp(L3VpnBgp l3VpnBgp) {
	    this.l3VpnBgp = l3VpnBgp;
	}

	public L3VpnAs getL3VpnAs() {
	    return l3VpnAs;
	}

	public void setL3VpnAs(L3VpnAs l3VpnAs) {
	    this.l3VpnAs = l3VpnAs;
	}

	@Override
	public String toString() {
		return "L3Vpn [l3VpnBgp=" + l3VpnBgp + ", l3VpnAs=" + l3VpnAs + "]";
	}
}

