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
import msf.ecmm.db.pojo.NodesStartupNotification;

/**
 * Device Start-up Notification Information DAO Class.
 */
public class NodesStartupNotificationDAO extends BaseDAO {

  /**
   * Device Start-up Notification Information DAO Class Constuctor.
   *
   * @param session
   *          data base session
   */
  public NodesStartupNotificationDAO(Session session) {
    this.session = session;
  }

  /**
   * Device Start-up Notification Information Table UPDATE.
   *
   * @param nodesStartupNotification
   *          device start-up notification information
   * @throws DBAccessException
   *           data base exception
   */
  public void update(NodesStartupNotification nodesStartupNotification) throws DBAccessException {
    try {
      NodesStartupNotification regNodesStartupNotification = this.search(nodesStartupNotification.getNode_id());
      if (regNodesStartupNotification == null) {
        this.errorMessage(NO_UPDATE_TARGET, NODES_STARTUP_NOTICE, null);
      } else {
        Query query = session.getNamedQuery("updateNodesStartupNotification");
        query.setString("key1", nodesStartupNotification.getNode_id());
        query.setInteger("key2",nodesStartupNotification.getNotification_reception_status());
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("nodes_startup_notice update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, NODES_STARTUP_NOTICE, e2);
    }
  }

  /**
   * Device Start-up Notification Information Table SELECT.
   *
   * @return nodesStartupNotificationList device start-up notification information list
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public List<NodesStartupNotification> getList() throws DBAccessException {
    List<NodesStartupNotification> nodesStartupNotificationList = new ArrayList<NodesStartupNotification>();
    try {
      Query query = session.getNamedQuery("selectNodesStartupNotificationAll");
      nodesStartupNotificationList = query.list();
      for (NodesStartupNotification nodesStartupNotification : nodesStartupNotificationList) {
        nodesStartupNotification.setNode_id(nodesStartupNotification.getNode_id());
      }

    } catch (Throwable e1) {
      logger.debug("nodes_startup_notice select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, NODES_STARTUP_NOTICE, e1);
    }
    return nodesStartupNotificationList;
  }

  /**
   * Device Start-up Notification Information Table INSERT.
   *
   * @param nodesStartupNotification
   *          device start-up notification information
   * @throws DBAccessException
   *           data base exception
   */

  public void save(NodesStartupNotification nodesStartupNotification) throws DBAccessException {
    try {
      NodesStartupNotification regNodesStartupNotification = this.search(nodesStartupNotification.getNode_id());
      if (regNodesStartupNotification != null) {
        this.errorMessage(DOUBLE_REGISTRATION, NODES_STARTUP_NOTICE, null);
      } else {
        session.save(nodesStartupNotification);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("nodes_startup_notice insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, NODES_STARTUP_NOTICE, e2);
    }
  }

  /**
   * Device Start-up Notification Information Table DELETE.
   *
   * @param node_id
   *          device ID (primary key)
   * @param check
   *          Whether is it a target to be deleted from the other relations or not?
   * @throws DBAccessException
   *           data base exception
   */
  public void delete(String node_id, boolean check) throws DBAccessException {
    try {
      if (node_id != null) {
        NodesStartupNotification nodesStartupNotification = this.search(node_id);
        if (nodesStartupNotification == null) {
          if (check) { 
            return;
          }
          this.errorMessage(NO_DELETE_TARGET, NODES_STARTUP_NOTICE, null);
        }
      }
      Query query = null;
      if (node_id != null) {
        query = session.getNamedQuery("deleteNodesStartupNotification");
        query.setString("key1", node_id);
      } else {
        query = session.getNamedQuery("deleteNodesStartupNotificationAllList");
      }
      query.executeUpdate();

    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("nodes_startup_notice delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, NODES_STARTUP_NOTICE, e2);
    }
  }

  /**
   * Device Start-up Notification Table SELECT.
   *
   * @param node_id
   *          device ID (primary key 1)
   * @return nodesStartupNotification
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public NodesStartupNotification search(String node_id) throws DBAccessException {
    NodesStartupNotification nodesStartupNotification = null;
    try {
      Query query = session.getNamedQuery("selectNodesStartupNotification");
      query.setString("key1", node_id);

      List<NodesStartupNotification> nodesStartupNotificationList = query.list();
      if (!(nodesStartupNotificationList == null) && nodesStartupNotificationList.size() == 1) {
        nodesStartupNotification = nodesStartupNotificationList.get(0);
        nodesStartupNotification.setNode_id(nodesStartupNotification.getNode_id());
      }

    } catch (Throwable e1) {
      logger.debug("nodes_startup_notice select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, NODES_STARTUP_NOTICE, e1);
    }
    return nodesStartupNotification;
  }

  /**
   * Device Start-up Notification Information Table DELETE (delete all cases).
   *
   * @throws DBAccessException
   *           data base exception
   */
  public void deleteAll() throws DBAccessException {
    try {
      List<NodesStartupNotification> nodesStartupNotificationList = this.getList();
      if (nodesStartupNotificationList.size() == 0) {
        this.errorMessage(NO_DELETE_TARGET, NODES_STARTUP_NOTICE, null);
      } else {
        Query query = session.getNamedQuery("deleteNodesStartupNotificationAllList");
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("nodes_startup_notice delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, NODES_STARTUP_NOTICE, e2);
    }
  }
}
