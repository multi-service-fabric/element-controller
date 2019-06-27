/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
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

  /** VNI. */
  private String vni;

  /** QoS Configuration Information. */
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
   * Getting VNI.
   *
   * @return vni
   */
  public String getVni() {
    return vni;
  }

  /**
   * Setting VNI.
   *
   * @param vni
   *          Setting vni
   */
  public void setVni(String vni) {
    this.vni = vni;
  }

  /**
   * Getting QoS Configuration Information.
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

  @Override
  public String toString() {
    return "L2VlanOption [vrfId=" + vrfId + ", vni=" + vni + ", qos=" + qos + "]";
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
    if (vrfId == null && vni == null) {
      throw new CheckDataException();
    }

    if (vrfId != null && vni != null) {
      throw new CheckDataException();
    }

    if (qos != null) {
      qos.check(ope);
    }
  }

}
