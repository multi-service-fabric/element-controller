/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */
package msf.ecmm.config;

import static msf.ecmm.common.CommonDefinitions.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Extension function configuration class.
 */
public class ExpandOperation {

  /** Extension function operation ID. */
  private static final int OPERATION_TYPE_ID = 0;
  /** Extension function execution class. */
  private static final int EXECUTE_CLASS = 1;
  /** Operation priority type .*/
  private static final int PRIORITY = 2;
  /** Lock required/not required. */
  private static final int IS_LOCK = 3;
  /** No. of value. */
  private static final int VALUE_NUM = 4;

  /** Own class instance. */
  private static ExpandOperation me = new ExpandOperation();

  /** Property instance. */
  private static Properties properties = new Properties();

  /** Log output instance. */
  private final Logger logger = LogManager.getLogger(EC_LOGGER);

  /** Retention data. */
  private Map<String, ExpandOperationDetailInfo> data = new HashMap<>();

  /**
   * Extension function configuration class constructor.
   */
  private ExpandOperation() {
  }

  /**
   * Returning instance of the Extension function configuration class.
   *
   * @return Extension function configuration class instance
   */
  public static ExpandOperation getInstance() {
    return me;
  }

  /**
   * Reading property.
   *
   * @param filename
   *          Property file path
   * @throws Exception
   *           Reading configuration failed
   */
  public void read(String filename) throws Exception {

    logger.trace(CommonDefinitions.START);
    logger.debug("load config start : " + filename);
    properties = new Properties();
    data = new HashMap<>();
    try (FileInputStream fis = new FileInputStream(filename)) {
      properties.load(new InputStreamReader(fis, "utf8"));
    } catch (IOException ioe) {
      throw new Exception("config file not found", ioe);
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
   * Configuration check & Data retention.
   *
   * @return Configuraiton check result
   */
  private boolean validate() {
    logger.trace(CommonDefinitions.START);

    boolean ret = true;

    for (Object key : properties.keySet()) {
      String val = properties.getProperty((String) key);
      boolean checkResult = false;
      do {
        String[] values = val.split(",", -1);
        if (values.length != VALUE_NUM) {
          logger.debug("value num NG key=", (String)key);
          break;
        }

        ExpandOperationDetailInfo opeData = new ExpandOperationDetailInfo();

        String opeName = (String) key;
        if (!checkString(opeName)) {
          break;
        }
        opeData.setOperationTypeName(opeName);

        Integer tmpInt = checkInteger(values[OPERATION_TYPE_ID], 0, Integer.MAX_VALUE);
        if (tmpInt == null) {
          break;
        }
        opeData.setOperationTypeId(tmpInt);

        String tmpStr = values[EXECUTE_CLASS];
        if (!checkString(tmpStr)) {
          break;
        }
        opeData.setOperationExecuteClassName(tmpStr);

        tmpStr = values[PRIORITY];
        if (!checkString(tmpStr, EXPAND_CONF_PRIMARY, EXPAND_CONF_NORMAL, "")) {
          break;
        }
        if (tmpStr.isEmpty()) {
          tmpStr = EXPAND_CONF_NORMAL;
        }
        opeData.setOperationPriority(tmpStr);

        tmpStr = values[IS_LOCK];
        if (!checkString(tmpStr, EXPAND_CONF_TRUE, EXPAND_CONF_FALSE, "")) {
          break;
        }
        boolean isLock = false;
        if (tmpStr.equals(EXPAND_CONF_TRUE)) {
          isLock = true;
        } else if (tmpStr.equals(EXPAND_CONF_FALSE)) {
          isLock = false;
        } else {
          isLock = false;
        }
        opeData.setLock(isLock);

        data.put(opeName, opeData);
        checkResult = true;

      }
      while (false);
      if (!checkResult) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_508032, key, val));
        ret = false;
      }
    }

    logger.trace(CommonDefinitions.END);
    return ret;
  }


  /**
   * Getting the extended operation information.
   * @param expandOperationName Extended operation name
   * @return Extended operation information
   */
  public ExpandOperationDetailInfo get(String expandOperationName) {
    return data.get(expandOperationName);
  }

  /**
   * Getting list of extended operation name.
   * @return List of extended operation name
   */
  public Set<String> getExpandOperationList() {
    return data.keySet();
  }

  /**
	* Property value (character string) check.
   *   In case that acceptable character string is not specified, regard empty character string as an error
   *
   * @param data
   *          Check target
   * @param acceptableStrings
   *          Acceptable character strings
   * @return true：Check OK false：Check NG
   */
  private boolean checkString(String data, String ... acceptableStrings) {
    logger.trace(CommonDefinitions.START);

    boolean ret = false;
    if (data == null) {
      logger.debug("data null.");
    } else if (acceptableStrings.length == 0 && data.isEmpty()) {
      logger.debug("data empty. [str]");
    } else if (acceptableStrings.length != 0 && !Arrays.asList(acceptableStrings).contains(data)) {
      logger.debug("not acceptable string.");
    } else {
      ret = true;
    }
    return ret;
  }

  /**
	* Property value (numeric value) check.
   * @param data
   *          Check target
   * @param min
   *          Min. range value
   * @param max
   *          Max. range value
   * @return Check OK：Numeric value  Check NG：null
   */
  private Integer checkInteger(String data, int min, int max) {
    logger.trace(CommonDefinitions.START);

    Integer num = null;

    if (data == null || data.isEmpty()) {
      logger.debug("data empty. [int]");
    } else {
      try {
        num = Integer.parseInt(data);
        if ((num != Integer.MIN_VALUE && num < min) || (num != Integer.MAX_VALUE && num > max)) {
          logger.debug("range error.");
          num = null;
        }
      } catch (NumberFormatException nfe) {
        logger.debug("data error.", nfe);
      }
    }
    logger.trace(CommonDefinitions.END);
    return num;
  }
}
