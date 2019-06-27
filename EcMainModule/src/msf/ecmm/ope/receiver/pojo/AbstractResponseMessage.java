/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.log.MsfLogger;

/**
 * REST Response Abstract Class
 */
public abstract class AbstractResponseMessage {

  /** Logger */
  private static final MsfLogger logger = new MsfLogger();

  /**
   * json Parser
   */
  private transient Gson jsonParser = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

  /**
   * Response Code
   */
  private transient int responseCode = CommonDefinitions.NOT_SET;

  /**
   * Converting own instance to json format
   *
   * @return json string
   */
  public String toJsonImage() {
    logger.trace(CommonDefinitions.START);
    String jsonImage = jsonParser.toJson(this);
    logger.debug("convertJsonImage=[" + jsonImage + "]");
    logger.trace(CommonDefinitions.END);
    return jsonImage;
  }

  /**
   * Getting response code.
   *
   * @return response code
   */
  public int getResponseCode() {
    return responseCode;
  }

  /**
   * Setting response code.
   *
   * @param responseCode
   *          response code
   */
  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }

}
