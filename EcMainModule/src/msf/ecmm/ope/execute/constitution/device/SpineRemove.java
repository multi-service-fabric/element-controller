/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.SpineAddDelete;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;

/**
 * Spine Device Removal.
 */
public class SpineRemove extends NodeRemove {

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public SpineRemove(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.SpineRemove);
  }

  @Override
  protected boolean executeDeleteNode(Nodes targetNodeDb) throws EmctrlException {
    logger.trace(CommonDefinitions.START);

    SpineAddDelete spineAddDeleteEm = EmMapper.toSpineInfoNodeDelete(targetNodeDb.getNode_name());

    EmController emController = EmController.getInstance();
    AbstractMessage ret = emController.request(spineAddDeleteEm, false);

    logger.trace(CommonDefinitions.END);
    return ret.isResult();
  }
}
