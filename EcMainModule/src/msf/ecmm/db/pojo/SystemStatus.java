/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * System Status Information POJO Class.
 */
public class SystemStatus implements Serializable {

  /** Service Start-up Status. */
  private int service_status = 0;
  /** Maintenance Blockage Status. */
  private int blockade_status = 0;

  /**
   * Generating new instance.
   */
  public SystemStatus() {
    super();
  }

  /**
   * Getting service start-up status.
   *
   * @return service start-up status
   */
  public int getService_status() {
    return service_status;
  }

  /**
   * Setting service start-up status.
   *
   * @param service_status
   *          service start-up status
   */
  public void setService_status(int service_status) {
    this.service_status = service_status;
  }

  /**
   * Getting Maintenance Blockage status.
   *
   * @return Maintenance Blockage status
   */
  public int getBlockade_status() {
    return blockade_status;
  }

  /**
   * Setting Maintenance Blockage status.
   *
   * @param blockade_status
   *          Maintenance Blockage status
   */
  public void setBlockade_status(int blockade_status) {
    this.blockade_status = blockade_status;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    Integer tmp1 = service_status;
    Integer tmp2 = blockade_status;
    return tmp1.hashCode() + tmp2.hashCode();
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

    SystemStatus target = (SystemStatus) obj;
    if (this.service_status == target.getService_status() && this.blockade_status == target.getBlockade_status()) {
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
    return "SystemStatus [service_status=" + service_status + ", blockade_status=" + blockade_status + "]";
  }
}
