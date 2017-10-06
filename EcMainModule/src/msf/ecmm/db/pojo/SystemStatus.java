package msf.ecmm.db.pojo;

import java.io.Serializable;

public class SystemStatus implements Serializable {

	private int blockade_status=0;

	public SystemStatus() {
		super();
	}

	public int getService_status() {
	    return service_status;
	}

	public void setService_status(int service_status) {
	    this.service_status = service_status;
	}

	public int getBlockade_status() {
	    return blockade_status;
	}

	public void setBlockade_status(int blockade_status) {
	    this.blockade_status = blockade_status;
	}

	@Override
	public int hashCode() {
		Integer tmp1 = service_status;
		Integer tmp2 = blockade_status;
		return tmp1.hashCode() + tmp2.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj){
			return true;
		}

		if(obj == null || this.hashCode() != obj.hashCode()){
			return false;
		}

		SystemStatus target = (SystemStatus)obj;
		if (this.service_status == target.getService_status() &&
			this.blockade_status == target.getBlockade_status()) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "SystemStatus [service_status=" + service_status
				+ ", blockade_status=" + blockade_status + "]";
	}
}
