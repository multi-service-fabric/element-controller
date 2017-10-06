package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "interna-lag")
public class InternalLag {

	@XmlElement(name = "minimum-links")
	private Long minimumLinks = null;

	private String address = null;

	@XmlElement(name = "internal-interface")
	private List<InternalInterface> internalInterface = null;

	public InternalLag() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getMinimumLinks() {
		return minimumLinks;
	}

	public void setMinimumLinks(Long minimumLinks) {
		this.minimumLinks = minimumLinks;
	}

	public String getLinkSpeed() {
		return linkSpeed;
	}

	public void setLinkSpeed(String linkSpeed) {
		this.linkSpeed = linkSpeed;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPrefix() {
		return prefix;
	}

	public void setPrefix(Integer prefix) {
		this.prefix = prefix;
	}

	public List<InternalInterface> getInternalInterface() {
		return internalInterface;
	}

	public void setInternalInterface(List<InternalInterface> internalInterface) {
		this.internalInterface = internalInterface;
	}

	@Override
	public String toString() {
		return "InternalLag [name=" + name + ", minimumLinks=" + minimumLinks + ", linkSpeed=" + linkSpeed
				+ ", address=" + address + ", prefix=" + prefix + ", internalInterface=" + internalInterface + "]";
	}
}
