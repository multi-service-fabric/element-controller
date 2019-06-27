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
import msf.ecmm.db.pojo.IRBInstanceInfo;

/**
 * IRB instance information DAO class.
 */
public class IRBInstanceInfoDAO extends BaseDAO {

  /**
   * IRB instance infomratio DAO constructor.
   *
   * @param session
   *          database session
   */
  public IRBInstanceInfoDAO(Session session) {
    this.session = session;
  }

  /**
   * IRB instance information table INSERT.
   *
   * @param irbInstanceInfo
   *          IRB instance information
   * @throws DBAccessException
   *           database exception
   */
  public void save(IRBInstanceInfo irbInstanceInfo) throws DBAccessException {
    try {
      IRBInstanceInfo regIrbInstanceInfo = this.search(irbInstanceInfo.getNode_id(), irbInstanceInfo.getVlan_id());
      if (regIrbInstanceInfo != null) {
        this.errorMessage(DOUBLE_REGISTRATION, IRB_INSTANCE_INFO, null);
      } else {
        session.save(irbInstanceInfo);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("irbInstanceInfo insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, IRB_INSTANCE_INFO, e2);
    }
  }

  /**
   * IRB instance information table UPDATE.
   *
   * @param irbInstanceInfo
   *          IRB instance information
   * @throws DBAccessException
   *           database exception
   */
  public void updateState(IRBInstanceInfo irbInstanceInfo) throws DBAccessException {
    try {
      IRBInstanceInfo regIrbInstanceInfo = this.search(irbInstanceInfo.getNode_id(), irbInstanceInfo.getVlan_id());
      if (regIrbInstanceInfo == null) {
        this.errorMessage(NO_UPDATE_TARGET, IRB_INSTANCE_INFO, null);
      } else {
        Query query = session.getNamedQuery("updateIRBInstanceInfoState");
        query.setString("key1", irbInstanceInfo.getNode_id());
        query.setString("key2", irbInstanceInfo.getIrb_vni());
        query.setString("key3", irbInstanceInfo.getIrb_ipv4_address());
        query.setInteger("key4", irbInstanceInfo.getIrb_ipv4_prefix());
        query.setString("key5", irbInstanceInfo.getVirtual_gateway_address());
        query.setString("key6", irbInstanceInfo.getVlan_id());

        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("irb_instance_info", e2);
      this.errorMessage(UPDATE_FAILURE, IRB_INSTANCE_INFO, e2);
    }
  }

  /**
   * IRB instance information table DELETE.
   *
   * @param nodeId
   *          device ID(primary key 1)
   * @param vlanId
   *          VLAN ID(primary key 2)
   * @throws DBAccessException
   *           database exception
   */
  public void delete(String nodeId, String vlanId) throws DBAccessException {
    try {
      Query query = null;
      IRBInstanceInfo irbInstanceInfo = this.search(nodeId, vlanId);
      if (irbInstanceInfo == null) {
        this.errorMessage(NO_DELETE_TARGET, IRB_INSTANCE_INFO, null);
      }
      query = session.getNamedQuery("deleteIRBInstanceInfo");
      query.setString("key1", nodeId);
      query.setString("key2", vlanId);
      query.executeUpdate();
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("irb_instance_info delete failed", e2);
      this.errorMessage(DELETE_FAILURE, IRB_INSTANCE_INFO, e2);
    }

  }

  /**
   * IRB instance information table SELECT(acquire only IRB instance ID).
   *
   * @return irbInstanceInfoList IRB instance information list
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public List<String> getList() throws DBAccessException {

    List<IRBInstanceInfo> irbInstanceInfoList = new ArrayList<IRBInstanceInfo>();
    List<String> irbInstanceIdList = new ArrayList<String>();

    try {
      Query query = session.getNamedQuery("selectIRBInstanceId");
      irbInstanceInfoList = query.list();
      for (IRBInstanceInfo irbInstanceInfo : irbInstanceInfoList) {
        irbInstanceIdList.add(irbInstanceInfo.getIrb_instance_id());
      }
    } catch (Throwable e1) {
      logger.debug("irb_instance_info select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, IRB_INSTANCE_INFO, e1);
    }
    return irbInstanceIdList;
  }

  /**
   * IRB instance information table SELECT.
   *
   * @param nodeId
   *          device ID(primary key 1)
   * @param vlanId
   *          VLAN ID(primary key 2)
   * @return irbInstanceInfo  search result
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public IRBInstanceInfo search(String nodeId, String vlanId) throws DBAccessException {

    IRBInstanceInfo irbInstanceInfo = null;
    try {
      Query query = session.getNamedQuery("selectIRBInstanceInfo");
      query.setString("key1", nodeId);
      query.setString("key2", vlanId);
      List<IRBInstanceInfo> irbInstanceInfoList = query.list();
      if (!(irbInstanceInfoList == null) && irbInstanceInfoList.size() == 1) {
        irbInstanceInfo = irbInstanceInfoList.get(0);
        irbInstanceInfo.setNode_id(irbInstanceInfo.getNode_id());
        irbInstanceInfo.setVlan_id(irbInstanceInfo.getVlan_id());
      }
    } catch (Throwable e1) {
      logger.debug("irbInstanceInfo select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, IRB_INSTANCE_INFO, e1);
    }
    return irbInstanceInfo;
  }
}
