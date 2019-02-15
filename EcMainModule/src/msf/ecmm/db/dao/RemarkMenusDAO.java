/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.RemarkMenus;

/**
 * Remark Menu Information DAO Class.
 */
public class RemarkMenusDAO extends BaseDAO {

  /**
   * Remark Menu Information Class Constructor.
   *
   * @param session
   *          data base session
   */
  public RemarkMenusDAO(Session session) {
    this.session = session;
  }

  /**
   * Remark Menu Table INSERT.
   *
   * @param remarkMenus
   *          Remark Menu
   * @throws DBAccessException
   *           data base exception
   */
  public void save(RemarkMenus remarkMenus) throws DBAccessException {
    try {
      List<RemarkMenus> regRemarkMenusList = this.search(remarkMenus.getEquipment_type_id());
      if (regRemarkMenusList != null) {
        this.errorMessage(DOUBLE_REGISTRATION, BOOT_ERROR_MESSAGES, null);
      } else {
        session.save(remarkMenus);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("RemarkMenus insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, REMARK_MENUS, e2);
    }
  }

  /**
   * Remark Menu Information Table DELETE.
   *
   * @param equipment_type_id
   *          model type ID (external key)
   * @throws DBAccessException
   *           data base exception
   */
  public void delete(String equipment_type_id) throws DBAccessException {
    try {
      List<RemarkMenus> remarkMenusList = this.search(equipment_type_id);
      if (remarkMenusList == null) {
        this.errorMessage(NO_DELETE_TARGET, REMARK_MENUS, null);
      } else if (remarkMenusList.isEmpty()) {
        logger.debug("RemarkMenus is not found. equipment_type_id=" + equipment_type_id);
      } else {
        Query query = session.getNamedQuery("deleteRemarkMenus");
        query.setString("key1", equipment_type_id);
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("RemarkMenu delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, REMARK_MENUS, e2);
    }

  }

  /**
   * Remark Menu Information Table SELECT.
   *
   * @param equipment_type_id
   *          model type ID(external key)
   * @return Remark Menu information list
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public List<RemarkMenus> search(String equipment_type_id) throws DBAccessException {
    List<RemarkMenus> remarkMenusList = null;
    try {
      Query query = null;
      if (equipment_type_id != null) {
        query = session.getNamedQuery("selectRemarkMenus");
        query.setString("key1", equipment_type_id);
        remarkMenusList = query.list();
      }
    } catch (Throwable e1) {
      logger.debug("RemarkMenus select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, REMARK_MENUS, e1);
    }
    return remarkMenusList;
  }
}
