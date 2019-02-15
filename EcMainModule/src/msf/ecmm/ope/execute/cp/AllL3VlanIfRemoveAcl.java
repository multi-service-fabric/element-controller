/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.cp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsDeleteVlanIf;

/**
 * L3VLAN IF Batch Deletion（ACL）.
 */
public class AllL3VlanIfRemoveAcl extends AllL3VlanIfRemove {

  /**
   * Constructor.
   *
   * @param idt
   *          Input Data
   * @param ukm
   *          URI Key Information
   */
  public AllL3VlanIfRemoveAcl(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.AllL3VlanIfRemove);
  }

  @Override
  protected boolean checkExpand(Map<String, List<VlanIfs>> allVlanIfsMap,
      List<VlanIfsDeleteVlanIf> delVlans) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    boolean ret = AclUtilityCp.checkRemoveVlan(allVlanIfsMap, delVlans);
    logger.trace(CommonDefinitions.END);
    return ret;
  }
}
