/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import msf.ecmm.emctrl.pojo.CeLagIfsChange;
import msf.ecmm.emctrl.pojo.InternalLinkLagIfsChange;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.UpdateLagInterface;
import msf.ecmm.ope.receiver.pojo.parts.PhysicalIfsCreateLagIf;

/**
 * Changing LagIF.
 */
public class LagIfInfoChange extends Operation {

  /** In case input paramter check is NG. */
  private static final String ERROR_CODE_580101 = "580101";

  /**
   * In case LagIF modification information does not exist. 
   * (eg. data cannot be received from DB becuse of failure in previous processing phase).
   */
  private static final String ERROR_CODE_580102 = "580102";

  /** In case link member does not exist as a result of decreased speed. */
  private static final String ERROR_CODE_580103 = "580103";

  /** In case there is no Lag information to be increased/decreased in speed. */
  private static final String ERROR_CODE_580201 = "580201";

  /** In case IF with increased speed  has already been used. */
  private static final String ERROR_CODE_580302 = "580302";

  /** In case disconnection with EM has been detected or timeout occurred while requesting LagIF change to EM. */
  private static final String ERROR_CODE_580401 = "580401";

  /** In case failure occurred during DB access. */
  private static final String ERROR_CODE_580402 = "580402";

  /** In case error has been received from EM while requesting to EM. */
  private static final String ERROR_CODE_580403 = "580403";

  /** In case DB commit failed after EM access is successful. */
  private static final String ERROR_CODE_900404 = "900404";

  /** Distributing Em mapping. */
  private static final String INTERNAL_LINK = "internal_link";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public LagIfInfoChange(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.LagIfChange);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_580101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      String nodeId = getUriKeyMap().get(KEY_NODE_ID);
      String fcLagIfId = getUriKeyMap().get(KEY_LAG_IF_ID);

      String lagIfId = null;

      Nodes nodes = session.searchNodes(nodeId, null);
      if (nodes == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db."));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_580102);
      }

      for (LagIfs lagIfs : nodes.getLagIfsList()) {
        if (lagIfs.getFc_lag_if_id().equals(fcLagIfId)) {
          lagIfId = lagIfs.getLag_if_id();
        }
      }

      LagIfs lagIfs = session.searchLagIfs(nodeId, lagIfId);
      if (lagIfs == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [LagIfs]."));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_580201);
      }

      Integer minLinkNum = lagIfs.getMinimum_link_num();

      List<VlanIfs> vlanIfsList = session.getVlanIfsList(nodeId);

      boolean addFlg = true;

      UpdateLagInterface inputData = (UpdateLagInterface) getInData();

      List<PhysicalIfs> physicalIfsList = new ArrayList<PhysicalIfs>();
      List<BreakoutIfs> breakoutIfsList = new ArrayList<BreakoutIfs>();

      List<LagMembers> lagMembersList = new ArrayList<LagMembers>();

      List<String> ifIdCheckList = new ArrayList<String>();
      boolean notExistIfFlg = false;
      boolean foundIfFlg = false;

      for (PhysicalIfsCreateLagIf physicalIfsCreateLagIf : inputData.getLagIfs().getPhysicalIfs()) {
        if (notExistIfFlg) {
          break;
        }
        foundIfFlg = false;
        LagMembers lagMembers = new LagMembers();
        lagMembers.setNode_id(nodeId);
        lagMembers.setLag_if_id(lagIfs.getLag_if_id());
        if (physicalIfsCreateLagIf.getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
          for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
            if (!(physicalIfs.getPhysical_if_id().equals(physicalIfsCreateLagIf.getIfId()))) {
              notExistIfFlg = true;
            } else {

              physicalIfsList.add(physicalIfs);

              lagMembers.setPhysical_if_id(physicalIfs.getPhysical_if_id());
              lagMembersList.add(lagMembers);

              ifIdCheckList.add(physicalIfs.getPhysical_if_id());
              notExistIfFlg = false;
              foundIfFlg = true;
              break;
            }
          }

        } else {
          for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
            if (foundIfFlg) {
              break;
            }
            if (!(physicalIfs.getBreakoutIfsList() == null || physicalIfs.getBreakoutIfsList().isEmpty())) {
              for (BreakoutIfs breakoutIfs : physicalIfs.getBreakoutIfsList()) {
                if (!(breakoutIfs.getBreakout_if_id().equals(physicalIfsCreateLagIf.getIfId()))) {
                  notExistIfFlg = true;
                } else {
                  breakoutIfsList.add(breakoutIfs);

                  lagMembers.setBreakout_if_id(breakoutIfs.getBreakout_if_id());
                  lagMembersList.add(lagMembers);

                  ifIdCheckList.add(breakoutIfs.getBreakout_if_id());
                  notExistIfFlg = false;
                  foundIfFlg = true;
                  break;
                }
              }
            }
          }
        }
      }
      if (notExistIfFlg) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "IFID not found."));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_580102);
      }

      if (inputData.getAction().equals(CommonDefinitions.OPERATION_TYPE_ADD)) {
        boolean inUseFlg = false;
        if (vlanIfsList != null) {
          for (VlanIfs vlanIfs : vlanIfsList) {
            String checkContainsIfId = null;
            if (vlanIfs.getPhysical_if_id() != null) {
              checkContainsIfId = vlanIfs.getPhysical_if_id();
            } else if (vlanIfs.getBreakout_if_id() != null) {
              checkContainsIfId = vlanIfs.getBreakout_if_id();
            } else {
              continue;
            }
            if ((ifIdCheckList.contains(checkContainsIfId))) {
              inUseFlg = true;
              break;
            }
          }
        }

        if (!inUseFlg) {
          for (PhysicalIfs physicalIfs : physicalIfsList) {
            if (physicalIfs.getIpv4_address() != null) {
              inUseFlg = true;
              break;
            }
          }
        }

        if (!inUseFlg) {
          for (BreakoutIfs breakoutIfs : breakoutIfsList) {
            if (breakoutIfs.getIpv4_address() != null) {
              inUseFlg = true;
              break;
            }
          }
        }

        if (nodes.getLagIfsList() != null) {
          for (LagIfs lagIfsCheck : nodes.getLagIfsList()) {
            if (lagIfsCheck.getLagMembersList() != null) {
              for (LagMembers members : lagIfsCheck.getLagMembersList()) {
                for (PhysicalIfsCreateLagIf phys : inputData.getLagIfs().getPhysicalIfs()) {
                  if (phys.getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
                    if ((members.getPhysical_if_id() != null) && members.getPhysical_if_id().equals(phys.getIfId())) {
                      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041,
                          "PhysicalIF is used in the other LagMember."));
                      return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_580302);
                    }
                  } else if (phys.getIfType().equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
                    if ((members.getBreakout_if_id() != null) && members.getBreakout_if_id().equals(phys.getIfId())) {
                      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041,
                          "BreakoutIF is used in the other LagMember."));
                      return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_580302);
                    }
                  }
                }
              }
            }
          }
        }

        if (inUseFlg) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "The IFID is already being used."));
          return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_580302);
        }
        minLinkNum += lagMembersList.size();

      } else {

        addFlg = false;

        if ((minLinkNum - lagMembersList.size()) <= 0) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "LinkMember becomes 0."));
          return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_580103);
        }
        minLinkNum -= lagMembersList.size();
      }

      LagIfs lagIfsDb = DbMapper.toLagIfChange(inputData, minLinkNum, lagIfs, physicalIfsList, breakoutIfsList,
          lagMembersList);

      session.startTransaction();

      session.updateLagIfs(lagIfsDb, addFlg);

      AbstractMessage result = null;
      if (inputData.getLagIfs().getLinkType().equals(INTERNAL_LINK)) {
        InternalLinkLagIfsChange internalLinkLagIfsChange = EmMapper.toInternalLinkLagIfsChange(inputData.getAction(),
            nodes, lagIfs, minLinkNum, physicalIfsList, breakoutIfsList);
        result = EmController.getInstance().request(internalLinkLagIfsChange);
      } else {
        CeLagIfsChange ceLagIfsChange = EmMapper.toLagIfsChange(inputData.getAction(), nodes, lagIfs, minLinkNum,
            physicalIfsList, breakoutIfsList);
        result = EmController.getInstance().request(ceLagIfsChange);
      }

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_580403);
      }

      session.commit();

      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      switch (dbae.getCode()) {
        case COMMIT_FAILURE:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900404);
          break;
        default:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_580402);
          break;
      }
    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_580401);

    } catch (IllegalArgumentException iae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), iae);
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_580101);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      UpdateLagInterface inputData = (UpdateLagInterface) getInData();

      inputData.check(new OperationType(OperationType.LagIfChange));

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    if (getUriKeyMap() == null) {
      result = false;
    } else {
      if (!(getUriKeyMap().containsKey(KEY_NODE_ID)) || getUriKeyMap().get(KEY_NODE_ID) == null) {
        result = false;
      }
      if (!(getUriKeyMap().containsKey(KEY_LAG_IF_ID)) || getUriKeyMap().get(KEY_LAG_IF_ID) == null) {
        result = false;
      }
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

}
