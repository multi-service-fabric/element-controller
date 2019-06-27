/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
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
import msf.ecmm.emctrl.pojo.parts.InternalInterface;
import msf.ecmm.emctrl.pojo.parts.XmlIntegerElement;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.AddNode;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkInfo;

/**
 * B-Leaf device extension （Extension function：priority Leaf/Spine）.
 */
public class BLeafAdditionPriorityLeafSpine extends BLeafAddition {

  /**
   * Constructor.
   *
   * @param idt
   *          Input data
   * @param ukm
   *          URI key informaiton
   */
  public BLeafAdditionPriorityLeafSpine(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
  }

  @Override
  protected boolean executeAddNode(String ecmainIpaddr, List<Nodes> nodesListDbMapper, int internallinkVlanId)
      throws EmctrlException, IllegalArgumentException {
    logger.trace(CommonDefinitions.START);

    AddNode addNode = (AddNode) getInData();

    AbstractMessage ret = null;

    try (DBAccessManager session = new DBAccessManager()) {
      Nodes pairNodes = null;
      if (((AddNode) getInData()).getUpdateNode() != null) {
        pairNodes = session.searchNodes(((AddNode) getInData()).getUpdateNode().getNodeId(), null);
      }

      BLeafAddDelete bleafAddEm = EmMapper.toBLeafInfoNodeCreate(addNode, ecmainIpaddr, nodesListDbMapper, pairNodes,
          internallinkVlanId);

      Nodes nodes = PriorityLeafSpineUtility.getNode(nodesListDbMapper, addNode.getCreateNode().getNodeId());
      for (InternalLinkInfo ifInfo : addNode.getCreateNode().getIfInfo().getInternalLinkIfs()) {
        String ifType = ifInfo.getInternalLinkIf().getIfType();
        String ifId = ifInfo.getInternalLinkIf().getIfId();
        String ifName = EmMapper.getIfName(ifType, ifId, nodes); 
        int cost = ifInfo.getInternalLinkIf().getCost();
        setCost(bleafAddEm, ifName, cost);
      }

      EmController emController = EmController.getInstance();
      ret = emController.request(bleafAddEm, false);

    } catch (DBAccessException dbae) {
      logger.debug("searchNodes error", dbae);
      throw new IllegalArgumentException();
    }

    logger.trace(CommonDefinitions.END);
    return ret.isResult();
  }

  /**
	* Cost Configuration (Own device).
   * @param leafAddEm EM-POJO
   * @param ifName IF name
   * @param cost Cost
   */
  private void setCost(BLeafAddDelete bleafAddEm, String ifName, int cost) {
    logger.trace(CommonDefinitions.START);
    for (InternalInterface ii : bleafAddEm.getDevice().getInternalLagList()) {
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
   * EM access（internal link addition）execution
   *
   * @param oppoNodeList
   *          Opposing device list
   * @param internalLinkVlanId
   *          Internal link VLANID
   * @return Feasibility
   * @throws EmctrlException
   *           :EM exception
   */
  protected boolean executeAddInternalLink(List<Nodes> nodesListDbMapper,
      int internallinkVlanId) throws EmctrlException {
    return PriorityLeafSpineUtility.executeAddInternalLink((AddNode) getInData(), nodesListDbMapper,
        internallinkVlanId);
  }
}
