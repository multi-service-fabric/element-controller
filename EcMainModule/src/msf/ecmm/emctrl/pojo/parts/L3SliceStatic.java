package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="static")
public class L3SliceStatic {

	@XmlElement (name="route6")
	private List<Route> routeList6=null;

	public L3SliceStatic() {
		super();
	}

	public List<Route> getRouteList() {
	    return routeList;
	}

	public void setRouteList(List<Route> routeList) {
	    this.routeList = routeList;
	}

	public List<Route> getRouteList6() {
	    return routeList6;
	}

	public void setRouteList6(List<Route> routeList6) {
	    this.routeList6 = routeList6;
	}

	@Override
	public String toString() {
		return "L3SliceStatic [routeList=" + routeList + ", routeList6="
				+ routeList6 + "]";
	}
}
