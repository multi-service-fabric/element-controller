/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.AclConf;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.AddAclSetting;
import msf.ecmm.emctrl.pojo.parts.AclFilter;
import msf.ecmm.emctrl.pojo.parts.DeviceLeafAcl;
import msf.ecmm.emctrl.pojo.parts.Term;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.parts.DeleteFilters;
import msf.ecmm.ope.receiver.pojo.parts.IfFilter;
import msf.ecmm.ope.receiver.pojo.parts.Terms;

/**
 * Filter Delete.
 */
public class DeleteFilter extends Operation {

  /** In case of input data check NG. */
  private static final String ERROR_CODE_480101 = "480101";

  /** Target IF/ target device does not exist. */
  private static final String ERROR_CODE_480201 = "480201";

  /** The filter to be deleted is not registered. */
  private static final String ERROR_CODE_480202 = "480202";

  /** Disconnection with EM/ timeout has occurred during EM requesting,  */
  private static final String ERROR_CODE_480401 = "480401";

  /** Error occurred from EM during EM requesting, (Error response was received). */
  private static final String ERROR_CODE_480402 = "480402";

  /** In case DB commitment failed after normal EM access. */
  private static final String ERROR_CODE_900403 = "900403";

  /** In case abnormality has occurred at DB access. */
  private static final String ERROR_CODE_480404 = "480404";

  /** Other exceptions. */
  private static final String ERROR_CODE_480499 = "480499";

  /** Filter Name. Fixing acl-filter. */
  private static final String ACL_FILTER_NAME = "acl-filter";

  private static final String IF_TYPE_PHYSICAL = "physical-ifs";
  private static final String IF_TYPE_LAG = "lag-ifs";
  private static final String IF_TYPE_VLAN = "vlan-ifs";

  /** Filter Delete  Extensiond Function Operation Name. */
  private static final String DELETE_FILTER = "DeleteFilter";

  /**
   * Constructor.
   *
   * @param idt
   *          Input Data
   * @param ukm
   *          URI Key Information
   */
  public DeleteFilter(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(DELETE_FILTER).getOperationTypeId());
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_480101);
    }
    DeleteFilters inputData = (DeleteFilters) getInData();

    if (operationMixCheck(inputData.getIfFilter().getTerms())) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Add mixed in operation."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_480101);
    }

    try (DBAccessManager session = new DBAccessManager()) {
      String nodeId = getUriKeyMap().get(CommonDefinitions.KEY_NODE_ID);
      String ifType = getUriKeyMap().get(CommonDefinitions.KEY_IF_TYPE);
      String ifId = getUriKeyMap().get(CommonDefinitions.KEY_IF_ID);

      Nodes nodeDb = null;
      nodeDb = session.searchNodes(nodeId, null);
      if (null == nodeDb) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Node not found."));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_480201);
      }

      if (ifType == IF_TYPE_PHYSICAL) {
        if (null == nodeDb.getPhysicalIfsList()) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "PhysicalIfs not found."));
          return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_480201);
        }
      } else if (ifType == IF_TYPE_LAG) {
        List<LagIfs> lagIfslist = session.getLagIfsList(nodeId);

        if (lagIfslist != null) {
          for (LagIfs lagIfs : lagIfslist) {
            if (lagIfs.getFc_lag_if_id().equals(ifId)) {
              ifId = lagIfs.getLag_if_id();
              break;
            }
          }
        }
        if (null == nodeDb.getLagIfsList()) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "tLagIfs not found."));
          return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_480201);
        }
      } else if (ifType == IF_TYPE_VLAN) {
        VlanIfs vlanIfs = session.searchVlanIfs(nodeId, ifId);
        if (null == vlanIfs) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "VlanIfs not found."));
          return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_480201);
        }
      }

      AclConf aclConf = session.getAclConf(nodeId, ifId, ifType);
      if (null == aclConf) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "AclInfo not found."));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_480202);
      }
      if (null == aclConf.getAclConfDetailList()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "AclInfoDetail not found."));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_480202);
      }

      session.startTransaction();

      List<String> termNameList = new ArrayList<String>();
      IfFilter inputfilter = inputData.getIfFilter();
      ArrayList<Terms> inputTerms = inputfilter.getTerms();
      for (Terms term : inputTerms) {
        termNameList.add(term.getTermName());
      }

      Boolean overlapcheckResult = termNameOverlapCheck(inputData.getIfFilter().getTerms());
      if (overlapcheckResult || termNameList.size() == 0) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
        return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_480101);
      }

      session.deleteAclConf(nodeId, ifId, ifType, termNameList);

      AddAclSetting emSendData = toDeleteAclSetting(inputData, nodeDb, aclConf);
      AbstractMessage result = EmController.getInstance().request(emSendData);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_480402);
      }

      session.commit();
      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      switch (dbae.getCode()) {
        case NO_DELETE_TARGET:
          response = makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_480202);
          break;
        case COMMIT_FAILURE:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_900403);
          break;
        default:
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_480404);
          break;
      }
    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_480401);

    } catch (IllegalArgumentException iae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), iae);
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_480499);
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  /**
   * Duplication Check of termName.
   *
   * @param termsList
   *          Input Information
   * @return  Duplication Check Results
   */
  private boolean termNameOverlapCheck(ArrayList<Terms> termsList) {
    boolean result = false;
    List<String> termNameList = new ArrayList<String>();
    for (Terms terms : termsList) {
      termNameList.add(terms.getTermName());
    }
    List<String> termNameHashList = new ArrayList<String>(new HashSet<>(termNameList));
    if (termNameList.size() != termNameHashList.size()) {
      result = true;
    }
    return result;
  }

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {
      DeleteFilters inputData = (DeleteFilters) getInData();
      inputData.check(new OperationType(super.getOperationType()));
    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    if (getUriKeyMap() == null) {
      result = false;
    } else {
      if (!(getUriKeyMap().containsKey(KEY_NODE_ID)) || getUriKeyMap().get(KEY_NODE_ID) == null) {
        result = false;
      } else if (!(getUriKeyMap().containsKey(KEY_IF_TYPE)) || getUriKeyMap().get(KEY_IF_TYPE) == null) {
        result = false;
      } else if (!(getUriKeyMap().containsKey(KEY_IF_ID)) || getUriKeyMap().get(KEY_IF_ID) == null) {
        result = false;
      }
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

  /**
   * EM Data Mapping_Filter Delete<br>
   * Converting the Filter Delete Information into EM transmittable format.
   *
   * @param input
   *          Input Information
   * @param nodes
   *          Device Information
   * @param aclConf
   *          ACL Configuration
	* @return  Information for filter delete (for EM transmission)
   */
  public AddAclSetting toDeleteAclSetting(DeleteFilters input, Nodes nodes, AclConf aclConf) {

    ArrayList<Term> termList = new ArrayList<Term>();
    IfFilter inputFilter = input.getIfFilter();
    for (Terms inputTerm : inputFilter.getTerms()) {
      Term term = new Term();
      term.setOperation(CommonDefinitions.OPERATION_TYPE_DELETE);
      term.setTermName(inputTerm.getTermName());
      termList.add(term);
    }

    AclFilter filter = new AclFilter();
    filter.setOperation(CommonDefinitions.OPERATION_TYPE_DELETE);
    filter.setFilterId(Integer.valueOf(aclConf.getAcl_id()));
    filter.setTerm(termList);

    ArrayList<AclFilter> filterList = new ArrayList<AclFilter>();
    filterList.add(filter);

    DeviceLeafAcl deviceLeaf = new DeviceLeafAcl();
    deviceLeaf.setName(nodes.getNode_name());
    deviceLeaf.setFilter(filterList);

    ArrayList<DeviceLeafAcl> deviceLeafList = new ArrayList<>();
    deviceLeafList.add(deviceLeaf);

    AddAclSetting emMapFilter = new AddAclSetting();
    emMapFilter.setName(ACL_FILTER_NAME);
    emMapFilter.setDeviceLeafAclList(deviceLeafList);

    return emMapFilter;

  }

  /**
   * Mixed Operation Check.
   *
   * @param termsList
	*          termsList (Input Information)
   * @return  Mixed Check Results
   */
  private boolean operationMixCheck(List<Terms> termsList) {
    boolean result = false;
    String comparison = termsList.get(0).getOperation();
    for (Terms terms : termsList) {
      if (!comparison.equals(terms.getOperation())) {
        result = true;
        break;
      }
    }
    return result;
  }
}
