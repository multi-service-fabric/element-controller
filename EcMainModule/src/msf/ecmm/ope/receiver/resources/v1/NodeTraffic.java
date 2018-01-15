/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.resources.v1;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
 * Traffic Information Resource
 */
@Path("/v1/internal/traffic/node_traffic")
public class NodeTraffic extends BaseResource {

	/** Traffic Information List Acquisition - operation execution preparation failure */
	private static final String ERROR_CODE_280301 = "280301";
	/** Traffic Information List Acquisition - other exceptions */
	private static final String ERROR_CODE_280499 = "280499";

	 /** Traffic Information Acquisition - operation execution preparation failure */
  private static final String ERROR_CODE_440101 = "440101";
  /** Traffic Information Acquisition - other exceptions */
  private static final String ERROR_CODE_440299 = "440299";

	/**
	 * Traffic Information Acquisition
	 * @return REST response
	 */
	@GET
	@Path("{node_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodeTraffic(@PathParam("node_id") String nodeId) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.TrafficDataAcquisition;

		setErrorCode("", ERROR_CODE_440101, ERROR_CODE_440299);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

		Response response = executeOperation(uriKeyMap, null);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	 /**
   * Traffic Information List Acquisition
   * @return REST response
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllNodeTraffic() {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.TrafficDataAllAcquisition;

    setErrorCode("", ERROR_CODE_280301, ERROR_CODE_280499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }
}
