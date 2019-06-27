/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.pojo;

import java.io.Serializable;

/**
 * Device Start-up Notification Information POJO Class.
 */
public class NodesStartupNotification implements Serializable {

  /** Device ID. */
  private String node_id = null;
  /** Notification reception status. */
  private int notification_reception_status = 0;

  /**
   * Device Start-up Notification Information POJO Class Constuctor.
   *
   * @param session
   *          data base session
   */
  public NodesStartupNotification() {
    super();
  }

  /**
   * GettingDevice ID.
   *
   * @return Device ID
   */
  public String getNode_id() {
    return node_id;
  }

  /**
   * Setting Device ID.
   *
   * @param node_id
   *          Device ID
   */
  public void setNode_id(String node_id) {
    this.node_id = node_id;
  }

  /**
   * Getting Device Start-up status.
   *
   * @return Device start-up status
   */
  public int getNotification_reception_status() {
    return notification_reception_status;
  }

  /**
   * Setting device start-up status.
   *
   * @param notification_reception_status
   *          Device start-up status
   */
  public void setNotification_reception_status(int notification_reception_status) {
    this.notification_reception_status = notification_reception_status;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    int hashCode = 0;
    if (node_id != null) {
      hashCode ^= node_id.hashCode();
    }
    return hashCode;
  }

  /*
   * (Non Javadoc)
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

    NodesStartupNotification target = (NodesStartupNotification) obj;
    if (this.node_id.equals(target.getNode_id())) {
      return true;
    }
    return false;
  }

  /*
   * (Non Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "NodesStartupNotification [node_id=" + node_id + ", notification_reception_status="
        + notification_reception_status + "]";
  }

}
