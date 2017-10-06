
package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.NodesStartupNotification;

import org.hibernate.Query;
import org.hibernate.Session;

public class NodesStartupNotificationDAO extends BaseDAO {

	public NodesStartupNotificationDAO(Session session) {
		this.session = session;
	}

	public void save(NodesStartupNotification nodesStartupNotification) throws DBAccessException {
		try {
			NodesStartupNotification regNodesStartupNotification = this.search(nodesStartupNotification.getNode_type(),
					nodesStartupNotification.getNode_id());
			if (regNodesStartupNotification != null) {
				this.errorMessage(DOUBLE_REGISTRATION, NODES_STARTUP_NOTICE, null);
			} else {
				session.save(nodesStartupNotification);
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("nodes_startup_notice insert failed.", e);
			this.errorMessage(INSERT_FAILURE, NODES_STARTUP_NOTICE, e);
		}
	}

	@SuppressWarnings("unchecked")
	public NodesStartupNotification search(int node_type, String node_id) throws DBAccessException {
		NodesStartupNotification nodesStartupNotification = null;
		try {
			Query query = session.getNamedQuery("selectNodesStartupNotification");
			query.setInteger("key1", node_type);
			query.setString("key2", node_id);

			List<NodesStartupNotification> nodesStartupNotificationList = query.list();
			if (!nodesStartupNotificationList.isEmpty() && nodesStartupNotificationList.size() == 1) {
				nodesStartupNotification = nodesStartupNotificationList.get(0);
				nodesStartupNotification.setNode_type(nodesStartupNotification.getNodes().getNode_type());
				nodesStartupNotification.setNode_id(nodesStartupNotification.getNodes().getNode_id());
			}

		} catch (Throwable e) {
			logger.debug("nodes_startup_notice select failed.", e);
			this.errorMessage(SERCH_FAILURE, NODES_STARTUP_NOTICE, e);
		}
		return nodesStartupNotification;
	}

	@SuppressWarnings("unchecked")
	public List<NodesStartupNotification> getList() throws DBAccessException {
		List<NodesStartupNotification> nodesStartupNotificationList = new ArrayList<NodesStartupNotification>();
		try {
			Query query = session.getNamedQuery("selectNodesStartupNotificationAll");
			nodesStartupNotificationList = query.list();
			for (NodesStartupNotification nodesStartupNotification : nodesStartupNotificationList) {
				nodesStartupNotification.setNode_id(nodesStartupNotification.getNodes().getNode_id());
				nodesStartupNotification.setNode_type(nodesStartupNotification.getNodes().getNode_type());
			}

		} catch (Throwable e) {
			logger.debug("nodes_startup_notice select failed.", e);
			this.errorMessage(SERCH_FAILURE, NODES_STARTUP_NOTICE, e);
		}
		return nodesStartupNotificationList;
	}

	public void update(NodesStartupNotification nodesStartupNotification) throws DBAccessException {
		try {
			NodesStartupNotification regNodesStartupNotification = this.search(nodesStartupNotification.getNode_type(),
					nodesStartupNotification.getNode_id());
			if (regNodesStartupNotification == null) {
				this.errorMessage(NO_UPDATE_TARGET, NODES_STARTUP_NOTICE, null);
			} else {
				regNodesStartupNotification.setNotification_reception_status(nodesStartupNotification
						.getNotification_reception_status());

				session.update(regNodesStartupNotification);
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("nodes_startup_notice update failed.", e);
			this.errorMessage(UPDATE_FAILURE, NODES_STARTUP_NOTICE, e);
		}
	}

	public void delete(int node_type, String node_id, boolean check) throws DBAccessException {
		try {
			NodesStartupNotification nodesStartupNotification = this.search(node_type, node_id);
			Query query = null;
			if (nodesStartupNotification == null) {
					return;
				}
				this.errorMessage(NO_DELETE_TARGET, NODES_STARTUP_NOTICE, null);
			} else {
				query = session.getNamedQuery("deleteNodesStartupNotification");
				query.setInteger("key1", node_type);
				query.setString("key2", node_id);
				query.executeUpdate();
			}

		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("nodes_startup_notice delete failed.", e);
			this.errorMessage(DELETE_FAILURE, NODES_STARTUP_NOTICE, e);
		}
	}

	public void delete() throws DBAccessException {
		try {
			List<NodesStartupNotification> nodesStartupNotificationList = this.getList();
			if (nodesStartupNotificationList.size() == 0) {
				this.errorMessage(NO_DELETE_TARGET, NODES_STARTUP_NOTICE, null);
			} else {
				Query query = session.getNamedQuery("deleteNodesStartupNotificationAllList");
				query.executeUpdate();
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("nodes_startup_notice delete failed.", e);
			this.errorMessage(DELETE_FAILURE, NODES_STARTUP_NOTICE, e);
		}
	}
}
