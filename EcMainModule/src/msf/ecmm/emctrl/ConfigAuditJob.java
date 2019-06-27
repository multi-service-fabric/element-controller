/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.emctrl;

import java.util.ArrayList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.LogFormatter;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.convert.RestMapper;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.pojo.AbstractRequest;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.ConfigAuditNotification;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.execute.constitution.allinfo.AllConfigAuditUtility;
import msf.ecmm.ope.receiver.pojo.GetConfigAuditList;
import msf.ecmm.ope.receiver.pojo.parts.NodeConfigAll;

/**
 * Class of the cyclic Config-Audit monitoring job.
 */
public class ConfigAuditJob implements Job {

  /** Logger. */
  private static final MsfLogger logger = new MsfLogger();

  @Override
  public void execute(JobExecutionContext arg0) throws JobExecutionException {

    logger.trace(CommonDefinitions.START);
    logger.info(LogFormatter.out.format(LogFormatter.MSG_304105));

    boolean startResult = OperationControlManager.getInstance().startConfigAudit();
    boolean stopFlag = ConfigAuditCycleManager.getInstance().isStopFlag();

    try {
      if (startResult && !stopFlag) {


        String address = EcConfiguration.getInstance().get(String.class,
            EcConfiguration.CONFIG_AUDIT_MONITOR_NOTIFY_ADDRESS);
        String port = EcConfiguration.getInstance().get(String.class, EcConfiguration.CONFIG_AUDIT_MONITOR_NOTIFY_PORT);
        String url = EcConfiguration.getInstance().get(String.class, EcConfiguration.CONFIG_AUDIT_MONITOR_NOTIFY_URL);

        if (address == null) {
          address = EcConfiguration.getInstance().get(String.class, EcConfiguration.FC_ADDRESS);
        }
        if (port == null) {
          port = EcConfiguration.getInstance().get(String.class, EcConfiguration.FC_PORT);
        }

        AllConfigAuditUtility utility = new AllConfigAuditUtility();
        GetConfigAuditList outputData = utility.execute();

        List<NodeConfigAll> diffList = new ArrayList<>();
        for (NodeConfigAll nodeConfig : outputData.getNodes()) {
          if (!nodeConfig.getNode().getDiff().getDiffDataUnified().equals("")) {

            diffList.add(nodeConfig);
          }
        }

        if (!diffList.isEmpty()) {
          GetConfigAuditList emresp = new GetConfigAuditList();
          emresp.setNodes(diffList);
          ConfigAuditNotification requestData = RestMapper.toConfigAuditNotification(emresp);
          RestClient rc = new RestClient();
          rc.request(address, port, RestClient.PUT, url, (AbstractRequest) requestData, CommonResponseFromFc.class);
        }

      }
    } catch (Exception exp) {
      logger.warn(LogFormatter.out.format(LogFormatter.MSG_404103), exp);
    }

    if (startResult) {
      OperationControlManager.getInstance().endConfigAudit();
      logger.debug("Config Audit notification stop.");
    } else {
      logger.debug("Config Audit notification skip.");
    }

    logger.trace(CommonDefinitions.END);
  }

}
