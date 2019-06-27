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
import msf.ecmm.ope.receiver.pojo.NotifyOsUpgrade;
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
 * The resource class for notifying that the OS has been upgraded(The extended function for upgrading OS in the node).
 */
@Path("/v1/internal/notify_upgrade")
public class NodeOsUpgradeNotificationRecv extends BaseResource {

  /** In case the check of the input parameter is NG(json is NG). */
  private static final String ERROR_CODE_660101 = "660101";

  /** In case the preparation of the OS upgrade operation has failed. */
  private static final String ERROR_CODE_660201 = "660201";

  /** Thr execptions other than the above. */
  private static final String ERROR_CODE_660399 = "660399";


  /** The notification that the OS upgrade process has been completed. */
  public static final int NODE_OS_UPGRADE_NOTIFICATION = 300003;

  /**
   * The OS in the node is upgraded.
   *
   * @param status
   *         Result success or failed
   * @return REST response
   */
  @POST
  @Path("{status}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response nodeOsUpgradeNotification(@PathParam("status") String status) {
    logger.trace(CommonDefinitions.START);

    expandOperationName = "NodeOsUpgradeNotification";

    setErrorCode(ERROR_CODE_660101, ERROR_CODE_660201, ERROR_CODE_660399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_STATUS, status);

    Response response = executeOperation(uriKeyMap, NotifyOsUpgrade.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

}
