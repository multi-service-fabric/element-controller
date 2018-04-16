/*
 * Copyright(c) 2018 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db;

import static msf.ecmm.common.LogFormatter.*;
import static msf.ecmm.db.DBAccessException.*;

import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.dao.BGPOptionsDAO;
import msf.ecmm.db.dao.BaseDAO;
import msf.ecmm.db.dao.BootErrorMessagesDAO;
import msf.ecmm.db.dao.BreakoutIfsDAO;
import msf.ecmm.db.dao.EgressQueueMenusDAO;
import msf.ecmm.db.dao.EquipmentIfsDAO;
import msf.ecmm.db.dao.EquipmentsDAO;
import msf.ecmm.db.dao.IfNameRulesDAO;
import msf.ecmm.db.dao.LagIfsDAO;
import msf.ecmm.db.dao.LagMembersDAO;
import msf.ecmm.db.dao.NodesDAO;
import msf.ecmm.db.dao.NodesStartupNotificationDAO;
import msf.ecmm.db.dao.PhysicalIfsDAO;
import msf.ecmm.db.dao.RemarkMenusDAO;
import msf.ecmm.db.dao.StaticRouteOptionsDAO;
import msf.ecmm.db.dao.SystemStatusDAO;
import msf.ecmm.db.dao.VRRPOptionsDAO;
import msf.ecmm.db.dao.VlanIfsDAO;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.InterfacesList;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.NodesStartupNotification;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.StaticRouteOptions;
import msf.ecmm.db.pojo.SystemStatus;
import msf.ecmm.db.pojo.VlanIfs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * DB Access Management Class.
 */
public class DBAccessManager implements AutoCloseable {

  /** Logger Instance. */
  private final Logger logger = LogManager.getLogger(CommonDefinitions.EC_LOGGER);
  /** Session Instance. */
  protected Session session;
  /** Transaction Instance. */
  protected Transaction tr;

  /**
   * Constructor.
   *
   * @throws DBAccessException
   *           session acquisition exception
   */
  public DBAccessManager() throws DBAccessException {
    try {
      session = SessionManager.getInstance().getSession();
    } catch (Throwable e1) {
      throw new DBAccessException(DB_COMMON_ERROR, MSG_509049, e1);
    }
  }

  /**
   * Transaction Start.
   *
   * @throws DBAccessException
   *           DBAccessException transaction start exception
   */
  public void startTransaction() throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    try {
      tr = session.beginTransaction();
    } catch (Throwable e1) {
      throw new DBAccessException(DB_COMMON_ERROR, MSG_509049, e1);
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Commitment.
   *
   * @throws DBAccessException
   *           data base exception
   */
  public void commit() throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    try {
      tr.commit();
    } catch (Throwable e1) {
      throw new DBAccessException(COMMIT_FAILURE, MSG_509070, e1);
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Roll Back.
   *
   * @throws DBAccessException
   *           data base exception
   */
  public void rollback() throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    try {
      tr.rollback();
    } catch (Throwable e1) {
      throw new DBAccessException(DB_COMMON_ERROR, MSG_509049);
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Transaction End.
   *
   * @throws DBAccessException
   *           data base exception
   */
  @Override
  public void close() throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    try {
      session.close();
    } catch (Throwable e1) {
      throw new DBAccessException(DB_COMMON_ERROR, MSG_509049);
    }
    logger.trace(CommonDefinitions.END);
  }

  /**
   * Model Information Registration.
   *
   * @param equipments
   *          model information to be registered
   * @throws DBAccessException
   *           data base exception
   */
  public void addEquipments(Equipments equipments) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("Equipments:" + equipments);

    EquipmentsDAO equipmentsDao = new EquipmentsDAO(session);
    equipmentsDao.save(equipments);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Model List Information Acquisition.
   *
   * @return model information table all data
   * @throws DBAccessException
   *           data base exception
   */
  public List<Equipments> getEquipmentsList() throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    EquipmentsDAO equipmentsDao = new EquipmentsDAO(session);

    List<Equipments> list = equipmentsDao.getList();

    logger.trace(CommonDefinitions.END);

    return list;
  }

  /**
   * Model Information Acquisition.
   *
   * @param equipment_type_id
   *          model ID
   * @return model information correlated to the entered device ID
   * @throws DBAccessException
   *           data base exception
   */
  public Equipments searchEquipments(String equipment_type_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("equipment_type_id:" + equipment_type_id);

    EquipmentsDAO equipmentsDao = new EquipmentsDAO(session);

    Equipments equipments = equipmentsDao.search(equipment_type_id);

    logger.trace(CommonDefinitions.END);

    return equipments;
  }

  /**
   * Model Information Deletion.
   *
   * @param equipment_type_id
   *          model ID
   * @throws DBAccessException
   *           data base exception
   */
  public void deleteEquipments(String equipment_type_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("equipment_type_id:" + equipment_type_id);

    RemarkMenusDAO remarkMenusDao = new RemarkMenusDAO(session);
    remarkMenusDao.delete(equipment_type_id);

    EgressQueueMenusDAO egressMenusDao = new EgressQueueMenusDAO(session);
    egressMenusDao.delete(equipment_type_id);

    EquipmentIfsDAO equipmentIfsDao = new EquipmentIfsDAO(session);
    equipmentIfsDao.delete(equipment_type_id);

    IfNameRulesDAO ifNameRulesDao = new IfNameRulesDAO(session);
    ifNameRulesDao.delete(equipment_type_id);

    BootErrorMessagesDAO bootErrorMessages = new BootErrorMessagesDAO(session);
    bootErrorMessages.delete(equipment_type_id);

    EquipmentsDAO equipmentsDao = new EquipmentsDAO(session);
    equipmentsDao.delete(equipment_type_id);

    logger.trace(CommonDefinitions.END);
  }



  /**
   * Device Information Registration.
   *
   * @param nodes
   *          device information to be registered
   * @throws DBAccessException
   *           data base exception
   */
  public void addNodes(Nodes nodes) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("nodes:" + nodes.toString());

    NodesDAO nodesDao = new NodesDAO(session);
    nodesDao.save(nodes);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device Information List Acquisition.
   *
   * @return device information list
   * @throws DBAccessException
   *           data base exception
   */
  public List<Nodes> getNodesList() throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    NodesDAO nodesDao = new NodesDAO(session);

    List<Nodes> list = nodesDao.getList();

    logger.trace(CommonDefinitions.END);

    return list;
  }

  /**
   * Device Information Acquisition.
   *
   * @param node_id
   *          device ID
   * @param management_if_address
   *          management IF address (NULL is permitted)
   * @return device information
   * @throws DBAccessException
   *           data base exception
   */
  public Nodes searchNodes(String node_id, String management_if_address) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", management_if_address:" + management_if_address);

    NodesDAO nodesDao = new NodesDAO(session);

    Nodes nodes = nodesDao.search(node_id, management_if_address);

    logger.trace(CommonDefinitions.END);

    return nodes;
  }

  /**
   * Device Information Acquisition (Model ID).
   *
   * @param equipment_type_id
   *          model ID
   * @return device information list
   * @throws DBAccessException
   *           data base exception
   */
  public List<Nodes> searchNodesByEquipmentId(String equipment_type_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("equipment_type_id:" + equipment_type_id);

    NodesDAO nodesDao = new NodesDAO(session);

    List<Nodes> list = nodesDao.getList2(equipment_type_id);

    logger.trace(CommonDefinitions.END);
    return list;
  }

  /**
   * Device Related Information Registration. Register the BreakoutIfList of physicalIfs.Update the phtsical IF.Register the LagIF. Register the start-up notification.
   *
   * @param physicalIfsList
   *          physical IF information to be registred
   * @param lagIfsList
   *          LAG information to be registered
   * @param nodesStartUpNotify
   *          device notification information to be registered
   * @throws DBAccessException
   *           data base exception
   */
  public void addNodesRelation(List<PhysicalIfs> physicalIfsList, List<LagIfs> lagIfsList,
      NodesStartupNotification nodesStartUpNotify) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("physicalIfsList:" + physicalIfsList.toString() + ", lagIfsList:" + lagIfsList.toString()
        + ", nodesStartupNotification:" + nodesStartUpNotify);

    PhysicalIfsDAO physicalIfsDao = new PhysicalIfsDAO(session);
    BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);
    for (PhysicalIfs physicalIfs : physicalIfsList) {
      if (physicalIfs.getBreakoutIfsList() != null) {
        for (BreakoutIfs breakoutIfs : physicalIfs.getBreakoutIfsList()) {
          breakoutIfsDao.save(breakoutIfs, true);
        }
      }
      physicalIfsDao.updateAll(physicalIfs);
    }

    LagIfsDAO lagIfsDao = new LagIfsDAO(session);
    for (LagIfs lagIfs : lagIfsList) {
      lagIfsDao.save(lagIfs, true);
    }

    if (nodesStartUpNotify != null) {
      NodesStartupNotificationDAO nodesStartupNotificationDao = new NodesStartupNotificationDAO(session);
      nodesStartupNotificationDao.save(nodesStartUpNotify);
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device Information/Device Related Information Deletion.
   *
   * @param node_id
   *          device ID
   * @throws DBAccessException
   *           data base exception
   */
  public void deleteNodesRelation(String node_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id);

    NodesStartupNotificationDAO nodesStartupNotificationDao = new NodesStartupNotificationDAO(session);
    nodesStartupNotificationDao.delete(node_id, true);

    VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);
    if ((!vlanIfsDao.getList(node_id).isEmpty())) {
      vlanIfsDao.errorMessage(RELATIONSHIP_UNCONFORMITY, BaseDAO.NODES, null);
    }

    NodesDAO nodesDao = new NodesDAO(session);
    Nodes nodes = nodesDao.search(node_id, null);

    if (nodes.getLagIfsList() != null) {
      for (LagIfs lagIfs : nodes.getLagIfsList()) {
        deleteLagIfs(node_id, lagIfs.getLag_if_id());
      }
    }

    if (nodes.getPhysicalIfsList() != null) {
      for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
        if (physicalIfs.getBreakoutIfsList() != null) {
          for (BreakoutIfs breakoutIfs : physicalIfs.getBreakoutIfsList()) {
            deletebreakoutIfs(node_id, breakoutIfs.getBreakout_if_id());
          }
        }
      }
      PhysicalIfsDAO physicalIfsDao = new PhysicalIfsDAO(session);
      physicalIfsDao.deleteAll(node_id, true);
    }

    nodesDao.delete(node_id);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Opposing Device Removal Information Deletion/Update.
   *
   * @param nodesList
   *          model information list
   * @throws DBAccessException
   *           data base exception
   */
  public void deleteUpdateOppositeNodes(List<Nodes> nodesList) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("nodesList:" + nodesList);

    String node_id = null;

    for (Nodes nodes : nodesList) {
      node_id = nodes.getNode_id();

      for (LagIfs lagIfs : nodes.getLagIfsList()) {
        deleteLagIfs(node_id, lagIfs.getLag_if_id());
      }

      PhysicalIfsDAO physicalIfsDao = new PhysicalIfsDAO(session);
      BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);
      for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
        if (physicalIfs.getBreakoutIfsList() != null) {
          for (BreakoutIfs breakoutIfs : physicalIfs.getBreakoutIfsList()) {
            breakoutIfsDao.updateIP(breakoutIfs);
            breakoutIfsDao.updateState(breakoutIfs);
          }
        }
        physicalIfsDao.updateAll(physicalIfs);
      }

    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Interface List Information.
   *
   * @param node_id
   *          model ID
   * @return search result
   * @throws DBAccessException
   *           data base exception
   */
  public InterfacesList getInterfaceList(String node_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id);

    InterfacesList interfaceList = new InterfacesList();

    PhysicalIfsDAO physicalIfsDao = new PhysicalIfsDAO(session);
    interfaceList.setPhysicalIfsList(physicalIfsDao.getList(node_id));

    LagIfsDAO lagIfsDao = new LagIfsDAO(session);
    interfaceList.setLagIfsList(lagIfsDao.getList(node_id));

    BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);
    interfaceList.setBreakoutIfsList(breakoutIfsDao.getList(node_id));

    logger.trace(CommonDefinitions.END);

    return interfaceList;
  }

  /**
   * Physical IF Information List Acquisition.
   *
   * @param node_id
   *          device ID
   * @return IF related information
   * @throws DBAccessException
   *           data base exception
   */
  public List<PhysicalIfs> getPhysicalIfsList(String node_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id);

    PhysicalIfsDAO physicalIfsDao = new PhysicalIfsDAO(session);

    List<PhysicalIfs> list = physicalIfsDao.getList(node_id);

    logger.trace(CommonDefinitions.END);

    return list;
  }

  /**
   * Physical IF Information Acquisition.
   *
   * @param node_id
   *          device ID
   * @param physical_if_id
   *          physical IF ID
   * @return physical IF information
   * @throws DBAccessException
   *           data base exception
   */
  public PhysicalIfs searchPhysicalIfs(String node_id, String physical_if_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", physical_if_id:" + physical_if_id);

    PhysicalIfsDAO physicalIfsDao = new PhysicalIfsDAO(session);

    PhysicalIfs physicalIfs = physicalIfsDao.search(node_id, physical_if_id);

    logger.trace(CommonDefinitions.END);

    return physicalIfs;
  }

  /**
   * Physical IF Information Change.
   *
   * @param physicalIfs
   *          physical IF information
   * @throws DBAccessException
   *           data base exception
   */
  public void updatePhysicalIfs(PhysicalIfs physicalIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("physicalIfs:" + physicalIfs);

    PhysicalIfsDAO physicalIfsDao = new PhysicalIfsDAO(session);
    physicalIfsDao.update(physicalIfs);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * LagIF Registration.
   *
   * @param lagIfs
   *          LAG information
   * @throws DBAccessException
   *           data base exception
   */
  public void addLagIfs(LagIfs lagIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("lagIfs:" + lagIfs.toString());

    LagIfsDAO lagIfsDao = new LagIfsDAO(session);
    lagIfsDao.save(lagIfs, false);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * LagIF Information List Acquisition.
   *
   * @param node_id
   *          device ID
   * @return LAG information list
   * @throws DBAccessException
   *           data base exception
   */
  public List<LagIfs> getLagIfsList(String node_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id);

    LagIfsDAO lagIfsDao = new LagIfsDAO(session);

    List<LagIfs> list = lagIfsDao.getList(node_id);

    logger.trace(CommonDefinitions.END);

    return list;
  }

  /**
   * LagIF Information Acquisition.
   *
   * @param node_id
   *          device ID
   * @param lag_if_id
   *          LAGIF ID
   * @return LAG information
   * @throws DBAccessException
   *           data base exception
   */
  public LagIfs searchLagIfs(String node_id, String lag_if_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", lag_if_id:" + lag_if_id);

    LagIfsDAO lagIfsDao = new LagIfsDAO(session);

    LagIfs lagIfs = lagIfsDao.search(node_id, lag_if_id);

    logger.trace(CommonDefinitions.END);

    return lagIfs;
  }

  /**
   * LagIF Information Deletion.
   *
   * @param node_id
   *          device ID
   * @param lag_if_id
   *          LAGIF ID
   * @throws DBAccessException
   *           data base exception
   */
  public void deleteLagIfs(String node_id, String lag_if_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", lag_if_id:" + lag_if_id);

    LagMembersDAO lagMembersDao = new LagMembersDAO(session);
    lagMembersDao.delete(node_id, lag_if_id);

    LagIfsDAO lagIfsDao = new LagIfsDAO(session);
    lagIfsDao.delete(node_id, lag_if_id, false);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * L2VLAN IF Registration.
   *
   * @param vlan_ifs
   *          VLAN IF
   * @throws DBAccessException
   *           data base exception
   */
  public void addL2VlanIf(VlanIfs vlan_ifs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("vlan_ifs:" + vlan_ifs.toString());

    VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);
    vlanIfsDao.save(vlan_ifs);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * L3VLAN IF Registration.
   *
   * @param vlan_ifs
   *          VLAN IF
   * @throws DBAccessException
   *           data base exception
   */
  public void addL3VlanIf(VlanIfs vlan_ifs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("vlan_ifs:" + vlan_ifs.toString());

    VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);
    vlanIfsDao.save(vlan_ifs);

    logger.trace(CommonDefinitions.END);
  }


  /**
   * VLAN IF Information Acquisition.
   *
   * @param node_id
   *          device ID
   * @param vlan_if_id
   *          VLAN ID
   * @return vlanifs VLAN IF information
   * @throws DBAccessException
   *           data base exception
   */
  public VlanIfs searchVlanIfs(String node_id, String vlan_if_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", vlan_if_id:" + vlan_if_id);

    VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);

    VlanIfs vlanIfs = vlanIfsDao.search(node_id, vlan_if_id);

    logger.trace(CommonDefinitions.END);

    return vlanIfs;
  }

  /**
   * VLAN IF Information List Acquisition.
   *
   * @param node_id
   *          device ID
   * @return list VLANIF information list
   * @throws DBAccessException
   *           data base exception
   */
  public List<VlanIfs> getVlanIfsList(String node_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id);

    VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);

    List<VlanIfs> list = vlanIfsDao.getList(node_id);

    logger.trace(CommonDefinitions.END);

    return list;
  }

  /**
   * VLAN IF Deletion.
   *
   * @param node_id
   *          device ID
   * @param vlan_if_id
   *          VLAN IF ID
   * @throws DBAccessException
   *           data base exception
   */
  public void deleteVlanIfs(String node_id, String vlan_if_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", vlan_if_id:" + vlan_if_id);

    BGPOptionsDAO bgpOptDao = new BGPOptionsDAO(session);
    if (bgpOptDao.searchByVlanIfId(node_id, vlan_if_id) != null) {
      bgpOptDao.delete(node_id, vlan_if_id);
    }

    VRRPOptionsDAO vrrpOptDao = new VRRPOptionsDAO(session);
    if (vrrpOptDao.searchByVlanIfId(node_id, vlan_if_id) != null) {
      vrrpOptDao.delete(node_id, vlan_if_id);
    }

    StaticRouteOptionsDAO srOptDao = new StaticRouteOptionsDAO(session);
    if (srOptDao.search(node_id, vlan_if_id) != null) {
      srOptDao.delete(node_id, vlan_if_id);
    }

    VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);
    vlanIfsDao.delete(node_id, vlan_if_id, false);

    logger.trace(CommonDefinitions.END);
  }


  /**
   * breakoutIf Registration.
   *
   * @param breakoutIfs
   *          breakoutIf
   * @throws DBAccessException
   *           data base exception
   */
  public void addBreakoutIfs(BreakoutIfs breakoutIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("breakoutIfs:" + breakoutIfs.toString());

    BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);
    breakoutIfsDao.save(breakoutIfs, false);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * breakoutIf Information List Acquisition.
   *
   * @param node_id
   *          device ID
   * @return list breakoutIf information list
   * @throws DBAccessException
   *           data base exception
   */
  public List<BreakoutIfs> getBreakoutIfsList(String node_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id);

    BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);

    List<BreakoutIfs> list = breakoutIfsDao.getList(node_id);

    logger.trace(CommonDefinitions.END);

    return list;
  }

  /**
   * breakoutIf Information Acquisition.
   *
   * @param node_id
   *          device ID
   * @param breakout_if_id
   *          breakoutIfID
   * @return breakoutIfs breakoutIf
   * @throws DBAccessException
   *           data base exception
   */
  public BreakoutIfs searchBreakoutIf(String node_id, String breakout_if_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", breakout_if_id:" + breakout_if_id);

    BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);

    BreakoutIfs breakoutIfs = breakoutIfsDao.search(node_id, breakout_if_id);

    logger.trace(CommonDefinitions.END);

    return breakoutIfs;
  }

  /**
   * breakoutIf Deletion.
   *
   * @param node_id
   *          device ID
   * @param breakout_if_id
   *          breakoutIf ID
   * @throws DBAccessException
   *           data base exception
   */
  public void deletebreakoutIfs(String node_id, String breakout_if_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", breakout_if_id:" + breakout_if_id);

    BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);
    breakoutIfsDao.delete(node_id, breakout_if_id, false);

    logger.trace(CommonDefinitions.END);
  }


  /**
   * Syste Status Information Registration.
   *
   * @param systemStatus
   *          system status information registration
   * @throws DBAccessException
   *           data base exception
   */
  public void addSystemStatus(SystemStatus systemStatus) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("systemStatus:" + systemStatus.toString());

    SystemStatusDAO systemStatusDao = new SystemStatusDAO(session);
    systemStatusDao.save(systemStatus);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * System Status Information Acquisition.
   *
   * @return system status information
   * @throws DBAccessException
   *           data base exception
   */
  public SystemStatus getSystemStatus() throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    SystemStatusDAO systemStatusDao = new SystemStatusDAO(session);

    SystemStatus systemStatus = systemStatusDao.search();

    logger.trace(CommonDefinitions.END);

    return systemStatus;
  }

  /**
   * System Status Information Change.
   *
   * @param service_status
   *          service start-up status
   * @param blockade_status
   *          maintenance blockage status
   * @throws DBAccessException
   *           data base exception
   */
  public void updateSystemStatus(int service_status, int blockade_status) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("service_status:" + service_status + ", blockade_status:" + blockade_status);

    SystemStatusDAO systemStatusDao = new SystemStatusDAO(session);
    systemStatusDao.update(service_status, blockade_status);

    logger.trace(CommonDefinitions.END);
  }


  /**
   * Device Start-up Notification Information Registration.
   *
   * @param nodeInfo
   *          Device Start-up Notification Information
   * @throws DBAccessException
   *           data base exception
   */
  public void addNodesStartupNotification(NodesStartupNotification nodeInfo) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("nodeInfo:" + nodeInfo.toString());

    NodesStartupNotificationDAO nodesStartupNotificationDao = new NodesStartupNotificationDAO(session);
    nodesStartupNotificationDao.save(nodeInfo);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device Start-up Notification Information List Acquisition.
   *
   * @return device start-up notification information list
   * @throws DBAccessException
   *           data base exception
   */
  public List<NodesStartupNotification> getNodesStartupNotificationList() throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    NodesStartupNotificationDAO nodesStartupNotificationDao = new NodesStartupNotificationDAO(session);

    List<NodesStartupNotification> list = nodesStartupNotificationDao.getList();

    logger.trace(CommonDefinitions.END);
    return list;

  }

  /**
   * Device Start-up Notification Information Change.
   *
   * @param nodesStartupNotification
   *          device start-up notification information
   * @throws DBAccessException
   *           data base exception
   */
  public void updateNodesStartupNotification(NodesStartupNotification nodesStartupNotification)
      throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("nodesStartupNotification:" + nodesStartupNotification.toString());

    NodesStartupNotificationDAO nodesStartupNotificationDao = new NodesStartupNotificationDAO(session);
    nodesStartupNotificationDao.update(nodesStartupNotification);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device Start-up Notification Information Deletion.
   *
   * @param node_id
   *          device ID
   * @throws DBAccessException
   *           data base exception
   */
  public void deleteNodesStartupNotification(String node_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id);

    NodesStartupNotificationDAO nodesStartupNotificationDao = new NodesStartupNotificationDAO(session);

    if (node_id != null) {
      nodesStartupNotificationDao.delete(node_id, false);
    } else {
      nodesStartupNotificationDao.delete(node_id, true);
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device Status Information Update.
   *
   * @param node_id
   *          device ID
   * @param node_state
   *          device status
   * @throws DBAccessException
   *           data base exception
   */
  public void updateNodeState(String node_id, int node_state) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", node_state:" + node_state);

    NodesDAO nodeStateDao = new NodesDAO(session);
    nodeStateDao.updateState(node_id, node_state);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device IF Status Change.
   *
   * @param physicalIfs
   *          physical If
   * @param lagIfs
   *          LagIf
   * @param vlanIfs
   *          VLANIf
   * @param breakoutIfs
   *          breakoutIF
   * @throws DBAccessException
   *           data base exception
   */
  public void updateNodeIfState(PhysicalIfs physicalIfs, LagIfs lagIfs, VlanIfs vlanIfs, BreakoutIfs breakoutIfs)
      throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("physicalIfs:" + physicalIfs + "lagIfs:" + lagIfs + "vlanIfs" + vlanIfs + "breakoutIfs" + breakoutIfs);

    if (physicalIfs != null) {
      PhysicalIfsDAO physicalIfsDao = new PhysicalIfsDAO(session);
      physicalIfsDao.updateState(physicalIfs);
    }

    if (lagIfs != null) {
      LagIfsDAO lagIfsDao = new LagIfsDAO(session);
      lagIfsDao.updateState(lagIfs);
    }

    if (vlanIfs != null) {
      VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);
      vlanIfsDao.updateState(vlanIfs);
    }

    if (breakoutIfs != null) {
      BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);
      breakoutIfsDao.updateState(breakoutIfs);
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device IF IP Address Change.
   *
   * @param physicalIfs
   *          physical If
   * @param lagIfs
   *          LagIf
   * @param vlanIfs
   *          VLANIf
   * @param breakoutIfs
   *          breakoutIF
   * @throws DBAccessException
   *           data base exception
   */
  public void updateNodeIfIpAddress(PhysicalIfs physicalIfs, LagIfs lagIfs, VlanIfs vlanIfs, BreakoutIfs breakoutIfs)
      throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("physicalIfs:" + physicalIfs + "lagIfs:" + lagIfs + "vlanIfs:" + vlanIfs);

    if (physicalIfs != null) {
      PhysicalIfsDAO physicalIfsDao = new PhysicalIfsDAO(session);
      physicalIfsDao.updateIP(physicalIfs);
    }

    if (lagIfs != null) {
      LagIfsDAO lagIfsDao = new LagIfsDAO(session);
      lagIfsDao.updateIP(lagIfs);
    }

    if (vlanIfs != null) {
      VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);
      vlanIfsDao.updateIP(vlanIfs);
    }

    if (breakoutIfs != null) {
      BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);
      breakoutIfsDao.updateIP(breakoutIfs);
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Acquiring the Number of Registered Devices.
   *
   * @return size the number of registered devices
   * @throws DBAccessException
   *           data base exception
   */
  public int getNodesNum() throws DBAccessException {
    int size = 0;
    logger.trace(CommonDefinitions.START);
    NodesDAO nodesDao = new NodesDAO(session);

    List<Nodes> list = nodesDao.getList();
    if (list != null) {
      size = list.size();
    }
    logger.trace(CommonDefinitions.END);
    return size;

  }

  /**
   * Cluster Type Information.
   *
   * @return search result
   * @throws DBAccessException
   *           data base exception
   */
  public int checkClusterType() throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    NodesDAO nodesDao = new NodesDAO(session);
    List<Nodes> list = nodesDao.getList();
    int routerType = CommonDefinitions.ROUTER_TYPE_UNKNOWN;
    if (!list.isEmpty()) {
      routerType = list.get(0).getEquipments().getRouter_type();
    }

    logger.trace(CommonDefinitions.END);
    return routerType;
  }

  /**
   * breakoutIF Change.
   *
   * @param breakoutIfs
   *          breakoutIF information
   * @throws DBAccessException
   *           data base exception
   */
  public void updateBreakoutIfs(BreakoutIfs breakoutIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("breakoutIfs:" + breakoutIfs);

    BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);
    breakoutIfsDao.updateState(breakoutIfs);

    logger.trace(CommonDefinitions.END);

  }

  /**
   * static Route Option Information Registration.
   *
   * @param staticRouteOptions
   *          static route option information
   * @throws DBAccessException
   *           data base exception
   */
  public void addStaticRouteOptions(StaticRouteOptions staticRouteOptions) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("staticRouteOptions:" + staticRouteOptions.toString());

    StaticRouteOptionsDAO staticRouteOptionsDao = new StaticRouteOptionsDAO(session);
    staticRouteOptionsDao.save(staticRouteOptions);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * static Route Option Information Deletion.
   *
   * @param staticRouteOptions
   *          static route option information
   * @throws DBAccessException
   *           data base exception
   */
  public void deleteStaticRouteOptions(StaticRouteOptions staticRouteOptions) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("staticRouteOptionsList:" + staticRouteOptions.toString());

    StaticRouteOptionsDAO staticRouteOptionsDao = new StaticRouteOptionsDAO(session);
    staticRouteOptionsDao.delete(staticRouteOptions);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * VLAN QoS Information Update.
   *
   * @param vlanIfs
   *          VLANIf
   * @throws DBAccessException
   *           data base exception
   */
  public void updateVlanIfs(VlanIfs vlanIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("vlanIfList:" + vlanIfs.toString());

    VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);
    vlanIfsDao.updateQos(vlanIfs);

    logger.trace(CommonDefinitions.END);
  }


  /**
   * Change in Additional Service Recovery device information.
   *
   * @param nodes
   *          Device information after recovery
   * @param addPhysiIfsList
   *          Physical IF information list to be added
   * @param delPhysiIfsList
   *          Physical IF information list to be deleted
   * @param vlanIfsList
   *          VLAN IF Information list after recovery
   * @throws DBAccessException
   *           data base exception
   */
  public void updateForRecover(Nodes nodes, List<PhysicalIfs> addPhysiIfsList, List<PhysicalIfs> delPhysiIfsList,
      List<VlanIfs> vlanIfsList) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("nodes:" + nodes + ", addPhysiIfsList:" + addPhysiIfsList + ", delPhysiIfsList:" + delPhysiIfsList
        + ", vlanIfsList:" + vlanIfsList);

    NodesDAO nodesDao = new NodesDAO(session);
    nodesDao.updateNodes(nodes);

    PhysicalIfsDAO physicalIfsDao = new PhysicalIfsDAO(session);
    BreakoutIfsDAO boIfsDao = new BreakoutIfsDAO(session);
    for (PhysicalIfs elem : nodes.getPhysicalIfsList()) {
      if (elem.getIf_name() != null) {
        physicalIfsDao.update(elem);
      }
      if (elem.getBreakoutIfsList() != null) {
        for (BreakoutIfs boElem : elem.getBreakoutIfsList()) {
          boIfsDao.updateIfName(boElem);
        }
      }
    }

    for (PhysicalIfs elem : addPhysiIfsList) {
      physicalIfsDao.save(elem);
    }

    for (PhysicalIfs elem : delPhysiIfsList) {
      physicalIfsDao.delete(elem.getNode_id(), elem.getPhysical_if_id());
    }

    LagIfsDAO lagIfsDao = new LagIfsDAO(session);
    for (LagIfs elem : nodes.getLagIfsList()) {
      lagIfsDao.updateName(elem);
    }

    VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);
    for (VlanIfs elem : vlanIfsList) {
      vlanIfsDao.updateIfName(elem);
      vlanIfsDao.updateQos(elem);
    }

    logger.trace(CommonDefinitions.END);
  }

}
