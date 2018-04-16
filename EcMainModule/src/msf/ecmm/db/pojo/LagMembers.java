/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * Lag Member Information Table POJO Class.
 */
public class LagMembers implements Serializable {

  /** Number. */
  private Integer num = null;
  /** Device ID. */
  private String node_id = null;
  /** LagIF ID. */
  private String lag_if_id = null;
  /** Physical IF ID. */
  private String physical_if_id = null;
  /** breakoutIF ID. */
  private String breakout_if_id = null;

  /**
   * Generating new instance.
   */
  public LagMembers() {
    super();
  }

  /**
   * Getting number.
   *
   * @return number
   */
  public Integer getNum() {
    return num;
  }

  /**
   * Setting number.
   *
   * @param num
   *          number
   */
  public void setNum(Integer num) {
    this.num = num;
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
   * Setting device ID.
   *
   * @param node_id
   *          device ID
   */
  public void setNode_id(String node_id) {
    this.node_id = node_id;
  }

  /**
   * Getting LagIF ID.
   *
   * @return LagIF ID
   */
  public String getLag_if_id() {
    return lag_if_id;
  }

  /**
   * Setting LagIF ID.
   *
   * @param lag_if_id
   *          LagIF ID
   */
  public void setLag_if_id(String lag_if_id) {
    this.lag_if_id = lag_if_id;
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

  /*
   * (Non-Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public synchronized int hashCode() {
    int hashCode = 0;
    if (num != null) {
      hashCode ^= num.hashCode();
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

    LagMembers target = (LagMembers) obj;
    if ((this.num != null) && this.num.equals(target.getNum())) {
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
    return "LagMembers [num=" + num + ", node_id=" + node_id + ", lag_if_id=" + lag_if_id + ", physical_if_id="
        + physical_if_id + ", breakout_if_id=" + breakout_if_id + "]";
  }

}
