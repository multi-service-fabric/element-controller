/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * Model IF Information Table POJO Class.
 */
public class EquipmentIfs implements Serializable {

  /** Model ID. */
  private String equipment_type_id = null;
  /** Physical IF ID. */
  private String physical_if_id = null;
  /** IF Slot Name. */
  private String if_slot = null;
  /** Port Rate. */
  private String port_speed = null;

  /**
   * Creating new instance.
   */
  public EquipmentIfs() {
    super();
  }

  /**
   * Getting model ID.
   *
   * @return model ID
   */
  public String getEquipment_type_id() {
    return equipment_type_id;
  }

  /**
   * Setting model ID.
   *
   * @param equipment_type_id
   *          model ID
   */
  public void setEquipment_type_id(String equipment_type_id) {
    this.equipment_type_id = equipment_type_id;
  }

  /**
   * Getting physical IF ID.
   *
   * @return physical IF ID
   */
  public String getPhysical_if_id() {
    return physical_if_id;
  }

  /**
   * Setting physical IF ID.
   *
   * @param physical_if_id
   *          physical IF ID
   */
  public void setPhysical_if_id(String physical_if_id) {
    this.physical_if_id = physical_if_id;
  }

  /**
   * Getting IF slot name.
   *
   * @return IF slot name
   */
  public String getIf_slot() {
    return if_slot;
  }

  /**
   * Setting IF slot name.
   *
   * @param if_slot
   *          IF slot name
   */
  public void setIf_slot(String if_slot) {
    this.if_slot = if_slot;
  }

  /**
   * Getting port speed.
   *
   * @return port speed
   */
  public String getPort_speed() {
    return port_speed;
  }

  /**
   * Setting port speed.
   *
   * @param port_speed
   *          port speed
   */
  public void setPort_speed(String port_speed) {
    this.port_speed = port_speed;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public synchronized int hashCode() {
    int hashCode = 0;
    if (equipment_type_id != null) {
      hashCode ^= equipment_type_id.hashCode();
    }
    if (physical_if_id != null) {
      hashCode ^= physical_if_id.hashCode();
    }
    if (port_speed != null) {
      hashCode ^= port_speed.hashCode();
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

    EquipmentIfs target = (EquipmentIfs) obj;
    if (this.equipment_type_id.equals(target.getEquipment_type_id())
        && this.physical_if_id.equals(target.getPhysical_if_id()) && this.port_speed.equals(target.getPort_speed())) {
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
    return "EquipmentIfs [equipment_type_id=" + equipment_type_id + ", physical_if_id=" + physical_if_id + ", if_slot="
        + if_slot + ", port_speed=" + port_speed + "]";
  }
}
