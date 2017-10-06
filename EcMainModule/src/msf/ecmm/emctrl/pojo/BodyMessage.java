package msf.ecmm.emctrl.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "config")
public class BodyMessage extends AbstractMessage {

	@XmlElement(name = "l3-slice")
	private L3SliceAddDelete l3SliceAddDelete = null;

	@XmlElement(name = "spine")
	private SpineAddDelete spineAddDelete = null;

	@XmlElement(name = "ce-lag")
	private CeLagAddDelete ceLagAddDelete = null;

	public BodyMessage() {
		super();
	}

	public L2SliceAddDelete getL2SliceAddDelete() {
		return l2SliceAddDelete;
	}

	public void setL2SliceAddDelete(L2SliceAddDelete l2SliceAddDelete) {
		this.l2SliceAddDelete = l2SliceAddDelete;
	}

	public L3SliceAddDelete getL3SliceAddDelete() {
		return l3SliceAddDelete;
	}

	public void setL3SliceAddDelete(L3SliceAddDelete l3SliceAddDelete) {
		this.l3SliceAddDelete = l3SliceAddDelete;
	}

	public LeafAddDelete getLeafAddDelete() {
		return leafAddDelete;
	}

	public void setLeafAddDelete(LeafAddDelete leafAddDelete) {
		this.leafAddDelete = leafAddDelete;
	}

	public SpineAddDelete getSpineAddDelete() {
		return spineAddDelete;
	}

	public void setSpineAddDelete(SpineAddDelete spineAddDelete) {
		this.spineAddDelete = spineAddDelete;
	}

	public InternalLinkLagAddDelete getInternalLinkLagAddDelete() {
		return internalLinkLagAddDelete;
	}

	public void setInternalLinkLagAddDelete(InternalLinkLagAddDelete internalLinkLagAddDelete) {
		this.internalLinkLagAddDelete = internalLinkLagAddDelete;
	}

	public CeLagAddDelete getCeLagAddDelete() {
		return ceLagAddDelete;
	}

	public void setCeLagAddDelete(CeLagAddDelete ceLagAddDelete) {
		this.ceLagAddDelete = ceLagAddDelete;
	}

	@Override
	public String toString() {
		return "BodyMessage [l2SliceAddDelete=" + l2SliceAddDelete + ", l3SliceAddDelete=" + l3SliceAddDelete
				+ ", leafAddDelete=" + leafAddDelete + ", spineAddDelete=" + spineAddDelete
				+ ", internalLinkLagAddDelete=" + internalLinkLagAddDelete + ", ceLagAddDelete=" + ceLagAddDelete + "]";
	}
}
