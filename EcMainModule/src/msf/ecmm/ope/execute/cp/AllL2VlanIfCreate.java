/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.cp;

import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.convert.LogicalPhysicalConverter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.L2SliceAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.BulkCreateL2VlanIf;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.parts.CreateVlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.UpdateVlanIfs;

/**
 * L2VLAN IF Batch Generate/Change.
 */
public class AllL2VlanIfCreate extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_010101 = "010101";

  /** In case the number of pieces of information for process execution is zero (data acquisition from DBs has failed in the previous step of process). */
  private static final String ERROR_CODE_010102 = "010102";

  /** In case QoS capability check result is NG. */
  private static final String ERROR_CODE_010103 = "010103";

  /** In case the VLAN IF information to be registered already exists. */
  private static final String ERROR_CODE_010301 = "010301";

  /** In case Rregistration of the same VLAN ID already exists in the same device / same IF. */
  private static final String ERROR_CODE_010306 = "010306";

  /** Disconnection or connection timeout with EM has occurred while requesting to EM. */
  private static final String ERROR_CODE_010401 = "010401";

  /** Error has occurred from EM while requesting to EM (error response received). */
  private static final String ERROR_CODE_010402 = "010402";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_010403 = "010403";

  /** In case DB commitment failed after successful EM access. */
  private static final String ERROR_CODE_900404 = "900404";

  /**
   * Constuctor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public AllL2VlanIfCreate(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.AllL2VlanIfCreate);
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

      BulkCreateL2VlanIf inputData = (BulkCreateL2VlanIf) getInData();

      List<Nodes> nodesDbList = session.getNodesList();
      Set<Nodes> nodesSet = new HashSet<Nodes>();

      Map<String, List<VlanIfs>> allVlanIfsMap = new HashMap<String, List<VlanIfs>>();
      if (inputData.getUpdateVlanIfs() != null) {
        for (UpdateVlanIfs updVlanIfs : inputData.getUpdateVlanIfs()) {
          for (Nodes listElem : nodesDbList) {
            if (listElem.getNode_id().equals(updVlanIfs.getNodeId())) {
              if (!nodesSet.contains(listElem)) {
                nodesSet.add(listElem);
              }
              break;
            }
          }
          allVlanIfsMap.put(updVlanIfs.getNodeId(), session.getVlanIfsList(updVlanIfs.getNodeId()));
        }
      }

      session.startTransaction();

      if (inputData.getCreateVlanIfs() != null) {
        for (CreateVlanIfs creVlanIfs : inputData.getCreateVlanIfs()) {
          Nodes nodesDb = null;
          for (Nodes listElem : nodesDbList) {
            if (listElem.getNode_id().equals(creVlanIfs.getBaseIf().getNodeId())) {
              nodesDb = listElem;
              break;
            }
          }
          if (nodesDb == null) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
            return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010102);
          }
          if (!nodesDb.getEquipments().getL2vpn_capability()) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "L2vpn_capability is false."));
            return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
          }
          if (!nodesDb.getEquipments().getEvpn_capability()) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "EVPN_capability is false."));
            return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
          }
          if (!nodesSet.contains(nodesDb)) {
            nodesSet.add(nodesDb);
          }

          if (creVlanIfs.getQos() != null) {
            int ifSpeed = LogicalPhysicalConverter.getIfSpeed(creVlanIfs.getBaseIf().getIfType(),
                creVlanIfs.getBaseIf().getIfId(), nodesDb);
            if (!DbMapper.checkQosShapingRateValue(creVlanIfs.getQos().getInflowShapingRate(), ifSpeed,
                nodesDb.getEquipments().getQos_shaping_flg())) {
              logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "InflowShapingRate capability error."));
              return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010103);
            }
            if (!DbMapper.checkQosShapingRateValue(creVlanIfs.getQos().getOutflowShapingRate(), ifSpeed,
                nodesDb.getEquipments().getQos_shaping_flg())) {
              logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "OutflowShapingRate capability error."));
              return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010103);
            }
            if (!DbMapper.checkQosRemarkMenuValue(creVlanIfs.getQos().getRemarkMenu(), nodesDb.getEquipments())) {
              logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "RemarkMenu capability error."));
              return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010103);
            }
            if (!DbMapper.checkQosEgressQueueMenuValue(creVlanIfs.getQos().getEgressQueue(), nodesDb.getEquipments())) {
              logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "EgressQueueMenu capability error."));
              return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010103);
            }
          }

          if (!checkDuplicateRegisteration(session.getVlanIfsList(creVlanIfs.getBaseIf().getNodeId()), creVlanIfs)) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Detect duplicate registration."));
            return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_010306);
          }

          VlanIfs vlanIfsDb = DbMapper.toL2VlanIfCreate(creVlanIfs, nodesDb);

          session.addL2VlanIf(vlanIfsDb);
        }
      }

      L2SliceAddDelete l2SliceAddDeleteEm = EmMapper.toL2VlanIfCreate(inputData, nodesSet, allVlanIfsMap);

      AbstractMessage result = EmController.getInstance().request(l2SliceAddDeleteEm);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010402);
      }

      session.commit();

      response = makeSuccessResponse(200, new CommonResponse());

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

      BulkCreateL2VlanIf inputData = (BulkCreateL2VlanIf) getInData();

      inputData.check(OperationType.AllL2VlanIfCreate);

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
   * @param dbVlanList
   *          information of VLAN which been already registered (same device)
   * @param creVlanIfs
   *          VLAN information to be registered (FC input data)
   * @return true: without duplication, false: with duplication
   */
  private boolean checkDuplicateRegisteration(List<VlanIfs> dbVlanList, CreateVlanIfs creVlanIfs) {
    logger.trace(CommonDefinitions.START);
    logger.debug(dbVlanList);

    String ifType = creVlanIfs.getBaseIf().getIfType();
    String ifId = creVlanIfs.getBaseIf().getIfId();
    Integer vlanId = creVlanIfs.getVlanId();

    boolean notDuplicate = true;
    for (VlanIfs dbVlan : dbVlanList) {
      String ifIdDb = "";
      if (dbVlan.getBreakout_if_id() != null) {
        ifIdDb = dbVlan.getBreakout_if_id();
      } else if (dbVlan.getLag_if_id() != null) {
        ifIdDb = dbVlan.getLag_if_id();
      } else {
        ifIdDb = dbVlan.getPhysical_if_id();
      }
      if (ifIdDb.equals(ifId) && dbVlan.getVlan_id().equals(vlanId.toString())) {
        logger.debug("duplicate registration ifType=" + ifType + " ifId=" + ifId + " vlanId=" + vlanId);
        notDuplicate = false;
        break;
      }
    }
    logger.debug(notDuplicate);
    logger.trace(CommonDefinitions.END);
    return notDuplicate;
  }

}
