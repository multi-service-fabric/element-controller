/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
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
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetVlanInterfaceFilter;
import msf.ecmm.ope.receiver.pojo.parts.BaseIf;
import msf.ecmm.ope.receiver.pojo.parts.GetTerms;
import msf.ecmm.ope.receiver.pojo.parts.VlanIfFilter;

/**
 * VLANIF Filter Information Acquisition.
 */
public class VlanInterfaceFilterAcquisition extends Operation {

  /** Extension Function Operation Name. */
  String operationName = "GetVlanInterfaceFilter";

  /** In case of input data check NG. */
  private static final String ERROR_CODE_500101 = "500101";
  /** In case target IF/ target device does not exist. */
  private static final String ERROR_CODE_500201 = "500201";
  /** In case abnormality has occurred at DB access. */
  private static final String ERROR_CODE_500401 = "500401";

  /**
   * Constructor.
   *
   * @param idt
   *          Input Data
   * @param ukm
   *          URI Key Information
   */
  public VlanInterfaceFilterAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(operationName).getOperationTypeId());
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    GetVlanInterfaceFilter outputData = new GetVlanInterfaceFilter();

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_500101);
    }

    String nodeId = getUriKeyMap().get(KEY_NODE_ID);
    String vlanIfId = getUriKeyMap().get(KEY_VLAN_IF_ID);

    VlanIfs dbVlanIfs = new VlanIfs();
    BaseIf baseIf = new BaseIf();
    List<GetTerms> termsList = new ArrayList<GetTerms>();
    GetTerms terms = null;

    VlanIfFilter vlanIfFilter = new VlanIfFilter();

    try (DBAccessManager session = new DBAccessManager()) {

      dbVlanIfs = session.searchVlanIfs(nodeId, vlanIfId);

      if (dbVlanIfs == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [VlanIfs]."));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_500201);
      }

      vlanIfFilter.setNodeId(nodeId);
      vlanIfFilter.setVlanIfId(vlanIfId);

      if (!(dbVlanIfs.getPhysical_if_id() == null)) {
        baseIf.setIfType(IF_TYPE_PHYSICAL_IF);
        baseIf.setIfId(dbVlanIfs.getPhysical_if_id());

      } else if (!(dbVlanIfs.getLag_if_id() == null)) {
        LagIfs lagIfs = session.searchLagIfs(nodeId, dbVlanIfs.getLag_if_id());
        if (lagIfs == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [LagIfs]."));
          return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_500201);
        }
        baseIf.setIfType(IF_TYPE_LAG_IF);
        baseIf.setIfId(lagIfs.getFc_lag_if_id());

      } else {
        baseIf.setIfType(IF_TYPE_BREAKOUT_IF);
        baseIf.setIfId(dbVlanIfs.getBreakout_if_id());
      }
      vlanIfFilter.setBaseIf(baseIf);

      AclConf aclConf = new AclConf();
      aclConf = session.getAclConf(nodeId, vlanIfId, IF_TYPE_VLAN_IFS);
      if (aclConf == null) {
        vlanIfFilter.setTerms(null);
        outputData.setVlanIfFilter(vlanIfFilter);
        response = makeSuccessResponse(RESP_OK_200, outputData);
        return response;
      }

      if (aclConf.getAclConfDetailList() != null) {
        for (AclConfDetail aclConfDetail : aclConf.getAclConfDetailList()) {
          terms = getTermList(aclConfDetail);
          termsList.add(terms);
        }
      }
      vlanIfFilter.setTerms((ArrayList<GetTerms>) termsList);

      outputData.setVlanIfFilter(vlanIfFilter);

      response = makeSuccessResponse(RESP_OK_200, outputData);

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_500401);
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
      if (!(getUriKeyMap().containsKey(KEY_VLAN_IF_ID)) || getUriKeyMap().get(KEY_VLAN_IF_ID) == null) {
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
