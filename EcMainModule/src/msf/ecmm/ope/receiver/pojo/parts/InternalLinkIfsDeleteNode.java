/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Internal Link Information for Device Removal.
 */
public class InternalLinkIfsDeleteNode {

  /** IF Information for Internal Link. */
  private InterfaceInfo internalLinkIf;

  /**
   * Getting IF information for internal link.
   *
   * @return IF information for internal link
   */
  public InterfaceInfo getIfInfo() {
    return internalLinkIf;
  }

  /**
   * Setting IF information for internal link.
   *
   * @param ifInfo
   *          IF information for internal link
   */
  public void setIfInfo(InterfaceInfo ifInfo) {
    this.internalLinkIf = ifInfo;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "InternalLinkIfsDeleteNode [internalLinkIf=" + internalLinkIf + "]";
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
    if (internalLinkIf == null) {
      throw new CheckDataException();
    } else {
      internalLinkIf.check(ope);
    }
  }
}
