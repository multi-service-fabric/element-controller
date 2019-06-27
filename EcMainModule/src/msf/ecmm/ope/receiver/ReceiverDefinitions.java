/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver;

/**
 * Operation Request Reception Functional Block Constant Definition Class.
 *
 */
public class ReceiverDefinitions {

  /** Context Path. */
  public static final String CONTEXT_PATH = "/";

  /** 200 OK. */
  public static final int RESP_OK_200 = 200;
  /** 201 Created. */
  public static final int RESP_CREATED_201 = 201;
  /** 202 Accepted. */
  public static final int RESP_ACCEPTED_202 = 202;
  /** 204 No Contents. */
  public static final int RESP_NOCONTENTS_204 = 204;
  /** 400 Bad Request. */
  public static final int RESP_BADREQUEST_400 = 400;
  /** 403 Forbidden. */
  public static final int RESP_FORBIDDEN_403 = 403;
  /** 404 Not Found. */
  public static final int RESP_NOTFOUND_404 = 404;
  /** 405 Not Allowed. */
  public static final int RESP_NOTALLOWED_405 = 405;
  /** 406 Not Acceptable. */
  public static final int RESP_NOTACCEPTABLE_406 = 406;
  /** 409 Conflict. */
  public static final int RESP_CONFLICT_409 = 409;
  /** 415 Not supported. */
  public static final int RESP_NOTSUPPORTED_415 = 415;
  /** 500 Internal Server Error. */
  public static final int RESP_INTERNALSERVERERROR_500 = 500;
  /** 50X Server Error. */
  public static final int RESP_SERVERERROR_501 = 501;

  /** Device Extention. */
  public static final String ADD_NODE = "add_node";
  /** Device Removal. */
  public static final String DEL_NODE = "del_node";
  /** Device Change. */
  public static final String UPDATE_NODE = "update_node";
  /** Inter-Cluster Link Generation. */
  public static final String CREATE_CLUSTERSLINK = "add_inter_cluster_link";
  /** Inter-Cluster Link Deletion. */
  public static final String DELETE_CLUSTERSLINK = "del_inter_cluster_link";

  /** L2VLAN IF Batch Generate/Change. */
  public static final String CREATE_UPDATE_L2VLANIF = "create_update_l2vlan_if";
  /** L2VLAN IF Batch Delete/Change. */
  public static final String DELETE_UPDATE_L2VLANIF = "delete_update_l2vlan_if";
  /** L3VLAN IF Batch Generation. */
  public static final String CREATE_L3VLANIF = "create_l3vlan_if";
  /** L3VLAN IF Batch Deletion. */
  public static final String DELETE_L3VLANIF = "delete_l3vlan_if";
  /** L2VLAN IF Batch Change. */
  public static final String UPDATE_L2VLANIF = "update_l2vlan_if";
  /** L3VLAN IF Batch Change. */
  public static final String UPDATE_L3VLANIF = "update_l3vlan_if";
  /** breakoutIF Addition. */
  public static final String REGISTER_BREAKOUTIF = "register_breakout_if";
  /** breakoutIF Deletion. */
  public static final String DELETE_BREAKOUTIF = "delete_breakout_if";

  /** Process Execution Request. */
  public static final int ALL_CP_OPERATION = 1;
  /** Leaf Device Extention/Removal. */
  public static final int LEAF_ADD_DEL = 2;
  /** Spine Device Extention/Removal. */
  public static final int SPINE_ADD_DEL = 3;
  /** MSF Controller Logical IF Control. */
  public static final int MSF_LOGICAL_IF_CONTROL = 4;
  /** Device Extention/Removal. */
  public static final int NODE_OPERATION = 5;

  /** The error code in case that the process doesn't reach to APL owing to errors in Jetty Jersey. */
  public static final String COMMON_ERROR_CODE = "990000";
}
