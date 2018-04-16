/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.VlanIfs;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

/**
 * The class in which VLANIF information related DB process is performed.
 */
public class VlanIfsDAO extends BaseDAO {

  /**
   * VLANIF Information Class Constructor.
   *
   * @param session
   *          data base session
   */
  public VlanIfsDAO(Session session) {
    this.session = session;
  }

  /**
   * VLANIF Information Table INSERT.
   *
   * @param vlanIfs
   *          VLANIF information
   * @throws DBAccessException
   *           data base exception
   */
  public void save(VlanIfs vlanIfs) throws DBAccessException {
    try {
      VlanIfs regvlanIfs = this.search(vlanIfs.getNode_id(), vlanIfs.getVlan_if_id());
      if (regvlanIfs != null) {
        this.errorMessage(DOUBLE_REGISTRATION, VLAN_IFS, null);
      } else {
        session.save(vlanIfs);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("vlanIfs insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, VLAN_IFS, e2);
    }
  }

  /**
   * VLAN IF Status Information Table UPDATE.
   *
   * @param vlanIfs
   *          VLANIF information
   * @throws DBAccessException
   *           data base exception
   */
  public void updateState(VlanIfs vlanIfs) throws DBAccessException {
    try {
      VlanIfs regVlanIfs = this.search(vlanIfs.getNode_id(), vlanIfs.getVlan_if_id());
      if (regVlanIfs == null) {
        this.errorMessage(NO_UPDATE_TARGET, VLAN_IFS, null);
      } else {
        Query query = session.getNamedQuery("updateVlanIfsState");
        query.setString("key1", vlanIfs.getNode_id());
        query.setString("key2", vlanIfs.getVlan_if_id());
        query.setParameter("key3", vlanIfs.getIf_status(), StandardBasicTypes.INTEGER);

        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("vlan_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, VLAN_IFS, e2);
    }
  }

  /**
   * VLAN IF IP Information Table UPDATE.
   *
   * @param vlanIfs
   *          VLANIF information
   * @throws DBAccessException
   *           data base exception
   */
  public void updateIP(VlanIfs vlanIfs) throws DBAccessException {
    try {
      VlanIfs regVlanIfs = this.search(vlanIfs.getNode_id(), vlanIfs.getVlan_if_id());
      if (regVlanIfs == null) {
        this.errorMessage(NO_UPDATE_TARGET, VLAN_IFS, null);
      } else {
        Query query = session.getNamedQuery("updateVlanIfsIP");
        query.setString("key1", vlanIfs.getNode_id());
        query.setString("key2", vlanIfs.getVlan_if_id());
        query.setString("key3", vlanIfs.getIpv4_address());
        query.setParameter("key4", vlanIfs.getIpv4_prefix(), StandardBasicTypes.INTEGER);
        query.setString("key5", vlanIfs.getIpv6_address());
        query.setParameter("key6", vlanIfs.getIpv6_prefix(), StandardBasicTypes.INTEGER);

        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("vlan_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, VLAN_IFS, e2);
    }
  }

  /**
   * VLAN IF QoS Information Table UPDATE.
   *
   * @param vlanIfs
   *          VLANIF information
   * @throws DBAccessException
   *           data base exception
   */
  public void updateQos(VlanIfs vlanIfs) throws DBAccessException {
    try {
      VlanIfs regVlanIfs = this.search(vlanIfs.getNode_id(), vlanIfs.getVlan_if_id());
      if (regVlanIfs == null) {
        this.errorMessage(NO_UPDATE_TARGET, VLAN_IFS, null);
      } else {
        Query query = session.getNamedQuery("updateVlanIfsQos");
        query.setString("key1", vlanIfs.getNode_id());
        query.setString("key2", vlanIfs.getVlan_if_id());
        query.setParameter("key3", vlanIfs.getInflow_shaping_rate(), StandardBasicTypes.FLOAT);
        query.setParameter("key4", vlanIfs.getOutflow_shaping_rate(), StandardBasicTypes.FLOAT);
        query.setString("key5", vlanIfs.getRemark_menu());
        query.setString("key6", vlanIfs.getEgress_queue_menu());

        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("vlan_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, VLAN_IFS, e2);
    }
  }

  /**
   * VLAN IF IP Information Table UPDATE(Name).
   *
   * @param vlanIfs
   *          VLANIF information
   * @throws DBAccessException
   *           data base exception
   */
  public void updateIfName(VlanIfs vlanIfs) throws DBAccessException {
    try {
      VlanIfs regVlanIfs = this.search(vlanIfs.getNode_id(), vlanIfs.getVlan_if_id());
      if (regVlanIfs == null) {
        this.errorMessage(NO_UPDATE_TARGET, VLAN_IFS, null);
      } else {
        Query query = session.getNamedQuery("updateVlanIfsName");
        query.setString("key1", vlanIfs.getNode_id());
        query.setString("key2", vlanIfs.getVlan_if_id());
        query.setString("key3", vlanIfs.getIf_name());

        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("vlan_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, VLAN_IFS, e2);
    }
  }

  /**
   * VLAN IF Information Table DELETE.
   *
   * @param node_id
   *          device ID (primary key 1)
   * @param vlan_if_id
   *          VLANIF ID information (primary key 2)(NULL is permitted) * @param check Whether is it a target to be deleted from the other relations or not?
   * @param check
   *          check
   * @throws DBAccessException
   *           data base exception
   */
  public void delete(String node_id, String vlan_if_id, boolean check) throws DBAccessException {
    try {
      Query query = null;
      if (vlan_if_id != null) {
        VlanIfs vlanIfs = this.search(node_id, vlan_if_id);
        if (vlanIfs == null) {
          this.errorMessage(NO_DELETE_TARGET, VLAN_IFS, null);
        }
        query = session.getNamedQuery("deleteVlanIfs");
        query.setString("key2", vlan_if_id);
      } else {
        List<VlanIfs> vlanIfsList = this.getList(node_id);
        if (vlanIfsList == null) {
          if (check) {
            return;
          }
          this.errorMessage(NO_DELETE_TARGET, VLAN_IFS, null);
        }
        query = session.getNamedQuery("deleteVlanIfsByNode");
      }
      query.setString("key1", node_id);
      query.executeUpdate();
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("vlan_ifs delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, VLAN_IFS, e2);
    }
  }

  /**
   * VLAN IF Information Table (condition: Device ID).
   *
   * @param node_id
   *          device ID (primary key)
   * @return vlanIfsList search result
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public List<VlanIfs> getList(String node_id) throws DBAccessException {
    List<VlanIfs> vlanIfsList = new ArrayList<VlanIfs>();
    try {
      Query query = session.getNamedQuery("selectVlanIfsOnlyId");
      query.setString("key1", node_id);
      vlanIfsList = query.list();
      for (VlanIfs vlanIfs : vlanIfsList) {
        vlanIfs.setNode_id(vlanIfs.getNode_id());
      }
    } catch (Throwable e1) {
      logger.debug("vlanIfs select failed.", e1);
      this.errorMessage(SERCH_FAILURE, VLAN_IFS, e1);
    }
    return vlanIfsList;
  }

  /**
   * VLAN IF Information Table SELECT.
   *
   * @param node_id
   *          device ID (primary key 1)
   * @param vlan_if_id
   *          VLANIF ID information (primary key 2)
   * @return vlanIfs search result
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public VlanIfs search(String node_id, String vlan_if_id) throws DBAccessException {

    VlanIfs vlanIfs = null;
    try {
      Query query = session.getNamedQuery("selectVlanIfs");
      query.setString("key1", node_id);
      query.setString("key2", vlan_if_id);
      List<VlanIfs> vlanIfsList = query.list();
      if (!(vlanIfsList == null) && vlanIfsList.size() == 1) {
        vlanIfs = vlanIfsList.get(0);
        vlanIfs.setNode_id(vlanIfs.getNode_id());
        vlanIfs.setVlan_if_id(vlanIfs.getVlan_if_id());
      }
    } catch (Throwable e1) {
      logger.debug("vlanIfs select failed.", e1);
      this.errorMessage(SERCH_FAILURE, VLAN_IFS, e1);
    }
    return vlanIfs;

  }

}
