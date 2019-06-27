/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.notification;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.config.PingOspfNeighborConfiguration;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.Operations;
import msf.ecmm.fcctrl.pojo.UpdateLogicalIfStatus;
import msf.ecmm.fcctrl.pojo.parts.IfsLogical;
import msf.ecmm.fcctrl.pojo.parts.NodeLogical;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.GetOspfNeighborInfoRequest;
import msf.ecmm.ope.receiver.pojo.GetOspfNeighborInfoResult;
import msf.ecmm.ope.receiver.pojo.parts.BaseIfInfo;
import msf.ecmm.ope.receiver.pojo.parts.OspfNeighborIfsResponse;
import msf.ecmm.ope.receiver.pojo.parts.OspfNeighborsRequest;
import msf.ecmm.ope.receiver.pojo.parts.OspfNeighborsResponse;

/**
 * Class definition for acquiring the OSPF neighbor information<br>
 * The OSPF neighbor information is acquired.
 */
public class OspfNeighborAcquisition extends Operation {

  /** The name of the extended function operation. */
  String operationName = "OspfNeighborAcquisition";

  /** In case the inmut parameter is NG(json is NG). */
  private static final String ERROR_CODE_560101 = "560101";

  /** The IF or the node does not exsist. */
  private static final String ERROR_CODE_560201 = "560201";

  /** In case an error has occurred when the DB is accessed. */	
  private static final String ERROR_CODE_560401 = "560401";

  /** In case the timeout error has occurred during  the thread execution. */
  private static final String ERROR_CODE_560402 = "560402";

  /** The node is running. */ 
  public static final int NODE_STATE_OPERATION = 0;

  /** The node is not running  because of the failure. */ 
  public static final int NODE_STATE_MALFUNCTION = 7;

  /** The status of the IF is OK. */
  public static final String IF_STATE_UP = "up";

  /** The status of the IF is not OK. */
  public static final String IF_STATE_DOWN = "down";

  /** The neighbor information is not detected. */
  public static final String NOT_FOUND = "notfound";

  /** It is unexecuted. */	
  public static final String UNEXECUTED = "unexecuted";

  /** OSPF neighbor is full. */
  public static final int OSPF_NEIGHBOR_FULL = 8;

  /** OSPF neighbor is not OK( The MIB cannot be acquired ). */
  public static final int OSPF_NEIGHBOR_FAILED = -1;

  /** The FC process request type to update the status of the logical interface. */
  public static final String UPDATE_LOGICAL_IF_STATUS = "update_logical_if_status";

  /** the list of the instances executing the thread. */
  private List<OspfNeighborAcquisitionThread> threadInstanceList = new ArrayList<>();

  /** Results of the acquired OSPF neighbor information. */
  private Map<Integer, Map<String, Integer>> resultMap = new HashMap<>();

  /** Results(the nodes list) of the acquired OSPF neighbor information. */
  private List<Nodes> nodesList = new ArrayList<>();

  /**
   * Constructor.
   *
   * @param idt
   *          Input data
   * @param ukm
   *          URI key information
   */
  public OspfNeighborAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(operationName).getOperationTypeId());
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    boolean mibFailedFlag = false;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_560101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      GetOspfNeighborInfoRequest inputData = (GetOspfNeighborInfoRequest) getInData();

      Set<String> allNodeIdSet = new HashSet<>();
      for (OspfNeighborsRequest ospfRequest : inputData.getOspfNeighbors()) {
        allNodeIdSet.add(ospfRequest.getNodeId());
        for (BaseIfInfo ifRequest : ospfRequest.getOspfNeighborIfs()) {
          allNodeIdSet.add(ifRequest.getNodeId());
        }
      }

      Map<String, Nodes> allNodeMap = new HashMap<>();
      for (String nodeId : allNodeIdSet) {
        Nodes nodesDb = session.searchNodes(nodeId, null);
        if (nodesDb == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db. [Nodes]"));
          return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_560201);
        } else {
          allNodeMap.put(nodeId, nodesDb);
        }
      }

      Map<String, BaseIfInfo> ipMap = new HashMap<>();

      for (OspfNeighborsRequest ospfRequest : inputData.getOspfNeighbors()) {
        OspfNeighborAcquisitionThread ospfThread = new OspfNeighborAcquisitionThread();
        for (BaseIfInfo ifRequest : ospfRequest.getOspfNeighborIfs()) {
          String ifIp = getIfIp(allNodeMap.get(ifRequest.getNodeId()), ifRequest.getIfType(), ifRequest.getIfId());
          if (ifIp == null) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db. [IF IP]"));
            return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_560201);
          }
          ospfThread.getNeighbors().add(ifIp);
          ipMap.put(ifIp, ifRequest);
        }
        Nodes node = allNodeMap.get(ospfRequest.getNodeId());
        ospfThread.setNode(node);
        ospfThread.setInstance(this);
        threadInstanceList.add(ospfThread);
      }

      for (OspfNeighborAcquisitionThread thread : threadInstanceList) {
        thread.start();
      }
      long timeout = (long) PingOspfNeighborConfiguration.getInstance().get(Integer.class,
          PingOspfNeighborConfiguration.ALL_OSPF_NEIGHBOR_TIMEOUT) * 1000;
      for (OspfNeighborAcquisitionThread thread : threadInstanceList) {
        try {
          thread.join(timeout);
        } catch (InterruptedException ie) {
        }
      }

      for (OspfNeighborAcquisitionThread thread : threadInstanceList) {
        if (thread.isAlive()) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Thread join is time out."));
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_560402);
          return response;
        }
      }

      GetOspfNeighborInfoResult getOspfNeighborInfoResult = new GetOspfNeighborInfoResult();
      for (int i : resultMap.keySet()) {
        Nodes node = nodesList.get(i);
        String nodeId = node.getNode_id();
        OspfNeighborsResponse ospfNeighborsResponse = new OspfNeighborsResponse();
        for (String ifIp : resultMap.get(i).keySet()) {
          mibFailedFlag = false;
          String status;
          if (resultMap.get(i).get(ifIp) == null) {
            status = NOT_FOUND;
          } else if (resultMap.get(i).get(ifIp) == OSPF_NEIGHBOR_FAILED) {
            status = UNEXECUTED;
            mibFailedFlag = true;
          } else if (resultMap.get(i).get(ifIp) == OSPF_NEIGHBOR_FULL) {
            status = IF_STATE_UP;
          } else {
            status = IF_STATE_DOWN;
          }
          String neighborNodeId = ipMap.get(ifIp).getNodeId();
          String neighborIfType = ipMap.get(ifIp).getIfType();
          String neighborIfId = ipMap.get(ifIp).getIfId();
          OspfNeighborIfsResponse ospfNeighborIfsResponse = new OspfNeighborIfsResponse();
          ospfNeighborIfsResponse.setNodeId(neighborNodeId);
          ospfNeighborIfsResponse.setIfType(neighborIfType);
          ospfNeighborIfsResponse.setIfId(neighborIfId);
          ospfNeighborIfsResponse.setStatus(status);
          ospfNeighborsResponse.getOspfNeighborIfs().add(ospfNeighborIfsResponse);

          boolean dbUpdateFlag = dbUpdate(session, allNodeMap.get(neighborNodeId), mibFailedFlag);
          if (dbUpdateFlag) {
            Operations ospfData = toFcNotification(neighborNodeId, neighborIfType, neighborIfId, mibFailedFlag);

            try {
              RestClient restClient = new RestClient();
              restClient.request(RestClient.OPERATION, new HashMap<String, String>(), ospfData,
                  CommonResponseFromFc.class);
            } catch (RestClientException re) {
              logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Rest request to FC failed."), re);
            }

          }
        }
        ospfNeighborsResponse.setNodeId(nodeId);
        getOspfNeighborInfoResult.getOspfNeighbors().add(ospfNeighborsResponse);
      }

      response = makeSuccessResponse(RESP_OK_200, getOspfNeighborInfoResult);

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_560401);
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      GetOspfNeighborInfoRequest inputData = (GetOspfNeighborInfoRequest) getInData();

      inputData.check(new OperationType(getOperationType()));

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

  /**
   * The acquired OSPF neighbor infomation is registered.
   *
   * @param node
   *          The node information
   * @param result
   *          Result
   */
  protected synchronized void setResult(Nodes node, Map<String, Integer> result) {
    nodesList.add(node);
    int size = nodesList.size();
    resultMap.put(size - 1, result);
  }

  /**
   * The IPIP is identified.
   *
   * @param node
   *          The node information
   * @param ifType
   *          The IF type
   * @param ifId
   *          IFID
   * @return  IFIP
   */
  private String getIfIp(Nodes node, String ifType, String ifId) {

    String ifIp = null;

    if (ifType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
      for (PhysicalIfs physicalIf : node.getPhysicalIfsList()) {
        if (physicalIf.getPhysical_if_id().equals(ifId)) {
          ifIp = physicalIf.getIpv4_address();
          break;
        }
      }
    } else if (ifType.equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
      for (LagIfs lagIf : node.getLagIfsList()) {
        if (lagIf.getFc_lag_if_id().equals(ifId)) {
          ifIp = lagIf.getIpv4_address();
          break;
        }
      }
    } else if (ifType.equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
      FOUNDIP: for (PhysicalIfs physicalIf : node.getPhysicalIfsList()) {
        for (BreakoutIfs breakoutIf : physicalIf.getBreakoutIfsList()) {
          if (breakoutIf.getBreakout_if_id().equals(ifId)) {
            ifIp = breakoutIf.getIpv4_address();
            break FOUNDIP;
          }
        }
      }
    }
    return ifIp;
  }

  /**
   * The DB-updating process
   *
   * @param session
   *          session
   * @param nodesDb
   *          The node information
   * @param mibFailedFlag
   *          The flag which shows the failure of MIB acquisition
   * @return DB-updating flag （true:updated, false:not updated）
   * @throws DBAccessException
   *           DB error
   */
  private boolean dbUpdate(DBAccessManager session, Nodes nodesDb, boolean mibFailedFlag) throws DBAccessException {

    boolean dbUpdateFlag = false;

    String nodeid = nodesDb.getNode_id();
    int nodestate = nodesDb.getNode_state();

    if (nodestate != NODE_STATE_MALFUNCTION && mibFailedFlag) {
      nodestate = NODE_STATE_MALFUNCTION;

    } else if (nodestate == NODE_STATE_MALFUNCTION && !mibFailedFlag) {
      nodestate = NODE_STATE_OPERATION;
    }

    if (nodesDb.getNode_state() != nodestate) {

      session.startTransaction();

      session.updateNodeState(nodeid, nodestate);
      dbUpdateFlag = true;

      session.commit();

    }

    return dbUpdateFlag;
  }

  /**
   * The data mapping of the FC notification.
   *
   * @param nodeId
   *          The node ID
   * @param ifType
   *          The IF type
   * @param ifId
   *          IFID
   * @param mibFailedFlag
   *          The flag showing the failure of MIB acquisition
   * @return  The data associated with  REST request
   */
  private Operations toFcNotification(String nodeId, String ifType, String ifId, boolean mibFailedFlag) {

    logger.trace(CommonDefinitions.START);
    logger.debug("nodeId=" + nodeId + ", ifType=" + ifType + ", ifId=" + ifId + "mibFailedFlag=" + mibFailedFlag);

    Operations ret = new Operations();
    ret.setUpdateLogicalIfStatusOption(new UpdateLogicalIfStatus());
    ret.setAction(UPDATE_LOGICAL_IF_STATUS);

    ret.getUpdateLogicalIfStatusOption().setNodes(new ArrayList<NodeLogical>());
    ret.getUpdateLogicalIfStatusOption().getNodes().add(new NodeLogical());
    ret.getUpdateLogicalIfStatusOption().getNodes().get(0).setNodeId(nodeId);

    String status;
    if (mibFailedFlag) {
      ret.getUpdateLogicalIfStatusOption().getNodes().get(0).setFailureStatus(CommonDefinitions.IF_STATE_NG_STRING);
      status = CommonDefinitions.IF_STATE_NG_STRING;
    } else {
      ret.getUpdateLogicalIfStatusOption().getNodes().get(0).setFailureStatus(CommonDefinitions.IF_STATE_OK_STRING);
      status = CommonDefinitions.IF_STATE_OK_STRING;
    }

    ret.getUpdateLogicalIfStatusOption().setIfs(new ArrayList<IfsLogical>());
    IfsLogical ifsLogical = new IfsLogical();
    ifsLogical.setNodeId(nodeId);
    ifsLogical.setIfType(ifType);
    ifsLogical.setIfId(ifId);
    ifsLogical.setStatus(status);
    ret.getUpdateLogicalIfStatusOption().getIfs().add(ifsLogical);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;
  }
}
