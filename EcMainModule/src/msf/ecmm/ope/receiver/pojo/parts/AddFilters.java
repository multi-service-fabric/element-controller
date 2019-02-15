/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Filter Addition Class.
 */
public class AddFilters extends AbstractRestMessage {

  /** VLANIF Filter Information. */
  private IfFilter ifFilter;

  /**
   * Getting the VLANIF Filter Information.
   *
   * @return  VLANIF Filter Information
   */
  public IfFilter getIfFilter() {
    return ifFilter;
  }

  /**
   * Setting the VLANIF Filter Information.
   *
   * @param ifFilter
   *          VLANIF Filter Information
   */
  public void setIfFilter(IfFilter ifFilter) {
    this.ifFilter = ifFilter;
  }

  @Override
  public String toString() {
    return "AddFilter[ifFilter" + ifFilter + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          Operation Type
   * @throws CheckDataException
   *           Input Check Error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (ifFilter == null) {
      throw new CheckDataException();
    } else {
      ifFilter.check(ope);
    }

  }

}
