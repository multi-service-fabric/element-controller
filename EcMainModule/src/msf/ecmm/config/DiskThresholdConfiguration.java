/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.CommonUtil;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;

/**
 * The class of the configuration for the disk threshold.
 */
public class DiskThresholdConfiguration {

  /** Logger. */
  private static final MsfLogger logger = new MsfLogger();

  /** The intance of my class. */
  private static DiskThresholdConfiguration me = new DiskThresholdConfiguration();

  /** The type is  Act. **/
  private static final int TYPE_ACT = 0;
  /** The type is Sby. **/
  private static final int TYPE_SBY = 1;
  /** The Act configuration file. */
  private static final String ACT_CONF = "../conf/act_disk_threshold.conf";
  /** The Sby configuration file. */
  private static final String SBY_CONF = "../conf/sby_disk_threshold.conf";

  /** The Act config hash. **/
  private Map<String, Integer> actData = new HashMap<>();
  /** The Sby config hash. **/
  private Map<String, Integer> sbyData = new HashMap<>();

  /** The flag which shows the Act configuration file has been read. */
  private boolean actAlreadyReadFlag = false;
  /** The flag which shows the Sby configuration file has been read. */
  private boolean sbyAlreadyReadFlag = false;

  /**
   * The disk threshold in ACT  is acquired.
   *
   * @throws Exception
   *           The reading of the disk threshold failed.
   */
  public void read() throws Exception {
    logger.trace(CommonDefinitions.START);
    try {
      readConfig(TYPE_ACT);
      if (CommonUtil.isFile(SBY_CONF)) {
        readConfig(TYPE_SBY);
      }
    } catch (Exception ex) {
      throw new Exception("config file not found", ex);
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * The Property is read.
   *
   * @param type
   *          The config type
   * @throws Exception
   *          The reading of the config failed.
   */
  public void readConfig(int type) throws Exception {
    logger.trace(CommonDefinitions.START);

    String fileName;
    Map<String, Integer> confMap = null;
    if (type == TYPE_ACT) {
      if (actAlreadyReadFlag) {
        logger.debug("act already read.");
        return;
      }
      fileName = ACT_CONF;
      confMap = actData;
    } else {
      if (sbyAlreadyReadFlag) {
        logger.debug("sby already read.");
        return;
      }
      fileName = SBY_CONF;
      confMap = sbyData;
    }

    /** The Property instance. */
    Properties properties = new Properties();
    try (FileInputStream fis = new FileInputStream(fileName)) {
      properties.load(new InputStreamReader(fis, "utf8"));
    } catch (IOException ioe) {
      throw new Exception("config file not found", ioe);
    }

    for (Object key : properties.keySet()) {
      String val = properties.getProperty((String) key);
      Integer threshold = checkInteger(val);
      if (null != threshold) {
        confMap.put((String) key, threshold);
      } else {
        logger.error(LogFormatter.out.format(LogFormatter.MSG_508032, (String) key, val));
        throw new Exception("parameter is not numeric value");
      }
    }

    if (type == TYPE_ACT) {
      actAlreadyReadFlag = true;
      logger.debug("load act config success");
    } else {
      sbyAlreadyReadFlag = true;
      logger.debug("load sby config success");
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * The property values(numeric) are checked.
   *
   * @param data
   *          The values to bechecked
   * @return The check result (OK：numric value NG：null)
   */
  private Integer checkInteger(String data) {
    logger.trace(CommonDefinitions.START);

    Integer num = null;

    if (data == null || data.isEmpty()) {
      logger.debug("data empty. [int]");
    } else {
      try {
        num = Integer.parseInt(data);
        if ((num <= Integer.MIN_VALUE) || (num >= Integer.MAX_VALUE)) {
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

  /**
   * The disk threshold is acquired.
   *
   * @param type
   *          The paramer indicating  the ACT or the SBY
   *
   * @return The disk threshold map.
   */
  public Map<String, Integer> get(int type) {
    if (type == TYPE_ACT) {
      return new HashMap<String, Integer>(actData);
    } else {
      return new HashMap<String, Integer>(sbyData);
    }
  }

  /**
   * The intastance of my class is returned.
   *
   * @return The instance of the ACT disk configuration  class.
   */
  public static DiskThresholdConfiguration getInstance() {
    return me;
  }

}
