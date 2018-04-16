/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo;

import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfUpdateOption;

/**
 * VLAN IF Change.
 */
public class UpdateVlanIf extends AbstractRestMessage {

  /** Optional Information for Change. */
  private VlanIfUpdateOption updateOption;

  /**
   * Constructor.
   */
  public UpdateVlanIf() {
    super();
  }

  /**
   * Getting optional information for change.
   *
   * @return optional information for change
   */
  public VlanIfUpdateOption getUpdateOption() {
    return updateOption;
  }

  /**
   * Setting optional information for change.
   *
   * @param updateOption
   *          optional information for change
   */
  public void setUpdateOption(VlanIfUpdateOption updateOption) {
    this.updateOption = updateOption;
  }

  /**
   * Stringizing Instance.
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "UpdateVlanIf [updateOption=" + updateOption + "]";
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
    if (updateOption == null) {
      throw new CheckDataException();
    } else {
      updateOption.check(ope);
    }
  }

}
