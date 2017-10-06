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

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.fcctrl.pojo.AbstractRequest;
import msf.ecmm.fcctrl.pojo.AbstractResponse;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class RestClient {

	protected static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	public static final int NOTIFY_LEAF_STARTUP = 1;
	private static final String PROTOCOL = "http";

	private static final int PUT = 1;

	private static final String NEED_RETRY = "000001";

	protected static Gson jsonParser = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
			.create();

	public AbstractResponse request(int requestType, HashMap<String, String> keyMap,
			AbstractRequest requestData, Class<? extends AbstractResponse> responseType) throws RestClientException {

		logger.trace(CommonDefinitions.START);
		logger.debug("requestType:" + requestType);

		EcConfiguration config = EcConfiguration.getInstance();

		ClientConfig clientConfig = new ClientConfig();
		clientConfig.property(ClientProperties.READ_TIMEOUT, timeout*1000);

		checkKeyMap(requestType, keyMap);

		String jsonImage = "";
		if (requestData != null) {
		}
		logger.debug("sendData:" + jsonImage);

		int lastErrorReason = RestClientException.NOT_SET;
		Response response;
		String responseBody = "";

		for (int retryCount = 0; retryCount <= retryNum; retryCount++) {
			lastErrorReason = RestClientException.NOT_SET;
			try {
				if (method == POST) {
					response  = ClientBuilder.newClient(clientConfig)
							.target(url).path(uri)
							.request(MediaType.APPLICATION_JSON)
							.post(Entity.entity(jsonImage, MediaType.APPLICATION_JSON));
				} else {
					response = ClientBuilder.newClient(clientConfig)
							.target(url).path(uri)
							.request(MediaType.APPLICATION_JSON)
							.put(Entity.entity(jsonImage, MediaType.APPLICATION_JSON));
				}

				if (isSuccess(response)) {
					logger.debug("Recv OK");
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

			} catch (ProcessingException e) {
				logger.error(LogFormatter.out.format(LogFormatter.MSG_513031, "-"), e);
				if (e.getCause() instanceof ConnectException) {
					lastErrorReason = RestClientException.CONNECT_NG;
					logger.debug("retry");
					continue;
				}
				if (e.getCause() instanceof SocketTimeoutException) {
					lastErrorReason = RestClientException.TIMEOUT;
					break;
				}
				lastErrorReason = RestClientException.COMMON_NG;
				break;
			} catch (Exception e) {
				logger.error(LogFormatter.out.format(LogFormatter.MSG_513031, "-"), e);
				lastErrorReason = RestClientException.COMMON_NG;
				break;
			}
		}

		AbstractResponse returnResponse;
		if (lastErrorReason == RestClientException.NOT_SET) {
			try {
				returnResponse = jsonParser.fromJson(responseBody, responseType);
			} catch (JsonSyntaxException e) {
				logger.debug("Notify to FC NG. format error.");
				throw new RestClientException(RestClientException.JSON_FORMAT_NG);
			}
			logger.debug("Notify to FC OK. data:" + responseBody);
		} else {
			logger.debug("Notify to FC NG.");
			throw new RestClientException(lastErrorReason);
		}

		logger.trace(CommonDefinitions.END);

		return returnResponse;
	}

	private void checkKeyMap(int requestType, HashMap<String, String> keyMap) throws RestClientException {

		logger.trace(CommonDefinitions.START);

		switch (requestType) {
		case OPERATION:
			break;
		case NOTIFY_LEAF_STARTUP:
		case NOTIFY_SPINE_STARTUP:
			if (!keyMap.containsKey(KEY_CLUSTER_ID) || !keyMap.containsKey(KEY_NODE_ID)) {
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

	private String createUrl(String fcIpaddr, String fcPort) {
		return url;
	}

	private String createUri(int requestType, HashMap<String, String> keyMap) throws RestClientException {

		logger.trace(CommonDefinitions.START);

		String uri = "";
		switch (requestType) {
		case OPERATION:
			uri = "/v1/internal/FabricController/operations";
			break;
		case NOTIFY_LEAF_STARTUP:
			uri = "/v1/internal/clusters/" + keyMap.get(KEY_CLUSTER_ID) + "/nodes/leafs/" + keyMap.get(KEY_NODE_ID);
			break;
		case NOTIFY_SPINE_STARTUP:
			uri = "/v1/internal/clusters/" + keyMap.get(KEY_CLUSTER_ID) + "/nodes/spines/" + keyMap.get(KEY_NODE_ID);
			break;
		default:
			logger.debug("Unknown requestType type:" + requestType);
			throw new RestClientException(RestClientException.COMMON_NG);
		}

		logger.trace(CommonDefinitions.END);

		return uri;
	}

	private int getMethod(int requestType) throws RestClientException {

		logger.trace(CommonDefinitions.START);

		int method = 0;

		switch (requestType) {
		case OPERATION:
			method = POST;
			break;
		case NOTIFY_LEAF_STARTUP:
		case NOTIFY_SPINE_STARTUP:
			method = PUT;
			break;
		default:
			logger.debug("Unknown requestType type:" + requestType);
			throw new RestClientException(RestClientException.COMMON_NG);
		}

		logger.trace(CommonDefinitions.END);

		return method;
	}

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

	private String getStatus(Response response) {
		logger.trace(CommonDefinitions.START);
		String responseInfo = "";
		if (response != null) {
			responseInfo = response.getStatus() + " " + response.getStatusInfo();
		}
		logger.trace(CommonDefinitions.END);
		return responseInfo;
	}

	private boolean needRetry(int requestType, String responseBody) {
		logger.trace(CommonDefinitions.START);
		boolean retry = false;
		if (requestType == NOTIFY_LEAF_STARTUP || requestType == NOTIFY_SPINE_STARTUP) {
			try {
				CommonResponseFromFc resp = jsonParser.fromJson(responseBody, CommonResponseFromFc.class);
				if (resp.getErrorCode().equals(NEED_RETRY)) {
					retry = true;
				}
			} catch (Exception e) {
			}
		}
		logger.trace(CommonDefinitions.END);
		return retry;
	}
}
