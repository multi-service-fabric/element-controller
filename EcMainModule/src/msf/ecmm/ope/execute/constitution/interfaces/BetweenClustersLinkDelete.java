/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.LagMembers;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.BetweenClustersLinkAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.DeleteBetweenClustersLink;

/**
 * Inter-Cluster Link Deletion Class Definition<br>
 * Deleting inter-cluster link.
 *
 */
public class BetweenClustersLinkDelete extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_360101 = "360101";

  /** In case the number of pieces of information of process execution is zero. */
  private static final String ERROR_CODE_360102 = "360102";

  /** In case there is no IF information of which IP address is to be changed. */
  private static final String ERROR_CODE_360201 = "360201";

  /** In case specified IF is in use. */
  private static final String ERROR_CODE_360302 = "360302";

  /** Disconnection or connection timeout with EM has occurred while requesting inter-cluster link generation to EM. */
  private static final String ERROR_CODE_360401 = "360401";

  /** Error has occurred from EM while requesting inter-cluster link generation (error response received). */
  private static final String ERROR_CODE_360402 = "360402";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_360403 = "360403";

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
  public BetweenClustersLinkDelete(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.BetweenClustersLinkDelete);
  }

  /*
   * (Non-Javadoc)
   *
   * @see msf.ecmm.ope.execute.Operation#execute()
   */
  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_360101);
    }

    DeleteBetweenClustersLink inputData = (DeleteBetweenClustersLink) getInData();
    String nodeId = inputData.getTargetIf().getNodeId();
    String ifType = inputData.getTargetIf().getIfType();
    String ifId = inputData.getTargetIf().getIfId();

    try (DBAccessManager session = new DBAccessManager()) {

      Nodes nodesDb = session.searchNodes(nodeId, null);
      if (nodesDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db."));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_360102);
      }

      Set<PhysicalIfs> physicalIfsSetDb = nodesDb.getPhysicalIfsList();
      PhysicalIfs physicalIfsDb = null;
      if (ifType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
        if (physicalIfsSetDb == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No data for update DB."));
          return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_360201);
        } else {
          for (PhysicalIfs physiElem : physicalIfsSetDb) {
            if (physiElem.getPhysical_if_id().equals(ifId)) {
              physicalIfsDb = physiElem;
              break;
            }
          }

          if (physicalIfsDb == null) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No data for update DB."));
            return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_360201);
          } else {
            if (physicalIfsDb.getBreakoutIfsList() != null) {
              for (BreakoutIfs boElem : physicalIfsDb.getBreakoutIfsList()) {
                if (boElem.getPhysical_if_id().equals(ifId)) {
                  logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "already in use as breakoutIF."));
                  return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_360302);
                }
              }
            }
          }
        }
      }

      Set<LagIfs> lagIfsSetDb = nodesDb.getLagIfsList();
      LagIfs lagIfsDb = null;
      if (ifType.equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
        if (lagIfsSetDb == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No data for update DB."));
          return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_360201);
        } else {
          for (LagIfs lagElem : lagIfsSetDb) {
            if (lagElem.getFc_lag_if_id().equals(ifId)) {
              lagIfsDb = lagElem;
              break;
            }
          }
          if (lagIfsDb == null) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No data for update DB."));
            return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_360201);
          }
        }
      }

      BreakoutIfs breakoutIfsDb = null;
      if (ifType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
        if (physicalIfsSetDb == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No data for update DB."));
          return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_360201);
        } else {
          physiSetLoop: for (PhysicalIfs physiElem : physicalIfsSetDb) {
            if (physiElem.getBreakoutIfsList() != null) {
              for (BreakoutIfs boElem : physiElem.getBreakoutIfsList()) {
                if (boElem.getBreakout_if_id().equals(ifId)) {
                  breakoutIfsDb = boElem;
                  break physiSetLoop;
                }
              }
            }
          }
          if (breakoutIfsDb == null) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No data for update DB."));
            return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_360201);
          }
        }
      }

      if (isUseLagMembers(lagIfsSetDb, ifType, ifId)) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "already in use as LAGMember."));
        return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_360302);
      }
      if (isUseAsVlanIfs(session.getVlanIfsList(nodeId), ifType, ifId)) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "already in use as VLAN IFs."));
        return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_360302);
      }

      session.startTransaction();

      PhysicalIfs physicalIfs = DbMapper.toPhysicalIfIpAddrChange(physicalIfsDb, null, null);
      LagIfs lagIfs = DbMapper.toLagIfIpAddrChange(lagIfsDb, null, null);
      BreakoutIfs breakoutIfs = DbMapper.toBreakoutIfIpAddrChange(breakoutIfsDb, null, null);
      session.updateNodeIfIpAddress(physicalIfs, lagIfs, null, breakoutIfs);

      BetweenClustersLinkAddDelete toEmData = EmMapper.toBetweenClustersLinkDelete(nodesDb, ifType, ifId);

      AbstractMessage result = EmController.getInstance().request(toEmData);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_360402);
      }

      session.commit();

      response = makeSuccessResponse(RESP_NOCONTENTS_204, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      switch (dbae.getCode()) {
        case NO_UPDATE_TARGET:
          response = makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_360201);
          break;
        case COMMIT_FAILURE:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900404);
          break;
        default:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_360403);
          break;
      }
    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_360401);

    } catch (IllegalArgumentException iae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), iae);
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_360101);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  /*
   * (Non-Javadoc)
   *
   * @see msf.ecmm.ope.execute.Operation#checkInData()
   */
  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      DeleteBetweenClustersLink inputData = (DeleteBetweenClustersLink) getInData();

      inputData.check(OperationType.BetweenClustersLinkDelete);

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

  /**
   * Return if the IF is used as LAG member or not.
   *
   * @param lagIfsSetDb
   *          LAG information table (DB)
   * @param ifId
   *          IF ID
   * @param ifType
   *          IF type
   * @return true: in use, false: not used
   *
   */
  private boolean isUseLagMembers(Set<LagIfs> lagIfsSetDb, String ifType, String ifId) {
    if (lagIfsSetDb != null) {
      for (LagIfs lagElem : lagIfsSetDb) {
        if (lagElem.getLagMembersList() != null) {
          for (LagMembers memElem : lagElem.getLagMembersList()) {
            if (CommonDefinitions.IF_TYPE_PHYSICAL_IF.equals(ifType)) {
              if ((memElem.getPhysical_if_id() != null) && memElem.getPhysical_if_id().equals(ifId)) {
                return true;
              }
            } else if (CommonDefinitions.IF_TYPE_BREAKOUT_IF.equals(ifType)) {
              if ((memElem.getBreakout_if_id() != null) && memElem.getBreakout_if_id().equals(ifId)) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }

  /**
   * Return if the IF is used as VLANIF or not.
   *
   * @param vlanIfsListDb
   *          VLANIF information table (DB)
   * @param ifId
   *          IF ID
   * @param ifType
   *          IF type
   * @return true: in use, false: not used
   */
  private boolean isUseAsVlanIfs(List<VlanIfs> vlanIfsListDb, String ifType, String ifId) {
    if (vlanIfsListDb != null) {
      for (VlanIfs vlanElem : vlanIfsListDb) {
        if (CommonDefinitions.IF_TYPE_PHYSICAL_IF.equals(ifType)) {
          if ((vlanElem.getPhysical_if_id() != null) && vlanElem.getPhysical_if_id().equals(ifId)) {
            return true;
          }
        } else if (CommonDefinitions.IF_TYPE_LAG_IF.equals(ifType)) {
          if ((vlanElem.getLag_if_id() != null) && vlanElem.getLag_if_id().equals(ifId)) {
            return true;
          }
        } else if (CommonDefinitions.IF_TYPE_BREAKOUT_IF.equals(ifType)) {
          if ((vlanElem.getBreakout_if_id() != null) && vlanElem.getBreakout_if_id().equals(ifId)) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
