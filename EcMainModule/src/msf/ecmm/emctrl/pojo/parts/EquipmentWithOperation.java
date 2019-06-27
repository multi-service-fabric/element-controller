/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

/**
 * Update Equipment Information.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class EquipmentWithOperation {

  /** Platform name. */
  private XmlStringElement platform = null;

  /** OS name. */
  private XmlStringElement os = null;

  /** Firm version. */
  private XmlStringElement firmware = null;

  /**
   * Creating a new instance.
   */
  public EquipmentWithOperation() {
    super();
  }

  /**
   * Getting platform name.
   *
   * @return platform name
   */
  public XmlStringElement getPlatform() {
    return platform;
  }

  /**
   * Setting platform name.
   *
   * @param platform
   *          platform name
   */
  public void setPlatform(XmlStringElement platform) {
    this.platform = platform;
  }

  /**
   * Getting OS name.
   *
   * @return OS name
   */
  public XmlStringElement getOs() {
    return os;
  }

  /**
   * Setting OS name.
   *
   * @param os
   *          OS name
   */
  public void setOs(XmlStringElement os) {
    this.os = os;
  }

  /**
   * Getting firm version.
   *
   * @return firm version
   */
  public XmlStringElement getFirmware() {
    return firmware;
  }

  /**
   * Setting firm version.
   *
   * @param firmware
   *          firm version
   */
  public void setFirmware(XmlStringElement firmware) {
    this.firmware = firmware;
  }

  /*
   * Stringizing Instance.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "EquipmentWithOperation [platform=" + platform + ", os=" + os + ", firmware=" + firmware + "]";
  }

}
