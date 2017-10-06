package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="ospf")
public class L3SliceOspf {

	public L3SliceOspf() {
		super();
	}

	public Long getMetric() {
	    return metric;
	}

	public void setMetric(Long metric) {
	    this.metric = metric;
	}

	@Override
	public String toString() {
		return "L3SliceOspf [metric=" + metric + "]";
	}
}
