
package msf.ecmm.ope.receiver.resources.v1;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.UpdateL3cp;
import msf.ecmm.ope.receiver.resources.BaseResource;

@Path("/v1/internal/slices/l3vpn")
public class L3Cp extends BaseResource {

	private static final String ERROR_CODE_270301 = "270301";
	@PUT
	@Path("{slice_id}/cps/{cp_id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateL3cp(@PathParam("slice_id") String sliceId, @PathParam("cp_id") String cpId) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.L3CPChange;

		setErrorCode(ERROR_CODE_270101, ERROR_CODE_270301, ERROR_CODE_270499);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_SLICE_ID, sliceId);
		uriKeyMap.put(KEY_CP_ID, cpId);

		Response response = executeOperation(uriKeyMap, UpdateL3cp.class);

		logger.trace(CommonDefinitions.END);
		return response;
	}
}
