package msf.ecmm.ope.control;

import java.util.Date;

public class OperationQueueEntry {

	private Date timestamp;

	public OperationQueueEntry(EcSession entry){
		operationId =entry;
		timestamp = new Date();
	}

	protected EcSession getOperationId() {
		return operationId;
	}

	protected Date getTimestamp() {
		return timestamp;
	}

	@Override
	public String toString() {
		return "OperationQueueEntry [operationId=" + operationId
				+ ", timestamp=" + timestamp + "]";
	}


}
