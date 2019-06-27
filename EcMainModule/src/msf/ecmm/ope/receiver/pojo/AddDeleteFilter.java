/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.IfFilter;

/**
 * Filter Addition Class.
 */
public class AddDeleteFilter extends AbstractRestMessage {

  /** VLANIF Filter Information. */
  private IfFilter ifFilter;

  /**
   * Getting the VLANIF Filter Information.
   *
   * @return VLANIF Filter Informaiton
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

  /* (Non Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "AddDeleteFilter [ifFilter=" + ifFilter + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          Operation Type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (ifFilter == null) {
      throw new CheckDataException();
    } else {
      ifFilter.check(ope);
    }

  }

}
