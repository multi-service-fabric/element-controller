package msf.ecmm.emctrl.pojo.parts;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="bgp")
public class L3VpnBgp {

	private String community=null;

	public L3VpnBgp() {
		super();
	}

	public List<L3VpnNeighbor> getL3VpnNeighbor() {
	    return l3VpnNeighbor;
	}

	public void setL3VpnNeighbor(List<L3VpnNeighbor> l3VpnNeighbor) {
	    this.l3VpnNeighbor = l3VpnNeighbor;
	}

	public String getCommunity() {
	    return community;
	}

	public void setCommunity(String community) {
	    this.community = community;
	}

	public String getCommunityWildcard() {
	    return communityWildcard;
	}

	public void setCommunityWildcard(String communityWildcard) {
	    this.communityWildcard = communityWildcard;
	}

	@Override
	public String toString() {
		return "L3VpnBgp [l3VpnNeighbor=" + l3VpnNeighbor + ", community="
				+ community + ", communityWildcard=" + communityWildcard + "]";
	}
}
