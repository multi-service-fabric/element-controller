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
import msf.ecmm.db.pojo.EquipmentIfs;

/**
 * Model IF Information DAO Class.
 */
public class EquipmentIfsDAO extends BaseDAO {

  /**
   * Model IF Information Class Constructor.
   *
   * @param session
   *          data base session
   */
  public EquipmentIfsDAO(Session session) {
    this.session = session;
  }

  /**
   * Model IF Table INSERT.
   *
   * @param equipmentIfs
   *          model IF to be registered
   * @throws DBAccessException
   *           data base exception
   */
  public void save(EquipmentIfs equipmentIfs) throws DBAccessException {
    try {
      List<EquipmentIfs> regEquipmentIfs = this.search(equipmentIfs.getEquipment_type_id());
      if (regEquipmentIfs != null) {
        this.errorMessage(DOUBLE_REGISTRATION, EQUIPMENT_IFS, null);
      } else {
        session.save(equipmentIfs);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("equipment_ifs insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, EQUIPMENT_IFS, e2);
    }
  }

  /**
   * Model IF Information Table DELETE.
   *
   * @param equipment_type_id
   *          model ID (primary key)
   * @throws DBAccessException
   *           data base exception
   */
  public void delete(String equipment_type_id) throws DBAccessException {
    try {
      Query query = null;
      if (equipment_type_id != null) {
        List<EquipmentIfs> equipmentIfs = this.search(equipment_type_id);
        if (equipmentIfs == null) {
          this.errorMessage(NO_DELETE_TARGET, EQUIPMENT_IFS, null);
        } else if(equipmentIfs.isEmpty()) {
          logger.debug("equipment_ifs is not found. equipment_type_id=" + equipment_type_id);
        } else {
          query = session.getNamedQuery("deleteEquipmentIfs");
          query.setString("key1", equipment_type_id);
          query.executeUpdate();
        }
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("equipment_ifs delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, EQUIPMENT_IFS, e2);
    }
  }

  /**
   * Model Information TableSELECT.
   *
   * @return equipmentList search result
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public List<EquipmentIfs> getList() throws DBAccessException {
    List<EquipmentIfs> equipmentIfsList = new ArrayList<EquipmentIfs>();
    try {
      Query query = session.getNamedQuery("selectEquipmentIfsAll");
      equipmentIfsList = query.list();
      for (EquipmentIfs equipmentIfs : equipmentIfsList) {
        equipmentIfs.setEquipment_type_id(equipmentIfs.getEquipment_type_id());
      }

    } catch (Throwable e1) {
      logger.debug("equipment_ifs select failed.", e1);
      this.errorMessage(SERCH_FAILURE, EQUIPMENT_IFS, e1);
    }
    return equipmentIfsList;
  }

  /**
   * Model Information IF Table SELECT.
   *
   * @param equipment_type_id
   *          model ID (primary key)
   * @return equipements model information
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public List<EquipmentIfs> search(String equipment_type_id) throws DBAccessException {
    List<EquipmentIfs> equipmentIfsList = null;
    try {
      Query query = session.getNamedQuery("selectEquipmentIfs");
      query.setString("key1", equipment_type_id);
      equipmentIfsList = query.list();
    } catch (Throwable e1) {
      logger.debug("equipment_ifs select failed.", e1);
      this.errorMessage(SERCH_FAILURE, EQUIPMENT_IFS, e1);
    }
    return equipmentIfsList;
  }
}
