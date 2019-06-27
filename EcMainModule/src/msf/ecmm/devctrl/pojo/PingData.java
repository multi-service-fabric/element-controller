/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.devctrl.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * The Result of the ping between the nodes.
 */
public class PingData {

  /** The IP address of the node executing ping. */
  private String nodeIp;

  /**
   * Result of the executed ping (key：sourceIP_destinationIP value:result)<br>
   * "success"：ping success<br>
   * "failed" ：ping failed<br>
   * "unexecuted"：The possible reasons for the unexecution are SSH-login to the node failed or the ping to the unexpected node was requested;
   */
  private Map<String, String> result = new HashMap<>();

  /**
   * The IP address of the node executing the ping is acquired.
   *
   * @return The IP address of the node executing the ping
   */
  public String getNodeIp() {
    return nodeIp;
  }

  /**
   * The IP address of the node executing the ping is set.
   *
   * @param nodeIp
   *          The IP address of the node executing the ping
   */
  public void setNodeIp(String nodeIp) {
    this.nodeIp = nodeIp;
  }

  /**
   * The ping-executed result is acquired.
   *
   * @return The ping-executed result
   */
  public Map<String, String> getResult() {
    return result;
  }

  /**
   * The ping-executed result is set
   *
   * @param result
   *          The result of the ping execution
   */
  public void setResult(Map<String, String> result) {
    this.result = result;
  }

  @Override
  public String toString() {
    return "PingData [nodeIp=" + nodeIp + ", result=" + result + "]";
  }

}
