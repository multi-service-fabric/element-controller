
package msf.ecmm.ope.receiver.resources.v1;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CreateLagInterface;
import msf.ecmm.ope.receiver.pojo.UpdatePhysicalInterface;
import msf.ecmm.ope.receiver.resources.BaseResource;

@Path("/v1/internal/nodes")
public class Interface extends BaseResource {

	private static final String ERROR_CODE_170399 = "170399";

	private static final String ERROR_CODE_180399 = "180399";

	private static final String ERROR_CODE_190499 = "190499";

	private static final String ERROR_CODE_200301 = "200301";
	private static final String ERROR_CODE_230101 = "230101";
	private static final String ERROR_CODE_230399 = "230399";

	private static final String ERROR_CODE_240399 = "240399";

	private static final String ERROR_CODE_250499 = "250499";

	private static final String ERROR_CODE_260499 = "260499";

	@GET
	@Path("{fabric_type}/{node_id}/interfaces")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInterfaceList(@PathParam("fabric_type") String fabricType, @PathParam("node_id") String nodeId) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.AllIfInfoAcquisition;

		setErrorCode("", ERROR_CODE_170201, ERROR_CODE_170399);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_FABRIC_TYPE, fabricType);
		uriKeyMap.put(KEY_NODE_ID, nodeId);

		Response response = executeOperation(uriKeyMap, null);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@GET
	@Path("{fabric_type}/{node_id}/interfaces/physical-ifs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPhysicalInterfaceList(@PathParam("fabric_type") String fabricType,
			@PathParam("node_id") String nodeId) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.AllPhysicalIfInfoAcquisition;

		setErrorCode("", ERROR_CODE_180201, ERROR_CODE_180399);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_FABRIC_TYPE, fabricType);
		uriKeyMap.put(KEY_NODE_ID, nodeId);

		Response response = executeOperation(uriKeyMap, null);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@GET
	@Path("{fabric_type}/{node_id}/interfaces/physical-ifs/{physical_if_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPhysicalInterface(@PathParam("fabric_type") String fabricType,
			@PathParam("node_id") String nodeId, @PathParam("physical_if_id") String physicalIfId) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.PhysicalIfInfoAcquisition;

		setErrorCode("", ERROR_CODE_190301, ERROR_CODE_190499);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_FABRIC_TYPE, fabricType);
		uriKeyMap.put(KEY_NODE_ID, nodeId);
		uriKeyMap.put(KEY_PHYSICAL_IF_ID, physicalIfId);

		Response response = executeOperation(uriKeyMap, null);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@PUT
	@Path("{fabric_type}/{node_id}/interfaces/physical-ifs/{physical_if_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updatePhysicalInterface(@PathParam("fabric_type") String fabricType,
			@PathParam("node_id") String nodeId, @PathParam("physical_if_id") String physicalIfId) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.PhysicalIfInfoChange;

		setErrorCode(ERROR_CODE_200101, ERROR_CODE_200301, ERROR_CODE_200499);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_FABRIC_TYPE, fabricType);
		uriKeyMap.put(KEY_NODE_ID, nodeId);
		uriKeyMap.put(KEY_PHYSICAL_IF_ID, physicalIfId);

		Response response = executeOperation(uriKeyMap, UpdatePhysicalInterface.class);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@POST
	@Path("{fabric_type}/{node_id}/interfaces/lag-ifs")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createLagInterface(@PathParam("fabric_type") String fabricType, @PathParam("node_id") String nodeId) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.LagCreate;

		setErrorCode(ERROR_CODE_230101, ERROR_CODE_230202, ERROR_CODE_230399);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_FABRIC_TYPE, fabricType);
		uriKeyMap.put(KEY_NODE_ID, nodeId);

		Response response = executeOperation(uriKeyMap, CreateLagInterface.class);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@GET
	@Path("{fabric_type}/{node_id}/interfaces/lag-ifs")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLagInterfaceList(@PathParam("fabric_type") String fabricType, @PathParam("node_id") String nodeId) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.AllLagInfoAcquisition;

		setErrorCode("", ERROR_CODE_240201, ERROR_CODE_240399);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_FABRIC_TYPE, fabricType);
		uriKeyMap.put(KEY_NODE_ID, nodeId);

		Response response = executeOperation(uriKeyMap, null);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@GET
	@Path("{fabric_type}/{node_id}/interfaces/lag-ifs/{lag_if_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getLagInterface(@PathParam("fabric_type") String fabricType,
			@PathParam("node_id") String nodeId, @PathParam("lag_if_id") String lagIfId) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.LagInfoAcquisition;

		setErrorCode("", ERROR_CODE_250301, ERROR_CODE_250499);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_FABRIC_TYPE, fabricType);
		uriKeyMap.put(KEY_NODE_ID, nodeId);
		uriKeyMap.put(KEY_LAG_IF_ID, lagIfId);

		Response response = executeOperation(uriKeyMap, null);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@DELETE
	@Path("{fabric_type}/{node_id}/interfaces/lag-ifs/{lag_if_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteLagInterface(@PathParam("fabric_type") String fabricType,
			@PathParam("node_id") String nodeId, @PathParam("lag_if_id") String lagIfId) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.LagRemove;

		setErrorCode("", ERROR_CODE_260301, ERROR_CODE_260499);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_FABRIC_TYPE, fabricType);
		uriKeyMap.put(KEY_NODE_ID, nodeId);
		uriKeyMap.put(KEY_LAG_IF_ID, lagIfId);

		Response response = executeOperation(uriKeyMap, null);

		logger.trace(CommonDefinitions.END);
		return response;
	}

}
