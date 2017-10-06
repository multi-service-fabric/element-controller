package msf.ecmm.fcctrl;

public class RestClientException extends Exception {

	public static final int NOT_SET = -1;
	public static final int CONNECT_NG = 1;
	public static final int ERROR_RESPONSE = 3;
	public RestClientException(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
