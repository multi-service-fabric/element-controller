/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * String received from snmptrapd
 */
public class Varbind {

  /** oid */
  private String oid;

  /** oid value */
  private String value;

  /**
   * Getting oid.
   *
   * @return oid
   */
  public String getOid() {
    return oid;
  }

  /**
   * Setting oid.
   *
   * @param oid
   *          oid
   */
  public void setOid(String oid) {
    this.oid = oid;
  }

  /**
   * Getting oid value.
   *
   * @return oid value
   */
  public String getValue() {
    return value;
  }

  /**
   * Setting oid value.
   *
   * @param value
   *          oid value
   */
  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "Varbind [oid=" + oid + ", value=" + value + "]";
  }

  /**
   * Input Parameter Check
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (oid == null) {
      throw new CheckDataException();
    } else {
    }
    if (value == null) {
      throw new CheckDataException();
    } else {
    }
  }

}
