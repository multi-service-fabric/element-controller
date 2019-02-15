/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.cp;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.DummyVlanIfs;
import msf.ecmm.db.pojo.IRBInstanceInfo;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.VlanIfs;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetVlanInterfaceList;

/**
 * VLANIF Information List Acquisition.
 */
public class AllVlanIfInfoAcquisition extends Operation {

  /** In case input data check result is NG. */
  private static final String ERROR_CODE_390101 = "390101";

  /** In case error has occurred in DB access. */
  private static final String ERROR_CODE_390301 = "390301";

  /**
   * Constructor.
   *
   * @param idt
   *          input data
   * @param ukm
   *          URI key information
   */
  public AllVlanIfInfoAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.AllVlanIfInfoAcquisition);
  }

  @Override
  public AbstractResponseMessage execute() {
    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_390101);
    }

    String nodeId = getUriKeyMap().get(KEY_NODE_ID);

    GetVlanInterfaceList getVlanIfList = null;

    try (DBAccessManager session = new DBAccessManager()) {

      List<VlanIfs> vlanIfsDb = session.getVlanIfsList(nodeId);
      List<IRBInstanceInfo> irbListDb = new ArrayList<IRBInstanceInfo>();
      IRBInstanceInfo irb = null;
      for (VlanIfs vlan : vlanIfsDb) {
        irb = session.searchIrbInstanceInfo(nodeId, vlan.getVlan_id());
        if (null != irb) {
          irbListDb.add(irb);
        }
      }
      List<DummyVlanIfs> dummyIfsDb = session.getDummyVlanIfsInfoList(nodeId);
      for (DummyVlanIfs vlan : dummyIfsDb) {
        irb = session.searchIrbInstanceInfo(nodeId, vlan.getVlan_id());
        if (null != irb) {
          irbListDb.add(irb);
        }
      }
      Nodes nodes = session.searchNodes(nodeId, null);

      getVlanIfList = RestMapper.toVlanIfsInfoList(vlanIfsDb, nodes, irbListDb);
      if (0 != dummyIfsDb.size()) {
        getVlanIfList.getVlanIfs()
            .addAll((RestMapper.toVlanIfsInfoListFromDummyIfs(dummyIfsDb, nodes, irbListDb).getVlanIfs()));
      }

      response = makeSuccessResponse(RESP_OK_200, getVlanIfList);

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_390301);
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
      if (!getUriKeyMap().containsKey(KEY_NODE_ID) || getUriKeyMap().get(KEY_NODE_ID) == null) {
        checkResult = false;
      }
    }

    logger.trace(CommonDefinitions.END);
    return checkResult;
  }

}
