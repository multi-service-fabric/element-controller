package msf.ecmm.emctrl;

import java.util.Date;

import msf.ecmm.emctrl.pojo.AbstractMessage;

public class RequestQueueEntry {
	private Date requestTime;

	public RequestQueueEntry(AbstractMessage request) {
		super();
		this.request = request;
		this.requestTime = new Date();
	}

	public AbstractMessage getRequest() {
		return request;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	@Override
	public String toString() {
		return "RequestQueryEntry [request=" + request + ", requestTime=" + requestTime + "]";
	}

}
