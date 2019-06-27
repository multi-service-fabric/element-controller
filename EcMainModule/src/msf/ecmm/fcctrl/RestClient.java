/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.fcctrl;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.fcctrl.pojo.AbstractRequest;
import msf.ecmm.fcctrl.pojo.AbstractResponse;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.ope.control.RestRequestCount;

/**
 * REST Client
 */
public class RestClient {

  /**
   * REST Client
   */
  protected MsfLogger logger = null;

  /** Process Execution Request. */
  public static final int OPERATION = 0;
  /** Extention Completion Notification. */
  public static final int NOTIFY_NODE_ADDITION = 1;


  /** Controler Status Notification(switch-over). */
  public static final int CONTROLLER_STATE_NOTIFICATION = 2;
  /** Controler Status Notification(log). */
  public static final int CONTROLLER_STATE_NOTIFICATION_LOG = 3;
  /** Controler Status Notification(server). */
  public static final int CONTROLLER_STATE_NOTIFICATION_SERVER = 4;

  /** Protocol name. */
  private static final String PROTOCOL = "http";

  /** POST method. */
  public static final int POST = 0;
  /** PUT method. */
  public static final int PUT = 1;
  /** GET method. */
  public static final int GET = 2;

  protected static final String NEED_RETRY = "000001";

  /**
   *Cunstructor for  RestClient class.
   */
  public RestClient() {
    logger = new MsfLogger();
  }

  /**
   * Cunstructor for RestClient class.
   *
   * @param logOutput
   *          Flag indicating no log output
   */
  public RestClient(boolean logOutput) {
    logger = new MsfLogger(logOutput);
  }

  /**
   * json Parser.
   */
  protected static Gson jsonParser = new GsonBuilder().disableHtmlEscaping()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

  /**
   * Key String for Acquiring Config Port Number.
   *
   * @return key
   */
  protected String getPortKey() {
    return EcConfiguration.FC_PORT;
  }

  /**
   * Key String for Acquiring Config Address Number.
   *
   * @return key
   */
  protected String getAddressKey() {
    return EcConfiguration.FC_ADDRESS;
  }

  /**
   * POST method excution.
   *
   * @param clientConfig
   *          Rest client setting
   * @param url
   *          URL string
   * @param url
   *          URI parameter string
   * @param jsonImage
   *         body value string
   * @return Response HTTP_Response
   */
  protected Response doPost(ClientConfig clientConfig, String url, String uri, String jsonImage) {
    return ClientBuilder.newClient(clientConfig).target(url).path(uri).request(MediaType.APPLICATION_JSON)
        .post(Entity.entity(jsonImage, MediaType.APPLICATION_JSON));
  }

  /**
   * PUT method excution.
   *
   * @param clientConfig
   *          Rest client setting
   * @param url
   *          URL string
   * @param url
   *          URI parameter string
   * @param jsonImage
   *         body value string
   * @return Response HTTP_Response
   */
  protected Response doPut(ClientConfig clientConfig, String url, String uri, String jsonImage) {
    return ClientBuilder.newClient(clientConfig).target(url).path(uri).request(MediaType.APPLICATION_JSON)
        .put(Entity.entity(jsonImage, MediaType.APPLICATION_JSON));
  }

  /**
   * GET method excution.
   *
   * @param clientConfig
   *          Rest client setting
   * @param url
   *          URL string
   * @param url
   *          URI parameter string
   * @param jsonImage
   *         body value string
   * @param keyMap
   *          key map
   * @return Response HTTP_Response
   */
  protected Response doGet(ClientConfig clientConfig, String url, String uri, String jsonImage,
      HashMap<String, String> keyMap) {
    throw new ProcessingException("GET method not support.");
  }

  /**
   * REST Request.
   *
   * @param requestType
   *          Request Type
   * @param keyMap
   *          URI Key Map
   * @param requestData
   *          Request Data (request body configuration data) - set null when there is no body
   * @param responseType
   *          Response POJO Type (set CommonResponse.class when it is not any acquiring requests)
   * @return Response POJO
   * @throws RestClientException
   *           REST Request Failure Exception - failing factor can be acquired with getCode()
   */
  public AbstractResponse request(int requestType, HashMap<String, String> keyMap, AbstractRequest requestData,
      Class<? extends AbstractResponse> responseType) throws RestClientException {

    logger.trace(CommonDefinitions.START);
    logger.debug("requestType:" + requestType);

    EcConfiguration config = EcConfiguration.getInstance();
    int retryNum = config.get(Integer.class, EcConfiguration.REST_RETRY_NUM);
    int timeout = config.get(Integer.class, EcConfiguration.REST_TIMEOUT); 
    String fcIpaddr = config.get(String.class, getAddressKey()); 
    String fcPort = config.get(String.class, getPortKey()); 

    ClientConfig clientConfig = new ClientConfig();
    clientConfig.property(ClientProperties.READ_TIMEOUT, timeout * 1000);

    checkKeyMap(requestType, keyMap);

    String url = createUrl(fcIpaddr, fcPort); 
    String uri = createUri(requestType, keyMap);
    String jsonImage = "";
    if (requestData != null) {
      jsonImage = jsonParser.toJson(requestData); 
    }
    logger.debug("sendData:" + jsonImage);
    int method = getMethod(requestType);

    int lastErrorReason = RestClientException.NOT_SET;
    Response response;
    String responseBody = "";

    for (int retryCount = 0; retryCount <= retryNum; retryCount++) {
      lastErrorReason = RestClientException.NOT_SET;
      try {
        if (method == POST) {
          response = doPost(clientConfig, url, uri, jsonImage);
        } else if (method == PUT) {
          response = doPut(clientConfig, url, uri, jsonImage);
        } else {
          response = doGet(clientConfig, url, uri, jsonImage, keyMap);
        }
        responseBody = response.readEntity(String.class);

        if (isSuccess(response)) {
          logger.debug("Recv OK");
          break;
        } else {
          logger.debug("Recv NG");
          logger.error(LogFormatter.out.format(LogFormatter.MSG_513031, getStatus(response)));
          lastErrorReason = RestClientException.ERROR_RESPONSE;
          if (needRetry(requestType, responseBody)) {
            logger.debug("retry");
            continue;
          }
          logger.debug("do not retry");
          break;
        }

      } catch (ProcessingException pe) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_513031, "-"), pe);
        if (pe.getCause() instanceof ConnectException) {
          lastErrorReason = RestClientException.CONNECT_NG;
          logger.debug("retry");
          continue;
        }
        if (pe.getCause() instanceof SocketTimeoutException) {
          lastErrorReason = RestClientException.TIMEOUT;
          break;
        }
        lastErrorReason = RestClientException.COMMON_NG;
        break;
      } catch (Exception exp) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_513031, "-"), exp);
        lastErrorReason = RestClientException.COMMON_NG;
        break;
      }
    }

    AbstractResponse returnResponse;
    if (lastErrorReason == RestClientException.NOT_SET) {
      RestRequestCount.requestcount(RestRequestCount.REST_TRANSMISSION);
      try {
        returnResponse = jsonParser.fromJson(responseBody, responseType);
      } catch (JsonSyntaxException jse) {
        logger.debug("Notify to FC/EM NG. format error.", jse);
        throw new RestClientException(RestClientException.JSON_FORMAT_NG);
      }
      logger.debug("Notify to FC/EM OK. data:" + responseBody);
    } else {
      logger.debug("Notify to FC/EM NG.");
      throw new RestClientException(lastErrorReason);
    }

    logger.trace(CommonDefinitions.END);

    return returnResponse;
  }

  /**
   * Manual for REST request.
   *
   * @param address
   *          destination address
   * @param port
   *          port 
   * @param method
   *          method
   * @param uri
   *          URI
   * @param requestData
   *           request data(data for setting request body) in case of no body, null is set.
   * @param responseType
   *           returned POJO type if it is not a getting request, CommonResponse.class is set)
   * @return Response POJO
   * @throws RestClientException
   *           REST request failure exception: failure cause is available by using getCode()
   */
  public AbstractResponse request(String address, String port, int method, String uri, AbstractRequest requestData,
      Class<? extends AbstractResponse> responseType) throws RestClientException {

    logger.trace(CommonDefinitions.START);
    logger.debug("address=" + address + " port=" + port + " method=" + method + " uri=" + uri);

    int timeout = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.REST_TIMEOUT);
    ClientConfig clientConfig = new ClientConfig();
    clientConfig.property(ClientProperties.READ_TIMEOUT, timeout * 1000);

    String url = createUrl(address, port); 

    String jsonImage = "";
    if (requestData != null) {
      jsonImage = jsonParser.toJson(requestData);
    }
    logger.debug("sendData:" + jsonImage);

    int lastErrorReason = RestClientException.NOT_SET;
    Response response;
    String responseBody = "";

    int retryNum = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.REST_RETRY_NUM);
    for (int retryCount = 0; retryCount <= retryNum; retryCount++) {
      lastErrorReason = RestClientException.NOT_SET;
      try {
        if (method == POST) {
          response = doPost(clientConfig, url, uri, jsonImage);
        } else if (method == PUT) {
          response = doPut(clientConfig, url, uri, jsonImage);
        } else {
          response = doGet(clientConfig, url, uri, jsonImage, null);
        }

        responseBody = response.readEntity(String.class);

        if (isSuccess(response)) {
          logger.debug("Recv OK");
          break; 
        } else {
          logger.debug("Recv NG");
          logger.error(LogFormatter.out.format(LogFormatter.MSG_513031, getStatus(response)));
          lastErrorReason = RestClientException.ERROR_RESPONSE;
          if (needRetry(NOT_SET, responseBody)) {
            logger.debug("retry");
            continue;
          }
          logger.debug("do not retry");
          break;
        }

      } catch (ProcessingException pe) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_513031, "-"), pe);
        if (pe.getCause() instanceof ConnectException) {
          lastErrorReason = RestClientException.CONNECT_NG;
          logger.debug("retry");
          continue;
        }
        if (pe.getCause() instanceof SocketTimeoutException) {
          lastErrorReason = RestClientException.TIMEOUT;
          break;
        }
        lastErrorReason = RestClientException.COMMON_NG;
        break;
      } catch (Exception exp) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_513031, "-"), exp);
        lastErrorReason = RestClientException.COMMON_NG;
        break;
      }
    }

    AbstractResponse returnResponse;
    if (lastErrorReason == RestClientException.NOT_SET) {
      RestRequestCount.requestcount(RestRequestCount.REST_TRANSMISSION);
      try {
        returnResponse = jsonParser.fromJson(responseBody, responseType);
      } catch (JsonSyntaxException jse) {
        logger.debug("Notify to FC/EM NG. format error.", jse);
        throw new RestClientException(RestClientException.JSON_FORMAT_NG);
      }
      logger.debug("Notify to FC/EM OK. data:" + responseBody);
    } else {
      logger.debug("Notify to FC/EM NG.");
      throw new RestClientException(lastErrorReason);
    }

    logger.trace(CommonDefinitions.END);

    return returnResponse;
  }

  /**
   * URI Key Check.
   *
   * @param requestType
   *          request type
   * @param keyMap
   *          URI key map
   * @throws RestClientException
   *           request type invalid, no required key
   */
  protected void checkKeyMap(int requestType, HashMap<String, String> keyMap) throws RestClientException {

    logger.trace(CommonDefinitions.START);

    switch (requestType) {
      case OPERATION:
      case CONTROLLER_STATE_NOTIFICATION:
      case CONTROLLER_STATE_NOTIFICATION_LOG:
      case CONTROLLER_STATE_NOTIFICATION_SERVER:
        break;
      case NOTIFY_NODE_ADDITION:
        if (!keyMap.containsKey(KEY_NODE_ID)) {
          logger.debug("Not found key");
          throw new RestClientException(RestClientException.COMMON_NG);
        }
        break;
      default:
        logger.debug("Unknown requestType type:" + requestType);
        throw new RestClientException(RestClientException.COMMON_NG);
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Generating URL (excl. path part).
   *
   * @param fcIpaddr
   *          FC IP address
   * @param fcPort
   *          FC port number
   * @return URL (excl. path)
   */
  private String createUrl(String fcIpaddr, String fcPort) {
    String url = PROTOCOL + "://" + fcIpaddr + ":" + fcPort;
    return url;
  }

  /**
   * Generating URI (path).
   *
   * @param requestType
   *          request type
   * @param keyMap
   *          URI key map
   * @return URI (path)
   * @throws RestClientException
   *           request type invalid, no required key
   */
  protected String createUri(int requestType, HashMap<String, String> keyMap) throws RestClientException {

    logger.trace(CommonDefinitions.START);

    String uri = "";
    switch (requestType) {
      case OPERATION:
        uri = "/v1/internal/FabricController/operations";
        break;
      case NOTIFY_NODE_ADDITION:
        uri = "/v1/internal/nodes/" + keyMap.get(KEY_NODE_ID);
        break;
      case CONTROLLER_STATE_NOTIFICATION:
        uri = "/v1/internal/controller/ec_em/status";
        break;
      case CONTROLLER_STATE_NOTIFICATION_LOG:
        uri = "/v1/internal/controller_status_notification/ec_em/log";
        break;
      case CONTROLLER_STATE_NOTIFICATION_SERVER:
        uri = "/v1/internal/controller_status_notification/ec_em/failure";
        break;
      default:
        logger.debug("Unknown requestType type:" + requestType);
        throw new RestClientException(RestClientException.COMMON_NG);
    }

    logger.trace(CommonDefinitions.END);

    return uri;
  }

  /**
   * Getting method.
   *
   * @param requestType
   *          request type
   * @return method type
   * @throws RestClientException
   *           request type invalid
   */
  protected int getMethod(int requestType) throws RestClientException {

    logger.trace(CommonDefinitions.START);

    int method = 0;

    switch (requestType) {
      case OPERATION:
        method = POST;
        break;
      case NOTIFY_NODE_ADDITION:
      case CONTROLLER_STATE_NOTIFICATION:
      case CONTROLLER_STATE_NOTIFICATION_LOG:
      case CONTROLLER_STATE_NOTIFICATION_SERVER:
        method = PUT;
        break;
      default:
        logger.debug("Unknown requestType type:" + requestType);
        throw new RestClientException(RestClientException.COMMON_NG);
    }

    logger.trace(CommonDefinitions.END);

    return method;
  }

  /**
   * Response Determination.
   *
   * @param response
   *          response from FC
   * @return true: request execution success - false: failed
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
   * Getting response information.
   *
   * @param response
   *          response
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
   * retry Requirement Determination.
   *
   * @param requestType
   *          request type
   * @param responseBody
   *          body of FC response
   * @return true: required, false: optional
   */
  protected boolean needRetry(int requestType, String responseBody) {
    logger.trace(CommonDefinitions.START);
    boolean retry = false;

    try {
      CommonResponseFromFc resp = jsonParser.fromJson(responseBody, CommonResponseFromFc.class);
      if (resp.getErrorCode().equals(NEED_RETRY)) {
        retry = true;
      }
    } catch (Exception exp) {
      retry = false; 
    }
    logger.trace(CommonDefinitions.END);
    return retry;
  }
}
