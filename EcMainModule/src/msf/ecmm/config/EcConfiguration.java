/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Configuration Management Class.
 */
public class EcConfiguration {

  /** REST waiting interface address. */
  public static final String REST_SERVER_ADDRESS = "rest_server_address";
  /** REST waiting port. */
  public static final String REST_SERVER_PORT = "rest_server_port";
  /** The number of retries when REST request failed. */
  public static final String REST_RETRY_NUM = "rest_retry_num";
  /** REST request timeout time.*/
  public static final String REST_TIMEOUT = "rest_timeout";
  /** FC connection address (for REST). */
  public static final String FC_ADDRESS = "fc_address";
  /** FC connection port number (for REST).*/
  public static final String FC_PORT = "fc_port";
  /** Service Start-up Status.*/
  public static final String SERVICE_STATUS = "service_status";
  /** Maintenance Blockage Status. */
  public static final String BLOCKADE_STATUS = "blockade_status";
  /** EM Connection Address (for NETCONF over ssh).*/
  public static final String EM_ADDRESS = "em_address";
  /** EM Connection Port Number (for NETCONF over ssh).*/
  public static final String EM_PORT = "em_port";
  /** EM Connection User Name (for NETCONF over ssh). */
  public static final String EM_USER = "em_user";
  /** EM Connection Password (for NETCONF over ssh).*/
  public static final String EM_PASSWORD = "em_password";
  /** EM Connection Timeout Value (for NETCONF over ssh).*/
  public static final String EM_TIMEOUT = "em_timeout";
  /** EC's Own Cluster ID. */
  public static final String CLUSTER_ID = "cluster_id";
  /** SNMP Request Timeout Time in Configuration Management Function. */
  public static final String DEVICE_SNMP_TIMEOUT = "device_snmp_timeout";
  /** No. of Retries for OSPF Neighbor Establishment ConfirmationOSPF. */
  public static final String OSPF_NEIGHBOR_RETRY_NUM = "ospf_neighbor_retry_num";
  /** Retry Interval for OSPF Neighbor Establishment Confirmation.*/
  public static final String OSPF_NEIGHBOR_RETRY_INTERVAL = "ospf_neighbor_retry_interval";
  /** I/F Status Adjustment Process Execution Cycle. */
  public static final String FAILURE_MIB_INTERVAL = "failure_mib_interval";
  /** SNMP Request Timeout Time in Failure Management Function.*/
  public static final String FAILURE_SNMP_TIMEOUT = "failure_snmp_timeout";
  /** Execution Cycle of MIB Collection from the Managed Device in Traffic Management Function. */
  public static final String TRAFFIC_MIB_INTERVAL = "traffic_mib_interval";
  /** Timeout Time for MIB Collection. */
  public static final String TRAFFIC_SNMP_TIMEOUT = "traffic_snmp_timeout";
  /** Timeout Value for Waiting EM Queue. */
  public static final String EM_QUEUE_TIMEOUT = "em_queue_timeout";
  /** DHCP Configuration File. */
  public static final String DEVICE_DHCP_CONFIG = "device_dhcp_config";
  /** Periodic Termination Process Timeout Time. */
  public static final String GATHER_MIB_STOP_TIMEOUT = "gather_mib_stop_timeout";
  /** Periodic Termination Process Status Confirmation Cycle. */
  public static final String GATHER_MIB_STOP_INTERVAL = "gather_mib_stop_interval";
  /** RSYSLOG Configuration File Name.*/
  public static final String DEVICE_RSYSLOG_CONFIG = "device_rsyslog_config";
  /** Timeout Value for Waiting Operation Queue. */
  public static final String OPERATION_QUEUE_TIMEOUT = "operation_queue_timeout";
  /** EC Management I/F Address. */
  public static final String DEVICE_EC_MANAGEMENT_IF = "device_ec_management_if";
  /** Max. No. of REST Process Threads. */
  public static final String REST_THREAD_MAX = "rest_thread_max";
  /** Script Installation Path. */
  public static final String SCRIPT_PATH = "script_path";
  /** Request Log Output Path. */
  public static final String REQUEST_LOG_PATH = "request_log_path";
  /** Request Log Retention Period (Days). */
  public static final String REQUEST_KEEP_PERIOD = "request_keep_period";
  /** No. of Retries for REST Waiting I/F bind Failure. */
  public static final String REST_SERVER_BIND_NG_RETRY_NUM = "rest_server_bind_ng_retry_num";
  /** ACT Server Physical IP Address. */
  public static final String SERVER_PHYSICAL_ADDRESS = "server_physical_address";
  /** No. of REST Requests Average Time. */
  public static final String REST_REQUEST_AVERAGE = "rest_request_average";
  /** EM Connection Address (for REST). */
  public static final String EM_REST_ADDRESS = "em_rest_address";
  /** EM Connection Port Number (for REST). */
  public static final String EM_REST_PORT = "em_rest_port";
  /** Log File Path. */
  public static final String LOG_FILE_PATH = "log_file_path";

  /** Own Class Instance. */
  private static EcConfiguration me = new EcConfiguration();

  /** Property Instance. */
  private static Properties properties = new Properties();

  /** Log Output Instance. */
  private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);


  /**
   * Configuration Management Functional Part Class Constructor.
   */
  private EcConfiguration() {
  }

  /**
   * Returning Instance of Configuration Management Functional Part Class.
   *
   * @return Configuration Management Functional Part Class Instance
   */
  public static EcConfiguration getInstance() {
    return me;
  }

  /**
   * Reading Properties.
   *
   * @param filename
   *          Property File Path
   * @throws Exception
   *           Configuration Read Failure
   */
  public void read(String filename) throws Exception {

    logger.trace(CommonDefinitions.START);
    logger.debug("load config start : " + filename);
    properties = new Properties();
    try (FileInputStream fis = new FileInputStream(filename)) {
      properties.load(new InputStreamReader(fis, "utf8"));
    } catch (IOException e) {
      throw new Exception("config file not found", e);
    }

    boolean ret = this.validate();
    if (ret != true) {
      logger.debug("load config fail");
      throw new Exception("illegal data in config file");
    }
    logger.debug("load config success");
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Checking configuration data values.
   *
   * @return property data check result
   */
  private boolean validate() {

    boolean ret = true;
    boolean chkFlg = false;

    HashMap<String, InnerConfigCheck> requiredParameterMap = new HashMap<String, InnerConfigCheck>();

    requiredParameterMap.put(REST_SERVER_ADDRESS, new InnerConfigCheck("0.0.0.0"));
    requiredParameterMap.put(REST_SERVER_PORT, new InnerConfigCheck("18080"));
    requiredParameterMap.put(REST_RETRY_NUM, new InnerConfigCheck("0"));
    requiredParameterMap.put(REST_TIMEOUT, new InnerConfigCheck());
    requiredParameterMap.put(FC_ADDRESS, new InnerConfigCheck());
    requiredParameterMap.put(FC_PORT, new InnerConfigCheck());
    requiredParameterMap.put(SERVICE_STATUS, new InnerConfigCheck("startready"));
    requiredParameterMap.put(BLOCKADE_STATUS, new InnerConfigCheck("inservice"));
    requiredParameterMap.put(EM_ADDRESS, new InnerConfigCheck());
    requiredParameterMap.put(EM_PORT, new InnerConfigCheck());
    requiredParameterMap.put(EM_USER, new InnerConfigCheck());
    requiredParameterMap.put(EM_PASSWORD, new InnerConfigCheck());
    requiredParameterMap.put(EM_TIMEOUT, new InnerConfigCheck());
    requiredParameterMap.put(CLUSTER_ID, new InnerConfigCheck());
    requiredParameterMap.put(DEVICE_SNMP_TIMEOUT, new InnerConfigCheck());
    requiredParameterMap.put(OSPF_NEIGHBOR_RETRY_NUM, new InnerConfigCheck());
    requiredParameterMap.put(OSPF_NEIGHBOR_RETRY_INTERVAL, new InnerConfigCheck());
    requiredParameterMap.put(FAILURE_MIB_INTERVAL, new InnerConfigCheck());
    requiredParameterMap.put(FAILURE_SNMP_TIMEOUT, new InnerConfigCheck());
    requiredParameterMap.put(TRAFFIC_MIB_INTERVAL, new InnerConfigCheck("1"));
    requiredParameterMap.put(TRAFFIC_SNMP_TIMEOUT, new InnerConfigCheck("30"));
    requiredParameterMap.put(EM_QUEUE_TIMEOUT, new InnerConfigCheck());
    requiredParameterMap.put(DEVICE_DHCP_CONFIG, new InnerConfigCheck());
    requiredParameterMap.put(GATHER_MIB_STOP_TIMEOUT, new InnerConfigCheck());
    requiredParameterMap.put(GATHER_MIB_STOP_INTERVAL, new InnerConfigCheck());
    requiredParameterMap.put(DEVICE_RSYSLOG_CONFIG, new InnerConfigCheck());
    requiredParameterMap.put(OPERATION_QUEUE_TIMEOUT, new InnerConfigCheck());
    requiredParameterMap.put(DEVICE_EC_MANAGEMENT_IF, new InnerConfigCheck());
    requiredParameterMap.put(REST_THREAD_MAX, new InnerConfigCheck());
    requiredParameterMap.put(SCRIPT_PATH, new InnerConfigCheck());
    requiredParameterMap.put(REQUEST_LOG_PATH, new InnerConfigCheck());
    requiredParameterMap.put(REQUEST_KEEP_PERIOD, new InnerConfigCheck());
    requiredParameterMap.put(REST_SERVER_BIND_NG_RETRY_NUM, new InnerConfigCheck());
    requiredParameterMap.put(SERVER_PHYSICAL_ADDRESS, new InnerConfigCheck());
    requiredParameterMap.put(REST_REQUEST_AVERAGE, new InnerConfigCheck());
    requiredParameterMap.put(EM_REST_ADDRESS, new InnerConfigCheck());
    requiredParameterMap.put(EM_REST_PORT, new InnerConfigCheck());
    requiredParameterMap.put(LOG_FILE_PATH, new InnerConfigCheck());

    for (Object key : properties.keySet()) {

      if (key.equals(REST_SERVER_ADDRESS)) {
        if (checkString((String) key, false)) {
          requiredParameterMap.get(REST_SERVER_ADDRESS).checkExec = true;
          requiredParameterMap.get(REST_SERVER_ADDRESS).checkRet = true;
        }
      }
      else if (key.equals(REST_SERVER_PORT)) {
        if (checkString((String) key, false)) {
          requiredParameterMap.get(REST_SERVER_PORT).checkExec = true;
          if (checkInteger((String) key, 0, 65535, false)) {
            requiredParameterMap.get(REST_SERVER_PORT).checkRet = true;
          }
        }
      }
      else if (key.equals(REST_RETRY_NUM)) {
        if (checkString((String) key, false)) {
          requiredParameterMap.get(REST_RETRY_NUM).checkExec = true;
          if (checkInteger((String) key, 0, Integer.MAX_VALUE, false)) {
            requiredParameterMap.get(REST_RETRY_NUM).checkRet = true;
          }
        }
      }
      else if (key.equals(REST_TIMEOUT)) {
        requiredParameterMap.get(REST_TIMEOUT).checkExec = true;
        if (checkInteger((String) key, 1, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(REST_TIMEOUT).checkRet = true;
        }
      }
      else if (key.equals(FC_ADDRESS)) {
        requiredParameterMap.get(FC_ADDRESS).checkExec = true;
        if (checkString((String) key, true)) {
          requiredParameterMap.get(FC_ADDRESS).checkRet = true;
        }
      }
      else if (key.equals(FC_PORT)) {
        requiredParameterMap.get(FC_PORT).checkExec = true;
        if (checkInteger((String) key, 0, 65535, true)) {
          requiredParameterMap.get(FC_PORT).checkRet = true;
        }
      }
      else if (key.equals(SERVICE_STATUS)) {
        if (checkString((String) key, false)) {
          requiredParameterMap.get(SERVICE_STATUS).checkExec = true;
          if (properties.getProperty((String) key).equals("startready")
              || properties.getProperty((String) key).equals("changeover")) {
            requiredParameterMap.get(SERVICE_STATUS).checkRet = true;
          }
        }
      }
      else if (key.equals(BLOCKADE_STATUS)) {
        if (checkString((String) key, false)) {
          requiredParameterMap.get(BLOCKADE_STATUS).checkExec = true;
          if (checkString((String) key, false)) {
            if (properties.getProperty((String) key).equals("busy")
                || properties.getProperty((String) key).equals("inservice")) {
              requiredParameterMap.get(BLOCKADE_STATUS).checkRet = true;
            }
          }
        }
      }
      else if (key.equals(EM_ADDRESS)) {
        requiredParameterMap.get(EM_ADDRESS).checkExec = true;
        if (checkString((String) key, true)) {
          requiredParameterMap.get(EM_ADDRESS).checkRet = true;
        }
      }
      else if (key.equals(EM_PORT)) {
        requiredParameterMap.get(EM_PORT).checkExec = true;
        if (checkInteger((String) key, 0, 65535, true)) {
          requiredParameterMap.get(EM_PORT).checkRet = true;
        }
      }
      else if (key.equals(EM_USER)) {
        requiredParameterMap.get(EM_USER).checkExec = true;
        if (checkString((String) key, true)) {
          requiredParameterMap.get(EM_USER).checkRet = true;
        }
      }
      else if (key.equals(EM_PASSWORD)) {
        requiredParameterMap.get(EM_PASSWORD).checkExec = true;
        if (checkString((String) key, true)) {
          requiredParameterMap.get(EM_PASSWORD).checkRet = true;
        }
      }
      else if (key.equals(EM_TIMEOUT)) {
        requiredParameterMap.get(EM_TIMEOUT).checkExec = true;
        if (checkInteger((String) key, 1, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(EM_TIMEOUT).checkRet = true;
        }
      }
      else if (key.equals(CLUSTER_ID)) {
        requiredParameterMap.get(CLUSTER_ID).checkExec = true;
        if (checkInteger((String) key, Integer.MIN_VALUE, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(CLUSTER_ID).checkRet = true;
        }
      }
      else if (key.equals(DEVICE_SNMP_TIMEOUT)) {
        requiredParameterMap.get(DEVICE_SNMP_TIMEOUT).checkExec = true;
        if (checkInteger((String) key, 1, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(DEVICE_SNMP_TIMEOUT).checkRet = true;
        }
      }
      else if (key.equals(OSPF_NEIGHBOR_RETRY_NUM)) {
        requiredParameterMap.get(OSPF_NEIGHBOR_RETRY_NUM).checkExec = true;
        if (checkInteger((String) key, 0, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(OSPF_NEIGHBOR_RETRY_NUM).checkRet = true;
        }
      }
      else if (key.equals(OSPF_NEIGHBOR_RETRY_INTERVAL)) {
        requiredParameterMap.get(OSPF_NEIGHBOR_RETRY_INTERVAL).checkExec = true;
        if (checkInteger((String) key, 0, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(OSPF_NEIGHBOR_RETRY_INTERVAL).checkRet = true;
        }
      }
      else if (key.equals(FAILURE_MIB_INTERVAL)) {
        requiredParameterMap.get(FAILURE_MIB_INTERVAL).checkExec = true;
        if (checkInteger((String) key, 1, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(FAILURE_MIB_INTERVAL).checkRet = true;
        }
      }
      else if (key.equals(FAILURE_SNMP_TIMEOUT)) {
        requiredParameterMap.get(FAILURE_SNMP_TIMEOUT).checkExec = true;
        if (checkInteger((String) key, 1, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(FAILURE_SNMP_TIMEOUT).checkRet = true;
        }
      }
      else if ((key.equals(TRAFFIC_MIB_INTERVAL) || key.equals(TRAFFIC_SNMP_TIMEOUT)) && chkFlg == false) {

        chkFlg = true;

        if (checkString((String) TRAFFIC_SNMP_TIMEOUT, false)) {
          requiredParameterMap.get(TRAFFIC_SNMP_TIMEOUT).checkExec = true;

          if (checkInteger(TRAFFIC_SNMP_TIMEOUT, 1, Integer.MAX_VALUE, false)) {
            requiredParameterMap.get(TRAFFIC_SNMP_TIMEOUT).checkRet = true;
          }
        }

        if (checkString((String) TRAFFIC_MIB_INTERVAL, false)) {
          requiredParameterMap.get(TRAFFIC_MIB_INTERVAL).checkExec = true;

          if (checkInteger(TRAFFIC_MIB_INTERVAL, 0, Integer.MAX_VALUE, false)) {
            requiredParameterMap.get(TRAFFIC_MIB_INTERVAL).checkRet = true;

            Integer trfMibIntval = this.get(Integer.class, TRAFFIC_MIB_INTERVAL);
            if (trfMibIntval.intValue() == 0) {
              requiredParameterMap.get(TRAFFIC_SNMP_TIMEOUT).checkExec = false;
              requiredParameterMap.get(TRAFFIC_SNMP_TIMEOUT).checkRet = false;
              requiredParameterMap.get(TRAFFIC_SNMP_TIMEOUT).defaultValue = "0";
            } else {
              requiredParameterMap.get(TRAFFIC_SNMP_TIMEOUT).defaultValue = String
                  .valueOf(((trfMibIntval.intValue() * 60) / 2));
            }
          }
        }
      }
      else if (key.equals(EM_QUEUE_TIMEOUT)) {
        requiredParameterMap.get(EM_QUEUE_TIMEOUT).checkExec = true;
        if (checkInteger((String) key, 1, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(EM_QUEUE_TIMEOUT).checkRet = true;
        }
      }
      else if (key.equals(DEVICE_DHCP_CONFIG)) {
        requiredParameterMap.get(DEVICE_DHCP_CONFIG).checkExec = true;
        if (checkString((String) key, true)) {
          requiredParameterMap.get(DEVICE_DHCP_CONFIG).checkRet = true;
        }
      }
      else if (key.equals(GATHER_MIB_STOP_TIMEOUT)) {
        requiredParameterMap.get(GATHER_MIB_STOP_TIMEOUT).checkExec = true;
        if (checkInteger((String) key, 1, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(GATHER_MIB_STOP_TIMEOUT).checkRet = true;
        }
      }
      else if (key.equals(GATHER_MIB_STOP_INTERVAL)) {
        requiredParameterMap.get(GATHER_MIB_STOP_INTERVAL).checkExec = true;
        if (checkInteger((String) key, 1, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(GATHER_MIB_STOP_INTERVAL).checkRet = true;
        }
      }
      else if (key.equals(DEVICE_RSYSLOG_CONFIG)) {
        requiredParameterMap.get(DEVICE_RSYSLOG_CONFIG).checkExec = true;
        if (checkString((String) key, true)) {
          requiredParameterMap.get(DEVICE_RSYSLOG_CONFIG).checkRet = true;
        }
      }
      else if (key.equals(OPERATION_QUEUE_TIMEOUT)) {
        requiredParameterMap.get(OPERATION_QUEUE_TIMEOUT).checkExec = true;
        if (checkInteger((String) key, 1, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(OPERATION_QUEUE_TIMEOUT).checkRet = true;
        }
      }
      else if (key.equals(DEVICE_EC_MANAGEMENT_IF)) {
        requiredParameterMap.get(DEVICE_EC_MANAGEMENT_IF).checkExec = true;
        if (checkString((String) key, true)) {
          requiredParameterMap.get(DEVICE_EC_MANAGEMENT_IF).checkRet = true;
        }
      }
      else if (key.equals(REST_THREAD_MAX)) {
        requiredParameterMap.get(REST_THREAD_MAX).checkExec = true;
        if (checkInteger((String) key, 1, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(REST_THREAD_MAX).checkRet = true;
        }
      }
      else if (key.equals(SCRIPT_PATH)) {
        requiredParameterMap.get(SCRIPT_PATH).checkExec = true;
        if (checkString((String) key, true)) {
          requiredParameterMap.get(SCRIPT_PATH).checkRet = true;
        }
      }
      else if (key.equals(REQUEST_LOG_PATH)) {
        requiredParameterMap.get(REQUEST_LOG_PATH).checkExec = true;
        if (checkString((String) key, true)) {
          requiredParameterMap.get(REQUEST_LOG_PATH).checkRet = true;
        }
      }
      else if (key.equals(REQUEST_KEEP_PERIOD)) {
        requiredParameterMap.get(REQUEST_KEEP_PERIOD).checkExec = true;
        if (checkInteger((String) key, 1, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(REQUEST_KEEP_PERIOD).checkRet = true;
        }
      }
      else if (key.equals(REST_SERVER_BIND_NG_RETRY_NUM)) {
        requiredParameterMap.get(REST_SERVER_BIND_NG_RETRY_NUM).checkExec = true;
        if (checkInteger((String) key, 0, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(REST_SERVER_BIND_NG_RETRY_NUM).checkRet = true;
        }
      }
      else if (key.equals(SERVER_PHYSICAL_ADDRESS)) {
        requiredParameterMap.get(SERVER_PHYSICAL_ADDRESS).checkExec = true;
        if (checkString((String) key, true)) {
          requiredParameterMap.get(SERVER_PHYSICAL_ADDRESS).checkRet = true;
        }
      }
      else if (key.equals(EM_REST_ADDRESS)) {
        requiredParameterMap.get(EM_REST_ADDRESS).checkExec = true;
        if (checkString((String) key, true)) {
          requiredParameterMap.get(EM_REST_ADDRESS).checkRet = true;
        }
      }
      else if (key.equals(EM_REST_PORT)) {
        requiredParameterMap.get(EM_REST_PORT).checkExec = true;
        if (checkInteger((String) key, 0, 65535, true)) {
          requiredParameterMap.get(EM_REST_PORT).checkRet = true;
        }
      }
      else if (key.equals(REST_REQUEST_AVERAGE)) {
        requiredParameterMap.get(REST_REQUEST_AVERAGE).checkExec = true;
        if (checkInteger((String) key, 0, Integer.MAX_VALUE, true)) {
          requiredParameterMap.get(REST_REQUEST_AVERAGE).checkRet = true;
        }
      }
      else if (key.equals(LOG_FILE_PATH)) {
        requiredParameterMap.get(LOG_FILE_PATH).checkExec = true;
        if (checkString((String) key, true)) {
          requiredParameterMap.get(LOG_FILE_PATH).checkRet = true;
        }
      }
    }

    for (Map.Entry<String, InnerConfigCheck> keyValue : requiredParameterMap.entrySet()) {
      if (keyValue.getValue().checkRet == false) {
        if (keyValue.getValue().requiredCheck == false) {
          if (keyValue.getValue().checkExec == false) {
            properties.setProperty(keyValue.getKey(), keyValue.getValue().defaultValue);
          }
          else {
            logger.error(LogFormatter.out.format(LogFormatter.MSG_508032, keyValue.getKey(),
                properties.getProperty(keyValue.getKey())));
            ret = false;
          }
        }
        else {
          if (keyValue.getValue().checkExec == false) {
            logger.error(LogFormatter.out.format(LogFormatter.MSG_508033, keyValue.getKey()));
          }
          ret = false;
        }
      }
    }
    return ret;
  }

  /**
   * Configuration Acquisition.
   *
   * @param cls
   *          class (Integer or String)
   * @param key
   *          property key value
   * @return property value
   */
  @SuppressWarnings("unchecked")
  public <T> T get(Class<T> cls, String key) {

    T value = null;
    String tmp = "";
    try {
      tmp = properties.getProperty(key);
      if (tmp != null) {

        if (cls == String.class) {
          value = (T) new String(tmp);
        } else if (cls == Integer.class) {
          value = (T) new Integer(Integer.parseInt(tmp));
        }
      }
    } catch (NumberFormatException e) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_508032, key, tmp), e);
    }
    return value;
  }

  /**
   * Property Value Check (String).
   *
   * @param key
   *          property key
   * @param msg
   *          necessity of error message
   * @return property value is string AND configuration exists
   */
  private boolean checkString(String key, boolean msg) {

    boolean ret = true;

    String val = properties.getProperty(key);
    if (val == null || val.isEmpty()) {
      if (msg) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_508033, key));
      }
      ret = false;
    }
    return ret;
  }

  /**
   * Property Value Check (Integer).
   *
   * @param key
   *          property key
   * @param min
   *          minimum range value
   * @param max
   *          maximum range value
   * @param msg
   *          necessity of error message
   * @return property value is string AND configuration exists
   */
  private boolean checkInteger(String key, int min, int max, boolean msg) {
    boolean ret = true;

    String val = properties.getProperty(key);
    if (val == null || val.isEmpty()) {
      if (msg) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_508033, key));
      }
      ret = false;

    } else {
      try {
        int num = Integer.parseInt(val);
        if ((num != Integer.MIN_VALUE && num < min) || (num != Integer.MAX_VALUE && num > max)) {
          if (msg) {
            logger.error(LogFormatter.out.format(LogFormatter.MSG_508032, key, val));
          }
          ret = false;
        }
      } catch (NumberFormatException | SecurityException | ClassCastException e) {
        if (msg) {
          logger.error(LogFormatter.out.format(LogFormatter.MSG_508032, key, val), e);
        }
        ret = false;
      }
    }
    return ret;
  }

  /**
   * Inner Class for Configuration Data Check.
   */
  protected class InnerConfigCheck {
    private Boolean requiredCheck = true;
    private Boolean checkExec = false;
    private Boolean checkRet = false;
    private String defaultValue = "";

    /**
     * Constructor (Mandatory Configuration).
     *
     * @param execCheck
     *          mandatory/optional check
     * @param checkRet
     *          check resul
     */
    protected InnerConfigCheck() {
      this.requiredCheck = true;
    }

    /**
     * Constructor (Optional Configuration).
     *
     * @param defaultValue
     *          Default Value
     */
    protected InnerConfigCheck(String defaultValue) {
      this.requiredCheck = false;
      this.defaultValue = defaultValue;
    }
  }
}
