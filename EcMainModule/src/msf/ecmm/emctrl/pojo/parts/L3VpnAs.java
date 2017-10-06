package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="as")
public class L3VpnAs {

	public L3VpnAs() {
		super();
	}

	public Long getAsNumber() {
	    return asNumber;
	}

	public void setAsNumber(Long asNumber) {
	    this.asNumber = asNumber;
	}

	@Override
	public String toString() {
		return "L3VpnAs [asNumber=" + asNumber + "]";
	}
}
