/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
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
import msf.ecmm.emctrl.pojo.parts.InternalInterface;
import msf.ecmm.emctrl.pojo.parts.XmlIntegerElement;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.AddNode;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkInfo;

/**
 * Leaf Device Extension （Extension Function: Priority Leaf/Spine）.
 */
public class LeafAdditionPriorityLeafSpine extends LeafAddition {

  /**
   * Constructor.
   *
   * @param idt
   *          Input Data
   * @param ukm
   *          URI Key Information
   */
  public LeafAdditionPriorityLeafSpine(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
  }

  @Override
  protected boolean executeAddNode(String ecmainIpaddr, List<Nodes> nodesListDbMapper, int internallinkVlanId)
      throws EmctrlException, IllegalArgumentException {
    logger.trace(CommonDefinitions.START);

    AddNode addNode = (AddNode) getInData();

    LeafAddDelete leafAddEm = EmMapper.toLeafInfoNodeCreate(addNode, ecmainIpaddr, nodesListDbMapper,
        internallinkVlanId);

    Nodes nodes = PriorityLeafSpineUtility.getNode(nodesListDbMapper, addNode.getCreateNode().getNodeId());
    for (InternalLinkInfo ifInfo : addNode.getCreateNode().getIfInfo().getInternalLinkIfs()) {
      String ifType = ifInfo.getInternalLinkIf().getIfType();
      String ifId = ifInfo.getInternalLinkIf().getIfId();
      String ifName = EmMapper.getIfName(ifType, ifId, nodes); 
      int cost = ifInfo.getInternalLinkIf().getCost();
      setCost(leafAddEm, ifName, cost);
    }

    EmController emController = EmController.getInstance();
    AbstractMessage ret = emController.request(leafAddEm, false);

    logger.trace(CommonDefinitions.END);
    return ret.isResult();
  }

  /**
   * Cost Setting（Current Device）.
   * @param leafAddEm EM-POJO
   * @param ifName IF Name
   * @param cost Cost
   */
  private void setCost(LeafAddDelete leafAddEm, String ifName, int cost) {
    logger.trace(CommonDefinitions.START);
    for (InternalInterface ii : leafAddEm.getDevice().getInternalLagList()) {
      if (ii.getName().equals(ifName)) {
        XmlIntegerElement costObj = new XmlIntegerElement();
        costObj.setValue(cost);
        ii.setCost(costObj);
        break;
      }
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * EM Access（Internal link Addition）Execution.
   *
   * @param oppoNodeList
   *          List of opposing device
   * @param internalLinkVlanId
   *          Internal Link VLANID
   * @return Execution succeeded or not
   * @throws EmctrlException
   *           :EM Exception
   */
  protected boolean executeAddInternalLink(List<Nodes> nodesListDbMapper,
      int internallinkVlanId) throws EmctrlException {
    return PriorityLeafSpineUtility.executeAddInternalLink((AddNode) getInData(), nodesListDbMapper,
        internallinkVlanId);
  }
}
