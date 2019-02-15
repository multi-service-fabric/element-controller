/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import java.util.HashMap;
import java.util.List;

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
import msf.ecmm.ope.receiver.pojo.AddNode;

/**
 * B-Leaf Device Extention.
 */
public class BLeafAddition extends NodeAddition {

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public BLeafAddition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.BLeafAddition);
  }

  @Override
  protected boolean executeAddNode(String ecmainIpaddr, List<Nodes> nodesListDbMapper, int internallinkVlanId)
      throws EmctrlException, IllegalArgumentException {
    logger.trace(CommonDefinitions.START);

    AbstractMessage ret = null;

    try (DBAccessManager session = new DBAccessManager()) {
      Nodes pairNodes = null;
      if (((AddNode) getInData()).getUpdateNode() != null) {
        pairNodes = session.searchNodes(((AddNode) getInData()).getUpdateNode().getNodeId(), null);
      }

      BLeafAddDelete bleafAddEm = EmMapper.toBLeafInfoNodeCreate((AddNode) getInData(), ecmainIpaddr, nodesListDbMapper,
          pairNodes, internallinkVlanId);

      EmController emController = EmController.getInstance();
      ret = emController.request(bleafAddEm, false);

    } catch (DBAccessException dbae) {
      logger.debug("searchNodes error", dbae);
      throw new IllegalArgumentException();
    }

    logger.trace(CommonDefinitions.END);
    return ret.isResult();
  }
}
