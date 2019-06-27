/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Clag backup IP setting information class.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "backup")
public class ClagBackup {
  /** Backup IP address. */
  private String address = null;

  /**
   * Acquiring backup IP.
   *
   * @return Backup IP
   */
  public String getAddress() {
    return address;
  }

  /**
   * Setting backup IP.
   *
   * @param address
   *          Backup IP
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "ClagBackup [address=" + address + "]";
  }
}
