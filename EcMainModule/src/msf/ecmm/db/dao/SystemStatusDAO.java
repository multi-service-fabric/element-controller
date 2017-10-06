
package msf.ecmm.db.dao;

import static msf.ecmm.db.DBAccessException.*;

import java.util.List;

import msf.ecmm.db.DBAccessException;
import msf.ecmm.db.pojo.SystemStatus;

import org.hibernate.Query;
import org.hibernate.Session;

public class SystemStatusDAO extends BaseDAO {

	public SystemStatusDAO(Session session) {
		this.session = session;
	}

	public void save(SystemStatus systemStatus) throws DBAccessException {
		try {
			SystemStatus regSystemStatus = this.search();
			if (regSystemStatus != null) {
				this.errorMessage(DOUBLE_REGISTRATION, SYSTEM_STATUS, null);
			} else {
				session.save(systemStatus);
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("systemStatus insert failed.", e);
			this.errorMessage(INSERT_FAILURE, SYSTEM_STATUS, e);
		}
	}

	@SuppressWarnings({ "unchecked" })
	public SystemStatus search() throws DBAccessException {
		List<SystemStatus> list = null;
		try {
			list = session.createCriteria(SystemStatus.class).list();
			if (list.size() == 0) {
				return null;
			} else if (list.size() > 1) {
				this.errorMessage(SYSTEM_FAILURE, SYSTEM_STATUS, null);
			}
		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("system_status select failed.", e);
			this.errorMessage(SERCH_FAILURE, SYSTEM_STATUS, e);
		}
		return list.get(0);
	}

	public void update(int service_status, int blockade_status) throws DBAccessException {
		try {
			SystemStatus orgSystemStatus = this.search();

			Query query = null;
			if (service_status != -1 && blockade_status != -1) {
				query = session.getNamedQuery("updateSystemStatus");
				query.setInteger("key1", service_status);
				query.setInteger("key2", blockade_status);
				query.executeUpdate();

				logger.info("before[service_status:" + orgSystemStatus.getService_status() + ",blockade_status:"
						+ orgSystemStatus.getBlockade_status() + "]->"
						+ "after[service_status:" + service_status + ",blockade_status:" + blockade_status + "]");

			} else if (service_status != -1) {
				query = session.getNamedQuery("updateSystemStatusForService");
				query.setInteger("key1", service_status);
				query.executeUpdate();

				logger.info("before[service_status:" + orgSystemStatus.getService_status() + "]->"
						+ "after[service_status:" + service_status + "]");

			} else if (blockade_status != -1) {
				query = session.getNamedQuery("updateSystemStatusForBlock");
				query.setInteger("key1", blockade_status);
				query.executeUpdate();

				logger.info("before[blockade_status:" + orgSystemStatus.getBlockade_status() + "]->"
						+ "after[blockade_status:" + blockade_status + "]");
			} else {
				logger.debug("system_status update input parameter failed.");
				this.errorMessage(SYSTEM_FAILURE, SYSTEM_STATUS, null);
			}

		} catch (DBAccessException e) {
			throw e;
		} catch (Throwable e) {
			logger.debug("system_status update failed.", e);
			this.errorMessage(UPDATE_FAILURE, SYSTEM_STATUS, e);
		}
	}
}
