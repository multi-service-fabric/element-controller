
package msf.ecmm.ope.receiver.pojo;

public class CommonResponse extends AbstractResponseMessage {

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "CommonResponse [errorCode=" + errorCode + "]";
	}

}
