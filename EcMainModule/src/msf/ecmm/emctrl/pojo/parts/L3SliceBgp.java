package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="bgp")
public class L3SliceBgp {

	@XmlElement (name="remote-as-number")
	private Long remoteAsNumber=null;

	@XmlElement (name="remote-address")
	private String remoteAddress=null;

	@XmlElement (name="remote-address6")
	private String remoteAddress6=null;

	public L3SliceBgp() {
		super();
	}

	public void addMaster() {
	    this.master = new String("");
	}

	public void delMaster() {
	    this.master = null;
	}

	public Long getRemoteAsNumber() {
	    return remoteAsNumber;
	}

	public void setRemoteAsNumber(Long remoteAsNumber) {
	    this.remoteAsNumber = remoteAsNumber;
	}

	public String getLocalAddress() {
	    return localAddress;
	}

	public void setLocalAddress(String localAddress) {
	    this.localAddress = localAddress;
	}

	public String getRemoteAddress() {
	    return remoteAddress;
	}

	public void setRemoteAddress(String remoteAddress) {
	    this.remoteAddress = remoteAddress;
	}

	public String getLocalAddress6() {
	    return localAddress6;
	}

	public void setLocalAddress6(String localAddress6) {
	    this.localAddress6 = localAddress6;
	}

	public String getRemoteAddress6() {
	    return remoteAddress6;
	}

	public void setRemoteAddress6(String remoteAddress6) {
	    this.remoteAddress6 = remoteAddress6;
	}

	@Override
	public String toString() {
		return "L3SliceBgp [master=" + master + ", remoteAsNumber="
				+ remoteAsNumber + ", localAddress=" + localAddress
				+ ", remoteAddress=" + remoteAddress + ", localAddress6="
				+ localAddress6 + ", remoteAddress6=" + remoteAddress6 + "]";
	}
}
