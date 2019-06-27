/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.resources.v1;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.receiver.pojo.InternalLinkChange;
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
* Interface Information(Priority Leaf/Spine).
 */
@Path("/v1/internal/intarfaces/internal-link-ifs")
public class InterfacePriorityNode extends BaseResource {

  /** Internal Link IF Change In case input data check is NG¡Êjson incorrect¡Ë. */
  private static final String ERROR_CODE_470101 = "470101";
  /** Internal Link IF Change Operation execution preparation failed. */
  private static final String ERROR_CODE_470301 = "470301";
  /** Internal Link IF Change Other exceptions. */
  private static final String ERROR_CODE_470499 = "470499";

  /**
   * Internal Link IF Change.
   *
   * @return REST response
   */
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response internalLinkChange() {

    logger.trace(CommonDefinitions.START);

    expandOperationName = "InternalLinkIfChange";

    setErrorCode(ERROR_CODE_470101, ERROR_CODE_470301, ERROR_CODE_470499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, InternalLinkChange.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }
}
