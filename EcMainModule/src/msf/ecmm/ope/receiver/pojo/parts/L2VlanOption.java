/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Optional Information for L2VLAN IF.
 */
public class L2VlanOption {

  /** VRF ID. */
  private String vrfId;

  /** QoS Configuration Infomation. */
  private QosUpdateVlanIf qos = null;

   /**
   * Getting VRF ID.
   *
   * @return vrfId
   */
  public String getVrfId() {
    return vrfId;
  }

  /**
   * Setting VRF ID.
   *
   * @param vrfId
   *          set vrfId
   */
  public void setVrfId(String vrfId) {
    this.vrfId = vrfId;
  }

  /**
   * Getting QoS Configuration Infomation.
   *
   * @return qos
   */
  public QosUpdateVlanIf getQos() {
    return qos;
  }

  /**
   * Setting QoS Configuration Infomation.
   *
   * @param qos
   *          setting qos
   */
  public void setQos(QosUpdateVlanIf qos) {
    this.qos = qos;
  }

  /*
   * Stringizing Instance.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "L2VlanOption [vrfId=" + vrfId + ", qos=" + qos + "]";
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
    if (vrfId == null) {
      throw new CheckDataException();
    }
    if (qos != null) {
      qos.check(ope);
    }
  }

}
