/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.DeleteNode;

/**
 * Spine Device Removal(ACL).
 */
public class SpineRemoveAcl extends LeafRemove {

  /**
   * Constructor.
   *
   * @param idt
   *          Input Data
   * @param ukm
   *          URI Key Information
   */
  public SpineRemoveAcl(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.SpineRemove);
  }

  @Override
  protected boolean checkExpand(Nodes nodes) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    DeleteNode deleteNodeRest = (DeleteNode) getInData();
    boolean ret = AclUtilityDevice.checkRemoveNode(deleteNodeRest, nodes);
    logger.trace(CommonDefinitions.END);
    return ret;
  }
}
