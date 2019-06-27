/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.RecoverUpdateNode;
import msf.ecmm.emctrl.pojo.RecoverUpdateNodeNodeOsUpgrade;
import msf.ecmm.emctrl.pojo.parts.DeviceNodeOsUpgrade;
import msf.ecmm.emctrl.pojo.parts.InternalInterface;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.RecoverNodeService;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkOppo;

/**
 * Class for recovering the service (The extended function to upgrade OS in the node). 
 */
public class NodeRecoverNodeOsUpgrade extends NodeRecover {

  /**
   * Constructor.
   *
   * @param idt
   *          Input data
   * @param ukm
   *          URI key information
   */
  public NodeRecoverNodeOsUpgrade(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
  }

  @Override
  protected boolean checkExecuteRecoverService() {
    logger.trace(CommonDefinitions.START);
    boolean ret;
    RecoverNodeService inputData = (RecoverNodeService) getInData();
    if (inputData.getNode().getNodeType() != null
        && inputData.getNode().getNodeType().equals(CommonDefinitions.NODETYPE_SPINE)) {
      ret = false;
    } else {
      ret = true;
    }
    logger.debug("ret=" + ret);
    logger.trace(CommonDefinitions.END);
    return ret;
  }

  @Override
  protected boolean executeRecoverNode(RecoverNodeService input, Nodes nodes, Map<String, String> physicalIfNamesMap,
      Map<String, String> lagIfNamesMap) throws EmctrlException, IllegalArgumentException, DBAccessException {
    logger.trace(CommonDefinitions.START);

    RecoverUpdateNode recoverUpdateNode = EmMapper.toRecoverUpdateNode(input, nodes, physicalIfNamesMap, lagIfNamesMap);

    RecoverUpdateNodeNodeOsUpgrade runnou = convertToNodeOsUpgrade(input, nodes, recoverUpdateNode);

    EmController emController = EmController.getInstance();
    AbstractMessage ret = emController.request(runnou, false);

    logger.trace(CommonDefinitions.END);
    return ret.isResult();
  }

  /**
   * The result of EM data mapping is reflected in the extened function mapping which is the OS upgrade process.
   * In addition, the remaining part is  mapped again.
   *
   * @param input
   *          FC input data
   * @param nodes
   *          The Node infomation (DB)
   * @param base
   *          The result of mapping EM data to recover  
   * @return  The result of mapping EM data to recover (when OS is upgraded in the node)
   * @throws IllegalArgumentException:The mapping failed
   *          DBAccessException:DB error
   */
  private RecoverUpdateNodeNodeOsUpgrade convertToNodeOsUpgrade(RecoverNodeService input, Nodes nodes,
      RecoverUpdateNode base) throws IllegalArgumentException, DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("input=" + input + " nodes=" + nodes + " base=" + base);

    RecoverUpdateNodeNodeOsUpgrade runnou = new RecoverUpdateNodeNodeOsUpgrade();

    try (DBAccessManager session = new DBAccessManager()) {

      runnou.setName(base.getName());
      DeviceNodeOsUpgrade device = new DeviceNodeOsUpgrade();
      device.setName(base.getDevice().getName());
      device.setNodeType(base.getDevice().getNodeType());
      device.setEquipment(base.getDevice().getEquipment());
      device.setPhysiIfNames(base.getDevice().getPhysiIfNames());
      device.setLagIfNames(base.getDevice().getLagIfNames());
      List<InternalInterface> iiList = new ArrayList<>();
      for (InternalLinkOppo iio : input.getNode().getInternalLinkIfs()) {
        String ifName = EmMapper.getIfName(iio.getIfType(), iio.getIfId(), nodes);
        if (ifName.isEmpty()) {
          logger.debug("Not found Interface. nodeId=" + nodes.getNode_id() + " ifType=" + iio.getIfType() + " ifId="
              + iio.getIfId());
          throw new IllegalArgumentException("Not found Interface.");
        }
        InternalInterface ii = new InternalInterface();
        ii.setName(ifName);
        Nodes oppoNodes = session.searchNodes(iio.getOppositeNodeId(), null);
        if (oppoNodes == null) {
          logger.debug("Not found Opposite nodes. nodeId=" + iio.getOppositeNodeId());
          throw new IllegalArgumentException("Not found Opposite nodes.");
        }
        ii.setOppositeNodeName(oppoNodes.getNode_name());
        iiList.add(ii);
      }
      device.setInternalInterfaceList(iiList);
      if (input.getNode().getNodeType().equals(CommonDefinitions.NODETYPE_SPINE)) {
        device.addDbUpdate();
      } else {
        device.delDbUpdate();
      }

      runnou.setDevice(device);

    } catch (Exception exp) {
      throw exp;
    }

    logger.debug("runnou=" + runnou);
    logger.trace(CommonDefinitions.END);
    return runnou;
  }
}
