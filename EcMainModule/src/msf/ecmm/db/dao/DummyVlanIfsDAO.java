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
import msf.ecmm.db.pojo.DummyVlanIfs;

/**
 * Dummy VLAN IF information DAO class.
 */
public class DummyVlanIfsDAO extends BaseDAO {

  /**
   * Dummy VLAN IF information DAO constructor.
   *
   * @param session
   *          database session
   */
  public DummyVlanIfsDAO(Session session) {
    this.session = session;
  }

  /**
   * Dummy VLAN IF information table INSERT.
   *
   * @param dummyVlanIfsInfo
   *          Dummy VLAN IF information that you want to register
   * @throws DBAccessException
   *           database exception
   */
  public void save(DummyVlanIfs dummyVlanIfsInfo) throws DBAccessException {
    try {
      DummyVlanIfs regDummyVlanIfsInfo = this.search(dummyVlanIfsInfo.getNode_id(), dummyVlanIfsInfo.getVlan_if_id());
      if (regDummyVlanIfsInfo != null) {
        this.errorMessage(DOUBLE_REGISTRATION, DUMMY_VLAN_IFS_INFO, null);
      } else {
        session.save(dummyVlanIfsInfo);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("dummyVlanIfsInfo insert failed", e2);
      this.errorMessage(INSERT_FAILURE, DUMMY_VLAN_IFS_INFO, e2);
    }
  }

  /**
   * Dummy VLAN IF information table DELETE.
   *
   * @param nodeId
   *          device ID
   * @param vlanIfId
   *          VLAN IF ID
   * @throws DBAccessException
   *           database exception
   */
  public void delete(String nodeId, String vlanIfId) throws DBAccessException {
    try {
      Query query = null;
      DummyVlanIfs dummyVlanIfsInfo = this.search(nodeId, vlanIfId);
      if (dummyVlanIfsInfo == null) {
        this.errorMessage(NO_DELETE_TARGET, DUMMY_VLAN_IFS_INFO, null);
      }
      query = session.getNamedQuery("deleteDummyVlanIfsInfo");
      query.setString("key1", nodeId);
      query.setString("key2", vlanIfId);
      query.executeUpdate();
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("dummy_vlan_ifs_info delete failed");
      this.errorMessage(DELETE_FAILURE, DUMMY_VLAN_IFS_INFO, e2);
    }
  }

  /**
   * Dummy VLAN IF information table UPDATE.
   *
   * @param dummyVlanIfsInfo
   *          dummy VLAN IF information
   * @throws DBAccessException
   *           database exception
   */
  public void updateState(DummyVlanIfs dummyVlanIfsInfo) throws DBAccessException {
    try {
      DummyVlanIfs regDummyVlanIfsInfo = this.search(dummyVlanIfsInfo.getNode_id(), dummyVlanIfsInfo.getVlan_if_id());
      if (regDummyVlanIfsInfo == null) {
        this.errorMessage(NO_UPDATE_TARGET, DUMMY_VLAN_IFS_INFO, null);
      } else {
        Query query = session.getNamedQuery("updateDummyVlanIfsInfoState");
        query.setString("key1", dummyVlanIfsInfo.getNode_id());
        query.setString("key2", dummyVlanIfsInfo.getVlan_if_id());
        query.setString("key3", dummyVlanIfsInfo.getVlan_id());
        query.setString("key4", dummyVlanIfsInfo.getIrb_instance_id());
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("dummy_vlan_ifs_info", e2);
      this.errorMessage(UPDATE_FAILURE, DUMMY_VLAN_IFS_INFO, e2);
    }
  }

  /**
   * Dummy VLAN IF information table SELECT(condition: device ID).
   *
   * @param nodeId
   *          device ID
   * @return dummyVlanInstanceIfsList Dummy VLAN IF information list
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public List<DummyVlanIfs> getList(String nodeId) throws DBAccessException {
    List<DummyVlanIfs> dummyVlanIfsList = new ArrayList<DummyVlanIfs>();
    try {
      Query query = session.getNamedQuery("selectDummyVlanIfsAll");
      query.setString("key1", nodeId);
      dummyVlanIfsList = query.list();
    } catch (Throwable e1) {
      logger.debug("dummy_vlan_ifs_info.", e1);
      this.errorMessage(SEARCH_FAILURE, DUMMY_VLAN_IFS_INFO, e1);
    }
    return dummyVlanIfsList;
  }

  /**
   * Dummy VLAN IF information table SELECT.
   *
   * @param nodeId
   *          device ID(primary key 1)
   * @param vlanIfId
   *          VLAN IF ID(primary key 2)
   * @return dummyVlanIfsInfo  Search result
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public DummyVlanIfs search(String nodeId, String vlanIfId) throws DBAccessException {

    DummyVlanIfs dummyVlanIfsInfo = null;
    try {
      Query query = session.getNamedQuery("selectDummyVlanIfsInfo");
      query.setString("key1", nodeId);
      query.setString("key2", vlanIfId);
      List<DummyVlanIfs> dummyVlanIfsInfoList = query.list();
      if (!(dummyVlanIfsInfoList == null) && dummyVlanIfsInfoList.size() == 1) {
        dummyVlanIfsInfo = dummyVlanIfsInfoList.get(0);
        dummyVlanIfsInfo.setNode_id(dummyVlanIfsInfo.getNode_id());
        dummyVlanIfsInfo.setVlan_if_id(dummyVlanIfsInfo.getVlan_if_id());
      }
    } catch (Throwable e1) {
      logger.debug("dummyVlanIfsInfo select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, DUMMY_VLAN_IFS_INFO, e1);
    }
    return dummyVlanIfsInfo;
  }
}
