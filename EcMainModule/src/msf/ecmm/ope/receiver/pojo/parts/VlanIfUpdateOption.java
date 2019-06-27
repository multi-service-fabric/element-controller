/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

import com.google.gson.annotations.SerializedName;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.CheckDataException;

/**
 * Optional Information for VLAN IF Change.
 */
public class VlanIfUpdateOption {

  /** Optional Information for L3VLAN IF. */
  @SerializedName("l3vlan_option")
  private L3VlanOption l3VlanOption;

  /** Optional Information for L2VLAN IF. */
  @SerializedName("l2vlan_option")
  private L2VlanOption l2VlanOption;

  /**
   * Getting optional information for L3VLAN IF.
   *
   * @return l3VlanOption
   */
  public L3VlanOption getL3VlanOption() {
    return l3VlanOption;
  }

  /**
   * Setting optional information for L3VLAN IF.
   *
   * @param l3VlanOption
   *          set l3VlanOption
   */
  public void setL3VlanOption(L3VlanOption l3VlanOption) {
    this.l3VlanOption = l3VlanOption;
  }

  /**
   * Getting optional information for L2VLAN IF.
   *
   * @return l2VlanOption
   */
  public L2VlanOption getL2VlanOption() {
    return l2VlanOption;
  }

  /**
   * Setting optional information for L2VLAN IF.
   *
   * @param l2VlanOption
   *          set l2VlanOption
   */
  public void setL2VlanOption(L2VlanOption l2VlanOption) {
    this.l2VlanOption = l2VlanOption;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "VlanIfUpdateOption [l3VlanOption=" + l3VlanOption + ", l2VlanOption=" + l2VlanOption + "]";
  }

  /**
   * Input Parameter Check.
   *
   * @param ope
   *          operation type
   * @throws CheckDataException
   *           input check error
   */
  public void check(OperationType ope) throws CheckDataException {
    if (l3VlanOption == null && l2VlanOption == null) {
      throw new CheckDataException();
    }
    if (l3VlanOption != null) {
      l3VlanOption.check(ope);
    }
    if (l2VlanOption != null) {
      l2VlanOption.check(ope);
    }
  }

}
