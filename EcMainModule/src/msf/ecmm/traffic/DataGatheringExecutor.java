/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.traffic;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.SnmpController;
import msf.ecmm.devctrl.pojo.SnmpIfTraffic;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.Operations;
import msf.ecmm.traffic.pojo.NodeKeySet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Traffic Information Collection Execution Class Definition. Collecting traffic information at the target node.
 */
public class DataGatheringExecutor extends Thread {

  /**
   * Logger
   */
  private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /** Traffic Information. */
  private ArrayList<SnmpIfTraffic> trafficData;

  /** getBulk Max. Value. */
  private int maxGetBulk;

  /** Device Information Set. */
  private NodeKeySet deviceDetail;

  /** All Nodes Information (for core router). */
  private Map<Equipments, List<Nodes>> nodeMap;

  /** Device Status: Running. */
  private static final int NODE_STATE_OPERATION = 0;
  /** Device Status: Out Of Order. */
  private static final int NODE_STATE_MALFUNCTION = 7;

  /**
   * Constructor
   *
   * @param maxGetBulk
   *          getBulk max. value
   * @param deviceDetail
   *          target node information
   * @param nodeMap
   *          node information list (for core router).
   */
  public DataGatheringExecutor(int maxGetBulk, NodeKeySet deviceDetail,
      Map<Equipments, List<Nodes>> nodeMap) {
    this.maxGetBulk = maxGetBulk;
    this.deviceDetail = deviceDetail;
    this.trafficData = new ArrayList<SnmpIfTraffic>();
    this.nodeMap = nodeMap;
  }

  public void run() {

    logger.trace(CommonDefinitions.START);

    SnmpController snmp = new SnmpController();
    int nodestate = 0;
    String nodeid = deviceDetail.getEquipmentsData().getNode_id();
    boolean mibflg = false;
    boolean mibSuccessFlg = false;

    try {
      trafficData = snmp.getTraffic(deviceDetail.getEquipmentsType(),
        deviceDetail.getEquipmentsData());
      if (deviceDetail.getEquipmentsData().getNode_state() == NODE_STATE_MALFUNCTION) {
        mibSuccessFlg = true;
      }
    } catch (DevctrlException e) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_407051,
                deviceDetail.getEquipmentsData().getNode_id(),
                deviceDetail.getEquipmentsType().getEquipment_type_id()), e);
      mibflg = true;
    } catch (Exception e) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_407051,
          deviceDetail.getEquipmentsData().getNode_id(),
          deviceDetail.getEquipmentsType().getEquipment_type_id()), e);
    }

    if ((deviceDetail.getEquipmentsData().getNode_state() != NODE_STATE_MALFUNCTION && mibflg)
        || ((deviceDetail.getEquipmentsData()
            .getNode_state() == NODE_STATE_MALFUNCTION) && mibSuccessFlg)) {
      String statusStr = null;
      try (DBAccessManager session = new DBAccessManager()) {
        session.startTransaction();
        if (deviceDetail.getEquipmentsData().getNode_state() != NODE_STATE_MALFUNCTION && mibflg) {
          nodestate = NODE_STATE_MALFUNCTION;
          statusStr = IF_STATE_NG_STRING;
        } else if (deviceDetail.getEquipmentsData().getNode_state() == NODE_STATE_MALFUNCTION
            && mibSuccessFlg) {
          nodestate = NODE_STATE_OPERATION;
          statusStr = IF_STATE_OK_STRING;
        }
        session.updateNodeState(nodeid, nodestate);
        session.commit();
        List<VlanIfs> vlanIfsList = new ArrayList<VlanIfs>();
        Operations snmpTrapData = RestMapper.toSnmpTrapNotificationInfo(nodeid,
            null, null, vlanIfsList, statusStr, mibflg);

        RestClient restClient = new RestClient();
        restClient.request(RestClient.OPERATION, new HashMap<String, String>(),
            snmpTrapData, CommonResponseFromFc.class);

      } catch (DBAccessException dbe) {
        logger.debug("DB access error", dbe);
      } catch (RestClientException re) {
        logger.debug("Rest request failed", re);
      }
    }


    try {
      TrafficDataGatheringManager.getInstance().getWatchDogThreadHolder()
          .setTrafficData(deviceDetail, trafficData,nodeMap);
    } catch (Exception e) {
      logger.debug("Unexpected exception occured at setting traffic data.:" + e);
    }

    logger.trace(CommonDefinitions.END);
  }

  @Override
  public String toString() {
    return "DataGatheringExecutor [trafficData=" + trafficData + ", maxGetBulk="
        + maxGetBulk + ", deviceDetail=" + deviceDetail + "]";
  }

}
