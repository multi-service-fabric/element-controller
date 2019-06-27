/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.traffic;

import static msf.ecmm.common.CommonDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.devctrl.DevctrlException;
import msf.ecmm.devctrl.ScriptController;
import msf.ecmm.devctrl.SnmpController;
import msf.ecmm.devctrl.pojo.SnmpIfTraffic;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.Operations;
import msf.ecmm.traffic.pojo.NodeKeySet;

/**
 * Traffic Information Collection Execution Class Definition. Collecting traffic information at the target node.
 */
public class DataGatheringExecutor extends Thread {

  /**
   * Logger
   */
  private final MsfLogger logger = new MsfLogger();

  /** Traffic Information. */
  private ArrayList<SnmpIfTraffic> trafficData;

  /** getBulk Max. Value. */
  private int maxGetBulk;

  /** Device Information set. */
  private NodeKeySet deviceDetail;

  /** All node information(for core router). */
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
  public DataGatheringExecutor(int maxGetBulk, NodeKeySet deviceDetail, Map<Equipments, List<Nodes>> nodeMap) {
    this.maxGetBulk = maxGetBulk;
    this.deviceDetail = deviceDetail;
    this.trafficData = new ArrayList<SnmpIfTraffic>();
    this.nodeMap = nodeMap;
  }

  public void run() {

    logger.trace(CommonDefinitions.START);

    SnmpController snmp = new SnmpController();
    ScriptController script = new ScriptController();
    int nodestate = 0;
    String nodeid = deviceDetail.getEquipmentsData().getNode_id();
    boolean mibflg = false;
    boolean mibSuccessFlg = false;

    try {
      trafficData = snmp.getTraffic(deviceDetail.getEquipmentsType(), deviceDetail.getEquipmentsData());
      if (deviceDetail.getEquipmentsData().getNode_state() == NODE_STATE_MALFUNCTION) {
        mibSuccessFlg = true;
      }
    } catch (DevctrlException e) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_407051, deviceDetail.getEquipmentsData().getNode_id(),
          deviceDetail.getEquipmentsType().getEquipment_type_id()), e);
      mibflg = true;
    } catch (Exception e) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_407051, deviceDetail.getEquipmentsData().getNode_id(),
          deviceDetail.getEquipmentsType().getEquipment_type_id()), e);
      mibflg = true;
    }

    if ((deviceDetail.getEquipmentsData().getNode_state() != NODE_STATE_MALFUNCTION && mibflg)
        || ((deviceDetail.getEquipmentsData().getNode_state() == NODE_STATE_MALFUNCTION) && mibSuccessFlg)) {
      String statusStr = null;
      try (DBAccessManager session = new DBAccessManager()) {
        session.startTransaction();
        if (deviceDetail.getEquipmentsData().getNode_state() != NODE_STATE_MALFUNCTION && mibflg) {
          nodestate = NODE_STATE_MALFUNCTION;
          statusStr = IF_STATE_NG_STRING;
        } else if (deviceDetail.getEquipmentsData().getNode_state() == NODE_STATE_MALFUNCTION && mibSuccessFlg) {
          nodestate = NODE_STATE_OPERATION;
          statusStr = IF_STATE_OK_STRING;
        }
        session.updateNodeState(nodeid, nodestate);
        session.commit();
        List<VlanIfs> vlanIfsList = new ArrayList<VlanIfs>();
        Operations snmpTrapData = RestMapper.toSnmpTrapNotificationInfo(nodeid, null, null, vlanIfsList, statusStr,
            mibflg);

        RestClient restClient = new RestClient();
        restClient.request(RestClient.OPERATION, new HashMap<String, String>(), snmpTrapData,
            CommonResponseFromFc.class);

      } catch (DBAccessException dbe) {
        logger.debug("DB access error", dbe);
      } catch (RestClientException re) {
        logger.debug("Rest request failed", re);
      }
    }
    if (!mibflg && deviceDetail.getEquipmentsType().getVlan_traffic_capability() != null) {
      mibflg = false;
      mibSuccessFlg = false;
      ArrayList<SnmpIfTraffic> vlanTrafficData = null;
      if (deviceDetail.getEquipmentsType().getVlan_traffic_capability().equals(VLAN_TRAFFIC_TYPE_MIB)) {
        try {
          vlanTrafficData = snmp.getVlanTraffic(deviceDetail.getEquipmentsType(), deviceDetail.getEquipmentsData());
          if (null != vlanTrafficData) {
            trafficData.addAll(vlanTrafficData);
          }
          if (deviceDetail.getEquipmentsData().getNode_state() == NODE_STATE_MALFUNCTION) {
            mibSuccessFlg = true;
          }
        } catch (DevctrlException devctrlEx) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_407094, deviceDetail.getEquipmentsData().getNode_id(),
              deviceDetail.getEquipmentsType().getEquipment_type_id()), devctrlEx);
          mibflg = true;
        } catch (Exception ex) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_407094, deviceDetail.getEquipmentsData().getNode_id(),
              deviceDetail.getEquipmentsType().getEquipment_type_id()), ex);
        }
        checkGetTraffic(mibflg, mibSuccessFlg);

      } else if (deviceDetail.getEquipmentsType().getVlan_traffic_capability().equals(VLAN_TRAFFIC_TYPE_CLI)) {
        try {
          vlanTrafficData = script.getVlanTraffic(deviceDetail.getEquipmentsType(), deviceDetail.getEquipmentsData());
        } catch (DevctrlException devctrlEx) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_407095, deviceDetail.getEquipmentsData().getNode_id(),
              deviceDetail.getEquipmentsType().getEquipment_type_id()), devctrlEx);
        } catch (Exception ex) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_407095, deviceDetail.getEquipmentsData().getNode_id(),
              deviceDetail.getEquipmentsType().getEquipment_type_id()), ex);
        }
        if (deviceDetail.getEquipmentsType().getSame_vlan_number_traffic_total_value_flag()
            && vlanTrafficData != null) {
          try (DBAccessManager session = new DBAccessManager()) {
            HashSet<String> hs = new HashSet<String>();
            List<VlanIfs> vlanIfsList = session.getVlanIfsList(nodeid);
            List<String> overlap = new ArrayList<String>();
            for (int i = 0; i < vlanIfsList.size(); i++) {
              if (!hs.contains(vlanIfsList.get(i).getVlan_id())) {
                hs.add(vlanIfsList.get(i).getVlan_id());
              } else {
                overlap.add(vlanIfsList.get(i).getVlan_id());
              }
            }
            Iterator<SnmpIfTraffic> itrTraffic = vlanTrafficData.iterator();

            while (itrTraffic.hasNext()) {
              SnmpIfTraffic target = itrTraffic.next();
              String[] ifname = target.getIfName().split("\\.");
              for (String vlanId : overlap) {
                if (vlanId.equals(ifname[1])) {
                  logger.debug("Remove CLI traffic : " + target);
                  itrTraffic.remove();
                }
              }
            }
          } catch (DBAccessException dbe) {
            logger.debug("DB access error", dbe);
          }
        }
        if (null != vlanTrafficData) {
          trafficData.addAll(vlanTrafficData);
        }
      }
    }

    try {
      TrafficDataGatheringManager.getInstance().getWatchDogThreadHolder().setTrafficData(deviceDetail, trafficData,
          nodeMap);
    } catch (Exception ex) {
      logger.debug("Unexpected exception occured at setting traffic data.:" + ex);
    }

    logger.trace(CommonDefinitions.END);
  }

  @Override
  public String toString() {
    return "DataGatheringExecutor [trafficData=" + trafficData + ", maxGetBulk=" + maxGetBulk + ", deviceDetail="
        + deviceDetail + "]";
  }

  /**
   * Acquisition result notificaiton function.
   *
   * @param mibflg
   *          MIB information acquisition failure flag
   * @param mibSuccessFlg
   *          MIB information acquisition success flag initialization
   */
  private void checkGetTraffic(boolean mibflg, boolean mibSuccessFlg) {
    int nodestate = 0;
    String nodeid = deviceDetail.getEquipmentsData().getNode_id();
    if ((deviceDetail.getEquipmentsData().getNode_state() != NODE_STATE_MALFUNCTION && mibflg)
        || ((deviceDetail.getEquipmentsData().getNode_state() == NODE_STATE_MALFUNCTION) && mibSuccessFlg)) {
      String statusStr = null;
      try (DBAccessManager session = new DBAccessManager()) {
        session.startTransaction();
        if (deviceDetail.getEquipmentsData().getNode_state() != NODE_STATE_MALFUNCTION && mibflg) {
          nodestate = NODE_STATE_MALFUNCTION;
          statusStr = IF_STATE_NG_STRING;
        } else if (deviceDetail.getEquipmentsData().getNode_state() == NODE_STATE_MALFUNCTION && mibSuccessFlg) {
          nodestate = NODE_STATE_OPERATION;
          statusStr = IF_STATE_OK_STRING;
        }
        session.updateNodeState(nodeid, nodestate);
        session.commit();
        List<VlanIfs> vlanIfsList = new ArrayList<VlanIfs>();
        Operations snmpTrapData = RestMapper.toSnmpTrapNotificationInfo(nodeid, null, null, vlanIfsList, statusStr,
            mibflg);

        RestClient restClient = new RestClient();
        restClient.request(RestClient.OPERATION, new HashMap<String, String>(), snmpTrapData,
            CommonResponseFromFc.class);

      } catch (DBAccessException dbe) {
        logger.debug("DB access error", dbe);
      } catch (RestClientException re) {
        logger.debug("Rest request failed", re);
      }
    }
  }

}
