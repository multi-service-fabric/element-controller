/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.convert.LogicalPhysicalConverter;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.AclConf;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;

/**
 * Inter-cluster Link Delete（ACL）.
 */
public class BetweenClustersLinkDeleteAcl extends BetweenClustersLinkDelete {

  /**
   * Constructor.
   *
   * @param idt
   *          Input Data
   * @param ukm
   *          URI Key Information
   */
  public BetweenClustersLinkDeleteAcl(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.BetweenClustersLinkDelete);
  }

  @Override
  protected boolean checkExpand(String nodeId, String ifType, String ifId) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    boolean ret = true;
    try (DBAccessManager session = new DBAccessManager()) {
      if (ifType.equals(CommonDefinitions.IF_TYPE_PHYSICAL_IF)) {
        AclConf aclConf = session.getAclConf(nodeId, ifId, LogicalPhysicalConverter.convertIfIfs(ifType));
        if (aclConf != null) {
          ret = false;
          logger.debug("target physical if is set ACL.");
        }
      }
    } catch (DBAccessException dbae) {
      logger.debug("DB Error at ACL expand function.", dbae);
      throw dbae;
    }
    logger.trace(CommonDefinitions.END);
    return ret;
  }
}
