package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="ce-lag-interface")
public class CeLagInterface {

	@XmlElement (name="minimum-links")
	private Long minimumLinks=null;

	@XmlElement (name="leaf-interface")
	private List<LeafInterface> leafInterfaceList=null;

	public CeLagInterface() {
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

	public List<LeafInterface> getLeafInterfaceList() {
	    return leafInterfaceList;
	}

	public void setLeafInterfaceList(List<LeafInterface> leafInterfaceList) {
	    this.leafInterfaceList = leafInterfaceList;
	}

	@Override
	public String toString() {
		return "CeLagInterface [name=" + name + ", minimumLinks="
				+ minimumLinks + ", linkSpeed=" + linkSpeed
				+ ", leafInterfaceList=" + leafInterfaceList + "]";
	}
}
