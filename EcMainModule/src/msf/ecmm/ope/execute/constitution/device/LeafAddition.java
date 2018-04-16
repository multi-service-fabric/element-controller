/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.LeafAddDelete;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.AddNode;

/**
 * Leaf Device Extention.
 */
public class LeafAddition extends NodeAddition {

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public LeafAddition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.LeafAddition);
  }

  @Override
  protected boolean executeAddNode(String ecmainIpaddr, List<Nodes> nodesListDbMapper)
      throws EmctrlException, IllegalArgumentException {
    logger.trace(CommonDefinitions.START);

    LeafAddDelete leafAddEm = EmMapper.toLeafInfoNodeCreate((AddNode) getInData(), ecmainIpaddr, nodesListDbMapper);

    EmController emController = EmController.getInstance();
    AbstractMessage ret = emController.request(leafAddEm, false);

    logger.trace(CommonDefinitions.END);
    return ret.isResult();
  }

}
