/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.LagIfCreateLagIf;

/**
 * LagIF Generation.
 *
 */
public class CreateLagInterface extends AbstractRestMessage {

  /** Information of the device in which LagIF is created. */
  private LagIfCreateLagIf lagIf;

  /**
   * Getting information of the device in which LagIF is created.
   *
   * @return information of the device in which LagIF is created
   */
  public LagIfCreateLagIf getLagIf() {
    return lagIf;
  }

  /**
   * Setting information of the device in which LagIF is created.
   *
   * @param lagIf
   *          information of the device in which LagIF is created
   */
  public void setLagIf(LagIfCreateLagIf lagIf) {
    this.lagIf = lagIf;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "CreateLagInterface [lagIf=" + lagIf + "]";
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
    if (lagIf == null) {
      throw new CheckDataException();
    } else {
      lagIf.check(ope);
    }
  }

}
