/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.cp;

import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.convert.LogicalPhysicalConverter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BGPOptions;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.VRRPOptions;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.L3SliceAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.BulkCreateL3VlanIf;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.parts.StaticRoute;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsCreateL3VlanIf;

/**
 * L3VLAN IF Batch Generation.
 *
 */
public class AllL3VlanIfCreate extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_010101 = "010101";

  /** In case the number of pieces of information for process execution is zero (Data acquisition from DBs failed in the previous step of process). */
  private static final String ERROR_CODE_010102 = "010102";

  /** In case QoS capability check result is NG. */
  private static final String ERROR_CODE_010103 = "010103";

  /** In case the information of VLAN IF to be registered already exists. */
  private static final String ERROR_CODE_010301 = "010301";

  /** In case Registration of the same VLAN ID already exists in the same device / same IF. */
  private static final String ERROR_CODE_010306 = "010306";

  /** Disconnectionor connection timeout with EM has occurred while requesting to EM. */
  private static final String ERROR_CODE_010401 = "010401";

  /** Error has occurred from EM while requesting to EM (error response received). */
  private static final String ERROR_CODE_010402 = "010402";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_010403 = "010403";

  /** In case DB commitment failed after successful EM access. */
  private static final String ERROR_CODE_900404 = "900404";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public AllL3VlanIfCreate(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.AllL3VlanIfCreate);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      List<Nodes> nodesDbList = session.getNodesList();
      Set<Nodes> nodesSet = new HashSet<Nodes>();
      List<List<VlanIfs>> vlanIfsTable = new ArrayList<>();
      for (Nodes listElem : nodesDbList) {
        vlanIfsTable.add(session.getVlanIfsList(listElem.getNode_id()));
      }
      List<Integer> bgpIdList = new ArrayList<Integer>();
      List<Integer> vrrpIdList = new ArrayList<Integer>();
      for (List<VlanIfs> list : vlanIfsTable) {
        for (VlanIfs elem : list) {
          if (elem.getBgpOptionsList() != null) {
            for (BGPOptions bgpOpts : elem.getBgpOptionsList()) {
              bgpIdList.add(new Integer(bgpOpts.getBgp_id()));
            }
          }
          if (elem.getVrrpOptionsList() != null) {
            for (VRRPOptions vrrpOpts : elem.getVrrpOptionsList()) {
              vrrpIdList.add(new Integer(vrrpOpts.getVrrp_id()));
            }
          }
        }
      }
      if (bgpIdList.isEmpty()) {
        bgpIdList.add(new Integer(1));
      } else {
        Collections.sort(bgpIdList);
      }
      if (vrrpIdList.isEmpty()) {
        vrrpIdList.add(new Integer(1));
      } else {
        Collections.sort(vrrpIdList);
      }

      BulkCreateL3VlanIf inputData = (BulkCreateL3VlanIf) getInData();

      session.startTransaction();

      for (VlanIfsCreateL3VlanIf vlanIfs : inputData.getVlanIfs()) {
        Nodes nodesDb = null;
        for (Nodes listElem : nodesDbList) {
          if (listElem.getNode_id().equals(vlanIfs.getBaseIf().getNodeId())) {
            nodesDb = listElem;
            break;
          }
        }
        if (nodesDb == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
          return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010102);
        }
        if (!nodesDb.getEquipments().getL3vpn_capability()) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "L3vpn_capability is false."));
          return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
        }
        if (!nodesSet.contains(nodesDb)) {
          nodesSet.add(nodesDb);
        }

        if (vlanIfs.getQos() != null) {
          int ifSpeed = LogicalPhysicalConverter.getIfSpeed(vlanIfs.getBaseIf().getIfType(),
              vlanIfs.getBaseIf().getIfId(), nodesDb);
          if (!DbMapper.checkQosShapingRateValue(vlanIfs.getQos().getInflowShapingRate(), ifSpeed,
              nodesDb.getEquipments().getQos_shaping_flg())) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "InflowShapingRate capability error."));
            return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010103);
          }
          if (!DbMapper.checkQosShapingRateValue(vlanIfs.getQos().getOutflowShapingRate(), ifSpeed,
              nodesDb.getEquipments().getQos_shaping_flg())) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "OutflowShapingRate capability error."));
            return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010103);
          }
          if (!DbMapper.checkQosRemarkMenuValue(vlanIfs.getQos().getRemarkMenu(), nodesDb.getEquipments())) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "RemarkMenu capability error."));
            return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010103);
          }
          if (!DbMapper.checkQosEgressQueueMenuValue(vlanIfs.getQos().getEgressQueue(), nodesDb.getEquipments())) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "EgressQueueMenu capability error."));
            return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010103);
          }
        }

        if (!checkDuplicateRegisteration(vlanIfsTable, vlanIfs)) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Detect duplicate registration."));
          return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_010306);
        }

        checkStaticRoutes(vlanIfs.getStaticRoutes());

        String bgpId = null;
        String vrrpId = null;
        int num = 0;

        if (vlanIfs.getBgp() != null) {
          num = bgpIdList.get(0);
          if (!bgpIdList.isEmpty()) {
            for (Integer id : bgpIdList) {
              if (id != num) {
                break;
              }
              num++;
            }
          }
          bgpId = Integer.toString(num);
          bgpIdList.add(num);
          Collections.sort(bgpIdList);
        }

        if (vlanIfs.getVrrp() != null) {
          num = vrrpIdList.get(0);
          if (!vrrpIdList.isEmpty()) {
            for (Integer id : vrrpIdList) {
              if (id != num) {
                break;
              }
              num++;
            }
          }
          vrrpId = Integer.toString(num);
          vrrpIdList.add(num);
          Collections.sort(vrrpIdList);
        }

        VlanIfs vlanIfsDb = DbMapper.toL3VlanIfCreate(vlanIfs, nodesDb, bgpId, vrrpId);

        session.addL3VlanIf(vlanIfsDb);
      }

      L3SliceAddDelete l3SliceAddDeleteEm = EmMapper.toL3VlanIfCreate(inputData, nodesSet);

      AbstractMessage result = EmController.getInstance().request(l3SliceAddDeleteEm);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010402);
      }

      session.commit();

      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      switch (dbae.getCode()) {
        case DOUBLE_REGISTRATION:
          response = makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_010301);
          break;
        case COMMIT_FAILURE:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900404);
          break;
        default:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010403);
          break;
      }
    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010401);
    } catch (IllegalArgumentException iae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), iae);
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {
      BulkCreateL3VlanIf inputData = (BulkCreateL3VlanIf) getInData();

      inputData.check(OperationType.AllL3VlanIfCreate);
    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    logger.trace(CommonDefinitions.END);
    return result;
  }

  /**
   * Check if the same vlan has already been set to the same IF of same device.
   *
   * @param vlanIfsTable
   *          information of VLAN which been already registered
   * @param vlanIfs
   *          VLAN information to be registered (FC input data)
   * @return true: without duplication, false: with duplication
   */
  private boolean checkDuplicateRegisteration(List<List<VlanIfs>> vlanIfsTable, VlanIfsCreateL3VlanIf vlanIfs) {
    logger.trace(CommonDefinitions.START);
    logger.debug(vlanIfs);

    String nodeId = vlanIfs.getBaseIf().getNodeId();
    String ifType = vlanIfs.getBaseIf().getIfType();
    String ifId = vlanIfs.getBaseIf().getIfId();
    Integer vlanId = vlanIfs.getVlanId();

    boolean notDuplicate = true;
    CHECK_DUP: for (List<VlanIfs> dbVlanList : vlanIfsTable) {
      for (VlanIfs dbVlan : dbVlanList) {
        if (!dbVlan.getNode_id().equals(nodeId)) {
          continue;
        }
        String ifIdDb = "";
        if (dbVlan.getBreakout_if_id() != null) {
          ifIdDb = dbVlan.getBreakout_if_id();
        } else if (dbVlan.getLag_if_id() != null) {
          ifIdDb = dbVlan.getLag_if_id();
        } else {
          ifIdDb = dbVlan.getPhysical_if_id();
        }
        if (ifIdDb.equals(ifId) && dbVlan.getVlan_id().equals(vlanId.toString())) {
          logger.debug("duplicate registration nodeId=" + nodeId + " ifType=" + ifType + " ifId=" + ifId + " vlanId="
              + vlanId);
          notDuplicate = false;
          break CHECK_DUP;
        }
      }
    }
    logger.debug(notDuplicate);
    logger.trace(CommonDefinitions.END);
    return notDuplicate;
  }

  /**
   * static-routes Duplicated Registration Determination.
   *
   * @param staticListRest
   *          static-routes information of input REST
   * @throws IllegalArgumentException
   *          with duplication
   */
  private void checkStaticRoutes(List<StaticRoute> staticListRest) throws IllegalArgumentException {
    logger.trace(CommonDefinitions.START);
    if (staticListRest == null) {
      return;
    }
    for (int i = 0; i <  staticListRest.size(); i++) {
      StaticRoute static1 = staticListRest.get(i);
      for (int j = 0; j <  staticListRest.size(); j++) {
        StaticRoute static2 = staticListRest.get(j);
        if (i == j) {
          continue;
        }
        if (static1.getAddressType().equals(static2.getAddressType())
            && static1.getAddress().equals(static2.getAddress())
            && static1.getPrefix() == static2.getPrefix()
            && static1.getNextHop().equals(static2.getNextHop())) {
          logger.debug("duplicate static-routes registration addressType=" + static1.getAddressType() + " address="
              + static1.getAddress() + " prefix=" + static1.getPrefix() + " nextHop=" + static1.getNextHop());
          throw new IllegalArgumentException();
        }
      }
    }
    logger.trace(CommonDefinitions.END);
  }

}
