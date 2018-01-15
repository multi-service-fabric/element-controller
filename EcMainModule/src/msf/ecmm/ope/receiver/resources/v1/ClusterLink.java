/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.resources.v1;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonSyntaxException;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.ReceiverDefinitions;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.ControlLogicalInterface;
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
 * Inter-Cluster Link Information Resource.
 */
@Path("/v1/internal/intarfaces/operations")
public class ClusterLink extends BaseResource {

  /** Process Execution Request - in case input data check result is NG (json error). */
  private static final String ERROR_CODE_360101 = "360101";
  /** Process Execution Request - operation execution preparation failure. */
  private static final String ERROR_CODE_360301 = "360301";
  /** Process Execution Request - other exceptions. */
  private static final String ERROR_CODE_360499 = "360499";

  /**
   * Process Execution Request.
   *
   * @return REST response
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response controlLogicalInterface() {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.None;
    operationGroupType = ReceiverDefinitions.MSF_LOGICAL_IF_CONTROL;

    setErrorCode(ERROR_CODE_360101, ERROR_CODE_360301, ERROR_CODE_360499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, ControlLogicalInterface.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * When process request type or control type exists, overwrite the function on the occurrence side with the process to return POJO which corresponds to the process request type/control type.<br>
   * If this is not necessary, return the input POJO as is with the base function and also configure the operation type..
   *
   * @param abstractRestMessage
   *          REST input POJO
   * @return POJO corresponding to the process request type/control type
   */
  @Override
  protected AbstractRestMessage getDetailRequest(AbstractRestMessage abstractRestMessage) {
    logger.trace(CommonDefinitions.START);

    AbstractRestMessage detailRequest = abstractRestMessage;

    try {
      if (operationGroupType == ReceiverDefinitions.MSF_LOGICAL_IF_CONTROL) {
        ControlLogicalInterface controlLogicalInterface = (ControlLogicalInterface) abstractRestMessage;
        if (controlLogicalInterface.getAction().equals(ReceiverDefinitions.CREATE_CLUSTERSLINK)) {
          detailRequest = controlLogicalInterface.getAddInterClusterLinkOption();
          operationType = OperationType.BetweenClustersLinkCreate;
        } else if (controlLogicalInterface.getAction().equals(ReceiverDefinitions.DELETE_CLUSTERSLINK)) {
          detailRequest = controlLogicalInterface.getDelInterClusterLinkOption();
          operationType = OperationType.BetweenClustersLinkDelete;
        } else {
          logger.debug("Unkown action " + controlLogicalInterface.getAction());
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
}
