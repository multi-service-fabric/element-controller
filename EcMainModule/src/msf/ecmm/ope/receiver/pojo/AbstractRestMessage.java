/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import msf.ecmm.common.CommonDefinitions;

/**
 * REST Request Abstract Class
 */
public abstract class AbstractRestMessage {

  /**
   * Logger
   */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /**
   * json Parser
   */
  protected static Gson jsonParser = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

  /**
   * Generating POJO instance in which json data is mapped.
   *
   * @param jsonMessage
   *          json data
   * @param requestType
   *          POJO type
   * @throws JsonSyntaxException
   *           in case error has occurred in converting POJO -> json
   * @return json POJO instance in which data is mapped
   */
  public static AbstractRestMessage createInstance(String jsonMessage, Class<? extends AbstractRestMessage> requestType)
      throws JsonSyntaxException {
    logger.trace(CommonDefinitions.START);
    AbstractRestMessage instance = jsonParser.fromJson(jsonMessage, requestType);
    logger.debug("requestType=[" + requestType + "] convertJsonImage=[" + instance.toString() + "]");
    logger.trace(CommonDefinitions.END);
    return instance;
  }
}
