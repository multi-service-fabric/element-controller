/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * LagIF Creating Device Information.
 *
 */
public class LagIfCreateLagIf {

  /** LagIF ID. */
  private String lagIfId;

  /** Physical IF List. */
  private List<PhysicalIfsCreateLagIf> physicalIfs = new ArrayList<PhysicalIfsCreateLagIf>();

  /**
   * Getting LagIF ID.
   *
   * @return LagIF ID
   */
  public String getLagIfId() {
    return lagIfId;
  }

  /**
   * Setting LagIF ID.
   *
   * @param lagIfId
   *          LagIF ID
   */
  public void setLagIfId(String lagIfId) {
    this.lagIfId = lagIfId;
  }

  /**
   * Getting physical IF list.
   *
   * @return physical IF list
   */
  public List<PhysicalIfsCreateLagIf> getPhysicalIfs() {
    return physicalIfs;
  }

  /**
   * Setting physical IF list.
   *
   * @param list
   *          physical IF list
   */
  public void setPhysicalIfList(ArrayList<PhysicalIfsCreateLagIf> list) {
    this.physicalIfs = list;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "LagIfCreateLagIf [lagIfId=" + lagIfId + ", physicalIfs=" + physicalIfs + "]";
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
    if (lagIfId == null || physicalIfs.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (PhysicalIfsCreateLagIf physi : physicalIfs) {
        if (physi == null) {
          throw new CheckDataException();
        } else {
          physi.check(ope);
        }
      }
    }
  }

}
