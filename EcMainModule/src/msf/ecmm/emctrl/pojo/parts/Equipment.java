/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Spine Extention-Spine Device-Device Information Class
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Equipment {

  /** Platform Name */
  private String platform = null;

  /** OS Name */
  private String os = null;

  /** Firmware Version */
  private String firmware = null;

  /** Login ID */
  private String loginid = null;

  /** Password */
  private String password = null;

  /** Device Configuration Necessity Flag */
  @XmlElement(name = "newly-establish")
  private String newlyEstablish;

  /**
   * Generating new instance.
   */
  public Equipment() {
    super();
  }

  /**
   * Getting platform name.
   *
   * @return platform name
   */
  public String getPlatform() {
    return platform;
  }

  /**
   * Setting platform name.
   *
   * @param platform
   *          platform name
   */
  public void setPlatform(String platform) {
    this.platform = platform;
  }

  /**
   * Getting OS name.
   *
   * @return OS name
   */
  public String getOs() {
    return os;
  }

  /**
   * Setting OS name.
   *
   * @param os
   *          OS name
   */
  public void setOs(String os) {
    this.os = os;
  }

  /**
   * Getting firmware version.
   *
   * @return firmware version
   */
  public String getFirmware() {
    return firmware;
  }

  /**
   * Setting firmware version.
   *
   * @param firmware
   *          firmware version
   */
  public void setFirmware(String firmware) {
    this.firmware = firmware;
  }

  /**
   * Getting login ID.
   *
   * @return login ID
   */
  public String getLoginid() {
    return loginid;
  }

  /**
   * Setting login ID.
   *
   * @param loginid
   *          login ID
   */
  public void setLoginid(String loginid) {
    this.loginid = loginid;
  }

  /**
   * Getting password.
   *
   * @return password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Setting password.
   *
   * @param password
   *          password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Adding newlyEstablish.
   */
  public void addNewlyEstablish() {
    this.newlyEstablish = new String("");
  }

  /**
   * Deleting newlyEstablish.
   */
  public void delNewlyEstablish() {
    this.newlyEstablish = null;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Equipment [platform=" + platform + ", os=" + os + ", firmware=" + firmware + ", loginid=" + loginid
        + ", password=" + password + ", newlyEstablish=" + newlyEstablish + "]";
  }

}
