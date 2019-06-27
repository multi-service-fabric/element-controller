/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.notification;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetNodeTraffic;
import msf.ecmm.traffic.TrafficDataGatheringManager;
import msf.ecmm.traffic.pojo.NodeKeySet;
import msf.ecmm.traffic.pojo.TrafficData;

/**
 * Traffic Information Acquisition. 
 */
public class TrafficDataAcquisition extends Operation {

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_440201 = "440201";

  /** Other exceptions. */
  private static final String ERROR_CODE_440299 = "440299";

  /**
   * Constructor.
   *
   * @param idt
   *          input data 
   * @param ukm
   *          URI key information 
   */
  public TrafficDataAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.TrafficDataAcquisition);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    try (DBAccessManager session = new DBAccessManager()) {

      Nodes nodes = session.searchNodes(getUriKeyMap().get(KEY_NODE_ID), null);
      List<BreakoutIfs> breakoutIfsDbList = session.getBreakoutIfsList(getUriKeyMap().get(KEY_NODE_ID));
      NodeKeySet nodeKey = new NodeKeySet();
      List<List<VlanIfs>> vlanIfsTable = new ArrayList<>();
      nodeKey.setEquipmentsData(nodes);
      nodeKey.setEquipmentsType(nodes.getEquipments());
      vlanIfsTable.add(session.getVlanIfsList(nodes.getNode_id()));

      HashMap<NodeKeySet, HashMap<String, TrafficData>> trafficDataMap = TrafficDataGatheringManager.getInstance()
          .getTrafficData();
      Timestamp time = TrafficDataGatheringManager.getInstance().getLastGathering();
      int cycle = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.TRAFFIC_MIB_INTERVAL);

      GetNodeTraffic resp = RestMapper.toNodeTrafficData(nodeKey, vlanIfsTable, trafficDataMap, time, cycle,breakoutIfsDbList);

      if (resp != null) {
        response = makeSuccessResponse(RESP_OK_200, resp);
      } else {
        logger.debug("Data Mapping error");
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_440299);
      }

    } catch (DBAccessException dbe) {
      logger.debug("DB access error", dbe);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_440201);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {
    return false;
  }

}
