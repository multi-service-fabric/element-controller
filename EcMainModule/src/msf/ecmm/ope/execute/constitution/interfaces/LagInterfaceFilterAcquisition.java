/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.AclConf;
import msf.ecmm.db.pojo.AclConfDetail;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.LagMembers;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetLagInterfaceFilter;
import msf.ecmm.ope.receiver.pojo.parts.GetTerms;
import msf.ecmm.ope.receiver.pojo.parts.LagIfFilter;
import msf.ecmm.ope.receiver.pojo.parts.LagMember;
import msf.ecmm.ope.receiver.pojo.parts.LagMembersBreakoutIfs;
import msf.ecmm.ope.receiver.pojo.parts.LagMembersPhysicalIfs;

/**
 * LagIF Filter Information Acquisition.
 */
public class LagInterfaceFilterAcquisition extends Operation {

  /** Extension Function Operation Name. */
  String operationName = "GetLagInterfaceFilter";

  /** In case of input data check NG. */
  private static final String ERROR_CODE_540101 = "540101";
  /** In case target IF/ target device does not exist. */
  private static final String ERROR_CODE_540201 = "540201";
  /** In case abnormality has occurred at DB access. */
  private static final String ERROR_CODE_540401 = "540401";

  /**
   * Constructor.
   *
   * @param idt
   *          Input Data
   * @param ukm
   *          URI Key Information
   */
  public LagInterfaceFilterAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(operationName).getOperationTypeId());
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    GetLagInterfaceFilter outputData = new GetLagInterfaceFilter();

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_540101);
    }

    String nodeId = getUriKeyMap().get(KEY_NODE_ID); 
    String fcLagIfId = getUriKeyMap().get(KEY_LAG_IF_ID); 
    String lagIfId = null;

    LagIfs dbLagIfs = null;

    LagIfFilter lagIfFilter = new LagIfFilter();

    try (DBAccessManager session = new DBAccessManager()) {

      List<LagIfs> lagIfslist = session.getLagIfsList(nodeId);

      if (lagIfslist != null) {
        for (LagIfs lagIfs : lagIfslist) {
          if (lagIfs.getFc_lag_if_id().equals(fcLagIfId)) {
            lagIfId = lagIfs.getLag_if_id();
            break;
          }
        }
      }

      dbLagIfs = session.searchLagIfs(nodeId, lagIfId);

      if (dbLagIfs == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [LagIfs]."));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_540201);
      }

      lagIfFilter.setNodeId(nodeId);
      lagIfFilter.setLagIfId(fcLagIfId);

      AclConf aclConf = session.getAclConf(nodeId, lagIfId, IF_TYPE_LAG_IFS);
      if (aclConf == null) {

        List<LagMembersPhysicalIfs> lagMembersPhysicalIfsList = new ArrayList<LagMembersPhysicalIfs>();
        List<LagMembersBreakoutIfs> lagMembersBreakoutIfsList = new ArrayList<LagMembersBreakoutIfs>();
        for (LagMembers dbLagMember : dbLagIfs.getLagMembersList()) {
          if (dbLagMember.getPhysical_if_id() != null) {
            LagMembersPhysicalIfs lagMembersPhysicalIfs = new LagMembersPhysicalIfs();
            lagMembersPhysicalIfs.setPhysicalIfId(dbLagMember.getPhysical_if_id());
            lagMembersPhysicalIfsList.add(lagMembersPhysicalIfs);
          } else {
            LagMembersBreakoutIfs lagMembersBreakoutIfs = new LagMembersBreakoutIfs();
            lagMembersBreakoutIfs.setBreakoutIfId(dbLagMember.getBreakout_if_id());
            BreakoutIfs breakoutIfs = session.searchBreakoutIf(nodeId, dbLagMember.getBreakout_if_id());
            lagMembersBreakoutIfs.setPhysicalIfId(breakoutIfs.getPhysical_if_id());
            lagMembersBreakoutIfsList.add(lagMembersBreakoutIfs);
          }
        }
        if (lagMembersPhysicalIfsList.size() == 0) {
          lagMembersPhysicalIfsList = null;
        }
        if (lagMembersBreakoutIfsList.size() == 0) {
          lagMembersBreakoutIfsList = null;
        }
        LagMember lagMember = new LagMember();
        lagMember.setPhysicalIfs(lagMembersPhysicalIfsList);
        lagMember.setBreakoutIfs(lagMembersBreakoutIfsList);

        lagIfFilter.setLagmember(lagMember);
        lagIfFilter.setTerms(null);

        outputData.setLagIfFilter(lagIfFilter);
        response = makeSuccessResponse(RESP_OK_200, outputData);
        return response;
      }

      LagIfs lagIfs = session.searchLagIfs(nodeId, aclConf.getLag_if_id());
      if (lagIfs == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [LagIfs]."));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_540201);
      }

      List<LagMembersPhysicalIfs> lagMembersPhysicalIfsList = new ArrayList<LagMembersPhysicalIfs>();
      List<LagMembersBreakoutIfs> lagMembersBreakoutIfsList = new ArrayList<LagMembersBreakoutIfs>();
      for (LagMembers dbLagMember : lagIfs.getLagMembersList()) {
        if (dbLagMember.getPhysical_if_id() != null) {
          LagMembersPhysicalIfs lagMembersPhysicalIfs = new LagMembersPhysicalIfs();
          lagMembersPhysicalIfs.setPhysicalIfId(dbLagMember.getPhysical_if_id());
          lagMembersPhysicalIfsList.add(lagMembersPhysicalIfs);
        } else {
          LagMembersBreakoutIfs lagMembersBreakoutIfs = new LagMembersBreakoutIfs();
          lagMembersBreakoutIfs.setBreakoutIfId(dbLagMember.getBreakout_if_id());
          BreakoutIfs breakoutIfs = session.searchBreakoutIf(nodeId, dbLagMember.getBreakout_if_id());
          lagMembersBreakoutIfs.setPhysicalIfId(breakoutIfs.getPhysical_if_id());
          lagMembersBreakoutIfsList.add(lagMembersBreakoutIfs);
        }
      }
      if (lagMembersPhysicalIfsList.size() == 0) {
        lagMembersPhysicalIfsList = null;
      }
      if (lagMembersBreakoutIfsList.size() == 0) {
        lagMembersBreakoutIfsList = null;
      }
      LagMember lagMember = new LagMember();
      lagMember.setPhysicalIfs(lagMembersPhysicalIfsList);
      lagMember.setBreakoutIfs(lagMembersBreakoutIfsList);

      lagIfFilter.setLagmember(lagMember);

      if (aclConf.getAclConfDetailList() != null) {
        for (AclConfDetail aclConfDetail : aclConf.getAclConfDetailList()) {
          GetTerms terms = new GetTerms();
          terms = getTermList(aclConfDetail);
          lagIfFilter.getTerms().add(terms);
        }
      }

      outputData.setLagIfFilter(lagIfFilter);

      response = makeSuccessResponse(RESP_OK_200, outputData);

    } catch (

    DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_540401);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean checkResult = true;

    if (getUriKeyMap() == null) {
      checkResult = false;
    } else {
      if (!(getUriKeyMap().containsKey(KEY_NODE_ID)) || getUriKeyMap().get(KEY_NODE_ID) == null) {
        checkResult = false;
      }
      if (!(getUriKeyMap().containsKey(KEY_LAG_IF_ID)) || getUriKeyMap().get(KEY_LAG_IF_ID) == null) {
        checkResult = false;
      }
    }

    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

  /**
	* Getting the LAG Member Information (for FC) from the LAG Member Information and the Breakout Information.
   *
   * @param dbLagMembers
   *          LAG Member Information
   * @param dbBreakoutIfs
   *          breakoutIF Information
   * @return lagMember
   *
   */
  public LagMember getLagMember(LagMembers dbLagMembers, BreakoutIfs dbBreakoutIfs) {
    LagMembersPhysicalIfs lagMembersPhysicalIfs = new LagMembersPhysicalIfs();
    lagMembersPhysicalIfs.setPhysicalIfId(dbLagMembers.getPhysical_if_id());

    LagMembersBreakoutIfs lagMembersBreakoutIfs = new LagMembersBreakoutIfs();
    lagMembersBreakoutIfs.setPhysicalIfId(dbBreakoutIfs.getPhysical_if_id());
    lagMembersBreakoutIfs.setBreakoutIfId(dbBreakoutIfs.getBreakout_if_id());

    LagMember lagMember = new LagMember();
    List<LagMembersPhysicalIfs> lagMembersPhysicalIfsList = new ArrayList<LagMembersPhysicalIfs>();
    lagMember.setPhysicalIfs(lagMembersPhysicalIfsList);
    List<LagMembersBreakoutIfs> lagMembersBreakoutIfsList = new ArrayList<LagMembersBreakoutIfs>();
    lagMember.setBreakoutIfs(lagMembersBreakoutIfsList);

    return lagMember;
  }

  /**
	* Getting Terms (for FC) from the ACL advanced setting information.
   *
   * @param aclConfDetail
   *          ACL configuration details information
   * @return terms
   *
   */
  public GetTerms getTermList(AclConfDetail aclConfDetail) {
    GetTerms terms = new GetTerms();
    terms.setTermName(aclConfDetail.getTerm_name());
    terms.setAction(aclConfDetail.getAction());
    terms.setDirection(aclConfDetail.getDirection());
    terms.setSourceMacAddress(aclConfDetail.getSource_mac_address());
    terms.setDestMacAddress(aclConfDetail.getDestination_mac_address());
    terms.setSourceIpAddress(aclConfDetail.getSource_ip_address());
    terms.setDestIpAddress(aclConfDetail.getDestination_ip_address());
    terms.setProtocol(aclConfDetail.getProtocol());
    if (aclConfDetail.getSource_port() == -1) {
      terms.setSourcePort(null);
    } else {
      terms.setSourcePort(String.valueOf(aclConfDetail.getSource_port()));
    }
    if (aclConfDetail.getDestination_port() == -1) {
      terms.setDestPort(null);
    } else {
      terms.setDestPort(String.valueOf(aclConfDetail.getDestination_port()));
    }
    return terms;
  }
}
