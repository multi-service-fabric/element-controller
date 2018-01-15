/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.BLeafAddDelete;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.DeleteNode;

/**
 * B-Leaf Device Removal.
 */
public class BLeafRemove extends NodeRemove {

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public BLeafRemove(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.BLeafRemove);
  }

  @Override
  protected boolean executeDeleteNode(Nodes targetNodeDb) throws EmctrlException, IllegalArgumentException {
    logger.trace(CommonDefinitions.START);

    AbstractMessage ret = null;

    try (DBAccessManager session = new DBAccessManager()) {
      Nodes pareNodes = null;
      if (((DeleteNode) getInData()).getUpdateNode() != null) {
        pareNodes = session.searchNodes(((DeleteNode) getInData()).getUpdateNode().getNodeId(), null);
      }

      BLeafAddDelete bleafDeleteEm = EmMapper.toBLeafInfoNodeDelete(targetNodeDb, (DeleteNode) getInData(), pareNodes);

      EmController emController = EmController.getInstance();
      ret = emController.request(bleafDeleteEm, false);

    } catch (DBAccessException dbae) {
      logger.debug("searchNodes error", dbae);
      throw new IllegalArgumentException();
    }

    logger.trace(CommonDefinitions.END);
    return ret.isResult();
  }
}
