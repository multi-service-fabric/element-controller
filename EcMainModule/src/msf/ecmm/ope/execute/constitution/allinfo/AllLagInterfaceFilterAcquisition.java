/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.allinfo;

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
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.receiver.ReceiverDefinitions;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetLagInterfaceFilterList;
import msf.ecmm.ope.receiver.pojo.parts.GetTerms;
import msf.ecmm.ope.receiver.pojo.parts.LagIfFilter;
import msf.ecmm.ope.receiver.pojo.parts.LagMember;
import msf.ecmm.ope.receiver.pojo.parts.LagMembersBreakoutIfs;
import msf.ecmm.ope.receiver.pojo.parts.LagMembersPhysicalIfs;

/**
 * Getting list of LagIF filter information.
 */
public class AllLagInterfaceFilterAcquisition extends Operation {

  /** Operation name of extended function. */
  String operationName = "GetLagInterfaceFilterList";

  /** In case of the entry data check NG. */
  private static final String ERROR_CODE_530101 = "530101";
  /** In case target device does not exist. */
  private static final String ERROR_CODE_530201 = "530201";
  /** In case abnormality has occurred at DB access. */
  private static final String ERROR_CODE_530401 = "530401";

  /**
   * Constructor.
   *
   * @param idt
   *          Input Data
   * @param ukm
   *          URI Key Information
   */
  public AllLagInterfaceFilterAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(operationName).getOperationTypeId());
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    GetLagInterfaceFilterList outputData = new GetLagInterfaceFilterList();

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_530101);
    }

    String nodeId = getUriKeyMap().get(KEY_NODE_ID);

    try (DBAccessManager session = new DBAccessManager()) {

      Nodes nodes = session.searchNodes(nodeId, null);

      if (nodes == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]."));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_530201);
      }

      List<AclConf> aclConfList = session.getAclConfList(nodeId);

      for (AclConf aclConf : aclConfList) {
        if (aclConf.getLag_if_id() != null) {
          LagIfFilter lagIfFilter = new LagIfFilter();

          lagIfFilter.setNodeId(nodeId);

          LagIfs lagIfs = session.searchLagIfs(nodeId, aclConf.getLag_if_id());
          if (lagIfs == null) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [LagIfs]."));
            return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_530201);
          }
          lagIfFilter.setLagIfId(lagIfs.getFc_lag_if_id());

          List<LagMembersPhysicalIfs> lagMembersPhysicalIfsList = new ArrayList<LagMembersPhysicalIfs>();
          List<LagMembersBreakoutIfs> lagMembersBreakoutIfsList = new ArrayList<LagMembersBreakoutIfs>();
          for (LagMembers lagMembers : lagIfs.getLagMembersList()) {
            if (lagMembers.getPhysical_if_id() != null) {
              LagMembersPhysicalIfs lagMembersPhysicalIfs = new LagMembersPhysicalIfs();
              lagMembersPhysicalIfs.setPhysicalIfId(lagMembers.getPhysical_if_id());
              lagMembersPhysicalIfsList.add(lagMembersPhysicalIfs);
            } else {
              LagMembersBreakoutIfs lagMembersBreakoutIfs = new LagMembersBreakoutIfs();
              lagMembersBreakoutIfs.setBreakoutIfId(lagMembers.getBreakout_if_id());
              BreakoutIfs breakoutIfs = session.searchBreakoutIf(nodeId, lagMembers.getBreakout_if_id());
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

          for (AclConfDetail aclConfDetail : aclConf.getAclConfDetailList()) {
            lagIfFilter.getTerms().add(getTermList(aclConfDetail));
          }
          outputData.getLagIfFilterList().add(lagIfFilter);
        }
      }

      response = makeSuccessResponse(ReceiverDefinitions.RESP_OK_200, outputData);

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_530401);
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

    }

    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

  /**
	* Getting Terms (for FC) from the ACL advanced setting information.
   *
   * @param aclConfDetail
   *          ACL configuration information details
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
