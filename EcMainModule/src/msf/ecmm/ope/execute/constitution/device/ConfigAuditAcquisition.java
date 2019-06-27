/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.ope.receiver.ReceiverDefinitions.*;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.config.ExpandOperation;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.RestClientToEm;
import msf.ecmm.emctrl.restpojo.AbstractRequest;
import msf.ecmm.emctrl.restpojo.ConfigAuditFromEm;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.ope.execute.Operation;
import msf.ecmm.ope.receiver.pojo.AbstractResponseMessage;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.GetConfigAudit;
import msf.ecmm.ope.receiver.pojo.parts.Diff;
import msf.ecmm.ope.receiver.pojo.parts.LatestEmConfig;
import msf.ecmm.ope.receiver.pojo.parts.NeConfig;
import msf.ecmm.ope.receiver.pojo.parts.NodeConfig;

/**
 * The config-Audit Acquisition is executed.
 */
public class ConfigAuditAcquisition extends Operation {

  /** The name of the extended function operation */
  String operationName = "ConfigAuditAcquisition";

  /** In case the check of the input paramter is NG. */
  private static final String ERROR_CODE_630101 = "630101";

  /** In case the node  does not exist. */
  private static final String ERROR_CODE_630201 = "630201";

  /** 
   * In case the disconnection with the EM  or  the timeout error have occurred during
   * when waiting for the response to the acquisition request of the Config-Audit listt.
   */
  private static final String ERROR_CODE_630401 = "630401";

  /** In case an error has occurred when the DB is accessed. */
  private static final String ERROR_CODE_630402 = "630402";

  /**
   * In case the error message has been received from EM
   * when waiting for the response to the acquisition request of the Config-Audit list.
   */
  private static final String ERROR_CODE_630403 = "630403";

  /**
   * Constructor.
   *
   * @param idt
   *          The input data
   * @param ukm
   *          URI key information
   */
  public ConfigAuditAcquisition(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(ExpandOperation.getInstance().get(operationName).getOperationTypeId());
  }

  @Override
  public AbstractResponseMessage execute() {

    logger.trace(CommonDefinitions.START);

    AbstractResponseMessage response = null;

    GetConfigAudit outputData = null;

    if (!checkInData()) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Input data wrong."));
      return makeFailedResponse(RESP_BADREQUEST_400, ERROR_CODE_630101);
    }

    String nodeId = getUriKeyMap().get(KEY_NODE_ID);

    try (DBAccessManager session = new DBAccessManager()) {

      Nodes nodesDb = session.searchNodes(nodeId, null);
      if (nodesDb == null) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Not found data. [Nodes]"));
        return makeFailedResponse(RESP_NOTFOUND_404, ERROR_CODE_630201);
      }

      AbstractRequest abstractRequest = new AbstractRequest();
      ConfigAuditFromEm result = new ConfigAuditFromEm();

      HashMap<String, String> keyMap = new HashMap<>();
      keyMap.put(CommonDefinitions.KEY_HOSTNAME, nodesDb.getHost_name());

      result = (ConfigAuditFromEm) new RestClientToEm().request(RestClientToEm.CONFIG_AUDIT, keyMap, abstractRequest,
          ConfigAuditFromEm.class);

      outputData = toConfigAudit(result, nodesDb);

      response = makeSuccessResponse(RESP_OK_200, outputData);

    } catch (DBAccessException dbae) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_403041, "Access to DB was failed."), dbae);
      response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_630402);
    } catch (RestClientException rce) {
      if (rce.getCode() == RestClientException.ERROR_RESPONSE) {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST request"), rce);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_630403);
      } else {
        logger.warn(LogFormatter.out.format(LogFormatter.MSG_504056, "EM REST timeout"), rce);
        response = makeFailedResponse(RESP_INTERNALSERVERERROR_500, ERROR_CODE_630401);
      }
    }

    logger.trace(CommonDefinitions.END);

    return response;
  }

  @Override
  protected boolean checkInData() {

    logger.trace(CommonDefinitions.START);

    boolean result = true;

    if (getUriKeyMap() == null) {
      result = false;
    } else {
      if (!(getUriKeyMap().containsKey(KEY_NODE_ID)) || getUriKeyMap().get(KEY_NODE_ID) == null) {
        result = false;
      }
    }

    logger.trace(CommonDefinitions.END);
    return result;

  }

  /**
   * The Config-Audit acquisition for REST data mapping is executed.
   *
   * @param result
   *          Result
   * @param nodes
   *          The node infomation
   * @return  The Config-Audit acquisition result for REST
   */
  private GetConfigAudit toConfigAudit(ConfigAuditFromEm result, Nodes nodes) {

    logger.trace(CommonDefinitions.START);

    NodeConfig nodeConfig = new NodeConfig();
    nodeConfig.setNodeId(nodes.getNode_id());

    LatestEmConfig latestEmConfig = null;
    if (result.getLatestEmConfig() != null) {
      latestEmConfig = new LatestEmConfig();
      latestEmConfig.setDate(result.getLatestEmConfig().getDate());
      latestEmConfig.setServerName(result.getLatestEmConfig().getServerName());
      latestEmConfig.setConfig(result.getLatestEmConfig().getConfig());
    }

    NeConfig neConfig = new NeConfig();
    neConfig.setDate(result.getNeConfig().getDate());
    neConfig.setConfig(result.getNeConfig().getConfig());

    Diff diff = new Diff();
    diff.setDiffDataUnified(result.getDiff().getDiffDataUnified());

    nodeConfig.setLatestEmConfig(latestEmConfig);
    nodeConfig.setNeConfig(neConfig);
    nodeConfig.setDiff(diff);

    GetConfigAudit ret = new GetConfigAudit();
    ret.setNode(nodeConfig);

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;

  }
}
