/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.resources.v1;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.UpdateVlanIf;
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
 * VLAN Interface Resource.
 */
@Path("/v1/internal/nodes/")
public class VlanInterface extends BaseResource {

  /** VLAN IF Information Change - input data check result is NG (json error). */
  private static final String ERROR_CODE_380101 = "380101";
  /** VLAN IF Information Change - operation execution preparation failure. */
  private static final String ERROR_CODE_380301 = "380301";
  /** VLAN IF Information Change - other exceptions. */
  private static final String ERROR_CODE_380499 = "380499";

  /** VLANIF Information Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_400301 = "400301";
  /** VLANIF Information Acquisition - other exceptions. */
  private static final String ERROR_CODE_400499 = "400499";

  /** VLANIF Information List Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_390201 = "390201";
  /** VLANIF Information List Acquisition - other exceptions. */
  private static final String ERROR_CODE_390399 = "390399";

  /**
   * VLAN IF Information Change.
   *
   * @param nodeId
   *          device ID
   * @param vlaIfId
   *          VLAN IF ID
   * @return REST response
   */
  @PUT
  @Path("{node_id}/interfaces/vlan-ifs/{vlan_if_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response updateVlanIf(@PathParam("node_id") String nodeId, @PathParam("vlan_if_id") String vlaIfId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.VlanIfChange;

    setErrorCode(ERROR_CODE_380101, ERROR_CODE_380301, ERROR_CODE_380499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);
    uriKeyMap.put(KEY_VLAN_IF_ID, vlaIfId);

    Response response = executeOperation(uriKeyMap, UpdateVlanIf.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * VLANIF Information Acquisition.
   *
   * @param nodeId
   *          device ID
   * @param vlanIfId
   *          VLANIF ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/vlan-ifs/{vlan_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getVlanInterface(@PathParam("node_id") String nodeId, @PathParam("vlan_if_id") String vlanIfId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.VlanIfInfoAcquisition;

    setErrorCode("", ERROR_CODE_400301, ERROR_CODE_400499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);
    uriKeyMap.put(KEY_VLAN_IF_ID, vlanIfId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * VLANIF Information List Acquisition.
   *
   * @param nodeId
   *          device ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/vlan-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getVlanInterfaceList(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.AllVlanIfInfoAcquisition;

    setErrorCode("", ERROR_CODE_390201, ERROR_CODE_390399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

}
