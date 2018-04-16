/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * QoS Configuration Registration Infomation.
 */
public class QosGetVlanIfs {

  /** QoS capability. */
  private QosCapabilities capability = null;

  /** QoS Configuration value. */
  private QosConfigValues setValue = null;

  /**
   * Getting QoS capability.
   *
   * @return capability
   */
  public QosCapabilities getCapability() {
    return capability;
  }

  /**
   * Setting QoS capability.
   *
   * @param capability
   *          setting capability
   */
  public void setCapability(QosCapabilities capability) {
    this.capability = capability;
  }

  /**
   * Getting QoS Configuration value.
   *
   * @return setValue
   */
  public QosConfigValues getSetValue() {
    return setValue;
  }

  /**
   * Setting QoS Configuration value.
   *
   * @param setValue
   *          setting setValue
   */
  public void setSetValue(QosConfigValues setValue) {
    this.setValue = setValue;
  }

  /*
   * Stringizing Instance
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "QosGetVlanIfs [capability=" + capability + ", setValue=" + setValue + "]";
  }

}
