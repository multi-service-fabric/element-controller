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
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.DummyVlanIfs;
import msf.ecmm.db.pojo.IRBInstanceInfo;
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
import msf.ecmm.ope.receiver.pojo.BulkDeleteL2VlanIf;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.parts.RemoveUpdateVlanIfs;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsDeleteVlanIf;

/**
 * L2VLAN IF Batch Delete/Change.
 */
public class AllL2VlanIfRemove extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_010101 = "010101";

  /** In case the information of VLAN IF to be deleted does not exist. */
  private static final String ERROR_CODE_010201 = "010201";

 /** Disconnection or connection timeout with EM has occurred while requesting to EM. */
  private static final String ERROR_CODE_010308 = "010308";

  /** Disconnection with EM or timeout has occurred while EM requesting. */
  private static final String ERROR_CODE_010401 = "010401";

  /** Error has occurred from EM while requesting to EM (error response received). */
  private static final String ERROR_CODE_010402 = "010402";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_010403 = "010403";

  /** In case DB commitment failed after successful EM access. */
  private static final String ERROR_CODE_900404 = "900404";

  /** In case dummy target IF is already a dummy. */
  private static final String ERROR_CODE_010307 = "010307";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public AllL2VlanIfRemove(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.AllL2VlanIfRemove);
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

      BulkDeleteL2VlanIf inputData = (BulkDeleteL2VlanIf) getInData();

      List<Nodes> nodesDbList = session.getNodesList();
      Set<Nodes> nodesSet = new HashSet<Nodes>();
      Map<String, IRBInstanceInfo> irbInstanceMap = new HashMap<String, IRBInstanceInfo>();

      Map<String, List<VlanIfs>> allVlanIfsMap = new HashMap<String, List<VlanIfs>>();
      Map<String, List<VlanIfs>> deleteVlanIfsMap = new HashMap<String, List<VlanIfs>>();
      Map<String, List<DummyVlanIfs>> allDummyVlanIfsMap = new HashMap<String, List<DummyVlanIfs>>();

      if (inputData.getUpdateVlanIfs() != null) {
        for (RemoveUpdateVlanIfs updVlanIfs : inputData.getUpdateVlanIfs()) {
          for (Nodes listElem : nodesDbList) {
            if (listElem.getNode_id().equals(updVlanIfs.getNodeId())) {
              if (!nodesSet.contains(listElem)) {
                nodesSet.add(listElem);
              }
              break;
            }
          }
          if (allVlanIfsMap.get(updVlanIfs.getNodeId()) == null) {
            allVlanIfsMap.put(updVlanIfs.getNodeId(), session.getVlanIfsList(updVlanIfs.getNodeId()));
          }
          if (allDummyVlanIfsMap.get(updVlanIfs.getNodeId()) == null) {
            allDummyVlanIfsMap.put(updVlanIfs.getNodeId(), session.getDummyVlanIfsInfoList(updVlanIfs.getNodeId()));
          }
        }
      }

      if (inputData.getDeleteVlanIfs() != null) {
        for (VlanIfsDeleteVlanIf delVlanIfs : inputData.getDeleteVlanIfs()) {
          for (Nodes dbElem : nodesDbList) {
            if (delVlanIfs.getNodeId().equals(dbElem.getNode_id())) {
              if (!nodesSet.contains(dbElem)) {
                nodesSet.add(dbElem);
              }
            }
          }
          if (allVlanIfsMap.get(delVlanIfs.getNodeId()) == null) {
            allVlanIfsMap.put(delVlanIfs.getNodeId(), session.getVlanIfsList(delVlanIfs.getNodeId()));
            deleteVlanIfsMap.put(delVlanIfs.getNodeId(), session.getVlanIfsList(delVlanIfs.getNodeId()));
          }
          if (allDummyVlanIfsMap.get(delVlanIfs.getNodeId()) == null) {
            allDummyVlanIfsMap.put(delVlanIfs.getNodeId(), session.getDummyVlanIfsInfoList(delVlanIfs.getNodeId()));
          }
        }
      }

      if (inputData.getDeleteVlanIfs() != null) {
        if (!checkExpand(deleteVlanIfsMap, inputData.getDeleteVlanIfs())) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "expand function check NG."));
          return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_010308);
        }
      }

      session.startTransaction();

      if (inputData.getDeleteVlanIfs() != null) {
        for (VlanIfsDeleteVlanIf delVlanIfs : inputData.getDeleteVlanIfs()) {
          VlanIfs deleteVlanIfsDb = null;
          deleteVlanIfsDb = searchVlanIfsFromList(delVlanIfs.getVlanIfId(),
              allVlanIfsMap.get(delVlanIfs.getNodeId()));

          DummyVlanIfs deleteDummyIfsDb = null;
          deleteDummyIfsDb = searchDeleteDummyIfsFromList(delVlanIfs.getVlanIfId(),
              allDummyVlanIfsMap.get(delVlanIfs.getNodeId()));

          String vlanId = "";
          if (null != deleteVlanIfsDb) {
            if (null != inputData.getVrfId() && null == deleteVlanIfsDb.getIrb_instance_id()) {
              logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "It is not compatible with IRB."));
              return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
            }
            if (null != inputData.getVni() && null != deleteVlanIfsDb.getIrb_instance_id()) {
              logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "It corresponds to IRB."));
              return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
            }
            session.deleteVlanIfs(delVlanIfs.getNodeId(), delVlanIfs.getVlanIfId());
            vlanId = deleteVlanIfsDb.getVlan_id();
          } else if (null != deleteDummyIfsDb) {
            if (null == inputData.getVrfId()) {
              logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "It corresponds to IRB."));
              return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
            }
            session.deleteDummyVlanIfsInfo(delVlanIfs.getNodeId(), delVlanIfs.getVlanIfId());
            vlanId = deleteDummyIfsDb.getVlan_id();
          } else if (null == deleteVlanIfsDb && null == deleteDummyIfsDb) {
            response = makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_010201);
            return response;
          }

          boolean isDelete = true;
          List<VlanIfs> vlanIfsList = session.getVlanIfsList(delVlanIfs.getNodeId());
          for (VlanIfs vlifs : vlanIfsList) {
            if (vlifs.getVlan_id().equals(vlanId)
                &&  !vlifs.getVlan_if_id().equals(delVlanIfs.getVlanIfId())) {
              isDelete = false;
              break;
            }
          }
          List<DummyVlanIfs> dummyList = session.getDummyVlanIfsInfoList(delVlanIfs.getNodeId());
          for (DummyVlanIfs dvlan : dummyList) {
            if (dvlan.getVlan_id().equals(vlanId)
                &&  !dvlan.getVlan_if_id().equals(delVlanIfs.getVlanIfId())) {
              isDelete = false;
              break;
            }
          }
          if (isDelete) {
            if (null != deleteVlanIfsDb) {
              if (null != session.searchIrbInstanceInfo(delVlanIfs.getNodeId(), deleteVlanIfsDb.getVlan_id())) {
                session.deleteIrbInstanceInfo(delVlanIfs.getNodeId(), deleteVlanIfsDb.getVlan_id());
              }
            } else {
              if (null != session.searchIrbInstanceInfo(deleteDummyIfsDb.getNode_id(), deleteDummyIfsDb.getVlan_id())) {
                session.deleteIrbInstanceInfo(delVlanIfs.getNodeId(), deleteDummyIfsDb.getVlan_id());
              }
            }
          }
        }
      }

      if (null != inputData.getUpdateVlanIfs()) {
        for (RemoveUpdateVlanIfs updateVlanIfs : inputData.getUpdateVlanIfs()) {
          if (updateVlanIfs.getDummyFlag()) {
            if (null == inputData.getVrfId()) {
              logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "VRF_ID is missing."));
              return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
            }
            for (Nodes nodes : nodesSet) {
              if (nodes.getNode_id().equals(updateVlanIfs.getNodeId())) {
                if (null == nodes.getIrb_type() || nodes.getIrb_type().equals(CommonDefinitions.IRB_TYPE_SYMMETRIC)) {
                  logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Can not register a dummy vlan."));
                  return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
                }
              }
            }
            if (null != searchDeleteDummyIfsFromList(updateVlanIfs.getVlanIfId(),
                allDummyVlanIfsMap.get(updateVlanIfs.getNodeId()))) {
              return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_010307);
            }
            VlanIfs vlifs = searchVlanIfsFromList(updateVlanIfs.getVlanIfId(),
                allVlanIfsMap.get(updateVlanIfs.getNodeId()));
            if (null == vlifs) {
              logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [VlanIfs]"));
              return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_010201);
            }
            if (null == vlifs.getIrb_instance_id()) {
              logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "This VlanIfs is not compatible with IRB"));
              return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010101);
            }
            session.deleteVlanIfs(updateVlanIfs.getNodeId(), updateVlanIfs.getVlanIfId());
            DummyVlanIfs dvlan = new DummyVlanIfs();
            dvlan.setNode_id(vlifs.getNode_id());
            dvlan.setVlan_if_id(vlifs.getVlan_if_id());
            dvlan.setVlan_id(vlifs.getVlan_id());
            dvlan.setIrb_instance_id(vlifs.getIrb_instance_id());
            session.addDummyVlanIfsInfo(dvlan);
            if (null == irbInstanceMap.get(vlifs.getIrb_instance_id())) {
              irbInstanceMap.put(vlifs.getIrb_instance_id(),
                  session.searchIrbInstanceInfo(vlifs.getNode_id(), vlifs.getVlan_id()));
            }
          }
        }
      }

      L2SliceAddDelete l2SliceAddDeleteEm = EmMapper.toL2VlanIfDelete(inputData, nodesSet, allVlanIfsMap,
          allDummyVlanIfsMap, irbInstanceMap);

      AbstractMessage result = EmController.getInstance().request(l2SliceAddDeleteEm);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_010402);
      }

      session.commit();

      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      switch (dbae.getCode()) {
        case NO_DELETE_TARGET:
          response = makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_010201);
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

  /**
   * Getting dummy VLAN information from list.
   *
   * @param vlanIfId
   *          VLAN IF ID
   * @param list
   *          Dummy VLAN list
   * @return DummyVlanIfs
   */
  private DummyVlanIfs searchDeleteDummyIfsFromList(String vlanIfId, List<DummyVlanIfs> list) {
    for (DummyVlanIfs dummyIfs : list) {
      if (dummyIfs.getVlan_if_id().equals(vlanIfId)) {
        return dummyIfs;
      }
    }
    return null;
  }

  /**
   * Getting dummy VLAN information to be deleted.
   *
   * @param vlanIfId
   *          VLAN IF ID
   * @param list
   *          VLAN list
   * @return VlanIfs
   */
  private VlanIfs searchVlanIfsFromList(String vlanIfId, List<VlanIfs> list) {
    for (VlanIfs vlanIfs : list) {
      if (vlanIfs.getVlan_if_id().equals(vlanIfId)) {
        return vlanIfs;
      }
    }
    return null;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      BulkDeleteL2VlanIf inputData = (BulkDeleteL2VlanIf) getInData();

      inputData.check(new OperationType(OperationType.AllL2VlanIfRemove));

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

  /**
   * Implementing any check if required in extension function.
   *
   * @param allVlanIfsMap
   *          Entire VLAN information held by such device having deletion target VLAN
   * @param delVlans
   *          Deletion target VLAN
   * @return check result
   * @throws DBAccessException  In case abnormality occurred in DB
   */
  protected boolean checkExpand(Map<String, List<VlanIfs>> allVlanIfsMap,
      List<VlanIfsDeleteVlanIf> delVlans) throws DBAccessException {
    return true;
  }
}
