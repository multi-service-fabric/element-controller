
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class BgpAddNode {

	private String community;

	public Neighbor getNeighbor() {
		return neighbor;
	}

	public void setNeighbor(Neighbor neighbor) {
		this.neighbor = neighbor;
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
		return "BgpAddNode [neighbor=" + neighbor + ", community=" + community + ", communityWildcard="
				+ communityWildcard + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (neighbor == null) {
			throw new CheckDataException();
		} else {
			neighbor.check(ope);
		}
		if (community == null) {
			throw new CheckDataException();
		}
		if (communityWildcard == null) {
			throw new CheckDataException();
		}
	}

}
