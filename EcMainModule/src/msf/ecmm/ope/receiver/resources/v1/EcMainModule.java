/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.resources.v1;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.execute.ecstate.ECMainStopper;
import msf.ecmm.ope.receiver.pojo.EmControllerReceiveStatus;
import msf.ecmm.ope.receiver.pojo.NotifyEmStatusLog;
import msf.ecmm.ope.receiver.pojo.NotifyEmStatusServer;
import msf.ecmm.ope.receiver.pojo.NotifyNodeStartUp;
import msf.ecmm.ope.receiver.pojo.NotifyReceiveSnmpTrap;
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
 * EC Internal IF Resource
 */
@Path("/v1/internal")
public class EcMainModule extends BaseResource {

  /** EC Stop - operation execution preparation failure */
  private static final String ERROR_CODE_300201 = "300201";
  /** EC Stop - other exceptions */
  private static final String ERROR_CODE_300399 = "300399";

  /** EC Blockage Status Change - operation execution preparation failure */
  private static final String ERROR_CODE_320201 = "320201";
  /** EC Blockage Status Change - other exceptions */
  private static final String ERROR_CODE_320399 = "320399";

  /** SNMPTrap Reception Notification - input data check result is NG (json error). */
  private static final String ERROR_CODE_330101 = "330101";
  /** SNMPTrap Reception Notification - operation execution preparation failure. */
  private static final String ERROR_CODE_330201 = "330201";
  /** SNMPTrap Reception Notification - other exceptions. */
  private static final String ERROR_CODE_330399 = "330399";

  /** Device Start-up Notification - input data check result is NG (json error). */
  private static final String ERROR_CODE_290101 = "290101";
  /** Device Start-up Notification - operation execution preparation failure.. */
  private static final String ERROR_CODE_290301 = "290301";
  /** Device Start-up Notification - other exceptions. */
  private static final String ERROR_CODE_290499 = "290499";

  /** Controller Status Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_310201 = "310201";
  /** Controller Status Acquisition - other exceptions. */
  private static final String ERROR_CODE_310399 = "310399";

  /** Controller Log Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_430201 = "430201";
  /** Controller Log Acquisition - other exceptions. */
  private static final String ERROR_CODE_430299 = "430299";

  /** Contoller Status Notification(switch-over) - operation execution preparation failure. */
  private static final String ERROR_CODE_450201 = "450201";
  /** Contoller Status Notification(switch-over) - other exceptions.. */
  private static final String ERROR_CODE_450399 = "450399";

  /** Contoller Status Notification(log) - operation execution preparation failure. */
  private static final String ERROR_CODE_640201 = "640201";
  /** Contoller Status Notification(log) -- other exceptions. */
  private static final String ERROR_CODE_640399 = "640399";

  /** Contoller Status Notification(server) - operation execution preparation failure. */
  private static final String ERROR_CODE_650201 = "650201";
  /** Contoller Status Notification(server) - other exceptions. */
  private static final String ERROR_CODE_650399 = "650399";

  /**
   * EC Stop
   *
   * @param stopType
   *          stop type
   * @return REST response
   */
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

  /**
   * EC Blockage Status Change
   *
   * @param instructionType
   *          instruction type
   * @return REST response
   */
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

  /**
   * SNMPTrap Reception Notification.
   *
   * @param linkStatus
   *          type
   * @return REST response
   */
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

  /**
   * Device Start-up Notification.
   *
   * @param bootResult
   *          start-up result
   * @return REST response
   */
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

  /**
   * Controller Log Acquisition.
   *
   * @param controller
   *          controller to be acquired
   * @param startdate
   *          acquisition start date and time
   * @param enddate
   *          acquisition end date and time
   * @param limitnumber
   *          the upper limit number of acquiring logs
   * @return REST response
   */
  @GET
  @Path("ec_ctrl/log")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getControllerLog(@QueryParam("controller") String controller,
      @QueryParam("start_date") String startdate, @QueryParam("end_date") String enddate,
      @QueryParam("limit_number") String limitnumber) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.ECMainLogAcquisition;

    setErrorCode("", ERROR_CODE_430201, ERROR_CODE_430299);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_CONTROLLER, controller);
    uriKeyMap.put(KEY_START_DATE, startdate);
    uriKeyMap.put(KEY_END_DATE, enddate);
    uriKeyMap.put(KEY_LIMIT_NUMBER, limitnumber);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Controller Status Acquisition.
   *
   * @param controller
   *          controller to be acquired
   * @param getinfo
   *          information to be acquired
   * @return REST response
   */
  @GET
  @Path("ec_ctrl/statusget")
  @Produces(MediaType.APPLICATION_JSON)
  public Response sendControllerState(@QueryParam("controller") String controller,
      @QueryParam("get_info") String getinfo) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.ECMainStateConfirm;

    setErrorCode("", ERROR_CODE_310201, ERROR_CODE_310399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_CONTROLLER, controller);
    uriKeyMap.put(KEY_GET_INFO, getinfo);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Controller Status Notification.
   *
   * @return REST response
   */

  @PUT
  @Path("ec_ctrl/statusnotify")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response requestEmStutas() {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.ControllerStateSendNotification;

    setErrorCode("", ERROR_CODE_450201, ERROR_CODE_450399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, EmControllerReceiveStatus.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Controller Status Notification(log).
   *
   * @return REST response
   */

  @PUT
  @Path("ec_ctrl/logstatusnotify")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response requestEmStatusLog() {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.ControllerStateSendNotificationLog;

    setErrorCode("", ERROR_CODE_640201, ERROR_CODE_640399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, NotifyEmStatusLog.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Controller Status Notification(server).
   *
   * @return REST response
   */

  @PUT
  @Path("ec_ctrl/serverstatusnotify")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response requestEmStatusServer() {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.ControllerStateSendNotificationServer;

    setErrorCode("", ERROR_CODE_650201, ERROR_CODE_650399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, NotifyEmStatusServer.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }
}
