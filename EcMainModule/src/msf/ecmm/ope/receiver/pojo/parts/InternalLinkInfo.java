/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Internal Link Information.
 */
public class InternalLinkInfo {

  private InternalLinkIf internalLinkIf;

  /**
   * Getting internalLinkIf.
   *
   * @return internalLinkIf
   */
  public InternalLinkIf getInternalLinkIf() {
    return internalLinkIf;
  }

  /**
   * Setting internalLinkIf.
   *
   * @param internalLinkIf
   *          internalLinkIf
   */
  public void setInternalLinkIf(InternalLinkIf internalLinkIf) {
    this.internalLinkIf = internalLinkIf;
  }

  @Override
  public String toString() {
    return "InternalLinkInfo [internalLinkIf=" + internalLinkIf + "]";
  }

  /**
   * Input Parametr Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (internalLinkIf == null) {
      throw new CheckDataException();
    } else {
      internalLinkIf.check(ope);
    }
  }
}
