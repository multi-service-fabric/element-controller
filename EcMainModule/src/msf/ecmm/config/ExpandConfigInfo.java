/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.config;

/**
 * The class for indiviual configuration  in the extended function.
 */
public class ExpandConfigInfo {

  /** The name of the configuration. */
  private String configName;

  /** Type. */
  private Class<?> type;

  /** The flag which indicates it is mandatory. */
  private boolean mustFlag = true;

  /** The minimum value. */
  private int minNum = Integer.MIN_VALUE;

  /** The maximum value. */
  private int maxNum = Integer.MAX_VALUE;

  /** The minimum length in  string. */
  private int minLength = 1;

  /** The maximum length in string. */
  private int maxLength = 1024;

  /** The default value for the string. */
  private String defaultValueStr = "";

  /** The default value for the numeric. */
  private Integer defaultValueInt = 0;

  /**
   * The config name is acquird\ed.
   *
   * @return The config name.
   */
  public String getConfigName() {
    return configName;
  }

  /**
   * The config name is set.
   *
   * @param configName
   *          .The config name
   */
  public void setConfigName(String configName) {
    this.configName = configName;
  }

  /**
   * The type is acquired.
   *
   * @return Type.
   */
  public Class<?> getType() {
    return type;
  }

  /**
   * The type is set.
   *
   * @param type
   *          The type.
   */
  public void setType(Class<?> type) {
    this.type = type;
  }

  /**
   * The mandatory flag is acquired.
   *
   * @return The mandatory flag.
   */
  public boolean isMustFlag() {
    return mustFlag;
  }

  /**
   * The mandatory flag is acquired.
   *
   * @param mustFlag
   *          The mandatory flag.
   */
  public void setMustFlag(boolean mustFlag) {
    this.mustFlag = mustFlag;
  }

  /**
   * The minimum value is acquired.
   *
   * @return The minimum value.
   */
  public int getMinNum() {
    return minNum;
  }

  /**
   * The minimum value is set.
   *
   * @param minNum
   *          The minimum value.
   */
  public void setMinNum(int minNum) {
    this.minNum = minNum;
  }

  /**
   * The maximum value is acquired.
   *
   * @return The maximum value.
   */
  public int getMaxNum() {
    return maxNum;
  }

  /**
   * The maximum value is set.
   *
   * @param maxNum
   *          The maximum value.
   */
  public void setMaxNum(int maxNum) {
    this.maxNum = maxNum;
  }

  /**
   * The minimum length in string is aquired.
   *
   * @return The minimum length.
   */
  public int getMinLength() {
    return minLength;
  }

  /**
   * The minimum length in string is set.
   *
   * @param minLength
   *          The minimum length.
   */
  public void setMinLength(int minLength) {
    this.minLength = minLength;
  }

  /**
   * The minimum length in string is aquired.
   *
   * @return The minimum length.
   */
  public int getMaxLength() {
    return maxLength;
  }

  /**
   * The maximum length in string is set.
   *
   * @param maxLength
   *          The maximum length in string.
   */
  public void setMaxLength(int maxLength) {
    this.maxLength = maxLength;
  }

  /**
   * The default value(string) is aquired.
   *
   * @return The default value(string)
   */
  public String getDefaultValueStr() {
    return defaultValueStr;
  }

  /**
   * The default value(string) is set.	
   *
   * @param defaultValueStr
   *          The default value(string).
   */
  public void setDefaultValueStr(String defaultValueStr) {
    this.defaultValueStr = defaultValueStr;
  }

  /**
   * The default value(numeric) is aquired.
   *
   * @return The default value(numeric).
   */
  public Integer getDefaultValueInt() {
    return defaultValueInt;
  }

  /**
   * The default value(numeric) is set.
   *
   * @param defaultValueInt
   *          The default value(numeric).
   */
  public void setDefaultValueInt(Integer defaultValueInt) {
    this.defaultValueInt = defaultValueInt;
  }

  @Override
  public String toString() {
    return "ExpandConfigInfo [configName=" + configName + ", type=" + type + ", mustFlag=" + mustFlag + ", minNum="
        + minNum + ", maxNum=" + maxNum + ", minLength=" + minLength + ", maxLength=" + maxLength + ", defaultValueStr="
        + defaultValueStr + ", defaultValueInt=" + defaultValueInt + "]";
  }

}
