/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/** Parameter required for L3VNI generation. */
/** Value used for l3VNI in IRB support VLANIF generation. Required when setting Symmetric configuration IRB.*/

public class L3VniValue {
  /** VNI value for L3VNI. */
  private Integer vniId = null;

  /** VLANID for L3VNI. */
  private Integer vlanId = null;


  /**
   *  Getting VNI value for L3VNI.
   *
   * @return vniId
   */
  public Integer getVniId() {
    return vniId;
  }

  /**
   *  Setting VNI value for L3VNI.
   *
   * @param vniId
   *          Setting vniId
   */
  public void setVniId(Integer vniId) {
    this.vniId = vniId;
  }

  /**
   *  Getting VLANID for L3VNI.
   *
   * @return vlanId
   */
  public Integer getVlanId() {
    return vlanId;
  }

  /**
   *  Setting VLANID for L3VNI.
   *
   * @param vlanId
   *          Setting vlanId
   */
  public void setVlanId(Integer vlanId) {
    this.vlanId = vlanId;
  }

  /**
   * Stringizing instance.
   *
   * @return Instance string
   */
  @Override
  public String toString() {
    return "L3VniValue [vniId=" + vniId + ", vlanId=" + vlanId + "]";
  }

  /**
   * Input parameter check.
   *
   * @param ope
   *          Operation type
   * @throws CheckDataException
   *           Input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (vniId == null) {
      throw new CheckDataException();
    }
    if (vlanId == null) {
      throw new CheckDataException();
    }

  }

}
