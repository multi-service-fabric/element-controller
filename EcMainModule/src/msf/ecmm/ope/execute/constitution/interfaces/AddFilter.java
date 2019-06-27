/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.db.DBAccessException.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.AclConf;
import msf.ecmm.db.pojo.AclConfDetail;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
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
import msf.ecmm.ope.receiver.pojo.parts.AddFilters;
import msf.ecmm.ope.receiver.pojo.parts.Terms;

/**
 * Filter Addition.
 */
public class AddFilter extends Operation {

  /** In case of input data check NG. */
  private static final String ERROR_CODE_480101 = "480101";

  /** Target IF/target device does not exist. */
  private static final String ERROR_CODE_480201 = "480201";

  /** In case there already exists the filter information to be registered. */
  private static final String ERROR_CODE_480302 = "480302";

  /** In case the filter registration target IF does not satisfy the filter registration condtions. */
  private static final String ERROR_CODE_480303 = "480303";

  /** Disconnection with EM / timeout occured during EM requesting, */
  private static final String ERROR_CODE_480401 = "480401";

  /** Error occured from EM during EM requesting, (Error response received). */
  private static final String ERROR_CODE_480402 = "480402";

  /** In case DB commitment failed after normal EM access. */
  private static final String ERROR_CODE_900403 = "900403";

  /** In case abnormality has occurred at DB access. */
  private static final String ERROR_CODE_480404 = "480404";

  /** Filter name. Fixing to acl-filter. */
  private static final String ACL_FILTER_NAME = "acl-filter";

  /** Filter Addition  Extension Function Operation Name. */
  private static final String ADD_FILTER = "AddFilter";

  /**
   * Constructor.
   *
   * @param idt
   *          Input Data
   * @param ukm
   *          URI Key Information
   */
  public AddFilter(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(ADD_FILTER).getOperationTypeId());
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    AddFilters inputData = (AddFilters) getInData();
    AddAclSetting emSendData = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_480101);
    }

    if (inputDataShortageCheck(inputData.getIfFilter().getTerms())) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "InputData is insufficient."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_480101);
    }

    if (operationMixCheck(inputData.getIfFilter().getTerms())) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Delete mixed in operation."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_480101);
    }

    if (termNameOverlapCheck(inputData.getIfFilter().getTerms())) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Inputdatas are overlapping."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_480101);
    }

    try (DBAccessManager session = new DBAccessManager()) {
      String nodeId = getUriKeyMap().get(CommonDefinitions.KEY_NODE_ID);
      String ifType = getUriKeyMap().get(CommonDefinitions.KEY_IF_TYPE);
      String ifId = getUriKeyMap().get(CommonDefinitions.KEY_IF_ID);

      String name = null;
      String vlanId = null;
      String ipaddr = null;

      Nodes nodeDb = session.searchNodes(nodeId, null);

      if (null == nodeDb) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Node not found."));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_480201);
      }

      List<VlanIfs> vlanIfsListDb = session.getVlanIfsList(nodeId);

      boolean ifFoundFlag = false;
      if (ifType.equals(IF_TYPE_PHYSICAL_IFS)) {
        for (PhysicalIfs pifs : nodeDb.getPhysicalIfsList()) {
          if (pifs.getPhysical_if_id().equals(ifId)) {
            name = pifs.getIf_name();
            ipaddr = pifs.getIpv4_address();
            ifFoundFlag = true;
            break;
          }
        }
      } else if (ifType.equals(IF_TYPE_LAG_IFS)) {
        for (LagIfs lagIfs : nodeDb.getLagIfsList()) {
          if (lagIfs.getFc_lag_if_id().equals(ifId)) {
            name = lagIfs.getIf_name();
            ifId = lagIfs.getLag_if_id();
            ifFoundFlag = true;
            break;
          }
        }
      } else if (ifType.equals(IF_TYPE_VLAN_IFS)) {
        for (VlanIfs vlanIfs : vlanIfsListDb) {
          if (vlanIfs.getVlan_if_id().equals(ifId)) {
            name = vlanIfs.getIf_name();
            vlanId = vlanIfs.getVlan_id();
            ifFoundFlag = true;
            break;
          }
        }
      }
      if (ifFoundFlag == false) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Interface not found."));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_480201);
      }

      if (ifType.equals(IF_TYPE_PHYSICAL_IFS) && ipaddr == null) {
        boolean useBasePhysIfFlag = false;
        for (VlanIfs vlanIfs : vlanIfsListDb) {
          if (vlanIfs.getPhysical_if_id() != null && vlanIfs.getPhysical_if_id().equals(ifId)) {
            useBasePhysIfFlag = true;
            break;
          }
        }
        if (useBasePhysIfFlag == false) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Interface not found."));
          return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_480303);
        }
      }

      List<AclConf> aclList = session.getAclConfList(nodeId);
      ArrayList<Integer> priorityList = getPriorityList(aclList);
      ArrayList<Integer> aclIdList = getAclIdList(aclList);

      AclConf dbAclConf = null;
      String aclId = "";
      dbAclConf = session.getAclConf(nodeId, ifId, ifType);

      Set<AclConfDetail> keepAclConfDetailSetList = new HashSet<AclConfDetail>();
      Set<AclConfDetail> tmpAclConfDetailSetList = new HashSet<AclConfDetail>();
      if (null == dbAclConf) {

        dbAclConf = new AclConf();
        dbAclConf.setNode_id(nodeId);
        if (ifType.equals(IF_TYPE_PHYSICAL_IFS)) {
          dbAclConf.setPhysical_if_id(ifId);
        } else if (ifType.equals(IF_TYPE_LAG_IFS)) {
          dbAclConf.setLag_if_id(ifId);
        } else if (ifType.equals(IF_TYPE_VLAN_IFS)) {
          dbAclConf.setVlan_if_id(ifId);
        }
        aclId = Integer.toString(getEmptyId(aclIdList));
        dbAclConf.setAclConfDetailList(keepAclConfDetailSetList);
      } else {
        aclId = dbAclConf.getAcl_id();
        tmpAclConfDetailSetList.addAll(dbAclConf.getAclConfDetailList());
      }

      dbAclConf.setAcl_id(aclId);

      session.startTransaction();
      ArrayList<Term> emTermList = new ArrayList<Term>();

      List<AclConfDetail> aclConfDetailList = new ArrayList<AclConfDetail>();

      for (Terms inputTerms : inputData.getIfFilter().getTerms()) {
        int priority = getEmptyId(priorityList);
        AclConfDetail dbAclDetail = toAclConfDetail(inputTerms, nodeId, aclId, priority, vlanId);
        Boolean isOverLap = false;
        for (AclConf checkAcl : aclList) {
          if (ifType.equals(IF_TYPE_PHYSICAL_IFS)) {
            if (checkAcl.getPhysical_if_id() != null) {
              if (checkAcl.getPhysical_if_id().equals(ifId)) {
                if (checkOverLap(checkAcl.getAclConfDetailList(), inputTerms)) {
                  isOverLap = true;
                }
              }
            }
          } else if (ifType.equals(IF_TYPE_LAG_IFS)) {
            if (checkAcl.getLag_if_id() != null) {
              if (checkAcl.getLag_if_id().equals(ifId)) {
                if (checkOverLap(checkAcl.getAclConfDetailList(), inputTerms)) {
                  isOverLap = true;
                }
              }
            }
          } else if (ifType.equals(IF_TYPE_VLAN_IFS)) {
            if (checkAcl.getVlan_if_id() != null) {
              if (checkAcl.getVlan_if_id().equals(ifId)) {
                if (checkOverLap(checkAcl.getAclConfDetailList(), inputTerms)) {
                  isOverLap = true;
                }
              }
            }
          }
        }

        if (!isOverLap) {

          aclConfDetailList.add(dbAclDetail);

          emTermList.add(toTerm(inputTerms, name, aclId, priority, vlanId));
          priorityList.add(priority);
        } else {
          return makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_480302);
        }
      }

      Set<AclConfDetail> aclConfDetailSetList = new HashSet<AclConfDetail>(aclConfDetailList);

      dbAclConf.getAclConfDetailList().clear();
      dbAclConf.getAclConfDetailList().addAll(aclConfDetailSetList);
      if (tmpAclConfDetailSetList.size() != 0) {
        dbAclConf.getAclConfDetailList().addAll(tmpAclConfDetailSetList);
      }

      session.addAclConf(dbAclConf);

      emSendData = toAddAclSetting(inputData, nodeDb, vlanId, emTermList, aclId);

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
        case DOUBLE_REGISTRATION:
          response = makeFailedResponse(RESP_CONFLICT_409, ERROR_CODE_480302);
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
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_480101);
    }

    return response;
  }

  /**
   * Necessary Item Check of inputData.
   *
   * @param termsList
   *          Input Information
   * @return  Duplication Check Results
   */
  private boolean inputDataShortageCheck(ArrayList<Terms> termsList) {
    boolean result = false;
    for (Terms terms : termsList) {
      if (terms.getOperation() == null || terms.getTermName() == null || terms.getAction() == null
          || terms.getDirection() == null
          || (terms.getSourceMacAddress() == null && terms.getDestMacAddress() == null
              && terms.getSourceIpAddress() == null && terms.getDestIpAddress() == null && terms.getProtocol() == null
              && terms.getSourcePort() == null && terms.getDestPort() == null)) {
        result = true;
      }
    }
    return result;
  }

  /**
   * Duplication Check of termName.
   *
   * @param termsList
   *          Input Information
   * @return Duplication Check Results
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

  /**
   * Mixed Operation Check.
   *
   * @param termsList
   *          termsList(Input Information)
   * @return  Mixed Check Results
   */
  private boolean operationMixCheck(ArrayList<Terms> termsList) {
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

  @Override
  protected boolean checkInData() {
    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {
      AddFilters inputData = (AddFilters) getInData();
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
   * DB Data Mapping_ACL Details Information.
   *
   * @param input
   *          Input Information
   * @param nodeId
   *          Node ID
   * @param aclId
   *          ACL Configuration ID
   * @param priority
   *          ACL Priority
   * @param vlanId
   *          VLAN ID
   * @return  ACL Configuration Details Information POJO
   */
  public AclConfDetail toAclConfDetail(Terms input, String nodeId, String aclId, int priority, String vlanId) {
    AclConfDetail dbAclDetail = new AclConfDetail();
    dbAclDetail.setNode_id(nodeId);
    dbAclDetail.setAcl_id(aclId);
    dbAclDetail.setTerm_name(input.getTermName());
    dbAclDetail.setAction(input.getAction());
    dbAclDetail.setDirection(input.getDirection());

    dbAclDetail.setSource_mac_address(input.getSourceMacAddress());
    dbAclDetail.setDestination_mac_address(input.getDestMacAddress());
    dbAclDetail.setSource_ip_address(input.getSourceIpAddress());
    dbAclDetail.setDestination_ip_address(input.getDestIpAddress());
    if (input.getSourcePort() != null) {
      dbAclDetail.setSource_port(Integer.parseInt(input.getSourcePort()));
    }
    if (input.getDestPort() != null) {
      dbAclDetail.setDestination_port(Integer.parseInt(input.getDestPort()));
    }
    dbAclDetail.setProtocol(input.getProtocol());
    dbAclDetail.setAcl_priority(priority);
    return dbAclDetail;
  }

  /**
   * EM Data Mapping_Term Area Section.
   *
   * @param input
   *          Input Information
   * @param name
   *          IF Name
   * @param aclId
   *          ACL Configuration ID
   * @param priority
   *          ACL Priority
   * @param vlanId
   *          VLAN ID
   * @return  ACL Configuration Details Information POJO
   */
  public Term toTerm(Terms input, String name, String aclId, int priority, String vlanId) {

    Term term = new Term();
    term.setTermName(input.getTermName());
    term.setName(name);
    if (null != vlanId) {
      term.setVlanId(Long.parseLong(vlanId));
    }
    term.setAction(input.getAction());
    term.setDirection(input.getDirection());
    term.setSourceMacAddress(input.getSourceMacAddress());
    term.setDestinationMacAddress(input.getDestMacAddress());
    if (input.getSourcePort() != null) {
      term.setSourcePort(Long.parseLong(input.getSourcePort()));
    }
    if (input.getDestPort() != null) {
      term.setDestinationPort(Long.parseLong(input.getDestPort()));
    }
    term.setSourceIpAddress(input.getSourceIpAddress());
    term.setDestinationIpAddress(input.getDestIpAddress());
    term.setProtocol(input.getProtocol());
    term.setPriority((long) priority);

    return term;
  }

  /**
   * EM Data Mapping_Filter Addition<br>
   * Convert the filter addition information into EM transmittable format.
   *
   * @param input
   *          Input Information
   * @param nodes
   *          Device Information
   * @param vlanId
   *          VLAN ID
   * @param emTermList
   *          ACL Priority
   * @param aclId
   *          ACL Configuration ID
   * @return Information for filter addition (for EM transmission)
   */
  public AddAclSetting toAddAclSetting(AddFilters input, Nodes nodes, String vlanId, ArrayList<Term> emTermList,
      String aclId) {
    AddAclSetting emMapFilter = new AddAclSetting();
    emMapFilter.setName(ACL_FILTER_NAME);
    DeviceLeafAcl deviceLeaf = new DeviceLeafAcl();
    deviceLeaf.setName(nodes.getNode_name());
    ArrayList<AclFilter> filterList = new ArrayList<AclFilter>();
    AclFilter filter = new AclFilter();
    filter.setFilterId(Integer.valueOf(aclId));
    filter.setTerm(emTermList);
    filterList.add(filter);
    deviceLeaf.setFilter(filterList);

    ArrayList<DeviceLeafAcl> deviceLeafList = new ArrayList<>();
    deviceLeafList.add(deviceLeaf);
    emMapFilter.setDeviceLeafAclList(deviceLeafList);

    return emMapFilter;
  }

  /**
   * Priority collection function for the ACL which was already used.
   *
   * @param aclList
   *          ACL Information
   * @return PriorityList
   */
  public static ArrayList<Integer> getPriorityList(List<AclConf> aclList) {
    ArrayList<Integer> priorityList = new ArrayList<Integer>();
    if (aclList.isEmpty() == false) {
      for (AclConf acl : aclList) {
        for (AclConfDetail aclDetail : acl.getAclConfDetailList()) {
          priorityList.add(aclDetail.getAcl_priority());
        }
      }
    }
    logger.trace(END);
    return priorityList;

  }

  /**
   * Configured ID collection function for the ACL which was already used.
   *
   * @param aclList
   *          ACL Information
   * @return  ACL Configuration ID List
   */
  public static ArrayList<Integer> getAclIdList(List<AclConf> aclList) {
    ArrayList<Integer> aclIdList = new ArrayList<Integer>();
    if (aclList.isEmpty() == false) {
      for (AclConf acl : aclList) {
        aclIdList.add(Integer.parseInt(acl.getAcl_id()));
      }
    }
    logger.trace(END);
    return aclIdList;
  }

  /**
   * Empty ID Collection Function.
   *
   * @param idList
   *          ID which was already used
   * @return  Priority
   */
  public static int getEmptyId(ArrayList<Integer> idList) {
    logger.trace(START);
    Collections.sort(idList);
    int ret = 0;
    if (idList.isEmpty() == false) {
      Collections.sort(idList);
      for (int i = 1; i < idList.size() + 1; i++) {
        if ((idList.get(i - 1)) != i) {
          ret = i;
          break;
        } else if (idList.get(idList.size() - 1) == i) {
          ret = i + 1;
        }
      }
    } else {
      ret = 1;
    }
    logger.debug("ID: " + ret);
    logger.trace(END);
    return ret;

  }

  /**
   * Filter Information Duplication Check.
   *
   * @param dbAclConDetailList
   *          TermName list acquired from DB
   * @param inputTerms
   *          TermName list acquired from inputData
   * @return Priority
   */
  public static boolean checkOverLap(Set<AclConfDetail> dbAclConDetailList, Terms inputTerms) {
    boolean result = false;
    for (AclConfDetail aclConfDetail : dbAclConDetailList) {
      if (inputTerms.getTermName().equals(aclConfDetail.getTerm_name())) {
        result = true;
      }
    }
    return result;
  }
}
