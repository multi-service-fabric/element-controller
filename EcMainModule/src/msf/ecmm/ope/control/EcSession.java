package msf.ecmm.ope.control;

import msf.ecmm.common.CommonDefinitions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EcSession implements AutoCloseable{

	private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	public EcSession(int oid){
		operationId = oid;
	}

	public void close(){

		logger.trace(CommonDefinitions.START);

		if(operationId > 0){
			OperationControlManager.getInstance().terminateOperation(this);
		}else{
			throw new InternalError();
		}

		logger.trace(CommonDefinitions.END);

	}

	protected int getOperationId() {
		return operationId;
	}

	@Override
	public String toString() {
		return "EcSession [operationId=" + operationId + "]";
	}
}
