
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class CpsDeleteCps {

	public String getCpId() {
		return cpId;
	}

	public void setCpId(String cpId) {
		this.cpId = cpId;
	}

	@Override
	public String toString() {
		return "CpsDeleteCps [cpId=" + cpId + "]";
	}

	public void check(OperationType ope) throws CheckDataException{
		if(cpId == null){
			throw new CheckDataException();
		}
	}

}
