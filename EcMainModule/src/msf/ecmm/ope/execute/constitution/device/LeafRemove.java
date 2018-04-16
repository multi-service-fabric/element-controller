/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.LeafAddDelete;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;

/**
 * Leaf Device Removal.
 */
public class LeafRemove extends NodeRemove {
  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public LeafRemove(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.LeafRemove);
  }

  @Override
  protected boolean executeDeleteNode(Nodes targetNodeDb) throws EmctrlException {
    logger.trace(CommonDefinitions.START);

    String hostname = "";
    if (targetNodeDb.getEquipments().getRouter_type() == CommonDefinitions.ROUTER_TYPE_COREROUTER) {
      hostname = CommonDefinitions.COREROUTER_HOSTNAME;
    } else {
      hostname = targetNodeDb.getNode_name();
    }
    LeafAddDelete leafDeleteEm = EmMapper.toLeafInfoNodeDelete(hostname);

    EmController emController = EmController.getInstance();
    AbstractMessage ret = emController.request(leafDeleteEm, false);

    logger.trace(CommonDefinitions.END);
    return ret.isResult();
  }
}
