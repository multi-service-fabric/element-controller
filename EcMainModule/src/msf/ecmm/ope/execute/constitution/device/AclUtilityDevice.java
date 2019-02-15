/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.device;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.convert.LogicalPhysicalConverter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.AclConf;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.ope.receiver.pojo.DeleteNode;
import msf.ecmm.ope.receiver.pojo.parts.OppositeNodesDeleteNode;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ACL Utility Class (Device).
 */
public class AclUtilityDevice {

  /** Logger. */
  private static final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);

  /**
   * Judging whether removal is possible or impossible at using ACL.
   * @param nodes  Information of the device to be removed
   * @param deleteNodeRest  Removal request（REST）
   * @return  Judgment Results
   * @throws DBAccessException  In case abnormality has occurred in DB
   */
  protected static boolean checkRemoveNode(DeleteNode deleteNodeRest, Nodes nodes) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    boolean ret = false;


    try (DBAccessManager session = new DBAccessManager()) {

      List<AclConf> aclConfList = session.getAclConfList(nodes.getNode_id());
      Set<String> physicalAclBaseIfIdSet = new HashSet<>();
      Set<String> lagAclBaseIfIdSet = new HashSet<>();
      for (AclConf aclConf : aclConfList) {
        if (aclConf.getPhysical_if_id() != null) {
          physicalAclBaseIfIdSet.add(aclConf.getPhysical_if_id());
        } else if (aclConf.getLag_if_id() != null) {
          lagAclBaseIfIdSet.add(aclConf.getLag_if_id());
        }
      }
      NG_FOUND:
      do {
        for (PhysicalIfs physIfs : nodes.getPhysicalIfsList()) {
          if (physicalAclBaseIfIdSet.contains(physIfs.getPhysical_if_id())) {
            logger.debug("delete target nodes lag inner if is set ACL. IFID=" + physIfs.getPhysical_if_id());
            break NG_FOUND;
          }
        }
        for (LagIfs lagIfs : nodes.getLagIfsList()) {
          if (lagAclBaseIfIdSet.contains(lagIfs.getLag_if_id())) {
            logger.debug("delete target nodes physical inner if is set ACL. IFID=" + lagIfs.getLag_if_id());
            break NG_FOUND;
          }
        }

        for (OppositeNodesDeleteNode oppo : deleteNodeRest.getDeleteNodes().getOppositeNodes()) {
          String nodeId = oppo.getNodeId();
          String ifType = LogicalPhysicalConverter.convertIfIfs(oppo.getInternalLinkIf().getIfInfo().getIfType());
          String ifId = oppo.getInternalLinkIf().getIfInfo().getIfId();
          AclConf aclConf = session.getAclConf(nodeId, ifId, ifType);
          if (aclConf != null) {
            logger.debug("delete opposite nodes inner if is set ACL. NODEID="
                          + nodeId + " IFTYPE=" + ifType + " IFID=" + ifId);
            break NG_FOUND;
          }
        }

        ret = true;
      }
      while (false);

    } catch (DBAccessException dbae) {
      logger.debug("DB Error at ACL expand function.", dbae);
      throw dbae;
    }
    logger.trace(CommonDefinitions.END);
    return ret;
  }
}
