/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;



/**
 * REST Client.
 */
public class RestClientToEm extends RestClient {


  /** Gettin controller Status. */
  public static final int CONTROLLER_STATE = 0;
  /** Getting controller log. */
  public static final int CONTROLLER_LOG = 1;



  @Override
  protected String getPortKey() {
    return EcConfiguration.EM_REST_PORT;
  }

  @Override
  protected String getAddressKey() {
    return EcConfiguration.EM_REST_ADDRESS;
  }

  @Override
  protected Response doPost(ClientConfig clientConfig, String url ,String uri ,String jsonImage){
    throw new ProcessingException("GET method not support.");
  }

  @Override
  protected Response doPut(ClientConfig clientConfig, String url ,String uri ,String jsonImage){
    throw new ProcessingException("GET method not support.");
  }

  @Override
  protected Response doGet(ClientConfig clientConfig, String url ,String uri,
      String jsonImage,HashMap<String, String> keyMap){
    WebTarget webTarget = ClientBuilder.newClient(clientConfig).target(url).path(uri);
    for (Map.Entry<String, String> queryParam : keyMap.entrySet()) {
      if (queryParam.getValue() != null) {
        webTarget = webTarget.queryParam(queryParam.getKey(), queryParam.getValue());
      }
    }
    return webTarget.request().get(); 
  }




  @Override
  protected void checkKeyMap(int requestType, HashMap<String, String> keyMap)
      throws RestClientException {
    logger.trace(CommonDefinitions.START);


    switch (requestType) {
      case CONTROLLER_STATE:
        if (!keyMap.containsKey(KEY_GET_INFO)) {
          logger.debug("Not found key");
          throw new RestClientException(RestClientException.COMMON_NG);
        }
        break;
      case CONTROLLER_LOG:
        if (!keyMap.containsKey(KEY_END_DATE) || !keyMap.containsKey(KEY_START_DATE)
            || !keyMap.containsKey(KEY_LIMIT_NUMBER)) {
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



  @Override
  protected String createUri(int requestType, HashMap<String, String> keyMap)
      throws RestClientException {
    logger.trace(CommonDefinitions.START);

    String uri = "";
    switch (requestType) {
      case CONTROLLER_STATE:
        uri = "/v1/internal/em_ctrl/statusget";
        break;
      case CONTROLLER_LOG:
        uri = "/v1/internal/em_ctrl/log";
        break;
      default:
        logger.debug("Unknown requestType type:" + requestType);
        throw new RestClientException(RestClientException.COMMON_NG);
    }

    logger.trace(CommonDefinitions.END);
    return uri ;
  }




  @Override
protected int getMethod(int requestType) throws RestClientException {
    logger.trace(CommonDefinitions.START);

    int method = 0;

    switch (requestType) {
      case CONTROLLER_STATE:
      case CONTROLLER_LOG:
        method = RestClient.GET;
        break;
      default:
        logger.debug("Unknown requestType type:" + requestType);
        throw new RestClientException(RestClientException.COMMON_NG);
    }

    logger.trace(CommonDefinitions.END);

    return method;
  }



  @Override
  protected boolean needRetry(int requestType, String responseBody) {
    logger.trace(CommonDefinitions.START);
    boolean retry = false;
    if (requestType == CONTROLLER_STATE || requestType == CONTROLLER_LOG) {
      try {
        CommonResponseFromFc resp = jsonParser.fromJson(responseBody, CommonResponseFromFc.class);
        if (resp.getErrorCode().equals(NEED_RETRY)) {
          retry = true;
        }
      } catch (Exception exp) {
        retry = false; 
      }
    }
    logger.trace(CommonDefinitions.END);
    return retry;
  }


}
