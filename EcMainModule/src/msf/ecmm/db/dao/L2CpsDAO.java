
package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.L2Cps;

import org.hibernate.Query;
import org.hibernate.Session;

public class L2CpsDAO extends BaseDAO {

	public L2CpsDAO(Session session) {
		this.session = session;
	}

	public void save(L2Cps l2Cps) throws DBAccessException {
		try {
			L2Cps regl2Cps = this.search(l2Cps.getSlice_id(), l2Cps.getCp_id());
			if (regl2Cps != null) {
				this.errorMessage(DOUBLE_REGISTRATION, L2_CPS, null);
			} else {
				session.save(l2Cps);
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("l2_cps insert failed.", e);
			this.errorMessage(INSERT_FAILURE, L2_CPS, e);
		}
	}

	@SuppressWarnings("unchecked")
	public L2Cps search(String slice_id, String cp_id) throws DBAccessException {

		L2Cps l2Cps = null;
		try {
			Query query = session.getNamedQuery("selectL2Cps");
			query.setString("key1", slice_id);
			query.setString("key2", cp_id);
			List<L2Cps> l2CpsList = query.list();
			if (!l2CpsList.isEmpty() && l2CpsList.size() == 1) {
				l2Cps = l2CpsList.get(0);
			}
		} catch (Throwable e) {
			logger.debug("l2_cps select failed.", e);
			this.errorMessage(SERCH_FAILURE, L2_CPS, e);
		}
		return l2Cps;
	}

	@SuppressWarnings("unchecked")
	public List<L2Cps> getList(int node_type, String node_id, String if_name) throws DBAccessException {
		List<L2Cps> l2CpsList = new ArrayList<L2Cps>();
		try {
			Query query = null;
			if (if_name != null) {
				query = session.getNamedQuery("selectL2CpsByNodeAndIf");
				query.setString("key3", if_name);
			} else {
				query = session.getNamedQuery("selectL2CpsByNode");
			}
			query.setInteger("key1", node_type);
			query.setString("key2", node_id);
			l2CpsList = query.list();
			for (L2Cps l2Cps : l2CpsList) {
				l2Cps.setNode_id(l2Cps.getNodes().getNode_id());
				l2Cps.setNode_type(l2Cps.getNodes().getNode_type());
			}
		} catch (Throwable e) {
			logger.debug("l2_cps select failed.", e);
			this.errorMessage(SERCH_FAILURE, L2_CPS, e);
		}
		return l2CpsList;
	}

	public void delete(String slice_id, String cp_id) throws DBAccessException {
		try {
			L2Cps l2Cps = this.search(slice_id, cp_id);
			if (l2Cps == null) {
				this.errorMessage(NO_DELETE_TARGET, L2_CPS, null);
			} else {
				Query query = session.getNamedQuery("deleteL2Cps");
				query.setString("key1", slice_id);
				query.setString("key2", cp_id);
				query.executeUpdate();
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("l2_cps delete failed.", e);
			this.errorMessage(DELETE_FAILURE, L2_CPS, e);
		}
	}
}
