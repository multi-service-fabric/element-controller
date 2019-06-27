/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.BreakoutIfs;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

/**
 * The Class in which breakoutIF information related DB process is performed.
 */
public class BreakoutIfsDAO extends BaseDAO {

  /**
   * breakoutIF information class constructor.
   *
   * @param session
   *          data base session
   */
  public BreakoutIfsDAO(Session session) {
    this.session = session;
  }

  /**
   * breakoutIF information table INSERT.
   *
   * @param breakoutIfs
   *          breakoutIF information that you want to register
   * @param check
   *          Whether is it a target to be inserted from the other relations or not?
   * @throws DBAccessException
   *           data base exception
   */
  public void save(BreakoutIfs breakoutIfs, boolean check) throws DBAccessException {
    try {
      BreakoutIfs regbreakoutIfs = this.search(breakoutIfs.getNode_id(), breakoutIfs.getBreakout_if_id());
      if (regbreakoutIfs != null) {
        if (check) { 
          this.updateIP(breakoutIfs);
        } else {
          this.errorMessage(DOUBLE_REGISTRATION, BREAKOUT_IFS, null);
        }
      } else {
        session.save(breakoutIfs);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("breakoutIfs insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, BREAKOUT_IFS, e2);
    }
  }

 /**
   * breakoutIF information table DELETE.
   *
   * @param node_id
   *          device ID(primary key 1)
   * @param breakout_if_id
   *          breakoutIF ID(primary key 2)(NULL allowed)
   * @param check
   *          check whether it is the deletion target related to other
   * @throws DBAccessException
   *           database exception
   */
  public void delete(String node_id, String breakout_if_id, boolean check) throws DBAccessException {
    try {
      Query query = null;
      if (breakout_if_id != null) {
        BreakoutIfs breakoutIfs = this.search(node_id, breakout_if_id);
        if (breakoutIfs == null) {
          this.errorMessage(NO_DELETE_TARGET, BREAKOUT_IFS, null);
        } 
        query = session.getNamedQuery("deleteBreakoutIfs");
        query.setString("key2", breakout_if_id);
      } else {
        List<BreakoutIfs> breakoutIfsList = this.getList(node_id);
        if (breakoutIfsList == null) {
          if (check) { 
            return;
          }
          this.errorMessage(NO_DELETE_TARGET, BREAKOUT_IFS, null);
        }
        query = session.getNamedQuery("deleteBreakoutIfsByNode");
      }
      query.setString("key1", node_id);
      query.executeUpdate();
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("breakout_ifs delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, BREAKOUT_IFS, e2);
    }
  }

  /**
   * breakoutIF information table（condition：model ID).
   *
   * @param node_id
   *          model ID(primary key)
   * @return breakoutIfsList breakoutIF information list
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public List<BreakoutIfs> getList(String node_id) throws DBAccessException {
    List<BreakoutIfs> breakoutIfsList = new ArrayList<BreakoutIfs>();
    try {
      Query query = session.getNamedQuery("selectBreakoutIfsByNode");
      query.setString("key1", node_id);
      breakoutIfsList = query.list();
      for (BreakoutIfs breakoutIfs : breakoutIfsList) {
        breakoutIfs.setNode_id(breakoutIfs.getNode_id());
      }
    } catch (Throwable e1) {
      logger.debug("breakout_ifs select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, BREAKOUT_IFS, e1);
    }
    return breakoutIfsList;
  }

  /**
   * breakoutIF information table SELECT.
   *
   * @param node_id
   *          model ID(primary key １)
   * @param breakout_if_id
   *          breakoutIF ID(primary key 2)
   * @return breakoutIfs breakoutIF information
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public BreakoutIfs search(String node_id, String breakout_if_id) throws DBAccessException {
    BreakoutIfs breakoutIfs = null;
    try {
      Query query = session.getNamedQuery("selectBreakoutIfs");
      query.setString("key1", node_id);
      query.setString("key2", breakout_if_id);
      List<BreakoutIfs> breakoutIfsList = query.list();
      if (!(breakoutIfsList == null) && breakoutIfsList.size() == 1) {
        breakoutIfs = breakoutIfsList.get(0);
        breakoutIfs.setNode_id(breakoutIfs.getNode_id());
      }
    } catch (Throwable e1) {
      logger.debug("breakout_ifs select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, BREAKOUT_IFS, e1);
    }
    return breakoutIfs;
  }

  /**
   * breakoutIF status information table UPDATE.
   *
   * @param breakoutIfs
   *          breakoutIF information
   * @throws DBAccessException
   *           database exception
   */
  public void updateState(BreakoutIfs breakoutIfs) throws DBAccessException {
    try {
      Query query = session.getNamedQuery("updateBreakoutIfsStatus");
      query.setString("key1", breakoutIfs.getNode_id());
      query.setString("key2", breakoutIfs.getBreakout_if_id());
      query.setParameter("key3", breakoutIfs.getIf_status(), StandardBasicTypes.INTEGER);

      int count = query.executeUpdate();
      if (count == 0) {
        this.errorMessage(NO_UPDATE_TARGET, BREAKOUT_IFS, null);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("breakout_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, BREAKOUT_IFS, e2);
    }
  }

  /**
   * breakoutIF IP status information table UPDATE.
   *
   * @param breakoutIfs
   *          breakoutIF information
   * @throws DBAccessException
   *           database exception
   */

  public void updateIP(BreakoutIfs breakoutIfs) throws DBAccessException {
    try {
      Query query = session.getNamedQuery("updateBreakoutIfsIP");
      query.setString("key1", breakoutIfs.getNode_id());
      query.setString("key2", breakoutIfs.getBreakout_if_id());
      query.setString("key3", breakoutIfs.getIpv4_address());
      query.setParameter("key4", breakoutIfs.getIpv4_prefix(), StandardBasicTypes.INTEGER);
      int count = query.executeUpdate();
      if (count == 0) {
        this.errorMessage(NO_UPDATE_TARGET, BREAKOUT_IFS, null);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("breakout_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, BREAKOUT_IFS, e2);
    }
  }

  /**
   * breakoutIF information table UPDATE.
   *
   * @param breakoutIfs
   *          breakoutIF information
   * @throws DBAccessException
   *           database exception
   */
  public void updateIfName(BreakoutIfs breakoutIfs) throws DBAccessException {
    try {
      Query query = session.getNamedQuery("updateBreakoutIfName");
      query.setString("key1", breakoutIfs.getNode_id());
      query.setString("key2", breakoutIfs.getBreakout_if_id());
      query.setString("key3", breakoutIfs.getIf_name());

      int count = query.executeUpdate();
      if (count == 0) {
        this.errorMessage(NO_UPDATE_TARGET, BREAKOUT_IFS, null);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("breakout_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, BREAKOUT_IFS, e2);
    }
  }

}
