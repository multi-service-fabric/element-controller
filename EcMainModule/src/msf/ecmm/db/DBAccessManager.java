
package msf.ecmm.db;

import static msf.ecmm.common.LogFormatter.*;
import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.common.CommonDefinitions;
import msf.ecmm.db.dao.EquipmentIfsDAO;
import msf.ecmm.db.dao.EquipmentsDAO;
import msf.ecmm.db.dao.IfNameRulesDAO;
import msf.ecmm.db.dao.InternalLinkIfsDAO;
import msf.ecmm.db.dao.L2CpsDAO;
import msf.ecmm.db.dao.L3CpsDAO;
import msf.ecmm.db.dao.LagIfsDAO;
import msf.ecmm.db.dao.NodesDAO;
import msf.ecmm.db.dao.NodesStartupNotificationDAO;
import msf.ecmm.db.dao.PhysicalIfsDAO;
import msf.ecmm.db.dao.SystemStatusDAO;
import msf.ecmm.db.pojo.CpsList;
import msf.ecmm.db.pojo.Equipments;
import msf.ecmm.db.pojo.InternalLinkIfs;
import msf.ecmm.db.pojo.L2Cps;
import msf.ecmm.db.pojo.L3Cps;
import msf.ecmm.db.pojo.LagIfs;
import msf.ecmm.db.pojo.Nodes;
import msf.ecmm.db.pojo.NodesStartupNotification;
import msf.ecmm.db.pojo.PhysicalIfs;
import msf.ecmm.db.pojo.SystemStatus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DBAccessManager implements AutoCloseable {

	protected Session session;
	public DBAccessManager() throws DBAccessException {
		try {
			session = SessionManager.getInstance().getSession();
		} catch (Throwable e) {
			throw new DBAccessException(DB_COMMON_ERROR, MSG_509049, e);
		}
	}

	public void startTransaction() throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		try {
			tr = session.beginTransaction();
		} catch (Throwable e) {
			throw new DBAccessException(DB_COMMON_ERROR, MSG_509049, e);
		}
		logger.trace(CommonDefinitions.END);
	}

	public void commit() throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		try {
			tr.commit();
		} catch (Throwable e) {
			throw new DBAccessException(COMMIT_FAILURE, MSG_509070, e);
		}
		logger.trace(CommonDefinitions.END);
	}

	public void rollback() throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		try {
			tr.rollback();
		} catch (Throwable e) {
			throw new DBAccessException(DB_COMMON_ERROR, MSG_509049);
		}
		logger.trace(CommonDefinitions.END);
	}

	@Override
	public void close() throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		try {
			session.close();
		} catch (Throwable e) {
			throw new DBAccessException(DB_COMMON_ERROR, MSG_509049);
		}
		logger.trace(CommonDefinitions.END);
	}

	public void addEquipments(Equipments ePojo) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("EquipmentsPojo:" + ePojo);

		EquipmentsDAO equipmentsDAO = new EquipmentsDAO(session);
		equipmentsDAO.save(ePojo);

		logger.trace(CommonDefinitions.END);
	}

	public List<Equipments> getEquipmentsList() throws DBAccessException {
		logger.trace(CommonDefinitions.START);

		EquipmentsDAO equipmentsDAO = new EquipmentsDAO(session);

		List<Equipments> list = equipmentsDAO.getList();

		logger.trace(CommonDefinitions.END);

		return list;
	}

	public Equipments searchEquipments(String equipment_type_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("equipment_type_id:" + equipment_type_id);

		EquipmentsDAO equipmentsDAO = new EquipmentsDAO(session);

		Equipments equipments = equipmentsDAO.search(equipment_type_id);

		logger.trace(CommonDefinitions.END);

		return equipments;
	}

	public void deleteEquipments(String equipment_type_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("equipment_type_id:" + equipment_type_id);

		EquipmentIfsDAO equipmentIfsDAO = new EquipmentIfsDAO(session);
		equipmentIfsDAO.delete(equipment_type_id);

		IfNameRulesDAO ifNameRulesDAO = new IfNameRulesDAO(session);
		ifNameRulesDAO.delete(equipment_type_id);

		EquipmentsDAO equipmentsDAO = new EquipmentsDAO(session);
		equipmentsDAO.delete(equipment_type_id);

		logger.trace(CommonDefinitions.END);
	}



	public void addNodes(Nodes nodes) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("nodes:" + nodes.toString());

		NodesDAO nodesDAO = new NodesDAO(session);
		nodesDAO.save(nodes);

		logger.trace(CommonDefinitions.END);
	}

	public Nodes searchNodes(int node_type, String node_id, String management_if_address) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("node_type:" + node_type + ", node_id:" + node_id + ", management_if_address:"
				+ management_if_address);

		NodesDAO nodesDAO = new NodesDAO(session);

		Nodes nodes = nodesDAO.search(node_type, node_id, management_if_address);

		logger.trace(CommonDefinitions.END);

		return nodes;
	}

	public List<Nodes> searchNodesByEquipmentId(String equipment_type_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("equipment_type_id:" + equipment_type_id);

		NodesDAO nodesDAO = new NodesDAO(session);

		List<Nodes> list = nodesDAO.getList2(equipment_type_id);

		logger.trace(CommonDefinitions.END);
		return list;
	}

	public void addNodesRelation(List<PhysicalIfs> physicalIfsList, List<LagIfs> lagIfsList,
			NodesStartupNotification nodesStartupNotification) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("physicalIfsList:" + physicalIfsList.toString() + ", lagIfsList:" + lagIfsList.toString() +
				", nodesStartupNotification:" + nodesStartupNotification);

		LagIfsDAO lagIfsDAO = new LagIfsDAO(session);
		for (LagIfs lagIfs : lagIfsList) {
			lagIfsDAO.save(lagIfs);
		}

		PhysicalIfsDAO physicalIfsDAO = new PhysicalIfsDAO(session);
		for (PhysicalIfs physicalIfs : physicalIfsList) {
			physicalIfsDAO.update(physicalIfs);
		}

		if (nodesStartupNotification != null) {
			NodesStartupNotificationDAO nodesStartupNotificationDAO = new NodesStartupNotificationDAO(session);
			nodesStartupNotificationDAO.save(nodesStartupNotification);
		}

		logger.trace(CommonDefinitions.END);
	}

	public List<Nodes> getNodesList(int node_type) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("node_type:" + node_type);

		NodesDAO nodesDAO = new NodesDAO(session);

		List<Nodes> list = nodesDAO.getList(node_type);

		logger.trace(CommonDefinitions.END);

		return list;
	}

	public void deleteNodesRelation(int node_type, String node_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("node_type:" + node_type + ", node_id:" + node_id);

		InternalLinkIfsDAO internalLinkIfsDAO = new InternalLinkIfsDAO(session);
		internalLinkIfsDAO.delete(node_type, node_id, null, null, true);

		LagIfsDAO lagIfsDAO = new LagIfsDAO(session);
		lagIfsDAO.delete(node_type, node_id, null, true);

		PhysicalIfsDAO physicalIfsDAO = new PhysicalIfsDAO(session);
		physicalIfsDAO.delete(node_type, node_id, true);

		NodesStartupNotificationDAO nodesStartupNotificationDAO = new NodesStartupNotificationDAO(session);
		nodesStartupNotificationDAO.delete(node_type, node_id, true);

		NodesDAO nodesDAO = new NodesDAO(session);
		nodesDAO.delete(node_type, node_id);

		logger.trace(CommonDefinitions.END);
	}

	public void deleteUpdateOppositeNodes(List<Nodes> nodesList) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("nodesList:" + nodesList);

		int node_type = 0;
		String node_id = null;

		InternalLinkIfsDAO internalLinkIfsDAO = new InternalLinkIfsDAO(session);
		LagIfsDAO lagIfsDAO = new LagIfsDAO(session);
		PhysicalIfsDAO physicalIfsDAO = new PhysicalIfsDAO(session);

		for (Nodes nodes : nodesList) {
			node_type = nodes.getNode_type();
			node_id = nodes.getNode_id();

			for (LagIfs lagIfs : nodes.getLagIfsList()) {
				for (InternalLinkIfs internalLinkIfs : lagIfs.getInternalLinkIfsList()) {
					internalLinkIfsDAO.delete(0, null, null, internalLinkIfs.getInternal_link_if_id(), false);
				}
				lagIfsDAO.delete(node_type, node_id, lagIfs.getLag_if_id(), false);
			}
			for (PhysicalIfs physicalIfs : nodes.getPhysicalIfsList()) {
				physicalIfsDAO.update(physicalIfs);
			}
		}

		logger.trace(CommonDefinitions.END);
	}


	public List<PhysicalIfs> getPhysicalIfsList(int node_type, String node_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("node_type:" + node_type + ", node_id:" + node_id);

		PhysicalIfsDAO physicalIfsDAO = new PhysicalIfsDAO(session);

		List<PhysicalIfs> list = physicalIfsDAO.getList(node_type, node_id);

		logger.trace(CommonDefinitions.END);

		return list;
	}

	public PhysicalIfs searchPhysicalIfs(int node_type, String node_id, String physical_if_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("node_type:" + node_type + ", node_id:" + node_id + ", physical_if_id:" + physical_if_id);

		PhysicalIfsDAO physicalIfsDAO = new PhysicalIfsDAO(session);

		PhysicalIfs physicalIfs = physicalIfsDAO.search(node_type, node_id, physical_if_id);

		logger.trace(CommonDefinitions.END);

		return physicalIfs;
	}

	public void updatePhysicalIfs(PhysicalIfs physicalIfs) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("physicalIfs:" + physicalIfs);

		PhysicalIfsDAO physicalIfsDAO = new PhysicalIfsDAO(session);
		physicalIfsDAO.update(physicalIfs);

		logger.trace(CommonDefinitions.END);
	}

	public List<InternalLinkIfs> getInternalLinkIfsList(int node_type, String node_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("node_type:" + node_type + ", node_id:" + node_id);

		InternalLinkIfsDAO internalLinkIfsDAO = new InternalLinkIfsDAO(session);

		List<InternalLinkIfs> list = internalLinkIfsDAO.getList(node_type, node_id);

		logger.trace(CommonDefinitions.END);

		return list;
	}

	public InternalLinkIfs searchInternalLinkIfs(String internal_link_if_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("internal_link_if_id:" + internal_link_if_id.toString());

		InternalLinkIfsDAO internalLinkIfsDAO = new InternalLinkIfsDAO(session);

		InternalLinkIfs internalLinkIfs = internalLinkIfsDAO.search(internal_link_if_id);

		logger.trace(CommonDefinitions.END);

		return internalLinkIfs;
	}

	public void addLagIfs(LagIfs lagIfs) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("lagIfs:" + lagIfs.toString());

		LagIfsDAO lagIfsDAO = new LagIfsDAO(session);
		lagIfsDAO.save(lagIfs);

		logger.trace(CommonDefinitions.END);
	}

	public List<LagIfs> getLagIfsList(int node_type, String node_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("node_type:" + node_type + ", node_id:" + node_id);

		LagIfsDAO lagIfsDAO = new LagIfsDAO(session);

		List<LagIfs> list = lagIfsDAO.getList(node_type, node_id);

		logger.trace(CommonDefinitions.END);

		return list;
	}

	public LagIfs searchLagIfs(int node_type, String node_id, String lag_if_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("node_type:" + node_type + ", node_id:" + node_id + ", lag_if_id:" + lag_if_id);

		LagIfsDAO lagIfsDAO = new LagIfsDAO(session);

		LagIfs lagIfs = lagIfsDAO.search(node_type, node_id, lag_if_id);

		logger.trace(CommonDefinitions.END);

		return lagIfs;
	}

	public void deleteLagIfs(int node_type, String node_id, String lag_if_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("node_type:" + node_type + ", node_id:" + node_id + ", lag_if_id:" + lag_if_id);

		InternalLinkIfsDAO internalLinkDAO = new InternalLinkIfsDAO(session);
		internalLinkDAO.delete(node_type, node_id, lag_if_id, null, true);

		LagIfsDAO lagIfsDAO = new LagIfsDAO(session);
		lagIfsDAO.delete(node_type, node_id, lag_if_id, false);

		logger.trace(CommonDefinitions.END);
	}

	public void addL2Cps(List<L2Cps> l2CpsList) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("l2CpsList:" + l2CpsList.toString());

		L2CpsDAO l2CpsDAO = new L2CpsDAO(session);
		for (L2Cps l2Cps : l2CpsList) {
			l2CpsDAO.save(l2Cps);
		}
		logger.trace(CommonDefinitions.END);
	}

	public L2Cps searchL2Cps(String slice_id, String cp_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("slice_id:" + slice_id + ", cp_id:" + cp_id);

		L2CpsDAO l2CpsDAO = new L2CpsDAO(session);

		L2Cps l2Cps = l2CpsDAO.search(slice_id, cp_id);

		logger.trace(CommonDefinitions.END);

		return l2Cps;
	}

	public void deleteL2Cps(List<L2Cps> l2CpsList) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("l2CpsList:" + l2CpsList.toString());

		L2CpsDAO l2CpsDAO = new L2CpsDAO(session);
		for (L2Cps l2Cps : l2CpsList) {
			l2CpsDAO.delete(l2Cps.getSlice_id(), l2Cps.getCp_id());
		}
		logger.trace(CommonDefinitions.END);
	}

	public void addL3Cps(List<L3Cps> l3CpsList) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("l3CpsList:" + l3CpsList.toString());

		L3CpsDAO l3CpsDAO = new L3CpsDAO(session);
		for (L3Cps l3Cps : l3CpsList) {
			l3CpsDAO.save(l3Cps);
		}
		logger.trace(CommonDefinitions.END);
	}

	public L3Cps searchL3Cps(String slice_id, String cp_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("slice_id:" + slice_id + ", cp_id:" + cp_id);

		L3CpsDAO l3CpsDAO = new L3CpsDAO(session);

		L3Cps l3Cps = l3CpsDAO.search(slice_id, cp_id);

		logger.trace(CommonDefinitions.END);

		return l3Cps;
	}

	public void deleteL3Cps(List<L3Cps> l3CpsList) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("l3CpsList:" + l3CpsList.toString());

		L3CpsDAO l3CpsDAO = new L3CpsDAO(session);
		for (L3Cps l3Cps : l3CpsList) {
			l3CpsDAO.delete(l3Cps.getSlice_id(), l3Cps.getCp_id());
		}
		logger.trace(CommonDefinitions.END);
	}

	public CpsList getCpsList(int node_type, String node_id, String if_name) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("node_type:" + node_type + ", node_id:" + node_id + ", if_name:" + if_name);

		CpsList cpsList = new CpsList();

		L2CpsDAO l2CpsDAO = new L2CpsDAO(session);
		cpsList.setL2CpsList(l2CpsDAO.getList(node_type, node_id, if_name));

		L3CpsDAO l3CpsDAO = new L3CpsDAO(session);
		cpsList.setL3CpsList(l3CpsDAO.getList(node_type, node_id, if_name));

		logger.trace(CommonDefinitions.END);

		return cpsList;
	}


	public void addSystemStatus(SystemStatus systemStatus) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("systemStatus:" + systemStatus.toString());

		SystemStatusDAO systemStatusDAO = new SystemStatusDAO(session);
		systemStatusDAO.save(systemStatus);

		logger.trace(CommonDefinitions.END);
	}

	public SystemStatus getSystemStatus() throws DBAccessException {
		logger.trace(CommonDefinitions.START);

		SystemStatusDAO systemStatusDAO = new SystemStatusDAO(session);

		SystemStatus systemStatus = systemStatusDAO.search();

		logger.trace(CommonDefinitions.END);

		return systemStatus;
	}

	public void updateSystemStatus(int service_status, int blockade_status) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("service_status:" + service_status + ", blockade_status:" + blockade_status);

		SystemStatusDAO systemStatusDAO = new SystemStatusDAO(session);
		systemStatusDAO.update(service_status, blockade_status);

		logger.trace(CommonDefinitions.END);
	}


	public List<NodesStartupNotification> getNodesStartupNotificationList() throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		List<NodesStartupNotification> list = new ArrayList<NodesStartupNotification>();

		NodesStartupNotificationDAO nodesStartupNotificationDAO = new NodesStartupNotificationDAO(session);
		list = nodesStartupNotificationDAO.getList();

		logger.trace(CommonDefinitions.END);
		return list;

	}

	public void updateNodesStartupNotification(NodesStartupNotification nodesStartupNotification)
			throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("nodesStartupNotification:" + nodesStartupNotification.toString());

		NodesStartupNotificationDAO nodesStartupNotificationDAO = new NodesStartupNotificationDAO(session);
		nodesStartupNotificationDAO.update(nodesStartupNotification);

		logger.trace(CommonDefinitions.END);
	}

	public void deleteNodesStartupNotification(int node_type, String node_id) throws DBAccessException {
		logger.trace(CommonDefinitions.START);
		logger.debug("node_type:" + node_type + ", node_id:" + node_id);

		NodesStartupNotificationDAO nodesStartupNotificationDAO = new NodesStartupNotificationDAO(session);

		if (node_type != -1 && node_id != null) {
			nodesStartupNotificationDAO.delete(node_type, node_id, false);
		} else {
			nodesStartupNotificationDAO.delete();
		}

		logger.trace(CommonDefinitions.END);
	}
}
