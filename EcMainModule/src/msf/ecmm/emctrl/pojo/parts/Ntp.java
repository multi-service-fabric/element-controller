package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Ntp {

	public Ntp() {
		super();
	}

	public String getServerAdddress() {
		return serverAdddress;
	}

	public void setServerAdddress(String serverAdddress) {
		this.serverAdddress = serverAdddress;
	}

	@Override
	public String toString() {
		return "Ntp [serverAdddress=" + serverAdddress + "]";
	}

}
