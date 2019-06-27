/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.convert.EmMapper;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.EmController;
import msf.ecmm.emctrl.EmctrlException;
import msf.ecmm.emctrl.pojo.AbstractMessage;
import msf.ecmm.emctrl.pojo.InternalLinkLagIfsChange;
import msf.ecmm.emctrl.pojo.parts.Device;
import msf.ecmm.emctrl.pojo.parts.InternalInterface;
import msf.ecmm.emctrl.pojo.parts.XmlIntegerElement;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.CheckDataException;
import msf.ecmm.ope.receiver.pojo.CommonResponse;
import msf.ecmm.ope.receiver.pojo.InternalLinkChange;
import msf.ecmm.ope.receiver.pojo.parts.InternalLinkIfInternalLinkChange;
import msf.ecmm.ope.receiver.pojo.parts.UpdateInternalLinkIfs;

/**
 * Class for changing the internal link IF<br>
 * The internal link and the IF are changed.
 */
public class InternalLinkIfChange extends Operation {

  /** The name of the extended function operation */
  String operationName = "InternalLinkIfChange";

  /** In case the check of the input paramter is NG. */
  private static final String ERROR_CODE_470101 = "470101";

  /** In case the IF or the node  does not exist. */
  private static final String ERROR_CODE_470201 = "470201";

  /** 
   * In case the disconnection with EM has been detected or the timout has occurred.
   * when waiting for the response to EM request.
   */
  private static final String ERROR_CODE_470401 = "470401";

  /** In case the error response has been received from the EM when waiting for the response to EM request. */
  private static final String ERROR_CODE_470402 = "470402";
	
  /** In case an error has occurred when the DB is accessed. */
  private static final String ERROR_CODE_470403 = "470403";

  /**
   * Constructor.
   *
   * @param idt
   *          The input data
   * @param ukm
   *          URI Key information
   */
  public InternalLinkIfChange(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(operationName).getOperationTypeId());
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_470101);
    }

    try (DBAccessManager session = new DBAccessManager()) {

      InternalLinkChange inputData = (InternalLinkChange) getInData();

      Map<String, Nodes> nodeMap = new HashMap<>();
      for (UpdateInternalLinkIfs internalLinkIfs : inputData.getUpdateInternalLinkIfsList()) {
        if (!nodeMap.containsKey(internalLinkIfs.getNodeId())) {
          Nodes nodesDb = session.searchNodes(internalLinkIfs.getNodeId(), null);
          if (nodesDb == null) {
            logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db."));
            return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_470201);
          } else {
            nodeMap.put(internalLinkIfs.getNodeId(), nodesDb);
          }
        }
      }

      InternalLinkLagIfsChange internalLinkEm =  toInternalLinkEm(nodeMap, inputData);

      AbstractMessage result = EmController.getInstance().request(internalLinkEm);

      if (!result.isResult()) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Request to EM was failed."));
        return makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_470402);
      }

      response = makeSuccessResponse(RESP_OK_200, new CommonResponse());

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_470403);

    } catch (EmctrlException ee) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to EM was failed."), ee);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_470401);

    } catch (IllegalArgumentException iae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "No input data from db."), iae);
      response = makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_470201);
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    try {

      InternalLinkChange inputData = (InternalLinkChange) getInData();

      inputData.check(new OperationType(getOperationType()));

    } catch (CheckDataException cde) {
      logger.warn("check error :", cde);
      result = false;
    }

    logger.trace(CommonDefinitions.END);

    return result;
  }

  /**
   * The internal IF information is changed to the format with which the EM can received.
   *
   * @param nodeMap
   *          The node information list
   * @param inputData
   *          The input data
	* @return the internal link IF information(to send EM)）
   * @exception IllegalArgumentException
   *              The IF information does not exist.
   */
  private InternalLinkLagIfsChange toInternalLinkEm(Map<String, Nodes> nodeMap, InternalLinkChange inputData)
      throws IllegalArgumentException {

    logger.trace(CommonDefinitions.START);
    logger.debug(inputData + ", " + nodeMap);

    InternalLinkLagIfsChange internalLinkEm = new InternalLinkLagIfsChange();

    internalLinkEm.setName("internal-link");
    internalLinkEm.setDevice(new ArrayList<Device>());

    Map<String, List<InternalLinkIfInternalLinkChange>> internalIfMap = new HashMap<>();
    for (UpdateInternalLinkIfs internalIfs : inputData.getUpdateInternalLinkIfsList()) {
      if (!internalIfMap.containsKey(internalIfs.getNodeId())) {
        List<InternalLinkIfInternalLinkChange> internalIfList = new ArrayList<>();
        internalIfList.add(internalIfs.getInternalLinkIf());
        internalIfMap.put(internalIfs.getNodeId(), internalIfList);
      } else {
        internalIfMap.get(internalIfs.getNodeId()).add(internalIfs.getInternalLinkIf());
      }
    }

    for (String nodeId : internalIfMap.keySet()) {
      Device device = new Device();
      device.setName(nodeMap.get(nodeId).getNode_name());
      device.setInternalLagList(new ArrayList<InternalInterface>());

      for (InternalLinkIfInternalLinkChange ili : internalIfMap.get(nodeId)) {
        InternalInterface iif = new InternalInterface();
        iif.setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);
        if (ili.getIfType().equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)
            || ili.getIfType().equals(CommonDefinitions.IF_TYPE_BREAKOUT_IF)) {
          iif.setType(CommonDefinitions.IF_TYPE_PHYSICAL_IF);
        } else if (ili.getIfType().equals(CommonDefinitions.IF_TYPE_LAG_IF)) {
          iif.setType(CommonDefinitions.IF_TYPE_LAG_IF);
        }
        String ifName = EmMapper.getIfName(ili.getIfType(), ili.getIfId(), nodeMap.get(nodeId));

        if (ifName.equals("")) {
          throw new IllegalArgumentException();
        }

        iif.setName(ifName);
        XmlIntegerElement cost = new XmlIntegerElement();
        cost.setOperation(CommonDefinitions.OPERATION_TYPE_REPLACE);
        cost.setValue(ili.getCost());
        iif.setCost(cost);

        device.getInternalLagList().add(iif);
      }

      internalLinkEm.getDevice().add(device);
    }

    logger.debug(internalLinkEm);
    logger.trace(CommonDefinitions.END);

    return internalLinkEm;
  }

}
