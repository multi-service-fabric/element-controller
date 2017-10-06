package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "l2-vpn")
public class L2Vpn {

	public L2Vpn() {
		super();
	}

	public L2VpnPim getL2VpnPim() {
		return l2VpnPim;
	}

	public void setL2VpnPim(L2VpnPim l2VpnPim) {
		this.l2VpnPim = l2VpnPim;
	}

	@Override
	public String toString() {
		return "L2Vpn [l2VpnPim=" + l2VpnPim + "]";
	}
}
