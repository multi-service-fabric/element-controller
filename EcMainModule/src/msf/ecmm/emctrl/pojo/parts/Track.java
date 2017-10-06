package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Track {

	public Track() {
		super();
	}

	public List<TrackInterface> getTrackInterfaceList() {
	    return TrackInterfaceList;
	}

	public void setTrackInterfaceList(List<TrackInterface> TrackInterfaceList) {
	    this.TrackInterfaceList = TrackInterfaceList;
	}

	@Override
	public String toString() {
		return "Track [TrackInterfaceList=" + TrackInterfaceList + "]";
	}
}
