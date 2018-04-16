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
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.L3SliceAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.BulkDeleteL3VlanIf;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfsDeleteVlanIf;

/**
 * L3VLAN IF Batch Deletion.
 */
public class AllL3VlanIfRemove extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_010101 = "010101";

  /** In case the information of VLAN IF to be deleted does not exist. */
  private static final String ERROR_CODE_010201 = "010201";

  /** Disconnection or connection timeout with EM has occurred while requesting to EM. */
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
  public AllL3VlanIfRemove(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.AllL3VlanIfRemove);
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

      BulkDeleteL3VlanIf inputData = (BulkDeleteL3VlanIf) getInData();

      List<Nodes> nodesDbList = session.getNodesList();
      Set<Nodes> nodesSet = new HashSet<Nodes>();
      Map<String, List<VlanIfs>> allVlanIfsMap = new HashMap<String, List<VlanIfs>>();

      for (VlanIfsDeleteVlanIf vlanIfs : inputData.getVlanIfs()) {
        for (Nodes dbElem : nodesDbList) {
          if (vlanIfs.getNodeId().equals(dbElem.getNode_id())) {
            if (!nodesSet.contains(dbElem)) {
              nodesSet.add(dbElem);
            }
            break;
          }
        }
        allVlanIfsMap.put(vlanIfs.getNodeId(), session.getVlanIfsList(vlanIfs.getNodeId()));
      }

      session.startTransaction();

      for (VlanIfsDeleteVlanIf vlanIfs : inputData.getVlanIfs()) {
        session.deleteVlanIfs(vlanIfs.getNodeId(), vlanIfs.getVlanIfId());
      }

      L3SliceAddDelete l3SliceAddDeleteEm = EmMapper.toL3VlanIfDelete(inputData, nodesSet, allVlanIfsMap);

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

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      BulkDeleteL3VlanIf inputData = (BulkDeleteL3VlanIf) getInData();

      inputData.check(OperationType.AllL3VlanIfRemove);

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

}
