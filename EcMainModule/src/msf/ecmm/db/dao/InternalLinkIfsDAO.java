
package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.InternalLinkIfs;

import org.hibernate.Query;
import org.hibernate.Session;

public class InternalLinkIfsDAO extends BaseDAO {

	public InternalLinkIfsDAO(Session session) {
		this.session = session;
	}

	@SuppressWarnings("unchecked")
	public InternalLinkIfs search(String internal_link_if_id) throws DBAccessException {

		InternalLinkIfs internalLinkIfs = null;
		try {
			Query query = session.getNamedQuery("selectInternalLinkIfs");
			query.setString("key1", internal_link_if_id);
			List<InternalLinkIfs> internalLinkIfsList = query.list();
			if (!internalLinkIfsList.isEmpty() && internalLinkIfsList.size() == 1) {
				internalLinkIfs = internalLinkIfsList.get(0);
				internalLinkIfs.setNode_type(internalLinkIfs.getNodes().getNode_type());
				internalLinkIfs.setNode_id(internalLinkIfs.getNodes().getNode_id());
				internalLinkIfs.setLag_if_id(internalLinkIfs.getLagIfs().getLag_if_id());
			}
		} catch (Throwable e) {
			logger.debug("internal_link_ifs select failed.", e);
			this.errorMessage(SERCH_FAILURE, INTERNAL_LINK_IFS, e);
		}
		return internalLinkIfs;
	}

	@SuppressWarnings("unchecked")
	public List<InternalLinkIfs> getList(int node_type, String node_id) throws DBAccessException {
		List<InternalLinkIfs> internalLinkIfsList = new ArrayList<InternalLinkIfs>();
		try {
			Query query = session.getNamedQuery("selectInternalLinkIfsByNode");
			query.setInteger("key1", node_type);
			query.setString("key2", node_id);
			internalLinkIfsList = query.list();
			for (InternalLinkIfs internalLinkIfs : internalLinkIfsList) {
				internalLinkIfs.setNode_id(internalLinkIfs.getNodes().getNode_id());
				internalLinkIfs.setNode_type(internalLinkIfs.getNodes().getNode_type());
				internalLinkIfs.setLag_if_id(internalLinkIfs.getLagIfs().getLag_if_id());
			}
		} catch (Throwable e) {
			logger.debug("internal_link_ifs select failed.", e);
			this.errorMessage(SERCH_FAILURE, INTERNAL_LINK_IFS, e);
		}
		return internalLinkIfsList;
	}

	public void delete(int node_type, String node_id, String lag_if_id, String internal_link_if_id, boolean check)
			throws DBAccessException {
		try {
			Query query = null;
			if (internal_link_if_id != null) {
				InternalLinkIfs internalLinkIfs = this.search(internal_link_if_id);
				if (internalLinkIfs == null) {
					this.errorMessage(NO_DELETE_TARGET, INTERNAL_LINK_IFS, null);
				}
				query = session.getNamedQuery("deleteInternalLinkIfs");
				query.setString("key1", internal_link_if_id);
			} else {
				List<InternalLinkIfs> internalLinkIfsList = this.getList(node_type, node_id);
				if (internalLinkIfsList.isEmpty()) {
						return;
					}
					this.errorMessage(NO_DELETE_TARGET, INTERNAL_LINK_IFS, null);
				} else {
					if (lag_if_id != null) {
						query = session.getNamedQuery("deleteInternalLinkIfsByLagIF");
						query.setInteger("key1", node_type);
						query.setString("key2", node_id);
						query.setString("key3", lag_if_id);
					} else {
						query = session.getNamedQuery("deleteInternalLinkIfsByNode");
						query.setInteger("key1", node_type);
						query.setString("key2", node_id);
					}
				}
			}
			query.executeUpdate();
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("internal_link_ifs delete failed.", e);
			this.errorMessage(DELETE_FAILURE, INTERNAL_LINK_IFS, e);
		}
	}
}
