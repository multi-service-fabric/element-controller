/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Remarkmenu Configuration Rest Class.
 */
public class Remark {

  /** Remarkmenu Configuration Feasibility. */
  private Boolean enable = null;

  /** Remarkmenu. */
  private List<String> menu = new ArrayList<String>();

  /** Remarkmenu default value. */
  @SerializedName("default")
  private String defaultValue = null;

  /**
   * Getting Remarkmenu Configuration Feasibility.
   *
   * @return enable
   */
  public Boolean getEnable() {
    return enable;
  }

  /**
   * Setting Remarkmenu Configuration Feasibility.
   *
   * @param enable
   *          setting enable
   */
  public void setEnable(Boolean enable) {
    this.enable = enable;
  }

  /**
   * Getting Remarkmenu.
   *
   * @return menu
   */
  public List<String> getMenu() {
    return menu;
  }

  /**
   * Setting Remarkmenu.
   *
   * @param menu
   *          setting menu
   */
  public void setMenu(List<String> menu) {
    this.menu = menu;
  }

  /**
   * Getting Remarkmenu default value.
   *
   * @return defaultValue
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * Setting Remarkmenu default value.
   *
   * @param defaultValue
   *         setting defaultValue
   */
  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  /*
   * Stringizing Instance
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Remark [enable=" + enable + ", menu=" + menu + ", defaultValue=" + defaultValue + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param operationType
   *            operation type
   * @throws CheckDataException
   *                input check error
   */
  public void check(OperationType operationType) throws CheckDataException {
    if (enable == null) {
      throw new CheckDataException();
    }

    if (menu != null && !menu.isEmpty()) {
      for (String elem : menu) {
        if (elem == null) {
          throw new CheckDataException();
        }
      }
    }
  }

}
