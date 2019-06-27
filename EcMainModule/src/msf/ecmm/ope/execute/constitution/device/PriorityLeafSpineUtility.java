/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.InternalLinkAddDelete;
import msf.ecmm.emctrl.pojo.parts.Device;
import msf.ecmm.emctrl.pojo.parts.InternalInterface;
import msf.ecmm.emctrl.pojo.parts.XmlIntegerElement;
import msf.ecmm.ope.receiver.pojo.AddNode;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodesInterface;

/**
 * The utility for the priority Leaf/Spine.
 */
public class PriorityLeafSpineUtility {

  /** Logger. */
  private static final MsfLogger logger = new MsfLogger();

  /**
   * The access to EM is executed.(to add the internal link).
   *
   * @param addNode
   *          The FC input information
   * @param oppoNodeList
   *          The list of the opposite nodes
   * @param internalLinkVlanId
   *          The VLAN ID for the internal link 
   * @return  Result
   * @throws EmctrlException
   *           :EM exceptions
   */
  protected static boolean executeAddInternalLink(AddNode addNode, List<Nodes> nodesListDbMapper,
      int internallinkVlanId) throws EmctrlException {
    logger.trace(CommonDefinitions.START);

    InternalLinkAddDelete internalLinkAddEm = EmMapper.toInternalLinkCreate(addNode, nodesListDbMapper,
        internallinkVlanId);

    Nodes nodes = null;
    for (OppositeNodesInterface oppo : addNode.getCreateNode().getOppositeNodes()) {
      for (Nodes tmp : nodesListDbMapper) {
        if (oppo.getNodeId().equals(tmp.getNode_id())) {
          nodes = tmp; 
          break;
        }
      }
      String ifType = oppo.getInternalLinkIf().getIfType();
      String ifId = oppo.getInternalLinkIf().getIfId();
      String ifName = EmMapper.getIfName(ifType, ifId, nodes);
      int cost = oppo.getInternalLinkIf().getCost();
      setCost(internalLinkAddEm, nodes.getNode_name(), ifName, cost);
    }

    EmController emController = EmController.getInstance();
    AbstractMessage ret = emController.request(internalLinkAddEm, false);

    logger.trace(CommonDefinitions.END);
    return ret.isResult();
  }


  /**
   * The cost is set(for the  opposite  node).
   *
   * @param leafAddEm
   *          EM-POJO
   * @param nodeName
   *          The node name
   * @param ifName
   *          The IF name
   * @param cost
   *          The cost
   */
  private static void setCost(InternalLinkAddDelete internalLinkAddEm, String nodeName, String ifName, int cost) {
    logger.trace(CommonDefinitions.START);
    loopEnd: for (Device dev : internalLinkAddEm.getDevice()) {
      if (dev.getName().equals(nodeName)) {
        for (InternalInterface ii : dev.getInternalLagList()) {
          if (ii.getName().equals(ifName)) {
            XmlIntegerElement costObj = new XmlIntegerElement();
            costObj.setValue(cost);
            ii.setCost(costObj);
            break loopEnd;
          }
        }
      }
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * The node information is acquired  from the LIST.
   *
   * @param nodesListDbMapper
   *          The LIST（DB）of the node information.
   * @param nodeId
   *          The node ID
   * @return  The node information
   */
  public static Nodes getNode(List<Nodes> nodesListDbMapper, String nodeId) {
    logger.trace(CommonDefinitions.START);
    Nodes nodes = null;
    for (Nodes tmp : nodesListDbMapper) {
      if (tmp.getNode_id().equals(nodeId)) {
        nodes = tmp;
        break;
      }
    }
    logger.trace(CommonDefinitions.END);
    return nodes;
  }
}
