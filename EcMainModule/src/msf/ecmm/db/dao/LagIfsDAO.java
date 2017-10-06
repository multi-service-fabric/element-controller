
package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.LagIfs;

import org.hibernate.Query;
import org.hibernate.Session;

public class LagIfsDAO extends BaseDAO {

	public LagIfsDAO(Session session) {
		this.session = session;
	}

	public void save(LagIfs lagIfs) throws DBAccessException {
		try {
			LagIfs regLagIfs = this.search(lagIfs.getNode_type(), lagIfs.getNode_id(), lagIfs.getLag_if_id());
			if (regLagIfs != null) {
				this.errorMessage(DOUBLE_REGISTRATION, LAG_IFS, null);
			} else {
				session.save(lagIfs);
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("lag_ifs insert failed.", e);
			this.errorMessage(INSERT_FAILURE, LAG_IFS, e);
		}
	}

	@SuppressWarnings("unchecked")
	public LagIfs search(int node_type, String node_id, String lag_if_id) throws DBAccessException {

		LagIfs lagIfs = null;
		try {
			Query query = session.getNamedQuery("selectLagIfs");
			query.setInteger("key1", node_type);
			query.setString("key2", node_id);
			query.setString("key3", lag_if_id);
			List<LagIfs> lagIfsList = query.list();
			if (!lagIfsList.isEmpty() && lagIfsList.size() == 1) {
				lagIfs = lagIfsList.get(0);
				lagIfs.setNode_type(lagIfs.getNodes().getNode_type());
				lagIfs.setNode_id(lagIfs.getNodes().getNode_id());
			}
		} catch (Throwable e) {
			logger.debug("lag_ifs select failed.", e);
			this.errorMessage(SERCH_FAILURE, LAG_IFS, e);
		}
		return lagIfs;
	}

	@SuppressWarnings("unchecked")
	public List<LagIfs> getList(int node_type, String node_id) throws DBAccessException {
		List<LagIfs> lagIfsList = new ArrayList<LagIfs>();
		try {
			Query query = session.getNamedQuery("selectLagIfsByNode");
			query.setInteger("key1", node_type);
			query.setString("key2", node_id);
			lagIfsList = query.list();
			for (LagIfs lagIfs : lagIfsList) {
				lagIfs.setNode_id(lagIfs.getNodes().getNode_id());
				lagIfs.setNode_type(lagIfs.getNodes().getNode_type());
			}
		} catch (Throwable e) {
			logger.debug("lag_ifs select failed.", e);
			this.errorMessage(SERCH_FAILURE, LAG_IFS, e);
		}
		return lagIfsList;
	}

	public void delete(int node_type, String node_id, String lag_if_id, boolean check) throws DBAccessException {
		try {
			Query query = null;
			if (lag_if_id != null) {
				LagIfs lagIfs = this.search(node_type, node_id, lag_if_id);
				if (lagIfs == null) {
					this.errorMessage(NO_DELETE_TARGET, LAG_IFS, null);
				query = session.getNamedQuery("deleteLagIfs");
				query.setString("key3", lag_if_id);
			} else {
				List<LagIfs> lagIfsList = this.getList(node_type, node_id);
				if (lagIfsList.isEmpty()) {
						return;
					}
					this.errorMessage(NO_DELETE_TARGET, LAG_IFS, null);
				}
				query = session.getNamedQuery("deleteLagIfsByNode");
			}
			query.setInteger("key1", node_type);
			query.setString("key2", node_id);
			query.executeUpdate();
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("lag_ifs delete failed.", e);
			this.errorMessage(DELETE_FAILURE, LAG_IFS, e);
		}
	}
}
