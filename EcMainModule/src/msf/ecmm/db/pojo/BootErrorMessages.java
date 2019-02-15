/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * Start-up Failure Determination Message Information Class.
 */
public class BootErrorMessages implements Serializable {

  /** Model ID. */
  private String equipment_type_id = null;
  /** start-up error message. */
  private String boot_error_msgs = null;

  /**
   * Generating new instance.
   */
  public BootErrorMessages() {
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
   * Getting start-up error message.
   *
   * @return start-up error message
   */
  public String getBoot_error_msgs() {
    return boot_error_msgs;
  }

  /**
   * Setting start-up error message.
   *
   * @param boot_error_msgs
   *          Start-up error message
   */
  public void setBoot_error_msgs(String boot_error_msgs) {
    this.boot_error_msgs = boot_error_msgs;
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

    BootErrorMessages target = (BootErrorMessages) obj;
    if (this.equipment_type_id.equals(target.getEquipment_type_id())
        && this.boot_error_msgs.equals(target.getBoot_error_msgs())) {
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
    return "BootErrorMessages [equipment_type_id=" + equipment_type_id + ", boot_error_msgs=" + boot_error_msgs + "]";
  }

}
