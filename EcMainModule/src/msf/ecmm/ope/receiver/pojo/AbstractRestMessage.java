package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.common.CommonDefinitions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public abstract class AbstractRestMessage {

    private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

    protected static Gson jsonParser = new GsonBuilder()
             .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
             .create();

    public static AbstractRestMessage createInstance(String jsonMessage, Class<? extends AbstractRestMessage> requestType)
            throws JsonSyntaxException {
        logger.trace(CommonDefinitions.START);
        AbstractRestMessage instance = jsonParser.fromJson(jsonMessage, requestType);
        logger.debug("requestType=[" + requestType + "] convertJsonImage=[" + instance.toString() + "]");
        logger.trace(CommonDefinitions.END);
        return instance;
    }
}
