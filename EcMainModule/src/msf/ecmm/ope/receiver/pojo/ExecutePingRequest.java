/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.PingTargetsRequest;

/**
 * The ping  between nodes is executed.
 */
public class ExecutePingRequest extends AbstractRestMessage {

  /** The ping information. */
  private List<PingTargetsRequest> pingTargets = new ArrayList<>();

  /**
   * The ping information is acquired.
   *
   * @return The ping information
   */
  public List<PingTargetsRequest> getPingTargets() {
    return pingTargets;
  }

  /**
   * The ping information is set
   *
   * @param pingTargets
   *          The ping information
   */
  public void setPingTargets(List<PingTargetsRequest> pingTargets) {
    this.pingTargets = pingTargets;
  }

  @Override
  public String toString() {
    return "ExecutePingRequest [pingTargets=" + pingTargets + "]";
  }

  /**
   * The input parameter is checked.
   *
   * @param ope
   *          The operation type
   * @throws CheckDataException
   *          Errors in the input parameter
   */
  public void check(OperationType ope) throws CheckDataException {
    if (pingTargets.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (PingTargetsRequest ping : pingTargets) {
        if (ping == null) {
          throw new CheckDataException();
        } else {
          ping.check(ope);
        }
      }
    }
  }

}
