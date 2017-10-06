
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class Ospf {

	public Integer getMetric() {
		return metric;
	}

	public void setMetric(Integer metric) {
		this.metric = metric;
	}

	@Override
	public String toString() {
		return "Ospf [metric=" + metric + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if(metric == null){
			throw new CheckDataException();
		}
	}

}
