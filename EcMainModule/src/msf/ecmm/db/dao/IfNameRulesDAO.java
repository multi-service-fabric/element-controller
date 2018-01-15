/*
 * Copyright(c) 2017 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.IfNameRules;

/**
 * Physical IF Naming Convention Information DAO Class.
 */
public class IfNameRulesDAO extends BaseDAO {

  /**
   * Physical IF Naming Convention Information Class Constructor.
   *
   * @param session
   *          data base session
   */
  public IfNameRulesDAO(Session session) {
    this.session = session;
  }

  /**
   * Physical IF Naming Convention Information DAO Class INSERT.
   *
   * @param ifNameRules
   *          physical IF naming convention information
   * @throws DBAccessException
   *           data base exception
   */
  public void save(IfNameRules ifNameRules) throws DBAccessException {
    try {
      List<IfNameRules> regifNameRules = this.search(ifNameRules.getEquipment_type_id());
      if (regifNameRules != null) {
        this.errorMessage(DOUBLE_REGISTRATION, IF_NAME_RULES, null);
      } else {
        session.save(ifNameRules);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("if_name_rules insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, IF_NAME_RULES, e2);
    }
  }

  /**
   * Physical IF Naming Convention Information Table DELETE.
   *
   * @param equipment_type_id
   *          model ID (primary key)
   * @throws DBAccessException
   *           data base exception
   */
  public void delete(String equipment_type_id) throws DBAccessException {
    try {
      Query query = null;
      List<IfNameRules> ifNameRules = this.search(equipment_type_id);
      if (ifNameRules == null) {
        this.errorMessage(NO_DELETE_TARGET, IF_NAME_RULES, null);
      } else if (ifNameRules.isEmpty()) {
        logger.debug("ifNameRules is not found. equipment_type_id=" + equipment_type_id);
      } else {
        query = session.getNamedQuery("deleteIfNameRules");
        query.setString("key1", equipment_type_id);
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("if_name_rules delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, IF_NAME_RULES, e2);
    }
  }

  /**
   * Physical IF Naming Convention Information Table SELECT.
   *
   * @return ifNameRulesList physical IF naming convention information list
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public List<IfNameRules> getList() throws DBAccessException {
    List<IfNameRules> ifNameRulesList = null;
    try {
      Query query = session.getNamedQuery("selectIfNameRulesAll");
      ifNameRulesList = query.list();
      for (IfNameRules ifNameRules : ifNameRulesList) {
        ifNameRules.setEquipment_type_id(ifNameRules.getEquipment_type_id());
      }
    } catch (Throwable e1) {
      logger.debug("if_name_rules select failed.", e1);
      this.errorMessage(SERCH_FAILURE, IF_NAME_RULES, e1);
    }
    return ifNameRulesList;
  }

  /**
   * Physical IF Naming Convention Information Table.
   *
   * @param equipment_type_id
   *          model ID
   * @return ifNameRulesList physical IF naming convention information
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public List<IfNameRules> search(String equipment_type_id) throws DBAccessException {
    List<IfNameRules> ifNameRulesList = null;
    try {
      Query query = session.getNamedQuery("selectIfNameRules");
      query.setString("key1", equipment_type_id);
      ifNameRulesList = query.list();
    } catch (Throwable e1) {
      logger.debug("if_name_rules select failed.", e1);
      this.errorMessage(SERCH_FAILURE, IF_NAME_RULES, e1);
    }
    return ifNameRulesList;
  }
}
