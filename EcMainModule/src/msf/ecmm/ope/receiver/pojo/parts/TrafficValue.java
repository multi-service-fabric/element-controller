/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Traffic Information.
 */
public class TrafficValue {

  /** IF Type. */
  private String ifType = null;
  /** IF ID. */
  private String ifId = null;
  /** Receiving Rate. */
  private Double receiveRate = null;
  /** Sending Rate. */
  private Double sendRate = null;

  /**
   * Getting IF type.
   *
   * @return ifType
   */
  public String getIfType() {
    return ifType;
  }

  /**
   * Setting IF type.
   *
   * @param ifType
   *          set ifType
   */
  public void setIfType(String ifType) {
    this.ifType = ifType;
  }

  /**
   * Getting IF ID.
   *
   * @return ifId
   */
  public String getIfId() {
    return ifId;
  }

  /**
   * Setting IF ID.
   *
   * @param ifId
   *          set ifId
   */
  public void setIfId(String ifId) {
    this.ifId = ifId;
  }

  /**
   * Getting receiving rate.
   *
   * @return receiveRate
   */
  public Double getReceiveRate() {
    return receiveRate;
  }

  /**
   * Setting receiving rate.
   *
   * @param receiveRate
   *          set receiveRate
   */
  public void setReceiveRate(Double receiveRate) {
    this.receiveRate = receiveRate;
  }

  /**
   * Getting sending rate.
   *
   * @return sendRate
   */
  public Double getSendRate() {
    return sendRate;
  }

  /**
   * Setting sending rate.
   *
   * @param sendRate
   *          set sendRate
   */
  public void setSendRate(Double sendRate) {
    this.sendRate = sendRate;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "TrafficValue [ifType=" + ifType + ", ifId=" + ifId + ", receiveRate=" + receiveRate + ", sendRate="
        + sendRate + "]";
  }

}
