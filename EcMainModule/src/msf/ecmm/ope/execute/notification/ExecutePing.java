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
import msf.ecmm.devctrl.pojo.PingData;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.ExecutePingRequest;
import msf.ecmm.ope.receiver.pojo.ExecutePingResult;
import msf.ecmm.ope.receiver.pojo.parts.BaseIfInfo;
import msf.ecmm.ope.receiver.pojo.parts.PingTargetsRequest;
import msf.ecmm.ope.receiver.pojo.parts.PingTargetsResponse;

/**
 * The definition of the class excuting ping between equipments <br>
 * The thread process for the ping between equipments is executed.
 */
public class ExecutePing extends Operation {

  /** The name of the extended function operation. */	
  String operationName = "ExecutePing";

  /** In case the check of the input parameter is NG(json is NG). */	
  private static final String ERROR_CODE_550101 = "550101";

  /** The IF or the node does not exsist. */
  private static final String ERROR_CODE_550201 = "550201";

  /** In case an error has occurred when the DB is accessed. */	
  private static final String ERROR_CODE_550401 = "550401";

  /** In case the timeout error has occurred during  the thread execution. */
  private static final String ERROR_CODE_550402 = "550402";

  /** Thelist of instances for the thread processing. */
  private List<ExecutePingThread> threadInstanceList = new ArrayList<>();

  /** The result of the ping execution. */
  private List<PingData> resultList = new ArrayList<>();

  /** The name  of sh executing the ping. */
  private String pingShName = "_ping_command.sh";

  /**
   * Constructor.
   *
   * @param idt
   *          Input data
   * @param ukm
   *          URI key information
   */
  public ExecutePing(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(operationName).getOperationTypeId());
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_550101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      ExecutePingRequest inputData = (ExecutePingRequest) getInData();

      Set<String> srcNodeIdSet = new HashSet<>();
      Set<String> dstNodeIdSet = new HashSet<>();
      for (PingTargetsRequest pingTarget : inputData.getPingTargets()) {
        srcNodeIdSet.add(pingTarget.getSrc().getNodeId());
        dstNodeIdSet.add(pingTarget.getDst().getNodeId());
      }

      Map<String, Nodes> srcNodeMap = new HashMap<>();
      for (String nodeId : srcNodeIdSet) {
        Nodes nodesDb = session.searchNodes(nodeId, null);
        if (nodesDb == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db. [Nodes]"));
          return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_550201);
        } else {
          srcNodeMap.put(nodeId, nodesDb);
        }
      }
      Map<String, Nodes> dstNodeMap = new HashMap<>();
      for (String nodeId : dstNodeIdSet) {
        if (srcNodeMap.containsKey(nodeId)) {
          dstNodeMap.put(nodeId, srcNodeMap.get(nodeId));
        } else {
          Nodes nodesDb = session.searchNodes(nodeId, null);
          if (nodesDb == null) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db. [Nodes]"));
            return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_550201);
          } else {
            dstNodeMap.put(nodeId, nodesDb);
          }
        }
      }


      Map<String, BaseIfInfo> srcIpMap = new HashMap<>();
      Map<String, BaseIfInfo> dstIpMap = new HashMap<>();
      Map<String, String> ipSetMap = new HashMap<>();

      for (PingTargetsRequest pingTarget : inputData.getPingTargets()) {
        Nodes srcNode = srcNodeMap.get(pingTarget.getSrc().getNodeId());
        String srcIfType = pingTarget.getSrc().getIfType();
        String srcIfId = pingTarget.getSrc().getIfId();
        String srcIp = getIfIp(srcNode, srcIfType, srcIfId);
        if (srcIp == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db. [IF IP]"));
          return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_550201);
        }
        srcIpMap.put(srcIp, pingTarget.getSrc());

        String dstNodeId = pingTarget.getDst().getNodeId();
        Nodes dstNode = dstNodeMap.get(dstNodeId);
        String dstIfType = pingTarget.getDst().getIfType();
        String dstIfId = pingTarget.getDst().getIfId();
        String dstIp = getIfIp(dstNode, dstIfType, dstIfId);
        if (dstIp == null) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db. [IF IP]"));
          return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_550201);
        }
        dstIpMap.put(dstIp, pingTarget.getDst());

        ipSetMap.put(srcIp, dstIp);
      }


      Map<String, String> pingMap = new HashMap<>();
      Map<String, Nodes> loginNodeMap = new HashMap<>();

      for (String srcIp : srcIpMap.keySet()) {
        Nodes loginNode = srcNodeMap.get(srcIpMap.get(srcIp).getNodeId());
        String loginIp = loginNode.getManagement_if_address();

        if (!loginNodeMap.containsKey(loginIp)) {
          loginNodeMap.put(loginIp, loginNode);
        }

        String dstIp = ipSetMap.get(srcIp);
        String pingIp = null;
        if (pingMap.containsKey(loginIp)) {
          pingIp = pingMap.get(loginIp) + "+" + srcIp + "_" + dstIp;
        } else {
          pingIp = srcIp + "_" + dstIp;
        }
        pingMap.put(loginIp, pingIp);
      }

      for (String loginIp : loginNodeMap.keySet()) {
        Nodes loginNode = loginNodeMap.get(loginIp);

        String platformName = loginNode.getEquipments().getPlatform_name();
        String osName = loginNode.getEquipments().getOs_name();
        String pingShFile = platformName + "_" + osName + pingShName;

        ExecutePingThread executePingThread = new ExecutePingThread();
        executePingThread.setLoginIp(loginIp);
        executePingThread.setPass(loginNode.getPassword());
        executePingThread.setUser(loginNode.getUsername());
        executePingThread.setPingList(pingMap.get(loginIp));
        executePingThread.setPingShFile(pingShFile);
        executePingThread.setInstance(this);
        threadInstanceList.add(executePingThread);
      }

      for (ExecutePingThread thread : threadInstanceList) {
        thread.start();
      }
      long timeout = (long) PingOspfNeighborConfiguration.getInstance().get(Integer.class,
          PingOspfNeighborConfiguration.ALL_PING_TIMEOUT) * 1000;
      for (ExecutePingThread thread : threadInstanceList) {
        try {
          thread.join(timeout);
        } catch (InterruptedException ie) {
        }
      }

      for (ExecutePingThread thread : threadInstanceList) {
        if (thread.isAlive()) {
          logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Thread join is time out."));
          response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_550402);
          return response;
        }
      }

      ExecutePingResult executePingResult = new ExecutePingResult();
      for (PingData result : resultList) {
        for (String srcDstIp : result.getResult().keySet()) {
          String status = result.getResult().get(srcDstIp);
          String[] ip = srcDstIp.split("_");
          PingTargetsResponse pingTargets = new PingTargetsResponse();
          pingTargets.setSrc(srcIpMap.get(ip[0]));
          pingTargets.setDst(dstIpMap.get(ip[1]));
          pingTargets.setResult(status);
          executePingResult.getPingTargets().add(pingTargets);
        }
      }

      response = makeSuccessResponse(RESP_OK_200, executePingResult);

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_550401);
    }

    logger.trace(CommonDefinitions.END);
    return response;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      ExecutePingRequest inputData = (ExecutePingRequest) getInData();

      inputData.check(new OperationType(getOperationType()));

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

  /**
   * The result of the ping wxecution is registered.
   *
   * @param result
   *          The result of the ping
   */
  protected void setResult(PingData result) {
    synchronized (resultList) {
      resultList.add(result);
    }
  }

  /**
   * IFIP is identified.
   *
   * @param node
   *          The node nfomation 
   * @param ifType
   *          The IF type
   * @param ifId
   *          IFID
   * @return IFIP
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
}
