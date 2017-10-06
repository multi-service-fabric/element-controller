
package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.CpsDeleteCps;

public class BulkDeleteL3cp extends AbstractRestMessage {

	private String sliceId;

	public ArrayList<CpsDeleteCps> getCps() {
		return cps;
	}

	public void setCps(ArrayList<CpsDeleteCps> cps) {
		this.cps = cps;
	}

	public String getSliceId() {
		return sliceId;
	}

	public void setSliceId(String sliceId) {
		this.sliceId = sliceId;
	}

	@Override
	public String toString() {
		return "BulkDeleteL3cp [cps=" + cps + ", sliceId=" + sliceId + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if(cps.isEmpty()){
			throw new CheckDataException();
		}else{
			for (CpsDeleteCps deleteCps : cps) {
				deleteCps.check(ope);
			}
		}
		if(sliceId == null){
			throw new CheckDataException();
		}
	}

}
