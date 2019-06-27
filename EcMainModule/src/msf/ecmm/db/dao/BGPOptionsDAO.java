/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.BGPOptions;

/**
 * The Class in which BGP option information related DB process is performed.
 */
public class BGPOptionsDAO extends BaseDAO {

  /**
   * BGP Option Information Constructor.
   *
   * @param session
   *          data base session
   */
  public BGPOptionsDAO(Session session) {
    this.session = session;
  }

  /**
   * BGP Option Table INSERT.
   *
   * @param bgpOptions
   *          BGP option information to be registered
   * @throws DBAccessException
   *           data base exception
   */
  public void save(BGPOptions bgpOptions) throws DBAccessException {
    try {
      BGPOptions regBGPOptions = this.search(bgpOptions.getBgp_id());
      if (regBGPOptions != null) {
        this.errorMessage(DOUBLE_REGISTRATION, BGP_OPTIONS, null);
      } else {
        session.save(bgpOptions);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("bgpOptions insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, BGP_OPTIONS, e2);
    }
  }

  /**
   * BGP Option Information Table DELETE.
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
      BGPOptions bgpOptions = this.searchByVlanIfId(nodeId, vlanIfId);
      if (bgpOptions == null) {
        this.errorMessage(NO_DELETE_TARGET, BGP_OPTIONS, null);
      } else {
        Query query = session.getNamedQuery("deleteBGPOptionsByVlanIfId");
        query.setString("key1", nodeId);
        query.setString("key2", vlanIfId);
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("bgp_Options delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, BGP_OPTIONS, e2);
    }
  }

  /**
   * BGP Option Information Table SELECT.
   *
   * @param bgp_id
   *          BGP information ID (primary key)
   * @return BGP option information search result
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public BGPOptions search(String bgp_id) throws DBAccessException {
    BGPOptions bgpOptions = null;
    try {
      Query query = session.getNamedQuery("selectBGPOptions");
      query.setString("key1", bgp_id);
      List<BGPOptions> bgpOptionsList = query.list();
      if (!(bgpOptionsList == null) && bgpOptionsList.size() == 1) {
        bgpOptions = bgpOptionsList.get(0);

      }
    } catch (Throwable e1) {
      logger.debug("bgp_Options select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, BGP_OPTIONS, e1);
    }
    return bgpOptions;
  }

  /**
   * BGP Option Information Table SELECT.
   *
   * @param nodeId
   *          device ID (external key)
   * @param vlanIfId
   *          VLAN IF ID (external key)
   * @return BGP option information search result
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public BGPOptions searchByVlanIfId(String nodeId, String vlanIfId) throws DBAccessException {
    BGPOptions bgpOptions = null;
    try {
      Query query = session.getNamedQuery("selectBGPOptionsByVlanIfId");
      query.setString("key1", nodeId);
      query.setString("key2", vlanIfId);
      List<BGPOptions> bgpOptionsList = query.list();
      if (!(bgpOptionsList == null) && bgpOptionsList.size() >= 1) {
        bgpOptions = bgpOptionsList.get(0);

      }
    } catch (Throwable e1) {
      logger.debug("bgp_Options select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, BGP_OPTIONS, e1);
    }
    return bgpOptions;
  }
}
