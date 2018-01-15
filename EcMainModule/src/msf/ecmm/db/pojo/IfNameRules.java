/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * Physical IF Naming Convention Information POJO Class.
 */
public class IfNameRules implements Serializable {

  /** Device ID. */
  private String equipment_type_id = null;
  /** Port Rate. */
  private String speed = null;
  /** Port Name Prefix. */
  private String port_prefix = null;

  /**
   * Generating new instance.
   */
  public IfNameRules() {
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
   * Getting port speed.
   *
   * @return port speed
   */
  public String getSpeed() {
    return speed;
  }

  /**
   * Setting port speed.
   *
   * @param speed
   *          port speed
   */
  public void setSpeed(String speed) {
    this.speed = speed;
  }

  /**
   * Getting port name prefix.
   *
   * @return port name prefix
   */
  public String getPort_prefix() {
    return port_prefix;
  }

  /**
   * Setting port name prefix.
   *
   * @param port_prefix
   *          port name prefix
   */
  public void setPort_prefix(String port_prefix) {
    this.port_prefix = port_prefix;
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
    if (speed != null) {
      hashCode ^= speed.hashCode();
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

    IfNameRules target = (IfNameRules) obj;
    if (this.equipment_type_id.equals(target.getEquipment_type_id()) && this.speed.equals(target.getSpeed())) {
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
    return "IfNameRules [equipment_type_id=" + equipment_type_id + ", speed=" + speed + ", port_prefix=" + port_prefix
        + "]";
  }
}
