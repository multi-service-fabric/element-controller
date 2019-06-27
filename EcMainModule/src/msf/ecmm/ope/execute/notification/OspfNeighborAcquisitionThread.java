/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.SnmpController;

/**
 * Class definition for acquiring the OSPF neighbor information<br>
 * The OSPF neighbor information is acquired.
 */
public class OspfNeighborAcquisitionThread extends Thread {

  /** Logger. */
  protected static final MsfLogger logger = new MsfLogger();

  /** The node information acquiring the OSPF neighbor information. */
  private Nodes node;

  /** The list of the neighbor nodes  acquiring the OSPF neighbor information. */
  private List<String> neighbors = new ArrayList<>();

  /** The instance of the parent thread. */
  private OspfNeighborAcquisition instance;

  /**
   * The thread is executed.
   */
  public void run() {

    logger.trace(CommonDefinitions.START);
    logger.debug("thread start. nodeId=" + node.getNode_id());

    SnmpController snmpController = new SnmpController();
    Map<String, Integer> result = null;
    try {
      result = snmpController.isOspfNeighborFull(node.getEquipments(), node, (ArrayList<String>) neighbors);
    } catch (DevctrlException de) {
      result = new HashMap<>();
      for (String ip : neighbors) {
        result.put(ip, OspfNeighborAcquisition.OSPF_NEIGHBOR_FAILED);
      }
    }
    if (result.containsValue(null) || result.containsValue(OspfNeighborAcquisition.OSPF_NEIGHBOR_FAILED)) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403109, node.getNode_id()));
    }
    instance.setResult(node, result);

    logger.debug("GetOspfNeighborInfo=" + result);
    logger.trace(CommonDefinitions.END);

  }

  /**
   * The node information is acquired.
   *
   * @return The node information
   */
  public Nodes getNode() {
    return node;
  }

  /**
   * The node information is set.
   *
   * @param node
   *          The node information
   */
  public void setNode(Nodes node) {
    this.node = node;
  }

  /**
   * The Neighbors list is acquired.
   *
   * @return The neighbor noes list
   */
  public List<String> getNeighbors() {
    return neighbors;
  }

  /**
   * The neighbor nodes list is set.
   *
   * @param neighbors
   *          The neighbor nodes list
   */
  public void setNeighbors(List<String> neighbors) {
    this.neighbors = neighbors;
  }

  /**
   * The instance of the parent thread is acquired.
   *
   * @return The instance of the parent thread
   */
  public OspfNeighborAcquisition getInstance() {
    return instance;
  }

  /**
   * The instance of the parent thread is set.
   *
   * @param instance
   *          The instance of the parent thread
   */
  public void setInstance(OspfNeighborAcquisition instance) {
    this.instance = instance;
  }

}
