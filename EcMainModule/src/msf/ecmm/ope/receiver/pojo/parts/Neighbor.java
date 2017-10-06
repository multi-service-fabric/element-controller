
package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class Neighbor {

	public ArrayList<String> getAddresses() {
		return addresses;
	}

	public void setAddresses(ArrayList<String> addresses) {
		this.addresses = addresses;
	}

	@Override
	public String toString() {
		return "Neighbor [addresses=" + addresses + "]";
	}

	public void check(OperationType ope) throws CheckDataException {
		if (addresses.isEmpty()) {
			throw new CheckDataException();
		} else {
			for (String addr : addresses) {
				if (addr == null) {
					throw new CheckDataException();
				}
			}
		}
	}

}
