/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * The ping infromation(in the request).
 */
public class PingTargetsRequest {

  /** The source information. */
  private BaseIfInfo src;

  /** The destination  information. */
  private BaseIfInfo dst;

  /**
   * The source information is acquired.
   *
   * @return The source information
   */
  public BaseIfInfo getSrc() {
    return src;
  }

  /**
   * The source information is set.
   *
   * @param src
   *          The source information
   */
  public void setSrc(BaseIfInfo src) {
    this.src = src;
  }

  /**
   * The destination information is acquired.
   *
   * @return The destination information
   */
  public BaseIfInfo getDst() {
    return dst;
  }

  /**
   * The destination information is set.
   *
   * @param dst
   *          The destination information
   */
  public void setDst(BaseIfInfo dst) {
    this.dst = dst;
  }

  @Override
  public String toString() {
    return "RequestPingTargets [src=" + src + ", dst=" + dst + "]";
  }

  /**
   * The input parameter is checked.
   *
   * @param ope
   *          The operation type
   * @throws CheckDataException
   *           The input paramter error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (src == null) {
      throw new CheckDataException();
    } else {
      src.check(ope);
    }

    if (dst == null) {
      throw new CheckDataException();
    } else {
      dst.check(ope);
    }
  }
}
