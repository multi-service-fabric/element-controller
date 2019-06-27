/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.resources.v1;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.JsonSyntaxException;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.ReceiverDefinitions;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.AddDeleteNode;
import msf.ecmm.ope.receiver.pojo.NodeInfoUpdate;
import msf.ecmm.ope.receiver.pojo.RecoverNodeService;
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
 * Device Information Resource.
 */
@Path("/v1/internal/nodes")
public class Node extends BaseResource {

  /** Device Extention/Removal - input parameter check result is NG (json error). */
  private static final String ERROR_CODE_080101 = "080101";
  /** Device Extention/Removal - operation execution preparation failure. */
  private static final String ERROR_CODE_800302 = "800302";
  /** Other exceptions. */
  private static final String ERROR_CODE_080499 = "080499";

  /** Device Information Acquisition - input data check result is NG (json error). */
  private static final String ERROR_CODE_100101 = "100101";
  /** Device Information Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_100301 = "100301";
  /** Other exceptions. */
  private static final String ERROR_CODE_100399 = "100399";

  /** Device List Information Acquisition - operation execution preparation failure. */
  private static final String ERROR_CODE_060101 = "060101";

  /**  Recover Node Service In case input data check result is NG(json error). */
  private static final String ERROR_CODE_460101 = "460101";
  /**  Recover Node Service operation execution preparation failure. */
  private static final String ERROR_CODE_460301 = "460301";
  /** Other exceptions. */
  private static final String ERROR_CODE_460499 = "460499";

  /** Device informtation update In case input data check result is NG(json error). */
  private static final String ERROR_CODE_590101 = "590101";
  /** Device Information Acquisition - operation  execution preparation failure. */
  private static final String ERROR_CODE_590301 = "590301";
  /** Other exceptions. */
  private static final String ERROR_CODE_590499 = "590499";

  /**
   * Device Extention/Removal.
   *
   * @return REST response
   */
  @POST
  @Path("operations")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response addDeleteNode() {
    logger.trace(CommonDefinitions.START);

    operationType = OperationType.None;
    operationGroupType = ReceiverDefinitions.NODE_OPERATION;

    setErrorCode(ERROR_CODE_080101, ERROR_CODE_800302, ERROR_CODE_080499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, AddDeleteNode.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * When process request type or control type exists, overwrite the function on the occurrence side with the process to return POJO which corresponds to the process request type/control type.
   * If this is not necessary, return the input POJO as is with the base function and also configure the operation type.
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
      if (operationGroupType == ReceiverDefinitions.NODE_OPERATION) {
        AddDeleteNode addDeleteNode = (AddDeleteNode) abstractRestMessage;
        if (addDeleteNode.getAction().equals(ReceiverDefinitions.ADD_NODE)) {
          detailRequest = addDeleteNode.getAddNodeOption();
          operationType = OperationType.NodeInfoRegistration;
        } else if (addDeleteNode.getAction().equals(ReceiverDefinitions.DEL_NODE)) {
          if (addDeleteNode.getDeleteNodeOption().getDeleteNodes().getNodeType().equals("Leaf")) {
            detailRequest = addDeleteNode.getDeleteNodeOption();
            operationType = OperationType.LeafRemove;
          } else if (addDeleteNode.getDeleteNodeOption().getDeleteNodes().getNodeType().equals("B-Leaf")) {
            detailRequest = addDeleteNode.getDeleteNodeOption();
            operationType = OperationType.BLeafRemove;
          } else if (addDeleteNode.getDeleteNodeOption().getDeleteNodes().getNodeType().equals("Spine")) {
            detailRequest = addDeleteNode.getDeleteNodeOption();
            operationType = OperationType.SpineRemove;
          }
        } else if (addDeleteNode.getAction().equals(ReceiverDefinitions.UPDATE_NODE)) {
          detailRequest = addDeleteNode.getChangeNodeOption();
          operationType = OperationType.LeafChange;
        } else {
          logger.debug("Unkown action " + addDeleteNode.getAction());
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
   * Device Information Acquisition.
   *
   * @param nodeId
   *          device ID
   * @return REST response
   */
  @GET
  @Path("{node_id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getNode(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.NodeInfoAcquisition;

    setErrorCode(ERROR_CODE_100101, ERROR_CODE_100301, ERROR_CODE_100399);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Device List Information Acquisition.
   *
   * @return REST response
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response getNodeList() {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.AllDeviceInfoAcquisition;

    setErrorCode("", ERROR_CODE_060101, "");

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, null);

    logger.trace(CommonDefinitions.END);

    return response;
  }

  /**
   * Recover Node Service.
   *
   * @param nodeId
   *          Node ID
   * @return REST response
   */
  @POST
  @Path("{node_id}/recover_node")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response recoverNode(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.AcceptNodeRecover;

    setErrorCode(ERROR_CODE_460101, ERROR_CODE_460301, ERROR_CODE_460499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, RecoverNodeService.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Update NOde information.
   *
   * @param nodeId
   *           Node ID
   * @return REST response
   */
  @PUT
  @Path("{node_id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response nodeUpdate(@PathParam("node_id") String nodeId) {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.NodeUpdate;

    setErrorCode(ERROR_CODE_590101, ERROR_CODE_590301, ERROR_CODE_590499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();
    uriKeyMap.put(KEY_NODE_ID, nodeId);

    Response response = executeOperation(uriKeyMap, NodeInfoUpdate.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

}
