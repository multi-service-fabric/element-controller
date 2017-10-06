
package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.ArrayList;
import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.PhysicalIfs;

import org.hibernate.Query;
import org.hibernate.Session;

public class PhysicalIfsDAO extends BaseDAO {

	public PhysicalIfsDAO(Session session) {
		this.session = session;
	}

	public void save(PhysicalIfs physicalIfs) throws DBAccessException {
		try {
			PhysicalIfs regPhysicalIfs = this.search(physicalIfs.getNode_type(), physicalIfs.getNode_id(),
					physicalIfs.getPhysical_if_id());
			if (regPhysicalIfs != null) {
				this.errorMessage(DOUBLE_REGISTRATION, PHYSICAL_IFS, null);
			} else {
				session.save(physicalIfs);
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("physical_ifs insert failed.", e);
			this.errorMessage(INSERT_FAILURE, PHYSICAL_IFS, e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<PhysicalIfs> getList(int node_type, String node_id) throws DBAccessException {
		List<PhysicalIfs> physicalIfsList = new ArrayList<PhysicalIfs>();
		try {
			Query query = session.getNamedQuery("selectPhysicalIfsByNode");
			query.setInteger("key1", node_type);
			query.setString("key2", node_id);
			physicalIfsList = query.list();
			for (PhysicalIfs physicalIfs : physicalIfsList) {
				physicalIfs.setNode_id(physicalIfs.getNodes().getNode_id());
				physicalIfs.setNode_type(physicalIfs.getNodes().getNode_type());
			}
		} catch (Throwable e) {
			logger.debug("physical_ifs select failed.", e);
			this.errorMessage(SERCH_FAILURE, PHYSICAL_IFS, e);
		}
		return physicalIfsList;
	}

	@SuppressWarnings("unchecked")
	public PhysicalIfs search(int node_type, String node_id, String physical_if_id) throws DBAccessException {
		PhysicalIfs physicalIfs = null;
		try {
			Query query = session.getNamedQuery("selectPhysicalIfs");
			query.setInteger("key1", node_type);
			query.setString("key2", node_id);
			query.setString("key3", physical_if_id);
			List<PhysicalIfs> physicalIfsList = query.list();
			if (!physicalIfsList.isEmpty() && physicalIfsList.size() == 1) {
				physicalIfs = physicalIfsList.get(0);
				physicalIfs.setNode_type(physicalIfs.getNodes().getNode_type());
				physicalIfs.setNode_id(physicalIfs.getNodes().getNode_id());
			}
		} catch (Throwable e) {
			logger.debug("physical_ifs select failed.", e);
			this.errorMessage(SERCH_FAILURE, PHYSICAL_IFS, e);
		}
		return physicalIfs;
	}

	public void update(PhysicalIfs physicalIfs) throws DBAccessException {
		try {
			PhysicalIfs regPhysicalIfs = this.search(physicalIfs.getNode_type(), physicalIfs.getNode_id(),
					physicalIfs.getPhysical_if_id());
			if (regPhysicalIfs == null) {
				this.errorMessage(NO_UPDATE_TARGET, PHYSICAL_IFS, null);
			} else {
				Query query = session.getNamedQuery("updatePhysicalIfs");
				query.setInteger("key1", physicalIfs.getNode_type());
				query.setString("key2", physicalIfs.getNode_id());
				query.setString("key3", physicalIfs.getPhysical_if_id());
				query.setString("key4", physicalIfs.getIf_name());

				query.executeUpdate();
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("physical_ifs update failed.", e);
			this.errorMessage(6, PHYSICAL_IFS, e);
		}
	}

	public void delete(int node_type, String node_id, boolean check) throws DBAccessException {
		try {
			List<PhysicalIfs> physicalIfsList = this.getList(node_type, node_id);
			if (physicalIfsList.isEmpty()) {
					return;
				}
				logger.debug("physical_ifs delete failed.");
				this.errorMessage(NO_DELETE_TARGET, PHYSICAL_IFS, null);
			} else {
				Query query = session.getNamedQuery("deletePhysicalIfsByNode");
				query.setInteger("key1", node_type);
				query.setString("key2", node_id);
				query.executeUpdate();
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("physical_ifs delete failed.", e);
			this.errorMessage(DELETE_FAILURE, PHYSICAL_IFS, e);
		}
	}
}
