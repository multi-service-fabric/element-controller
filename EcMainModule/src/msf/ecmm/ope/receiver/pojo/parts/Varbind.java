
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class Varbind {

	private String value;

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Varbind [oid=" + oid + ", value=" + value + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (oid == null) {
			throw new CheckDataException();
		} else {
		}
		if (value == null) {
			throw new CheckDataException();
		} else {
		}
	}

}
