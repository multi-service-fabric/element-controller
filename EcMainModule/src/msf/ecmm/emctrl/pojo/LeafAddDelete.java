package msf.ecmm.emctrl.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import msf.ecmm.emctrl.pojo.parts.Device;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "leaf")
public class LeafAddDelete extends AbstractMessage {

	private String name = null;

	public LeafAddDelete() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	@Override
	public String toString() {
		return "LeafAddDelete [name=" + name + ", device=" + device + "]";
	}
}
