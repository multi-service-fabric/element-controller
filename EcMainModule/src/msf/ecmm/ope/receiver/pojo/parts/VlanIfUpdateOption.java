/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
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
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "VlanIfUpdateOption [l3VlanOption=" + l3VlanOption + "]";
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
    if (l3VlanOption != null) {
      l3VlanOption.check(ope);
    }
  }

}
