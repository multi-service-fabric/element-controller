/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.cp;

import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
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
import msf.ecmm.ope.receiver.pojo.BulkUpdateL2VlanIf;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsBulkUpdate;

/**
 * L2VLAN IF Batch Change.
 *
 */
public class AllL2VlanIfChange extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_010101 = "010101";

  /** In case the number of pieces of information for process execution is zero (Data acquisition from DBs failed in the previous step of process). */
  private static final String ERROR_CODE_010102 = "010102";

  /** In case QoS capability check result is NG. */
  private static final String ERROR_CODE_010103 = "010103";

  /** In case VLAN IF information to be changed does not exist. */
  private static final String ERROR_CODE_010201 = "010201";

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
  public AllL2VlanIfChange(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.AllL2VlanIfChange);
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

      BulkUpdateL2VlanIf inputData = (BulkUpdateL2VlanIf) getInData();

      List<Nodes> nodesDbList = session.getNodesList();
      Map<String, Nodes> nodesMap = new HashMap<String, Nodes>();
      Map<String, List<VlanIfs>> allVlanIfsMap = new HashMap<String, List<VlanIfs>>();

      session.startTransaction();

      if (inputData.getVlanIfs() != null) {

        for (VlanIfsBulkUpdate updVlanIfs : inputData.getVlanIfs()) {
          for (Nodes listElem : nodesDbList) {
            if (listElem.getNode_id().equals(updVlanIfs.getNodeId())) {
              if (!nodesMap.containsKey(listElem.getNode_id())) {
                nodesMap.put(listElem.getNode_id(), listElem);
              }
              break;
            }
          }
          allVlanIfsMap.put(updVlanIfs.getNodeId(), session.getVlanIfsList(updVlanIfs.getNodeId()));
        }

        for (VlanIfsBulkUpdate updVlanIfs : inputData.getVlanIfs()) {
          Nodes nodesDb = null;
          for (Nodes listElem : nodesDbList) {
            if (listElem.getNode_id().equals(updVlanIfs.getNodeId())) {
              nodesDb = listElem;
              break;
            }
          }
          if (nodesDb == null) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
            return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010102);
          }
          VlanIfs vlanIfsDb = null;
          for (VlanIfs listElem : allVlanIfsMap.get(updVlanIfs.getNodeId())) {
            if (listElem.getVlan_if_id().equals(updVlanIfs.getVlanIfId())) {
              vlanIfsDb = listElem;
              break;
            }
          }
          if (vlanIfsDb == null) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [VlanIfs]"));
            return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_010201);
          }

          if (!DbMapper.checkQosRemarkMenuValue(inputData.getRemarkMenu(), nodesDb.getEquipments())) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "RemarkMenu capability error."));
            return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_010103);
          }

          VlanIfs updVlanIfsDb = DbMapper.toVlanIfQosBulkChange(inputData.getRemarkMenu(), vlanIfsDb);

          session.updateVlanIfs(updVlanIfsDb);
        }
      }

      L2SliceAddDelete l2SliceAddDeleteEm = EmMapper.toL2VlanIfBulkChange(inputData, nodesMap, allVlanIfsMap);

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

      BulkUpdateL2VlanIf inputData = (BulkUpdateL2VlanIf) getInData();

      inputData.check(OperationType.AllL2VlanIfChange);

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

}
