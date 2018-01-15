/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.Nodes;

/**
 * Device Information DAO Class.
 */
public class NodesDAO extends BaseDAO {

  /**
   * Device Information Class Constructor.
   *
   * @param session
   *          data base session
   */
  public NodesDAO(Session session) {
    this.session = session;
  }

  /**
   * Device Information Table INSERT.
   *
   * @param nodes
   *          device information to be registered
   * @throws DBAccessException
   *           data base exception
   */
  public void save(Nodes nodes) throws DBAccessException {
    try {
      Nodes regNodes = this.search(nodes.getNode_id(), null);
      if (regNodes != null) {
        this.errorMessage(DOUBLE_REGISTRATION, NODES, null);
      } else {
        session.save(nodes);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("nodes insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, NODES, e2);
    }
  }

  /**
   * Device Information Table UPDATE.
   *
   * @param node_id
   *          device ID (primary key 1)
   * @param node_state
   *          device status
   * @throws DBAccessException
   *           data base exception
   */
  public void updateState(String node_id, int node_state) throws DBAccessException {
    try {
      Nodes regNodes = this.search(node_id, null);
      if (regNodes == null) {
        this.errorMessage(NO_UPDATE_TARGET, NODES, null);
      } else {
        Query query = session.getNamedQuery("updateNodes");
        query.setString("key1", node_id);
        query.setInteger("key2", node_state);
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("nodes update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, NODES, e2);
    }
  }

  /**
   * Device Information Table DELETE.
   *
   * @param node_id
   *          device ID (primary key)
   * @throws DBAccessException
   *           data base exception
   */
  public void delete(String node_id) throws DBAccessException {
    try {
      Nodes nodes = this.search(node_id, null);

      if (nodes == null) {
        this.errorMessage(NO_DELETE_TARGET, NODES, null);

      } else {
        Query query = session.getNamedQuery("deleteNodes");
        query.setString("key1", node_id);
        query.executeUpdate();
      }

    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("nodes delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, NODES, e2);
    }
  }

  /**
   * Device Information Table SELECT (condition: Device Type).
   *
   * @return nodesList device information list
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public List<Nodes> getList() throws DBAccessException {
    List<Nodes> nodesList = new ArrayList<Nodes>();
    try {
      Query query = session.getNamedQuery("selectNodesAll");
      nodesList = query.list();
      for (Nodes nodes : nodesList) {
        nodes.setNode_id(nodes.getNode_id());
        nodes.toString(); 
        session.flush();
        session.evict(nodes);
      }
    } catch (Throwable e1) {
      logger.debug("nodes select failed.", e1);
      this.errorMessage(SERCH_FAILURE, NODES, e1);
    }
    return nodesList;
  }

  /**
   * Device Information Table SELECT (condition: Device ID).
   *
   * @param equipment_type_id
   *          device ID
   * @return nodesList device information list
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public List<Nodes> getList2(String equipment_type_id) throws DBAccessException {
    List<Nodes> nodesList = new ArrayList<Nodes>();
    try {
      Query query = session.getNamedQuery("selectNodesOnlyEquipmentTypeId");
      query.setString("key1", equipment_type_id);
      nodesList = query.list();
      for (Nodes nodes : nodesList) {
        nodes.setEquipment_type_id(nodes.getEquipments().getEquipment_type_id());
      }
    } catch (Throwable e1) {
      logger.debug("nodes select failed.", e1);
      this.errorMessage(SERCH_FAILURE, NODES, e1);
    }
    return nodesList;
  }

  /**
   * Device Information Table SELECT.
   *
   * @param node_id
   *          device ID (primary key)
   * @param management_if_address
   *          management IF address (NULL is permitted)
   * @return nodes device information
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public Nodes search(String node_id, String management_if_address) throws DBAccessException {
    Nodes nodes = null;
    try {
      Query query = null;
      if (management_if_address == null) {
        query = session.getNamedQuery("selectNodes");
        query.setString("key1", node_id);
      } else {
        query = session.getNamedQuery("selectNodesAndIfAddr");
        query.setString("key1", management_if_address);
      }
      List<Nodes> nodesList = query.list();
      nodesList.toString();
      for (Nodes nodesTmp : nodesList) {
        session.flush();
        session.evict(nodesTmp);
      }
      if (!nodesList.isEmpty() && nodesList.size() >= 1) {
        nodes = nodesList.get(0);
        nodes.setEquipment_type_id(nodes.getEquipment_type_id());
      }
    } catch (Throwable e1) {
      logger.debug("nodes select failed.", e1);
      this.errorMessage(SERCH_FAILURE, NODES, e1);
    }
    return nodes;
  }

}
