/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.cp;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.DbMapper;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.StaticRouteOptions;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.L3SliceAddDelete;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.UpdateVlanIf;
import msf.ecmm.ope.receiver.pojo.parts.UpdateStaticRoute;

/**
 * L3VLAN IF Change.
 *
 */
public class VlanIfChange extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_380101 = "380101";

  /** In case the information to be changed does not exist. */
  private static final String ERROR_CODE_380201 = "380201";

  /** Disconnection or connection timeout with EM has occurred while requesting L3 slice addition to EM. */
  private static final String ERROR_CODE_380401 = "380401";

  /** Error has occurred from EM while requesting L3 slice addition to EM (error response received). */
  private static final String ERROR_CODE_380402 = "380402";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_380403 = "380403";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public VlanIfChange(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.VlanIfChange);
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_380101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      String nodeId = getUriKeyMap().get(KEY_NODE_ID);
      String vlanIfId = getUriKeyMap().get(KEY_VLAN_IF_ID);

      Nodes nodesDb = session.searchNodes(nodeId, null);
      VlanIfs vlanIfsDb = session.searchVlanIfs(nodeId, vlanIfId);

      if (nodesDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_380201);
      }
      if (vlanIfsDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [VlanIfs]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_380201);
      }

      UpdateVlanIf inputData = (UpdateVlanIf) getInData();

      checkStaticRoutes(inputData);

      checkDuplicateOperation(vlanIfsDb, inputData);

      session.startTransaction();

      for (UpdateStaticRoute srElem : inputData.getUpdateOption().getL3VlanOption().getStaticRoutes()) {

        StaticRouteOptions srOpts = DbMapper.toVlanIfChange(srElem, nodeId, vlanIfId);

        if (srElem.getOperationType().equals(CommonDefinitions.OPERATION_TYPE_ADD)) {
          session.addStaticRouteOptions(srOpts);
        } else if (srElem.getOperationType().equals(CommonDefinitions.OPERATION_TYPE_DELETE)) {
          session.deleteStaticRouteOptions(srOpts);
        }

      }

      L3SliceAddDelete l3SliceAddDeleteEm = EmMapper.toVlanIfChange(inputData, nodesDb.getNode_name(), vlanIfsDb);

      AbstractMessage result = EmController.getInstance().request(l3SliceAddDeleteEm);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_380402);
      }

      session.commit();

      response = makeSuccessResponse(200, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_380403);

    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_380401);
    } catch (IllegalArgumentException iae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "input data error"), iae);
      response = makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_380101);
    }

    logger.trace(CommonDefinitions.END);

    return response;

  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      UpdateVlanIf inputData = (UpdateVlanIf) getInData();

      inputData.check(OperationType.VlanIfChange);

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
      if (!(getUriKeyMap().containsKey(KEY_VLAN_IF_ID)) || getUriKeyMap().get(KEY_VLAN_IF_ID) == null) {
        result = false;
      }

    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

  /**
   * Checking duplicated registration/deletion of static-routes.
   *
   * @param vlanIfsDb
   *          information of VLAN to be updated (DB)
   * @param inputData
   *          input VLAN update data (REST)
   * @throws IllegalArgumentException
   *           with duplication/deletion
   */
  private void checkDuplicateOperation(VlanIfs vlanIfsDb, UpdateVlanIf inputData) throws IllegalArgumentException {
    logger.trace(CommonDefinitions.START);
    logger.debug("vlanIfsDb=" + vlanIfsDb + "inputData=" + vlanIfsDb);

    for (UpdateStaticRoute staticRest : inputData.getUpdateOption().getL3VlanOption().getStaticRoutes()) {
      boolean findTarget = false;
      for (StaticRouteOptions staticDb : vlanIfsDb.getStaticRouteOptionsList()) {
        String ifTypeDb = "";
        if (staticDb.getStatic_route_address_type() == 4) {
          ifTypeDb = CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV4_STRING;
        } else {
          ifTypeDb = CommonDefinitions.STATIC_ROUTEADDRESS_TYPE_IPV6_STRING;
        }
        if (staticRest.getAddressType().equals(ifTypeDb)
            && staticRest.getAddress().equals(staticDb.getStatic_route_destination_address())
            && staticRest.getPrefix() == staticDb.getStatic_route_prefix()
            && staticRest.getNextHop().equals(staticDb.getStatic_route_nexthop_address())) {

          findTarget = true;
          logger.debug("find static-routes in db. addressType=" + staticRest.getAddressType() + " address="
              + staticRest.getAddress() + " prefix=" + staticRest.getPrefix() + " nextHop=" + staticRest.getNextHop());
          break;
        }
      }
      if (staticRest.getOperationType().equals(CommonDefinitions.OPERATION_TYPE_ADD)) {
        if (findTarget == true) {
          logger.debug("duplicate registration detected.");
          throw new IllegalArgumentException();
        }
      } else {
        if (findTarget == false) {
          logger.debug("duplicate deletion detected.");
          throw new IllegalArgumentException();
        }
      }
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * static-routes Duplicated Registration Determination.
   *
   * @param inputData
   *          input REST information
   * @throws IllegalArgumentException
   *          with duplication
   */
  private void checkStaticRoutes(UpdateVlanIf inputData) throws IllegalArgumentException {
    logger.trace(CommonDefinitions.START);

    List<UpdateStaticRoute> staticListRest = inputData.getUpdateOption().getL3VlanOption().getStaticRoutes();
    if (staticListRest == null) {
      return; 
    }
    for (int i = 0; i <  staticListRest.size(); i++) {
      UpdateStaticRoute static1 = staticListRest.get(i);
      for (int j = 0; j <  staticListRest.size(); j++) {
        UpdateStaticRoute static2 = staticListRest.get(j);
        if (i == j) {
          continue;
        }
        if (static1.getAddressType().equals(static2.getAddressType())
            && static1.getAddress().equals(static2.getAddress())
            && static1.getPrefix() == static2.getPrefix()
            && static1.getNextHop().equals(static2.getNextHop())) {
          logger.debug("duplicate static-routes registration addressType=" + static1.getAddressType() + " address="
              + static1.getAddress() + " prefix=" + static1.getPrefix() + " nextHop=" + static1.getNextHop());
          throw new IllegalArgumentException();
        }
      }
    }
    logger.trace(CommonDefinitions.END);
  }
}
