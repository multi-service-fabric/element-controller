/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * Remark Menu Information class.
 */
public class RemarkMenus implements Serializable {

  /** model ID. */
  private String equipment_type_id = null;
  /** Remark Menu. */
  private String remark_menu = null;

  /**
   * Generating new instance.
   */
  public RemarkMenus() {
    super();
  }

  /**
   * Getting Model ID.
   *
   * @return Model ID
   */
  public String getEquipment_type_id() {
    return equipment_type_id;
  }

  /**
   * Setting Model ID.
   *
   * @param equipment_type_id
   *          Model ID
   */
  public void setEquipment_type_id(String equipment_type_id) {
    this.equipment_type_id = equipment_type_id;
  }

  /**
   * Getting Remark Menu.
   *
   * @return Remark Menu
   */
  public String getRemark_menu() {
    return remark_menu;
  }

  /**
   * Setting Remark Menu.
   *
   * @param remark_menu
   *          Remark Menu
   */
  public void setRemark_menu(String remark_menu) {
    this.remark_menu = remark_menu;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int hashCode = 0;
    if (equipment_type_id != null) {
      hashCode ^= equipment_type_id.hashCode();
    }

    return hashCode;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj == null || this.hashCode() != obj.hashCode()) {
      return false;
    }

    if (getClass() != obj.getClass()) {
      return false;
    }

    RemarkMenus target = (RemarkMenus) obj;
    if (this.equipment_type_id.equals(target.getEquipment_type_id())
        && this.remark_menu.equals(target.getRemark_menu())) {
      return true;
    }
    return false;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "RemarkMenus [equipment_type_id=" + equipment_type_id + ", remark_menu=" + remark_menu + "]";
  }

}
