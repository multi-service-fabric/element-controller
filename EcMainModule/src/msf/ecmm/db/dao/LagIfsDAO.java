/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.LagIfs;

/**
 * LAGIF Information DAO Class.
 */
public class LagIfsDAO extends BaseDAO {

  /**
   * LAGIF Information Class Constructor.
   *
   * @param session
   *          data base session
   */
  public LagIfsDAO(Session session) {
    this.session = session;
  }

  /**
   * LAGIF Information Table INSERT.
   *
   * @param lagIfs
   *          LAG information to be registered
   * @param check
   *           check
   * @throws DBAccessException
   *           data base exception
   */
  public void save(LagIfs lagIfs, boolean check) throws DBAccessException {
    try {
      LagIfs regLagIfs = this.search(lagIfs.getNode_id(), lagIfs.getLag_if_id());
      if (regLagIfs != null) {
        if (!check) { 
          this.errorMessage(DOUBLE_REGISTRATION, LAG_IFS, null);
        }
      } else {
        session.save(lagIfs);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("lag_ifs insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, LAG_IFS, e2);
    }
  }

  /**
   * LAGIF Status Information Table UPDATE.
   *
   * @param lagIfs
   *          LAGIF information
   * @throws DBAccessException
   *           data base exception process
   */
  public void updateState(LagIfs lagIfs) throws DBAccessException {
    try {
      LagIfs regLagIfs = this.search(lagIfs.getNode_id(), lagIfs.getLag_if_id());
      if (regLagIfs == null) {
        this.errorMessage(NO_UPDATE_TARGET, LAG_IFS, null);
      } else {
        Query query = session.getNamedQuery("updateLagIfsState");
        query.setString("key1", lagIfs.getNode_id());
        query.setString("key2", lagIfs.getLag_if_id());
        query.setParameter("key3", lagIfs.getIf_status(), StandardBasicTypes.INTEGER);

        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("lag_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, LAG_IFS, e2);
    }
  }

  /**
   * LAGIF IP Information Table UPDATE.
   *
   * @param lagIfs
   *          LAGIF information
   * @throws DBAccessException
   *           data base exception process
   */
  public void updateIP(LagIfs lagIfs) throws DBAccessException {
    try {
      LagIfs regLagIfs = this.search(lagIfs.getNode_id(), lagIfs.getLag_if_id());
      if (regLagIfs == null) {
        this.errorMessage(NO_UPDATE_TARGET, LAG_IFS, null);
      } else {
        Query query = session.getNamedQuery("updateLagIfsIP");
        query.setString("key1", lagIfs.getNode_id());
        query.setString("key2", lagIfs.getLag_if_id());
        query.setString("key3", lagIfs.getIpv4_address());
        query.setParameter("key4", lagIfs.getIpv4_prefix(), StandardBasicTypes.INTEGER);

        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("lag_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, LAG_IFS, e2);
    }
  }

  /**
   * LAGIF Name Information Table UPDATE.
   *
   * @param lagIfs
   *          LAGIF information
   * @throws DBAccessException
   *           data base exception process
   */
  public void updateName(LagIfs lagIfs) throws DBAccessException {
    try {
      LagIfs regLagIfs = this.search(lagIfs.getNode_id(), lagIfs.getLag_if_id());
      if (regLagIfs == null) {
        this.errorMessage(NO_UPDATE_TARGET, LAG_IFS, null);
      } else {
        Query query = session.getNamedQuery("updateLagIfName");
        query.setString("key1", lagIfs.getNode_id());
        query.setString("key2", lagIfs.getLag_if_id());
        query.setString("key3", lagIfs.getIf_name());

        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("lag_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, LAG_IFS, e2);
    }
  }

  /**
   * LAGIF Information Table DELETE.
   *
   * @param node_id
   *          device ID (primary key)
   * @param lag_if_id
   *          LAGIF ID (primary key 2) (NULL is permitted)
   * @param check
   *          Whether is it a target to be deleted from the other relations or not?
   * @throws DBAccessException
   *           data base exception
   */
  public void delete(String node_id, String lag_if_id, boolean check) throws DBAccessException {
    try {
      Query query = null;
      if (lag_if_id != null) {
        LagIfs lagIfs = this.search(node_id, lag_if_id);
        if (lagIfs == null) {
          this.errorMessage(NO_DELETE_TARGET, LAG_IFS, null);
        } 
        query = session.getNamedQuery("deleteLagIfs");
        query.setString("key1", node_id);
        query.setString("key2", lag_if_id);
      } else {
        List<LagIfs> lagIfsList = this.getList(node_id);
        if (lagIfsList == null) {
          if (check) { 
            return;
          }
          this.errorMessage(NO_DELETE_TARGET, LAG_IFS, null);
        }
        query = session.getNamedQuery("deleteLagIfsByNode");
        query.setString("key1", node_id);
      }
      query.executeUpdate();
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("lag_ifs delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, LAG_IFS, e2);
    }
  }

  /**
   * LAGIF Information Table (condition: Device ID).
   *
   * @param node_id
   *          device ID (primary key)
   * @return lagIfsList search result
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public List<LagIfs> getList(String node_id) throws DBAccessException {
    List<LagIfs> lagIfsList = new ArrayList<LagIfs>();
    try {
      Query query = session.getNamedQuery("selectLagIfsByNode");
      query.setString("key1", node_id);
      lagIfsList = query.list();
      for (LagIfs lagIfs : lagIfsList) {
        lagIfs.setNode_id(lagIfs.getNode_id());
      }
    } catch (Throwable e1) {
      logger.debug("lag_ifs select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, LAG_IFS, e1);
    }
    return lagIfsList;
  }

  /**
   * LAGIF Information Table SELECT.
   *
   * @param node_id
   *          device ID (primary key 1)
   * @param lag_if_id
   *          LAGIF ID (primary key 2)
   * @return lagIfs search result
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public LagIfs search(String node_id, String lag_if_id) throws DBAccessException {

    LagIfs lagIfs = null;
    try {
      Query query = session.getNamedQuery("selectLagIfs");
      query.setString("key1", node_id);
      query.setString("key2", lag_if_id);
      List<LagIfs> lagIfsList = query.list();
      if (!(lagIfsList == null) && lagIfsList.size() == 1) {
        lagIfs = lagIfsList.get(0);
        lagIfs.setNode_id(lagIfs.getNode_id());
        lagIfs.setLag_if_id(lagIfs.getLag_if_id());
      }
    } catch (Throwable e1) {
      logger.debug("lag_ifs select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, LAG_IFS, e1);
    }
    return lagIfs;
  }
}
