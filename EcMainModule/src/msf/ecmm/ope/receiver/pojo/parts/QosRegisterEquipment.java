/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * QoS Capability Configuration Rest Class.
 */
public class QosRegisterEquipment {

  /** Inflow / Outflow amount Controll. */
  private Shaping shaping = null;

  /** Remark Menu Configuration. */
  private Remark remark = null;

  /** Egress Queue Configuration. */
  private Egress egress = null;

  /**
   * Getting Inflow / Outflow amount Controll.
   *
   * @return shaping
   */
  public Shaping getShaping() {
    return shaping;
  }

  /**
   * Setting Inflow / Outflow amount Controll.
   *
   * @param shaping
   *          setting shaping
   */
  public void setShaping(Shaping shaping) {
    this.shaping = shaping;
  }

  /**
   * Getting Remark Menu Configuration.
   *
   * @return remark
   */
  public Remark getRemark() {
    return remark;
  }

  /**
   * Setting Remark Menu Configuration.
   *
   * @param remark
   *          setting remark
   */
  public void setRemark(Remark remark) {
    this.remark = remark;
  }

  /**
   * Getting Egress Queue Configuration.
   *
   * @return egress
   */
  public Egress getEgress() {
    return egress;
  }

  /**
   * Setting Egress Queue Configuration.
   *
   * @param egress
   *          setting egress
   */
  public void setEgress(Egress egress) {
    this.egress = egress;
  }

  /*
   *  Stringizing Instance
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "QosRegisterEquipment [shaping=" + shaping + ", remark=" + remark + ", egress=" + egress + "]";
  }

   /**
   * Input Parameter Check.
   *
   * @param operationType
   *           operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType operationType) throws CheckDataException {

    if (shaping == null) {
      throw new CheckDataException();
    } else {
      shaping.check(operationType);
    }

    if (remark == null) {
      throw new CheckDataException();
    } else {
      remark.check(operationType);
    }

    if (egress != null) {
      egress.check(operationType);
    }
  }

}
