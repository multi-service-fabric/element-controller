/*
 * Copyright(c) 2019 Nippon Telegraph and Telephone Corporation
 */

package msf.ecmm.db;

import static msf.ecmm.common.LogFormatter.*;
import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.common.log.MsfLogger;
import msf.ecmm.db.dao.AclConfDAO;
import msf.ecmm.db.dao.BGPOptionsDAO;
import msf.ecmm.db.dao.BaseDAO;
import msf.ecmm.db.dao.BootErrorMessagesDAO;
import msf.ecmm.db.dao.BreakoutIfsDAO;
import msf.ecmm.db.dao.DummyVlanIfsDAO;
import msf.ecmm.db.dao.EgressQueueMenusDAO;
import msf.ecmm.db.dao.EquipmentIfsDAO;
import msf.ecmm.db.dao.EquipmentsDAO;
import msf.ecmm.db.dao.IRBInstanceInfoDAO;
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
import msf.ecmm.db.pojo.AclConf;
import msf.ecmm.db.pojo.BreakoutIfs;
import msf.ecmm.db.pojo.DummyVlanIfs;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.IRBInstanceInfo;
import msf.ecmm.db.pojo.InterfacesList;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.LagMembers;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.NodesStartupNotification;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.StaticRouteOptions;
import msf.ecmm.db.pojo.SystemStatus;
import msf.ecmm.db.pojo.VlanIfs;

/**
 * DB access management class.
 */
public class DBAccessManager implements AutoCloseable {

  /** logger instance. */
  private final MsfLogger logger = new MsfLogger();
  /** session instance. */
  protected Session session;
  /** transaction instance. */
  protected Transaction tr;

  /**
   * constructor.
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
   * start transaction.
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
   * commit.
   *
   * @throws DBAccessException
   *           database exception
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
   * roll back.
   *
   * @throws DBAccessException
   *           database exception
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
   * transaction finish.
   *
   * @throws DBAccessException
   *           database exception
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
   * model information registration.
   *
   * @param equipments
   *          Model information to be registered
   * @throws DBAccessException
   *           database exception
   */
  public void addEquipments(Equipments equipments) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("Equipments:" + equipments);

    EquipmentsDAO equipmentsDao = new EquipmentsDAO(session);
    equipmentsDao.save(equipments);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Model list information acquisition.
   *
   * @return model information table entire data
   * @throws DBAccessException
   *           database exception
   */
  public List<Equipments> getEquipmentsList() throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    EquipmentsDAO equipmentsDao = new EquipmentsDAO(session);

    List<Equipments> list = equipmentsDao.getList();

    logger.trace(CommonDefinitions.END);

    return list;
  }

  /**
   * model information acquisition.
   *
   * @param equipment_type_id
   *          model ID
   * @return model information which is associated with the input model ID
   * @throws DBAccessException
   *           database exception
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
   * model information deletion.
   *
   * @param equipment_type_id
   *          model ID
   * @throws DBAccessException
   *           database exception
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
   * device information registration.
   *
   * @param nodes
   *          device information to be registered
   * @throws DBAccessException
   *           database exception
   */
  public void addNodes(Nodes nodes) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("nodes:" + nodes.toString());

    NodesDAO nodesDao = new NodesDAO(session);
    nodesDao.save(nodes);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device information list acquisition.
   *
   * @return Device information list
   * @throws DBAccessException
   *           database exception
   */
  public List<Nodes> getNodesList() throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    NodesDAO nodesDao = new NodesDAO(session);

    List<Nodes> list = nodesDao.getList();

    logger.trace(CommonDefinitions.END);

    return list;
  }

  /**
   * Device information Acquisition.
   *
   * @param node_id
   *          Device ID
   * @param management_if_address
   *          Management IF address (NULL permitted)
   * @return Device information 
   * @throws DBAccessException
   *           database exception
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
   * Device information Acquisition (Model ID).
   *
   * @param equipment_type_id
   *          Model ID
   * @return Device information list
   * @throws DBAccessException
   *           database exception
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
   * Device-related information registration. Registering the BreakoutIf List of physicalIfs. Updating Physical IF. Register LagIF. Registering start-up notification.
   *
   * @param physicalIfsList
   *          Physical IF information to be registered
   * @param lagIfsList
   *          LAG information to be registered
   * @param nodesStartUpNotify
   *          Device notification that you want to register
   * @throws DBAccessException
   *           database exception
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
   * Device information / Device-related information Deletion.
   *
   * @param node_id
   *          Device ID
   * @throws DBAccessException
   *           database exception
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
   * Opposing device removal information Deletion/ update.
   *
   * @param nodesList
   *          Model information list
   * @throws DBAccessException
   *           database exception
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
   * Interface list information.
   *
   * @param node_id
   *          Model ID
   * @return search result
   * @throws DBAccessException
   *           database exception
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
   * Physical IF Informaiton List Acquisition.
   *
   * @param node_id
   *          Device ID
   * @return IF related information
   * @throws DBAccessException
   *           database exception
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
   * Physical IF information Acquisition.
   *
   * @param node_id
   *          Device ID
   * @param physical_if_id
   *          Physical IFID
   * @return Physical IF information
   * @throws DBAccessException
   *           database exception
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
   * Physical IF information Change.
   *
   * @param physicalIfs
   *          Physical IF information
   * @throws DBAccessException
   *           database exception
   */
  public void updatePhysicalIfs(PhysicalIfs physicalIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("physicalIfs:" + physicalIfs);

    PhysicalIfsDAO physicalIfsDao = new PhysicalIfsDAO(session);
    physicalIfsDao.update(physicalIfs);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * LagIF registration.
   *
   * @param lagIfs
   *          LAG information
   * @throws DBAccessException
   *           database exception
   */
  public void addLagIfs(LagIfs lagIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("lagIfs:" + lagIfs.toString());

    LagIfsDAO lagIfsDao = new LagIfsDAO(session);
    lagIfsDao.save(lagIfs, false);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * LagIF Informaiton List Acquisition.
   *
   * @param node_id
   *          Device ID
   * @return LAG information list
   * @throws DBAccessException
   *           database exception
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
   * LagIFinformationAcquisition.
   *
   * @param node_id
   *          Device ID
   * @param lag_if_id
   *          LAGIF ID
   * @return LAGinformation
   * @throws DBAccessException
   *           database exception
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
   * Changing LagIF.
   *
   * @param lagIfs
   *          Lag IF
   * @param addFlg
   *          Lag-addition flag
   * @throws DBAccessException
   *           database exceptions
   */
  public void updateLagIfs(LagIfs lagIfs, boolean addFlg) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("lagIfs:" + lagIfs + ", addFlg:" + addFlg);

    LagIfsDAO lagIfsDao = new LagIfsDAO(session);
    LagMembersDAO lagMembersDao = new LagMembersDAO(session);

    lagIfsDao.updateMinLinkNum(lagIfs);

    if (addFlg) {
      for (LagMembers lagMembers : lagIfs.getLagMembersList()) {
        lagMembersDao.save(lagMembers);
      }
    } else {
      for (LagMembers lagMembers : lagIfs.getLagMembersList()) {
        lagMembersDao.delete(lagMembers);
      }
    }

    logger.trace(CommonDefinitions.END);
  }

  /**
   * LagIFinformation Deletion.
   *
   * @param node_id
   *          Device ID
   * @param lag_if_id
   *          LAGIF ID
   * @throws DBAccessException
   *           database exception
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
   * LAG member information acquisition
   *
   * @param node_id
   *          Device ID
   * @param lag_if_id
   *          LAG IF ID
   * @throws DBAccessException
   *           database exception
   */
  public LagMembers searchLagMember(String node_id, String lag_if_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", lag_if_id:" + lag_if_id);
    LagMembersDAO lagMembersDAO = new LagMembersDAO(session);
    LagMembers lagMembers = lagMembersDAO.search(node_id, lag_if_id, null, null);

    logger.trace(CommonDefinitions.END);

    return lagMembers;
  }

  /**
   * L2VLAN IF registration.
   *
   * @param vlan_ifs
   *          VLAN IF
   * @throws DBAccessException
   *           database exception
   */
  public void addL2VlanIf(VlanIfs vlan_ifs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("vlan_ifs:" + vlan_ifs.toString());

    VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);
    vlanIfsDao.save(vlan_ifs);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * L3VLAN IF registration.
   *
   * @param vlan_ifs
   *          VLAN IF
   * @throws DBAccessException
   *           database exception
   */
  public void addL3VlanIf(VlanIfs vlan_ifs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("vlan_ifs:" + vlan_ifs.toString());

    VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);
    vlanIfsDao.save(vlan_ifs);

    logger.trace(CommonDefinitions.END);
  }


  /**
   * VLAN IF information Acquisition.
   *
   * @param node_id
   *          Device ID
   * @param vlan_if_id
   *          VLAN ID
   * @return vlanifs VLAN IF information
   * @throws DBAccessException
   *           database exception
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
   * VLAN IF information list acquisition.
   *
   * @param node_id
   *          Device ID
   * @return list VLANIFinformation list
   * @throws DBAccessException
   *           database exception
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
   *          Device ID
   * @param vlan_if_id
   *          VLAN IF ID
   * @throws DBAccessException
   *           database exception
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
   * breakoutIf registration.
   *
   * @param breakoutIfs
   *          breakoutIf
   * @throws DBAccessException
   *           database exception
   */
  public void addBreakoutIfs(BreakoutIfs breakoutIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("breakoutIfs:" + breakoutIfs.toString());

    BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);
    breakoutIfsDao.save(breakoutIfs, false);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * breakoutIf information list acquisition.
   *
   * @param node_id
   *          Device ID
   * @return list breakoutIf information list
   * @throws DBAccessException
   *           database exception
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
   * breakoutIf information acquisition.
   *
   * @param node_id
   *          Device ID
   * @param breakout_if_id
   *          breakoutIf ID
   * @return breakoutIfs breakoutIf
   * @throws DBAccessException
   *           database exception
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
   *          Device ID
   * @param breakout_if_id
   *          breakoutIf ID
   * @throws DBAccessException
   *           database exception
   */
  public void deletebreakoutIfs(String node_id, String breakout_if_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", breakout_if_id:" + breakout_if_id);

    BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);
    breakoutIfsDao.delete(node_id, breakout_if_id, false);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * System status information registration.
   *
   * @param systemStatus
   *          System status information registration
   * @throws DBAccessException
   *           database exception
   */
  public void addSystemStatus(SystemStatus systemStatus) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("systemStatus:" + systemStatus.toString());

    SystemStatusDAO systemStatusDao = new SystemStatusDAO(session);
    systemStatusDao.save(systemStatus);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * System status information acquisition.
   *
   * @return system status information
   * @throws DBAccessException
   *           database exception
   */
  public SystemStatus getSystemStatus() throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    SystemStatusDAO systemStatusDao = new SystemStatusDAO(session);

    SystemStatus systemStatus = systemStatusDao.search();

    logger.trace(CommonDefinitions.END);

    return systemStatus;
  }

  /**
   * System status information change.
   *
   * @param service_status
   *          Service start-up status
   * @param blockade_status
   *          Maintenance blockage status
   * @throws DBAccessException
   *           database exception
   */
  public void updateSystemStatus(int service_status, int blockade_status) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("service_status:" + service_status + ", blockade_status:" + blockade_status);

    SystemStatusDAO systemStatusDao = new SystemStatusDAO(session);
    systemStatusDao.update(service_status, blockade_status);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device start-up notification registration.
   *
   * @param nodeInfo
   *          Device start-up notification
   * @throws DBAccessException
   *           database exception
   */
  public void addNodesStartupNotification(NodesStartupNotification nodeInfo) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("nodeInfo:" + nodeInfo.toString());

    NodesStartupNotificationDAO nodesStartupNotificationDao = new NodesStartupNotificationDAO(session);
    nodesStartupNotificationDao.save(nodeInfo);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device start-up notification list acquisition.
   *
   * @return Device start-up notification list
   * @throws DBAccessException
   *           database exception
   */
  public List<NodesStartupNotification> getNodesStartupNotificationList() throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    NodesStartupNotificationDAO nodesStartupNotificationDao = new NodesStartupNotificationDAO(session);

    List<NodesStartupNotification> list = nodesStartupNotificationDao.getList();

    logger.trace(CommonDefinitions.END);
    return list;

  }

  /**
   * Device start-up notification Change.
   *
   * @param nodesStartupNotification
   *          Device start-up notification
   * @throws DBAccessException
   *           database exception
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
   * Device start-up notification Deletion.
   *
   * @param node_id
   *          Device ID
   * @throws DBAccessException
   *           database exception
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
   * Device status information update.
   *
   * @param node_id
   *          Device ID
   * @param node_state
   *          Device status 
   * @throws DBAccessException
   *           database exception
   */
  public void updateNodeState(String node_id, int node_state) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", node_state:" + node_state);

    NodesDAO nodeStateDao = new NodesDAO(session);
    nodeStateDao.updateState(node_id, node_state);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * node information updates（node ID）.
   *
   * @param node_id
   *          node ID
   * @param equipment_type_id
   *          equipment_type_id
   * @throws DBAccessException
   *          database exceptions
   */
  public void updateNodeEquipment(String node_id, String equipment_type_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", equipment_type_id:" + equipment_type_id);

    NodesDAO nodeStateDao = new NodesDAO(session);
    nodeStateDao.updateEquipment(node_id, equipment_type_id);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device IF status change.
   *
   * @param physicalIfs
   *          Physical If
   * @param lagIfs
   *          LagIf
   * @param vlanIfs
   *          VLANIf
   * @param breakoutIfs
   *          breakoutIF
   * @throws DBAccessException
   *           database exception
   */
  public void updateNodeIfState(PhysicalIfs physicalIfs, LagIfs lagIfs, VlanIfs vlanIfs, BreakoutIfs breakoutIfs)
      throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug(
        "physicalIfs:" + physicalIfs + ", lagIfs:" + lagIfs + ", vlanIfs:" + vlanIfs + "breakoutIfs" + breakoutIfs);

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
   * Device IF IP address change.
   *
   * @param physicalIfs
   *          Physical If
   * @param lagIfs
   *          LagIf
   * @param vlanIfs
   *          VLANIf
   * @throws DBAccessException
   *           database exception
   */
  public void updateNodeIfIpAddress(PhysicalIfs physicalIfs, LagIfs lagIfs, VlanIfs vlanIfs, BreakoutIfs breakoutIfs)
      throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("physicalIfs:" + physicalIfs + ", lagIfs:" + lagIfs + ", vlanIfs:" + vlanIfs);

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
   * Acquire the no. of the registered devices.
   *
   * @return size the no. of the registered devices
   * @throws DBAccessException
   *           database exception
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
   * Cluster Type Informaiton.
   *
   * @return search result
   * @throws DBAccessException
   *           database exception
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
   *          breakoutIFinformation
   * @throws DBAccessException
   *           database exception
   */
  public void updateBreakoutIfs(BreakoutIfs breakoutIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("breakoutIfs:" + breakoutIfs);

    BreakoutIfsDAO breakoutIfsDao = new BreakoutIfsDAO(session);
    breakoutIfsDao.updateState(breakoutIfs);

    logger.trace(CommonDefinitions.END);

  }

  /**
   * Static route option information registration.
   *
   * @param staticRouteOptions
   *          static route option information
   * @throws DBAccessException
   *           database exception
   */
  public void addStaticRouteOptions(StaticRouteOptions staticRouteOptions) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("staticRouteOptions:" + staticRouteOptions.toString());

    StaticRouteOptionsDAO staticRouteOptionsDao = new StaticRouteOptionsDAO(session);
    staticRouteOptionsDao.save(staticRouteOptions);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * static route option information deletion.
   *
   * @param staticRouteOptions
   *          static route option information
   * @throws DBAccessException
   *           database exception
   */
  public void deleteStaticRouteOptions(StaticRouteOptions staticRouteOptions) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("staticRouteOptionsList:" + staticRouteOptions.toString());

    StaticRouteOptionsDAO staticRouteOptionsDao = new StaticRouteOptionsDAO(session);
    staticRouteOptionsDao.delete(staticRouteOptions);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * VLAN IF information (QoS) update.
   *
   * @param vlanIfs
   *          VLAN IFinformation
   * @throws DBAccessException
   *           database exception
   */
  public void updateVlanIfs(VlanIfs vlanIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("vlanIfList:" + vlanIfs.toString());

    VlanIfsDAO vlanIfsDao = new VlanIfsDAO(session);
    vlanIfsDao.updateQos(vlanIfs);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Device information change at the time of recovery expansion.
   *
   * @param nodes
   *           Device information after change 
   * @param addPhysiIfsList
   *          Addition target physical IF information
   * @param delPhysiIfsList
   *           Deletion target physical IF information
   * @param vlanIfsList
   *           VLAN IF information list after change
   * @throws DBAccessException
   *           database exception
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

  /**
   * Device information change at the time of recovery expansion.
   *
   * @param nodes
   *          Device information after change
   * @param addPhysiIfsList
   *          The physical IF information to be deleted
   * @param delPhysiIfsList
   *          The physical IF informaition to be deleted
   * @param vlanIfsList
   *          VLAN IF information list after change
   * @throws DBAccessException
   *           database exception
   */


  /**
   * IRB instance information registration.
   *
   * @param irbInstanceInfo
   *          IRB instance information
   * @throws DBAccessException
   *           database exception
   */
  public void addIrbInstanceInfo(IRBInstanceInfo irbInstanceInfo) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("irbInstanceInfo:" + irbInstanceInfo.toString());

    IRBInstanceInfoDAO irbInstanceInfoDAO = new IRBInstanceInfoDAO(session);
    irbInstanceInfoDAO.save(irbInstanceInfo);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * IRB instance information acquisition.
   *
   * @param node_id
   *          Device ID
   * @param vlan_id
   *          VLAN ID
   * @return irbInstanceInfo IRB instance information
   * @throws DBAccessException
   *           database exception
   */
  public IRBInstanceInfo searchIrbInstanceInfo(String node_id, String vlan_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", vlan_id:" + vlan_id);

    IRBInstanceInfoDAO irbInstanceInfoDAO = new IRBInstanceInfoDAO(session);

    IRBInstanceInfo irbInstanceInfo = irbInstanceInfoDAO.search(node_id, vlan_id);

    logger.trace(CommonDefinitions.END);

    return irbInstanceInfo;
  }

  /**
   * IRB instance information ID list acquisition.
   *
   * @return irbInstanceIdList IRB instance ID list
   * @throws DBAccessException
   *           database exception
   */
  public List<String> getIrbInstanceInfoIdList() throws DBAccessException {
    logger.trace(CommonDefinitions.START);

    IRBInstanceInfoDAO irbInstanceInfoDAO = new IRBInstanceInfoDAO(session);
    List<String> irbInstanceInfoIdList = irbInstanceInfoDAO.getList();

    return irbInstanceInfoIdList;
  }

  /**
   * IRB instance information Change.
   *
   * @param irbInstanceInfo
   *          IRB instance information
   * @throws DBAccessException
   *           database exception
   */
  public void updateIrbInstanceInfo(IRBInstanceInfo irbInstanceInfo) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("irbInstanceInfo:" + irbInstanceInfo.toString());

    IRBInstanceInfoDAO irbInstanceInfoDAO = new IRBInstanceInfoDAO(session);

    irbInstanceInfoDAO.updateState(irbInstanceInfo);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * IRB instance information Deletion.
   *
   * @param node_id
   *          Device ID
   * @param vlan_id
   *          VLAN ID
   * @throws DBAccessException
   *           database exception
   */
  public void deleteIrbInstanceInfo(String node_id, String vlan_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", vlan_id:" + vlan_id);

    IRBInstanceInfoDAO irbInstanceInfoDAO = new IRBInstanceInfoDAO(session);

    irbInstanceInfoDAO.delete(node_id, vlan_id);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Dummy VLAN IF information registration.
   *
   * @param dummyVlanIfs
   *          Dummy VLAN IF information
   * @throws DBAccessException
   *           database exception
   */
  public void addDummyVlanIfsInfo(DummyVlanIfs dummyVlanIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("dummyVlanIfs:" + dummyVlanIfs.toString());

    DummyVlanIfsDAO dummyVlanIfsDAO = new DummyVlanIfsDAO(session);

    dummyVlanIfsDAO.save(dummyVlanIfs);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Dummy VLAN IFinformationlist acquisition.
   *
   * @param node_id
   *          Device ID
   * @return dummy VlanIfsList Dummy VLAN IF information list
   * @throws DBAccessException
   *           database exception
   *
   */
  public List<DummyVlanIfs> getDummyVlanIfsInfoList(String node_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id);

    DummyVlanIfsDAO dummyVlanIfsDAO = new DummyVlanIfsDAO(session);
    List<DummyVlanIfs> dummyVlanIfsList = new ArrayList<DummyVlanIfs>();

    dummyVlanIfsList = dummyVlanIfsDAO.getList(node_id);

    logger.trace(CommonDefinitions.END);
    return dummyVlanIfsList;
  }

  /**
   * Dummy VLAN IF information acquisition.
   *
   * @param node_id
   *          Device ID
   * @param vlan_if_id
   *          VLAN IF ID
   * @return dummy VlanIfsInfo Dummy VLAN IF information
   * @throws DBAccessException
   *           database exception
   */
  public DummyVlanIfs searchDummyVlanIfsInfo(String node_id, String vlan_if_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", vlan_if_id:" + vlan_if_id);

    DummyVlanIfsDAO dummyVlanIfsDAO = new DummyVlanIfsDAO(session);
    DummyVlanIfs dummyVlanIfs = dummyVlanIfsDAO.search(node_id, vlan_if_id);

    logger.trace(CommonDefinitions.END);
    return dummyVlanIfs;
  }

  /**
   * Dummy VLAN IF information change.
   *
   * @param dummy VlanIfs
   *          Dummy VLAN IF information
   * @throws DBAccessException
   *           database exception
   */
  public void updateDummyVlanIfsInfo(DummyVlanIfs dummyVlanIfs) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("dummyVlanIfs:" + dummyVlanIfs.toString());

    DummyVlanIfsDAO dummyVlanIfsDAO = new DummyVlanIfsDAO(session);
    dummyVlanIfsDAO.updateState(dummyVlanIfs);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * Dummy VLAN IFinformation Deletion.
   *
   * @param node_id
   *          Device ID
   * @param vlan_if_id
   *          VLAN IF ID
   * @throws DBAccessException
   *           database exception
   */
  public void deleteDummyVlanIfsInfo(String node_id, String vlan_if_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", vlan_if_id:" + vlan_if_id);

    DummyVlanIfsDAO dummyVlanIfsDAO = new DummyVlanIfsDAO(session);
    dummyVlanIfsDAO.delete(node_id, vlan_if_id);

    logger.trace(CommonDefinitions.END);
  }

  /**
   * ACL configuration information list acquisition.
   *
   * @param node_id
   *          Device ID
   * @return aclConfList ACL configuration information list
   * @throws DBAccessException
   *           database exception
   */
  public List<AclConf> getAclConfList(String node_id) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id);

    AclConfDAO aclConfDAO = new AclConfDAO(session);
    List<AclConf> aclConfList = aclConfDAO.getList(node_id);
    return aclConfList;
  }

  /**
   * Acquire ACL configuration information.
   *
   * @param node_id
   *          Device ID
   * @param if_id
   *          IF ID
   * @param if_type
   *          IF Type
   * @return aclConf ACL configuration information
   * @throws DBAccessException
   *           database exception
   */
  public AclConf getAclConf(String node_id, String if_id, String if_type) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("node_id:" + node_id + ", if_id:" + if_id + ", if_type:" + if_type);

    AclConfDAO aclConfDAO = new AclConfDAO(session);
    AclConf aclConf = aclConfDAO.search(node_id, if_id, if_type);
    return aclConf;
  }

  /**
   * ACL configuration information/ACL configuration details information registration.
   *
   * @param aclConf
   *          ACL configuration information
   * @throws DBAccessException
   *           database exception
   */
  public void addAclConf(AclConf aclConf) throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug("aclConf:" + aclConf.toString());
    AclConfDAO aclConfDAO = new AclConfDAO(session);
    aclConfDAO.save(aclConf);
  }

  /**
   * ACL configuration information/ACL configuration details information deletion.
   *
   * @param nodeId
   *          Device ID
   * @param ifId
   *          IF ID
   * @param ifType
   *          IF Type
   * @throws DBAccessException
   *           database exception
   */
  public void deleteAclConf(String nodeId, String ifId, String ifType, List<String> termNameList)
      throws DBAccessException {
    logger.trace(CommonDefinitions.START);
    logger.debug(
        "node_id:" + nodeId + ", if_id:" + ifId + ", if_type:" + ifType + ", termNameList" + termNameList.toString());
    AclConfDAO aclConfDAO = new AclConfDAO(session);
    aclConfDAO.delete(nodeId, ifId, ifType, termNameList);
  }
}
