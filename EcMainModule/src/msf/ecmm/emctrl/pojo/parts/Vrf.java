package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Vrf {

	private String rt=null;

	@XmlElement (name="router-id")
	private String routerId=null;

	public Vrf() {
		super();
	}

	public String getVrfName() {
	    return vrfName;
	}

	public void setVrfName(String vrfName) {
	    this.vrfName = vrfName;
	}

	public String getRt() {
	    return rt;
	}

	public void setRt(String rt) {
	    this.rt = rt;
	}

	public String getRd() {
	    return rd;
	}

	public void setRd(String rd) {
	    this.rd = rd;
	}

	public String getRouterId() {
	    return routerId;
	}

	public void setRouterId(String routerId) {
	    this.routerId = routerId;
	}

	@Override
	public String toString() {
		return "Vrf [vrfName=" + vrfName + ", rt=" + rt + ", rd=" + rd
				+ ", routerId=" + routerId + "]";
	}
}
