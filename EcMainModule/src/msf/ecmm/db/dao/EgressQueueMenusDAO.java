/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.EgressQueueMenus;

/**
 * Class which performs DB processing related to the Egress queue menu information.
 */
public class EgressQueueMenusDAO extends BaseDAO {

  /**
   * Egress queue menu information constructor.
   *
   * @param session
   *          database session
   */
  public EgressQueueMenusDAO(Session session) {
    this.session = session;
  }

  /**
   * Egress queue menu information table INSERT.
   *
   * @param egressQueueMenus
   *          Egress queue menu information
   * @throws DBAccessException
   *           database exception
   */
  public void save(EgressQueueMenus egressQueueMenus) throws DBAccessException {
    try {
      List<EgressQueueMenus> regEgressMenusList = this.search(egressQueueMenus.getEquipment_type_id());
      if (regEgressMenusList != null) {
        this.errorMessage(DOUBLE_REGISTRATION, EGRESS_QUEUE_MENUS, null);
      } else {
        session.save(egressQueueMenus);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("EgressQueueMenu insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, EGRESS_QUEUE_MENUS, e2);
    }
  }

  /**
   * Egress queue menu information table DELETE.
   *
   * @param equipment_type_id
   *          model type ID(external key)
   * @throws DBAccessException
   *           database exception
   */
  public void delete(String equipment_type_id) throws DBAccessException {
    try {
      List<EgressQueueMenus> egressQueueMenusList = this.search(equipment_type_id);
      if (egressQueueMenusList == null) {
        this.errorMessage(NO_DELETE_TARGET, EGRESS_QUEUE_MENUS, null);
      } else if (egressQueueMenusList.isEmpty()) {
        logger.debug("EgressQueueMenu is not found. equipment_type_id=" + equipment_type_id);
      } else {
        Query query = session.getNamedQuery("deleteEgressQueueMenus");
        query.setString("key1", equipment_type_id);
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("EgressQueueMenus delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, EGRESS_QUEUE_MENUS, e2);
    }

  }

  /**
   * Egress queue menu information table SELECT.
   *
   * @param equipment_type_id
   *          model type ID(external key)
   * @return egressQueueMenusList Egress queue menu information list
   * @throws DBAccessException
   *           database exception
   */
  @SuppressWarnings("unchecked")
  public List<EgressQueueMenus> search(String equipment_type_id) throws DBAccessException {
    List<EgressQueueMenus> egressQueueMenusList = null;
    try {
      Query query = null;
      if (equipment_type_id != null) {
        query = session.getNamedQuery("selectEgressQueueMenus");
        query.setString("key1", equipment_type_id);
        egressQueueMenusList = query.list();
      }
    } catch (Throwable e1) {
      logger.debug("equipment_type_id select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, EGRESS_QUEUE_MENUS, e1);
    }
    return egressQueueMenusList;
  }
}
