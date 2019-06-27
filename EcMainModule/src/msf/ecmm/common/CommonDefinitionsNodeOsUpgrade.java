/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.common;

/**
 * Class for defining constants to uprade OS in the node with the extended function.
 */
public class CommonDefinitionsNodeOsUpgrade {

  /** The node status（KEY） when OS is upgraded. */
  public static final String NODE_OS_UPGRADE_STATUS_KEY = "NODE_OS_UPGRADE_STATUS";
  /** The value which means OS upgrade has started. */	
  public static final String NODE_OS_UPGRADE_STARTED = "NODE_OS_UPGRADE_STARTED";
  /** The value which means it is waiting for OS upgrade has finished. */	
  public static final String NODE_OS_UPGRADE_WAIT = "NODE_OS_UPGRADE_WAIT";
  /** The value which means OS has been  successfully upgraded in the node. */
  public static final String NODE_OS_UPGRADE_SUCCESS = "NODE_OS_UPGRADE_SUCCESS";
  /** The value which means OS has not been successfully upgraded in the node. */
  public static final String NODE_OS_UPGRADE_FAILED = "NODE_OS_UPGRADE_FAILED";

  /** The IP address(KEY) for receiving the notification that OS has been upgraded. */
  public static final String NODE_OS_UPGRADE_NOTIFIED_IP_KEY = "NODE_OS_UPGRADE_NOTIFIED_IP_KEY";

  /** the notification(FC-URI) that OS has been upgraded. */
  public static final String NODE_OS_UPGRADE_NOTIFICATION_URI = "/v1/internal/upgrade_operations";

  /** The operation type is the OS. */
  public static final String OPETYPE_OS_UPGRADE = "os_upgrade";
  /** The operation type is the service recovery. */
  public static final String OPETYPE_RECOVER_NODE = "recover_node";
}
