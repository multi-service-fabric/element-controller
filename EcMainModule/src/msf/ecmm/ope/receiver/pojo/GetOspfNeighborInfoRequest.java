/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.OspfNeighborsRequest;

/**
 * The OSPF neighbor information is acquired(in the response).
 */
public class GetOspfNeighborInfoRequest extends AbstractRestMessage {

  /** The OSPF neighbor information. */
  private List<OspfNeighborsRequest> ospfNeighbors = new ArrayList<>();

  /**
   * The OSPF neighbor information is acquired.
   *
   * @return The OSPF neighbor information
   */
  public List<OspfNeighborsRequest> getOspfNeighbors() {
    return ospfNeighbors;
  }

  /**
   * The OSPF neighbor information is set.
   *
   * @param ospfNeighbors
   *          The OSPF neighbor information
   */
  public void setOspfNeighbors(List<OspfNeighborsRequest> ospfNeighbors) {
    this.ospfNeighbors = ospfNeighbors;
  }

  @Override
  public String toString() {
    return "GetOspfNeighborInfoRequest [ospfNeighbors=" + ospfNeighbors + "]";
  }

  /**
   * The input parameter is checked.
   *
   * @param ope
   *          The operation type
   * @throws CheckDataException
   *          Errors in the input paramter
   */
  public void check(OperationType ope) throws CheckDataException {
    if (ospfNeighbors.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (OspfNeighborsRequest ospfNeighbor : ospfNeighbors) {
        if (ospfNeighbor == null) {
          throw new CheckDataException();
        } else {
          ospfNeighbor.check(ope);
        }
      }
    }
  }

}
