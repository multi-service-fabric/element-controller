/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.resources.v1;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.RegisterEquipmentType;
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
 * Model Information Resource
 */
@Path("/v1/internal/equipment-types")
public class EquipmentType extends BaseResource {

  /** Model Information Registration - input data check result is NG (json error) */
  private static final String ERROR_CODE_290101 = "290101";
  /** Model Information Registration - operation execution preparation failure */
  private static final String ERROR_CODE_020202 = "020202";
  /** Model Information Registration - other exceptions */
  private static final String ERROR_CODE_020399 = "020399";

  /** Model List Information Acquisition - operation execution preparation failure */
  private static final String ERROR_CODE_030101 = "030101";
  /** Model List Information Acquisition - other exceptions */
  private static final String ERROR_CODE_030299 = "030299";

  /** Model Information Acquisition - operation execution preparation failure */
  private static final String ERROR_CODE_040301 = "040301";
  /** Model Information Acquisition - other exceptions */
  private static final String ERROR_CODE_040499 = "040499";

  /** Model Information Deletion - operation execution preparation failure */
  private static final String ERROR_CODE_050301 = "050301";
  /** Model Information Deletion - other exceptions */
  private static final String ERROR_CODE_050499 = "050499";

  /**
   * Model Information Registration
   *
   * @return REST response
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response regiterEquipmentType() {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.DeviceInfoRegistration;

    setErrorCode(ERROR_CODE_290101, ERROR_CODE_020202, ERROR_CODE_020399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, RegisterEquipmentType.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Model List Information Acquisition
   *
   * @return REST response
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getEquipmentTypeList() {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.AllDeviceTypeInfoAcquisition;

    setErrorCode("", ERROR_CODE_030101, ERROR_CODE_030299);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Model Information Acquisition
   *
   * @param equipmentTypeId
   *          model ID
   * @return REST response
   */
  @GET
  @Path("{equipment_type_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getEquipmentType(@PathParam("equipment_type_id") String equipmentTypeId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.DeviceInfoAcquisition;

    setErrorCode("", ERROR_CODE_040301, ERROR_CODE_040499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_EQUIPMENT_TYPE_ID, equipmentTypeId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Model Information Deletion
   *
   * @param equipmentTypeId
   *          model ID
   * @return REST response
   */
  @DELETE
  @Path("{equipment_type_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response deleteEquipmentType(@PathParam("equipment_type_id") String equipmentTypeId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.DeviceInfoRemove;

    setErrorCode("", ERROR_CODE_050301, ERROR_CODE_050499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_EQUIPMENT_TYPE_ID, equipmentTypeId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }
}
