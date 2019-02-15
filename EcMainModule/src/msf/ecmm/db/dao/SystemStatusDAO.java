/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.SystemStatus;

/**
 * System Status Information DAO Class.
 */
public class SystemStatusDAO extends BaseDAO {

  /**
   * System Status Information Class Constructor.
   *
   * @param session
   *          data base session
   */
  public SystemStatusDAO(Session session) {
    this.session = session;
  }

  /**
   * System Status Information Table INSERT.
   *
   * @param systemStatus
   *          system status information to be registered
   * @throws DBAccessException
   *           data base exception
   */
  public void save(SystemStatus systemStatus) throws DBAccessException {
    try {
      SystemStatus regSystemStatus = this.search();
      if (regSystemStatus != null) {
        this.errorMessage(DOUBLE_REGISTRATION, SYSTEM_STATUS, null);
      } else {
        session.save(systemStatus);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("systemStatus insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, SYSTEM_STATUS, e2);
    }
  }

  /**
   * System Status Information Table SELECT.
   *
   * @return system status information
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings({ "unchecked" })
  public SystemStatus search() throws DBAccessException {
    List<SystemStatus> list = null;
    try {
      list = session.createCriteria(SystemStatus.class).list();
      if (list.size() == 0) {
        return null;
      } else if (list.size() > 1) {
        this.errorMessage(SYSTEM_FAILURE, SYSTEM_STATUS, null);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("system_status select failed.", e2);
      this.errorMessage(SEARCH_FAILURE, SYSTEM_STATUS, e2);
    }
    return list.get(0);
  }

  /**
   * System Status Information Table UPDATE.
   *
   * @param service_status
   *          service start-up status
   * @param blockade_status
   *          maintenance blockage status
   * @throws DBAccessException
   *           data base exception
   */
  public void update(int service_status, int blockade_status) throws DBAccessException {
    try {
      SystemStatus orgSystemStatus = this.search();

      Query query = null;
      if (service_status != -1 && blockade_status != -1) {
        query = session.getNamedQuery("updateSystemStatus");
        query.setInteger("key1", service_status);
        query.setInteger("key2", blockade_status);
        query.executeUpdate();

        logger.info("before[service_status:" + orgSystemStatus.getService_status() + ",blockade_status:"
            + orgSystemStatus.getBlockade_status() + "]->" + "after[service_status:" + service_status
            + ",blockade_status:" + blockade_status + "]");

      } else if (service_status != -1) {
        query = session.getNamedQuery("updateSystemStatusForService");
        query.setInteger("key1", service_status);
        query.executeUpdate();

        logger.info("before[service_status:" + orgSystemStatus.getService_status() + "]->" + "after[service_status:"
            + service_status + "]");

      } else if (blockade_status != -1) {
        query = session.getNamedQuery("updateSystemStatusForBlock");
        query.setInteger("key1", blockade_status);
        query.executeUpdate();

        logger.info("before[blockade_status:" + orgSystemStatus.getBlockade_status() + "]->" + "after[blockade_status:"
            + blockade_status + "]");
      } else {
        logger.debug("system_status update input parameter failed.");
        this.errorMessage(NO_UPDATE_TARGET, SYSTEM_STATUS, null);
      }

    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("system_status update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, SYSTEM_STATUS, e2);
    }
  }
}
