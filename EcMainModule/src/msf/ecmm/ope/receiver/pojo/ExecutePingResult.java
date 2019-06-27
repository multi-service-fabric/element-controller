/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.ope.receiver.pojo.parts.PingTargetsResponse;

/**
 * The ping(in the response) between nodes is executed.
 */
public class ExecutePingResult extends AbstractResponseMessage {

  /** The ping information. */
  private List<PingTargetsResponse> pingTargets = new ArrayList<>();

  /**
   * The ping informaton is acquired.
   *
   * @return The ping informtion
   */
  public List<PingTargetsResponse> getPingTargets() {
    return pingTargets;
  }

  /**
   * The ping informaton is set.
   *
   * @param pingTargets
   *          The ping informtion
   */
  public void setPingTargets(List<PingTargetsResponse> pingTargets) {
    this.pingTargets = pingTargets;
  }

  @Override
  public String toString() {
    return "ExecutePingResult [pingTargets=" + pingTargets + "]";
  }

}
