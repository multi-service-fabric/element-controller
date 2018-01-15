/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

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

/**
 * Interface Information Resource.
 *
 */
@Path("/v1/internal/nodes")
public class Interface extends BaseResource {

  /** IF Information List Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_170201 = "170201";
  /** IF Information List Acquisition - other exceptions. */
  private static final String ERROR_CODE_170399 = "170399";

  /** Physical IF Information List Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_180201 = "180201";
  /** Physical IF Information List Acquisition - other exceptions. */
  private static final String ERROR_CODE_180399 = "180399";

  /** Physical IF Information Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_190301 = "190301";
  /** Physical IF Information Acquisition - other exceptions. */
  private static final String ERROR_CODE_190499 = "190499";

  /** Physical IF Information Change - input data check result is NG (json error). */
  private static final String ERROR_CODE_200101 = "200101";
  /** Physical IF Information Change - operation execution preparation failure. */
  private static final String ERROR_CODE_200301 = "200301";
  /** Physical IF Information Change - other exceptions. */
  private static final String ERROR_CODE_200499 = "200499";

  /** LagIF Generation - input data check result is NG (json error). */
  private static final String ERROR_CODE_230101 = "230101";
  /** LagIF Generation - operation execution preparation failure. */
  private static final String ERROR_CODE_230202 = "230202";
  /** LagIF Generation - other exceptions. */
  private static final String ERROR_CODE_230399 = "230399";

  /** LagIF Information List Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_240201 = "240201";
  /** LagIF Information List Acquisition - other exceptions. */
  private static final String ERROR_CODE_240399 = "240399";

  /** LagIF Information Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_250301 = "250301";
  /** LagIF Information Acquisition - other exceptions. */
  private static final String ERROR_CODE_250499 = "250499";

  /** LagIF Deletion - operation execution preparation failure. */
  private static final String ERROR_CODE_260301 = "260301";
  /** LagIF Deletion - other exceptions. */
  private static final String ERROR_CODE_260499 = "260499";

  /** BreakoutIF Information Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_350301 = "350301";
  /** BreakoutIF Information Acquisition - other exceptions. */
  private static final String ERROR_CODE_350499 = "350499";

  /** BreakoutIF Information List Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_340201 = "340201";
  /** BreakoutIF Information List Acquisition - other exceptions. */
  private static final String ERROR_CODE_340399 = "340399";

  /**
   * IF Information List Acquisition.
   *
   * @param nodeId
   *          device ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getInterfaceList(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.AllIfInfoAcquisition;

    setErrorCode("", ERROR_CODE_170201, ERROR_CODE_170399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Physical IF Information List Acquisition.
   *
   * @param nodeId
   *          device ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/physical-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPhysicalInterfaceList(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.AllPhysicalIfInfoAcquisition;

    setErrorCode("", ERROR_CODE_180201, ERROR_CODE_180399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Physical IF Information Acquisition.
   *
   * @param nodeId
   *          device ID
   * @param physicalIfId
   *          physical IF-ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/physical-ifs/{physical_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPhysicalInterface(@PathParam("node_id") String nodeId,
      @PathParam("physical_if_id") String physicalIfId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.PhysicalIfInfoAcquisition;

    setErrorCode("", ERROR_CODE_190301, ERROR_CODE_190499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);
    uriKeyMap.put(KEY_PHYSICAL_IF_ID, physicalIfId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Physical IF Information Change.
   *
   * @param nodeId
   *          device ID
   * @param physicalIfId
   *          physical IF-ID
   * @return REST response
   */
  @PUT
  @Path("{node_id}/interfaces/physical-ifs/{physical_if_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updatePhysicalInterface(@PathParam("node_id") String nodeId,
      @PathParam("physical_if_id") String physicalIfId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.PhysicalIfInfoChange;

    setErrorCode(ERROR_CODE_200101, ERROR_CODE_200301, ERROR_CODE_200499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);
    uriKeyMap.put(KEY_PHYSICAL_IF_ID, physicalIfId);

    Response response = executeOperation(uriKeyMap, UpdatePhysicalInterface.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * LagIF Generation.
   *
   * @param nodeId
   *          device ID
   * @return REST response
   */
  @POST
  @Path("{node_id}/interfaces/lag-ifs")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createLagInterface(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.LagCreate;

    setErrorCode(ERROR_CODE_230101, ERROR_CODE_230202, ERROR_CODE_230399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, CreateLagInterface.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * LagIF Information List Acquisition.
   *
   * @param nodeId
   *          device ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/lag-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getLagInterfaceList(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.AllLagInfoAcquisition;

    setErrorCode("", ERROR_CODE_240201, ERROR_CODE_240399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * LagIF Information Acquisition.
   *
   * @param nodeId
   *          device ID
   * @param lagIfId
   *          LagIF-ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/lag-ifs/{lag_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getLagInterface(@PathParam("node_id") String nodeId, @PathParam("lag_if_id") String lagIfId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.LagInfoAcquisition;

    setErrorCode("", ERROR_CODE_250301, ERROR_CODE_250499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);
    uriKeyMap.put(KEY_LAG_IF_ID, lagIfId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * LagIF Deletion.
   *
   * @param nodeId
   *          device ID
   * @param lagIfId
   *          LagIF-ID
   * @return REST resonse
   */
  @DELETE
  @Path("{node_id}/interfaces/lag-ifs/{lag_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteLagInterface(@PathParam("node_id") String nodeId, @PathParam("lag_if_id") String lagIfId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.LagRemove;

    setErrorCode("", ERROR_CODE_260301, ERROR_CODE_260499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);
    uriKeyMap.put(KEY_LAG_IF_ID, lagIfId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * BreakoutIF Information List Acquisition.
   *
   * @param nodeId
   *          device ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/breakout-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBreakoutInterfaceList(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.AllBreakoutIfInfoAcquisition;

    setErrorCode("", ERROR_CODE_340201, ERROR_CODE_340399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * BreakoutIF Information Acquisition.
   *
   * @param nodeId
   *          device ID
   * @param breakoutIfId
   *          breakoutIFID
   *
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/breakout-ifs/{breakout_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getBreakoutInterface(@PathParam("node_id") String nodeId,
      @PathParam("breakout_if_id") String breakoutIfId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.BreakoutIfInfoAcquisition;

    setErrorCode("", ERROR_CODE_350301, ERROR_CODE_350499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);
    uriKeyMap.put(KEY_BREAKOUT_IF_ID, breakoutIfId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }
}
