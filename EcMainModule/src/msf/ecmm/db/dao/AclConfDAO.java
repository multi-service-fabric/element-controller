/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.AclConf;
import msf.ecmm.db.pojo.AclConfDetail;

/**
 * Class which performs DB processing related to the ACL configuration information.
 */
public class AclConfDAO extends BaseDAO {

  /**
   * ACL configuraiton information DAO constructor.
   *
   * @param session
   *          database session
   */
  public AclConfDAO(Session session) {
    this.session = session;
  }

  /**
   * ACL configuration information table INSERT.
   *
   * @param aclConf
   *         ACL configuration information that you want to register
   * @throws DBAccessException
   *           database exceptions
   */
  public void save(AclConf aclConf) throws DBAccessException {
    try {
      session.save(aclConf);
    } catch (Throwable e2) {
      logger.debug("aclConf insert failed", e2);
      this.errorMessage(INSERT_FAILURE, ACL_INFO, e2);
    }
  }

  /**
   * ACL configuraiton informaiton table DELETE.
   *
   * @param nodeId
   *          Device ID
   * @param ifId
   *          IF ID
   * @param ifType
   *          IF type
   * @param termNameList
   *          term name list
   * @throws DBAccessException
   *           database exception
   */
  public void delete(String nodeId, String ifId, String ifType, List<String> termNameList) throws DBAccessException {
    try {
      Query query = null;
      AclConf aclConf = this.search(nodeId, ifId, ifType);
      if (aclConf == null) {
        this.errorMessage(NO_DELETE_TARGET, ACL_INFO, null);
      }

      AclConfDetailDAO aclConfDetailDao = new AclConfDetailDAO(session);

      for (String termName : termNameList) {
        AclConfDetail aclConfDetail = new AclConfDetail();
        aclConfDetail = aclConfDetailDao.searchPreDelete(nodeId, aclConf.getAcl_id(), termName);

        if (aclConfDetail.getNode_id() == null || aclConfDetail.getAcl_id() == null
            || aclConfDetail.getTerm_name() == null) {
          this.errorMessage(NO_DELETE_TARGET, ACL_DETAIL_INFO, null);
        }
      }

      aclConfDetailDao.delete(nodeId, aclConf.getAcl_id(), termNameList);

      List<AclConfDetail> aclConfDetailList = aclConfDetailDao.search(nodeId, aclConf.getAcl_id());

      if (aclConfDetailList.size() == 0) {
        query = session.getNamedQuery("deleteConectAclConfDetail");
        query.setString("key1", nodeId);
        query.setString("key2", aclConf.getAcl_id());
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("acl_info delete failed", e2);
      this.errorMessage(DELETE_FAILURE, ACL_DETAIL_INFO, e2);
    }
  }

  /**
   * ACL configuration information table SELECT(condition: device ID).
   *
   * @param nodeId
   *          device ID
   * @return aclConfList ACL configuration informaiton list
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public List<AclConf> getList(String nodeId) throws DBAccessException {
    List<AclConf> aclConfList = new ArrayList<AclConf>();
    try {
      Query query = session.getNamedQuery("selectAclConfAll");
      query.setString("key1", nodeId);
      aclConfList = query.list();
    } catch (Throwable e1) {
      logger.debug("acl_info select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, ACL_INFO, e1);
    }
    return aclConfList;
  }

  /**
   * ACL configuration information table SELECT.
   *
   * @param nodeId
   *          device ID(main key 1)
   * @param ifId
   *          IF ID(main key 2)
   * @param ifType
   *          IF type (main key 3)
   * @return aclConf search result
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public AclConf search(String nodeId, String ifId, String ifType) throws DBAccessException {
    AclConf aclConf = null;
    try {
      Query query;
      if (ifType.equals("physical-ifs")) {
        query = session.getNamedQuery("selectPhysicalAclConf");
      } else if (ifType.equals("lag-ifs")) {
        query = session.getNamedQuery("selectLagAclConf");
      } else if (ifType.equals("vlan-ifs")) {
        query = session.getNamedQuery("selectVlanAclConf");
      } else {
        query = session.getNamedQuery("selectBreakoutAclConf");
      }

      query.setString("key1", nodeId);
      query.setString("key2", ifId);

      List<AclConf> aclConfList = query.list();
      if (!(aclConfList == null) && aclConfList.size() == 1) {
        aclConf = aclConfList.get(0);
        aclConf.setNode_id(aclConf.getNode_id());
      }
    } catch (Throwable e1) {
      logger.debug("aclInfo select failed", e1);
      this.errorMessage(SEARCH_FAILURE, ACL_INFO, e1);
    }
    return aclConf;
  }
}
