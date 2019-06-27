/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import static msf.ecmm.common.CommonDefinitions.*;
import static msf.ecmm.common.CommonDefinitionsNodeOsUpgrade.*;
import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.config.EcConfiguration;
import msf.ecmm.fcctrl.RestClient;
import msf.ecmm.fcctrl.pojo.CommonResponseFromFc;
import msf.ecmm.fcctrl.pojo.UpgradeOperationToFc;
import msf.ecmm.ope.control.OperationControlManager;
import msf.ecmm.ope.receiver.pojo.RecoverNodeService;

/**
* Class for adding a node with the extended function to upgrade OS. 
 */
public class NodeAdditionThreadNodeOsUpgrade extends NodeAdditionThread {

  @Override
  protected String notifyAddNodeResult(boolean bootStatus, boolean executeResult, String nodeId)
      throws AddNodeException {
    logger.trace(CommonDefinitions.START);

    String status = "";

    try {
      RecoverNodeService recoverFcMessage = OperationControlManager.getInstance().getNodeAdditionInfo()
          .getRecoverNodeInfo();
      if (recoverFcMessage.getNode().getNodeUpgrade()) {

        UpgradeOperationToFc fcNotificationMessage = new UpgradeOperationToFc();
        fcNotificationMessage.setFabricType(recoverFcMessage.getNode().getNodeType());
        fcNotificationMessage.setNodeId(nodeId);
        fcNotificationMessage.setOperationType(OPETYPE_RECOVER_NODE);
        if (bootStatus == false) {
          status = RECV_NG_NOTIFICATION_STRING;
        } else {
          if (executeResult == true) {
            status = RECV_OK_NOTIFICATION_STRING_SUCCEEDED;
          } else {
            status = RECV_NG_NOTIFICATION_STRING;
          }
        }
        fcNotificationMessage.setOsUpgradeResult(status);

        EcConfiguration config = EcConfiguration.getInstance();
        String fcIpaddr = config.get(String.class, EcConfiguration.FC_ADDRESS);
        String fcPort = config.get(String.class, EcConfiguration.FC_PORT);

        new RestClient().request(fcIpaddr, fcPort, RestClient.PUT, NODE_OS_UPGRADE_NOTIFICATION_URI,
            fcNotificationMessage, CommonResponseFromFc.class);

      } else {
        status = super.notifyAddNodeResult(bootStatus, executeResult, nodeId);
      }
    } catch (Exception exp) {
      logger.debug("Error occurred.", exp);
      throw new AddNodeException();
    }

    logger.debug("status=" + status);
    logger.trace(CommonDefinitions.END);
    return status;
  }
}
