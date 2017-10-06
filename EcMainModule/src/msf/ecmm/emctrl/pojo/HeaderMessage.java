package msf.ecmm.emctrl.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "edit-config")
public class HeaderMessage extends AbstractMessage {

	@XmlElement(name = "config")
	private BodyMessage bodyMessage = null;

	public HeaderMessage() {
		super();
	}

	public TargetMessage getTargetMessage() {
		return targetMessage;
	}

	public void setTargetMessage(TargetMessage targetMessage) {
		this.targetMessage = targetMessage;
	}

	public BodyMessage getBodyMessage() {
		return bodyMessage;
	}

	public void setBodyMessage(BodyMessage bodyMessage) {
		this.bodyMessage = bodyMessage;
	}

	@Override
	public String toString() {
		return "HeaderMessage [targetMessage=" + targetMessage + ", bodyMessage=" + bodyMessage + "]";
	}
}
