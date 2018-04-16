/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
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
import msf.ecmm.ope.receiver.pojo.Operations;
import msf.ecmm.ope.receiver.resources.BaseResource;

/**
 * CP Information Resource.
 */
@Path("/v1/internal/ElementController/operations")
public class Cps extends BaseResource {

  /** Process Execution Request - input data check result is NG (json error). */
  private static final String ERROR_CODE_010101 = "010101";
  /** Process Execution Request - operation execution preparation failure. */
  private static final String ERROR_CODE_010302 = "010302";
  /** Process Execution Request - other exceptions. */
  private static final String ERROR_CODE_010499 = "010499";

  /**
   * Process Execution Request.
   *
   * @return REST response
   */
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response operations() {

    logger.trace(CommonDefinitions.START);

    operationType = OperationType.None;
    operationGroupType = ReceiverDefinitions.ALL_CP_OPERATION;

    setErrorCode(ERROR_CODE_010101, ERROR_CODE_010302, ERROR_CODE_010499);

    HashMap<String, String> uriKeyMap = new HashMap<String, String>();

    Response response = executeOperation(uriKeyMap, Operations.class);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * When process request type or control type exists, overwrite the function on the occurrence side with the process to return POJO which corresponds to the process request type/control type.<br>
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
      if (operationGroupType == ReceiverDefinitions.ALL_CP_OPERATION) {
        Operations operations = (Operations) abstractRestMessage;
        if (operations.getAction().equals(ReceiverDefinitions.CREATE_UPDATE_L2VLANIF)) {
          detailRequest = operations.getCreateUpdateL2VlanIfOption();
          operationType = OperationType.AllL2VlanIfCreate;
        } else if (operations.getAction().equals(ReceiverDefinitions.DELETE_UPDATE_L2VLANIF)) {
          detailRequest = operations.getDeleteUpdateL2VlanIfOption();
          operationType = OperationType.AllL2VlanIfRemove;
        } else if (operations.getAction().equals(ReceiverDefinitions.CREATE_L3VLANIF)) {
          detailRequest = operations.getCreateL3VlanIfOption();
          operationType = OperationType.AllL3VlanIfCreate;
        } else if (operations.getAction().equals(ReceiverDefinitions.DELETE_L3VLANIF)) {
          detailRequest = operations.getDeleteL3VlanIfOption();
          operationType = OperationType.AllL3VlanIfRemove;
        } else if (operations.getAction().equals(ReceiverDefinitions.UPDATE_L2VLANIF)) {
          detailRequest = operations.getUpdateL2VlanIfOption();
          operationType = OperationType.AllL2VlanIfChange;
        } else if (operations.getAction().equals(ReceiverDefinitions.UPDATE_L3VLANIF)) {
          detailRequest = operations.getUpdateL3VlanIfOption();
          operationType = OperationType.AllL3VlanIfChange;
        } else if (operations.getAction().equals(ReceiverDefinitions.REGISTER_BREAKOUTIF)) {
          detailRequest = operations.getRegisterBreakoutIfOption();
          operationType = OperationType.BreakoutIfCreate;
        } else if (operations.getAction().equals(ReceiverDefinitions.DELETE_BREAKOUTIF)) {
          detailRequest = operations.getDeleteBreakoutIfOption();
          operationType = OperationType.BreakoutIfDelete;
        } else {
          logger.debug("Unkown action " + operations.getAction());
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
