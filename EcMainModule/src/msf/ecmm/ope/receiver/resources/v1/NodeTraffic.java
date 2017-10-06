
package msf.ecmm.ope.receiver.resources.v1;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.resources.BaseResource;

@Path("/v1/internal/traffic/node_traffic")
public class NodeTraffic extends BaseResource {

	private static final String ERROR_CODE_280499 = "280499";


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNodeTraffic() {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.TrafficDataAcquisition;

		setErrorCode("", ERROR_CODE_280301, ERROR_CODE_280499);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();

		Response response = executeOperation(uriKeyMap, null);

		logger.trace(CommonDefinitions.END);
		return response;
	}
}
