/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.AclConf;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;

/**
 * LagIF Delete（ACL）.
 */
public class LagRemoveAcl extends LagRemove {

  /**
   * Constructor.
   *
   * @param idt
   *          Input Data
   * @param ukm
   *          URI Key Information
   */
  public LagRemoveAcl(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.LagRemove);
  }

  @Override
  protected boolean checkExpand(LagIfs lagIfsDb) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    boolean ret = true;
    try (DBAccessManager session = new DBAccessManager()) {
      String nodeId = lagIfsDb.getNode_id();
      String ifType = CommonDefinitions.IF_TYPE_LAG_IFS;
      String ifId = lagIfsDb.getLag_if_id();
      AclConf aclConf = session.getAclConf(nodeId, ifId, ifType);
      if (aclConf != null) {
        ret = false;
        logger.debug("target LAG if is set ACL.");
      }
    } catch (DBAccessException dbae) {
      logger.debug("DB Error at ACL expand function.", dbae);
      throw dbae;
    }
    logger.trace(CommonDefinitions.END);
    return ret;
  }
}
