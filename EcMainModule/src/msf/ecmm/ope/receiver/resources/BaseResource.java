
package msf.ecmm.ope.receiver.resources;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationFactory;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CommonResponse;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public abstract class BaseResource {

	protected static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

	@Inject
	private HttpServletRequest req;

	protected static Gson jsonParser = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();

	protected int requestPattern = CommonDefinitions.NOT_SET;

	protected OperationType operationType = OperationType.None;

	protected int operationGroupType = CommonDefinitions.NOT_SET;

	protected String prepareErrorCode;
	protected Response executeOperation(HashMap<String, String> uriKeyMap,
			Class<? extends AbstractRestMessage> reqestBodyType) {

		logger.trace(CommonDefinitions.START);
		logger.debug("operationType:" + operationType);

		Response response = null;
		boolean requestSuccessFlag = false;

		try {
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
			response = createUnexpectedErrorResponse();
			logger.debug("Execute operation error", e);
		} finally {
			if (requestSuccessFlag == true) {
				logger.info(LogFormatter.out.format(LogFormatter.MSG_301036, getUri(), getStatus(response), getBody(response)));
			} else {
				logger.error(LogFormatter.out.format(LogFormatter.MSG_501037, getUri(), getStatus(response), getBody(response)));
			}
		}
		logger.trace(CommonDefinitions.END);

		return response;
	}

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
			}
		}

		Response response = createResponse(executeResponse);

		logger.trace(CommonDefinitions.END);
		return response;
	}

	protected void setErrorCode(String illegalJsonErrorCode, String prepareErrorCode, String unexpectedError) {
		this.illegalJsonErrorCode = illegalJsonErrorCode;
		this.prepareErrorCode = prepareErrorCode;
		this.unexpectedErrorCode = unexpectedError;
	}

	protected String getRequestValue() throws IOException {
		logger.trace(CommonDefinitions.START);
		String requestValue = "";
		requestValue = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);
		logger.trace(CommonDefinitions.END);
		return requestValue;
	}

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
			}
		} else {
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

	protected AbstractRestMessage getDetailRequest(AbstractRestMessage abstractRestMessage) {
		return abstractRestMessage;
	}

	protected Response createIllegalJsonErrorResponse() {
		logger.trace(CommonDefinitions.START);
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setResponseCode(RESP_BADREQUEST_400);
		commonResponse.setErrorCode(illegalJsonErrorCode);
		logger.trace(CommonDefinitions.END);
		return createResponse(commonResponse);
	}

	private Response createPrepareErrorResponse() {
		logger.trace(CommonDefinitions.START);
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setResponseCode(RESP_CONFLICT_409);
		commonResponse.setErrorCode(prepareErrorCode);
		logger.trace(CommonDefinitions.END);
		return createResponse(commonResponse);
	}

	protected Response createUnexpectedErrorResponse() {
		logger.trace(CommonDefinitions.START);
		CommonResponse commonResponse = new CommonResponse();
		commonResponse.setResponseCode(RESP_INTERNALSERVERERROR_500);
		commonResponse.setErrorCode(unexpectedErrorCode);
		logger.trace(CommonDefinitions.END);
		return createResponse(commonResponse);
	}

	private String getUri() {
		return req.getRequestURI();
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

	private String getBody(Response response) {
		logger.trace(CommonDefinitions.START);
		Object body = response.getEntity();
		String ret = "";
		if (ret != null) {
			ret = (String)body;
		}
		logger.trace(CommonDefinitions.END);
		return ret;
	}
}
