/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Controller Related Information
 */
public class ControllerInfo {

  /**
   * CPU Usage Per Unit Time
   */
  private Float cpu;

  /**
   * Memory Usage at Specific Time
   */
  private Integer memory;

  /**
   * Number of Receiving REST Requests per unit time
   */
  private Integer receiveRestRequest;

  /**
   * Number of Sending REST Request per unit time
   */
  private Integer sendRestRequest;

  /**
   * Getting CPU usage per unit time.
   * @return CPU usage per unit time
   */
  public Float getCpu() {
    return cpu;
  }

  /**
   * Setting CPU usage per unit time.
   * @param cpu CPU usage per unit time
   */
  public void setCpu(Float cpu) {
    this.cpu = cpu;
  }

  /**
   * Getting memory usage at specific time.
   * @return memory usage at specific time
   */
  public Integer getMemory() {
    return memory;
  }

  /**
   * Setting memory usage at specific time.
   * @param memory memory usage at specific time
   */
  public void setMemory(Integer memory) {
    this.memory = memory;
  }

  /**
   * Getting the number of receiving REST requests per unit time.
   * @return the number of receiving REST requests per unit time
   */
  public Integer getReceiveRestRequest() {
    return receiveRestRequest;
  }

  /**
   * Setting the number of receiving REST requests per unit time.
   * @param receiveRestRequest the number of receiving REST requests per unit time
   */
  public void setReceiveRestRequest(Integer receiveRestRequest) {
    this.receiveRestRequest = receiveRestRequest;
  }

  /**
   * Getting the number of sending REST requests per unit time.
   * @return the number of sending REST requests per unit time
   */
  public Integer getSendRestRequest() {
    return sendRestRequest;
  }

  /**
   * Setting the number of sending REST requests per unit time.
   * @param sendRestRequest the number of sending REST requests per unit time
   */
  public void setSendRestRequest(Integer sendRestRequest) {
    this.sendRestRequest = sendRestRequest;
  }

  @Override
  public String toString() {
    return "ControllerInfo [cpu=" + cpu + ", memory=" + memory + ", receiveRestRequest=" + receiveRestRequest
        + ", sendRestRequest=" + sendRestRequest + "]";
  }
}
