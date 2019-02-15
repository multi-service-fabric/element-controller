/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.LagIfFilter;

/**
 * LagIF Fiter Information Acquisition.
 */
public class GetLagInterfaceFilter extends AbstractResponseMessage {

  /** LagIF Filter Information. */
  private LagIfFilter lagIfFilter;

  /**
   * Getting the LagIF Filter Information.
   *
   * @return LagIF Filter Information
   */
  public LagIfFilter getLagIfFilter() {
    return lagIfFilter;
  }

  /**
   * Setting the LagIF Filter Information.
   *
   * @param lagIfFilter
   *          LagIF Filter Information
   */
  public void setLagIfFilter(LagIfFilter lagIfFilter) {
    this.lagIfFilter = lagIfFilter;
  }

  @Override
  public String toString() {
    return "GetLagInterfaceFilter[lagIfFilter" + lagIfFilter + "]";
  }

  /**
   * Checking input parameter.
   *
   * @param ope
   *          Operation Type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (lagIfFilter == null) {
      throw new CheckDataException();
    }
  }
}
