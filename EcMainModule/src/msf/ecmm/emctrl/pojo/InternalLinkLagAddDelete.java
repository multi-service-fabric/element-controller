package msf.ecmm.emctrl.pojo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import msf.ecmm.emctrl.pojo.parts.Device;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "internal-lag")
public class InternalLinkLagAddDelete extends AbstractMessage {

	private String name = null;

	public InternalLinkLagAddDelete() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Device> getDevice() {
		return device;
	}

	public void setDevice(List<Device> device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "InternalLinkLagAddDelete [name=" + name + ", device=" + device + "]";
	}
}
