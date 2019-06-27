/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * The ping information(in the response). 
 */
public class PingTargetsResponse {

  /** The source information. */
  private BaseIfInfo src;

  /** The destination  information. */
  private BaseIfInfo dst;

  /**
   * Result
   * ("success"：ping success,"failed"：ping failed
   * "unexecuted"：The posible reasons for the unexection are SSH-login to the node failed or the ping to the unexpected node was requested;)
   */
  private String result;

  /**
   * The source information is acquired.
   *
   * @return The source information
   */
  public BaseIfInfo getSrc() {
    return src;
  }

  /**
   * The source information is set.
   *
   * @param src
   *          The source information
   */
  public void setSrc(BaseIfInfo src) {
    this.src = src;
  }

  /**
   * The destination information is acquired.
   *
   * @return The destination information
   */
  public BaseIfInfo getDst() {
    return dst;
  }

  /**
   * The destination information is set.
   *
   * @param dst
   *          The destination information
   */
  public void setDst(BaseIfInfo dst) {
    this.dst = dst;
  }

  /**
   * The result is acquired.
   *
   * @return The result
   */
  public String getResult() {
    return result;
  }

  /**
   * The result is set.
   *
   * @param result
   *          The result
   */
  public void setResult(String result) {
    this.result = result;
  }

  @Override
  public String toString() {
    return "ResponsePingTargets [src=" + src + ", dst=" + dst + ", result=" + result + "]";
  }
}
