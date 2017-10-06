
package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.L3Cps;

import org.hibernate.Query;
import org.hibernate.Session;

public class L3CpsDAO extends BaseDAO {

	public L3CpsDAO(Session session) {
		this.session = session;
	}

	public void save(L3Cps l3Cps) throws DBAccessException {
		try {
			L3Cps regl3Cps = this.search(l3Cps.getSlice_id(), l3Cps.getCp_id());
			if (regl3Cps != null) {
				this.errorMessage(DOUBLE_REGISTRATION, L3_CPS, null);
			} else {
				session.save(l3Cps);
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("l3_cps insert failed.", e);
			this.errorMessage(INSERT_FAILURE, L3_CPS, e);
		}
	}

	@SuppressWarnings("unchecked")
	public L3Cps search(String slice_id, String cp_id) throws DBAccessException {
		L3Cps l3Cps = null;
		try {
			Query query = session.getNamedQuery("selectL3Cps");
			query.setString("key1", slice_id);
			query.setString("key2", cp_id);
			List<L3Cps> l3CpsList = query.list();
			if (!l3CpsList.isEmpty() && l3CpsList.size() == 1) {
				l3Cps = l3CpsList.get(0);
			}
		} catch (Throwable e) {
			logger.debug("l3_cps select failed.", e);
			this.errorMessage(SERCH_FAILURE, L3_CPS, e);
		}
		return l3Cps;
	}

	@SuppressWarnings("unchecked")
	public List<L3Cps> getList(int node_type, String node_id, String if_name) throws DBAccessException {
		List<L3Cps> l3CpsList = new ArrayList<L3Cps>();
		try {
			Query query = null;
			if (if_name != null) {
				query = session.getNamedQuery("selectL3CpsByNodeAndIf");
				query.setString("key3", if_name);
			} else {
				query = session.getNamedQuery("selectL3CpsByNode");
			}
			query.setInteger("key1", node_type);
			query.setString("key2", node_id);
			l3CpsList = query.list();
			for (L3Cps l3Cps : l3CpsList) {
				l3Cps.setNode_id(l3Cps.getNodes().getNode_id());
				l3Cps.setNode_type(l3Cps.getNodes().getNode_type());
			}
		} catch (Throwable e) {
			logger.debug("l3_cps select failed.", e);
			this.errorMessage(SERCH_FAILURE, L3_CPS, e);
		}
		return l3CpsList;
	}

	public void delete(String slice_id, String cp_id) throws DBAccessException {
		try {
			L3Cps l3Cps = this.search(slice_id, cp_id);
			if (l3Cps == null) {
				this.errorMessage(NO_DELETE_TARGET, L3_CPS, null);
			} else {
				Query query = session.getNamedQuery("deleteL3Cps");
				query.setString("key1", slice_id);
				query.setString("key2", cp_id);
				query.executeUpdate();
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("l3_cps delete failed.", e);
			this.errorMessage(DELETE_FAILURE, L3_CPS, e);
		}
	}

}
