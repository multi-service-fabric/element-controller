/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.notification;

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
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetAllNodeTraffic;
import msf.ecmm.traffic.TrafficDataGatheringManager;
import msf.ecmm.traffic.pojo.NodeKeySet;
import msf.ecmm.traffic.pojo.TrafficData;

/**
 * Traffic Information Acquisition.
 */
public class AllTrafficDataAcquisition extends Operation {

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_280401 = "280401";

  /** Other exceptions. */
  private static final String ERROR_CODE_280499 = "280499";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public AllTrafficDataAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.TrafficDataAllAcquisition);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    try (DBAccessManager session = new DBAccessManager()) {

      List<Equipments> nodetypeList = session.getEquipmentsList();
      ArrayList<NodeKeySet> nodeList = new ArrayList<>();
      List<List<VlanIfs>> vlanIfsTable = new ArrayList<>();
      List<BreakoutIfs> breakoutIfsDbList = new ArrayList<>();

      for (Equipments nodeType : nodetypeList) {
        List<Nodes> nodes = session.searchNodesByEquipmentId(nodeType.getEquipment_type_id());

        for (Nodes node : nodes) {
          NodeKeySet key = new NodeKeySet();
          key.setEquipmentsData(node);
          key.setEquipmentsType(nodeType);

          nodeList.add(key);
          breakoutIfsDbList.addAll(session.getBreakoutIfsList(node.getNode_id()));
          vlanIfsTable.add(session.getVlanIfsList(node.getNode_id()));
        }
      }

      HashMap<NodeKeySet, ArrayList<TrafficData>> trafficDataMap = TrafficDataGatheringManager.getInstance()
          .getTrafficData();
      Timestamp time = TrafficDataGatheringManager.getInstance().getLastGathering();
      int cycle = EcConfiguration.getInstance().get(Integer.class, EcConfiguration.TRAFFIC_MIB_INTERVAL);

      GetAllNodeTraffic resp = RestMapper.toNodeTrafficDataList(nodeList, vlanIfsTable, trafficDataMap, time, cycle,breakoutIfsDbList);

      if (resp != null) {
        response = makeSuccessResponse(RESP_OK_200, resp);
      } else {
        logger.debug("Data Mapping error");
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_280499);
      }

    } catch (DBAccessException dbe) {
      logger.debug("DB access error", dbe);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_280401);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {
    return false;
  }

}
