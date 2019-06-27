/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.resources.v1;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.receiver.pojo.NodeOsUpgradeRequest;
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
 * The resource Class for upgrading  OS in the noe(The extended function for upgrading OS in the node).
 */
@Path("/v1/internal/upgrade")
public class NodeOsUpgradeRecv extends BaseResource {

  /** In case the check of the input parameter is NG(json is NG). */
  private static final String ERROR_CODE_600101 = "600101";
  /** In case the preparation of the OS upgrade operation has failed. */
  private static final String ERROR_CODE_600301 = "600301";
  /** Thr execptions other than the above. */
  private static final String ERROR_CODE_600499 = "600499";

  /** The OS upgrade in the node. */ 
  public static final int NODE_OS_UPGRADE = 300002;

  /**
   * The OS upgrade in the node.
   *
   * @param nodeId
   *          The node ID
   * @return  REST response
   */
  @POST
  @Path("{node_id}/node_os")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response nodeOsUpgrade(@PathParam("node_id") String nodeId) {
    logger.trace(CommonDefinitions.START);

    expandOperationName = "NodeOsUpgrade";

    setErrorCode(ERROR_CODE_600101, ERROR_CODE_600301, ERROR_CODE_600499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, NodeOsUpgradeRequest.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

}
