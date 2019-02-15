/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.ope.execute.constitution.interfaces;

import java.util.HashMap;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.DBAccessManager;
import msf.ecmm.db.pojo.AclConf;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.ope.execute.OperationType;
import msf.ecmm.ope.receiver.pojo.AbstractRestMessage;
import msf.ecmm.ope.receiver.pojo.UpdatePhysicalInterface;

/**
 * Physical IF Information Change(ACL).
 */
public class PhysicalIfInfoChangeAcl extends PhysicalIfInfoChange {

  /**
   * Constructor.
   *
   * @param idt
   *          Input Data
   * @param ukm
   *          URI Key Information
   */
  public PhysicalIfInfoChangeAcl(AbstractRestMessage idt, HashMap<String, String> ukm) {
    super(idt, ukm);
    super.setOperationType(OperationType.PhysicalIfInfoChange);
  }

  @Override
  protected boolean checkExpand(PhysicalIfs physicalIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    UpdatePhysicalInterface inputData = (UpdatePhysicalInterface) getInData();
    if (inputData.getAction().equals(CommonDefinitions.SPEED_SET)) {
      return true;
    }

    boolean ret = true;
    try (DBAccessManager session = new DBAccessManager()) {
      String nodeId = physicalIfs.getNode_id();
      String ifType = CommonDefinitions.IF_TYPE_PHYSICAL_IFS;
      String ifId = physicalIfs.getPhysical_if_id();
      AclConf aclConf = session.getAclConf(nodeId, ifId, ifType);
      if (aclConf != null) {
        ret = false;
        logger.debug("target physical if is set ACL.");
      }
    } catch (DBAccessException dbae) {
      logger.debug("DB Error at ACL expand function.", dbae);
      throw dbae;
    }
    logger.trace(CommonDefinitions.END);
    return ret;
  }
}

