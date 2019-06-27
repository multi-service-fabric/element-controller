/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;

/**
 * Base class for the indivisual configuration in the exended fuction.
 */
public class ExpandConfigurationBase {

  /** The instance for the log output. */
  protected static final MsfLogger logger = new MsfLogger();

  /** The propert instance. */
  protected Properties properties = new Properties();

  /** The name of the confuguration file. */
  protected String configFileName;

  /** The configuration information. */
  protected Map<String, ExpandConfigInfo> configCondition = new HashMap<>();

  /** The flag which indicating the read comletion. */
  protected boolean alreadyReadFlag = false;

  /**
   * Cunstructor of the base class for the indivisual configuration in the exended fuction.
   */
  protected ExpandConfigurationBase() {
  }

  /**
   * The configuration is aquired.
   *
   * @param cls
   *          Class(Integer or String)
   * @param key
   *          The property key value
   * @return  The property key value
   */
  @SuppressWarnings("unchecked")
  public <T> T get(Class<T> cls, String key) {
    logger.trace(CommonDefinitions.START);
    logger.debug("key=" + key);

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
    } catch (NumberFormatException nfe) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_508032, key, tmp), nfe);
    }
    logger.debug("value=" + value);
    logger.trace(CommonDefinitions.START);
    return value;
  }

  /**
   * The individual configuration in the extended function is read.
   *
   * @throws Exception
   *           The reading failed
   */
  public static void readExpandConfiguration() throws Exception {
    logger.trace(CommonDefinitions.START);
    ExpandOperation expandOperation = ExpandOperation.getInstance();
    for (String operationName : expandOperation.getExpandOperationList()) {
      String configClassName = expandOperation.get(operationName).getExpandConfigurationClassName();
      if (configClassName != null && !configClassName.isEmpty()) {
        initialize(configClassName);
      }
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * The extended configuration class is initialized (in reading).
   *
   * @param className
   *          The etended configuration class FQCN
   */
  private static void initialize(String className) {
    logger.trace(CommonDefinitions.START);
    logger.debug("className=" + className);

    try {
      Class<?> cls = Class.forName(className);
      Method method = cls.getMethod("getInstance");
      ExpandConfigurationBase config = (ExpandConfigurationBase) method.invoke(null);
      config.read();
    } catch (Throwable ex) {
      logger.error(LogFormatter.out.format(LogFormatter.MSG_508124, className));
      throw new IllegalArgumentException();
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * The property is read.
   *
   * @throws Exception
   *           The reading failed.
   */
  public void read() throws Exception {
    logger.trace(CommonDefinitions.START);
    logger.debug("load config start : " + configFileName);

    if (alreadyReadFlag) {
      logger.debug("already read.");
      return;
    }
    try (FileInputStream fis = new FileInputStream(configFileName)) {
      properties.load(new InputStreamReader(fis, "utf8"));
    } catch (IOException ioe) {
      throw new Exception("config file not found", ioe);
    }

    boolean ret = this.validate();
    if (ret != true) {
      logger.debug("load config fail");
      throw new Exception("illegal data in config file");
    }

    alreadyReadFlag = true;

    logger.debug("load config success");
    logger.trace(CommonDefinitions.END);
  }

  /**
   * The config is checked.
   *
   * @return Result of checking the config
   */
  private boolean validate() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    for (String key : configCondition.keySet()) {
      ExpandConfigInfo val = configCondition.get(key);
      if (properties.getProperty(key) == null || properties.getProperty(key).isEmpty()) {
        if (val.isMustFlag()) {
          logger.debug("not found must config setting.");
          checkResult = false;
        } else {
          String defaultValue = "";
          if (val.getType() == String.class) {
            defaultValue = val.getDefaultValueStr();
          } else if (val.getType() == Integer.class) {
            defaultValue = val.getDefaultValueInt().toString();
          }
          properties.setProperty(key, defaultValue);
          logger.debug("set default configuration. KEY=" + key + " DEFAULT=" + defaultValue);
        }
      }
    }

    for (Object key : properties.keySet()) {
      String val = properties.getProperty((String) key);
      do {
        ExpandConfigInfo condition = configCondition.get(key);
        if (condition.getType() == String.class) {
          int minLength = condition.getMinLength();
          int maxLength = condition.getMaxLength();
          if (!checkString(val, minLength, maxLength)) {
            checkResult = false;
          }
        } else if ((condition.getType() == Integer.class)) {
          int minNum = condition.getMinNum();
          int maxNum = condition.getMaxNum();
          Integer tmpInt = checkInteger(val, minNum, maxNum);
          if (tmpInt == null) {
            checkResult = false;
          }
        }
      } while (false);
      if (!checkResult) {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_508032, key, val));
      }
    }

    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

  /**
   * The property value(strings) is checked.
   * If the acceptable strings are not provided, the empty string is also regarded as an error.
   *
   * @param data
   *          The data to be checked.
   * @param minLength
   *          The minimum length of the strings
   * @param maxLength
   *          The maximum length of the strings
   * @param acceptableStrings
   *          The acceptable strings
   * @return  true：if the check result is OK, false：if the check result is NG.
   */
  private boolean checkString(String data, int minLength, int maxLength, String... acceptableStrings) {
    logger.trace(CommonDefinitions.START);

    boolean ret = false;
    if (data == null) {
      logger.debug("data null.");
    } else if (acceptableStrings.length == 0 && data.isEmpty()) {
      logger.debug("data empty. [str]");
    } else if (acceptableStrings.length != 0 && !Arrays.asList(acceptableStrings).contains(data)) {
      logger.debug("not acceptable string.");
    } else if (minLength > data.length() || data.length() > maxLength) {
      logger.debug("too long or too shot length.");
    } else {
      ret = true;
    }
    logger.debug("ret=" + ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  /**
   * The properrt value (numeric) is checked.
   *
   * @param data
   *          The data to be checked.
   * @param min
   *          The minimum range value
   * @param max
   *          The maximum range value
   * @return  The numeric value is returned  if the check result is OK.
   *          The null is returned if thecheck is NG.
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
    logger.debug("num=" + num);
    logger.trace(CommonDefinitions.END);
    return num;
  }
}
