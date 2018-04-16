/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.PhysicalIfs;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

/**
 * Physical IF Information DAO Class.
 */
public class PhysicalIfsDAO extends BaseDAO {

  /**
   * Physical IF Information Class Constructor.
   *
   * @param session
   *          data base session
   */
  public PhysicalIfsDAO(Session session) {
    this.session = session;
  }

  /**
   * Physical IF INformation Table INSERT.
   *
   * @param physicalIfs
   *          physical IF informatio to be registered
   * @throws DBAccessException
   *           data base exception
   */
  public void save(PhysicalIfs physicalIfs) throws DBAccessException {
    try {
      PhysicalIfs regPhysicalIfs = this.search(physicalIfs.getNode_id(), physicalIfs.getPhysical_if_id());
      if (regPhysicalIfs != null) {
        this.errorMessage(DOUBLE_REGISTRATION, PHYSICAL_IFS, null);
      } else {
        session.save(physicalIfs);
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("physical_ifs insert failed.", e2);
      this.errorMessage(INSERT_FAILURE, PHYSICAL_IFS, e2);
    }
  }

  /**
   * Physical IF Information Table UPDATE.
   *
   * @param physicalIfs
   *          physical IF information
   * @throws DBAccessException
   *           data base exception
   */
  public void update(PhysicalIfs physicalIfs) throws DBAccessException {
    try {
      PhysicalIfs regPhysicalIfs = this.search(physicalIfs.getNode_id(), physicalIfs.getPhysical_if_id());
      if (regPhysicalIfs == null) {
        this.errorMessage(NO_UPDATE_TARGET, PHYSICAL_IFS, null);
      } else {
        Query query = session.getNamedQuery("updatePhysicalIfs");
        query.setString("key1", physicalIfs.getNode_id());
        query.setString("key2", physicalIfs.getPhysical_if_id());
        query.setString("key3", physicalIfs.getIf_speed());
        query.setString("key4", physicalIfs.getIf_name());

        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("physical_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, PHYSICAL_IFS, e2);
    }
  }

  /**
   * Physical IF Status Information Table UPDATE.
   *
   * @param physicalIfs
   *          physical IF information
   * @throws DBAccessException
   *           data base exception
   */
  public void updateState(PhysicalIfs physicalIfs) throws DBAccessException {
    try {
      PhysicalIfs regPhysicalIfs = this.search(physicalIfs.getNode_id(), physicalIfs.getPhysical_if_id());
      if (regPhysicalIfs == null) {
        this.errorMessage(NO_UPDATE_TARGET, PHYSICAL_IFS, null);
      } else {
        Query query = session.getNamedQuery("updatePhysicalIfsState");
        query.setString("key1", physicalIfs.getNode_id());
        query.setString("key2", physicalIfs.getPhysical_if_id());
        query.setParameter("key3", physicalIfs.getIf_status(), StandardBasicTypes.INTEGER);
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("physical_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, PHYSICAL_IFS, e2);
    }
  }

  /**
   * Physical IF IP Information Table UPDATE (IP).
   *
   * @param physicalIfs
   *          physical IF information
   * @throws DBAccessException
   *           data base exception
   */
  public void updateIP(PhysicalIfs physicalIfs) throws DBAccessException {
    try {
      PhysicalIfs regPhysicalIfs = this.search(physicalIfs.getNode_id(), physicalIfs.getPhysical_if_id());
      if (regPhysicalIfs == null) {
        this.errorMessage(NO_UPDATE_TARGET, PHYSICAL_IFS, null);
      } else {
        Query query = session.getNamedQuery("updatePhysicalIfsIP");
        query.setString("key1", physicalIfs.getNode_id());
        query.setString("key2", physicalIfs.getPhysical_if_id());
        query.setString("key3", physicalIfs.getIpv4_address());
        query.setParameter("key4", physicalIfs.getIpv4_prefix(), StandardBasicTypes.INTEGER);

        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("physical_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, PHYSICAL_IFS, e2);
    }
  }

  /**
   * Physical IF IP Information Table UPDATE (status).
   *
   * @param physicalIfs
   *          physical IF information
   * @throws DBAccessException
   *           data base exception
   */
  public void updateAll(PhysicalIfs physicalIfs) throws DBAccessException {
    try {
      PhysicalIfs regPhysicalIfs = this.search(physicalIfs.getNode_id(), physicalIfs.getPhysical_if_id());
      if (regPhysicalIfs == null) {
        this.errorMessage(NO_UPDATE_TARGET, PHYSICAL_IFS, null);
      } else {
        Query query = session.getNamedQuery("updatePhysicalIfsAll");
        query.setString("key1", physicalIfs.getNode_id());
        query.setString("key2", physicalIfs.getPhysical_if_id());
        query.setString("key3", physicalIfs.getIf_name());
        query.setString("key4", physicalIfs.getIf_speed());
        query.setParameter("key5", physicalIfs.getIf_status(), StandardBasicTypes.INTEGER);
        query.setString("key6", physicalIfs.getIpv4_address());
        query.setParameter("key7", physicalIfs.getIpv4_prefix(), StandardBasicTypes.INTEGER);

        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("physical_ifs update failed.", e2);
      this.errorMessage(UPDATE_FAILURE, PHYSICAL_IFS, e2);
    }
  }

  /**
   * Physical IF Information Table DELETE.
   *
   * @param node_id
   *          device ID (primary key)
   * @param check
   *          Whether is it a target to be deleted from the other relations or not?
   * @throws DBAccessException
   *           data base exception
   */
  public void deleteAll(String node_id, boolean check) throws DBAccessException {
    try {
      List<PhysicalIfs> physicalIfsList = this.getList(node_id);
      if (physicalIfsList.isEmpty()) {
        if (check) {
          return;
        }
        logger.debug("physical_ifs delete failed.");
        this.errorMessage(NO_DELETE_TARGET, PHYSICAL_IFS, null);
      } else {
        Query query = session.getNamedQuery("deletePhysicalIfsByNode");
        query.setString("key1", node_id);
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("physical_ifs delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, PHYSICAL_IFS, e2);
    }
  }

  /**
   * Physical IF Information Table DELETE.
   *
   * @param nodeId
   *          device ID (primary key)
   * @param physicalIfId
   *          physical IF (primary key)
   * @throws DBAccessException
   *           data base exception
   */
  public void delete(String nodeId, String physicalIfId) throws DBAccessException {
    try {
      System.out.println(nodeId);
      System.out.println(physicalIfId);
      PhysicalIfs physicalIfs = this.search(nodeId, physicalIfId);
      if (physicalIfs == null) {
        logger.debug("physical_ifs delete failed.");
        this.errorMessage(NO_DELETE_TARGET, PHYSICAL_IFS, null);
      } else {
        Query query = session.getNamedQuery("deletePhysicalIfs");
        query.setString("key1", nodeId);
        query.setString("key2", physicalIfId);
        query.executeUpdate();
      }
    } catch (DBAccessException e1) {
      throw e1;
    } catch (Throwable e2) {
      logger.debug("physical_ifs delete failed.", e2);
      this.errorMessage(DELETE_FAILURE, PHYSICAL_IFS, e2);
    }
  }

  /**
   * Physical IF Information Table (condition: Device Type, Device ID).
   *
   * @param node_id
   *          device ID (primary key)
   * @return physicalIfsList search result
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public List<PhysicalIfs> getList(String node_id) throws DBAccessException {
    List<PhysicalIfs> physicalIfsList = new ArrayList<PhysicalIfs>();
    try {
      Query query = session.getNamedQuery("selectPhysicalIfsByNode");
      query.setString("key1", node_id);
      physicalIfsList = query.list();
      for (PhysicalIfs physicalIfs : physicalIfsList) {
        physicalIfs.setNode_id(physicalIfs.getNode_id());
      }
    } catch (Throwable e1) {
      logger.debug("physical_ifs select failed.", e1);
      this.errorMessage(SERCH_FAILURE, PHYSICAL_IFS, e1);
    }
    return physicalIfsList;
  }

  /**
   * Physical IF Information Table SELECT.
   *
   * @param node_id
   *          device ID (primary key 1)
   * @param physical_if_id
   *          physical IF ID (primary key 2)
   * @return system status information
   * @throws DBAccessException
   *           data base exception
   */
  @SuppressWarnings("unchecked")
  public PhysicalIfs search(String node_id, String physical_if_id) throws DBAccessException {
    PhysicalIfs physicalIfs = null;
    try {
      Query query = session.getNamedQuery("selectPhysicalIfs");
      query.setString("key1", node_id);
      query.setString("key2", physical_if_id);
      List<PhysicalIfs> physicalIfsList = query.list();
      if (!physicalIfsList.isEmpty() && physicalIfsList.size() == 1) {
        physicalIfs = physicalIfsList.get(0);
        physicalIfs.setNode_id(physicalIfs.getNode_id());
      }
    } catch (Throwable e1) {
      logger.debug("physical_ifs select failed.", e1);
      this.errorMessage(SERCH_FAILURE, PHYSICAL_IFS, e1);
    }
    return physicalIfs;
  }

}
