/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.StaticRouteOptions;

/**
 * The class in which static route option information related DB process is performed.
 */
public class StaticRouteOptionsDAO extends BaseDAO {

  /**
   * static Route Option Information Class Constructor
   *
   * @param session
   *          data base session
   */
  public StaticRouteOptionsDAO(Session session) {
    this.session = session;
  }

  /**
   * static Route Option Information INSERT.
   *
   * @param staticRouteOptions
   *          static route option information
   * @throws DBAccessException
   *           data base exception
   */
  public void save(StaticRouteOptions staticRouteOptions) throws DBAccessException {
    try {

      session.save(staticRouteOptions);

    } catch (Throwable e2) {
      logger.debug("staticRouteOptions insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, STATIC_ROUTE_OPTIONS, e2);
    }
  }

  /**
   * static Route Option Information Table DELETE.
   *
   * @param staticRouteOptions
   *          static route option information
   * @throws DBAccessException
   *           data base exception
   */
  public void delete(StaticRouteOptions staticRouteOptions) throws DBAccessException {
    try {
      Query query = session.getNamedQuery("deleteStaticRouteOptionsAll");
      query.setString("key1", staticRouteOptions.getNode_id());
      query.setString("key2", staticRouteOptions.getVlan_if_id());
      query.setParameter("key3", staticRouteOptions.getStatic_route_address_type(), StandardBasicTypes.INTEGER);
      query.setString("key4", staticRouteOptions.getStatic_route_destination_address());
      query.setParameter("key5", staticRouteOptions.getStatic_route_prefix(), StandardBasicTypes.INTEGER);
      query.setString("key6", staticRouteOptions.getStatic_route_nexthop_address());

      query.executeUpdate();
    } catch (Throwable e1) {
      logger.debug("static_route_options delete failed.", e1);
      this.errorMessage(DELETE_FAILURE, STATIC_ROUTE_OPTIONS, e1);
    }
  }

  /**
   * static Route Option Information Table SELECT.
   *
   * @param node_id
   *          device ID (primary key 1)
   * @param vlan_if_id
   *          VLANIF ID (primary key 2)
   * @return staticRouteOptions search result
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public StaticRouteOptions search(String node_id, String vlan_if_id) throws DBAccessException {
    StaticRouteOptions staticRouteOptions = null;
    try {
      Query query = session.getNamedQuery("selectStaticRouteOptions");
      query.setString("key1", node_id);
      query.setString("key2", vlan_if_id);
      List<StaticRouteOptions> staticRouteOptionsList = query.list();
      if (!(staticRouteOptionsList == null) && staticRouteOptionsList.size() >= 1) {
        staticRouteOptions = staticRouteOptionsList.get(0);
        staticRouteOptions.setNode_id(staticRouteOptions.getNode_id());
      }
    } catch (Throwable e1) {
      logger.debug("static_route_options select failed.", e1);
      this.errorMessage(SERCH_FAILURE, STATIC_ROUTE_OPTIONS, e1);
    }
    return staticRouteOptions;

  }

  /**
   * static Route Option Information Table DELETE.
   *
   * @param node_id
   *          device ID (primary key 1)
   * @param vlan_if_id
   *          VLANIF ID (primary key 2)
   * @throws DBAccessException
   *           data base exception
   */
  public void delete(String node_id, String vlan_if_id) throws DBAccessException {
    try {
      Query query = null;
      if (vlan_if_id != null) {
        StaticRouteOptions staticRouteOptions = this.search(node_id, vlan_if_id);
        if (staticRouteOptions == null) {
          this.errorMessage(NO_DELETE_TARGET, STATIC_ROUTE_OPTIONS, null);
        } 
        else {

          query = session.getNamedQuery("deleteStaticRouteOptions");
          query.setString("key1", node_id);
          query.setString("key2", vlan_if_id);
          query.executeUpdate();
        }
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("static_route_options delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, STATIC_ROUTE_OPTIONS, e2);
    }

  }
}
