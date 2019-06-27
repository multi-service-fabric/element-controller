/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl;

import java.util.Date;

import msf.ecmm.emctrl.pojo.AbstractMessage;

/**
 * Queue entry which stores requests to EM
 */
public class RequestQueueEntry {
  /** Request Message to EM */
  private AbstractMessage request;

  /** Sending Request Time */
  private Date requestTime;

  /**
   * Constructor
   *
   * @param request
   *          request to EM
   */
  public RequestQueueEntry(AbstractMessage request) {
    super();
    this.request = request;
    this.requestTime = new Date();
  }

  /**
   * Getting request message to EM
   *
   * @return request message to EM
   */
  public AbstractMessage getRequest() {
    return request;
  }

  /**
   * Getting sending request time.
   *
   * @return sending request time
   */
  public Date getRequestTime() {
    return requestTime;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "RequestQueryEntry [request=" + request + ", requestTime=" + requestTime + "]";
  }

}
