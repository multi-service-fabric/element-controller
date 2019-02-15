/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.resources;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.ope.control.RestRequestCount;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationFactory;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

/**
 * REST Resource Base Class
 */
public abstract class BaseResource {

  /**
   * Logger
   */
  protected static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /**
   * REST Request
   */
  @Inject
  private HttpServletRequest req;

  /**
   * json Parser
   */
  protected static Gson jsonParser = new GsonBuilder().disableHtmlEscaping()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

  /**
   * Request Pattern
   */
  protected int requestPattern = CommonDefinitions.NOT_SET;

  /**
   * Operation Type
   */
  protected int operationType = OperationType.None;

  /**
   * Operation Group Type
   */
  protected int operationGroupType = CommonDefinitions.NOT_SET;

  /** Error code of Json -> pojo conversion error */
  protected String illegalJsonErrorCode;
  /** Error code of operation execution preparation error */
  protected String prepareErrorCode;
  /** Error code of unexpected error */
  protected String unexpectedErrorCode;

  /**
   * Extension function operation name.
   */
  protected String expandOperationName = "";


  /**
   * Operation Execution
   *
   * @param uriKeyMap
   *          URI key map
   * @param reqestBodyType
   *          REST request body POJO type (specify null for the body having no body)
   * @return operation execution result (REST response)
   */
  protected Response executeOperation(HashMap<String, String> uriKeyMap,
      Class<? extends AbstractRestMessage> reqestBodyType) {

    logger.trace(CommonDefinitions.START);

    Response response = null;
    boolean requestSuccessFlag = false;

    RestRequestCount.requestcount(RestRequestCount.REST_RECEPTION);

    try {

      if (!expandOperationName.isEmpty()) {
        operationType = ExpandOperation.getInstance().get(expandOperationName).getOperationTypeId();
      }

      logger.debug("operationType:" + OperationType.name(operationType));

      String requestJson = "";
      AbstractRestMessage requestPojo = null;

      if (reqestBodyType != null) {

        requestJson = getRequestValue();

        logger.info(LogFormatter.out.format(LogFormatter.MSG_301035, getUri(), requestJson));

        requestPojo = AbstractRestMessage.createInstance(requestJson, reqestBodyType);

        requestPojo = getDetailRequest(requestPojo);
      } else {
        logger.info(LogFormatter.out.format(LogFormatter.MSG_301035, getUri(), ""));
      }

      response = execute(uriKeyMap, requestPojo);

      if (isSuccess(response)) {
        requestSuccessFlag = true;
      }

    } catch (JsonSyntaxException e) {
      response = createIllegalJsonErrorResponse();
      logger.debug("Json decode error", e);
    } catch (Throwable e) { 
      response = createUnexpectedErrorResponse();
      logger.debug("Execute operation error", e);
    } finally {
      if (requestSuccessFlag == true) {
        logger.info(LogFormatter.out.format(LogFormatter.MSG_301036, getUri(), getStatus(response), getBody(response)));
      } else {
        logger
            .error(LogFormatter.out.format(LogFormatter.MSG_501037, getUri(), getStatus(response), getBody(response)));
      }
    }
    logger.trace(CommonDefinitions.END);

    return response;
  }

  /**
   * Operation Execution
   *
   * @param uriKeyMap
   *          URI key map
   * @param requestPojo
   *          REST request body POJO type (specify null for the request having no body)
   * @return operation execution result (REST response)
   */
  private Response execute(HashMap<String, String> uriKeyMap, AbstractRestMessage requestPojo) {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage executeResponse = null;
    boolean prepareResult = true;

    Operation operation = OperationFactory.create(operationType, uriKeyMap, requestPojo);

    try {
      prepareResult = operation.prepare();
      if (prepareResult == false) {
        logger.debug("Operation.prepare error");
        return createPrepareErrorResponse();
      }

      executeResponse = operation.execute();

    } finally {
      try {
        operation.close();
      } catch (Throwable e) {
        ; 
      }
    }

    Response response = createResponse(executeResponse);

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Configuration of Error Codes which are used in error occurrence
   *
   * @param illegalJsonErrorCode
   *          error code of Json -> pojo conversion error
   * @param prepareErrorCode
   *          error code of operation execution preparation error
   * @param unexpectedError
   *          error code of unexpected error
   */
  protected void setErrorCode(String illegalJsonErrorCode, String prepareErrorCode, String unexpectedError) {
    this.illegalJsonErrorCode = illegalJsonErrorCode;
    this.prepareErrorCode = prepareErrorCode;
    this.unexpectedErrorCode = unexpectedError;
  }

  /**
   * REST Request Body Acquisition
   *
   * @return REST request body string
   * @exception IOException
   *              body acquisition failed
   */
  protected String getRequestValue() throws IOException {
    logger.trace(CommonDefinitions.START);
    String requestValue = "";
    requestValue = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);
    logger.trace(CommonDefinitions.END);
    return requestValue;
  }

  /**
   * REST Response Generation
   *
   * @param getData
   * @return REST response
   */
  protected Response createResponse(AbstractResponseMessage getData) {
    logger.trace(CommonDefinitions.START);

    int responseCode = getData.getResponseCode();

    switch (responseCode) {
      case RESP_OK_200:
      case RESP_CREATED_201:
      case RESP_ACCEPTED_202:
      case RESP_NOCONTENTS_204:
      case RESP_BADREQUEST_400:
      case RESP_NOTFOUND_404:
      case RESP_CONFLICT_409:
      case RESP_INTERNALSERVERERROR_500:
        break;
      default:
        responseCode = RESP_INTERNALSERVERERROR_500;
        break;
    }

    String jsonImage = jsonParser.toJson(getData);
    boolean isBody = false;
    ResponseBuilder responseBuilder = null;
    if (getData instanceof CommonResponse) {
      if (!((CommonResponse) getData).getErrorCode().isEmpty()) {
        isBody = true; 
      }
    } else {
      isBody = true; 
    }
    if (isBody) {
      responseBuilder = Response.status(responseCode).entity(jsonImage);
    } else {
      responseBuilder = Response.status(responseCode);
    }

    Response response = responseBuilder.build();

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * When process request type or control type exists, overwrite the function on the occurrence side with the process to return POJO which corresponds to the process request type/control type.
   * If this is not necessary, return the input POJO as is with the base function and also configure the operation type.
   *
   * @param abstractRestMessage
   *          REST iput POJO
   * @return POJO corresponding to the process request type or control type
   */
  protected AbstractRestMessage getDetailRequest(AbstractRestMessage abstractRestMessage) {
    return abstractRestMessage;
  }

  /**
   * Generating the Error Code of Json -> pojo Conversion Error
   *
   * @return error code
   */
  protected Response createIllegalJsonErrorResponse() {
    logger.trace(CommonDefinitions.START);
    CommonResponse commonResponse = new CommonResponse();
    commonResponse.setResponseCode(RESP_BADREQUEST_400);
    commonResponse.setErrorCode(illegalJsonErrorCode);
    logger.trace(CommonDefinitions.END);
    return createResponse(commonResponse);
  }

  /**
   * Generating the Error Code of Operation Executio Preparation Error
   *
   * @return error code
   */
  private Response createPrepareErrorResponse() {
    logger.trace(CommonDefinitions.START);
    CommonResponse commonResponse = new CommonResponse();
    commonResponse.setResponseCode(RESP_CONFLICT_409);
    commonResponse.setErrorCode(prepareErrorCode);
    logger.trace(CommonDefinitions.END);
    return createResponse(commonResponse);
  }

  /**
   * Generating the Error Code of Unexpected Error
   *
   * @return error code
   */
  protected Response createUnexpectedErrorResponse() {
    logger.trace(CommonDefinitions.START);
    CommonResponse commonResponse = new CommonResponse();
    commonResponse.setResponseCode(RESP_INTERNALSERVERERROR_500);
    commonResponse.setErrorCode(unexpectedErrorCode);
    logger.trace(CommonDefinitions.END);
    return createResponse(commonResponse);
  }

  /**
   * URI Acquisition
   *
   * @return URI
   */
  private String getUri() {
    return req.getRequestURI();
  }

  /**
   * Response Information Acquisition
   *
   * @param response
   *          resonse
   * @return response information
   */
  private String getStatus(Response response) {
    logger.trace(CommonDefinitions.START);
    String responseInfo = "";
    if (response != null) {
      responseInfo = response.getStatus() + " " + response.getStatusInfo();
    }
    logger.trace(CommonDefinitions.END);
    return responseInfo;
  }

  /**
   * Request Execution Success/Fail Acquisition
   *
   * @param response
   *          request execution result response
   * @return true: succeeded, false: failed
   */
  protected boolean isSuccess(Response response) {
    logger.trace(CommonDefinitions.START);
    boolean ret = false;
    int respCode = response.getStatus();
    switch (respCode) {
      case RESP_OK_200:
      case RESP_CREATED_201:
      case RESP_ACCEPTED_202:
      case RESP_NOCONTENTS_204:
        ret = true;
        break;
      default:
        ret = false;
        break;
    }
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * Getting Body from Response
   *
   * @param response
   *          response
   * @return body
   */
  private String getBody(Response response) {
    logger.trace(CommonDefinitions.START);
    Object body = response.getEntity();
    String ret = "";
    if (ret != null) {
      ret = (String) body;
    }
    logger.trace(CommonDefinitions.END);
    return ret;
  }
}
