
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

public class Msdp {

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	@Override
	public String toString() {
		return "Msdp [peer=" + peer + "]";
	}

	public void check(OperationType ope) throws CheckDataException {

		if (peer == null) {
			throw new CheckDataException();
		} else {
			peer.check(ope);
		}
	}

}
