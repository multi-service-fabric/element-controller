/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.execute.OperationType;

/**
 * Opening and closing IF.
 */
public class IfBlockAndOpenRequest extends AbstractRestMessage {

  /** IF status ("up"：open "down"：closed). */
  private String status;

  /**
   * Getting IF status.
   *
   * @return IF status
   */
  public String getStatus() {
    return status;
  }

  /**
   * Getting IF status.
   *
   * @param status
   *          IF status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "IfBlockAndOpenRequest [status=" + status + "]";
  }

  /**
   * Input paramter check.
   *
   * @param ope
   *           operation type
   * @throws CheckDataException
   *           input parameter error 
   */
  public void check(OperationType ope) throws CheckDataException {
    if (status == null) {
      throw new CheckDataException();
    } else if (!status.equals(CommonDefinitions.IF_STATE_OK_STRING)
        && !status.equals(CommonDefinitions.IF_STATE_NG_STRING)) {
      throw new CheckDataException();
    }
  }
}
