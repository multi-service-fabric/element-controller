/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.resources.v1;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.receiver.pojo.ExecutePingRequest;
import msf.ecmm.ope.receiver.pojo.GetOspfNeighborInfoRequest;
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
 * Ping, OSPF neighbor information resource
 */
@Path("/v1/internal/ec_ctrl/link-status")
public class PingOspf extends BaseResource {

  /** In case the check result of the input parameter is NG(json is NG). */
  private static final String ERROR_CODE_550101 = "550101";
  /** The preparation of the ping execution operation between nodes has failed. */
  private static final String ERROR_CODE_550301 = "550301";
  /** The execptions other than the above in the ping execution. */
  private static final String ERROR_CODE_550499 = "550499";

  /** In case NG is the check result of the input parameter for acquiring the OSPF neighbor information(json is NG). */	
  private static final String ERROR_CODE_560101 = "560101";
  /** The preparation of the execution operation for acquiring the OSPF neighbor information has failed. */
  private static final String ERROR_CODE_560301 = "560301";
  /** The execptions other than the above in the OSPF neighbor information acquisition. */
  private static final String ERROR_CODE_560499 = "560499";

  /**
   * The ping between nodes is executed.
   *
   * @return REST response
   */
  @POST
  @Path("ping")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response executePing() {

    logger.trace(CommonDefinitions.START);

    expandOperationName = "ExecutePing";

    setErrorCode(ERROR_CODE_550101, ERROR_CODE_550301, ERROR_CODE_550499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, ExecutePingRequest.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * The OSPF neighbor information is acquired.
   *
   * @return REST response
   */
  @POST
  @Path("ospf-neighbor")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response getOspfNeighborInfo() {

    logger.trace(CommonDefinitions.START);

    expandOperationName = "OspfNeighborAcquisition";

    setErrorCode(ERROR_CODE_560101, ERROR_CODE_560301, ERROR_CODE_560499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, GetOspfNeighborInfoRequest.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }
}
