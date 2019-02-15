/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * BreakoutIfs Information Table POJO Class.
 */
public class BreakoutIfs implements Serializable {

  /** Device ID. */
  private String node_id = null;
  /** breakoutIF ID. */
  private String breakout_if_id = null;
  /** Physical IF ID. */
  private String physical_if_id = null;
  /** IF Speed. */
  private String speed = null;
  /** IF Name. */
  private String if_name = null;
  /** breakoutIF inidex. */
  private Integer breakout_if_index = null;
  /** IF Status. */
  private int if_status = 0;
  /** Accommodated Device IF Address (IPv4). */
  private String ipv4_address = null;
  /** Accommodated Device IF Prefix (IPv4). */
  private Integer ipv4_prefix = null;

  /**
   * Generating new instance.
   */
  public BreakoutIfs() {
    super();
  }

  /**
   * Getting device ID.
   *
   * @return device ID
   */
  public String getNode_id() {
    return node_id;
  }

  /**
   * Device ID.
   *
   * @param node_id
   *          model information
   */
  public void setNode_id(String node_id) {
    this.node_id = node_id;
  }

  /**
   * Getting breakoutIF ID.
   *
   * @return breakoutIF ID
   */
  public String getBreakout_if_id() {
    return breakout_if_id;
  }

  /**
   * Setting breakoutIF ID.
   *
   * @param breakout_if_id
   *          breakoutIF ID
   */
  public void setBreakout_if_id(String breakout_if_id) {
    this.breakout_if_id = breakout_if_id;
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
   * @param phisical_if_id
   *          physical IF ID
   */
  public void setPhysical_if_id(String phisical_if_id) {
    this.physical_if_id = phisical_if_id;
  }

  /**
   * Getting IF speed.
   *
   * @return IF speed
   */
  public String getSpeed() {
    return speed;
  }

  /**
   * Setting IF speed.
   *
   * @param speed
   *          IF speed
   */
  public void setSpeed(String speed) {
    this.speed = speed;
  }

  /**
   * Getting IF name.
   *
   * @return IF name
   */
  public String getIf_name() {
    return if_name;
  }

  /**
   * Setting IF name.
   *
   * @param if_name
   *          IF name
   */
  public void setIf_name(String if_name) {
    this.if_name = if_name;
  }

  /**
   * Getting Breakout IF index.
   *
   * @return Breakout IF index
   */
  public Integer getBreakout_if_index() {
    return breakout_if_index;
  }

  /**
   * Setting Breakout IF index.
   *
   * @param breakout_if_index
   *          Breakout IF index
   */
  public void setBreakout_if_index(Integer breakout_if_index) {
    this.breakout_if_index = breakout_if_index;
  }

  /**
   * Getting IF status.
   *
   * @return IF status
   */
  public int getIf_status() {
    return if_status;
  }

  /**
   * Setting IF status.
   *
   * @param if_status
   *          IF status
   */
  public void setIf_status(int if_status) {
    this.if_status = if_status;
  }

  /**
   * Getting accommodated device IF address (IPv4).
   *
   * @return accommodated IF address (IPv4)
   */
  public String getIpv4_address() {
    return ipv4_address;
  }

  /**
   * Setting accommodated device IF address (IPv4).
   *
   * @param ipv4_address
   *          accommodated device IF address (IPv4)
   */
  public void setIpv4_address(String ipv4_address) {
    this.ipv4_address = ipv4_address;
  }

  /**
   * Getting accommodated device IF prefix (IPv4).
   *
   * @return accommodated device IF prefix (IPv4)
   */
  public Integer getIpv4_prefix() {
    return ipv4_prefix;
  }

  /**
   * Setting accommodated device IF prefix (IPv4).
   *
   * @param ipv4_prefix
   *          accommodated device IF prefix (IPv4)
   */
  public void setIpv4_prefix(Integer ipv4_prefix) {
    this.ipv4_prefix = ipv4_prefix;
  }

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public synchronized int hashCode() {
    int hashCode = 0;
    if (node_id != null) {
      hashCode ^= node_id.hashCode();
    }
    if (breakout_if_id != null) {
      hashCode ^= breakout_if_id.hashCode();
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

    BreakoutIfs target = (BreakoutIfs) obj;
    if (this.node_id.equals(target.getNode_id()) && this.breakout_if_id.equals(target.getBreakout_if_id())) {
      return true;
    }
    return false;
  }

  /*
   * Stringize instance
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "BreakoutIfs [node_id=" + node_id + ", breakout_if_id=" + breakout_if_id + ", physical_if_id="
        + physical_if_id + ", speed=" + speed + ", if_name=" + if_name + ", breakout_if_index=" + breakout_if_index
        + ", if_status=" + if_status + ", ipv4_address=" + ipv4_address + ", ipv4_prefix=" + ipv4_prefix + "]";
  }

}
