/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.allinfo;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.emctrl.restpojo.ConfigAuditFromEm;
import msf.ecmm.fcctrl.RestClientException;
import msf.ecmm.ope.receiver.pojo.GetConfigAuditList;
import msf.ecmm.ope.receiver.pojo.parts.Diff;
import msf.ecmm.ope.receiver.pojo.parts.LatestEmConfig;
import msf.ecmm.ope.receiver.pojo.parts.NeConfig;
import msf.ecmm.ope.receiver.pojo.parts.NodeConfig;
import msf.ecmm.ope.receiver.pojo.parts.NodeConfigAll;

/**
 * Executing cyclic Config-Audit.
 */
public class AllConfigAuditUtility {

  /** Logger. */
  protected static final MsfLogger logger = new MsfLogger();

  /** Instance list of thread process instance. */
  private List<AllConfigAuditAcquisitionThread> threadInstanceList = new ArrayList<>();

  /** Result of received Config-Audit. */
  private List<ConfigAuditFromEm> resultList = new ArrayList<>();

  /** Error code list. */
  private List<Integer> errorList = new ArrayList<>();

  /**
   * Getting Config-Audit list.
   *
   * @return Result of received Config-Audit(for sending REST)
   * @throws DBAccessException
   *           DB access error
   * @throws RestClientException
   *           EM REST request error
   */
  public GetConfigAuditList execute() throws DBAccessException, RestClientException {


    GetConfigAuditList outputData = new GetConfigAuditList();

    try (DBAccessManager session = new DBAccessManager()) {

      List<Nodes> nodesList = session.getNodesList();

      if (nodesList.isEmpty()) {
        logger.debug("Nodes is unregistered.");
        return outputData;
      }

      for (Nodes node : nodesList) {
        AllConfigAuditAcquisitionThread thread = new AllConfigAuditAcquisitionThread();
        thread.setHostname(node.getHost_name());
        thread.setInstance(this);
        threadInstanceList.add(thread);
      }

      for (AllConfigAuditAcquisitionThread thread : threadInstanceList) {
        thread.start();
      }
      for (AllConfigAuditAcquisitionThread thread : threadInstanceList) {
        try {
          thread.join();
        } catch (InterruptedException ie) {
        }
      }

      if (!errorList.isEmpty()) {
        logger.debug("errorList=" + errorList);
        throw new RestClientException(errorList.get(0));
      }

      outputData = toConfigAuditList(resultList, nodesList);

    } catch (DBAccessException | RestClientException exp) {
      logger.debug("Error Occurred. Exception is thrown.");
      throw exp;
    }
    return outputData;
  }

  /**
   * Registering result.
   *
   * @param result
   *          result
   */
  protected void setResult(ConfigAuditFromEm result) {
    synchronized (resultList) {
      resultList.add(result);
    }
  }

  /**
   * Registering result(in case of failure).
   *
   * @param errorCode
   *          error code
   */
  protected void setResult(int errorCode) {
    synchronized (errorList) {
      errorList.add(errorCode);
    }
  }

  /**
   * Getting Config-Audit list of REST data mapping.
   *
   * @param resultList
   *          result
   * @param nodesList
   *          node list
   * @return result of Config-Audi list received(for REST sending)
   */
  private GetConfigAuditList toConfigAuditList(List<ConfigAuditFromEm> resultList, List<Nodes> nodesList) {

    logger.trace(CommonDefinitions.START);

    GetConfigAuditList ret = new GetConfigAuditList();

    for (ConfigAuditFromEm result : resultList) {
      for (Nodes node : nodesList) {
        if (result.getHostname().equals(node.getHost_name())) {
          NodeConfigAll nodeConfigAll = new NodeConfigAll();
          NodeConfig nodeConfig = new NodeConfig();
          nodeConfig.setNodeId(node.getNode_id());

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

          nodeConfigAll.setNode(nodeConfig);
          ret.getNodes().add(nodeConfigAll);
          break;
        }
      }
    }

    logger.debug(ret);
    logger.trace(CommonDefinitions.END);

    return ret;

  }
}
