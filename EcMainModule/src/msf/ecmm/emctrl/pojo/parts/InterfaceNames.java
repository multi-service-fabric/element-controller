/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl.pojo.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * Expansion Restoration of Node-interface infomation class.<br>
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class InterfaceNames {

  /** IF Name after restoration. */
  String name = null;

  /** IF Name befor restoration. */
  @XmlElement(name = "old-name")
  String oldName = null;

  /**
   * Generating new instance.
   */
  public InterfaceNames() {
    super();
  }

  /**
   * Getting IF Name after restoration.
   *
   * @return IF Name after restoration.
   */
  public String getName() {
    return name;
  }

  /**
   * Setting IF Name after restoration.
   *
   * @param name
   *         IF Name after restoration.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Getting IF Name befor restoration.
   *
   * @return IF Name befor restoration.
   */
  public String getOldName() {
    return oldName;
  }

  /**
   * Setting IF Name befor restoration.
   *
   * @param oldName
   *          IF Name befor restoration.
   */
  public void setOldName(String oldName) {
    this.oldName = oldName;
  }

  /**
   * Stringizing Instance.
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "InterfaceNames [name=" + name + ", oldName=" + oldName + "]";
  }

}
