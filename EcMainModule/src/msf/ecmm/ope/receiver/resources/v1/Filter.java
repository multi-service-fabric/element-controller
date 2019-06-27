/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
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
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.AddDeleteFilter;
import msf.ecmm.ope.receiver.pojo.parts.AddFilters;
import msf.ecmm.ope.receiver.pojo.parts.DeleteFilters;
import msf.ecmm.ope.receiver.resources.BaseResource;

import com.google.gson.JsonSyntaxException;

/**
 * Filter Information Resources.
 *
 */
@Path("/v1/internal/filter/nodes")
public class Filter extends BaseResource {

  /** Filter Addition/Delete Conversion of pojo failed. */
  private static final String ERROR_CODE_480101 = "480101";
  /** Filter Addition/Delete Preparation for operation execution failed. */
  private static final String ERROR_CODE_480301 = "480301";
  /** Filter Addition/Delete Other exceptions. */
  private static final String ERROR_CODE_480499 = "480499";

  /** Acquisition of List of VLANIFFilter Information  Preparation for operation execution failed. */
  private static final String ERROR_CODE_490301 = "490301";
  /** Acquisition of List of VLANIFFilter Information  Other exceptions. */
  private static final String ERROR_CODE_490499 = "490499";

  /** Acquisition of VLANIFFilter Information  Preparation for operation execution failed. */
  private static final String ERROR_CODE_500301 = "500301";
  /** Acquisition of VLANIFFilter Information  Other exceptions. */
  private static final String ERROR_CODE_500499 = "500499";

  /** Acquisition of List of Physical IFFilter Information  Preparation for operation execution failed. */
  private static final String ERROR_CODE_510301 = "510301";
  /** Acquistion of Physical IFFilter Information  Other exceptions. */
  private static final String ERROR_CODE_510499 = "510499";

  /** Acquisition of Physical IFFilter Information  Preparatio for operation execution failed. */
  private static final String ERROR_CODE_520301 = "520301";
  /** Acquisiton of Physical IFFilter Information  Other exceptions. */
  private static final String ERROR_CODE_520499 = "520499";

  /** Acquisition of List of LagIFFilter Information  Preparation for operation execution failed. */
  private static final String ERROR_CODE_530301 = "530301";
  /** Acquisition of List of LagIFFilter Information  Other exceptions. */
  private static final String ERROR_CODE_530499 = "530499";

  /** Acquisition of List of LagIFFilter Information  Preparation for operation execution failed. */
  private static final String ERROR_CODE_540301 = "540301";
  /** Acquisition of LagIFFilter Information  Other exceptions. */
  private static final String ERROR_CODE_540499 = "540499";

  /** Fillter Addition  Extended Function Operation Name. */
  private static final String ADD_FILTER = "AddFilter";
  /** Filter Delete  Extended Function Operation Name. */
  private static final String DELETE_FILTER = "DeleteFilter";

  /** Filter Addition. */
  public static final String ADD_FILTER_DEFINITION = "add";
  /** Filter Delete. */
  public static final String DELETE_FILTER_DEFINITION = "delete";

  /** Filter Addition/Delete. */
  public static final int FILTER_ADD_DEL = 6;


  /**
   * When process request type or control type exists, overwrite the function on the occurrence side with the process to return POJO which corresponds to the process request type/control type.
   * If this is not necessary, return the input POJO as is with the base function and also configure the operation type.
   *
   * @param abstractRestMessage
   *          REST InputPOJO
   * @return POJO which corresponds to Processing Request Type/Control Type
   */
  @Override
  protected AbstractRestMessage getDetailRequest(AbstractRestMessage abstractRestMessage) {
    logger.trace(CommonDefinitions.START);

    AbstractRestMessage detailRequest = abstractRestMessage;

    try {
      if (operationGroupType == FILTER_ADD_DEL) {
        AddDeleteFilter addDeleteFilters = (AddDeleteFilter) abstractRestMessage;

        if (addDeleteFilters.getIfFilter().getTerms().get(0).getOperation().equals(ADD_FILTER_DEFINITION)) {
          operationType =  ExpandOperation.getInstance().get(ADD_FILTER).getOperationTypeId();
          detailRequest = new AddFilters();
          ((AddFilters) detailRequest).setIfFilter(addDeleteFilters.getIfFilter());
        } else if (addDeleteFilters.getIfFilter().getTerms().get(0).getOperation().equals(DELETE_FILTER_DEFINITION)) {
          operationType =  ExpandOperation.getInstance().get(DELETE_FILTER).getOperationTypeId();
          detailRequest = new DeleteFilters();
          ((DeleteFilters) detailRequest).setIfFilter(addDeleteFilters.getIfFilter());
        } else {
          logger.debug("Unkown action " + addDeleteFilters.getIfFilter().getTerms().get(0).getOperation());
          throw new JsonSyntaxException(""); 
        }
      }
    } catch (NullPointerException nullPointerException) {
      logger.debug(nullPointerException);
      throw new JsonSyntaxException("");
    }

    logger.trace(CommonDefinitions.END);
    return detailRequest;
  }

  /**
   * Filter Addition/Delete（VLAN）.
   *
   * @param nodeId
   *          Equipment ID
   * @param ifId
   *          IF ID
   * @return REST response
   */
  @PUT
  @Path("{node_id}/interfaces/vlan-ifs/{if_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addDeleteFilterVlan(@PathParam("node_id") String nodeId, @PathParam("if_id") String ifId) {
    logger.trace(CommonDefinitions.START);
    Response response = addAndDeleteFilter(nodeId, IF_TYPE_VLAN_IFS, ifId);
    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Filter Addition/Delete（Physical）.
   *
   * @param nodeId
   *          Equipment ID
   * @param ifId
   *          IF ID
   * @return REST response
   */
  @PUT
  @Path("{node_id}/interfaces/physical-ifs/{if_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addDeleteFilterPhysical(@PathParam("node_id") String nodeId, @PathParam("if_id") String ifId) {
    logger.trace(CommonDefinitions.START);
    Response response = addAndDeleteFilter(nodeId, IF_TYPE_PHYSICAL_IFS, ifId);
    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Filter Addition/Delete（LAG）.
   *
   * @param nodeId
   *          Equipment ID
   * @param ifId
   *          IF ID
   * @return REST response
   */
  @PUT
  @Path("{node_id}/interfaces/lag-ifs/{if_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addDeleteFilterLag(@PathParam("node_id") String nodeId, @PathParam("if_id") String ifId) {
    logger.trace(CommonDefinitions.START);
    Response response = addAndDeleteFilter(nodeId, IF_TYPE_LAG_IFS, ifId);
    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Filter Addition/Delete Common Processing.
   *
   * @param nodeId
   *          Equipment ID
   * @param ifType
   *          IF Type
   * @param ifId
   *          IF ID
   * @return REST response
   */
  public Response addAndDeleteFilter(String nodeId, String ifType, String ifId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.None;
    operationGroupType = FILTER_ADD_DEL;

    setErrorCode(ERROR_CODE_480101, ERROR_CODE_480301, ERROR_CODE_480499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);
    uriKeyMap.put(KEY_IF_TYPE, ifType);
    uriKeyMap.put(KEY_IF_ID, ifId);

    Response response = executeOperation(uriKeyMap, AddDeleteFilter.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Getting list of VLANIF Filter Information.
   *
   * @param nodeId
   *          Equipment ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/vlan-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getVlanInterfaceFilterList(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    expandOperationName = "GetVlanInterfaceFilterList";

    setErrorCode("", ERROR_CODE_490301, ERROR_CODE_490499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * VLANIFFilter Information Acquisition.
   *
   * @param nodeId
   *          Equipment ID
   * @param vlanIfId
   *          VLANIF ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/vlan-ifs/{vlan_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getVlanInterfaceFilter(@PathParam("node_id") String nodeId,
      @PathParam("vlan_if_id") String vlanIfId) {

    logger.trace(CommonDefinitions.START);

    expandOperationName = "GetVlanInterfaceFilter";

    setErrorCode("", ERROR_CODE_500301, ERROR_CODE_500499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);
    uriKeyMap.put(KEY_VLAN_IF_ID, vlanIfId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Getting list of Physical IFFilter Information.
   *
   * @param nodeId
   *          Equipment ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/physical-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPhysicalInterfaceFilterList(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    expandOperationName = "GetPhysicalInterfaceFilterList";

    setErrorCode("", ERROR_CODE_510301, ERROR_CODE_510499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Getting Physical IFFilter Information.
   *
   * @param nodeId
   *          Equipment ID
   * @param physicalIfId
   *          Physical IF ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/physical-ifs/{physical_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getPhysicalInterfaceFilter(@PathParam("node_id") String nodeId,
      @PathParam("physical_if_id") String physicalIfId) {

    logger.trace(CommonDefinitions.START);

    expandOperationName = "GetPhysicalInterfaceFilter";

    setErrorCode("", ERROR_CODE_520301, ERROR_CODE_520499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);
    uriKeyMap.put(KEY_PHYSICAL_IF_ID, physicalIfId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Getting list of LagIFFilter Information.
   *
   * @param nodeId
   *          Equipment ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/lag-ifs")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getLagInterfaceFilterList(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    expandOperationName = "GetLagInterfaceFilterList";

    setErrorCode("", ERROR_CODE_530301, ERROR_CODE_530499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Getting list of LagIF Filter Information.
   *
   * @param nodeId
   *          Equipment ID
   * @param lagIfId
   *          LagIF ID
   * @return REST response
   */
  @GET
  @Path("{node_id}/interfaces/lag-ifs/{lag_if_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getLagInterfaceFilter(@PathParam("node_id") String nodeId, @PathParam("lag_if_id") String lagIfId) {

    logger.trace(CommonDefinitions.START);

    expandOperationName = "GetLagInterfaceFilter";

    setErrorCode("", ERROR_CODE_540301, ERROR_CODE_540499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);
    uriKeyMap.put(KEY_LAG_IF_ID, lagIfId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }
}
