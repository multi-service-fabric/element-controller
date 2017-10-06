

package msf.ecmm.ope.receiver.resources.v1;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.ReceiverDefinitions;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.Operations;
import msf.ecmm.ope.receiver.resources.BaseResource;

import com.google.gson.JsonSyntaxException;

@Path("/v1/internal/ElementController/operations")
public class Cps extends BaseResource {

	private static final String ERROR_CODE_010302 = "010302";
	@POST
    @Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response operations() {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.None;
		operationGroupType = ReceiverDefinitions.ALL_CP_OPERATION;

		setErrorCode(ERROR_CODE_010101, ERROR_CODE_010302, ERROR_CODE_010499);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();

		Response response = executeOperation(uriKeyMap, Operations.class);

		logger.trace(CommonDefinitions.END);
		return response;
	}



	@Override
	protected AbstractRestMessage getDetailRequest(AbstractRestMessage abstractRestMessage) {
		logger.trace(CommonDefinitions.START);

		AbstractRestMessage detailRequest = abstractRestMessage;

		if (operationGroupType == ReceiverDefinitions.ALL_CP_OPERATION) {
			Operations operations = (Operations)abstractRestMessage;
			if (operations.getAction().equals(ReceiverDefinitions.CREATE_L2CPS)) {
				detailRequest = operations.getCreateL2cpsOption();
				operationType = OperationType.AllL2CPCreate;
			} else if (operations.getAction().equals(ReceiverDefinitions.DELETE_L2CPS)) {
				detailRequest = operations.getDeleteL2cpsOption();
				operationType = OperationType.AllL2CPRemove;
			} else if (operations.getAction().equals(ReceiverDefinitions.CREATE_L3CPS)) {
				detailRequest = operations.getCreateL3cpsOption();
				operationType = OperationType.AllL3CPCreate;
			} else if (operations.getAction().equals(ReceiverDefinitions.DELETE_L3CPS)) {
				detailRequest = operations.getDeleteL3cpsOption();
				operationType = OperationType.AllL3CPRemove;
			} else {
				logger.debug("Unkown action " + operations.getAction());
			}
		}

		logger.trace(CommonDefinitions.END);
		return detailRequest;
	}
}
