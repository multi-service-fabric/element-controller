package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "vrrp")
public class Vrrp {

	@XmlElement(name = "virtual-address")
	private String virtualAddress = null;

	private Integer priority = null;

	public Vrrp() {
		super();
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getVirtualAddress() {
		return virtualAddress;
	}

	public void setVirtualAddress(String virtualAddress) {
		this.virtualAddress = virtualAddress;
	}

	public String getVirtualAddress6() {
		return virtualAddress6;
	}

	public void setVirtualAddress6(String virtualAddress6) {
		this.virtualAddress6 = virtualAddress6;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	@Override
	public String toString() {
		return "Vrrp [groupId=" + groupId + ", virtualAddress=" + virtualAddress + ", virtualAddress6="
				+ virtualAddress6 + ", priority=" + priority + ", track=" + track + "]";
	}

}
