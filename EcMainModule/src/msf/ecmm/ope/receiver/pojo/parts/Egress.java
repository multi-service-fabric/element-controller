/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Egress Queue Menu Configuration Rest POJO Class.
 */
public class Egress {

  /** Egress Queue Menu. */
  private List<String> menu = new ArrayList<String>();

  /** Egress Queue Menu default value. */
  @SerializedName("default")
  private String defaultValue = null;

  /**
   * Getting Egress Queue Menu.
   *
   * @return menu
   */
  public List<String> getMenu() {
    return menu;
  }

  /**
   * Setting Egress Queue Menu.
   *
   * @param menu
   *          setting  menu
   */
  public void setMenu(List<String> menu) {
    this.menu = menu;
  }

  /**
   *  Getting Egress Queue Menu default value.
   *
   * @return defaultValue
   */
  public String getDefaultValue() {
    return defaultValue;
  }

  /**
   * Setting Egress Queue Menu default value.
   *
   * @param defaultValue
   *          setting defaultValue
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
    return "Egress [menu=" + menu + ", defaultValue=" + defaultValue + "]";
  }

   /**
   * Input Parameter Check.
   *
   * @param operationType
   *           operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType operationType) throws CheckDataException {

    if (menu == null) {
      throw new CheckDataException();
    } else if (menu.isEmpty()) {
      throw new CheckDataException();
    } else {
      for (String elem : menu) {
        if (elem == null) {
          throw new CheckDataException();
        }
      }
    }

    if (defaultValue == null) {
      throw new CheckDataException();
    }
  }
}
