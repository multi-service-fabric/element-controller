/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.common;

import java.util.HashMap;

/**
 * format for log output.
 */
public class LogFormatter {

  /** formatter. */
  public static LogFormatter out = new LogFormatter();

  /** The model information is successfully registered. */
  public static final int MSG_301001 = 301001;
  /** The data you tried to register had already been registered in the model information table and resulted in an error. */
  public static final int MSG_509001 = 509001;
  /** The data you tried to register had already been registered in the device information table and resulted in an error. */
  public static final int MSG_509002 = 509002;
  /** The data you tried to register had already been registered in the physical IF information table and resulted in an error. */
  public static final int MSG_509003 = 509003;
  /** The data you tried to register had already been registered in the LAG information table and resulted in an error. */
  public static final int MSG_509004 = 509004;
  /** The data you tried to register had already been registered in the internal link IF information table and resulted in an error. */
  public static final int MSG_509005 = 509005;
  /** The data you tried to register had already been registered in the VLANIF information table and resulted in an error. */
  public static final int MSG_509006 = 509006;
  /** The data you tried to register had already been registered in the VLANIF information table and resulted in an error. */
  public static final int MSG_509007 = 509007;
  /** The key information of data you tried to update did not exist in the physical IF information table and resulted in an error. */
  public static final int MSG_509008 = 509008;
  /** The key information of data you tried to update did not exist in the system status information table and resulted in an error. */
  public static final int MSG_509009 = 509009;
  /** The data you tried to delete did not exist in the model information table and resulted in an error. */
  public static final int MSG_509010 = 509010;
  /** The data you tried to delete did not exist in the device information table and resulted in an error. */
  public static final int MSG_509011 = 509011;
  /** The data you tried to delete did not exist in the physical IF information table and resulted in an error. */
  public static final int MSG_509012 = 509012;
  /** The data you tried to delete did not exist in the LAG information table and resulted in an error. */
  public static final int MSG_509013 = 509013;
  /** The data you tried to delete did not exist in the internal link IF information table and resulted in an error. */
  public static final int MSG_509014 = 509014;
  /** The data you tried to delete did not exist in the VLANIF information table and resulted in an error. */
  public static final int MSG_509015 = 509015;
  /** The data you tried to delete did not exist in the VLANIF information table and resulted in an error. */
  public static final int MSG_509016 = 509016;
  /** The reference source information of device information table has already existed and resulted in an error. */
  public static final int MSG_509017 = 509017;
  /** The reference source information of model information table has already existed and resulted in an error. */
  public static final int MSG_509018 = 509018;
  /** An unexpected registration failure has occurred and resulted in an error. * The location of %s will be replaced with an error message of HibernateException. */
  public static final int MSG_509019 = 509019;
  /** An unexpected deletion failure has occurred and resulted in an error. * The location of %s will be replaced with an error message of HibernateException. */
  public static final int MSG_509020 = 509020;
  /** An unexpected search failure has occurred and resulted in an error. * The location of %s will be replaced with an error message of HibernateException.*/
  public static final int MSG_509021 = 509021;
  /** An unexpected update failure has occurred and resulted in an error. * The location of %s will be replaced with an error message of HibernateException. */
  public static final int MSG_509022 = 509022;
  /** The connection to EM could not been established. */
  public static final int MSG_504023 = 504023;
  /** A request to EM turned out to be an error response. */
  public static final int MSG_504024 = 504024;
  /** Decoding a message to be sent to EM failed. */
  public static final int MSG_504025 = 504025;
  /** Encoding the message received from EM failed. */
  public static final int MSG_504026 = 504026;
  /** Sending massage of SNMP failed. */
  public static final int MSG_505027 = 505027;
  /** REST request to FC/EM failed. */
  public static final int MSG_513031 = 513031;
  /** Checking the contents of configuration turned out to be an error. * The location of %s will be replaced by the key of configured value and the value actually configured. */
  public static final int MSG_508032 = 508032;
  /** Checking the contnts of configuration detected required parameters are not specified. * The location of %s will be replace by the key of configured value. */
  public static final int MSG_508033 = 508033;
  /** Starting up the REST server failed. */
  public static final int MSG_501034 = 501034;
  /** REST request has been received. * The location of %S will be replaced by the request URI and body. */
  public static final int MSG_301035 = 301035;
  /** REST response has been sent. * The location of %s will be replaced by the URI, response code and response body. */
  public static final int MSG_301036 = 301036;
  /** REST error response has been sent. * The location of %s will be replaced by the URI, response code and response body. */
  public static final int MSG_501037 = 501037;
  /** Reading configuration file failed at the time of starting up EC. */
  public static final int MSG_503038 = 503038;
  /** Starting up EC failed due to the initialization failure of [%s]. */
  public static final int MSG_503039 = 503039;
  /** Preparation of operation execution failed. */
  public static final int MSG_403040 = 403040;
  /** Execution of operation failed due to [%s]. */
  public static final int MSG_403041 = 403041;
  /** Termination process of operation execution failed. */
  public static final int MSG_403042 = 403042;
  /** In case the periodic execution of IF status adjustment failed. */
  public static final int MSG_407043 = 407043;
  /** In case the periodic execution of traffic information collection failed. */
  public static final int MSG_407044 = 407044;
  /** The operation [%s] has been judged as no in the feasibility jusdgment. */
  public static final int MSG_402045 = 402045;
  /** The key information of data you tried to update did not exist in the device start-up notification information table and resulted in an error. */
  public static final int MSG_509046 = 509046;
  /** The key information of data you tried to delete did not exist in the device start-up notification information table and resulted in an error. */
  public static final int MSG_509047 = 509047;
  /** An error occurred while in the course of device start-up notification which had not been sent. */
  public static final int MSG_402048 = 402048;
  /** An error occurred due to an unexpected session failure. */
  public static final int MSG_509049 = 509049;
  /** The start up of EC has been started. */
  public static final int MSG_303050 = 303050;
  /** Traffic data collection from a device failed. The device's node ID is [%s] and model ID is [%s]. */
  public static final int MSG_407051 = 407051;
  /** Duplicated IF status adjustment execution management is started. */
  public static final int MSG_407052 = 407052;
  /** IF status adjustment has been started. */
  public static final int MSG_307053 = 307053;
  /** An error occurred due to an error in the registration information of system status information. */
  public static final int MSG_509054 = 509054;
  /** The IF status adjustment has been running already. */
  public static final int MSG_402055 = 402055;
  /** The operarion management function has been started already. */
  public static final int MSG_402056 = 402056;
  /** The traffic information collection management function has been started already. */
  public static final int MSG_407057 = 407057;
  /** Traffic information collection is starting. */
  public static final int MSG_307058 = 307058;
  /** Device information could not been acquired from the device: node ID=[%s], node type=[%s], IFID=[%s]. */
  public static final int MSG_407059 = 407059;
  /** Device information could not been acquired from the device: CPID=[%s], SliceID=[%s] */
  public static final int MSG_407060 = 407060;
  /** The data you tried to register had already been registered in the device start-up notification information table and resulted in an error. */
  public static final int MSG_509061 = 509061;
  /** Starting up the periodic execution scheduler of IF status adjustment failed. */
  public static final int MSG_507062 = 507062;
  /** Starting up the periodic execution scheduler of traffic information collection failed. */
  public static final int MSG_507063 = 507063;
  /** Terminating the periodic execution scheduler of IF status adjustment failed. */
  public static final int MSG_407064 = 407064;
  /** Terminating the periodic execution scheduler of traffic information collection failed. */
  public static final int MSG_407065 = 407065;
  /** The last traffic information collection has not completed yet. */
  public static final int MSG_407066 = 407066;
  /** Starting up EC has been completed. */
  public static final int MSG_303067 = 303067;
  /** Updating EC status while in the course of starting up EC failed. */
  public static final int MSG_503068 = 503068;
  /** The data you tried to register had already been registered in the system status information table and resulted in an error. */
  public static final int MSG_509069 = 509069;
  /** An unexpected DB commit exception has occurred. */
  public static final int MSG_509070 = 509070;
  /** EC process has been terminated. */
  public static final int MSG_303071 = 303071;
  /** Duplicated EC process termination operation has been started. */
  public static final int MSG_402072 = 402072;
  /** A warning has been output because the opposing device information to be deleted/changed doesn't exist. */
  public static final int MSG_409073 = 409073;
  /** The data you tried to register had already been registered in the breakout IF information table and resulted in an error. */
  public static final int MSG_509074 = 509074;
  /** The data you tried to register had already been registered in the VLAN IF information table and resulted in an error. */
  public static final int MSG_509075 = 509075;
  /** The key information of data you tried to update did not exist in the breakout IF information table and resulted in an error. */
  public static final int MSG_509076 = 509076;
  /** The key information of data you tried to delete did not exist in the LAG IF information table and resulted in an error. */
  public static final int MSG_509077 = 509077;
  /** The key information of data you tried to delete did not exist in the VLAN IF information table and resulted in an error. */
  public static final int MSG_509078 = 509078;
  /** The data you tried to delete did not exist in the breakout IF information table and resulted in an error. */
  public static final int MSG_509079 = 509079;
  /** The data you tried to delete did not exist in the VLAN IF information table and resulted in an error. */
  public static final int MSG_509080 = 509080;
  /** REST request to EM failed. * The location of %s will be replaced by a specific error message (such as Exception error message). */
  public static final int MSG_504056 = 504056;
  /** Aggregation of REST transmission information failed. */
  public static final int MSG_502057 = 502057;
  /** Log information acquisition matched with the specified condition didn't exist. * The location of %s will be replaced by the request URI and body. */
  public static final int MSG_403058 = 403058;
  /** A node failure has occurred. * The location of %s will be replaced with the controversial device information. */
  public static final int MSG_503059 = 503059;
  /** Execution process of Linux command failed. */
  public static final int MSG_503060 = 503060;
  /** The processing of expanded information rollback at the time of expantion failure failed: node ID=[%s]. */
  public static final int MSG_402086 = 402086;
  /** Failed to record service state at EC startup. */
  public static final int MSG_503090 = 503090;
  /** Successful system switch notification (start / completion) * Start or end is entered .start indicates start, end indicates completion. */
  public static final int MSG_303091 = 303091;
  /** Failed to system switch notification (start / completion) * Start or end is entered .start indicates start, end indicates completion. */
  public static final int MSG_503092 = 503092;
  /** An error occurred because the key information of the data to be updated does not exist in "nodes" table. */
  public static final int MSG_509093 = 509093;
  /** Failed to generate operation execution class of expansion function. */
  public static final int MSG_503102 = 503102;
  /** Failed to collect the traffic data from device by using extension MIB.Target device is Node ID=[%s] Model ID=[%s]. */
  public static final int MSG_407094 = 407094;
  /** Failed to collect the traffic data from the device by using traffic information collection shell script.Target device is Node ID=[%s] Model ID=[%s]. */
  public static final int MSG_407095 = 407095;
  /** Traffic information collection shell script execution error. Model information table, the value of li_exec_path =[%s]. */
  public static final int MSG_407096 = 407096;
  /** Traffic information collection shell script has failed in SSH connection.Connection destination IP address=[%s]. */
  public static final int MSG_407097 = 407097;
  /** The data you tried to register had already been registered in the dummy VLANIF information table and resulted in an error. */
  public static final int MSG_509081 = 509081;
  /** The data you tried to register had already been registered in the IRB instance information table and resulted in an error. */
  public static final int MSG_509082 = 509082;
  /** The data you tried to register had already been registered in the ACL set information table and resulted in an error. */
  public static final int MSG_509083 = 509083;
  /** The data you tried to register had already been registered in the ACL configuration details information table and resulted in an error. */
  public static final int MSG_509084 = 509084;
  /** The key information you want to update does not exist in the dummy VLANIF information table, and resulted in an error. */
  public static final int MSG_509085 = 509085;
  /** The key information you want to update does not exist in the IRB instance information table, and resulted in an error. */
  public static final int MSG_509086 = 509086;
  /** The data you want to delete does not exist in dummy VLANIF information table, and resulted in an error. */
  public static final int MSG_509087 = 509087;
  /** The data you want to delete does not exist in the IRB instance information table, and resulted in an error. */
  public static final int MSG_509088 = 509088;
  /** The data you want to delete does not exist in the ACL set information table, and resulted in an error. */
  public static final int MSG_509089 = 509089;
  /** The data you want to delete does not exist in the ACL configuration details information, and resulted in an error. */
  public static final int MSG_509090 = 509090;

  /** Log Message Map. */
  private static HashMap<Integer, String> dictionary = new HashMap<Integer, String>() {
    {
      put(MSG_301001, "Equipments register complete");
      put(MSG_509001, "Register error:duplicate registration[equipments]");
      put(MSG_509002, "Register error:duplicate registration[nodes]");
      put(MSG_509003, "Register error:duplicate registration[physical_ifs]");
      put(MSG_509004, "Register error:duplicate registration[lag_ifs]");
      put(MSG_509005, "Register error:duplicate registration[internal_link_ifs]");
      put(MSG_509006, "Register error:duplicate registration[l2_cps]");
      put(MSG_509007, "Register error:duplicate registration[l3_cps]");
      put(MSG_509008, "Update error:not found update[physical_ifs]");
      put(MSG_509009, "Update error:not found update[system_status]");
      put(MSG_509010, "Delete error:not found delete[equipments]");
      put(MSG_509011, "Delete error:not found delete[nodes]");
      put(MSG_509012, "Delete error:not found delete[physical_ifs]");
      put(MSG_509013, "Delete error:not found delete[lag_ifs]");
      put(MSG_509014, "Delete error:not found delete[internal_link_ifs]");
      put(MSG_509015, "Delete error:not found delete[l2_cps]");
      put(MSG_509016, "Delete error:not found delete[l3_cps]");
      put(MSG_509017, "Delete error:relation illegal[nodes]");
      put(MSG_509018, "Delete error:relation illegal[equipments]");
      put(MSG_509019, "DB Register error:[%s]");
      put(MSG_509020, "DB Delete error:[%s]");
      put(MSG_509021, "DB Search error:[%s]");
      put(MSG_509022, "DB Update error:[%s]");
      put(MSG_504023, "EM Connection error:[%s]");
      put(MSG_504024, "EM Send error:[%s]");
      put(MSG_504025, "EM Decode error:[%s]");
      put(MSG_504026, "EM Encode error:[%s]");
      put(MSG_505027, "SNMP send error:[%s]");
      put(MSG_513031, "FC/EM Request REST massege error [%s]");
      put(MSG_508032, "Config check error [%s = %s]");
      put(MSG_508033, "Not found required parameter error [%s]");
      put(MSG_501034, "REST Server start error");
      put(MSG_301035, "Receive REST request uri:[%s] data:[%s]");
      put(MSG_301036, "Send REST response uri:[%s] responsecode:[%s] data:[%s]");
      put(MSG_501037, "Send REST error response uri:[%s] responsecode:[%s]  data:[%s]");
      put(MSG_503038, "Failed reading config file");
      put(MSG_503039, "Failed starting process : [%s]");
      put(MSG_403040, "Operation preparing failed.");
      put(MSG_403041, "Operation failed. : [%s]");
      put(MSG_403042, "Oparation closing failed.");
      put(MSG_407043, "Interface Integrity Validation failed.");
      put(MSG_407044, "Traffic Data Gathering failed.");
      put(MSG_402045, "Operation [%s] can not execute.");
      put(MSG_509046, "Update error:not found update[NodesStartupNotification]");
      put(MSG_509047, "Delete error:not found delete[NodesStartupNotification]");
      put(MSG_402048, "Error occured in sending unsent node state notification. : [%s]");
      put(MSG_509049, "DB Session error");
      put(MSG_303050, "Starting EC main module.");
      put(MSG_407051, "Fail getting traffic data : nodeId= [%s], equipmentTypeId= [%s]");
      put(MSG_407052, "InterfaceIntegrityValidationManager is already started.");
      put(MSG_307053, "Start IF state integrate.");
      put(MSG_509054, "SystemStatus Data error");
      put(MSG_402055, "IF integrity is already started.");
      put(MSG_402056, "OperationControlManager is already started.");
      put(MSG_407057, "TrafficDataGatheringManager is already started");
      put(MSG_307058, "Gathering traffic data start.");
      put(MSG_407059, "IF state from device didn't get.:node_id=[%s], node_type=[%s], if_id=[%s]");
      put(MSG_407060, "IF state from device didn't get.:cp_id=[%s], slice_id=[%s]");
      put(MSG_509061, "Register error:duplicate registration[nodes_startup_notification]");
      put(MSG_507062, "Integrity execute scheduler start error.");
      put(MSG_507063, "Traffic data gathering scheduler start error.");
      put(MSG_407064, "Integrity execute scheduler stop error.");
      put(MSG_407065, "Traffic data gathering scheduler stop error.");
      put(MSG_407066, "Present traffic data gathering has not finished.");
      put(MSG_303067, "Booting EC main module has finished.");
      put(MSG_503068, "EC main module state update error in starting EC main module.");
      put(MSG_509069, "Register error:duplicate registration[system_status]");
      put(MSG_509070, "DB commit error.");
      put(MSG_303071, "EC main module is stop.");
      put(MSG_402072, "EC main module has been stopping.");
      put(MSG_409073, "DB warn : OppositeNodes not found");
      put(MSG_509074, "Register error：duplicate registration[breakout_ifs]");
      put(MSG_509075, "Register error：duplicate registration[vlan_ifs]");
      put(MSG_509076, "Update error：not found update[breakout_ifs]");
      put(MSG_509077, "Update error：not found update[lag_ifs]");
      put(MSG_509078, "Update error：not found update[vlan_ifs]");
      put(MSG_509079, "Delete error：not found delete[breakout_ifs]");
      put(MSG_509080, "Delete error：not found delete[vlan_ifs]");
      put(MSG_504056, "EM Request REST massege error [%s]");
      put(MSG_502057, "REST Count error");
      put(MSG_403058, "Not found required log error [%s]");
      put(MSG_503059, "Node error:[%s]");
      put(MSG_503060, "Controller status acquisition error");
      put(MSG_402086, "Rollback added node info error : nodeId=[%s]");
      put(MSG_503090, "EC initial service status has not recorded");
      put(MSG_303091, "Complete to system switch notification:[%s]");
      put(MSG_503092, "Failed to system switch notification:[%s]");
      put(MSG_509093, "Update error：not found update[Nodes]");
      put(MSG_503102, "Unknown expandOperationClass:[%s]");
      put(MSG_407094, "Fail getting traffic data by extend MIB : nodeId= [%s], equipmentTypeId= [%s]");
      put(MSG_407095, "Fail getting traffic data with CLI script : nodeId= [%s], equipmentTypeId= [%s]");
      put(MSG_407096, "Script execution failure : Path= [%s]");
      put(MSG_407097, "SSH connection error : IP= [%s]");
      put(MSG_509081, "Register error:duplicate registration[dummy_vlan_ifs_info]");
      put(MSG_509082, "Register error:duplicate registration[irb_instance_info]");
      put(MSG_509083, "Register error:duplicate registration[acl_info]");
      put(MSG_509084, "Register error:duplicate registration[acl_detail_info]");
      put(MSG_509085, "Update error：not found update[dummy_vlan_ifs_info]");
      put(MSG_509086, "Update error：not found update[irb_instance_info]");
      put(MSG_509087, "Delete error：not found delete[dummy_vlan_ifs_info]");
      put(MSG_509088, "Delete error：not found delete[irb_instance_info]");
      put(MSG_509089, "Delete error：not found delete[acl_info]");
      put(MSG_509090, "Delete error：not found delete[acl_detail_info]");

    }
  };

  /**
   * It formats the output.
   *
   * @param msgNo
   *          message number
   * @param args
   *          arguments
   * @return formatted text string
   */
  public String format(Integer msgNo, Object... args) {

    return String.format("[" + msgNo + "] " + dictionary.get(msgNo), args);

  }

}
