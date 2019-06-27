/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.AddNode;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkInfo;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodesInterface;

/**
 * Device Extension（Extended Function：Priority Leaf/Spine）.
 */
public class NodeInfoRegistrationPriorityLeafSpine extends NodeInfoRegistration {

  /**
   * Constructor.
   *
   * @param idt
   *          Input Data
   * @param ukm
   *          URI Key Information
   */
  public NodeInfoRegistrationPriorityLeafSpine(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    AddNode addNode = (AddNode) getInData();
    checkResult = super.checkInData();
    if (checkResult) {
      try {
        for (InternalLinkInfo ifInfo : addNode.getCreateNode().getIfInfo().getInternalLinkIfs()) {
          Integer cost = ifInfo.getInternalLinkIf().getCost();
          if (cost == null) {
            throw new CheckDataException();
          }
        }
        for (OppositeNodesInterface oppo : addNode.getCreateNode().getOppositeNodes()) {
          Integer cost = oppo.getInternalLinkIf().getCost();
          if (cost == null) {
            throw new CheckDataException();
          }
        }

      } catch (CheckDataException cde) {
        logger.warn("check error :", cde);
        checkResult = false;
      }
    }
    logger.trace(CommonDefinitions.END);
    return checkResult;
  }
}
