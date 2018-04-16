/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.ope.execute.OperationType;

/**
 * breakoutIF Deletion.
 */
public class DeleteBreakoutIf extends AbstractRestMessage {

  /** Device ID. */
  private String nodeId = null;

  /** breakoutIF ID List. */
  private List<String> breakoutIfId = new ArrayList<String>();

  /**
   * Getting device ID.
   *
   * @return device ID
   */
  public String getNodeId() {
    return nodeId;
  }

  /**
   * Setting device ID.
   *
   * @param id
   *          device ID
   */
  public void setNodeId(String id) {
    this.nodeId = id;
  }

  /**
   * Getting breakoutIF ID list.
   *
   * @return breakoutIfId
   */
  public List<String> getBreakoutIfId() {
    return breakoutIfId;
  }

  /**
   * Setting breakoutIF ID list.
   *
   * @param list
   *          set list
   */
  public void setBreakoutIfId(List<String> list) {
    this.breakoutIfId = list;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "DeleteBreakoutIf [nodeId=" + nodeId + ", breakoutIfId=" + breakoutIfId + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (nodeId == null) {
      throw new CheckDataException();
    }

    if (breakoutIfId == null || breakoutIfId.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (String breakoutIfId : breakoutIfId) {
        if (breakoutIfId == null) {
          throw new CheckDataException();
        }
      }
    }
  }
}
