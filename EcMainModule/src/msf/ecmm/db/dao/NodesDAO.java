
package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.Nodes;

import org.hibernate.Query;
import org.hibernate.Session;

public class NodesDAO extends BaseDAO {

	public NodesDAO(Session session) {
		this.session = session;
	}

	public void save(Nodes nodes) throws DBAccessException {
		try {
			Nodes regNodes = this.search(nodes.getNode_type(), nodes.getNode_id(), null);
			if (regNodes != null) {
				this.errorMessage(DOUBLE_REGISTRATION, NODES, null);
			} else {
				session.save(nodes);
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("nodes insert failed.", e);
			this.errorMessage(INSERT_FAILURE, NODES, e);
		}
	}

	@SuppressWarnings("unchecked")
	public Nodes search(int node_type, String node_id, String management_if_address) throws DBAccessException {
		Nodes nodes = null;
		try {
			Query query = null;
			if (management_if_address == null) {
				query = session.getNamedQuery("selectNodes");
				query.setInteger("key1", node_type);
				query.setString("key2", node_id);
			} else {
				query = session.getNamedQuery("selectNodesAndIfAddr");
				query.setString("key1", management_if_address);
			}
			List<Nodes> nodesList = query.list();
			if (!nodesList.isEmpty() && nodesList.size() == 1) {
				nodes = nodesList.get(0);
				nodes.setEquipment_type_id(nodes.getEquipments().getEquipment_type_id());
			}
		} catch (Throwable e) {
			logger.debug("nodes select failed.", e);
			this.errorMessage(SERCH_FAILURE, NODES, e);
		}
		return nodes;
	}

	@SuppressWarnings("unchecked")
	public List<Nodes> getList(int node_type) throws DBAccessException {
		List<Nodes> nodesList = new ArrayList<Nodes>();
		try {
			Query query = session.getNamedQuery("selectNodesOnlyType");
			query.setInteger("key1", node_type);
			nodesList = query.list();
			for (Nodes nodes : nodesList) {
				nodes.setEquipment_type_id(nodes.getEquipments().getEquipment_type_id());
			}
		} catch (Throwable e) {
			logger.debug("nodes select failed.", e);
			this.errorMessage(SERCH_FAILURE, NODES, e);
		}
		return nodesList;
	}

	@SuppressWarnings("unchecked")
	public List<Nodes> getList2(String equipment_type_id) throws DBAccessException {
		List<Nodes> nodesList = new ArrayList<Nodes>();
		try {
			Query query = session.getNamedQuery("selectNodesOnlyEquipmentTypeId");
			query.setString("key1", equipment_type_id);
			nodesList = query.list();
			for (Nodes nodes : nodesList) {
				nodes.setEquipment_type_id(nodes.getEquipments().getEquipment_type_id());
			}
		} catch (Throwable e) {
			logger.debug("nodes select failed.", e);
			this.errorMessage(SERCH_FAILURE, NODES, e);
		}
		return nodesList;
	}

	public void delete(int node_type, String node_id) throws DBAccessException {
		try {
			Nodes nodes = this.search(node_type, node_id, null);
			if (nodes == null) {
				this.errorMessage(NO_DELETE_TARGET, NODES, null);
			} else {
				if ((!nodes.getL2CpsList().isEmpty()) || (!nodes.getL3CpsList().isEmpty())) {
					this.errorMessage(RELATIONSHIP_UNCONFORMITY, NODES, null);
				} else {
					Query query = session.getNamedQuery("deleteNodes");
					query.setInteger("key1", node_type);
					query.setString("key2", node_id);
					query.executeUpdate();
				}
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("nodes delete failed.", e);
			this.errorMessage(DELETE_FAILURE, NODES, e);
		}
	}
}
