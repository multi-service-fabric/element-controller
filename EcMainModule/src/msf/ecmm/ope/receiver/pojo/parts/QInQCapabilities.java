/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.receiver.pojo.parts;

/**
 * Propriety of Q-in-Q setting
 */
public class QInQCapabilities {

  /* Propriety of setting in each node. */
  private Boolean selectableByNode = null;

  /* Propriety of setting in each VLAN IF */	
  private Boolean selectableByVlanIf = null;

  /**
   * Getting propriety of setting in each node
   *
   * @return propriety of setting in each node
   */
  public Boolean getSelectableByNode() {
    return selectableByNode;
  }

  /**
   * Setting propriety of setting in each node
   *
   * @param selectableByNode
   *          propriety of setting in each node
   */
  public void setSelectableByNode(Boolean selectableByNode) {
    this.selectableByNode = selectableByNode;
  }

  /**
   * Getting propriety of setting in each VLAN IF
   *
   * @return propriety of setting in each VLAN IF
   */
  public Boolean getSelectableByVlanIf() {
    return selectableByVlanIf;
  }

  /**
   * Setting propriety of setting in each VLAN IF
   *
   * @param selectableByVlanIf
   *          propriety of setting in each VLAN IF
   */
  public void setSelectableByVlanIf(Boolean selectableByVlanIf) {
    this.selectableByVlanIf = selectableByVlanIf;
  }

  /**
   * Stringizing Instance
   *
   * @return instance string
   */
  @Override
  public String toString() {
    return "QInQCapabilities [selectableByNode=" + selectableByNode + ", selectableByVlanIf=" + selectableByVlanIf
        + "]";
  }

}
