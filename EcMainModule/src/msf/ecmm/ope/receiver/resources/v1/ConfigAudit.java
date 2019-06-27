/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
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
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
 * Class of the Config-Audit-related resource
 */
@Path("/v1/internal/device_config_audit/nodes")
public class ConfigAudit extends BaseResource {

  /** In case the preparation of the Config-Audit list acquisition  has failed. */
  private static final String ERROR_CODE_620201 = "620201";
  /** The execeptions other than the above  in the Config-Audit list acquisition  */
  private static final String ERROR_CODE_620399 = "620399";

  /** In case the preparation of the Config-Audit acquisition  has failed. */
  private static final String ERROR_CODE_630301 = "630301";
  /** The execeptions other than the abovev in the Config-Audit acquisition. */
  private static final String ERROR_CODE_630499 = "630499";

  /**
   * Config-Audit List Acquisition
   *
   * @return REST response
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getConfigAuditList() {

    logger.trace(CommonDefinitions.START);

    expandOperationName = "AllConfigAuditAcquisition";

    setErrorCode("", ERROR_CODE_620201, ERROR_CODE_620399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Config-Audit Acquisition
   *
   * @param nodeId
   *          node ID
   * @return REST response
   */
  @GET
  @Path("{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getConfigAudit(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    expandOperationName = "ConfigAuditAcquisition";

    setErrorCode("", ERROR_CODE_630301, ERROR_CODE_630499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }
}
