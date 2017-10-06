package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Snmp {

	private String community = null;

	public Snmp() {
		super();
	}

	public String getServerAdddress() {
		return serverAdddress;
	}

	public void setServerAdddress(String serverAdddress) {
		this.serverAdddress = serverAdddress;
	}

	public String getCommunity() {
		return community;
	}

	public void setCommunity(String community) {
		this.community = community;
	}

	@Override
	public String toString() {
		return "Snmp [serverAdddress=" + serverAdddress + ", community=" + community + "]";
	}

}
