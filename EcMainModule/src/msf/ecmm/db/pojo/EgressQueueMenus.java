/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * Egress Queue Menu Information class.
 */
public class EgressQueueMenus implements Serializable {

  /** model ID. */
  private String equipment_type_id = null;
  /** Egress Queue Menu. */
  private String egress_queue_menu = null;

  /**
   * Generating new instance.
   */
  public EgressQueueMenus() {
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
   * Getting Egress Queue Menu.
   *
   * @return Egress Queue Menu
   */
  public String getEgress_queue_menu() {
    return egress_queue_menu;
  }

  /**
   * Setting Egress Queue Menu.
   *
   * @param egress_queue_menu
   *          Egress Queue Menu
   */
  public void setEgress_queue_menu(String egress_queue_menu) {
    this.egress_queue_menu = egress_queue_menu;
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

    EgressQueueMenus target = (EgressQueueMenus) obj;
    if (this.equipment_type_id.equals(target.getEquipment_type_id())
        && this.egress_queue_menu.equals(target.getEgress_queue_menu())) {
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
    return "EgressQueueMenus [equipment_type_id=" + equipment_type_id + ", egress_queue_menu=" + egress_queue_menu + "]";
  }

}
