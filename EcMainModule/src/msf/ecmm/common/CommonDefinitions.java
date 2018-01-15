/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.common;

/**
 * System Common Constants Definition.
 */
public class CommonDefinitions {

  /** Logger which is used in the entire EC. */
  public static final String EC_LOGGER = "ApplicationLogger";
  /** trace log constant (start). */
  public static final String START = "start";
  /** trace log constant (end). */
  public static final String END = "end";

  /** Leaf. */
  public static final int NODE_TYPE_LEAF = 1;
  /** Spine. */
  public static final int NODE_TYPE_SPINE = 2;

  /** A side. */
  public static final int NODE_PLANE_A = 1;
  /** B side. */
  public static final int NODE_PLANE_B = 2;

  /** active. */
  public static final int NODE_STATE_IN_SERVICE = 0;
  /** waiting for device start. */
  public static final int NODE_STATE_BEFORE_SETTING = 1;
  /** ZTP completed. */
  public static final int NODE_STATE_ZTP_COMPLETE = 2;
  /** infrastructure configurtion completed. */
  public static final int NODE_STATE_NODE_RESETTING_COMPLETE = 3;
  /** out of order (expansion failed). */
  public static final int NODE_STATE_FAILER_SETTING = 4;
  /** infrastructure configuration failed. */
  public static final int NODE_STATE_FAILER_NODE_RESETTING = 5;
  /** service configuration failed. */
  public static final int NODE_STATE_FAILER_SERVICE_SETTING = 6;
  /** out of order (other). */
  public static final int NODE_STATE_FAILER_OTHER = 7;
  /** active (text string). */
  public static final String NODE_STATE_IN_SERVICE_STRING = "in-service";
  /** waiting for device start (string). */
  public static final String NODE_STATE_BEFORE_SETTING_STRING = "before-setting";
  /** ZTP completed (string). */
  public static final String NODE_STATE_ZTP_COMPLETE_STRING = "ztp-complete";
  /** infrastructure configuration completed (string). */
  public static final String NODE_STATE_NODE_RESETTING_COMPLETE_STRING = "node-resetting-complete";
  /** out of order (expansion failed) (string). */
  public static final String NODE_STATE_FAILER_SETTING_STRING = "failer-setting";
  /** infrastructure configuration failed (string). */
  public static final String NODE_STATE_FAILER_NODE_RESETTING_STRING = "failer-node-resetting";
  /** service configuration failed (string). */
  public static final String NODE_STATE_FAILER_SERVICE_SETTING_STRING = "failer-service-setting";
  /** out of order (other) (string). */
  public static final String NODE_STATE_FAILER_OTHER_STRING = "failer-other";

  /** notification received (start success). */
  public static final int RECV_OK_NOTIFICATION = 1;
  /** notification received (start failed). */
  public static final int RECV_NG_NOTIFICATION = 2;
  /** waiting for notification reception. */
  public static final int WAIT_NOTIFICATION = 3;
  /** notification received (start success, string). */
  public static final String RECV_OK_NOTIFICATION_STRING = "success";
  /** notification received (start failed, string). */
  public static final String RECV_NG_NOTIFICATION_STRING = "failed";
  /** waiting for notification reception (string). */
  public static final String WAIT_NOTIFICATION_STRING = "cancel";

  /** IF status normal. */
  public static final int IF_STATE_OK = 1;
  /** IF status error. */
  public static final int IF_STATE_NG = 2;
  /** IF status error (case of L3CP). */
  public static final int IF_STATE_NG_L3CP = 7;
  /** IF status unknown. */
  public static final int IF_STATE_UNKNOWN = 0;

  /** IF status normaru (string). */
  public static final String IF_STATE_OK_STRING = "up";
  /** IF status error (string). */
  public static final String IF_STATE_NG_STRING = "down";
  /** IF status unknown (string). */
  public static final String IF_STATE_UNKNOWN_STRING = "unknown";

  /** link up */
  public static final String LINK_STATUS_LINKUP = "linkup";
  /** link down */
  public static final String LINK_STATUS_LINKDOWN = "linkdown";

  /** device status (active). */
  public static final String IF_NODE_UP_STRING = "up";
  /** device status (out of order). */
  public static final String IF_NODE_DOWN_STRING = "down";

  /** value (no configuration). */
  public static final int NOT_SET = -1;

  /** physical IF. */
  public static final String IF_TYPE_PHYSICAL = "physical";
  /** LAG IF. */
  public static final String IF_TYPE_LAG = "lag";
  /** physical IF. */
  public static final String IF_TYPE_PHYSICAL_IF = "physical-if";
  /** LAG IF. */
  public static final String IF_TYPE_LAG_IF = "lag-if";
  /** breakout IF. */
  public static final String IF_TYPE_BREAKOUT_IF = "breakout-if";
  /** VLAN IF. */
  public static final String IF_TYPE_VLAN_IF = "vlan-if";

  /** blocked. */
  public static final int EC_BUSY_VALUE = 1;
  /** opened. */
  public static final int EC_IN_SERVICE_VALUE = 0;
  /** blocked (string). */
  public static final String EC_BUSY_STRING = "busy";
  /** opened (string). */
  public static final String EC_IN_SERVICE_STRING = "inservice";

  /** device model ID. */
  public static final String KEY_EQUIPMENT_TYPE_ID = "equipment_type_id";
  /** device type. */
  public static final String KEY_FABRIC_TYPE = "fabric_type";
  /** device ID. */
  public static final String KEY_NODE_ID = "node_id";
  /** physical IF ID. */
  public static final String KEY_PHYSICAL_IF_ID = "physical_if_id";
  /** Lag IF ID. */
  public static final String KEY_LAG_IF_ID = "lag_if_id";
  /** slice ID. */
  public static final String KEY_SLICE_ID = "slice_id";
  /** CP ID. */
  public static final String KEY_CP_ID = "cp_id";
  /** stop type. */
  public static final String KEY_STOP_TYPE = "stop_type";
  /** instruction type. */
  public static final String KEY_INSTRUCTION_TYPE = "instruction_type";
  /** type. */
  public static final String KEY_LINK_STATUS = "link_status";
  /** start result. */
  public static final String KEY_STATUS = "status";
  /** cluster ID. */
  public static final String KEY_CLUSTER_ID = "cluster_id";
  /** VLAN IF ID. */
  public static final String KEY_VLAN_IF_ID = "vlan_if_id";
  /** Breakout IF ID. */
  public static final String KEY_BREAKOUT_IF_ID = "breakout_if_id";

  /** normal router (switch fablic). */
  public static final int ROUTER_TYPE_NORMAL = 0;
  /** core router. */
  public static final int ROUTER_TYPE_COREROUTER = 1;
  /** not installed. */
  public static final int ROUTER_TYPE_UNKNOWN = 2;
  /** normal router (string). */
  public static final String ROUTER_TYPE_NORMAL_STRING = "normal";
  /** core router (string). */
  public static final String ROUTER_TYPE_COREROUTER_STRING = "core-router";
  /** not installed (string). */
  public static final String ROUTER_TYPE_UNKNOWN_STRING = "unknown";

  /** Spine. */
  public static final String NODETYPE_SPINE = "Spine";
  /** Leaf. */
  public static final String NODETYPE_LEAF = "Leaf";
  /** B-Leaf. */
  public static final String NODETYPE_BLEAF = "B-Leaf";
  /** RR. */
  public static final String NODETYPE_RR = "RR";

  /** L3VPN. */
  public static final String VPNTYPE_L3 = "l3";
  /** L2VPN. */
  public static final String VPNTYPE_L2 = "l2";

  /** access. */
  public static final int VLAN_PORTMODE_ACCESS = 1;
  /** trunk. */
  public static final int VLAN_PORTMODE_TRUNK = 2;
  /** access (string). */
  public static final String VLAN_PORTMODE_ACCESS_STRING = "access";
  /** trunk (string). */
  public static final String VLAN_PORTMODE_TRUNK_STRING = "trunk";

  /** master. */
  public static final int BGP_ROLE_MASTER = 1;
  /** slave. */
  public static final int BGP_ROLE_SLAVE = 2;
  /** master (string). */
  public static final String BGP_ROLE_MASTER_STRING = "master";
  /** slave (string). */
  public static final String BGP_ROLE_SLAVE_STRING = "slave";

  /** IPv4. */
  public static final int STATIC_ROUTEADDRESS_TYPE_IPV4 = 4;
  /** IPv6. */
  public static final int STATIC_ROUTEADDRESS_TYPE_IPV6 = 6;
  /** IPv4 (string). */
  public static final String STATIC_ROUTEADDRESS_TYPE_IPV4_STRING = "ipv4";
  /** IPv6 (string). */
  public static final String STATIC_ROUTEADDRESS_TYPE_IPV6_STRING = "ipv6";

  /** master. */
  public static final int VRRP_ROLE_MASTER = 1;
  /** slave. */
  public static final int VRRP_ROLE_SLAVE = 2;
  /** master (string). */
  public static final String VRRP_ROLE_MASTER_STRING = "master";
  /** slave (string). */
  public static final String VRRP_ROLE_SLAVE_STRING = "slave";

  /** open. */
  public static final String PORT_CONDITION_ENABLE = "enable";
  /** block. */
  public static final String PORT_CONDITION_DISABLE = "disable";

  /** Giga. */
  public static final String SPEED_UNIT_GIGA = "g";

  /** B-Leaf change. */
  public static final String CHANGE_B_LEAF = "chg_b_leaf";
  /** Leaf change. */
  public static final String CHANGE_LEAF = "chg_leaf";
  /** OSPF rout aggregation configuration add (become B-Leaf). */
  public static final String ADD_OSPF_ROUTE = "add_ospf_route";
  /** OSPF rout aggregation configuration delete (become Leaf). */
  public static final String DEL_OSPF_ROUTE = "delete_ospf_route";

  /** core router host name. */
  public static final String COREROUTER_HOSTNAME = "Corerouter";

  /** Add. */
  public static final String OPERATION_TYPE_ADD = "add";
  /** Delete. */
  public static final String OPERATION_TYPE_DELETE = "delete";

  /** Controller to be acquired. */
  public static final String KEY_CONTROLLER = "controller";
  /** acquired info. */
  public static final String KEY_GET_INFO = "get_info";
  /** acqiured info. */
  public static final String KEY_START_DATE = "start_date";
  /** acquired info. */
  public static final String KEY_END_DATE = "end_date";
  /** acquired info. */
  public static final String KEY_LIMIT_NUMBER = "limit_number";

}
