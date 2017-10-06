
package msf.ecmm.ope.receiver.resources.v1;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.ReceiverDefinitions;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.AddDeleteLeaf;
import msf.ecmm.ope.receiver.pojo.AddDeleteSpine;
import msf.ecmm.ope.receiver.pojo.RegisterLeaf;
import msf.ecmm.ope.receiver.pojo.RegisterSpine;
import msf.ecmm.ope.receiver.resources.BaseResource;

import com.google.gson.JsonSyntaxException;

@Path("/v1/internal/nodes")
public class Node extends BaseResource {

	private static final String ERROR_CODE_070202 = "070202";
	private static final String ERROR_CODE_080101 = "080101";
	private static final String ERROR_CODE_080499 = "080499";

	private static final String ERROR_CODE_120202 = "120202";
	private static final String ERROR_CODE_130101 = "130101";
	private static final String ERROR_CODE_130499 = "130499";

	@POST
	@Path("leafs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerLeaf() {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.LeafInfoRegistration;

		setErrorCode(ERROR_CODE_070101, ERROR_CODE_070202, ERROR_CODE_070399);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();

		Response response = executeOperation(uriKeyMap, RegisterLeaf.class);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@PUT
	@Path("leafs/{node_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDeleteLeaf(@PathParam("node_id") String nodeId) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.None;
		operationGroupType = ReceiverDefinitions.LEAF_ADD_DEL;

		setErrorCode(ERROR_CODE_080101, ERROR_CODE_800302, ERROR_CODE_080499);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_NODE_ID, nodeId);

		Response response = executeOperation(uriKeyMap, AddDeleteLeaf.class);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@POST
	@Path("spines")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerSpine() {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.SpineInfoRegistration;

		setErrorCode(ERROR_CODE_120101, ERROR_CODE_120202, ERROR_CODE_120399);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();

		Response response = executeOperation(uriKeyMap, RegisterSpine.class);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@PUT
	@Path("spines/{node_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addDeleteSpine(@PathParam("node_id") String nodeId) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.None;
		operationGroupType = ReceiverDefinitions.SPINE_ADD_DEL;

		setErrorCode(ERROR_CODE_130101, ERROR_CODE_800302, ERROR_CODE_130499);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_NODE_ID, nodeId);

		Response response = executeOperation(uriKeyMap, AddDeleteSpine.class);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@Override
	protected AbstractRestMessage getDetailRequest(AbstractRestMessage abstractRestMessage) {
		logger.trace(CommonDefinitions.START);

		AbstractRestMessage detailRequest = abstractRestMessage;

		if (operationGroupType == ReceiverDefinitions.LEAF_ADD_DEL) {
			AddDeleteLeaf addDeleteLeaf = (AddDeleteLeaf) abstractRestMessage;
			if (addDeleteLeaf.getAction().equals(ReceiverDefinitions.ADD_NODE)) {
				detailRequest = addDeleteLeaf.getAddNodeOption();
				operationType = OperationType.LeafAddition;
			} else if (addDeleteLeaf.getAction().equals(ReceiverDefinitions.DEL_NODE)) {
				detailRequest = addDeleteLeaf.getDelNodeOption();
				operationType = OperationType.LeafRemove;
			} else {
				logger.debug("Unkown action " + addDeleteLeaf.getAction());
			}
		}
		else if (operationGroupType == ReceiverDefinitions.SPINE_ADD_DEL) {
			AddDeleteSpine addDeleteSpine = (AddDeleteSpine) abstractRestMessage;
			if (addDeleteSpine.getAction().equals(ReceiverDefinitions.ADD_NODE)) {
				detailRequest = addDeleteSpine.getAddNodeOption();
				operationType = OperationType.SpineAddition;
			} else if (addDeleteSpine.getAction().equals(ReceiverDefinitions.DEL_NODE)) {
				detailRequest = addDeleteSpine.getDelNodeOption();
				operationType = OperationType.SpineRemove;
			} else {
				logger.debug("Unkown action " + addDeleteSpine.getAction());
			}
		}
		logger.trace(CommonDefinitions.END);
		return detailRequest;
	}
}
