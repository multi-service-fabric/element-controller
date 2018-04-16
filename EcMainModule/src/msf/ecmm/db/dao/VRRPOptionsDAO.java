/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.VRRPOptions;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 * The class in which VRRP option information related DB process is performed.
 */
public class VRRPOptionsDAO extends BaseDAO {

  /**
   * VRRP Option Information Class.
   *
   * @param session
   *          data base session
   */
  public VRRPOptionsDAO(Session session) {
    this.session = session;
  }

  /**
   * VRRP Option Information INSERT.
   *
   * @param vrrpOptions
   *          VRRP option information
   * @throws DBAccessException
   *           data base exception
   */
  public void save(VRRPOptions vrrpOptions) throws DBAccessException {
    try {
      VRRPOptions regVRRPOptions = this.search(vrrpOptions.getVrrp_id());
      if (regVRRPOptions != null) {
        this.errorMessage(DOUBLE_REGISTRATION, VRRP_OPTIONS, null);
      } else {
        session.save(vrrpOptions);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("vrrp_options insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, VRRP_OPTIONS, e2);
    }
  }

  /**
   * VRRP Option Information Table DELETE.
   *
   * @param nodeId
   *          device ID (external key)
   * @param vlanIfId
   *          VLAN IF ID (external key)
   * @throws DBAccessException
   *           data base exception
   */
  public void delete(String nodeId, String vlanIfId) throws DBAccessException {
    try {
      VRRPOptions vrrpOptions = this.searchByVlanIfId(nodeId, vlanIfId);
      if (vrrpOptions == null) {
        this.errorMessage(NO_DELETE_TARGET, VRRP_OPTIONS, null);
      } else {
        Query query = session.getNamedQuery("deleteVRRPOptionsByVlanIfId");
        query.setString("key1", nodeId);
        query.setString("key2", vlanIfId);
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("vrrp_options delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, VRRP_OPTIONS, e2);
    }
  }

  /**
   * VRRP Option Information Table SELECT.
   *
   * @param vrrp_id
   *          VRRP ID (primary key)
   * @return vrrpOptions search result
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public VRRPOptions search(String vrrp_id) throws DBAccessException {
    VRRPOptions vrrpOptions = null;
    try {
      Query query = session.getNamedQuery("selectVRRPOptions");
      query.setString("key1", vrrp_id);
      List<VRRPOptions> vrrpOptionsList = query.list();
      if (!(vrrpOptionsList == null) && vrrpOptionsList.size() == 1) {
        vrrpOptions = vrrpOptionsList.get(0);
      }
    } catch (Throwable e1) {
      logger.debug("vrrp_options select failed.", e1);
      this.errorMessage(SERCH_FAILURE, VRRP_OPTIONS, e1);
    }
    return vrrpOptions;
  }

  /**
   * VRRP Option Information Table SELECT.
   *
   * @param nodeId
   *          device ID (external key)
   * @param vlanIfId
   *          VLAN IF ID (external key)
   * @return vrrpOptions search result
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public VRRPOptions searchByVlanIfId(String nodeId, String vlanIfId) throws DBAccessException {
    VRRPOptions vrrpOptions = null;
    try {
      Query query = session.getNamedQuery("selectVRRPOptionsByVlanIfId");
      query.setString("key1", nodeId);
      query.setString("key2", vlanIfId);
      List<VRRPOptions> vrrpOptionsList = query.list();
      if (!(vrrpOptionsList == null) && vrrpOptionsList.size() >= 1) {
        vrrpOptions = vrrpOptionsList.get(0);
      }
    } catch (Throwable e1) {
      logger.debug("vrrp_options select failed.", e1);
      this.errorMessage(SERCH_FAILURE, VRRP_OPTIONS, e1);
    }
    return vrrpOptions;
  }
}
