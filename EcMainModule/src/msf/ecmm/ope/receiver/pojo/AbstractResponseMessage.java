package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.common.CommonDefinitions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class AbstractResponseMessage {

    private transient Gson jsonParser = new GsonBuilder()
             .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
             .create();

    private transient int responseCode = CommonDefinitions.NOT_SET;

    public String toJsonImage() {
        logger.trace(CommonDefinitions.START);
        String jsonImage = jsonParser.toJson(this);
        logger.debug("convertJsonImage=[" + jsonImage + "]");
        logger.trace(CommonDefinitions.END);
        return jsonImage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

}
