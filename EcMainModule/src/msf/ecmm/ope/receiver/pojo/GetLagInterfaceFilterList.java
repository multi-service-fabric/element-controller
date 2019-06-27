/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.LagIfFilter;

/**
 *Getting list of LagIF Filter Information.
 */
public class GetLagInterfaceFilterList extends AbstractResponseMessage {
  private ArrayList<LagIfFilter> lagIfFilters = new ArrayList<>();

  /**
   * Getting list of LagIF Filter Information.
   *
   * @return List of LagIF Filter Information
   */
  public ArrayList<LagIfFilter> getLagIfFilterList() {
    return lagIfFilters;
  }

  /**
   * Setting list of LafIF Filter Information.
   *
   * @param lagIfFilterList
   *          List of LafIF Filter Information
   */
  public void setLagIfFilterList(ArrayList<LagIfFilter> lagIfFilterList) {
    this.lagIfFilters = lagIfFilterList;
  }

  @Override
  public String toString() {
    return "GetLagInterfaceFilterList[lagIfFilterList" + lagIfFilters + "]";
  }

  /**
   * Checking the input parameter.
   *
   * @param ope
   *          Operation Type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (lagIfFilters == null) {
      throw new CheckDataException();
    }
  }
}
