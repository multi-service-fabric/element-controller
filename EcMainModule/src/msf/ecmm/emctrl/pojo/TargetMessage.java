package msf.ecmm.emctrl.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "target")
public class TargetMessage extends AbstractMessage {

	public TargetMessage() {
		super();
	}

	public String getRunning() {
		return running;
	}

	public void setRunning(String running) {
		this.running = running;
	}

	@Override
	public String toString() {
		return "TargetMessage [running=" + running + "]";
	}
}
