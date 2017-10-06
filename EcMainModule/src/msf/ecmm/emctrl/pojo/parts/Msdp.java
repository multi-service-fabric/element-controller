package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Msdp {

	public Msdp() {
		super();
	}

	public MsdpPeer getMsdpPeer() {
		return msdpPeer;
	}

	public void setMsdpPeer(MsdpPeer msdpPeer) {
		this.msdpPeer = msdpPeer;
	}

	@Override
	public String toString() {
		return "Msdp [msdpPeer=" + msdpPeer + "]";
	}
}
