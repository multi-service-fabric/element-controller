/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.resources.v1;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
  * Resource class for updating the controller file
  */
@Path("/v1/internal/ec_ctrl/ctrl-switch")
public class Switchover extends BaseResource {

  /** In case the check of the input parameter is NG(json is not OK). */
  private static final String ERROR_CODE_610101 = "610101";
  /** In case the preparation of the operation has failed. */
  private static final String ERROR_CODE_610301 = "610301";
  /** The execptions other than the above. */
  private static final String ERROR_CODE_610499 = "610499";

  /**
   * The switch-over process in the controller is executed.
   * @param controller
   *        The input arguments
   *
   * @return REST response
   */
  @POST
  @Produces(MediaType.APPLICATION_JSON)
  public Response switchoverController(@QueryParam("controller") String controller) {

    logger.trace(CommonDefinitions.START);

    expandOperationName = "ControllerSwitch";

    setErrorCode(ERROR_CODE_610101, ERROR_CODE_610301, ERROR_CODE_610499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_CONTROLLER, controller);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

}
