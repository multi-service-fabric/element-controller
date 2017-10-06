package msf.ecmm.traffic.pojo;


public class SliceKeySet {

	private String cpId = "";

	public SliceKeySet() {
		super();
	}

	public String getSliceId() {
		return sliceId;
	}

	public void setSliceId(String sliceId) {
		this.sliceId = sliceId;
	}

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	@Override
	public String toString() {
		return "SliceKeySet [sliceId=" + sliceId + ", cpId=" + cpId + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpId == null) ? 0 : cpId.hashCode());
		result = prime * result + ((sliceId == null) ? 0 : sliceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SliceKeySet other = (SliceKeySet) obj;
		if (!cpId.equals(other.cpId))
			return false;
		if (!sliceId.equals(other.sliceId))
			return false;
		return true;
	}

}
