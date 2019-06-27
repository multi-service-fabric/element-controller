/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import java.util.ArrayList;
import java.util.List;

/**
 * QoS Configuration Feasibility Infomation Class.
 */
public class QosCapabilities {

  /** Inflow / Outflow amount Controll Feasibility. */
  private Boolean shaping = null;

  /** Remark Menu Configuration Feasibility. */
  private Boolean remark = null;

  /** Remark Menu List. */
  private List<String> remarkMenuList = new ArrayList<String>();

  /** Egress Queue Menu List. */
  private List<String> egressMenuList = new ArrayList<String>();

  /**
   * Getting Inflow / Outflow amount Controll Feasibility.
   *
   * @return shaping
   */
  public Boolean getShaping() {
    return shaping;
  }

  /**
   * Setting Inflow / Outflow amount Controll Feasibility.
   *
   * @param shaping
   *          setting shaping
   */
  public void setShaping(Boolean shaping) {
    this.shaping = shaping;
  }

  /**
   * Getting Remark Menu Configuration Feasibility.
   *
   * @return remark
   */
  public Boolean getRemark() {
    return remark;
  }

  /**
   * Setting Remark Menu Configuration Feasibility.
   *
   * @param remark
   *          setting remark
   */
  public void setRemark(Boolean remark) {
    this.remark = remark;
  }

  /**
   * Getting Remark Menu List.
   *
   * @return remarkMenuList
   */
  public List<String> getRemarkMenuList() {
    return remarkMenuList;
  }

  /**
   * Setting Remark Menu List.
   *
   * @param remarkMenuList
   *          setting remarkMenuList
   */
  public void setRemarkMenuList(List<String> remarkMenuList) {
    this.remarkMenuList = remarkMenuList;
  }

  /**
   * Getting Egress Queue Menu List.
   *
   * @return egressMenuList
   */
  public List<String> getEgressMenuList() {
    return egressMenuList;
  }

  /**
   * Setting Egress Queue Menu List.
   *
   * @param egressMenuList
   *          setting egressMenuList
   */
  public void setEgressMenuList(List<String> egressMenuList) {
    this.egressMenuList = egressMenuList;
  }

  /*
   *  Stringizing Instance
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "QosCapabilities [shaping=" + shaping + ", remark=" + remark + ", remarkMenuList=" + remarkMenuList
        + ", egressMenuList=" + egressMenuList + "]";
  }

}
