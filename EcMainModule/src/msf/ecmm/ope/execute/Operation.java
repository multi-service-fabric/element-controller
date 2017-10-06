package msf.ecmm.ope.execute;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.ope.control.EcSession;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class Operation {

	protected final static Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	private AbstractRestMessage inData;

	private HashMap<String,String> uriKeyMap;

	protected String fabricType = "" ;

	public Operation(AbstractRestMessage idt,HashMap<String,String> ukm){
		operationId = new EcSession(-1);
		inData = idt;
		uriKeyMap = ukm;
		if (uriKeyMap != null && uriKeyMap.containsKey(KEY_NODE_ID)) {
			nodeId = uriKeyMap.get(KEY_NODE_ID);
		}
		if (uriKeyMap != null && uriKeyMap.containsKey(KEY_FABRIC_TYPE)) {
			fabricType = uriKeyMap.get(KEY_FABRIC_TYPE);
		}
	}

	public boolean prepare(){

		logger.trace(CommonDefinitions.START);

		boolean judge = false;

		synchronized(OperationControlManager.getInstance()){
			judge = OperationControlManager.getInstance().judgeExecution(operationType);

			if(judge){
				operationId = OperationControlManager.getInstance().startOperation(this);

				if(operationId == null){
					judge = false;
				}else{
				}
			}else{
			}
		}

		logger.trace(CommonDefinitions.END + ", return=" + judge);

		return judge;
	}

	public abstract AbstractResponseMessage execute();

	public boolean close(){

		logger.trace(CommonDefinitions.START);

		try{
			operationId.close();
		}catch(IllegalArgumentException | InternalError e){
			logger.warn(LogFormatter.out.format(LogFormatter.MSG_403042),e);
			return false;
		}

		logger.trace(CommonDefinitions.END);

		return true;
	}

	abstract protected boolean checkInData();

	protected AbstractResponseMessage makeSuccessResponse(int rescode,AbstractResponseMessage res){

		res.setResponseCode(rescode);

		return res;
	}

	protected CommonResponse makeFailedResponse(int rescode,String errcode){

		CommonResponse res = new CommonResponse();

		res.setErrorCode(errcode);
		res.setResponseCode(rescode);

		return res;
	}

	protected void setOperationType(OperationType operationType) {
		this.operationType = operationType;
	}

	protected EcSession getOperationId() {
		return operationId;
	}

	protected AbstractRestMessage getInData() {
		return inData;
	}

	public OperationType getOperationType() {
		return operationType;
	}

	protected HashMap<String, String> getUriKeyMap() {
		return uriKeyMap;
	}

	public String getNodeId(){
		return nodeId ;
	}

	public String getFabricType() {
		return fabricType;
	}

	@Override
	public String toString() {
		return "Operation [operationId=" + operationId + ", inData=" + inData + ", operationType=" + operationType
				+ ", uriKeyMap=" + uriKeyMap + ", nodeId=" + nodeId + ", fabricType" + fabricType +"]";
	}

}
