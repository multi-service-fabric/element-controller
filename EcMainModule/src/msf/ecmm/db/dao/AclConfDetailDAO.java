/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.AclConfDetail;

/**
 * Class which performs DB processing related to the ACL advanced setting information.
 */
public class AclConfDetailDAO extends BaseDAO {

  /**
   * ACL configuration setting information DAO constructor.
   *
   * @param session
   *          database session
   */
  public AclConfDetailDAO(Session session) {
    this.session = session;
  }

  /**
   * ACL configuration details information table INSERT.
   *
   * @param aclConfDetail
   *          ACL advanced setting informaiton that you want to register
   * @throws DBAccessException
   *           database exception
   *
   */
  public void save(AclConfDetail aclConfDetail) throws DBAccessException {
    try {
      session.save(aclConfDetail);
    } catch (Throwable e2) {
      logger.debug("aclConfDetail insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, ACL_DETAIL_INFO, e2);
    }
  }

  /**
   * ACL configuration details information table DELETE.
   *
   * @param nodeId
   *          device ID
   * @param aclId
   *          ACL configuration ID
   * @param termNameList
   *          term name list
   * @throws DBAccessException
   *           database exception
   */
  public void delete(String nodeId, String aclId, List<String> termNameList) throws DBAccessException {
    try {
      Query query = null;
      for (String termName : termNameList) {

        query = session.getNamedQuery("deleteAclDetailInfo");
        query.setString("key1", nodeId);
        query.setString("key2", aclId);
        query.setString("key3", termName);
        query.executeUpdate();
      }
    } catch (Throwable e2) {
      logger.debug("acl_detail_info delete failed", e2);
      this.errorMessage(DELETE_FAILURE, ACL_DETAIL_INFO, e2);
    }

  }

  /**
   * ACL configuration details information table SELECT(condition: Device ID).
   *
   * @param nodeId
   *          Device ID
   * @return aclConfDetailList ACL configuration details information list
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public List<AclConfDetail> getList(String nodeId) throws DBAccessException {
    List<AclConfDetail> aclConfDetailList = new ArrayList<AclConfDetail>();
    try {
      Query query = session.getNamedQuery("selectAclConfDetailAll");
      query.setString("key1", nodeId);
      aclConfDetailList = query.list();
    } catch (Throwable e1) {
      logger.debug("acl_detail_info.", e1);
      this.errorMessage(SEARCH_FAILURE, ACL_DETAIL_INFO, e1);
    }
    return aclConfDetailList;
  }

  /**
   * ACL configuration details information table SELECT.
   *
   * @param nodeId
   *          Device ID（main key 1)
   * @param aclId
   *          ACL configuration ID（main key 2)
   * @return aclConfDetailList
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public List<AclConfDetail> search(String nodeId, String aclId) throws DBAccessException {
    List<AclConfDetail> aclConfDetailList = new ArrayList<AclConfDetail>();
    try {
      Query query = session.getNamedQuery("selectAclConfDetail");
      query.setString("key1", nodeId);
      query.setString("key2", aclId);
      aclConfDetailList = query.list();
    } catch (Throwable e1) {
      logger.debug("aclDetailInfo select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, ACL_DETAIL_INFO, e1);
    }
    return aclConfDetailList;
  }

  /**
   * ACL configuration details information table SELECT (for confirmation before DELETE).
   *
   * @param nodeId
   *          device ID（main key 1)
   * @param aclId
   *          ACL configuration ID（main key 2)
   * @param termName
   *          term name（main key 3)
   * @return aclConfDetailList
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public AclConfDetail searchPreDelete(String nodeId, String aclId, String termName) throws DBAccessException {
    List<AclConfDetail> aclConfDetailList = new ArrayList<AclConfDetail>();
    AclConfDetail aclConfDetail = new AclConfDetail();
    try {
      Query query = session.getNamedQuery("selectPreDeleteAclConfDetail");
      query.setString("key1", nodeId);
      query.setString("key2", aclId);
      query.setString("key3", termName);
      aclConfDetailList = query.list();
      if (!(aclConfDetailList == null) && aclConfDetailList.size() == 1) {
        aclConfDetail = aclConfDetailList.get(0);
      }
    } catch (Throwable e1) {
      logger.debug("aclDetailInfo select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, ACL_DETAIL_INFO, e1);
    }
    return aclConfDetail;
  }
}
