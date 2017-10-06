
package msf.ecmm.ope.receiver.resources.v1;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.execute.ecstate.ECMainStopper;
import msf.ecmm.ope.receiver.pojo.NotifyNodeStartUp;
import msf.ecmm.ope.receiver.pojo.NotifyReceiveSnmpTrap;
import msf.ecmm.ope.receiver.resources.BaseResource;

@Path("/v1/internal")
public class EcMainModule extends BaseResource {

	private static final String ERROR_CODE_300399 = "300399";

	private static final String ERROR_CODE_320399 = "320399";

	private static final String ERROR_CODE_310399 = "310399";

	private static final String ERROR_CODE_330201 = "330201";
	private static final String ERROR_CODE_290101 = "290101";
	private static final String ERROR_CODE_290499 = "290499";

	@POST
	@Path("ec_ctrl/stop/{stop_type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response requestStop(@PathParam("stop_type") String stopType) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.ECMainStopper;

		setErrorCode("", ERROR_CODE_300201, ERROR_CODE_300399);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_STOP_TYPE, stopType);

		Response response = executeOperation(uriKeyMap, null);

		if (isSuccess(response)) {
			ECMainStopper.systemExit();
		}

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@POST
	@Path("ec_ctrl/statuschg/{instruction_type}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response requestBusy(@PathParam("instruction_type") String instructionType) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.ObstructionStateController;

		setErrorCode("", ERROR_CODE_320201, ERROR_CODE_320399);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_INSTRUCTION_TYPE, instructionType);

		Response response = executeOperation(uriKeyMap, null);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@GET
	@Path("ec_ctrl/statusget")
	@Produces(MediaType.APPLICATION_JSON)
	public Response checkStatus() {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.ECMainStateConfirm;

		setErrorCode("", ERROR_CODE_310201, ERROR_CODE_310399);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();

		Response response = executeOperation(uriKeyMap, null);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@POST
	@Path("snmp/{link_status}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response notifyReceiveSnmpTrap(@PathParam("link_status") String linkStatus) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.SNMPTrapSignalRecieveNotification;

		setErrorCode(ERROR_CODE_330101, ERROR_CODE_330201, ERROR_CODE_330399);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_LINK_STATUS, linkStatus);

		Response response = executeOperation(uriKeyMap, NotifyReceiveSnmpTrap.class);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	@POST
	@Path("node_boot/{status}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response notifyNodeStartUp(@PathParam("status") String bootResult) {

		logger.trace(CommonDefinitions.START);

		operationType = OperationType.NodeAddedNotification;

		setErrorCode(ERROR_CODE_290101, ERROR_CODE_290301, ERROR_CODE_290499);

		HashMap<String, String> uriKeyMap = new HashMap<String, String>();
		uriKeyMap.put(KEY_STATUS, bootResult);

		Response response = executeOperation(uriKeyMap, NotifyNodeStartUp.class);

		logger.trace(CommonDefinitions.END);
		return response;
	}
}
