/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.common.LogFormatter.*;
import static msf.ecmm.db.DBAccessException.*;
import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.DBAccessException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;

/**
 * Data Base Regulation Class.
 */
public class BaseDAO {

  /** Logger Instance. */
  protected final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** Session Instance. */
  protected Session session;

  /** Table Name (Model Information). */
  public static final int EQUIPMENTS = 1;
  /** Table Name (Model IF Information). */
  public static final int EQUIPMENT_IFS = 2;
  /** Table Name (Physical IF Naming Convention Information). */
  public static final int IF_NAME_RULES = 3;
  /** Table Name (Start-up Failure Message Information). */
  public static final int BOOT_ERROR_MESSAGES = 4;
  /** Table Name (BGP Option Information). */
  public static final int BGP_OPTIONS = 5;
  /** Table Name (static Route Information). */
  public static final int STATIC_ROUTE_OPTIONS = 6;
  /** Table Name (VRRP Option Information). */
  public static final int VRRP_OPTIONS = 7;
  /** Table Name (Device Information). */
  public static final int NODES = 8;
  /** Table Name (Device Start-up Notification Information). */
  public static final int NODES_STARTUP_NOTICE = 9;
  /** Table Name (Physical IF Information). */
  public static final int PHYSICAL_IFS = 10;
  /** Table Name (LAG Information). */
  public static final int LAG_IFS = 11;
  /** Table Name (BreakoutIF Information). */
  public static final int BREAKOUT_IFS = 12;
  /** Table Name (VLanIF Information). */
  public static final int VLAN_IFS = 13;
  /** Table Name (LAG Member Information). */
  public static final int LAG_MEMBERS = 14;
  /** Table Name (System Status Information). */
  public static final int SYSTEM_STATUS = 15;
  /** Table Name (Remark Menu Information). */
  public static final int REMARK_MENUS = 16;
  /** Table Name (Egress queue Menu Information). */
  public static final int EGRESS_QUEUE_MENUS = 17;

  /**
   * Error Process.
   *
   * @param code
   *          internal error code
   * @param tableName
   *          table name
   * @param e1
   *          content of error
   * @throws DBAccessException
   *           data base exception
   */
  public void errorMessage(int code, int tableName, Throwable e1) throws DBAccessException {

    int errorMessage = -1;

    switch (code) {

      case DOUBLE_REGISTRATION:

        switch (tableName) {
          case EQUIPMENTS:
          case EQUIPMENT_IFS:
          case IF_NAME_RULES:
          case BOOT_ERROR_MESSAGES:
          case EGRESS_QUEUE_MENUS:
          case REMARK_MENUS:
            errorMessage = MSG_509001;
            break;

          case NODES:
            errorMessage = MSG_509002;
            break;

          case PHYSICAL_IFS:
            errorMessage = MSG_509003;
            break;

          case LAG_IFS:
          case LAG_MEMBERS:
            errorMessage = MSG_509004;
            break;

          case NODES_STARTUP_NOTICE:
            errorMessage = MSG_509061;
            break;

          case SYSTEM_STATUS:
            errorMessage = MSG_509069;
            break;

          case BREAKOUT_IFS:
            errorMessage = MSG_509074;
            break;

          case VLAN_IFS:
          case STATIC_ROUTE_OPTIONS:
          case BGP_OPTIONS:
          case VRRP_OPTIONS:
            errorMessage = MSG_509075;
            break;

          default:
            errorMessage = MSG_509019;
            break;
        }

        break;

      case NO_UPDATE_TARGET:

        switch (tableName) {
          case PHYSICAL_IFS:
            errorMessage = MSG_509008;
            break;

          case SYSTEM_STATUS:
            errorMessage = MSG_509009;
            break;

          case NODES_STARTUP_NOTICE:
            errorMessage = MSG_509046;
            break;

          case BREAKOUT_IFS:
            errorMessage = MSG_509076;
            break;

          case LAG_IFS:
            errorMessage = MSG_509077;
            break;

          case VLAN_IFS:
            errorMessage = MSG_509078;
            break;

          case NODES:
            errorMessage = MSG_509093;
            break;

          default:
            errorMessage = MSG_509022;
            break;
        }
        break;

      case NO_DELETE_TARGET:

        switch (tableName) {
          case EQUIPMENTS:
          case EQUIPMENT_IFS:
          case IF_NAME_RULES:
          case BOOT_ERROR_MESSAGES:
          case EGRESS_QUEUE_MENUS:
          case REMARK_MENUS:
            errorMessage = MSG_509010;
            break;

          case NODES:
            errorMessage = MSG_509011;
            break;

          case PHYSICAL_IFS:
            errorMessage = MSG_509012;
            break;

          case LAG_IFS:
          case LAG_MEMBERS:
            errorMessage = MSG_509013;
            break;

          case NODES_STARTUP_NOTICE:
            errorMessage = MSG_509047;
            break;

          case BREAKOUT_IFS:
            errorMessage = MSG_509079;
            break;

          case VLAN_IFS:
          case STATIC_ROUTE_OPTIONS:
          case BGP_OPTIONS:
          case VRRP_OPTIONS:
            errorMessage = MSG_509080;
            break;

          default:
            errorMessage = MSG_509020;
            break;

        }

        break;

      case RELATIONSHIP_UNCONFORMITY:

        switch (tableName) {
          case NODES:
            errorMessage = MSG_509017;
            break;

          case EQUIPMENTS:
            errorMessage = MSG_509008;
            break;

          default:
            errorMessage = -1;
            break;
        }

        break;

      case SERCH_FAILURE:
        errorMessage = MSG_509021;
        break;

      case INSERT_FAILURE:
        errorMessage = MSG_509019;
        break;

      case UPDATE_FAILURE:
        errorMessage = MSG_509022;
        break;

      case DELETE_FAILURE:
        errorMessage = MSG_509020;
        break;

      case SYSTEM_FAILURE:
        errorMessage = MSG_509054;
        break;

      default:
        errorMessage = -1;
        break;

    }

    if (e1 != null) {
      throw new DBAccessException(code, errorMessage, e1);
    } else {
      throw new DBAccessException(code, errorMessage);
    }

  }
}
