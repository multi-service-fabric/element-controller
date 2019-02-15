/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.BootErrorMessages;

/**
 * The class in which start-up failure determination message information related DB process is performed.
 */
public class BootErrorMessagesDAO extends BaseDAO {

  /**
   * Start-up Failure Determination Message Information Constructor.
   *
   * @param session
   *          data base session
   */
  public BootErrorMessagesDAO(Session session) {
    this.session = session;
  }

  /**
   * Start-up Failure Determination Message Information Table INSERT.
   *
   * @param bootErrMsgs
   *          start-up failure determination message information
   * @throws DBAccessException
   *           data base exception
   */
  public void save(BootErrorMessages bootErrMsgs) throws DBAccessException {
    try {
      List<BootErrorMessages> regBootErrorMessages = this.search(bootErrMsgs.getEquipment_type_id());
      if (regBootErrorMessages != null) {
        this.errorMessage(DOUBLE_REGISTRATION, BOOT_ERROR_MESSAGES, null);
      } else {
        session.save(bootErrMsgs);
      }

    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("bootErrorMessages insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, BOOT_ERROR_MESSAGES, e2);
    }
  }

  /**
   * Start-up Failure Determination Message Information Table DELETE.
   *
   * @param equipment_type_id
   *          model type ID (primary key)
   * @throws DBAccessException
   *           data base exception
   */
  public void delete(String equipment_type_id) throws DBAccessException {
    try {
      List<BootErrorMessages> bootErrMsgs = this.search(equipment_type_id);
      if (bootErrMsgs == null) {
        this.errorMessage(NO_DELETE_TARGET, BOOT_ERROR_MESSAGES, null);
      } else if (bootErrMsgs.isEmpty()) {
        logger.debug("BootErrorMessages is not found. equipment_type_id=" + equipment_type_id);
      } else {
        Query query = session.getNamedQuery("deleteBootErrorMessages");
        query.setString("key1", equipment_type_id);
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("BootErrorMessages delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, BOOT_ERROR_MESSAGES, e2);
    }

  }

  /**
   * Start-up Failure Determination Message Information Table SELECT.
   *
   * @param equipment_type_id
   *          model type ID (primary key)
   * @return bootErrorMsgList start-up failure determination message information list
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public List<BootErrorMessages> search(String equipment_type_id) throws DBAccessException {
    List<BootErrorMessages> bootErrorMsgList = null;
    try {
      Query query = null;
      if (equipment_type_id != null) {
        query = session.getNamedQuery("selectBootErrorMessages");
        query.setString("key1", equipment_type_id);
        bootErrorMsgList = query.list();
      }

    } catch (Throwable e1) {
      logger.debug("equipment_type_id select failed.", e1);
      this.errorMessage(SEARCH_FAILURE, BOOT_ERROR_MESSAGES, e1);
    }
    return bootErrorMsgList;
  }
}
