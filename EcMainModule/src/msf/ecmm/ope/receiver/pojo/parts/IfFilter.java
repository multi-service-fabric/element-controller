/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * VLANIF Filter Information.
 */
public class IfFilter {
  private ArrayList<Terms> terms = new ArrayList<>();

  /**
   * Getting condition list.
   *
   * @return  Condition List
   */
  public ArrayList<Terms> getTerms() {
    return terms;
  }

  /**
   * Setting condition list.
   *
   * @param termNames
   *          Condition List
   */
  public void setTerms(ArrayList<Terms> termNames) {
    this.terms = termNames;
  }

  @Override
  public String toString() {
    return "IfFilter[terms" + terms + "]";
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
    if (null != terms) {
      for (Terms term : terms) {
        term.check(ope);
      }
    }
  }
}
